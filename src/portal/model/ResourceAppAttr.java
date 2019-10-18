package portal.model;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public class ResourceAppAttr
  extends Model<ResourceAppAttr>
{
  private static final long serialVersionUID = 1L;
  public static final ResourceAppAttr dao = new ResourceAppAttr();
  
  public List<ResourceAppAttr> listByResourceAppId(String resAppId)
  {
    return find("select * from base_resource_app_attr where resource_app_id=?", new Object[] { resAppId });
  }
}
