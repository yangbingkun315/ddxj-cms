package net.zn.ddxj.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * @author Tom
 */
public class DateUtils
{
    private StringBuffer buffer = new StringBuffer();

    private static String ZERO = "0";

    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static boolean isDate(String strDate,String formatter)
    {
    	if(strDate == null)
    	{
    		return false;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        try
        {
        	sdf.parse(strDate);
        	return true;
        }
        catch (ParseException e)
        {
            
        }
        return false;
    }
    public String getNowString()
    {
        Calendar calendar = getCalendar();
        buffer.delete(0, buffer.capacity());
        buffer.append(getYear(calendar));

        if (getMonth(calendar) < 10)
        {
            buffer.append(ZERO);
        }
        buffer.append(getMonth(calendar));

        if (getDate(calendar) < 10)
        {
            buffer.append(ZERO);
        }
        buffer.append(getDate(calendar));
        if (getHour(calendar) < 10)
        {
            buffer.append(ZERO);
        }
        buffer.append(getHour(calendar));
        if (getMinute(calendar) < 10)
        {
            buffer.append(ZERO);
        }
        buffer.append(getMinute(calendar));
        if (getSecond(calendar) < 10)
        {
            buffer.append(ZERO);
        }
        buffer.append(getSecond(calendar));
        return buffer.toString();
    }
    public static Calendar getCalendarDate(Long time)
    {
    	Calendar c = getCalendar();
    	c.setTime(new Date(time));
    	return c;
    }
    public static Date getDateTime(Long time)
    {
    	Date date = new Date();
    	date.setTime(time);
    	return date;
    }
    public static Calendar getCalendarYMD(int year, int month, int day)
    {
        Calendar c = getCalendar();
        c.setTime(getDate(year, month-1, day));
        return c;
    }
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {  
    	  
        GregorianCalendar cal = new GregorianCalendar();  
        cal.setTime(date);  
        XMLGregorianCalendar gc = null;  
        try {  
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);  
        } catch (Exception e) {  
  
             e.printStackTrace();  
        }  
        return gc;  
    }  
    public static int getDateField(Date date, int field)
    {
        Calendar c = getCalendar();
        c.setTime(date);
        return c.get(field);
    }

    public static int getInternalDay(Date beginDate, Date endDate)
    {
        return (int)((endDate.getTime() - beginDate.getTime()) / (3600L * 1000 * 24));
    }

    public static int getYearsBetweenDate(Date begin, Date end)
    {
        int bYear = getDateField(begin, Calendar.YEAR);
        int eYear = getDateField(end, Calendar.YEAR);
        return eYear - bYear;
    }

    public static int getMonthsBetweenDate(Date begin, Date end)
    {
        int bMonth = getDateField(begin, Calendar.MONTH);
        int eMonth = getDateField(end, Calendar.MONTH);
        return eMonth - bMonth;
    }

    public static int getWeeksBetweenDate(Date begin, Date end)
    {
        int bWeek = getDateField(begin, Calendar.WEEK_OF_YEAR);
        int eWeek = getDateField(end, Calendar.WEEK_OF_YEAR);
        return eWeek - bWeek;
    }

    public static long getHoursBetweenDate(Date begin, Date end)
    {
    	return getMinutesBetweenDate(begin,end) / 60;
    }
    public static long getMinutesBetweenDate(Date begin, Date end)
    {
    	long interval = end.getTime() - begin.getTime();
    	return interval / (1000 * 60);
    }
    public static long getSecondBetweenDate(Date begin, Date end)
    {
    	long interval = end.getTime() - begin.getTime();
    	return interval / 1000;
    }

    public static Date getDate(int year, int month, int day)
    {
        Calendar c = getCalendar();
        c.set(year, month, day);
        return c.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second)
    {
        Calendar c = getCalendar();
        c.set(year, month, day, hour, minute, second);
        return c.getTime();
    }

	public static int getDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int javaDay = cal.get(Calendar.DAY_OF_WEEK);
		if(javaDay == 1)
		{
			return 7;
		}
		else
		{
			return javaDay - 1;
		}
	}

    /**
     * 获取date年后的amount年的第一天的开始时间
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficYearStart(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, amount);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取date年后的amount年的最后一天的终止时间
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficYearEnd(Date date, int amount)
    {
        Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return getFinallyDate(cal.getTime());
    }

    /**
     * 获取date月后的amount月的第一天的开始时间
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficMonthStart(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取当前自然月后的amount月的最后一天的终止时间
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficMonthEnd(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSpecficMonthStart(date, amount + 1));
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return getFinallyDate(cal.getTime());
    }

    /**
     * 获取date周后的第amount周的开始时间（这里星期一为一周的开始）
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficWeekStart(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getStartDate(cal.getTime());
    }

    /**
     * 获取date周后的第amount周的最后时间（这里星期日为一周的最后一天）
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getSpecficWeekEnd(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY); /* 设置一周的第一天为星期一 */
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getFinallyDate(cal.getTime());
    }

    public static Date getSpecficDateStart(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, amount);
        return getStartDate(cal.getTime());
    }

    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getFinallyDate(Date date)
    {
        String temp = format.format(date);
        temp += " 23:59:59";

        try
        {
            return format1.parse(temp);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static String getStringDate(Date date, String formatter)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }
    public static Date getDate(String strDate, String formatter)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        try
        {
            return sdf.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getStartDate(Date date)
    {
        String temp = format.format(date);
        temp += " 00:00:00";

        try
        {
            return format1.parse(temp);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private int getYear(Calendar calendar)
    {
        return calendar.get(Calendar.YEAR);
    }

    private int getMonth(Calendar calendar)
    {
        return calendar.get(Calendar.MONDAY) + 1;
    }

    private int getDate(Calendar calendar)
    {
        return calendar.get(Calendar.DATE);
    }

    private int getHour(Calendar calendar)
    {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getMinute(Calendar calendar)
    {
        return calendar.get(Calendar.MINUTE);
    }
    public static int getNowMinute()
    {
    	return getCalendar().get(Calendar.MINUTE);
    }

    private int getSecond(Calendar calendar)
    {
        return calendar.get(Calendar.SECOND);
    }

    private static Calendar getCalendar()
    {
        return Calendar.getInstance();
    }

    public static Date getFutureDate(Date now,int dateField,int num)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(dateField, num);
        return cal.getTime();
    }
    /**
     *获取几天前，刚刚
    * @Title: getTimeDiff 
    * @Description: TODO
    * @param @param date
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String getTimeDiff(Date date)
	{
		Calendar cal = Calendar.getInstance();
		long diff = 0;
		Date dnow = cal.getTime();
		String str = "";
		diff = dnow.getTime() - date.getTime();

		if (diff > 24 * 60 * 60 * 1000)
		{
			long day = diff / (24 * 60 * 60 * 1000);
			if (day > 3)
			{
				str = getStringDate(date, "MM-dd HH:mm");
			}
			else
			{
				str = day + "天前";
			}
		}
		else if (diff > 60 * 60 * 1000)
		{
			str = diff / (60 * 60 * 1000) + "小时前";
		}
		else if (diff > 60 * 1000)
		{
			str = diff / (60 * 1000) + "分钟前";
		}
		else
		{
			str = "刚刚";
		}
		return str;
	}
    
    public static String getTimeDiffDay(Date date)
    {
    	long diff = date.getTime();
    	
    	String str = null;
    	
    	long today = DateUtils.getStartDate(new Date()).getTime();
    	long yesterday = today - 24 * 60 * 60 * 1000;
    	long threeDay = yesterday - 24 * 60 * 60 * 1000;
    	
    	
    	if (diff < threeDay)
    	{
    		str = getStringDate(date, "MM-dd HH:mm");
    	}
    	else if (diff < yesterday)
    	{
    		str = "前天  " +  getStringDate(date, "HH:mm");
    	}
    	else if (diff < today)
    	{
    		str = "昨天  " +  getStringDate(date, "HH:mm");
    	}
    	else
    	{
    		str = getStringDate(date, "HH:mm");
    	}
    	return str;
    }
    /** 
     * 对字符串处理:将指定位置到指定位置的字符以星号代替 
     *  
     * @param content 
     *            传入的字符串 
     * @param begin 
     *            开始位置 
     * @param end 
     *            结束位置 
     * @return 
     */  
    public static String getStarString(String content, int begin, int end) {  
  
        if (begin >= content.length() || begin < 0) {  
            return content;  
        }  
        if (end >= content.length() || end < 0) {  
            return content;  
        }  
        if (begin >= end) {  
            return content;  
        }  
        String starStr = "";  
        for (int i = begin; i < end; i++) {  
            starStr = starStr + "*";  
        }  
        return content.substring(0, begin) + starStr + content.substring(end, content.length());  
    }  
    
    /**
     * 获取从今天开始第 amount 个月后的今天
     * 
     * @param amount
     *            可正、可负
     * @return
     */
    public static Date getFromAmountMonthToDay(Date date, int amount)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getStartDate(cal.getTime());
    }
}
