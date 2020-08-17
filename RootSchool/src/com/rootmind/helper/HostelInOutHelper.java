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
import com.rootmind.wrapper.HostelInOutWrapper;


import com.rootmind.wrapper.UsersWrapper;

public class HostelInOutHelper extends Helper{
	
	//-----------------Start insertHostelInOut---------------------
	
		public AbstractWrapper insertHostelInOut(UsersWrapper usersProfileWrapper,HostelInOutWrapper  hostelInOutWrapper)throws Exception {
				
				Connection con = null;
				ResultSet resultSet = null;
		
				DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				String sql=null;
				//String countryCode=null;
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator(',');
//				formatter.applyPattern("###,###,###,##0.00");
//				formatter.setDecimalFormatSymbols(symbols);
				
				PreparedStatement pstmt=null;
				
				
				try {
						con = getConnection();
						
							
							if(hostelInOutWrapper.inOutMode.equals("IN"))
							{
								
								sql=" INSERT INTO HostelInOut(InOutRefNo, HostelID, StudentID, InOutMode, InDateTime,InOutTime, MakerID, MakerDateTime, SchoolID) "
									+ "Values (?,?,?,?,?,?,?,?,?)";
							}
							if(hostelInOutWrapper.inOutMode.equals("OUT"))
							{
								
								sql=" INSERT INTO HostelInOut(InOutRefNo, HostelID, StudentID, InOutMode, OutDateTime,InOutTime, MakerID, MakerDateTime, SchoolID) "
									+ "Values (?,?,?,?,?,?,?,?,?)";
							}
							
							System.out.println("sql " + sql);
							
							pstmt = con.prepareStatement(sql);
							
							hostelInOutWrapper.inOutRefNo=generateInOutRefNo(usersProfileWrapper.schoolID);
							 
							pstmt.setString(1,Utility.trim(hostelInOutWrapper.inOutRefNo));
							pstmt.setString(2,Utility.trim(hostelInOutWrapper.hostelID));
							pstmt.setString(3,Utility.trim(hostelInOutWrapper.studentID));
							pstmt.setString(4,Utility.trim(hostelInOutWrapper.inOutMode));
							if(hostelInOutWrapper.inOutMode.equals("IN"))
							{
								pstmt.setDate(5,Utility.getDate(hostelInOutWrapper.inDateTime));
							}
							if(hostelInOutWrapper.inOutMode.equals("OUT"))
							{
								pstmt.setDate(5,Utility.getDate(hostelInOutWrapper.outDateTime));
							}
							pstmt.setString(6,Utility.trim(hostelInOutWrapper.inOutTime));
							pstmt.setString(7,Utility.trim(usersProfileWrapper.userid));
							pstmt.setTimestamp(8,Utility.getCurrentTime()); // maker date time
							pstmt.setString(9,Utility.trim(usersProfileWrapper.schoolID));
							
							pstmt.executeUpdate();
							pstmt.close();
							
							hostelInOutWrapper.recordFound=true;
					
						
						dataArrayWrapper.hostelInOutWrapper=new HostelInOutWrapper[1];
						dataArrayWrapper.hostelInOutWrapper[0]=hostelInOutWrapper;
						dataArrayWrapper.recordFound=true;
						
						System.out.println("Successfully inserted into HostelInOut");
						
						
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
			
			//-----------------End insertHostelInOut---------------------
		
		
		
		//-----------------Start updateHostelInOut---------------------
		public AbstractWrapper updateHostelInOut(UsersWrapper usersProfileWrapper, HostelInOutWrapper[] hostelInOutWrapperArray)throws Exception {
			
				Connection con = null;
				ResultSet resultSet = null;
		
				DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
				
				//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator(',');
//				formatter.applyPattern("###,###,###,##0.00");
//				formatter.setDecimalFormatSymbols(symbols);
		
				PreparedStatement pstmt=null;
				
				String sql=null;
				
				try {
						con = getConnection();
						
						
						for(int i=0;i<=hostelInOutWrapperArray.length-1;i++)
						{
							
							//check studentID available or not in users table;
							//pstmt = con.prepareStatement("SELECT StudentID FROM Users WHERE StudentID=?");

							System.out.println("HostelInOut studentid is " + hostelInOutWrapperArray[i].inOutRefNo);

							
//							pstmt = con.prepareStatement("SELECT STUDENT_ID from STUDENT_HOSTEL WHERE STUDENT_ID=?");
//							
//							pstmt.setString(1,hostelInOutWrapperArray[i].studentID);
//							
//							resultSet = pstmt.executeQuery();
//							
//							if(resultSet.next()){
//								
//								
//								resultSet.close();
//								pstmt.close();
								
								hostelInOutWrapperArray[i].studentIDFound=true;
								
							
								pstmt = con.prepareStatement("SELECT InOutRefNo FROM HostelInOut WHERE InOutRefNo=? and SchoolID=?");
								
								System.out.println("HostelInOut  is " + hostelInOutWrapperArray[i].inOutRefNo);
								
								pstmt.setString(1,hostelInOutWrapperArray[i].inOutRefNo); //--it may null
								pstmt.setString(2,usersProfileWrapper.schoolID); //--it may null
								
								
								resultSet = pstmt.executeQuery();
								
								if (!resultSet.next()) 
								{
									resultSet.close();
									pstmt.close();
									dataArrayWrapper=(DataArrayWrapper)insertHostelInOut(usersProfileWrapper,hostelInOutWrapperArray[i]);
								}
								else
								{
									resultSet.close();
									pstmt.close();
									
									
									if(hostelInOutWrapperArray[i].inOutMode.equals("IN"))
									{
										sql="UPDATE HostelInOut SET HostelID=?, StudentID=?, InOutMode=?, InDateTime=?, InOutTime=?, ModifierID=?,"
									 		+ "  ModifierDateTime=? WHERE InOutRefNo=? and SchoolID=?";
									}
									
									if(hostelInOutWrapperArray[i].inOutMode.equals("OUT"))
									{
										sql="UPDATE HostelInOut SET HostelID=?, StudentID=?, InOutMode=?, OutDateTime=?,  InOutTime=?, ModifierID=?,"
									 		+ "  ModifierDateTime=? WHERE InOutRefNo=? and SchoolID=?";
									}
									
									
									pstmt = con.prepareStatement(sql);
									 
									
									
									pstmt.setString(1,Utility.trim(hostelInOutWrapperArray[i].hostelID));
									pstmt.setString(2,Utility.trim(hostelInOutWrapperArray[i].studentID));
									pstmt.setString(3,Utility.trim(hostelInOutWrapperArray[i].inOutMode));
									
									if(hostelInOutWrapperArray[i].inOutMode.equals("IN"))
									{
										System.out.println("inDateTime "+hostelInOutWrapperArray[i].inDateTime);
										System.out.println("inDateTime getDate "+Utility.getDate(hostelInOutWrapperArray[i].inDateTime));
										
										pstmt.setDate(4,Utility.getDate(hostelInOutWrapperArray[i].inDateTime));
									}
									if(hostelInOutWrapperArray[i].inOutMode.equals("OUT"))
									{
										pstmt.setDate(4,Utility.getDate(hostelInOutWrapperArray[i].outDateTime));
									}
									pstmt.setString(5,Utility.trim(hostelInOutWrapperArray[i].inOutTime));  
									pstmt.setString(6,Utility.trim(usersProfileWrapper.userid));   
									pstmt.setTimestamp(7,Utility.getCurrentTime());  // modifier date time
									pstmt.setString(8,Utility.trim(hostelInOutWrapperArray[i].inOutRefNo));
									pstmt.setString(9,Utility.trim(usersProfileWrapper.schoolID));   
									
									pstmt.executeUpdate();
									pstmt.close();
		
									hostelInOutWrapperArray[i].recordFound=true;
									
								}	
								
									
//							}//if end
//							else{
//								resultSet.close();
//								pstmt.close();
//								
//								System.out.println("HostelInOut studentIdfound is " + hostelInOutWrapperArray[i].studentIDFound);
//
//								
//								hostelInOutWrapperArray[i].studentIDFound=false;
//								
//								
//							}
							
						}
						
						System.out.println("Successfully HostelInOut Updated");
						dataArrayWrapper.hostelInOutWrapper=hostelInOutWrapperArray;
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
		//-----------------End updateHostelInOut---------------------
		

		
		
		//-----------------Start fetchHostelInOut---------------------
		
		public AbstractWrapper fetchHostelInOut(UsersWrapper usersProfileWrapper, HostelInOutWrapper hostelInOutWrapper)throws Exception {

				Connection con = null;
				ResultSet resultSet = null;
				
				DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
				
				Vector<Object> vector = new Vector<Object>();
				String sql=null;
				
				try {
						PopoverHelper popoverHelper=new PopoverHelper();
						con = getConnection();

						sql = "SELECT InOutRefNo, HostelID, StudentID, InOutMode, InDateTime, OutDateTime, InOutTime,  MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM HostelInOut where SchoolID=?";

						if(!Utility.isEmpty(hostelInOutWrapper.inOutRefNo))
						{
							sql = sql + " and InOutRefNo=?";
						}
						
						PreparedStatement pstmt = con.prepareStatement(sql);
					
						pstmt.setString(1,Utility.trim(usersProfileWrapper.schoolID));   

						if(!Utility.isEmpty(hostelInOutWrapper.inOutRefNo))
						{
							pstmt.setString(2,Utility.trim(hostelInOutWrapper.inOutRefNo));   
						}

						
						resultSet = pstmt.executeQuery();
						
						while (resultSet.next()) 
						{
							
							hostelInOutWrapper=new HostelInOutWrapper();
							
							
							hostelInOutWrapper.inOutRefNo=Utility.trim(resultSet.getString("InOutRefNo"));
							hostelInOutWrapper.hostelID=Utility.trim(resultSet.getString("HostelID"));
							hostelInOutWrapper.studentID=Utility.trim(resultSet.getString("StudentID"));  
							hostelInOutWrapper.inOutMode=Utility.trim(resultSet.getString("InOutMode"));
							hostelInOutWrapper.inDateTime=Utility.setDate(resultSet.getString("InDateTime")); 
							hostelInOutWrapper.outDateTime=Utility.setDate(resultSet.getString("OutDateTime")); 
							hostelInOutWrapper.inOutTime=Utility.trim(resultSet.getString("InOutTime")); 
							hostelInOutWrapper.makerID=Utility.trim(resultSet.getString("MakerID"));
							hostelInOutWrapper.makerDateTime=Utility.setDate(resultSet.getString("MakerDateTime"));
							hostelInOutWrapper.modifierID=Utility.trim(resultSet.getString("ModifierID"));
							hostelInOutWrapper.modifierDateTime=Utility.setDate(resultSet.getString("ModifierDateTime")); 
							
							hostelInOutWrapper.hostelIDValue=popoverHelper.fetchPopoverDesc(hostelInOutWrapper.hostelID, "MST_Hostel", usersProfileWrapper.schoolID);
							
							
							hostelInOutWrapper.recordFound=true;

							System.out.println("MST_Hostel fetch successful");
			
							vector.addElement(hostelInOutWrapper);
			
						}
					
						if (vector.size()>0)
						{
							dataArrayWrapper.hostelInOutWrapper = new HostelInOutWrapper[vector.size()];
							vector.copyInto(dataArrayWrapper.hostelInOutWrapper);
							dataArrayWrapper.recordFound=true;
				
							System.out.println("total trn. in fetch " + vector.size());
				
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
		//-----------------End fetchHostel---------------------

		
		//-----------------Generate InOutRefNo-------------------------------
		public String generateInOutRefNo(String schoolID)throws Exception {
			
			Connection con = null;
			ResultSet resultSet = null;

			//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
			String sql=null;
			
			SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");
		
//			DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//			DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//			symbols.setGroupingSeparator(',');
//			formatter.applyPattern("###,###,###,##0.00");
//			formatter.setDecimalFormatSymbols(symbols);
			
			int inOutRefNo=0; //InOutRefNo
			String finalInOutRefNo=null;
			//String inOutCode=null;
			
			try {
					con = getConnection();
				
					sql="SELECT ServiceTicketRefNo FROM MST_Parameter where SchoolID=?";
					
					PreparedStatement pstmt = con.prepareStatement(sql);
					
					pstmt.setString(1,Utility.trim(schoolID)); 
				
					resultSet = pstmt.executeQuery();
					if (resultSet.next()) 
					{
						
						inOutRefNo=resultSet.getInt("ServiceTicketRefNo");
						System.out.println("inOutRefNo " + inOutRefNo);
						
					}
					
					resultSet.close();
					pstmt.close();
					
					if(inOutRefNo==0)
					{
						inOutRefNo=1;
						
					}
					else
					{
						
						inOutRefNo=inOutRefNo+1;
					}
						
					sql="UPDATE MST_Parameter set ServiceTicketRefNo=? and SchoolID=?";
					
					
					System.out.println("sql " + sql);
					
					pstmt = con.prepareStatement(sql);
			
					pstmt.setInt(1,inOutRefNo);
					pstmt.setString(2,Utility.trim(schoolID)); 
					
					pstmt.executeUpdate();
					pstmt.close();
					
					int paddingSize=6;
	
				
					finalInOutRefNo="INOUT"+dmyFormat.format(new java.util.Date()).toUpperCase()+String.format("%0" + paddingSize +"d",inOutRefNo);
					
					
					System.out.println("Successfully generated finalInOutRefNo  " + finalInOutRefNo);
				
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

			return finalInOutRefNo;
		}
		
		//-----------------End Generate Staff RefNo---------------------------

}
