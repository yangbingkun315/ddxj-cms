package net.zn.ddxj.utils.aliyun;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import net.zn.ddxj.utils.HttpUtils;
import net.zn.ddxj.utils.PropertiesUtils;
public class BankValidateUtils {
	/**
	 * 阿里云市场（验证银行卡四要素）
	* @Title: BankValidateUtils.java  
	* @param @param bankCardNo
	* @param @param identityNo
	* @param @param mobileNo
	* @param @param name
	* @param @return
	* @param @throws Exception参数  
	* @return String    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static String validateBankInfo(String bankCardNo,String identityNo,String mobileNo,String name) throws Exception
	{
	     Map<String, String> headers = new HashMap<String, String>();
	     //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	     headers.put("Authorization", "APPCODE " + PropertiesUtils.getPropertiesByName("validate_appcode"));
	     Map<String, String> querys = new HashMap<String, String>();
	     querys.put("bankCardNo", bankCardNo);
	     querys.put("identityNo", identityNo);
	     querys.put("mobileNo", mobileNo);
	     querys.put("name", name);
     	/**
    	* 重要提示如下:
    	* HttpUtils请从
    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
    	* 下载
    	*
    	* 相应的依赖请参照
    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
    	*/
    	HttpResponse response = HttpUtils.doGet(PropertiesUtils.getPropertiesByName("bank_validate_url"), PropertiesUtils.getPropertiesByName("bank_validate_path"), "GET", headers, querys);
    	System.out.println(response.toString());
    	String resultString = "";
    	 if (response.getStatusLine().getStatusCode() == 200) {
             resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
         }
         	
    	return resultString;
	}
}
