package fort.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import common.enums.DataStatus;
import java.util.List;

public class FortViewHelper
{
  public static long getFortResNum()
  {
    return Db.queryLong("select count(*) from fort_res where data_status<>?", new Object[] { DataStatus.DELETED.name() }).longValue();
  }
  
  public static long getFortAccNum()
  {
    return Db.queryLong("select count(*) from fort_acc a, fort_res b where a.fort_id=b.id and b.data_status<>?", new Object[] {
      DataStatus.DELETED.name() }).longValue();
  }
  
  public static List<Record> listFortRes()
  {
    return Db.find("select id, fort_ip, running_status, data_status from fort_res where data_status<>?", new Object[] {
      DataStatus.DELETED.name() });
  }
  
  public static List<Record> getFortAccAnalysis(String fortId)
  {
    return Db.find("select s_status, count(*) num from fort_acc where fort_id=? group by s_status", new Object[] { fortId });
  }
}
