package sys.index.utils;

import java.util.ArrayList;
import java.util.List;
import sys.privilege.model.Menu;

public class MenuToHtml
{
  private String path;
  private List<Menu> menus;
  
  public MenuToHtml(String path, List<Menu> menus)
  {
    this.path = path;
    this.menus = menus;
  }
  
  public String getMenuHtml()
  {
    StringBuffer sb = new StringBuffer();
    ArrayList<Menu> tops = getChildren("0");
    for (int i = 0; i < tops.size(); i++)
    {
      Menu m = (Menu)tops.get(i);
      parseOne(sb, m);
    }
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
    ArrayList<Menu> ch = getChildren(menu.get("id").toString());
    if (ch.size() > 0)
    {
      sb.append("<li class=\"dropdown\">");
      sb.append("<a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">" + menu.getStr("name") + 
        " <span class=\"caret\"></span></a>");
      sb.append("<ul class=\"dropdown-menu\">");
      for (Menu m : ch) {
        parseOne(sb, m);
      }
      sb.append("</ul>");
      sb.append("</li>");
    }
    else
    {
      sb.append("<li><a href=\"" + this.path + menu.getStr("url") + "\">" + menu.getStr("name") + "</a></li>");
    }
  }
}
