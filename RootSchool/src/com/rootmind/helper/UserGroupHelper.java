package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.UserGroupWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class UserGroupHelper  extends Helper{
	
	public AbstractWrapper fetchUserGroupList(UsersWrapper usersProfileWrapper, UserGroupWrapper userGroupWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
	
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt=null;
		
		String sql=null;
		
		//boolean useridFound=false;
		
		System.out.println("fetchUserGroupList");
	
		try {
			
				   //PopoverHelper popoverHelper=new PopoverHelper();
				con = getConnection();
				
//				sql="SELECT Userid from MSTSEC_UserGroup WHERE Userid=? and SchoolID=?";
//				pstmt = con.prepareStatement(sql);
//				
//				pstmt.setString(1,Utility.trim(userGroupWrapper.userid));
//				pstmt.setString(2,Utility.trim(usersProfileWrapper.schoolID));
//				
//				
//				resultSet = pstmt.executeQuery();
//				if (resultSet.next()) 
//				{
//					useridFound=true;
//				}
//				resultSet.close();
//				pstmt.close();
//	
//				
//				if(useridFound==true)
//				{
						//1st condition //and b.Userid=?
						sql="SELECT a.Code , a.Description, b.Userid, b.GroupID, b.Assigned  from  MSTSEC_UserGroup b JOIN MST_UserGroup a ON b.GroupID=a.Code  and b.SchoolID=?";
	
						pstmt = con.prepareStatement(sql);
						
						//pstmt.setString(1,Utility.trim(userGroupWrapper.userid));
						pstmt.setString(1,Utility.trim(usersProfileWrapper.schoolID));
						
						resultSet = pstmt.executeQuery();
						while (resultSet.next()) 
						{
	
							userGroupWrapper = new UserGroupWrapper();
							
							//userGroupWrapper.code = Utility.trim(resultSet.getString("Code"));
							
							userGroupWrapper.groupID = Utility.trim(resultSet.getString("Code"));
							System.out.println("groupID" + userGroupWrapper.groupID);
							
							userGroupWrapper.desc = Utility.trim(resultSet.getString("Description"));
							userGroupWrapper.userid = Utility.trim(resultSet.getString("Userid"));
							System.out.println("Userid" + userGroupWrapper.userid);
						
							userGroupWrapper.assignFlag = Utility.trim(resultSet.getString("Assigned"));
							
							
							userGroupWrapper.recordFound = true;
							
							vector.addElement(userGroupWrapper);
		
						}
						if (vector.size()>0)
						{
							dataArrayWrapper.userGroupWrapper = new UserGroupWrapper[vector.size()];
							vector.copyInto(dataArrayWrapper.userGroupWrapper);
							dataArrayWrapper.recordFound=true;
			
							System.out.println("total trn. in fetch " + vector.size());
		
							System.out.println("Fetch UserMenu Details Successful" );
						}
						
						resultSet.close();
						pstmt.close();
						
//				}
//	
//				if(useridFound==false)
//				{
//					
//						sql = "SELECT  Code, Description, 'N' as Assigned  from MST_UserGroup where SchoolID=?";
//					
//						pstmt = con.prepareStatement(sql);
//						
//						pstmt.setString(1,Utility.trim(usersProfileWrapper.schoolID));
//
//						resultSet = pstmt.executeQuery();
//						while (resultSet.next()) 
//						{
//	
//							userGroupWrapper = new UserGroupWrapper();
//							
//							userGroupWrapper.groupID = Utility.trim(resultSet.getString("Code"));
//							System.out.println("groupID" + userGroupWrapper.groupID);
//							
//							userGroupWrapper.desc = Utility.trim(resultSet.getString("Description"));
//						
//							userGroupWrapper.assignFlag = Utility.trim(resultSet.getString("Assigned"));
//							userGroupWrapper.recordFound = true;
//							
//							vector.addElement(userGroupWrapper);
//		
//						}
//						if (vector.size()>0)
//						{
//							dataArrayWrapper.userGroupWrapper = new UserGroupWrapper[vector.size()];
//							vector.copyInto(dataArrayWrapper.userGroupWrapper);
//							dataArrayWrapper.recordFound=true;
//			
//							System.out.println("total trn. in fetch " + vector.size());
//		
//							System.out.println("Fetch User Group List Details Successful" );
//						}
//						
//						resultSet.close();
//						pstmt.close();
//	
//				}



			
			
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
	
	
	public AbstractWrapper updateUserGroupList(UsersWrapper usersProfileWrapper,UserGroupWrapper[] userGroupWrapperArray)throws Exception {
		
		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt=null;
		String sql=null;
		//String currentAcademicYear=null;
		
		
		try {
				con = getConnection();
				
				for(int i=0;i<=userGroupWrapperArray.length-1;i++)
				{
					//--if event is assigned to user than only insert
					
							sql="SELECT GroupID FROM MSTSEC_UserGroup WHERE GroupID=? AND Userid=? and SchoolID=?";
							pstmt = con.prepareStatement(sql);
							
							System.out.println(" groupID "+userGroupWrapperArray[i].groupID);
							System.out.println(" Userid "+userGroupWrapperArray[i].userid);
							
							pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].groupID)); 
							pstmt.setString(2,Utility.trim(userGroupWrapperArray[i].userid)); 
							pstmt.setString(3,Utility.trim(usersProfileWrapper.schoolID)); 
						 
							
							resultSet = pstmt.executeQuery();
							
							if (!resultSet.next()) 
							{
									resultSet.close();
									pstmt.close();
									
									
										   System.out.println(" Insert into MSTSEC_UserGroup "+userGroupWrapperArray[i].groupID);
										
											sql=" INSERT INTO MSTSEC_UserGroup(Userid, GroupID, Assigned,SchoolID, MakerID, MakerDateTime) Values (?,?,?,?,?,?)";
											
											System.out.println("sql " + sql);
											
											pstmt = con.prepareStatement(sql);
											
											pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].userid)); 
											pstmt.setString(2,Utility.trim(userGroupWrapperArray[i].groupID)); 
											pstmt.setString(3,Utility.trim(userGroupWrapperArray[i].assignFlag)); 
											pstmt.setString(4,Utility.trim(usersProfileWrapper.schoolID)); 
											pstmt.setString(5,Utility.trim(usersProfileWrapper.userid)); 
											pstmt.setTimestamp(6,Utility.getCurrentTime()); // maker date time
									
											System.out.println(" Userid "+userGroupWrapperArray[i].userid);
											
											pstmt.executeUpdate();
											pstmt.close();
											
											userGroupWrapperArray[i].recordFound=true;
											
											
											//-----INSERT INTO RMTSEC_UserGroup_Audit--TO PERFORM AUDIT-----------
											
											/*sql=" INSERT INTO MSTSEC_UserGroup_Audit(Userid, GroupID, Assigned, MakerID, MakerDateTime) Values (?,?,?,?,?)";
											
											System.out.println("sql " + sql);
											
											pstmt = con.prepareStatement(sql);
											
											pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].userid)); 
											pstmt.setString(2,Utility.trim(userGroupWrapperArray[i].groupID)); 
											pstmt.setString(3,Utility.trim(userGroupWrapperArray[i].assignFlag)); 
											pstmt.setString(4,Utility.trim(usersProfileWrapper.userid)); 
											pstmt.setTimestamp(5,Utility.getCurrentTime()); // maker date time
									
											System.out.println(" Userid "+userGroupWrapperArray[i].userid);
											
											pstmt.executeUpdate();
											pstmt.close();*/
											
											//-----------
											
									
									
							}
							else
							{
									resultSet.close();
									pstmt.close();
								
									
										
									pstmt = con.prepareStatement("UPDATE MSTSEC_UserGroup SET  Assigned=?, ModifierID=?, ModifierDateTime=? WHERE GroupID=? AND Userid=? and SchoolID=?");
									
									//pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].groupID)); 
									pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].assignFlag)); 
									pstmt.setString(2,Utility.trim(usersProfileWrapper.userid)); 
									pstmt.setTimestamp(3,Utility.getCurrentTime());
									pstmt.setString(4,Utility.trim(userGroupWrapperArray[i].groupID)); 
									pstmt.setString(5,Utility.trim(userGroupWrapperArray[i].userid)); 
									pstmt.setString(6,Utility.trim(usersProfileWrapper.schoolID)); 
									
									pstmt.executeUpdate();
									pstmt.close();
									
									userGroupWrapperArray[i].recordFound=true;
										
									
									//-----INSERT INTO RMTSEC_UserGroup_Audit--to PERFORM AUDIT-----------
									
									/*sql=" INSERT INTO RMTSEC_UserGroup_Audit(Userid, GroupID, Assigned, ModifierID, ModifierDateTime) Values (?,?,?,?,?)";
									
									System.out.println("sql " + sql);
									
									pstmt = con.prepareStatement(sql);
									
									pstmt.setString(1,Utility.trim(userGroupWrapperArray[i].userid)); 
									pstmt.setString(2,Utility.trim(userGroupWrapperArray[i].groupID)); 
									pstmt.setString(3,Utility.trim(userGroupWrapperArray[i].assignFlag)); 
									pstmt.setString(4,Utility.trim(usersProfileWrapper.userid)); 
									pstmt.setTimestamp(5,Utility.getCurrentTime()); // maker date time
							
									System.out.println(" Userid "+userGroupWrapperArray[i].userid);
									
									pstmt.executeUpdate();
									pstmt.close();*/
									
									//-----------
									
									
									
							}
					}
						
				
				
				dataArrayWrapper.userGroupWrapper=userGroupWrapperArray;
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
	
	
	public String fetchSupervisorHostels(UsersWrapper usersProfileWrapper)throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		
	
		//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
		
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		//Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt=null;
		
		String sql=null;
		
		ArrayList<String> hostelIDArray = new ArrayList<String>();
		//ArrayList<String> groupIDArray = new ArrayList<String>();
		
		String hostelID=null;
		//String groupID=null;
		
	
		try 
		{
			con = getConnection();
			
			
			//-------- Fetch   groupID from MSTSEC_UserGroup
			/*pstmt = con.prepareStatement("SELECT GroupID FROM MSTSEC_UserGroup WHERE Userid=? AND Assigned=?");
			
			pstmt.setString(1,Utility.trim(usersProfileWrapper.userid));
			pstmt.setString(2,"Y");
			resultSet = pstmt.executeQuery();
			while(resultSet.next()) 
			{	
				groupIDArray.add(resultSet.getString("GroupID"));
				
			
				System.out.println(" groupIDArray "+groupIDArray );
				
			}
			
			resultSet.close();
			pstmt.close();*/
			
			//-------
			
			/*
			System.out.println(" groupIDArray final "+groupIDArray );
			groupID="(";
			for(int i=0;i<=groupIDArray.size()-1;i++)
			{
				groupID=groupID+"'"+groupIDArray.get(i)+"',";
			}
			
			if(groupID.endsWith(","))
			{
				groupID = groupID.substring(0, groupID.length() - 1);
			}
			
			groupID=groupID+")";
			
			System.out.println(" groupID "+groupID );*/
			
			//-------- Fetch   Hostel Code from MST_Hostel
			
			sql="SELECT Code FROM MST_Hostel WHERE HostelGroup IN (SELECT GroupID FROM MSTSEC_UserGroup WHERE Userid=? AND Assigned=?)";
			
			System.out.println(" Fetch HostelCode SQL "+sql );
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,Utility.trim(usersProfileWrapper.userid));
			pstmt.setString(2,"Y");
			resultSet = pstmt.executeQuery();
			while(resultSet.next()) 
			{	
				hostelIDArray.add(resultSet.getString("Code"));
				
			
				System.out.println(" hostelIDArray "+hostelIDArray );
			}
			
			resultSet.close();
			pstmt.close();
			
			//-------
			if(hostelIDArray.size()>0){
				
				System.out.println(" hostelCodeArray final "+hostelIDArray );
				hostelID="(";
				for(int i=0;i<=hostelIDArray.size()-1;i++)
				{
					hostelID=hostelID+"'"+hostelIDArray.get(i)+"',";
				}
				
				if(hostelID.endsWith(","))
				{
					hostelID = hostelID.substring(0, hostelID.length() - 1);
				}
				
				hostelID=hostelID+")";
				
				System.out.println(" hostelID  "+hostelID); //('HST001','HST002')
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

		return hostelID;
	}

}
