package portal.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class ResourceAppValidator
  extends BaseValidator
{
  @Override
  protected void validate(Controller c)
  {
    if ("addInvoke".equals(getActionMethod().getName())) {
      validateRequired("resapp.app_id", "validate_app_id", "必选 ");
    }
    "editInvoke".equals(getActionMethod().getName());
  }
}
