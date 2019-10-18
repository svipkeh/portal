package sys.privilege.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class RoleValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("saveInvoke".equals(getActionMethod().getName())) {
      validateString("role.name", 1, 30, "validate_role_name", "必填，长度范围: 1-30");
    }
    if ("updateInvoke".equals(getActionMethod().getName())) {
      validateString("role.name", 1, 30, "validate_role_name", "必填，长度范围: 1-30");
    }
  }
}
