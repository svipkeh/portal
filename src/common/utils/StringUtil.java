package common.utils;

import java.util.List;

public class StringUtil
{
  public static boolean isBlank(String s)
  {
    if ((s == null) || ("".equals(s.trim()))) {
      return true;
    }
    return false;
  }
  
  public static String toArray(List list)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for (int i = 0; i < list.size(); i++)
    {
      Object o = list.get(i);
      if (i != 0) {
        sb.append(",");
      }
      if ((o instanceof Number))
      {
        sb.append(o);
      }
      else
      {
        sb.append("'");
        sb.append(o);
        sb.append("'");
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
