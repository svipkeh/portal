package sys;

import com.jfinal.config.Routes;
import sys.index.controller.BinController;
import sys.privilege.controller.FunctionController;
import sys.privilege.controller.LegalSourceController;
import sys.privilege.controller.MenuController;
import sys.privilege.controller.RoleController;
import sys.privilege.controller.SysLogController;
import sys.privilege.controller.UserController;

public class Router
{
  public static void config(Routes me)
  {
    me.add("/bin", BinController.class);
    
    me.add("/sys/priv/user", UserController.class);
    me.add("/sys/priv/role", RoleController.class);
    me.add("/sys/priv/menu", MenuController.class);
    me.add("/sys/priv/function", FunctionController.class);
    me.add("/sys/priv/syslog", SysLogController.class);
    me.add("/sys/priv/legalsource", LegalSourceController.class);
  }
}
