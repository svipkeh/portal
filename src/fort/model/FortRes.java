package fort.model;

import com.jfinal.plugin.activerecord.Model;
import common.enums.DataStatus;
import java.util.List;

public class FortRes
  extends Model<FortRes>
{
  private static final long serialVersionUID = 1L;
  public static final FortRes dao = new FortRes();
  
  public List<FortRes> listAll()
  {
    return find("select * from fort_res where data_status<>?", new Object[] { DataStatus.DELETED.name() });
  }
  
  public FortRes getByIp(String fortIp)
  {
    return (FortRes)findFirst("select * from fort_res where fort_ip=?", new Object[] { fortIp });
  }
  
  public FortRes getByIpWan(String fortIpWan)
  {
    return (FortRes)findFirst("select * from fort_res where fort_ip_wan=?", new Object[] { fortIpWan });
  }
  
  public List<FortRes> listAllEnabled()
  {
    return find("select * from fort_res where data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
}
