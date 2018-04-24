package interfaceApplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.java.JGrapeSystem.rMsg;
import common.java.apps.appsProxy;
import common.java.database.dbFilter;
import common.java.interfaceModel.GrapeDBDescriptionModel;
import common.java.interfaceModel.GrapeDBModel;
import common.java.interfaceModel.GrapePermissionsModel;
import common.java.interfaceModel.GrapeTreeDBModel;
import common.java.security.codec;
import common.java.string.StringHelper;

public class WebGroup {
    private GrapeTreeDBModel gDbModel;
    private String pkString;

    public WebGroup() {

        gDbModel = new GrapeTreeDBModel();//数据库对象
        //数据模型
        GrapeDBDescriptionModel gdbField = new GrapeDBDescriptionModel();
        System.out.println(appsProxy.tableConfig("WebGroup"));
        gdbField.importDescription(appsProxy.tableConfig("WebGroup"));
        gDbModel.descriptionModel(gdbField);
        
        //权限模型
        GrapePermissionsModel gperm = new GrapePermissionsModel();
  		gperm.importDescription(appsProxy.tableConfig("WebGroup"));
  		gDbModel.permissionsModel(gperm);
  		
  		pkString = gDbModel.getPk();
  		
  		gDbModel.enableCheck();
    }

    /**
     * 新增站群
     * 
     * @param webgroupInfo
     * @return
     */
    public String WebGroupInsert(String webgroupInfo) {
        String result = rMsg.netMSG(100, "新增站群失败");
        Object code = 99;
        if (!StringHelper.InvaildString(webgroupInfo)) {
        	//解码
        	webgroupInfo = codec.DecodeFastJSON(webgroupInfo);
            JSONObject groupInfo = JSONObject.toJSON(webgroupInfo);
            // 判断库中是否存在同名站群
            String name = groupInfo.getString("name");
            if (findByName(name)) {
                return rMsg.netMSG(1, "站群已存在"); // 站群已存在
            }
            code = gDbModel.data(groupInfo).autoComplete().insertEx();
        } else {
        	return rMsg.netMSG(2, "站群信息为空");
        }
        result = code != null ? rMsg.netMSG(0, "新增站群成功") : result;
        return result;
    }

    /**
     * 搜索
     * 
     * @param webinfo
     * @return
     */
    public String WebGroupFind(String webinfo) {
        JSONArray array = null;
        if (!StringHelper.InvaildString(webinfo)) {
        	//解码
        	webinfo = codec.DecodeFastJSON(webinfo);
        	
            JSONArray condArray = buildCond(webinfo);
            if (condArray != null && condArray.size() > 0) {
                array = gDbModel.where(condArray).mask("dMode,dValue,sort,uValue,rValue,uMode,itemfatherID,itemSort,rMode,itemLevel").select();
            }
        }
        return rMsg.netMSG(true, (array != null && array.size() > 0) ? array : new JSONArray());
    }

    /**
     * 查询站群信息
     * 
     * @param wbid
     * @return
     */
    public String WebGroupFindBywbid(String wbid) {
        JSONObject object = null;
        if(StringHelper.InvaildString(wbid)){
        	 return rMsg.netMSG(2,"参数信息为空");
        }
        object = gDbModel.eq(pkString, wbid).mask("dMode,dValue,sort,uValue,rValue,uMode,itemfatherID,itemSort,rMode,itemLevel").find();
        return rMsg.netMSG(0, (object != null && object.size() > 0) ? object : new JSONObject());
    }

    /**
     * 修改站群信息
     * 
     * @param wbgid
     * @param webgroupInfo
     * @return
     */
    public String WebGroupUpdate(String wbgid, String webgroupInfo) {
        String result = rMsg.netMSG(100, "站群修改失败");
        Object code = 99;
        if(StringHelper.InvaildString(wbgid) || StringHelper.InvaildString(webgroupInfo)){
        	result = rMsg.netMSG(code, "站群信息为空");
        	return result;
        }
        //解码
        webgroupInfo = codec.DecodeFastJSON(webgroupInfo);
        JSONObject obj = JSONObject.toJSON(webgroupInfo);
        if (obj != null && obj.size() > 0) {
            if (obj.containsKey("name")) {
                String name = obj.get("name").toString();
                if (findByName(name)) {
                    return rMsg.netMSG(1, "该站群已存在"); // 站群已存在
                }
            }
            
            gDbModel.eq(pkString, wbgid);
            GrapeDBModel dataEx = gDbModel.dataEx(obj);
            boolean updateEx = dataEx.updateEx();
            System.out.println(updateEx);
//			code = (gDbModel.dataEx(obj).updateEx()) ? 0 : 99;
            
//            gDbModel.eq(pkString, wbgid);
//            boolean updateEx = gDbModel.dataEx(_webinfo).updateEx();
            result = (int)code == 0 ? rMsg.netMSG(0, "站群修改成功") : result;
        }
        return result;
    }

    /**
     * 分页显示
     * 
     * @param idx
     * @param pageSize
     * @return
     */
    public String WebGroupPage(int idx, int pageSize) {
        return WebGroupPageBy(idx, pageSize, "");
    }

    public String WebGroupPageBy(int idx, int pageSize, String webinfo) {
        long total = 0;
        JSONArray array = null;
        if (!StringHelper.InvaildString(webinfo)) {
            JSONArray condArray = buildCond(webinfo);
            if (condArray != null && condArray.size() > 0) {
            	gDbModel.where(condArray);
            } else {
                return rMsg.netPAGE(idx, pageSize, total, new JSONArray());
            }
        }
        array = gDbModel.page(idx, pageSize);
        return rMsg.netPAGE(idx, pageSize, total, (array != null && array.size() > 0) ? array : new JSONArray());
    }

    /**
     * 验证网站是否存在于表中
     * 
     * @param name
     * @return true 表中已存在
     */
    private boolean findByName(String name) {
        JSONObject object = gDbModel.eq("name", name).find();
        return object != null && object.size() > 0;
    }
    
    /**
     * 整合参数，将JSONObject类型的参数封装成JSONArray类型
     * 
     * @param object
     * @return
     */
    public JSONArray buildCond(String Info) {
        String key;
        Object value;
        JSONArray condArray = null;
        JSONObject object = JSONObject.toJSON(Info);
        dbFilter filter = new dbFilter();
        if (object != null && object.size() > 0) { 	// 判断条件可以改为object instanceof JSONObject 
            for (Object object2 : object.keySet()) {
                key = object2.toString();
                value = object.get(key);
                filter.eq(key, value);
            }
            condArray = filter.build();
        } else {
            condArray = JSONArray.toJSONArray(Info);
        }
        return condArray;
    }
}
