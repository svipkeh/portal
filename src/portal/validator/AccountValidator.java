package portal.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class AccountValidator
  extends BaseValidator
{
  @Override
  protected void validate(Controller c)
  {
    if ("addInvoke".equals(getActionMethod().getName()))
    {
      validateRequired("account.resource_id", "validate_resource_id", "必选");
      validateRequired("account.name", "validate_name", "必填");
      validateString("account.name", 1, 64, "validate_name", "必填，长度范围：1-64");
      validateRequired("account.password", "validate_password", "必填");
      validateString("account.password", 1, 64, "validate_password", "必填，长度范围：1-64");
    }
  }
}
