package sys.privilege.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Function
  extends Model<Function>
{
  private static final long serialVersionUID = 1L;
  public static final Function dao = new Function();
  
  public void deleteRef(String funcId)
  {
    Db.update("delete from sys_method where func_id=?", new Object[] { funcId });
  }
  
  public List<Function> getAllFunction()
  {
    return find("select * from sys_function a where a.data_status<>?", new Object[] { DataStatus.DELETED.name() });
  }
  
  public List<Function> getAllEnabledFunction()
  {
    return find("select * from sys_function a where a.data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
  
  public List<Function> getFunctionsByUser(int userId)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(" select distinct d.*");
    sb.append(" from sys_user_role a, sys_role b, sys_role_priv c, sys_function d");
    sb.append(" where a.user_id=? and a.role_id=b.id");
    sb.append(" and b.data_status=? and b.id=c.role_id");
    sb.append(" and c.res_type=? and c.res_id=d.id");
    sb.append(" and d.data_status=?");
    return find(sb.toString(), new Object[] { Integer.valueOf(userId), DataStatus.ENABLED.name(), "function", DataStatus.ENABLED.name() });
  }
  
  public List<Function> getFunctions(String menuId, String functionId)
  {
    if ((StringUtil.isBlank(menuId)) && (StringUtil.isBlank(functionId))) {
      return new LinkedList();
    }
    StringBuffer sql = new StringBuffer();
    List<Object> paramList = new ArrayList();
    sql.append("select * from sys_function a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(menuId))
    {
      sql.append(" and a.menu_id=?");
      paramList.add(menuId);
    }
    if (!StringUtil.isBlank(functionId))
    {
      sql.append(" and a.id=?");
      paramList.add(functionId);
    }
    return find(sql.toString(), paramList.toArray(new Object[0]));
  }
}
