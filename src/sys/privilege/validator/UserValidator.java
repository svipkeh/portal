package sys.privilege.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class UserValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("saveInvoke".equals(getActionMethod().getName()))
    {
      validateString("user.name", 1, 50, "validate_user_name", "必填，长度范围: 1-50");
      validateString("user.name_cn", 1, 50, "validate_user_name_cn", "必填，长度范围: 1-50");
      validateRequired("role_ids", "validate_role_id", "必选");
    }
    if ("updateInvoke".equals(getActionMethod().getName()))
    {
      validateString("user.name", 1, 50, "validate_user_name", "必填，长度范围: 1-50");
      validateString("user.name_cn", 1, 50, "validate_user_name_cn", "必填，长度范围: 1-50");
      validateRequired("role_ids", "validate_role_id", "必选");
    }
  }
}
