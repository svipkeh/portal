package sys.index.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import common.enums.DataStatus;
import java.util.List;

public class BinDbHelper
{
  public static List<Record> listOwnRes(int userId)
  {
    return 
      Db.find(
      "select * from base_resource where data_status=? and id in (select resource_id from base_account_user where user_id=?)", new Object[] {
      DataStatus.ENABLED.name(), Integer.valueOf(userId) });
  }
  
  public static List<Record> listOwnAcc(int userId)
  {
    return 
      Db.find("select * from base_account where id in (select account_id from base_account_user where user_id=?)", new Object[] {
      Integer.valueOf(userId) });
  }
  
  public static List<Record> listResApp(Integer rsourceId)
  {
    return 
      Db.find(
      "select * from fort_app where data_status=? and id in (select app_id from base_resource_app where resource_id=? and data_status=?)", new Object[] {
      DataStatus.ENABLED.name(), rsourceId, DataStatus.ENABLED.name() });
  }
}
