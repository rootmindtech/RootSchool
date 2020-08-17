package com.rootmind.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import com.rootmind.helper.TeacherReportsHelper;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ErrorWrapper;
import com.rootmind.wrapper.TeacherReportsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TeacherReportsController {
	
	
public AbstractWrapper validate(UsersWrapper usersProfileWrapper,TeacherReportsWrapper teacherReportsWrapper)throws Exception {
		
		
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
	
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		
		//Date currentDate=new Date();
		//String strCurrentDate=dmyFormat.format(currentDate);
       
		Vector<Object> vector = new Vector<Object>();
		ErrorWrapper errorWrapper=null;
		
		try {
			

				//call helper
				TeacherReportsHelper teacherReportsHelper=new TeacherReportsHelper(); 
				
		        
	        	System.out.println("Insert teachers Reports controller  ");
	        	
	        	teacherReportsWrapper.staffUserID=usersProfileWrapper.userid;
	        	
	        	
        		boolean useridFound= teacherReportsHelper.findTeacherReports(teacherReportsWrapper);
        		 
 
					
				if(useridFound==false)
				{
						errorWrapper = new ErrorWrapper();
						errorWrapper.errorCode="ERR001";
						errorWrapper.errorDesc="You are not authorized to perform this activity";
						vector.add(errorWrapper);
				}
				if (vector.size()>0)
				{
					
					dataArrayWrapper.errorWrapper = new ErrorWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.errorWrapper);
					teacherReportsWrapper.recordFound=false;
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				if((vector.size()>0)&&(teacherReportsWrapper.recordFound==false))
				{
					
					System.out.println("Vector Size > 0 And RecordFound false " );
		        	dataArrayWrapper.teacherReportsWrapper=new TeacherReportsWrapper[1];
					dataArrayWrapper.teacherReportsWrapper[0]=teacherReportsWrapper;
					dataArrayWrapper.recordFound=true;
			
					
				}
				else
				{

		        	System.out.println("Insert Teachers Reports controller  ");
		        	
		        	teacherReportsWrapper.recordFound=true;
		        	dataArrayWrapper.teacherReportsWrapper=new TeacherReportsWrapper[1];
					dataArrayWrapper.teacherReportsWrapper[0]=teacherReportsWrapper;
					dataArrayWrapper.recordFound=true;
	        		//dataArrayWrapper = (DataArrayWrapper)teacherReportsHelper.updateTeacherReports(usersProfileWrapper, teacherReportsWrapper);

				}
				
				
				
			
		} 
		 catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		finally
		{
			/*try
			{
				
			} 
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}*/
		}

		return dataArrayWrapper;
	}



}
