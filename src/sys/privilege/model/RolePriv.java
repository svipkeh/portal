package sys.privilege.model;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;

public class RolePriv
  extends Model<RolePriv>
{
  private static final long serialVersionUID = 1L;
  public static final RolePriv dao = new RolePriv();
  
  public List<RolePriv> getAllPrivilege(String roleId)
  {
    return find("select * from sys_role_priv where role_id=?", new Object[] { roleId });
  }
}
