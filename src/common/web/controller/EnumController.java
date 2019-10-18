package common.web.controller;

import common.jfinal.controller.BaseController;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumController
  extends BaseController
{
  public void enumList()
  {
    List<Map<String, String>> list = new ArrayList();
    String enumString = getPara(0);
    try
    {
      Class clazz = Class.forName("common.enums." + enumString);
      Method nameM = clazz.getMethod("name", new Class[0]);
      Method noteM = clazz.getMethod("note", new Class[0]);
      Object[] objs = clazz.getEnumConstants();
      for (Object obj : objs)
      {
        String name = (String)nameM.invoke(obj, new Object[0]);
        if (!"UNKNOWN".equals(name))
        {
          Map<String, String> map = new LinkedHashMap();
          map.put("val", name);
          map.put("note", (String)noteM.invoke(obj, new Object[0]));
          list.add(map);
        }
      }
      renderJson(list);
    }
    catch (Exception e)
    {
      renderJson(list);
    }
  }
}
