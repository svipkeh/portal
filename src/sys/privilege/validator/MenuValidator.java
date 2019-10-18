package sys.privilege.validator;

import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;

public class MenuValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("saveInvoke".equals(getActionMethod().getName()))
    {
      validateString("menu.name", 1, 30, "validate_menu_name", "必填，长度范围: 1-30");
      validateInteger("menu.index", 0, 10000, "validate_menu_index", "排序输入错误");
    }
    if ("updateInvoke".equals(getActionMethod().getName()))
    {
      validateString("menu.name", 1, 30, "validate_menu_name", "必填，长度范围: 1-30");
      validateInteger("menu.index", 0, 10000, "validate_menu_index", "排序输入错误");
    }
  }
}
