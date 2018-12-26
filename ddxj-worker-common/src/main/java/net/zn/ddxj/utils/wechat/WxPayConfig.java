package net.zn.ddxj.utils.wechat;


public class WxPayConfig {
	

	// =======【证书路径设置】=====================================
	/*
	 * 证书路径,注意应该填写绝对路径（仅退款、撤销订单时需要）
	 */
	public static String SSLCERT_PATH = "apiclient_cert.p12";
	public static String SSLCERT_PASSWORD = "1233410002";

	// =======【商户系统后台机器IP】=====================================
	/*
	 * 此参数可手动配置也可在程序中自动获取
	 */
	public static String IP = "8.8.8.8";

	// =======【代理服务器设置】===================================
	/*
	 * 默认IP和端口号分别为0.0.0.0和0，此时不开启代理（如有需要才设置）
	 */
	public static String PROXY_URL = "http://0.0.0.0:0";

	// =======【上报信息配置】===================================
	/*
	 * 测速上报等级，0.关闭上报; 1.仅错误时上报; 2.全量上报
	 */
	public static int REPORT_LEVENL = 0;

	// =======【日志级别】===================================
	/*
	 * 日志等级，0.不输出日志；1.只输出错误信息; 2.输出错误和正常信息; 3.输出错误信息、正常信息和调试信息
	 */
	public static int LOG_LEVENL = 0;
}
