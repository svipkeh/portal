package common.utils;

import com.jfinal.core.Action;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class JspToHtml
{
  public static File excute(Controller controller, String view, String toFilePath)
    throws Exception
  {
    String contextPath = controller.getRequest().getServletContext().getContextPath();
    int contextPathLength = (contextPath == null) || ("/".equals(contextPath)) ? 0 : contextPath.length();
    Action action = JFinal.me().getAction(controller.getRequest().getRequestURI().substring(contextPathLength), 
      new String[1]);
    if ((view != null) && (!view.startsWith("/")) && (action != null)) {
      view = action.getViewPath() + view;
    }
    File file = new File(toFilePath);
    if (file.exists()) {
      throw new Exception("the file: [" + toFilePath + "] is exists");
    }
    final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), 
      Charset.defaultCharset().name()));
    RequestDispatcher rd = controller.getRequest().getRequestDispatcher(view);
    HttpServletResponse rep = new HttpServletResponseWrapper(controller.getResponse())
    {
      @Override
      public PrintWriter getWriter()
        throws IOException
      {
        return pw;
      }
    };
    rd.include(controller.getRequest(), rep);
    pw.flush();
    pw.close();
    return file;
  }
}
