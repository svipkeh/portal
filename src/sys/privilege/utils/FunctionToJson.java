package sys.privilege.utils;


import com.jfinal.plugin.activerecord.Model;
import java.util.ArrayList;
import java.util.List;
import sys.privilege.model.Function;
import sys.privilege.model.Menu;

public class FunctionToJson
{
  private static final String Y = "\"";
  private static final String M = ":";
  private static final String D = ",";
  private List<Menu> menus = null;
  private List<Function> functions = null;
  
  public FunctionToJson(List<Menu> menus, List<Function> functions)
  {
    this.menus = menus;
    this.functions = functions;
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
        pareseOneMenu(sb, m);
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
  
  private ArrayList<Function> getFunctions(String menuId)
  {
    ArrayList<Function> fs = new ArrayList();
    for (Function f : this.functions) {
      if (f.get("menu_id").toString().equals(menuId)) {
        fs.add(f);
      }
    }
    for (int i = 0; i < fs.size(); i++) {
      for (int j = i + 1; j < fs.size(); j++)
      {
        Function f1 = (Function)fs.get(i);
        Function f2 = (Function)fs.get(j);
        int index1 = f1.getInt("index").intValue();
        int index2 = f2.getInt("index").intValue();
        if (index1 > index2)
        {
          fs.set(i, f2);
          fs.set(j, f1);
        }
      }
    }
    return fs;
  }
  
  private void pareseOneMenu(StringBuffer sb, Menu menu)
  {
    ArrayList<Menu> ch = getChildren(menu.get("id").toString());
    ArrayList<Function> functions = getFunctions(menu.get("id").toString());
    
    sb.append("{");
    sb.append(getOneAttr(menu, "id") + ",");
    if (functions.size() != 0) {
      sb.append(getOneAttr(menu, "name", "", new StringBuilder("(").append(functions.size()).append(")").toString()) + ",");
    } else {
      sb.append(getOneAttr(menu, "name") + ",");
    }
    sb.append("\"type\":\"menu\"");
    if (ch.size() > 0)
    {
      sb.append(",\"children\":[");
      for (int i = 0; i < ch.size(); i++)
      {
        Menu m = (Menu)ch.get(i);
        pareseOneMenu(sb, m);
        if (i != ch.size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
    }
    else if (functions.size() > 0)
    {
      sb.append(",\"children\":[");
      for (int i = 0; i < functions.size(); i++)
      {
        Function f = (Function)functions.get(i);
        pareseOneFunction(sb, f);
        if (i != functions.size() - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
    }
    sb.append("}");
  }
  
  private void pareseOneFunction(StringBuffer sb, Function function)
  {
    sb.append("{");
    sb.append(getOneAttr(function, "id") + ",");
    sb.append(getOneAttr(function, "name", "功能：", "") + ",");
    sb.append("\"type\":\"function\"");
    sb.append("}");
  }
  
  private String getOneAttr(Model m, String attr, String head, String tail)
  {
    return "\"" + attr + "\"" + ":" + "\"" + head + m.get(attr) + tail + "\"";
  }
  
  private String getOneAttr(Model m, String attr)
  {
    return "\"" + attr + "\"" + ":" + "\"" + m.get(attr) + "\"";
  }
}
