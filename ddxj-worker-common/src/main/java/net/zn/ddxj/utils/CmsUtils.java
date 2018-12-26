package net.zn.ddxj.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;

import lombok.extern.slf4j.Slf4j;
/**
 *  提供一些工银app中使用到的共用方法
 * 
 * @ClassName:  CmsUtils   
 * @Description:TODO  
 * @author: 何俊辉-上海耸智信息科技有限公司
 * @date:   2017年7月6日 下午2:57:13   
 *     
 * @Copyright: 2017 
 *
 */
@Slf4j
public class CmsUtils
{
	/**
	 * 得到定长的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String generateStr(int length)
	{
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < length; i++)
		{
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}
	/**
	 * 得到定长度数字的随机数
	 * @Title: generateNumber   
	 * @Description: TODO
	 * @param: @param length
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String generateNumber(int length)
	{
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		String allChar = "0123456789";
		for (int i = 0; i < length; i++)
		{
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}
	/**
	 * String不等于空
	 * @Title: isNullOrEmpty   
	 * @Description: TODO
	 * @param: @param string
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean isNullOrEmpty(String string)
	{
		if (string == null || "".equals(string) || "null".equalsIgnoreCase(string) || "undefined".equalsIgnoreCase(string))
		{
			return true;
		}
		return false;
	}
	public static boolean isNullOrEmpty(Object string)
	{
		if (string == null || "".equals(string) || "null".equalsIgnoreCase(string.toString()) || "undefined".equalsIgnoreCase(string.toString()))
		{
			return true;
		}
		return false;
	}
	public static boolean isNullOrEmpty(Map<Object,Object> map)
	{
	    if (map == null || map.isEmpty())
	    {
	        return true;
	    }
	    return false;
	}
	
	public static boolean isNullOrEmpty(Set<?> set)
	{
	    if (set == null || set.isEmpty())
	    {
	        return true;
	    }
	    return false;
	}

	public static boolean isNullOrEmpty(java.util.List<?> list)
	{
		if (list == null || list.isEmpty())
		{
			return true;
		}
		return false;
	}

	public static boolean isNullOrEmpty(Object[] array)
	{
		if (array == null || array.length == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为邮箱
	 */
	public static boolean isEmail(String email)
	{
		Pattern emailPattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = emailPattern.matcher(email);
		if (m.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * 得到随机字符串,大小写数字结合
	 * 
	 * @param length
	 * @return
	 */
	public static final String randomString(int length)
	{
		if (length < 1)
		{
			return null;
		}

		Random randGen = new Random();
		char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
				.toCharArray();

		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++)
		{
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}
	/**
	 * 下载网络图片
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @throws Exception
	 */
	public static void downloadNetFile(String urlString, String filePath) throws Exception
	{
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filePath);
		// 开始读取
		while ((len = is.read(bs)) != -1)
		{
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}
	/**
	 * 是不是数字
	 * @Title: isNumber   
	 * @Description: TODO
	 * @param: @param number
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean isNumber(String number)
	{
		if(CmsUtils.isNullOrEmpty(number))
		{
			return false;
		}
		return NumberUtils.isNumber(number);
	}
	public static int getRandomInt(int a, int b) {  
        if (a > b || a < 0)  
            return -1;  
        return a + (int) (Math.random() * (b - a + 1));  
    }
	public static double getRandomDouble(double a, double b) {  
		if (a > b || a < 0)  
			return -1;  
		return formatterDouble(Math.random() * (b - a) + a);  
	}

    public static double formatterDouble(double number,int newScale)
    {
    	return new BigDecimal(number).setScale(newScale,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double formatterDouble(double number)
    {
        return formatterDouble(number,2);
    }
    /**
     * 计算字符串长度，中文两个字节，英文一个字节
     * @param s
     * @return
     */
    public static int getStringLength(String s)
    {
        int length = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;
                
        }
        return length;
    }
    /**
     * 验证手机号码
     * @Title: isMobile   
     * @Description: TODO
     * @param: @param str
     * @param: @return      
     * @return: boolean      
     * @throws
     */
    public static boolean isMobile(String str) 
    {   
    	if(CmsUtils.isNullOrEmpty(str))
    	{
    		return false;
    	}
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    } 
    public static String dateToString(Date date) {  
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	String str=sdf.format(date); 
    	return str;
    } 
    /**
     * 
    * @Title: getImageIo 
    * @Description: TODO(用图片流获取上传的二进制字节码) 
    * @param @param base64data
    * @param @return
    * @param @throws IOException    设定文件 
    * @return byte[]    返回类型 
    * @throws
     */
    public static byte[] getImageIo(String base64data) throws IOException
    {
    	sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		String Base64IMGData = base64data.replaceAll("data:image/jpg;base64,", "");
		Base64IMGData = Base64IMGData.replaceAll("data:image/png;base64,", "");
		Base64IMGData = Base64IMGData.replaceAll("data:image/jpeg;base64,", "");
		Base64IMGData = Base64IMGData.replaceAll("data:image/gif;base64,", "");
		byte[] bytes = decoder.decodeBuffer(Base64IMGData);
		
		return bytes;
    	
    }
    /** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值 
     *  
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
    	String ip = request.getHeader("x-forwarded-for");
		log.info("从ip = request.getHeader(x-forwarded-for)中获取ip_____" + ip);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info("从ip = request.getHeader(Proxy-Client-IP)中获取ip_____" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			log.info("从ip = request.getHeader(WL-Proxy-Client-IP)中获取ip_____" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			log.info("从ip = request.getHeader(HTTP_CLIENT_IP)中获取ip_____" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			log.info("从ip = request.getHeader(HTTP_X_FORWARDED_FOR)中获取ip_____" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip)) {
//				if (true) {
				//根据网卡取本机配置的ip
				try {
					InetAddress  inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				log.info("从InetAddress.getLocalHost().getHostAddress()获取ip_____" + ip);
			}else{
				log.info("从request.getRemoteAddr()中获取ip_____" + ip);
			}
		}
		log.info("#################获取客户端ip:"+splitIp(ip)+"#################");
		return splitIp(ip);
    }
    /**
	 * 取request的参数
	 * @param request
	 * @return json格式
	 * @throws IOException
	 */
	public static String readRequestStream(HttpServletRequest request) throws IOException {
		InputStream inputStream = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuilder builder = new StringBuilder();
		String s = "";
		while ((s = br.readLine()) != null) {
			builder.append(s);
		}
		String retStr = builder.toString();
		// 如果字符串最后一位是换行符则删除该换行符
        if(null != retStr && (retStr.lastIndexOf('\n') == retStr.length()-1)){
        	retStr = retStr.substring(0, retStr.length()-1);
        }
		return retStr;
	}
	/**
	 * @author wxy
	 * @创建时间 2017年9月1日
	 * @所属公司 xxxx软件有限公司
	 * @版本描述 1.0
	 * @方法描述 
 	 *      如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串Ｉｐ值
	 *      如：X-Forwarded-For：192.168.1.110， 192.168.1.120， 192.168.1.130， 192.168.1.100用户真实IP为： 192.168.1.110
	 *      所以下面这个方法就派上了用场.
	 * @param ip
	 * @return
	 */
	private static String splitIp(String ip){
//		String ip = "unknown, 192.168.137.141, 172.0.0.1";
		String result = "";
		if (null != ip && ip.indexOf(",") != -1) {
			String[] arr = ip.split(",");
			for (String string : arr) {
				if (! "unknown".equals(string.trim().toLowerCase())) {//过滤掉第一个为unknown的。
					result = string.trim();
					break;
				}
			}
			return result;
		}else{
			return ip;
		}
	}
	public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
           return 0;
        }
    }
	public static Date getBirthByAge(int age)
	{
		String stringDate = DateUtils.getStringDate(new Date(),"yyyy-MM-dd");
		String year = stringDate.split("-")[0];
		DecimalFormat df = new DecimalFormat("0");
		int parseInt = Integer.parseInt(year);
		float thisYear = (float)parseInt-age;
		String num3 = df.format(thisYear);
		
		String birth = num3+"-00-00";
		
		return DateUtils.getDate(birth, "yyyy-MM-dd");
	}
	 /**  
     * 验证码身份证信息
     */
    public static boolean validateIdCard(String id) {
    	String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!id.matches(reg)) {
            return false;
        }
        return true;
    }
    /**
     *  验证姓名是否是汉子
     */
   public static boolean validateRealName(String str) {
	    Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
   }
   public static Integer imageSize(String image){  
       String str=image.substring(22); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。  
       Integer equalIndex= str.indexOf("=");//2.找到等号，把等号也去掉  
       if(str.indexOf("=")>0) {  
           str=str.substring(0, equalIndex);  
       }  
       Integer strLength=str.length();//3.原来的字符流大小，单位为字节  
       Integer size=strLength-(strLength/8)*2;//4.计算后得到的文件流大小，单位为字节  
       return size;  
   } 
   public static int compareIndexNumber(int n1,int n2,int id1,int id2)
   {
   	if(n1 > n2)
		{
			return 1;
		}
		else if(n1 == n2)
		{
			if(id1 > id2)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
   }
   public static String getUuid() {
	   String s = UUID.randomUUID().toString();
            // 去掉"-"符号
       return s.replace("-", "");
    }
}
