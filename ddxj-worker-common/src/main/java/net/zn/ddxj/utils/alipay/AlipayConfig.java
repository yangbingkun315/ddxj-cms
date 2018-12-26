package net.zn.ddxj.utils.alipay;

public class AlipayConfig {
	
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String PARTNER = "2088031759595165";
	// 商户appid
	public static String APPID = "2018042660061238";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCwcDsY1TzDkrhiH5cKd9s72z+1ym3tYfzhNMyjo2NoLJqEtdCfRYjSGgPKigwX/cNN1+dpfSIKQVmLU4wPHqgsl8spOIIdsREctw3GwOkplmY006fMrXnr/FJ9prKpmssxWiDF64t0VhuBrYerh8PYmm+3uPU4qX4Qe1iEUKQf/yW7PsQyKZgIIJEV068BtNDbfd+3ToTZ5g0H8DNJdObWxb9qHmWGj0FXiLaoZUEfe2rxz4v2fm6eg6vovM6B45Fkhn+1Kl6+bux4V+V3YRQBhlMq/nJWa3xV8t6Yh/GLr/+5NbAYgEr8vprPWcN3sMAxHTeXr9G5yIRloN4KDfXVAgMBAAECggEAVRC3HWDpm+0oIn7ShoSE2ZX6YH5Jg5KjpjybkSWcLsSadvNlsvSGY62ROFxYOlTpjaVTd+ORvtoE/wx30W5ZbK5+j9Ajw4b0FiEXbkTh5WASxNNq6l6VErajWts89lKgNpoahPHsWVmOh3YPnB6nyM2koTJ2YCwvkGi3o4txpc7EAanDNl+zflsktheHKMcvUxK2b5ofMWPWOLSFehagbXeQdgD1S9QCmXwueG9GqFemmIuw0R0iai5TzX7LWDBNLtWuca1xKDvVhH/0J/QqU262GKW/YWqK2v8ikypJ2wNn/+tDt9wAuZIjADEI85t0YeYFmUv6WZg7TPSEJQQd3QKBgQDXY1RftNGNDuRGNH7xT3n7wpslnfu4p6hh4iXyJjgXrnhI8Y3XC59O+VilRcaHNtF5emNwKAMEOmVhPdGQko4YX7SYtniKwwTsmpvaw4R57iUo14KRxoBUU4H0ufsGIFe0qD5r+waDloMOLmbu7MLe323+Nu6r2TwZidwHTWVK1wKBgQDRtNLRDkgsC62+qAgPscX3w4OjKNWVFQItrucjAFEiDoONc6rrDDZf0HXsjGNYfCaaBQcAGWTWyzPeFerrTWjTUrCPaLPc7eqIPKVkLJJDb5Bvuajww9Iij4OHA5PvkKs5vdIWuf456r4VrhkN8tF9nuTCI0FfperfLYk/XtO7MwKBgERAPnlBU/Gt2VoBwoP22kGTzhPiduRxhCLYRYvw3m9qosUCfh65mRauXo+oh5tLHJ+wz5e3phUN6BgjPafhONOyaAjIMXRPDd1ntKFiuTTl6LKDb+V/kCSofZKt6ERttT0S3wzac5r487hnrchiD/INvM4EXCbqZGf3TlimBsXrAoGADBUfEGyCPAdwO0vzKvRTM7NQIlZ3997+ASPSrXGXOx8a9Ut0zZYmi9627KoqDy8YCke/34GXDcknc+RPZL9RCtv40Ep4EoKiwLa90MfApaIH8GgCE900fklGisDqBSOQugL119lfjHa5/QXK68LRzysYgxcE7T3iCnnVLpGIIYECgYAdSFb2z2SvdGD5GflW0j/T1WyPCfYFi7u+zTL+PSBQk4sumpxxZ42SPhogdomcCQlUTie245EQrkx22q70VA+ImKkE5vEM/HEXaqvBx6407QJ+IIZCIYBQ7RzDCvw6oJbc5g3JZswXKVTE9p8hVhx8bmbgfIEJC9fBI+bRDMYWKg==";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String NOTIFY_URL = "http://test.diandxj.com/worker-app/alipay/callback/upload.ddxj";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String RETURN_URL = "http://test.diandxj.com/worker-app/alipay/callback/upload.ddxj";
	// 请求网关地址
	public static String URL = "https://openapi.alipaydev.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArgzirJMksbV5EiZ33QKihurt7/goPx4/IPcxTzDrE2UkvegDWzY/svWS498Yr2ua2BNtYbCLzDgRhxXv7tuawQlCnABgXXh35NvI1f6U3rQ/kHLpQNQgWVyhafyVkkXC7n509cUXBGiP1Z7D+JyGa6b3xWE1Uqe8sQl0qI1ZE27B/9ILZ9BtgsR6uRxzyCq+G8kSUg9hGxYOi0IguGqc9/Sy7nbCrYoVUz8P3yH6E8vmHL+uX11AFFzA9nm012+KRWJ7bkM+wCS1mNuzBzd1W+3J7YV+WoFmkdVJnUGlzuKo1nyO9Oj2j1KczwLC3xX5+gWiD3jSL+MlQWHklLnjhwIDAQAB";
	// 日志记录目录
	public static String LOG_PATH = "E:/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";
	// 交易方式
	public static String PRODUCT_CODE = "QUICK_MSECURITY_PAY";
	
}
