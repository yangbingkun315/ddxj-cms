package net.zn.ddxj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CityUtils {

	public static JSONArray queryCityJSON()
	{
		String str = CityUtils.read(CityUtils.class.getResourceAsStream("/json/city.json"));
		JSONArray json = new JSONArray().parseArray(str);
		return json;
	}
	
	public static String read(InputStream inputStream) {
		  BufferedReader reader = null;
		  String laststr = "";
		  try{
		 
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		    reader = new BufferedReader(inputStreamReader);
		    String tempString = null;
		    while((tempString = reader.readLine()) != null){
		      laststr += tempString;
		    }
		  }catch(Exception e){
		    e.printStackTrace();
		  }finally{
		    if(reader != null){
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		  }
		  return laststr;
	}
}
