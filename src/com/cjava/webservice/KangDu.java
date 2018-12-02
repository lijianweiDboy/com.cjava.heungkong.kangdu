package com.cjava.webservice;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tempuri.IHospitalInterfaceProxy;
import com.cjava.dbconnnect.DbConnect;
import com.cjava.dom4j.Dom4jTool;

/**
 * @author CJAVA
 * �����ӿ����ݴ���
 */
public class KangDu {
	public List<Element> sampleInfos;
	public List<Element> sampleResult;
	public List<Element> nodeResult; 
	public List<Element> CommonResultTable;
	public String HosBarcode = null;//杏林妙手体检编号
	public String SubItemCode = null;//项目编号
	public String ItemName = null;//项目名称
	public String TestResult =null;//康都检验结果
	public String UnitName =null;//单位
	public String ReferenceValue = null;//参考范围;
	public String SHR = null;//检验医生
	public String TestTime = null;//检验日期
	public String HLFlag = null;//高低标识
	public String CZY = null;//操作人
	public String insertsql = null;//SQL
	public String formatString="yyyy-MM-dd'T'HH:mm:ss.ssX";
	public SimpleDateFormat dateParse=new SimpleDateFormat(formatString);
	public SimpleDateFormat dateFormat=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	public SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	public static Connection conn=DbConnect.connectSQLSERVER("10.90.1.49:1433", "tj_xlms", "xlmsuser", "topsky");
	public static Statement sta =null;
    /**
     * @return ����WEBSERVSER����
     */
    public static IHospitalInterfaceProxy getInstance()
    { 
      try {
		sta=conn.createStatement();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  	
	  return new IHospitalInterfaceProxy();
	  
	}   
  
	/**
	 * @param date ���鱨��ʱ��εĿ�ʼʱ�� 
	 * @param date2 ���鱨��ʱ��εĽ���ʱ��
	 * @return 
	 */
	public void getSampleInfos(java.util.Date date,java.util.Date date2) {
		
		String xml = null;
		try {
			xml = this.getInstance().loadLIMSSampleInfosGetResult(sdf.format(date), sdf.format(date2), "J19149", "123456", "J19149");
			//System.out.println(xml);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = null;
		try {
			root = DocumentHelper.parseText(xml).getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sampleInfos=Dom4jTool.ListElement(root, "SampleInfo");
	}

	/**
	 * @return
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	public void getResult() throws SQLException, ParseException {
		 for(int i= 0;i<sampleInfos.size();i++) {
			 Element e = sampleInfos.get(i);
			 Element Barcode = e.element("Barcode");
			 Element SampleNo = e.element("SampleNo");
			 HosBarcode = e.element("HosBarcode").getStringValue();
			 if(HosBarcode==null)continue;
			 String xml = null;  
	            //�ڶ��ӿڻ�ȡ����ʼ
	            try {
					xml=this.getInstance().ILoadLIMSSampleSpecialResultAudited(Barcode.getStringValue(), SampleNo.getStringValue(), "J19149", "123456", "J19149");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	 
	            //System.out.println(xml);

	            Element root = null;
				try {
					root = DocumentHelper.parseText(xml).getRootElement();
				} catch (DocumentException e1) {
					 System.out.println(xml);
					 System.out.println("XMLת��ʧ��:"+Barcode.getStringValue());
				}//��ȡxml����
				if(root!=null){
	            //sampleResult=Dom4jTool.ListElement(root, "SampleResult");//��ȡSampleResult����Ԫ��NodeResult  ����˵������ڵ�
	            //nodeResult=Dom4jTool.ListElement(root, "NodeResult");//��ȡSampleResult����Ԫ��NodeResult ����˵������ڵ�
	            CommonResultTable=Dom4jTool.ListElement(root, "CommonResultTable");//��ȡSampleResult����Ԫ��NodeResult 
				}
				try {
				insertData(HosBarcode);
				}catch(NullPointerException e2){
					e2.printStackTrace();
					System.out.println("�����Ϊ�գ�");
				}
		 	}
	}
       public void insertData(String HosBarcode) throws SQLException, ParseException {        
           for(int z= 0;z<CommonResultTable.size();z++) {
      	   	 Element e3 = CommonResultTable.get(z);
      	   	TestResult= e3.element("TestResult").getStringValue();//都检验结果
	             if(TestResult!=null&&TestResult.length()>=1) {
	             SubItemCode = e3.element("SubItemCode").getStringValue();//康都项目代码
	             if(SubItemCode.equals("5300")) {SubItemCode="5336";}
	             ItemName = e3.element("ItemName").getStringValue();//康都项目名称
	             TestResult = e3.element("TestResult").getStringValue();//康都检验结果
	             UnitName = e3.element("UnitName").getStringValue();//单位
	             if(UnitName==null) {
	            	 e3.addElement("UnitName");
	            	 UnitName = "";
	             }
	             ReferenceValue = e3.element("ReferenceValue")!=null?e3.element("ReferenceValue").getStringValue():"";//参考范围;
	             ReferenceValue=(ReferenceValue.replaceAll("&lt;", "<"));
	             ReferenceValue=(ReferenceValue.replaceAll("&gt;", ">"));
	             SHR = e3.element("审核人姓名").getStringValue();//审核医生
	             TestTime = formatDateTime(e3.element("CreateDate").getStringValue());//检验日期
	             
	             HLFlag = e3.element("HLFlag") != null?e3.element("HLFlag").getStringValue():"";//�ߵͱ�ʶ
	             CZY = e3.element("检查人姓名").getStringValue();//检验医生
	             if(HLFlag==null) {
	            	 HLFlag="";
	             }

				insertsql= "INSERT INTO futian_user.tj_jyjgb (djlsh,xmbh,xmmc,JG,DW,CKFW,SHR,SHRQ,prompt,CZY) "
								+ "VALUES('"+HosBarcode+"','"+SubItemCode+"','"+ItemName+"'"
								+ ",'"+TestResult+"','"+UnitName+"','"+ReferenceValue+"','"+SHR+"',"
										+ "'"+TestTime+"','"+HLFlag+"',"
												+ "'"+CZY+"')";	
	             
				System.out.println("CommonResultTable:"+insertsql);
	             if(HosBarcode.equals("1811240001")) {
	            	 System.out.println("CommonResultTable:"+insertsql);
	 	            }
	             //sta=conn.createStatement();
	            // sta.execute("delete futian_user.tj_jyjgb where djlsh='"+HosBarcode+"' and xmbh='"+SubItemCode.getStringValue()+"'");
	             //sta.execute(insertsql);
	            }
	            }
       }
       
       /*�ξ�����Ե������ӿ��Ƿ����
        * public void getTestxml() throws RemoteException {
    	   String xml =this.getInstance().ILoadLIMSSampleResultAudited("1811110519", "", "", "");
    	                     
    	   System.out.println(xml);
    	   
       }
       */
       public static void main(String[] args) throws ParseException {
           String time1="2018-11-12T13:28:47+08:00";
           String time2="2018-11-23T17:59:36.8+08:00";
           
		     new KangDu().formatDateTime(time1);
		     new KangDu().formatDateTime(time2);
    }
    
      public String formatDateTime(String time) throws ParseException {
    	 
    		if(time.contains(".")) {
    			formatString = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    			
    		}else {
    			formatString = "yyyy-MM-dd'T'HH:mm:ssX";
    		}
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			return dateFormat.format(format.parse(time));
  }
}