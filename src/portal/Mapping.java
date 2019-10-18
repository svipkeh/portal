package portal;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import portal.model.Account;
import portal.model.AccountUser;
import portal.model.Resource;
import portal.model.ResourceApp;
import portal.model.ResourceAppAttr;

public class Mapping
{
  public static void config(ActiveRecordPlugin arp)
  {
    arp.addMapping("base_resource", Resource.class);
    arp.addMapping("base_resource_app", ResourceApp.class);
    arp.addMapping("base_resource_app_attr", ResourceAppAttr.class);
    
    arp.addMapping("base_account", Account.class);
    arp.addMapping("base_account_user", AccountUser.class);
  }
}
