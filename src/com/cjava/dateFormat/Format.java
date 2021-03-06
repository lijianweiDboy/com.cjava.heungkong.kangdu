package com.cjava.dateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author win10
 *
 */
public class Format {


	/**
	 * @param dateParseString  目前的字符串时间格式
	 * @param dateFormatString 需要装换成的格式
	 * @param dateString 日期字符串
	 * @deprecated 时间格式种类:2018-07-23T21:00:44.653+08:00:yyyy-MM-dd'T'HH:mm:ss.SSSXXX,
	 * 2018-07-23:yyyy-MM-dd 等
	 * @return
	 */
	public static String formatDate(String dateParseString,String dateFormatString,String dateString) {
		SimpleDateFormat dateParse=new SimpleDateFormat(dateParseString);
		SimpleDateFormat dateFormat=new SimpleDateFormat(dateFormatString);
		String d = null;
		try {
			d = dateFormat.format(dateParse.parse(dateParseString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;	
	}
	
	/**
	 * @param dateParseString  目前的字符串时间格式
	 * @param dateFormatString 需要装换成的格式
	 * @param dateString 日期字符串
	 * @deprecated 时间格式种类:2018-07-23T21:00:44.653+08:00:yyyy-MM-dd'T'HH:mm:ss.SSSXXX,
	 * 2018-07-23:yyyy-MM-dd 等
	 * @return
	 */
    public static Date formatDateTime(String time) throws ParseException {
   	 
    	String formatString="";
  		if(time.contains(".")) {
  			formatString = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  			SimpleDateFormat format = new SimpleDateFormat(formatString);
  			
            return format.parse(time);
  			
  		}else {
  			formatString = "yyyy-MM-dd'T'HH:mm:ssX";
  			SimpleDateFormat format = new SimpleDateFormat(formatString);
  			return format.parse(time);
  		}
}
}
