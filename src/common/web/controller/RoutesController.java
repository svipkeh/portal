package common.web.controller;

import common.jfinal.controller.BaseController;
import common.jfinal.routes.ControllerMethodInfo;
import common.jfinal.routes.RoutesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesController
  extends BaseController
{
  public void routesListInvoke()
  {
    List<Map<String, String>> list = new ArrayList();
    List<ControllerMethodInfo> cmiList = RoutesContext.instance().getCmiList();
    for (ControllerMethodInfo cmi : cmiList) {
      if (cmi.isControl())
      {
        Map<String, String> map = new HashMap();
        map.put("value", cmi.toString());
        map.put("note", cmi.toString());
        list.add(map);
      }
    }
    renderJson(list);
  }
}
