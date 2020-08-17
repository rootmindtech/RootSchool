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
import com.rootmind.wrapper.StudentMessageWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentMessageHelper extends Helper {

	// ------------- Start insertStudentMessage------------------
	public AbstractWrapper insertStudentMessage(UsersWrapper usersProfileWrapper, StudentMessageWrapper studentMessageWrapper) throws Exception {

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

			sql = " INSERT INTO StudentMessage(RefNo,StudentID,MessageID,Message,MessageDateTime,Delivered, SchoolID) Values (?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, generateMessageID(usersProfileWrapper.schoolID));
			pstmt.setString(2, Utility.trim(studentMessageWrapper.studentID));
			pstmt.setString(3, Utility.trim(studentMessageWrapper.messageID));
			pstmt.setString(4, Utility.trim(studentMessageWrapper.message));
			pstmt.setTimestamp(5, Utility.getCurrentTime());
			pstmt.setString(6, Utility.trim(studentMessageWrapper.delivered));
			pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			studentMessageWrapper.recordFound = true;

			dataArrayWrapper.studentMessageWrapper = new StudentMessageWrapper[1];
			dataArrayWrapper.studentMessageWrapper[0] = studentMessageWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentMessage");

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
	// ------------- End insertStudentMessage------------------

	// ------------- Start updateStudentMessage------------------

	public AbstractWrapper updateStudentMessage(UsersWrapper usersProfileWrapper,StudentMessageWrapper studentMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT RefNo FROM StudentMessage WHERE RefNo=? and SchoolID=?");

			System.out.println("Student Message  RefNo is" + studentMessageWrapper.refNo);

			pstmt.setString(1, Utility.trim(studentMessageWrapper.refNo));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertStudentMessage(usersProfileWrapper, studentMessageWrapper);
			} else {

				resultSet.close();
				pstmt.close();

				pstmt = con.prepareStatement(
						"UPDATE StudentMessage SET StudentID=?,MessageID=?,Message=?,MessageDateTime=?,Delivered=? "
								+ "WHERE RefNo=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(studentMessageWrapper.studentID));
				pstmt.setString(2, Utility.trim(studentMessageWrapper.messageID));
				pstmt.setString(3, Utility.trim(studentMessageWrapper.message));
				pstmt.setTimestamp(4, Utility.getCurrentTime());
				pstmt.setString(5, Utility.trim(studentMessageWrapper.delivered));
				pstmt.setString(6, Utility.trim(studentMessageWrapper.refNo));
				pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				studentMessageWrapper.recordFound = true;

				dataArrayWrapper.studentMessageWrapper = new StudentMessageWrapper[1];
				dataArrayWrapper.studentMessageWrapper[0] = studentMessageWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Student Message Updated");
			}

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
	// ------------- End updateStudentMessage------------------

	// ------------- Start fetchStudentMessage------------------
	public AbstractWrapper fetchStudentMessage(UsersWrapper usersProfileWrapper, StudentMessageWrapper studentMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchStudentMessage RefNo" + studentMessageWrapper.refNo);

		Vector<Object> vector = new Vector<Object>();

		String sql =null;
		
		try {

			sql = "SELECT RefNo, StudentID, MessageID, Message, MessageDateTime, Delivered, SchoolID"
					+ " FROM StudentMessage WHERE StudentID=? and SchoolID=? ";
			

			if(!Utility.isEmpty(studentMessageWrapper.refNo))
			{
				sql = sql + " and RefNo=?";
				
			}
			
			sql = sql + " ORDER BY MessageDateTime desc";

			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, Utility.trim(studentMessageWrapper.studentID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			if(!Utility.isEmpty(studentMessageWrapper.refNo))
			{
				pstmt.setString(3, Utility.trim(studentMessageWrapper.refNo));
				
			}
			
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				studentMessageWrapper = new StudentMessageWrapper();

				studentMessageWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentMessageWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentMessageWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				studentMessageWrapper.message = Utility.trim(resultSet.getString("Message"));
				studentMessageWrapper.messageDateTime = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				studentMessageWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
				studentMessageWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentMessageWrapper.recordFound = true;

				System.out.println("StudentMessage Details fetch successful");

				vector.addElement(studentMessageWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentMessageWrapper = new StudentMessageWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentMessageWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}
			else
			{

				dataArrayWrapper.studentMessageWrapper = new StudentMessageWrapper[1];
				dataArrayWrapper.studentMessageWrapper[0] = studentMessageWrapper;
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
	// ------------- End fetchStudentMessage------------------
	
	// -----------------Generate MessageID-------------------------------
		public String generateMessageID(String schoolID) throws Exception {

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

				finalMessageID = "MSGS" + dmyFormat.format(new java.util.Date()).toUpperCase()
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
