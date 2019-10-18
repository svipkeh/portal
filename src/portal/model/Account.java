package portal.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Account
  extends Model<Account>
{
  private static final long serialVersionUID = 1L;
  public static final Account dao = new Account();
  
  public Account getByName(String resid, String name)
  {
    return (Account)findFirst("select * from base_account a where a.resource_id=? and a.name=?", new Object[] { resid, name });
  }
  
  public Page<Account> getPage(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String resname = (String)queryMap.get("res_name");
    String res_name_cn = (String)queryMap.get("res_name_cn");
    String ip = (String)queryMap.get("ip");
    String name = (String)queryMap.get("name");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.id, a.name, b.name res_name, b.name_cn res_name_cn, b.ip, b.res_type");
    sqlExceptSelect.append("from base_account a, base_resource b where a.resource_id=b.id and b.data_status=?");
    paramList.add(DataStatus.ENABLED.name());
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and a.name like ?");
      paramList.add("%" + name + "%");
    }
    if (!StringUtil.isBlank(resname))
    {
      sqlExceptSelect.append(" and b.name like ?");
      paramList.add("%" + resname + "%");
    }
    if (!StringUtil.isBlank(res_name_cn))
    {
      sqlExceptSelect.append(" and b.name_cn like ?");
      paramList.add("%" + res_name_cn + "%");
    }
    if (!StringUtil.isBlank(ip))
    {
      sqlExceptSelect.append(" and b.ip like ?");
      paramList.add("%" + ip + "%");
    }
    sqlExceptSelect.append(" order by a.resource_id, a.name");
    Page<Account> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getPage(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
