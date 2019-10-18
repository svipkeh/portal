package sys.privilege.model;

import com.jfinal.plugin.activerecord.Model;
import common.enums.DataStatus;
import java.util.List;

public class Method
  extends Model<Method>
{
  private static final long serialVersionUID = 1L;
  public static final Method dao = new Method();
  
  public List<Method> getMethodsByFunc(String funcId)
  {
    return find("select * from sys_method a where a.func_id=?", new Object[] { funcId });
  }
  
  public List<Method> getMethodsByUser(String userId)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(" select distinct e.*");
    sb.append(" from sys_user_role a, sys_role b, sys_role_priv c, sys_function d, sys_method e, sys_menu f");
    sb.append(" where a.user_id=? and a.role_id=b.id");
    sb.append(" and b.data_status=? and b.id=c.role_id");
    sb.append(" and c.res_type=? and c.res_id=d.id");
    sb.append(" and d.data_status=? and d.id=e.func_id");
    sb.append(" and d.menu_id=f.id and f.data_status=?");
    return find(
      sb.toString(), 
      new Object[] { userId, DataStatus.ENABLED.name(), "function", DataStatus.ENABLED.name(), 
      DataStatus.ENABLED.name() });
  }
}
