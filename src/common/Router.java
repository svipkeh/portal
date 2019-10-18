package common;

import com.jfinal.config.Routes;
import common.web.controller.EnumController;
import common.web.controller.RoutesController;

public class Router
{
  public static void config(Routes me)
  {
    me.add("/common/enum", EnumController.class);
    me.add("/common/routes", RoutesController.class);
  }
}
