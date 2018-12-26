package net.zn.ddxj.utils.aliyun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FindBankUtils {
	/**
	 * 阿里云（查询银行卡信息）
	* @Title: FindBankUtils.java  
	* @param @param bankCardNo
	* @param @return
	* @param @throws Exception参数  
	* @return String    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static String findBankInfo(String bankCardNo) throws Exception
	{
		   // 创建HttpClient实例       
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=";  
        url+=bankCardNo;  
        url+="&cardBinCheck=true";  
        StringBuilder sb = new StringBuilder();    
        try {    
            URL urlObject = new URL(url);    
            URLConnection uc = urlObject.openConnection();    
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));    
            String inputLine = null;    
            while ( (inputLine = in.readLine()) != null) {    
                sb.append(inputLine);    
            }    
            in.close();    
        } catch (MalformedURLException e) {    
            e.printStackTrace();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        return sb.toString();  
	}
	/**
	 * 验证是否支持该银行
	* @Title: FindBankUtils.java  
	* @param @param bankCode
	* @param @return参数  
	* @return boolean    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static boolean validateSupportBank(String bankCode)
	{
		boolean flag = false;
		String[] allBank = new String[]{"SRCB","BGB","SHRCB","ZJQL","RCC","BJBANK","WHCCB","BOZK","KORLABANK","SPABANK","SDEB","HURCB","WRCB","BOCY","CZBANK","HDBANK","BOC","BOD","CCB","ZYCBANK","SXCB","GZRCU","ZJKCCB","BOJZ","BOP","HKB","SPDB","NXRCU","NYNB","GRCB","BOSZ","HZCB","HSBK","CABANK","HBC","JXBANK","HRXJB","BODD","AYCB","EGBANK","CDB","TCRCB","NJCB","ZZBANK","DYCB","YBCCB","SCRCU","KLB","LSBANK","YDRCB","CCQTGB","FDB","JSRCU","JNBANK","CMB","JINCHB","FXCB","WHRCB","HBYCBANK","TZCB","TACCB","XCYH","CEB","NXBANK","HSBANK","JJBANK","NHQS","MTBANK","LANGFB","ASCB","KSRB","YXCCB","DLB","DRCBCL","GCB","NBBANK","BOYK","SXRCCU","GLBANK","BOQH","CDRCB","QDCCB","HKBEA","HBHSBANK","WZCB","TRCB","QLBANK","GDRCC","ZJTLCB","GZB","GYCB","CQBANK","DAQINGB","CGNB","SCCB","CSRCB","SHBANK","JLBANK","CZRCB","BANKWF","ZRCBANK","FJHXBC","ZJNX","LZYH","JSB","BOHAIB","CZCB","YQCCB","SJBANK","XABANK","BSB","JSBANK","FSCB","HNRCU","COMM","XTB","CITIC","HXBANK","HNRCC","DYCCB","ORBANK","BJRCB","XYBANK","ZGCCB","HRBANK","CDCB","HANABANK","CMBC","LYBANK","GDB","ZBCB","CBKF","H3CB","CIB","CRCBANK","SZSBK","DZBANK","SRBANK","LSCCB","JXRCU","ICBC","JZBANK","HZCCB","NHB","XXBANK","JRCB","YNRCC","ABC","GXRCU","PSBC","BZMD","ARCU","GSRCU","LYCB","JLRCU","URMQCCB","XLBANK","CSCB","JHBANK","BHB","NBYZ","LSBC","BOCD","SDRCU","NCB","TCCB","WJRCB","CBBQS","HBRCU"};
		for(String bankcode : allBank)
		{
			if(bankCode.equals(bankcode))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 获取银行背景颜色
	* @Title: FindBankUtils.java  
	* @param @param bankCode
	* @param @return参数  
	* @return String    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static String getBankColor(String bankCode)
	{
		String[] redListCode = new String[]{"BOC","ICBC","CITIC","SJBANK","H3CB","CMB","HSBANK","GDB","HXBANK","CSCB","BJBANK"};
		String[] orangeListCode = new String[]{"HRBANK","ZZBANK","CEB","BHB","BOQH","SHBANK"};
		String[] greenListCode = new String[]{"CMBC","PSBC","ABC","SZBANK","BOSZ"};
		String[] blueListCode = new String[]{"SPDB","RCC","CCB","ZJTLCB","SPABANK","TCCB","LZYH","CIB","COMM","XABANK"};
		
		String returnContent = "";
		boolean flag = false;
		for(int d = 0;d<redListCode.length;d++)
		{
			if(bankCode.equals(redListCode[d]) )
			{
				returnContent = "red";
				flag = true;
				break ;
			}
		}
		if(!flag)
		{
			for(int d = 0;d<orangeListCode.length;d++)
			{
				if(bankCode.equals(orangeListCode[d]))
				{
					returnContent = "orange";
					flag = true;
					break ;
				}
			}
		}
		if(!flag)
		{
			for(int d = 0;d<greenListCode.length;d++)
			{
				if(bankCode.equals(greenListCode[d]))
				{
					returnContent = "green";
					flag = true;
					break ;
				}
			}
		}
		if(!flag)
		{
			for(int d = 0;d<blueListCode.length;d++)
			{
				if(bankCode.equals(blueListCode[d]))
				{
					returnContent = "blue";
					flag = true;
					break ;
				}
			}
		}
		
		return returnContent;
	}
	/**
	 * 获取所有银行列表
	* @Title: FindBankUtils.java  
	* @param @return参数  
	* @return JSONObject    返回类型 
	* @throws
	* @Package net.zn.ddxj.utils.aliyun  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @date 2018年5月11日  
	* @version V1.0
	 */
	public static JSONObject queryBankJSON()
	{
		String str = FindBankUtils.read(FindBankUtils.class.getResourceAsStream("/json/bank.json"));
		JSONObject json = new JSONObject().parseObject(str);
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
