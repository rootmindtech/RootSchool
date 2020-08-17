package com.rootmind.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;



import com.rootmind.helper.TeachersProfileHelper;
import com.rootmind.helper.UsersHelper;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ErrorWrapper;
import com.rootmind.wrapper.TeachersProfileWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TeachersProfileController {
	
public AbstractWrapper validate(UsersWrapper usersProfileWrapper,TeachersProfileWrapper teachersProfileWrapper)throws Exception {
		
		
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		//Date currentDate=new Date();
		//String strCurrentDate=dmyFormat.format(currentDate);
       
		Vector<Object> vector = new Vector<Object>();
		ErrorWrapper errorWrapper=null;
		
		try {
			

				//call helper
				TeachersProfileHelper teachersProfileHelper=new TeachersProfileHelper(); 
				
		        
	        	System.out.println("Insert teachers profile controller  ");
	        	
	        	UsersHelper usersHelper = new UsersHelper();
        		boolean useridFound= usersHelper.fetchUserid(teachersProfileWrapper.staffUserID, usersProfileWrapper.schoolID, teachersProfileWrapper.email);
					
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
					teachersProfileWrapper.recordFound=false;
					System.out.println("total trn. in fetch " + vector.size());
	
				}
				if((vector.size()>0)&&(teachersProfileWrapper.recordFound==false))
				{
					
					System.out.println("Vector Size > 0 And RecordFound false " );
		        	dataArrayWrapper.teachersProfileWrapper=new TeachersProfileWrapper[1];
					dataArrayWrapper.teachersProfileWrapper[0]=teachersProfileWrapper;
					dataArrayWrapper.recordFound=true;
			
					
				}
				else
				{

		        	System.out.println("Insert Teachers profile controller  ");
	        		dataArrayWrapper = (DataArrayWrapper)teachersProfileHelper.updateTeachersProfile(usersProfileWrapper, teachersProfileWrapper);

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
