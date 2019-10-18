package portal.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Resource
  extends Model<Resource>
{
  private static final long serialVersionUID = 1L;
  public static final Resource dao = new Resource();
  
  public Resource getByName(String name)
  {
    return (Resource)findFirst("select * from base_resource a where a.name=?", new Object[] { name });
  }
  
  public List<Resource> listAllEnabled()
  {
    return find("select * from base_resource a where a.data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
  
  public Page<Resource> getPage(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String name = (String)queryMap.get("name");
    String name_cn = (String)queryMap.get("name_cn");
    String ip = (String)queryMap.get("ip");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from base_resource a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and a.name like ?");
      paramList.add("%" + name + "%");
    }
    if (!StringUtil.isBlank(name_cn))
    {
      sqlExceptSelect.append(" and a.name_cn like ?");
      paramList.add("%" + name_cn + "%");
    }
    if (!StringUtil.isBlank(ip))
    {
      sqlExceptSelect.append(" and a.ip like ?");
      paramList.add("%" + ip + "%");
    }
    Page<Resource> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getPage(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
