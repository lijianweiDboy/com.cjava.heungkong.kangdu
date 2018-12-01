package com.cjava.test;



import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import com.cjava.webservice.KangDu;
public class mytest {
	//获取10天内数据到杏林妙手临时表，2018-12-1修改
   public static void main(String[] args) throws SQLException, ParseException { 
	KangDu kd=new KangDu();
	kd.getSampleInfos(new Date(new Date().getTime() - 10*24*3600*1000L),new Date());
	kd.getResult();
}
}
