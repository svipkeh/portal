package sys.privilege.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User
  extends Model<User>
{
  private static final long serialVersionUID = 1L;
  public static final User dao = new User();
  
  public void deleteRef(String userId)
  {
    Db.update("delete from sys_user_role where user_id=?", new Object[] { userId });
    Db.update("delete from sys_logs where user_id=?", new Object[] { userId });
    Db.update("delete from base_account_user where user_id=?", new Object[] { userId });
  }
  
  public User getByName(String username)
  {
    return (User)findFirst("select * from sys_user a where a.name=?", new Object[] { username });
  }
  
  public Page<User> getAllUser(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String name = (String)queryMap.get("name");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from sys_user a where a.name<>? and a.data_status<>?");
    paramList.add("admin");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and (a.name like ? or a.name_cn like ?)");
      paramList.add("%" + name + "%");
      paramList.add("%" + name + "%");
    }
    Page<User> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getAllUser(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
