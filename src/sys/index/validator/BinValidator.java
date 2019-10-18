package sys.index.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class BinValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("loginInvoke".equals(getActionMethod().getName()))
    {
      validateString("username", 1, 50, "validate_login", "请输入用户名");
      validateString("password", 1, 50, "validate_login", "请输入密码");
    }
    if ("weihuInvoke".equals(getActionMethod().getName()))
    {
      validateRequired("password_old", "validate_password_old", "请输入当前密码");
      validateString("user.password", 1, 50, "validate_user_password", "必填，长度范围: 1-50");
      validateString("password_repeat", 1, 50, "validate_password_repeat", "必填，长度范围: 1-50");
      validateEqualField("password_repeat", "user.password", "validate_password_repeat", "两次输入的密码不一致");
    }
  }
}
