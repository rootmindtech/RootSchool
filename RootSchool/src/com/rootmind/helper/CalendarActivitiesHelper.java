package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.CalendarActivitiesWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class CalendarActivitiesHelper extends Helper {
	
	//------------- Start insertCalendarActivities------------------
	
	public AbstractWrapper insertCalendarActivities(UsersWrapper usersProfileWrapper,CalendarActivitiesWrapper calendarActivitiesWrapper)throws Exception {
		
			Connection con = null;
			ResultSet resultSet = null;
	
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql=null;
			
			//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
			
			
		
			try {
					con = getConnection();
					
					sql=" INSERT INTO CalendarActivities(CalendarRefNo,HostelID,ActivityCategory,StartDate,EndDate,Description,RecordStatus, "
							+ " MakerID,MakerDateTime, SchoolID) Values (?,?,?,?,?,?,?,?,?,?)";
					
					System.out.println("sql " + sql);
					
					PreparedStatement pstmt = con.prepareStatement(sql);
					
					calendarActivitiesWrapper.calendarRefNo=generateCalendarRefNo(usersProfileWrapper.schoolID);
					
					pstmt.setString(1,Utility.trim(calendarActivitiesWrapper.calendarRefNo));
					
					pstmt.setString(2,Utility.trim(calendarActivitiesWrapper.hostelID));
					pstmt.setString(3,Utility.trim(calendarActivitiesWrapper.activityCategory));
					pstmt.setDate(4,Utility.getDate(calendarActivitiesWrapper.startDate)); 
					pstmt.setDate(5,Utility.getDate(calendarActivitiesWrapper.endDate)); 
					pstmt.setString(6,Utility.trim(calendarActivitiesWrapper.desc));
					pstmt.setString(7,Utility.trim(calendarActivitiesWrapper.status));
					pstmt.setString(8,Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(9,Utility.getCurrentTime());
					pstmt.setString(10,Utility.trim(usersProfileWrapper.schoolID));
	
				
					pstmt.executeUpdate();
					pstmt.close();
					
					
					calendarActivitiesWrapper.recordFound=true;
		
				
					dataArrayWrapper.calendarActivitiesWrapper=new CalendarActivitiesWrapper[1];
					dataArrayWrapper.calendarActivitiesWrapper[0]=calendarActivitiesWrapper;
					
					dataArrayWrapper.recordFound=true;
					
					System.out.println("Successfully inserted into CalendarActivities");
					
				
			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			}
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				try
				{
					releaseConnection(resultSet, con);
				} 
				catch (SQLException se)
				{
					se.printStackTrace();
					throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
				}
			}
	
			return dataArrayWrapper;
	}
	//------------- End insertCalendarActivities------------------
	
	
	//------------- Start updateCalendarActivities------------------
	public AbstractWrapper updateCalendarActivities(UsersWrapper usersProfileWrapper,CalendarActivitiesWrapper calendarActivitiesWrapper)throws Exception {
		
				Connection con = null;
				ResultSet resultSet = null;
		
				DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				//String sql=null;
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator(',');
//				formatter.applyPattern("###,###,###,##0.00");
//				formatter.setDecimalFormatSymbols(symbols);
				PreparedStatement pstmt=null;
				
				
				try {
						con = getConnection();
						
						pstmt = con.prepareStatement("SELECT CalendarRefNo FROM CalendarActivities WHERE CalendarRefNo=? and SchoolID=?");
						
						System.out.println("CalendarActivities  is" + calendarActivitiesWrapper.calendarRefNo);
						
						pstmt.setString(1,calendarActivitiesWrapper.calendarRefNo);
						pstmt.setString(2,usersProfileWrapper.schoolID);
						
						resultSet = pstmt.executeQuery();
						if (!resultSet.next()) 
						{
							resultSet.close();
							pstmt.close();
							dataArrayWrapper=(DataArrayWrapper)insertCalendarActivities(usersProfileWrapper,calendarActivitiesWrapper);
						}
						else
						{
						
							resultSet.close();
							pstmt.close();
						
							pstmt = con.prepareStatement("UPDATE CalendarActivities SET ActivityCategory=?,StartDate=?,EndDate=?,Description=?,RecordStatus=?,ModifierID=?,ModifierDateTime=? WHERE CalendarRefNo=? and SchoolID=?");
							
							pstmt.setString(1,Utility.trim(calendarActivitiesWrapper.activityCategory));  
							pstmt.setDate(2,Utility.getDate(calendarActivitiesWrapper.startDate));  
							pstmt.setDate(3,Utility.getDate(calendarActivitiesWrapper.endDate));
							pstmt.setString(4,Utility.trim(calendarActivitiesWrapper.desc));
							pstmt.setString(5,Utility.trim(calendarActivitiesWrapper.status));
							pstmt.setString(6,Utility.trim(usersProfileWrapper.userid));
							pstmt.setTimestamp(7,Utility.getCurrentTime());
							pstmt.setString(8,Utility.trim(calendarActivitiesWrapper.calendarRefNo));
							pstmt.setString(9,Utility.trim(usersProfileWrapper.schoolID));
						
							pstmt.executeUpdate();
							pstmt.close();
							
							calendarActivitiesWrapper.recordFound=true;
							
						
						
							dataArrayWrapper.calendarActivitiesWrapper=new CalendarActivitiesWrapper[1];
							dataArrayWrapper.calendarActivitiesWrapper[0]=calendarActivitiesWrapper;
							dataArrayWrapper.recordFound=true;
							
							System.out.println("Successfully Calendar Activities Updated");
						}
						
				} catch (SQLException se) {
					se.printStackTrace();
					throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
				} catch (NamingException ne) {
					ne.printStackTrace();
					throw new NamingException(ne.getMessage());
				}
				 catch (Exception ex) {
					ex.printStackTrace();
					throw new Exception(ex.getMessage());
				}
				finally
				{
					try
					{
						releaseConnection(resultSet, con);
					} 
					catch (SQLException se)
					{
						se.printStackTrace();
						throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
					}
				}
		
				return dataArrayWrapper;
	}
	//------------- End updateCalendarActivities------------------
	
	
	
	//------------- Start fetchCalendarActivities------------------
	public AbstractWrapper fetchCalendarActivities(UsersWrapper usersProfileWrapper,CalendarActivitiesWrapper calendarActivitiesWrapper)throws Exception {

			Connection con = null;
			ResultSet resultSet = null;
			
			DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	
			System.out.println("fetchCalendarActivities HostelID" + calendarActivitiesWrapper.hostelID);
			
			Vector<Object> vector = new Vector<Object>();
			PreparedStatement pstmt =null;
			
			
//			int queueMaxRecords=0;
//			int mQueueMaxRecords=0;
//			
			String sql=null;
			int n=1;
			
			try {
					PopoverHelper popoverHelper=new PopoverHelper();
				
					con = getConnection();
					
					//----Queue Max Records--
					
//					//-------- Fetch QueueMaxRecords  Parameter
//					pstmt = con.prepareStatement("SELECT QueueMaxRecords,MQueueMaxRecords FROM MST_Parameter");
//					
//					resultSet = pstmt.executeQuery();
//					if (resultSet.next()) 
//					{	
//						queueMaxRecords=resultSet.getInt("QueueMaxRecords");
//						mQueueMaxRecords=resultSet.getInt("MQueueMaxRecords");
//						System.out.println(" queueMaxRecords " + queueMaxRecords);
//						
//					}
//					
//					resultSet.close();
//					pstmt.close();
					
					//-------
					
					
					
					sql="SELECT CalendarRefNo, HostelID, ActivityCategory, StartDate, EndDate, Description,"
							+ " RecordStatus,MakerID,MakerDateTime,ModifierID,ModifierDateTime FROM CalendarActivities WHERE SchoolID=?";
					
					
					if(!Utility.isEmpty(calendarActivitiesWrapper.calendarRefNo))
					{
						sql=sql+ " AND CalendarRefNo=?";
					}	
					
					if(!Utility.isEmpty(calendarActivitiesWrapper.hostelID))
					{
						sql=sql+" AND HostelID = ? " ;
					}
					else if(!Utility.isEmpty(calendarActivitiesWrapper.startDate))
					{
		 				
						sql=sql + " AND StartDate >= ?";
							
					}
					
					else if(!Utility.isEmpty(calendarActivitiesWrapper.endDate))
					{
						
						sql=sql +" AND EndDate <= ?";
						
					}
					
//					if(!Utility.isEmpty(calendarActivitiesWrapper.searchCode) && calendarActivitiesWrapper.searchCode.equals("SUPERVISOR_MAINT_QUEUE")){
//						
//						sql=sql + " AND ROWNUM<= "+queueMaxRecords+" ORDER BY MakerDateTime DESC " ;
//					}
//					if(calendarActivitiesWrapper.searchCode !=null && calendarActivitiesWrapper.searchCode.equals("STUDENT_DEVICE_QUEUE")){
//						
//						//MYSQL
//						//sql=sql + " ORDER BY MakerDateTime DESC LIMIT " + mQueueMaxRecords;
//						
//						//Oracle
//						sql=sql + " AND ROWNUM<= "+mQueueMaxRecords+" ORDER BY MakerDateTime DESC " ;
//						
//					}
//					
//					if(calendarActivitiesWrapper.searchCode !=null && calendarActivitiesWrapper.searchCode.equals("CALENDAR_ACTIVITIES_SEARCH"))
//					{
//						//MYSQL
//						//sql=sql + " ORDER BY MakerDateTime DESC LIMIT " + queueMaxRecords;
//						
//						//Oracle
//						sql=sql + " AND ROWNUM<= "+queueMaxRecords+" ORDER BY MakerDateTime DESC " ;
//					}
					
					
					sql = sql + " ORDER BY MakerDateTime DESC ";
					
					System.out.println("final SQL "+sql);
					pstmt = con.prepareStatement(sql);
					

					pstmt.setString(n,Utility.trim(usersProfileWrapper.schoolID));

					
					if(!Utility.isEmpty(calendarActivitiesWrapper.calendarRefNo))
					{
						pstmt.setString(++n,Utility.trim(calendarActivitiesWrapper.calendarRefNo));
					}

					if(!Utility.isEmpty(calendarActivitiesWrapper.hostelID ))
					{
						pstmt.setString(++n,Utility.trim(calendarActivitiesWrapper.hostelID));
					}
					
					if(!Utility.isEmpty(calendarActivitiesWrapper.startDate)){
						
						pstmt.setDate(++n, Utility.getDate(calendarActivitiesWrapper.startDate.trim()));
					}
					
					if(!Utility.isEmpty(calendarActivitiesWrapper.endDate)){
						
						pstmt.setDate(++n, Utility.getDate(calendarActivitiesWrapper.endDate.trim()));
					}
					
			
					resultSet = pstmt.executeQuery();
					while (resultSet.next()) 
					{
						calendarActivitiesWrapper=new CalendarActivitiesWrapper();
						
						calendarActivitiesWrapper.calendarRefNo=Utility.trim(resultSet.getString("CalendarRefNo"));
						calendarActivitiesWrapper.hostelID=Utility.trim(resultSet.getString("HostelID"));
						calendarActivitiesWrapper.activityCategory=Utility.trim(resultSet.getString("ActivityCategory"));
						calendarActivitiesWrapper.startDate=Utility.setDate(resultSet.getString("StartDate"));
						calendarActivitiesWrapper.endDate=Utility.setDate(resultSet.getString("EndDate"));
						calendarActivitiesWrapper.desc=Utility.trim(resultSet.getString("Description"));
						calendarActivitiesWrapper.status=Utility.trim(resultSet.getString("RecordStatus"));
						calendarActivitiesWrapper.makerID=Utility.trim(resultSet.getString("MakerID"));
						calendarActivitiesWrapper.makerDateTime=Utility.setDate(resultSet.getString("MakerDateTime"));
						calendarActivitiesWrapper.modifierID=Utility.trim(resultSet.getString("ModifierID"));
						calendarActivitiesWrapper.modifierDateTime=Utility.setDate(resultSet.getString("ModifierDateTime"));
						calendarActivitiesWrapper.statusValue=popoverHelper.fetchPopoverDesc(calendarActivitiesWrapper.status, "MST_Status", usersProfileWrapper.schoolID);
						
						calendarActivitiesWrapper.activityCategoryValue=popoverHelper.fetchPopoverDesc(calendarActivitiesWrapper.activityCategory, "MST_ActivityCategory", usersProfileWrapper.schoolID);
						calendarActivitiesWrapper.hostelIDValue=popoverHelper.fetchPopoverDesc(calendarActivitiesWrapper.hostelID, "MST_Hostel", usersProfileWrapper.schoolID);
						calendarActivitiesWrapper.startDateAMPM=Utility.setDateAMPM(resultSet.getString("StartDate"));
						calendarActivitiesWrapper.endDateAMPM=Utility.setDateAMPM(resultSet.getString("EndDate"));
						
						calendarActivitiesWrapper.makerDateTimeAMPM=Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
						
						calendarActivitiesWrapper.recordFound=true;
	
						System.out.println("CalendarActivities Details fetch successful");
		
						vector.addElement(calendarActivitiesWrapper);
		
					}
					
					if (vector.size()>0)
					{
						dataArrayWrapper.calendarActivitiesWrapper = new CalendarActivitiesWrapper[vector.size()];
						vector.copyInto(dataArrayWrapper.calendarActivitiesWrapper);
						dataArrayWrapper.recordFound=true;
			
						System.out.println("total trn. in fetch " + vector.size());
			
					}
					else{
						dataArrayWrapper.calendarActivitiesWrapper = new CalendarActivitiesWrapper[1];
						calendarActivitiesWrapper.recordFound = false;
						dataArrayWrapper.calendarActivitiesWrapper[0]=calendarActivitiesWrapper;
						dataArrayWrapper.recordFound=true;
					}
					if (resultSet!=null)  resultSet.close();
					pstmt.close();
				
			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			}
			 catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			finally
			{
				try
				{
					releaseConnection(resultSet, con);
				} 
				catch (SQLException se)
				{
					se.printStackTrace();
					throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
				}
			}
	
			return dataArrayWrapper;
	}
	
	//------------- End fetchCalendarActivities------------------
	
	//-----------------Generate generate Calendar RefNo-------------------------------
		public String generateCalendarRefNo(String schoolID)throws Exception 
		{
			
			Connection con = null;
			ResultSet resultSet = null;

			// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql = null;

			SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);

			int messageID = 0;
			String finalMessageID = null;

			try {
				con = getConnection();

				sql = "SELECT MessageID from MST_Parameter where SchoolID=?";

				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(1, schoolID);
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					messageID = resultSet.getInt("MessageID");
					System.out.println("MessageID" + messageID);

				}

				resultSet.close();
				pstmt.close();

				if (messageID == 0) {
					messageID = 1;

				} else {

					messageID = messageID + 1;
				}

				sql = "UPDATE MST_Parameter set MessageID=? where SchoolID=?";

				System.out.println("sql " + sql);

				pstmt = con.prepareStatement(sql);

				pstmt.setInt(1, messageID);
				pstmt.setString(2, schoolID);

				pstmt.executeUpdate();
				pstmt.close();

				int paddingSize = 5;// 6-String.valueOf(messageID).length();

				// System.out.println("Savings Account " + studentProfileWrapper.accountType);

				// System.out.println("Savings Account " +
				// studentProfileWrapper.accountType.substring(0,2));

				finalMessageID = "CAL" + dmyFormat.format(new java.util.Date()).toUpperCase()
						+ String.format("%0" + paddingSize + "d", messageID);

				// studentProfileWrapper.recordFound=true;

				// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
				// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
				// dataArrayWrapper.recordFound=true;

				System.out.println("Successfully generated school messageID " + finalMessageID);

			} catch (SQLException se) {
				se.printStackTrace();
				throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
			} catch (NamingException ne) {
				ne.printStackTrace();
				throw new NamingException(ne.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			} finally {
				try {
					releaseConnection(resultSet, con);
				} catch (SQLException se) {
					se.printStackTrace();
					throw new Exception(se.getSQLState() + " ; " + se.getMessage());
				}
			}

			return finalMessageID;
		}
		
		//-----------------End generateCalendarRefNo ---------------------------
	

}

   
