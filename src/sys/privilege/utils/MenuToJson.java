package sys.privilege.utils;

import java.util.ArrayList;
import java.util.List;
import sys.privilege.model.Menu;

public class MenuToJson
{
  private static final String Y = "\"";
  private static final String M = ":";
  private static final String D = ",";
  private List<Menu> menus = null;
  
  public MenuToJson(List<Menu> menus)
  {
    this.menus = menus;
  }
  
  public String toJson()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if (this.menus != null)
    {
      ArrayList<Menu> tops = getChildren("0");
      for (int i = 0; i < tops.size(); i++)
      {
        Menu m = (Menu)tops.get(i);
        parseOne(sb, m);
        if (i != tops.size() - 1) {
          sb.append(",");
        }
      }
    }
    sb.append("]");
    return sb.toString();
  }
  
  private ArrayList<Menu> getChildren(String nowId)
  {
    ArrayList<Menu> ch = new ArrayList();
    for (Menu m : this.menus) {
      if (m.get("pid").toString().equals(nowId)) {
        ch.add(m);
      }
    }
    for (int i = 0; i < ch.size(); i++) {
      for (int j = i + 1; j < ch.size(); j++)
      {
        Menu m1 = (Menu)ch.get(i);
        Menu m2 = (Menu)ch.get(j);
        int index1 = m1.getInt("index").intValue();
        int index2 = m2.getInt("index").intValue();
        if (index1 > index2)
        {
          ch.set(i, m2);
          ch.set(j, m1);
        }
      }
    }
    return ch;
  }
  
  private void parseOne(StringBuffer sb, Menu menu)
  {
    sb.append("{");
    sb.append(getOneAttr(menu, "id") + ",");
    sb.append(getOneAttr(menu, "name"));
    ArrayList<Menu> ch = getChildren(menu.get("id").toString());
    if (ch.size() > 0)
    {
      sb.append(",\"children\":[");
      for (int i = 0; i < ch.size(); i++)
      {
        Menu m = (Menu)ch.get(i);
        parseOne(sb, m);
        if (i != ch.size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
    }
    sb.append("}");
  }
  
  private String getOneAttr(Menu menu, String attr)
  {
    return "\"" + attr + "\"" + ":" + "\"" + menu.get(attr) + "\"";
  }
}
