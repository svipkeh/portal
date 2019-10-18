package sys.privilege.validator;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import common.jfinal.validator.BaseValidator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FunctionValidator
  extends BaseValidator
{
  protected void validate(Controller c)
  {
    if ("saveInvoke".equals(getActionMethod().getName()))
    {
      validateRequired("function.menu_id", "validate_function_menu_id", "必选");
      validateString("function.name", 1, 50, "validate_function_name", "必填，长度范围: 1-50");
      validateInteger("function.index", 0, 10000, "validate_function_index", "排序输入错误");
      
      String f = getController().getPara("f");
      Gson gson = new Gson();
      Map fMap = (Map)gson.fromJson(f, Map.class);
      List<List<String>> methods = (List)fMap.get("methods");
      if (methods.size() == 0) {
        addError("validate_method", "至少选择一个方法");
      }
    }
    if ("updateInvoke".equals(getActionMethod().getName()))
    {
      validateString("function.name", 1, 50, "validate_function_name", "必填，长度范围: 1-50");
      validateInteger("function.index", 0, 10000, "validate_function_index", "排序输入错误");
      
      String f = getController().getPara("f");
      Gson gson = new Gson();
      Map fMap = (Map)gson.fromJson(f, Map.class);
      List<List<String>> methods = (List)fMap.get("methods");
      if (methods.size() == 0) {
        addError("validate_method", "至少选择一个方法");
      }
    }
  }
}
