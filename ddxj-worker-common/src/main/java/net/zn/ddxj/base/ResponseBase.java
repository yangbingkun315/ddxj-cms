package net.zn.ddxj.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.utils.json.JsonUtil;


/**
 * Created by zhouquan on 2016/3/1.
 */
public class ResponseBase {
	
	    private boolean response;
	    private String responseMsg;
	    private Integer responseCode;
	    private String data;
	    private Map dataMap = new HashMap();

		public boolean isResponse() {
			return response;
		}

		public void setResponse(boolean response) {
			this.response = response;
		}

		public String getResponseMsg() {
			return responseMsg;
		}

		public void setResponseMsg(String responseMsg) {
			this.responseMsg = responseMsg;
		}

		public Integer getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(Integer responseCode) {
			this.responseCode = responseCode;
		}
		public String getData() {
	        return JsonUtil.object2jsonToString(dataMap);
	    }

	    public void setData(Map data) {
	        this.dataMap = data;
	    }

	    public void push(String key, Object value, boolean useFilter) {
	        Class c = value.getClass();
	        Field[] fs = c.getDeclaredFields();
	        for (Field f : fs) {
	            if (f.getAnnotation(FilterAnnotion.class) != null) {
	                f.setAccessible(true);
	                try {
	                    f.set(value, null);
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        dataMap.put(key, value);
	    }

	    public void push(String key, Object value) {
	    	dataMap.put(key, value);
	    }
//	    public void push(String name, List list) throws IllegalArgumentException, IllegalAccessException{
//	        if (list != null && list.size() > 0) {
//	            for (Object o : list) {
//	                Class c = o.getClass();
//	                Field[] fs = c.getDeclaredFields();
//	                for (Field f : fs) {
//	                    if (f.getAnnotation(FilterAnnotion.class) != null) {
//	                        f.setAccessible(true);
//	                        f.set(o, null);
//	                    }
//	                }
//	            }
//	            data.put(name, list);
//	        }
//	    }

	    public void push(String name, List list){
	    	dataMap.put(name, list);
	    }

	public static ResponseBase getInitResponse()
	{
		return new ResponseBase();
	}
}
