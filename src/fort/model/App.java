package fort.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import common.enums.DataStatus;
import common.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App
  extends Model<App>
{
  private static final long serialVersionUID = 1L;
  public static final App dao = new App();
  
  public List<App> listAll()
  {
    return find("select * from fort_app where data_status=?", new Object[] { DataStatus.ENABLED.name() });
  }
  
  public Page<App> getPage(int pageCurrent, int pageSize, Map<String, String> queryMap)
  {
    String name = (String)queryMap.get("name");
    
    StringBuilder select = new StringBuilder();
    StringBuilder sqlExceptSelect = new StringBuilder();
    List<Object> paramList = new ArrayList();
    
    select.append("select a.*");
    sqlExceptSelect.append("from fort_app a where a.data_status<>?");
    paramList.add(DataStatus.DELETED.name());
    if (!StringUtil.isBlank(name))
    {
      sqlExceptSelect.append(" and a.name_cn like ?");
      paramList.add("%" + name + "%");
    }
    Page<App> page = paginate(pageCurrent, pageSize, select.toString(), sqlExceptSelect.toString(), 
      paramList.toArray(new Object[0]));
    if ((pageCurrent > 1) && (page.getList().isEmpty())) {
      page = getPage(pageCurrent - 1, pageSize, queryMap);
    }
    return page;
  }
}
