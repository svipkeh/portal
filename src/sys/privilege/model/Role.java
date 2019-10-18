package sys.privilege.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Role
  extends Model<Role>
{
  private static final long serialVersionUID = 1L;
  public static final Role dao = new Role();
  
  public void deleteRef(String roleId)
  {
    Db.update("delete from sys_role_priv where role_id=?", new Object[] { roleId });
  }
  
  public List<Role> getAllRole()
  {
    return find("select * from sys_role a where a.data_status<>?", new Object[] { DataStatus.DELETED.name() });
  }
  
  public List<Role> getAllEnabledRole()
  {
    return find("select * from sys_role a where a.data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
  
  public Role getByName(String name)
  {
    return (Role)findFirst("select * from sys_role where name=?", new Object[] { name });
  }
  
  public Page<Role> getAllRole(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String name = (String)queryMap.get("name");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from sys_role a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and a.name like ?");
      paramList.add("%" + name + "%");
    }
    Page<Role> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getAllRole(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
