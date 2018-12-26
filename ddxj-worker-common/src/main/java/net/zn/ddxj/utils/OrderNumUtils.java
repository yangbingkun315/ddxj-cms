package net.zn.ddxj.utils;


public class OrderNumUtils {
	public static String getOredrNum(){
		int r1 = (int) (Math.random() * (10));// 产生3个0-9的随机数
		int r2 = (int) (Math.random() * (10));
		int r3 = (int) (Math.random() * (10));
		long now = System.currentTimeMillis();// 时间戳
		String orderNumber = String.valueOf(now) + String.valueOf(r1) + String.valueOf(r2)+ String.valueOf(r3);// 订单ID
		return orderNumber;
	}
	
	public static void main(String[] args) {
		String mobile = "哈喽13856984571，15000507156也可以哦";
		System.out.println(OrderNumUtils.getNewString(mobile));

	}
	public static String getNewString(String mobile){
		mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	    //System.out.println(mobile);
		return 	mobile;
		
	}

}
