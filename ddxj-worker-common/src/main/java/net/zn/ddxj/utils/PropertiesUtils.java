package net.zn.ddxj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by zhouquan on 2016/3/8.
 */
//定义所有的正则表达式，常量形势
public class PropertiesUtils {


    public static String getPropertiesByName(String name){
        Properties properties = new Properties();
        InputStream in = PropertiesUtils.class.getResourceAsStream("/config/config.yml");
        try {
            properties.load(new InputStreamReader(in,"utf-8"));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(name);
    }
    public static String readResource(InputStream inputStream) {
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
//    public static void main(String[] args) {
//        String s = null;
//        System.out.println(getRegByName("test"));
//    }
}
