package com.rootmind.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;






import com.rootmind.helper.StudentAcademicsHelper;
import com.rootmind.helper.TeacherSubjectsHelper;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ErrorWrapper;
import com.rootmind.wrapper.StudentAcademicsWrapper;
import com.rootmind.wrapper.TeacherSubjectsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentAcademicsController {
	
	
	public AbstractWrapper validate(UsersWrapper usersProfileWrapper,StudentAcademicsWrapper studentAcademicsWrapper)throws Exception {
			
			
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd/MM/yyyy");
		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
			
			//Date currentDate=new Date();
			//String strCurrentDate=dmyFormat.format(currentDate);
	       
			Vector<Object> vector = new Vector<Object>();
			ErrorWrapper errorWrapper=null;
			
			try {
				
	
					//call helper
					StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper(); 
					 
					TeacherSubjectsHelper teacherSubjectsHelper=new TeacherSubjectsHelper();
					TeacherSubjectsWrapper teacherSubjectsWrapper=new TeacherSubjectsWrapper();
					
					teacherSubjectsWrapper.staffUserID=usersProfileWrapper.userid;
					teacherSubjectsWrapper.gradeID=studentAcademicsWrapper.gradeID;
					teacherSubjectsWrapper.subjectID=studentAcademicsWrapper.subjectID;
			        
		        	System.out.println("Insert Student profile controller  ");
	        		boolean recordFound= teacherSubjectsHelper.findTeacherSubjects(usersProfileWrapper, teacherSubjectsWrapper);
	        		 
					if(recordFound==false)
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
						studentAcademicsWrapper.recordFound=false;
						System.out.println("total trn. in fetch " + vector.size());
		
					}
					if((vector.size()>0)&&(studentAcademicsWrapper.recordFound==false))
					{
						
						System.out.println("Vector Size > 0 And RecordFound false " );
			        	dataArrayWrapper.studentAcademicsWrapper=new StudentAcademicsWrapper[1];
						dataArrayWrapper.studentAcademicsWrapper[0]=studentAcademicsWrapper;
						dataArrayWrapper.recordFound=true;
				
						
					}
					else
					{
	
			        	System.out.println("Insert Student profile controller  ");
		        		dataArrayWrapper = (DataArrayWrapper)studentAcademicsHelper.updateStudentAcademics(usersProfileWrapper, studentAcademicsWrapper);
	
					}
					
					
					
				
			} 
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			
	
			return dataArrayWrapper;
		}




}
