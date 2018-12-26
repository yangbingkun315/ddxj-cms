package net.zn.ddxj;

import java.util.Date;

import net.zn.ddxj.utils.DateUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Date d = new Date();
		System.out.println(DateUtils.getStringDate(DateUtils.getSpecficMonthStart(new Date(), 1), "yyyy-MM-dd"));;
	}

}
