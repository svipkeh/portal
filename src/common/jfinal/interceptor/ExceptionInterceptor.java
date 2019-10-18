package common.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import common.enums.OpResult;
import common.web.excetpion.TxDataException;
import common.web.ws.Res;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;

public class ExceptionInterceptor
  implements Interceptor
{
  private static final Logger logger = Logger.getLogger(ExceptionInterceptor.class);
  
  public void intercept(Invocation ai)
  {
    try
    {
      ai.invoke();
    }
    catch (TxDataException e)
    {
      warnning("TxDataException", e);
      renderTxDataException(ai, e);
    }
    catch (Throwable e)
    {
      if ((e.getCause() != null) && ((e.getCause() instanceof TxDataException)))
      {
        warnning("Caused TxDataException", (TxDataException)e.getCause());
        renderTxDataException(ai, (TxDataException)e.getCause());
      }
      else
      {
        logger.warn("ExceptionInterceptor catch Exception: ", e);
        renderException(ai, "程序处理错误");
      }
    }
  }
  
  private void warnning(String type, TxDataException e)
  {
    logger.warn("ExceptionInterceptor catch " + type + ": " + e.getMessage() + ", errorMap: " + e.getErrorMap());
  }
  
  private void renderTxDataException(Invocation ai, TxDataException e)
  {
    String method = ai.getMethod().getName();
    if (method.endsWith("Invoke"))
    {
      Res res = new Res();
      res.setCode(OpResult.FAILED.name());
      res.setNote(e.getMessage());
      if (e.getErrorMap() != null) {
        res.setValue(e.getErrorMap());
      }
      ai.getController().renderJson(res);
    }
    else
    {
      ai.getController().setAttr("exception_msg", e.getMessage());
      ai.getController().render("/WEB-INF/jsp/common/error.jsp");
    }
  }
  
  private void renderException(Invocation ai, String msg)
  {
    String method = ai.getMethod().getName();
    if (method.endsWith("Invoke"))
    {
      Res res = new Res();
      res.setCode(OpResult.FAILED.name());
      res.setNote(msg);
      ai.getController().renderJson(res);
    }
    else
    {
      ai.getController().setAttr("exception_msg", msg);
      ai.getController().render("/WEB-INF/jsp/common/error.jsp");
    }
  }
}
