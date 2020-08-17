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
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.SchoolMessageWrapper;

import com.rootmind.wrapper.UsersWrapper;

public class SchoolMessageHelper extends Helper {

	// ----------------------- Start insertSchoolMessage-------------------

	public AbstractWrapper insertSchoolMessage(UsersWrapper usersProfileWrapper,
			SchoolMessageWrapper schoolMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		try {
			con = getConnection();

			sql = " INSERT INTO SchoolMessage(MessageID, Message, MessageDateTime, Delivered, GradeList, "
					+ "MakerID, MakerDateTime, SchoolID) Values(?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			PreparedStatement pstmt = con.prepareStatement(sql);

			schoolMessageWrapper.messageID = generateMessageID(usersProfileWrapper.schoolID);

			pstmt.setString(1, Utility.trim(schoolMessageWrapper.messageID));
			pstmt.setString(2, Utility.trim(schoolMessageWrapper.message));
			pstmt.setTimestamp(3, Utility.getCurrentTime());
			pstmt.setString(4, Utility.trim(schoolMessageWrapper.delivered));
			pstmt.setString(5, Utility.trim(schoolMessageWrapper.gradeList));
			pstmt.setString(6, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(7, Utility.getCurrentTime());
			pstmt.setString(8, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			schoolMessageWrapper.recordFound = true;

			dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
			dataArrayWrapper.schoolMessageWrapper[0] = schoolMessageWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into School Message");

			// FCMNotification fcmNotification = new FCMNotification("SCHOOL_MESSAGE", usersProfileWrapper.schoolID,null,null);
//			fcmNotification.sendSchoolMessage(usersProfileWrapper.schoolID);

			System.out.println("GCM notification SCHOOL_MESSAGE started");

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

		return dataArrayWrapper;
	}
	// ----------------------- End insertSchoolMessage-------------------

	
	   //----------------------- Start updateSchoolMessage------------------- 
	public AbstractWrapper updateSchoolMessage(UsersWrapper usersProfileWrapper,SchoolMessageWrapper
	   schoolMessageWrapper)throws Exception {
	   
	   Connection con = null; 
	   ResultSet resultSet = null;
	   
	   DataArrayWrapper dataArrayWrapper = new DataArrayWrapper(); //
	   String sql=null;
	   
	   //SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	   
		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US); DecimalFormatSymbols symbols =
		// formatter.getDecimalFormatSymbols(); symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols); PreparedStatement pstmt=null;
		//	   
	   
	   try { 
		   
		   con = getConnection();
		   
		  sql =  "UPDATE SchoolMessage SET Message=?, MessageDateTime=?, Delivered=?, GradeList=?, "
				  + " ModifierId=?, ModifierDateTime=? WHERE MessageID=? and SchoolID=?";
		  
		PreparedStatement pstmt = con.prepareStatement(sql);
	   
	   pstmt.setString(1,Utility.trim(schoolMessageWrapper.message));
	   pstmt.setTimestamp(2,Utility.getCurrentTime());
	   
	   pstmt.setString(3,Utility.trim(schoolMessageWrapper.delivered));
	   pstmt.setString(4,Utility.trim(schoolMessageWrapper.gradeList));
	   
	   pstmt.setString(5,Utility.trim(usersProfileWrapper.userid));
	   pstmt.setTimestamp(6,Utility.getCurrentTime());
	   
	   pstmt.setString(7,Utility.trim(schoolMessageWrapper.messageID));
	   pstmt.setString(8,Utility.trim(usersProfileWrapper.schoolID));
	   
	   pstmt.executeUpdate(); pstmt.close();
	   
	   schoolMessageWrapper.recordFound=true;
	   
	   dataArrayWrapper.schoolMessageWrapper=new SchoolMessageWrapper[1];
	   dataArrayWrapper.schoolMessageWrapper[0]=schoolMessageWrapper;
	   dataArrayWrapper.recordFound=true;
	   
	   System.out.println("Successfully School Message Updated");
	   
//	   GCMNotification gcmNotification = new GCMNotification("SCHOOL_MESSAGE");
//	   gcmNotification.start();
	   
	  // System.out.println("GCM notification started");
	   
	   
	   
	   
	   } 
	   catch (SQLException se) {
		   se.printStackTrace(); 
		   throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage()); 
	   
	   } 
	   catch
	   (NamingException ne) { 
		   ne.printStackTrace(); 
		   throw new NamingException(ne.getMessage()); 
		   } 
	   catch (Exception ex) {
	   ex.printStackTrace(); 
	   throw new Exception(ex.getMessage()); 
	   } 
	   finally { 
		   
		   try {
			   releaseConnection(resultSet, con); 
			   } catch (SQLException se) {
				   se.printStackTrace(); 
				   throw new Exception(se.getSQLState()+ " ; " + se.getMessage()); } }
	   
	   return dataArrayWrapper; 
	   
	   } //----------------------- Start
	   //-------insertSchoolMessage-------------------
	 
	 

	// ----------------------- Start fetchSchoolMessage-------------------
	public AbstractWrapper fetchSchoolMessage(UsersWrapper usersProfileWrapper,
			SchoolMessageWrapper schoolMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchSchoolMessage MessageID" + schoolMessageWrapper.messageID);

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		//String queueMaxRecords = null;

		String sql=null;
		
		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Queue Max Records--

			// ------------get MST_Parameter table details----
//			ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
//					.fetchParameters(usersProfileWrapper.schoolID);
//			queueMaxRecords = parameterWrapper.queueMaxRecords;

			// ----------

			// ----------
			/*
			 * pstmt = con.prepareStatement("SELECT TOP "
			 * +queueMaxRecords+" MessageID, Message, MessageDateTime, Delivered, GradeList, MakerID, "
			 * + " MakerDateTime FROM SchoolMessage ORDER BY MessageDateTime Desc");
			 */
			
			sql = "SELECT MessageID, Message, MessageDateTime, Delivered, GradeList, MakerID, "
					+ " MakerDateTime, SchoolID FROM SchoolMessage WHERE SchoolID=? ";
			
			if(!Utility.isEmpty(schoolMessageWrapper.messageID))
			{
				sql = sql + " and MessageID=? ";
			}
			
			sql = sql + "ORDER BY MessageDateTime DESC";
			
			pstmt = con.prepareStatement(sql); // LIMITqueueMaxRecords  "");

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			if(!Utility.isEmpty(schoolMessageWrapper.messageID))
			{
				pstmt.setString(2, Utility.trim(schoolMessageWrapper.messageID));
				
			}
			

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				schoolMessageWrapper = new SchoolMessageWrapper();

				schoolMessageWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				schoolMessageWrapper.message = Utility.trim(resultSet.getString("Message"));
				schoolMessageWrapper.messageDate = Utility.setDateMMM(resultSet.getString("MessageDateTime"));
				schoolMessageWrapper.messageDateTime = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				schoolMessageWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
				schoolMessageWrapper.gradeList = Utility.trim(resultSet.getString("GradeList"));
				schoolMessageWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				schoolMessageWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				schoolMessageWrapper.recordFound = true;
				schoolMessageWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				schoolMessageWrapper.gradeListValue = popoverHelper.fetchPopoverDesc(schoolMessageWrapper.gradeList,
						"MST_Grade", usersProfileWrapper.schoolID);

				System.out.println("SchoolMessage Details fetch successful");

				vector.addElement(schoolMessageWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.schoolMessageWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				dataArrayWrapper.schoolMessageWrapper = new SchoolMessageWrapper[1];
				dataArrayWrapper.schoolMessageWrapper[0] = schoolMessageWrapper;
				dataArrayWrapper.recordFound = true;

			}

			if (resultSet != null)
				resultSet.close();
			pstmt.close();

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

		return dataArrayWrapper;
	}
	// ----------------------- End fetchSchoolMessage-------------------

	// -----------------Generate MessageID-------------------------------
	public String generateMessageID(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

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

			finalMessageID = "MSGA" + dmyFormat.format(new java.util.Date()).toUpperCase()
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

	// -----------------End Generate MessageID---------------------------

}
