package sys.privilege.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.enums.LegalSourceScopeType;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LegalSource
  extends Model<LegalSource>
{
  private static final long serialVersionUID = 1L;
  public static final LegalSource dao = new LegalSource();
  
  public LegalSource getEnabledByIpAndUserName(String ip, String username)
  {
    return (LegalSource)findFirst(
      "select * from sys_legal_source where data_status=? and ip=? and (scope_type=? or (scope_type=? and scope_target=?))", new Object[] {
      DataStatus.ENABLED.name(), ip, LegalSourceScopeType.ALL.name(), LegalSourceScopeType.FIXED.name(), username });
  }
  
  public LegalSource getByIp(String ip, String scope_type)
  {
    return (LegalSource)findFirst("select * from sys_legal_source where data_status<>? and ip=? and scope_type=?", new Object[] {
      DataStatus.DELETED.name(), ip, scope_type });
  }
  
  public LegalSource getByIpAndUserName(String ip, String scope_type, String userName)
  {
    return (LegalSource)findFirst(
      "select * from sys_legal_source where data_status<>? and ip=? and scope_type=? and scope_target=?", new Object[] {
      DataStatus.DELETED.name(), ip, scope_type, userName });
  }
  
  public Page<LegalSource> getAll(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String scope_type = (String)queryMap.get("scope_type");
    String ip = (String)queryMap.get("ip");
    String username = (String)queryMap.get("username");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from sys_legal_source a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(scope_type))
    {
      sqlExceptSelect.append(" and a.scope_type=?");
      paramList.add(scope_type);
    }
    if (!StringUtil.isBlank(ip))
    {
      sqlExceptSelect.append(" and a.ip like ?");
      paramList.add("%" + ip + "%");
    }
    if (!StringUtil.isBlank(username))
    {
      sqlExceptSelect.append(" and a.scope_target like ?");
      paramList.add("%" + username + "%");
    }
    Page<LegalSource> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getAll(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
