package common.jfinal.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

public class BaseController
  extends Controller
{
  private static final Logger logger = Logger.getLogger(BaseController.class);
  public static final String ADMIN = "admin";
  public static final String SESSEION_USER_ID = "SESSEION_USER_ID";
  public static final String SESSEION_USER_NAME = "SESSEION_USER_NAME";
  public static final String SESSEION_USER_NAME_CN = "SESSEION_USER_NAME_CN";
  public static final String SESSEION_USER_MENU = "SESSEION_USER_MENU";
  public static final String SESSEION_USER_METHODS = "SESSEION_USER_METHODS";
  
  protected void clearSession()
  {
    removeSessionAttr("SESSEION_USER_ID");
    removeSessionAttr("SESSEION_USER_NAME");
    removeSessionAttr("SESSEION_USER_NAME_CN");
    removeSessionAttr("SESSEION_USER_MENU");
    removeSessionAttr("SESSEION_USER_METHODS");
  }
  
  protected void clearPagerInfo()
  {
    int keep = getParaToInt("keep", Integer.valueOf(0)).intValue();
    if (keep != 1)
    {
      removeSessionAttr("qm." + getUri() + "Invoke");
      removeSessionAttr("page.current." + getUri() + "Invoke");
      removeSessionAttr("page.size." + getUri() + "Invoke");
      removeSessionAttr("page.totalRow." + getUri() + "Invoke");
      removeSessionAttr("page.totalPage." + getUri() + "Invoke");
      logger.debug("清除session查询和翻页参数：" + getUri() + "Invoke");
    }
    else
    {
      setAttr("qm", getSessionAttr("qm." + getUri() + "Invoke"));
      setAttr("page.current", getSessionAttr("page.current." + getUri() + "Invoke"));
      setAttr("page.size", getSessionAttr("page.size." + getUri() + "Invoke"));
      setAttr("page.totalRow", getSessionAttr("page.totalRow." + getUri() + "Invoke"));
      setAttr("page.totalPage", getSessionAttr("page.totalPage." + getUri() + "Invoke"));
      logger.debug("注入request查询和翻页参数：" + getUri() + "Invoke");
    }
  }
  
  protected void savePagerInfo(Page page, Map<String, String> queryMap)
  {
    if (queryMap != null) {
      setSessionAttr("qm." + getUri(), queryMap);
    }
    if (page != null)
    {
      setSessionAttr("page.current." + getUri(), Integer.valueOf(page.getPageNumber()));
      setSessionAttr("page.size." + getUri(), Integer.valueOf(page.getPageSize()));
      setSessionAttr("page.totalRow." + getUri(), Integer.valueOf(page.getTotalRow()));
      setSessionAttr("page.totalPage." + getUri(), Integer.valueOf(page.getTotalPage()));
      logger.debug("保存session查询和翻页参数：" + getUri());
    }
  }
  
  protected Map<String, String> getQueryMap()
  {
    Map<String, String> map = new HashMap();
    String modelNameAndDot = "qm.";
    Map<String, String[]> parasMap = getRequest().getParameterMap();
    for (Map.Entry<String, String[]> e : parasMap.entrySet())
    {
      String paraKey = (String)e.getKey();
      if (paraKey.startsWith(modelNameAndDot))
      {
        String paraName = paraKey.substring(modelNameAndDot.length());
        String[] paraValue = (String[])e.getValue();
        
        String value = paraValue[0];
        map.put(paraName, value);
      }
    }
    return map;
  }
  
  protected int getPageCurrent()
  {
    int current = getParaToInt("current", Integer.valueOf(0)).intValue();
    if (current == 0)
    {
      String key = "page.current." + getUri();
      current = getSessionAttr(key) != null ? Integer.parseInt(getSessionAttr(key).toString()) : 0;
    }
    if (current == 0) {
      current = 1;
    }
    return current;
  }
  
  protected int getPageSize()
  {
    int size = getParaToInt("size", Integer.valueOf(0)).intValue();
    if (size == 0)
    {
      String key = "page.size." + getUri();
      size = getSessionAttr(key) != null ? Integer.parseInt(getSessionAttr(key).toString()) : 0;
    }
    if (size == 0) {
      size = 10;
    }
    return size;
  }
  
  protected int getPageCurrent(String actionUri)
  {
    String key = "page.current." + getActionUri() + actionUri;
    int current = getSessionAttr(key) != null ? Integer.parseInt(getSessionAttr(key).toString()) : 0;
    if (current == 0) {
      current = 1;
    }
    return current;
  }
  
  protected int getPageSize(String actionUri)
  {
    String key = "page.size." + getActionUri() + actionUri;
    int size = getSessionAttr(key) != null ? Integer.parseInt(getSessionAttr(key).toString()) : 0;
    if (size == 0) {
      size = 10;
    }
    return size;
  }
  
  private String getUri()
  {
    return getRequest().getRequestURI();
  }
  
  private String getActionUri()
  {
    String uri = getRequest().getRequestURI();
    int j = uri.lastIndexOf("/");
    return uri.substring(0, j + 1);
  }
}
