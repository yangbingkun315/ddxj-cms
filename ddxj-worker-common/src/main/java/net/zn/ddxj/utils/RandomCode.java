package net.zn.ddxj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.zn.ddxj.constants.Constants;

/**
 * Created by zhouquan on 2016/3/11.
 */
public class RandomCode {
    private static final Random r = new Random();

//    //获取订单流水号，两个随机字母 + 当前时间值 + 一个随机字母命名规则
//    public static String getCode(Integer id) {
//        String code = TimeUtils.getYYYY_MM_DD_HH_MM_SS();
//        StringBuilder sb = new StringBuilder();
//        sb.append(code.toCharArray());
//        for (int i = 0; i < 4; i++) {
//            sb.append(Constants.RANDOM_NUMBER.charAt(r.nextInt(10)));
//        }
//        for (int i = 0; i < 4; i++) {
//            sb.append(Constants.RANDOM_UPPER_WORD.charAt(r.nextInt(26)));
//        }
//        return sb.toString();
//    }
    
	// 获取提现流水号
	public static String getWithdrawCode() {
		String code = new SimpleDateFormat("YYYYMMddhhmmss").format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append(code.toCharArray());
		for (int i = 0; i < 4; i++) {
			sb.append(Constants.RANDOM_NUMBER.charAt(r.nextInt(10)));
		}
		return sb.toString();
	}

    public static String getCode(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    public static String getPicture() {
        String date = new Date().getTime() + "";
        return Constants.ORDER_RANDOM.charAt(r.nextInt(62)) + "" + Constants.ORDER_RANDOM.charAt(r.nextInt(62)) + date;
    }

    public static String getTicketNum() {
        String vars = "";
        for (int i = 0; i < 7; i++) {
            vars += Constants.ORDER_RANDOM.charAt(r.nextInt(62));
        }
        vars = vars.toUpperCase();
        return vars;
    }

    public static String getNonce_str(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<32;i++){
            sb.append(Constants.ORDER_RANDOM.charAt(r.nextInt(62)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
//        System.out.println(getNonce_str());
        System.out.println(getCode());
    }

	

}
