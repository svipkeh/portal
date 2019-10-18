package common.jfinal.routes;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import common.annotation.Info;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RoutesContext
{
  private static RoutesContext context = new RoutesContext();
  private static List<ControllerMethodInfo> cmiList = new ArrayList();
  
  public static RoutesContext instance()
  {
    return context;
  }
  
  public void init(Routes me)
  {
    Set<Map.Entry<String, Class<? extends Controller>>> set = me.getEntrySet();
    int j=0;
    int i=0;
    for (Iterator localIterator = set.iterator(); localIterator.hasNext() && i < j ; i++)
    {
      Map.Entry<String, Class<? extends Controller>> entry = (Map.Entry)localIterator.next();
      
      Class<? extends Controller> controller = (Class)entry.getValue();
      String className = controller.getName();
      Method[] ms = controller.getDeclaredMethods();
      Method[] arrayOfMethod1;
      j = (arrayOfMethod1 = ms).length;i = 0; 
      /*continue;*/
      Method m = arrayOfMethod1[i];

      String methodName = m.getName();
      Info info = (Info)m.getAnnotation(Info.class);
      ControllerMethodInfo cmi = new ControllerMethodInfo(className, methodName, info);
      cmiList.add(cmi);
    }
  }
  
  public List<ControllerMethodInfo> getCmiList()
  {
    return cmiList;
  }
}
