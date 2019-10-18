package sys.privilege.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu
  extends Model<Menu>
{
  private static final long serialVersionUID = 1L;
  public static final Menu dao = new Menu();
  
  public void deleteRef(String menuId)
  {
    Db.update("delete from sys_method where func_id in (select id from sys_function where menu_id=?)", new Object[] { menuId });
    Db.update("delete from sys_function where menu_id=?", new Object[] { menuId });
  }
  
  public List<Menu> getAllMenu()
  {
    return find("select * from sys_menu a where a.data_status<>?", new Object[] { DataStatus.DELETED.name() });
  }
  
  public List<Menu> getAllEnabledMenu()
  {
    return find("select * from sys_menu a where a.data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
  
  public List<Menu> getMenusByUser(int userId)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(" select distinct d.*");
    sb.append(" from sys_user_role a, sys_role b, sys_role_priv c, sys_menu d");
    sb.append(" where a.user_id=? and a.role_id=b.id");
    sb.append(" and b.data_status=? and b.id=c.role_id");
    sb.append(" and c.res_type=? and c.res_id=d.id");
    sb.append(" and d.data_status=?");
    return find(sb.toString(), new Object[] { Integer.valueOf(userId), DataStatus.ENABLED.name(), "menu", DataStatus.ENABLED.name() });
  }
  
  public Page<Menu> getAllMenu(int pageCurrent, int pageSize, Map<String, String> qm)
  {
    String pid = (String)qm.get("pid");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from sys_menu a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(pid))
    {
      sqlExceptSelect.append(" and (a.id=? or a.pid = ?)");
      paramList.add(pid);
      paramList.add(pid);
    }
    Page<Menu> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getAllMenu(pageCurrent - 1, pageSize, qm);
    }
    return page;
  }
}
