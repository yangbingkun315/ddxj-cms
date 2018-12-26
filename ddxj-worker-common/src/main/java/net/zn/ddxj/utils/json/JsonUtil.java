package net.zn.ddxj.utils.json;


import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class JsonUtil
{
    private static Log log = LogFactory.getLog(JsonUtil.class);
    /**
     * 对象转JSON
    * @Title: bean2json 
    * @Description: TODO
    * @param @param bean
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String bean2jsonToString(Object bean)
    {
    	JSONObject jsonObject = JSONObject.fromObject(bean);
        return String.valueOf(jsonObject);
    }
    public static JSONObject bean2jsonObject(Object bean)
    {
    	JSONObject jsonObject = JSONObject.fromObject(bean);
        return jsonObject;
    }
    /**
     * list转json
    * @Title: list2json 
    * @Description: TODO
    * @param @param list
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String list2jsonToString(List<?> list)
    {
    	JSONArray jsonObject = JSONArray.fromObject(list);
        return String.valueOf(jsonObject);
    }
    public static JSONArray list2jsonToArray(List<?> list)
    {
    	JSONArray jsonObject = JSONArray.fromObject(list);
        return jsonObject;
    }
    public static JSONArray list2jsonToArray(Object obj)
    {
    	JSONArray jsonObject = JSONArray.fromObject(obj);
        return jsonObject;
    }
    /**
     * map转json
    * @Title: map2json 
    * @Description: TODO
    * @param @param map
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String map2jsonToString(Map<?, ?> map)
    {
		JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject.toString();
    }
    public static JSONObject map2jsonToObject(Map<?, ?> map)
    {
		JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }
    /**
     * 对象转json
     */
    public static String object2jsonToString(Object obj)
    {
		JSONObject jsonObject = JSONObject.fromObject(obj);
    	return jsonObject.toString();
    }
    /**
     * 对象转json
     */
    public static JSONObject object2jsonToObject(Object obj)
    {
		JSONObject jsonObject = JSONObject.fromObject(obj);
    	return jsonObject;
    }
}
