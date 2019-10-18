package common.jfinal.validator;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;
import common.web.excetpion.TxDataException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseValidator
  extends Validator
{
  public BaseValidator()
  {
    setShortCircuit(true);
  }
  
  protected void validateString2(String field, int minLen, int maxLen, String errorKey, String errorMessage)
  {
    String value = getController().getPara(field);
    if (StrKit.isBlank(value))
    {
      if (minLen > 0) {
        addError(errorKey, errorMessage);
      }
    }
    else if ((value.length() < minLen) || (value.length() > maxLen)) {
      addError(errorKey, errorMessage);
    }
  }
  
  protected void validateString(String field, int minLen, int maxLen, String errorKey, String errorMessage)
  {
    String value = getController().getPara(field);
    if (StrKit.isBlank(value))
    {
      if (minLen > 0) {
        addError(errorKey, errorMessage);
      }
    }
    else
    {
      if (!isLegalStr(value)) {
        addError(errorKey, "包含非法字符");
      }
      if ((value.length() < minLen) || (value.length() > maxLen)) {
        addError(errorKey, errorMessage);
      }
    }
  }
  
  private boolean isLegalStr(String value)
  {
    boolean legal = true;
    if (value.contains("<")) {
      legal = false;
    }
    if (value.contains(">")) {
      legal = false;
    }
    if (value.contains("&")) {
      legal = false;
    }
    if (value.contains("|")) {
      legal = false;
    }
    if (value.contains("\\")) {
      legal = false;
    }
    if (value.contains("'")) {
      legal = false;
    }
    return legal;
  }
  
  protected void handleError(Controller c)
  {
    Map<String, String> errorMap = getValidateMessageMap(c);
    if (errorMap.size() > 0)
    {
      TxDataException tx = new TxDataException("验证出错");
      tx.setErrorMap(errorMap);
      throw tx;
    }
  }
  
  protected Map<String, String> getValidateMessageMap(Controller c)
  {
    Map<String, String> errorMap = new HashMap();
    Enumeration<String> names = c.getAttrNames();
    if (names != null) {
      while (names.hasMoreElements())
      {
        String name = (String)names.nextElement();
        if ((name != null) && (name.startsWith("validate_"))) {
          if (c.getAttr(name) != null) {
            errorMap.put(name, c.getAttr(name).toString());
          }
        }
      }
    }
    return errorMap;
  }
}
