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

public class StudentMarksController {
	
	
	
	public AbstractWrapper validate(UsersWrapper usersProfileWrapper,StudentAcademicsWrapper[] studentAcademicsWrapperArray)throws Exception {
			
			
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
			boolean recordFound=false;
			
			try {
					
	
					//call helper
					StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper(); 
					 
					if(studentAcademicsWrapperArray !=null && studentAcademicsWrapperArray.length>0){
							TeacherSubjectsHelper teacherSubjectsHelper=new TeacherSubjectsHelper();
							TeacherSubjectsWrapper teacherSubjectsWrapper=new TeacherSubjectsWrapper();
							
							teacherSubjectsWrapper.staffUserID=usersProfileWrapper.userid;
							teacherSubjectsWrapper.gradeID=studentAcademicsWrapperArray[0].gradeID;
							teacherSubjectsWrapper.subjectID=studentAcademicsWrapperArray[0].subjectID;
							
					        
				        	System.out.println("Insert Student profile controller  ");
			        		recordFound= teacherSubjectsHelper.findTeacherSubjects(usersProfileWrapper, teacherSubjectsWrapper);
	        		
					}
	        		 
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
						StudentAcademicsWrapper  studentAcademicsWrapper=new StudentAcademicsWrapper();
						studentAcademicsWrapper.recordFound=false;
						System.out.println("total trn. in fetch " + vector.size());

						
						System.out.println("Vector Size > 0 And RecordFound false " );
			        	dataArrayWrapper.studentAcademicsWrapper=new StudentAcademicsWrapper[1];
						dataArrayWrapper.studentAcademicsWrapper[0]=studentAcademicsWrapper;
						dataArrayWrapper.recordFound=true;
				
						
					}
					else
					{
	
			        	System.out.println("Insert Student profile controller  ");
		        		dataArrayWrapper = (DataArrayWrapper)studentAcademicsHelper.updateStudentMarks(usersProfileWrapper, studentAcademicsWrapperArray);
	
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
