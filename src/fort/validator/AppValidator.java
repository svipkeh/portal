package fort.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import fort.utils.AppConverseTool;
import java.lang.reflect.Method;
import java.util.List;

public class AppValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("addInvoke".equals(getActionMethod().getName()))
    {
      validateString("app.name_cn", 1, 32, "validate_app_name_cn", "必填，长度范围: 1-32");
      
      validateString2("app.parameter", 1, 128, "validate_parameter", "长度范围: 1-128");
    }
    if ("editInvoke".equals(getActionMethod().getName()))
    {
      validateString("app.name_cn", 1, 32, "validate_app_name_cn", "必填，长度范围: 1-32");
      
      validateString2("app.parameter", 1, 128, "validate_parameter", "长度范围: 1-128");
    }
    if ("runInvoke".equals(getActionMethod().getName()))
    {
      List<String> ps = AppConverseTool.getParametes(c.getPara("app.parameter"));
      for (String p : ps) {
        validateString2(p, 1, 32, "validate_" + p, "必填");
      }
    }
  }
}
