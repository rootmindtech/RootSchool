package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;


import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.UsersWrapper;
import com.rootmind.wrapper.WeekdaySetupWrapper;

public class WeekdaySetupHelper extends Helper{
	
	
	//-----------------Start insertWeekdaySetup---------------------
	
			public AbstractWrapper insertWeekdaySetup(UsersWrapper usersProfileWrapper,WeekdaySetupWrapper[]  weekdaySetupWrapperArray)throws Exception {

				Connection con = null;
				ResultSet resultSet = null;
			

				DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				//String sql=null;
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
				symbols.setGroupingSeparator(',');
				formatter.applyPattern("###,###,###,##0.00");
				formatter.setDecimalFormatSymbols(symbols);
				
				PreparedStatement pstmt=null;
				
				String sql=null;
				
				System.out.println("updateWeekdaySetup Status");
				
				
				try {
							con = getConnection();
							
							
							pstmt=con.prepareStatement("DELETE FROM WeekdaySetup WHERE MonthName=?");
							
							System.out.println("month name "+weekdaySetupWrapperArray[0].month);
							pstmt.setString(1,Utility.trim(weekdaySetupWrapperArray[0].month));
							pstmt.executeUpdate();
							pstmt.close();
							
							for(int i=0;i<=weekdaySetupWrapperArray.length-1;i++)
							{
								
								sql="INSERT INTO WeekdaySetup(MonthName, Weekday, MakerID, MakerDateTime) Values(?,?,?,?)";
								
								System.out.println("sql " + sql);
								
								pstmt = con.prepareStatement(sql);
								
								//hostelInOutWrapper.inOutRefNo=generateInOutRefNo();
								 
								pstmt.setString(1,Utility.trim(weekdaySetupWrapperArray[i].month));
								pstmt.setString(2,Utility.trim(weekdaySetupWrapperArray[i].weekday));
								pstmt.setString(3,Utility.trim(usersProfileWrapper.userid));
								pstmt.setTimestamp(4,Utility.getCurrentTime()); // maker date time
								
								pstmt.executeUpdate();
								pstmt.close();
								
								weekdaySetupWrapperArray[i].recordFound=true;
							
							}
							
							dataArrayWrapper.weekdaySetupWrapper=weekdaySetupWrapperArray;
							dataArrayWrapper.recordFound=true;
					
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
			
			//-----------------End insertWeekdaySetup---------------------
			
			

			//-----------------Start fetchWeekdaySetup---------------------
			
			public AbstractWrapper fetchWeekdaySetup(UsersWrapper usersProfileWrapper)throws Exception {

					Connection con = null;
					ResultSet resultSet = null;
					
					DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
					WeekdaySetupWrapper  weekdaySetupWrapper=null;
					Vector<Object> vector = new Vector<Object>();
					
					try {
							PopoverHelper popoverHelper=new PopoverHelper();
							con = getConnection();
	
							PreparedStatement pstmt = con.prepareStatement("SELECT MonthName,Weekday FROM WeekdaySetup");
						
							
							resultSet = pstmt.executeQuery();
							
							while (resultSet.next()) 
							{
								
								weekdaySetupWrapper=new WeekdaySetupWrapper();
								
								
								weekdaySetupWrapper.month=Utility.trim(resultSet.getString("MonthName"));
								weekdaySetupWrapper.weekday=Utility.trim(resultSet.getString("Weekday"));
								 
								weekdaySetupWrapper.monthValue=popoverHelper.fetchPopoverDesc(weekdaySetupWrapper.month, "MST_Months", usersProfileWrapper.schoolID);
								weekdaySetupWrapper.weekdayValue=popoverHelper.fetchPopoverDesc(weekdaySetupWrapper.weekday, "MST_Days", usersProfileWrapper.schoolID);
								
								weekdaySetupWrapper.recordFound=true;
		
								
				
								vector.addElement(weekdaySetupWrapper);
				
							}
						
							if (vector.size()>0)
							{
								dataArrayWrapper.weekdaySetupWrapper = new WeekdaySetupWrapper[vector.size()];
								vector.copyInto(dataArrayWrapper.weekdaySetupWrapper);
								dataArrayWrapper.recordFound=true;
					
								System.out.println("total trn. in fetch " + vector.size());
								System.out.println("WeekdaySetup fetch successful");
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
			//-----------------End fetchWeekdaySetup---------------------

}
