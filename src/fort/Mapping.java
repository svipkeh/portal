package fort;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import fort.model.App;
import fort.model.FortAcc;
import fort.model.FortRes;
import fort.model.FortSessionRecord;

public class Mapping
{
  public static void config(ActiveRecordPlugin arp)
  {
    arp.addMapping("fort_app", App.class);
    arp.addMapping("fort_res", FortRes.class);
    arp.addMapping("fort_acc", FortAcc.class);
    arp.addMapping("fort_session_record", FortSessionRecord.class);
  }
}
