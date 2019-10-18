package sys;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import sys.privilege.model.Function;
import sys.privilege.model.LegalSource;
import sys.privilege.model.Menu;
import sys.privilege.model.Method;
import sys.privilege.model.Role;
import sys.privilege.model.RolePriv;
import sys.privilege.model.SysLog;
import sys.privilege.model.User;
import sys.privilege.model.UserRole;

public class Mapping
{
  public static void config(ActiveRecordPlugin arp)
  {
    arp.addMapping("sys_user", User.class);
    arp.addMapping("sys_role", Role.class);
    arp.addMapping("sys_menu", Menu.class);
    arp.addMapping("sys_function", Function.class);
    arp.addMapping("sys_method", Method.class);
    arp.addMapping("sys_logs", SysLog.class);
    arp.addMapping("sys_legal_source", LegalSource.class);
    arp.addMapping("sys_user_role", UserRole.class);
    arp.addMapping("sys_role_priv", RolePriv.class);
  }
}
