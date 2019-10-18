package portal.model;

import com.jfinal.plugin.activerecord.Model;
import common.enums.DataStatus;
import java.util.List;

public class ResourceApp
  extends Model<ResourceApp>
{
  private static final long serialVersionUID = 1L;
  public static final ResourceApp dao = new ResourceApp();
  
  public List<ResourceApp> listAll(String resid)
  {
    return find("select a.*, b.app_type, b.name_cn, b.link_name, b.link_name, b.parameter from base_resource_app a, fort_app b where a.app_id=b.id and a.resource_id=? and b.data_status=?", new Object[] {
    
      resid, DataStatus.ENABLED.name() });
  }
  
  public List<ResourceApp> listAllEnabled(String resid)
  {
    return find(
      "select a.*, b.app_type, b.name_cn, b.link_name, b.link_name, b.parameter from base_resource_app a, fort_app b where a.app_id=b.id and a.resource_id=? and b.data_status=? and a.data_status=?", new Object[] {
      
      resid, DataStatus.ENABLED.name(), DataStatus.ENABLED.name() });
  }
  
  public ResourceApp findByResAndApp(String resid, String appid)
  {
    return (ResourceApp)findFirst("select * from base_resource_app where resource_id=? and app_id=?", new Object[] { resid, appid });
  }
}
