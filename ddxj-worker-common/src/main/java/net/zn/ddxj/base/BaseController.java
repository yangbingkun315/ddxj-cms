package net.zn.ddxj.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController{

	    private boolean response;
	    private String responseMsg;
	    private Integer responseCode;
	    private Map data = new HashMap();

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

		public Map getData() {
	        return data;
	    }

	    public void setData(Map data) {
	        this.data = data;
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
	        data.put(key, value);
	    }

	    public void push(String key, Object value) {
	        data.put(key, value);
	    }

	    public void push(String name, List list, boolean useFilter) throws IllegalAccessException {
	        if (list != null && list.size() > 0) {
	            for (Object o : list) {
	                Class c = o.getClass();
	                Field[] fs = c.getDeclaredFields();
	                for (Field f : fs) {
	                    if (f.getAnnotation(FilterAnnotion.class) != null) {
	                        f.setAccessible(true);
	                        f.set(o, null);
	                    }
	                }
	            }
	            data.put(name, list);
	        }
	    }

	    public void push(String name, List list) throws IllegalAccessException {
	        data.put(name, list);
	    }


}
