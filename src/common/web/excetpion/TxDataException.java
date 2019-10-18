package common.web.excetpion;

import java.util.HashMap;
import java.util.Map;

public class TxDataException
  extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  private Map<String, String> errorMap;
  
  public Map<String, String> getErrorMap()
  {
    return this.errorMap;
  }
  
  public void setErrorMap(Map<String, String> errorMap)
  {
    this.errorMap = errorMap;
  }
  
  public TxDataException(String cause)
  {
    super(cause);
  }
  
  public TxDataException(String cause, Throwable t)
  {
    super(cause, t);
  }
  
  public TxDataException(String labelId, String cause)
  {
    super(cause);
    if (this.errorMap == null) {
      this.errorMap = new HashMap();
    }
    this.errorMap.put(labelId, cause);
  }
}
