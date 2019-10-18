package common.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import common.enums.OpResult;
import common.jfinal.routes.ControllerMethodInfo;
import common.jfinal.routes.RoutesContext;
import common.web.ws.Res;
import java.util.List;
import org.apache.log4j.Logger;

public class PrivilegeInterceptor implements Interceptor
{
  private static final Logger logger = Logger.getLogger(PrivilegeInterceptor.class);
  
  @Override
  public void intercept(Invocation ai) {
    Object u = ai.getController().getSessionAttr("SESSEION_USER_NAME");
    String controller = ai.getController().getClass().getName();
    String method = ai.getMethod().getName();
    if ((u == null) && (isControl(controller, method)))
    {
      if (method.endsWith("Invoke"))
      {
        Res res = new Res();
        res.setCode(OpResult.FAILED.name());
        res.setNote("请重新登录");
        ai.getController().renderJson(res);
      }
      else
      {
        ai.getController().redirect("/bin");
      }
      return;
    }
    if ((u != null) && ("admin".equals(u.toString())))
    {
      ai.invoke();
      return;
    }
    if (isControl(controller, method))
    {
      logger.debug("进入权限检查");
      boolean legal = false;
      Object o = ai.getController().getSessionAttr("SESSEION_USER_METHODS");
      if (o != null)
      {
        List<sys.privilege.model.Method> methods = (List)o;
        for (sys.privilege.model.Method m : methods) {
          if ((m.getStr("controller").equals(controller)) && (m.getStr("method").equals(method)))
          {
            legal = true;
            break;
          }
        }
      }
      if (legal)
      {
        logger.debug("合法访问");
        ai.invoke();
      }
      else
      {
        logger.debug("非法访问");
        if (method.endsWith("Invoke"))
        {
          Res res = new Res();
          res.setCode(OpResult.FAILED.name());
          res.setNote("权限不足，拒绝访问");
          ai.getController().renderJson(res);
        }
        else
        {
          ai.getController().render("/WEB-INF/jsp/common/forbidden.jsp");
        }
      }
    }
    else
    {
      ai.invoke();
    }
  }
  
  private boolean isControl(String controller, String method)
  {
    List<ControllerMethodInfo> cmiList = RoutesContext.instance().getCmiList();
    for (ControllerMethodInfo cmi : cmiList) {
      if ((cmi.getController().equals(controller)) && (cmi.getMethod().endsWith(method)) && (cmi.isControl())) {
        return true;
      }
    }
    return false;
  }
}
