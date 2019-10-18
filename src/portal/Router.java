package portal;

import com.jfinal.config.Routes;
import portal.controller.AccountController;
import portal.controller.AccountUserController;
import portal.controller.ResourceAppController;
import portal.controller.ResourceController;

public class Router
{
  public static void config(Routes me)
  {
    me.add("/portal/resource", ResourceController.class);
    me.add("/portal/resourceapp", ResourceAppController.class);
    
    me.add("/portal/account", AccountController.class);
    me.add("/portal/accountuser", AccountUserController.class);
  }
}
