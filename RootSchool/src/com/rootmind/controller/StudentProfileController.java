package com.rootmind.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.Locale;
import java.util.Vector;


import com.rootmind.helper.StudentProfileHelper;
import com.rootmind.helper.UsersHelper;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ErrorWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentProfileController {
	
public AbstractWrapper validate(UsersWrapper usersProfileWrapper,StudentProfileWrapper studentProfileWrapper)throws Exception {
		
		
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
				StudentProfileHelper studentProfileHelper=new StudentProfileHelper(); 
				
		        
	        	System.out.println("Insert Student profile controller  ");
	        	UsersHelper usersHelper = new UsersHelper();

        		 boolean useridFound= usersHelper.fetchUserid(studentProfileWrapper.userid, usersProfileWrapper.schoolID, studentProfileWrapper.email);
        		 
 
					
				if(useridFound==true)
				{
						errorWrapper = new ErrorWrapper();
						errorWrapper.errorCode="ERR001";
						errorWrapper.errorDesc="Userid already exists, please assign new Userid";
						vector.add(errorWrapper);
				}
				if (vector.size()>0)
				{
					
					dataArrayWrapper.errorWrapper = new ErrorWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.errorWrapper);
					studentProfileWrapper.recordFound=false;
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				if((vector.size()>0)&&(studentProfileWrapper.recordFound==false))
				{
					
					System.out.println("Vector Size > 0 And RecordFound false " );
		        	dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
					dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
					dataArrayWrapper.recordFound=true;
			
					
				}
				else
				{

		        	System.out.println("Insert Student profile controller  ");
	        		dataArrayWrapper = (DataArrayWrapper)studentProfileHelper.insertStudentProfile(usersProfileWrapper,studentProfileWrapper);

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
