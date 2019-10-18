package sys.privilege.validator;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class LegalSourceValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("saveInvoke".equals(getActionMethod().getName()))
    {
      validateString("source.ip", 1, 30, "validate_source_ip", "必填");
      if (!StrKit.isBlank(c.getPara("source.des"))) {
        validateString("source.des", 1, 30, "validate_source_des", "超出范围，长度1-30");
      }
    }
  }
}
