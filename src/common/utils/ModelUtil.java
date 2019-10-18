package common.utils;

import com.google.gson.Gson;
import com.jfinal.plugin.activerecord.Model;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelUtil
{
  public static String toJson(List<? extends Model<?>> models, String... attrs)
  {
    List<Map<String, String>> list = new LinkedList();
    for (Model<?> m : models)
    {
      Map<String, String> map = new LinkedHashMap();
      for (String attr : attrs) {
        map.put(attr, m.get(attr).toString());
      }
      list.add(map);
    }
    return new Gson().toJson(list);
  }
  
  public static String toArray(List<? extends Model<?>> models, String idStr)
  {
    LinkedList<String> list = new LinkedList();
    for (Model<?> m : models) {
      list.add(m.get(idStr).toString());
    }
    return new Gson().toJson(list);
  }
}
