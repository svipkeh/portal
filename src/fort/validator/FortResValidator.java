package fort.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class FortResValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("addInvoke".equals(getActionMethod().getName()))
    {
      validateString("res.fort_ip", 7, 16, "validate_fort_ip", "必填，请输入正确的IP");
      validateString("res.fort_ip_wan", 7, 16, "validate_fort_ip_wan", "必填，请输入正确的IP");
      validateInteger("res.mstsc_port", 0, 65535, "validate_mstsc_port", "必填，请输入正确的端口");
      validateInteger("res.servmgr_port", 0, 65535, "validate_servmgr_port", "必填，请输入正确的端口");
    }
    if ("editInvoke".equals(getActionMethod().getName()))
    {
      validateString("res.fort_ip", 7, 16, "validate_fort_ip", "必填，请输入正确的IP");
      validateString("res.fort_ip_wan", 7, 16, "validate_fort_ip_wan", "必填，请输入正确的IP");
      validateInteger("res.mstsc_port", 0, 65535, "validate_mstsc_port", "必填，请输入正确的端口");
      validateInteger("res.servmgr_port", 0, 65535, "validate_servmgr_port", "必填，请输入正确的端口");
    }
  }
}
