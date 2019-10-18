package common.jfinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import common.annotation.Info;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import sys.privilege.model.SysLog;

public class LogInterceptor implements Interceptor {
  @Override
  public void intercept(Invocation ai) {
    ai.invoke();
    Info r = ai.getMethod().getAnnotation(Info.class);
    if ((r != null) && (r.isNeesLog())) {
      String des = r.des();
      SysLog log = new SysLog();
      if (ai.getController().getSessionAttr("SESSEION_USER_ID") != null) {
        log.set("user_id", ai.getController().getSessionAttr("SESSEION_USER_ID").toString());
        log.set("source_ip", ai.getController().getRequest().getRemoteAddr());
        log.set("action", des);
        log.set("ex_time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        log.save();
      }
    }
  }
}
