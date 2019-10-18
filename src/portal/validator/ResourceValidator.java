package portal.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class ResourceValidator
  extends BaseValidator
{
  @Override
  protected void validate(Controller c)
  {
    if ("addInvoke".equals(getActionMethod().getName()))
    {
      validateRequired("resource.name_cn", "validate_name_cn", "必填");
      validateString("resource.name_cn", 1, 64, "validate_name_cn", "必填，长度范围：1-64");
      validateRequired("resource.name", "validate_name", "必填");
      validateString("resource.name", 1, 64, "validate_name", "必填，长度范围：1-64");
      validateRequired("resource.ip", "validate_ip", "必填");
      validateString("resource.ip", 1, 64, "validate_ip", "必填，IP格式不正常");
    }
    if ("editInvoke".equals(getActionMethod().getName()))
    {
      validateRequired("resource.name_cn", "validate_name_cn", "必填");
      validateString("resource.name_cn", 1, 64, "validate_name_cn", "必填，长度范围：1-64");
      validateRequired("resource.name", "validate_name", "必填");
      validateString("resource.name", 1, 64, "validate_name", "必填，长度范围：1-64");
      validateRequired("resource.ip", "validate_ip", "必填");
      validateString("resource.ip", 1, 64, "validate_ip", "必填，IP格式不正常");
    }
  }
}
