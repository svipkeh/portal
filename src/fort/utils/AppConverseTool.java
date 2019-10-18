package fort.utils;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class AppConverseTool
{
  static ArrayList<String> notNeedInputParas = new ArrayList();
  
  static
  {
    notNeedInputParas.add("username");
    notNeedInputParas.add("ip");
    notNeedInputParas.add("accname");
    notNeedInputParas.add("password");
  }
  
  public static String getHtmlWithRes(String parameter)
  {
    List<String> ps = getParametes(parameter);
    StringBuffer sb = new StringBuffer();
    for (String p : ps) {
      if (!notNeedInputParas.contains(p))
      {
        String input = "";
        if (p.contains("password")) {
          input = "<input type=\"password\" name=\"" + p + "\" class=\"form-control\"/>";
        } else {
          input = "<input type=\"text\" name=\"" + p + "\" class=\"form-control\"/>";
        }
        sb.append("<div class=\"form-group\">");
        sb.append("<label class=\"control-label col-xs-2\"><font color=\"red\">*</font>" + p + "</label>");
        sb.append("<div class=\"col-xs-4\">" + input + "</div>");
        sb.append("<div class=\"col-xs-4\"><label id=\"validate_" + p + 
          "\" class=\"control-label validate_label\"></label></div>");
        sb.append("</div>");
      }
    }
    return sb.toString();
  }
  
  public static List<String> getNeedInputParams(String parameter)
  {
    List<String> needInputs = new ArrayList();
    List<String> ps = getParametes(parameter);
    for (String p : ps) {
      if (!notNeedInputParas.contains(p)) {
        needInputs.add(p);
      }
    }
    return needInputs;
  }
  
  public static String getCmdLine(String parameter, HttpServletRequest request)
  {
    List<String> ps = getParametes(parameter);
    for (String p : ps) {
      parameter = parameter.replace("{{" + p + "}}", request.getParameter(p));
    }
    return parameter;
  }
  
  public static String getHtml(String parameter)
  {
    List<String> ps = getParametes(parameter);
    StringBuffer sb = new StringBuffer();
    for (String p : ps)
    {
      String input = "";
      if (p.contains("password")) {
        input = "<input type=\"password\" name=\"" + p + "\" class=\"form-control\"/>";
      } else {
        input = "<input type=\"text\" name=\"" + p + "\" class=\"form-control\"/>";
      }
      sb.append("<div class=\"form-group\">");
      sb.append("<label class=\"control-label col-xs-2\"><font color=\"red\">*</font>" + p + "</label>");
      sb.append("<div class=\"col-xs-4\">" + input + "</div>");
      sb.append("<div class=\"col-xs-4\"><label id=\"validate_" + p + 
        "\" class=\"control-label validate_label\"></label></div>");
      sb.append("</div>");
    }
    return sb.toString();
  }
  
  public static List<String> getParametes(String parameter)
  {
    List<String> ps = new ArrayList();
    if (parameter != null)
    {
      String[] list1 = parameter.split("\\{\\{");
      for (String p : list1)
      {
        int ind = p.indexOf("}}");
        if (ind > 0) {
          ps.add(p.substring(0, ind));
        }
      }
    }
    return ps;
  }
}
