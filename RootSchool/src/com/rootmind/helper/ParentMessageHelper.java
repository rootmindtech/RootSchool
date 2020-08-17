package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.text.NumberFormat;
import java.text.SimpleDateFormat;
//import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.LastMessageWrapper;
import com.rootmind.wrapper.MessengerServiceWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.ParentMessageWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ParentMessageHelper extends Helper {

	// -----------------Start insertParentMessage---------------------

	public AbstractWrapper insertParentMessage(UsersWrapper usersProfileWrapper,
			ParentMessageWrapper parentMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// String countryCode=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {

			/*
			 * //-----country code--
			 * 
			 * sql="SELECT CountryCode from MST_Parameter";
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * countryCode=resultSet.getString("CountryCode");
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(parentMessageWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				parentMessageWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			sql = " INSERT INTO ParentMessage(AcademicYearID,RefNo,StudentID,GradeID,SectionID,MessageID,Message, "
					+ " MessageDateTime,Delivered,MakerID,MakerDateTime,UserGroup, SchoolID, ImageFileFolder, StaffRefNo)  "
					+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			// parentMessageWrapper.refNo=generateRefNo(parentMessageWrapper);

			pstmt.setString(1, Utility.trim(parentMessageWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(parentMessageWrapper.refNo));
			pstmt.setString(3, Utility.trim(parentMessageWrapper.studentID));
			pstmt.setString(4, Utility.trim(parentMessageWrapper.gradeID));
			pstmt.setString(5, Utility.trim(parentMessageWrapper.sectionID));
			pstmt.setString(6, generateMessageID(usersProfileWrapper.schoolID));
			pstmt.setString(7, Utility.trim(parentMessageWrapper.message));
			pstmt.setTimestamp(8, Utility.getCurrentTime());
			pstmt.setString(9, "N");
			pstmt.setString(10, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(11, Utility.getCurrentTime()); // maker date time
			pstmt.setString(12, Utility.trim(parentMessageWrapper.userGroup));
			pstmt.setString(13, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(14, null);
			pstmt.setString(15, null);

			if (Utility.trim(parentMessageWrapper.userGroup).equals(Utility.student_type)) {

				pstmt.setString(14, Utility.trim(new StudentProfileHelper().fetchStudentImage(usersProfileWrapper, Utility.trim(parentMessageWrapper.studentID))));
			}
			if (Utility.trim(parentMessageWrapper.userGroup).equals(Utility.staff_type)) {
				
				pstmt.setString(14, Utility.trim(new TeachersProfileHelper().fetchStaffImage(usersProfileWrapper, Utility.trim(parentMessageWrapper.staffRefNo))));
				pstmt.setString(15, Utility.trim(parentMessageWrapper.staffRefNo));

			}

			System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid);

			pstmt.executeUpdate();
			pstmt.close();

			parentMessageWrapper.recordFound = true;

			dataArrayWrapper.parentMessageWrapper = new ParentMessageWrapper[1];
			dataArrayWrapper.parentMessageWrapper[0] = parentMessageWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into ParentMessage");

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

	// -----------------End insertStudentProfile---------------------

	// ----------------------- Start fetchParentMessage-------------------
	public AbstractWrapper fetchParentMessage(UsersWrapper usersProfileWrapper,
			ParentMessageWrapper parentMessageWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchSchoolMessage MessageID" +
		// parentMessageWrapper.messageID);

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords = null;
		String sql = null;

		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Queue Max Records--

			// ------------get MST_Parameter table details----
			// ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
			// .fetchParameters(usersProfileWrapper.schoolID);
			// queueMaxRecords = parameterWrapper.queueMaxRecords;

			// ----------

			if (Utility.isEmpty(parentMessageWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				parentMessageWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			/*
			 * sql="SELECT AcademicYearID,RefNo,StudentID,GradeID,SectionID,MessageID,Message,MessageDateTime, Delivered, MakerID, "
			 * + " MakerDateTime  FROM ParentMessage";
			 */

			// sql = "SELECT
			// a.AcademicYearID,a.RefNo,a.StudentID,a.GradeID,a.SectionID,a.MessageID,a.Message,a.MessageDateTime,a.Delivered,a.MakerID,
			// "
			// + " a.MakerDateTime,a.UserGroup,b.StudentName,b.Surname, a.SchoolID"
			// + " FROM ParentMessage a JOIN StudentProfile b ON a.RefNo=b.RefNo WHERE
			// a.SchoolID=?";

			sql = "SELECT AcademicYearID,RefNo,StudentID,GradeID,SectionID,MessageID,Message,MessageDateTime,Delivered,MakerID, "
					+ " MakerDateTime,UserGroup, SchoolID FROM ParentMessage WHERE SchoolID=? ";

			if (!Utility.isEmpty(parentMessageWrapper.studentID)) {
				sql = sql + "  AND  StudentID=? "; // AcademicYearID=? AND AND a.GradeID=? AND a.SectionID=?
			}

			sql = sql + " ORDER BY MessageDateTime DESC"; // LIMIT " + queueMaxRecords + "";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(parentMessageWrapper.studentID)) {

				pstmt.setString(2, Utility.trim(parentMessageWrapper.studentID));
			}

			// pstmt.setString(2, Utility.trim(parentMessageWrapper.academicYearID));
			// pstmt.setString(3, Utility.trim(parentMessageWrapper.refNo));
			// pstmt.setString(5, Utility.trim(parentMessageWrapper.gradeID));
			// pstmt.setString(6, Utility.trim(parentMessageWrapper.sectionID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				parentMessageWrapper = new ParentMessageWrapper();

				parentMessageWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				parentMessageWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				parentMessageWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				parentMessageWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				parentMessageWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				parentMessageWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				parentMessageWrapper.message = Utility.trim(resultSet.getString("Message"));
				parentMessageWrapper.messageDate = Utility.setDateMMM(resultSet.getString("MessageDateTime"));
				parentMessageWrapper.messageDateTime = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				parentMessageWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
				parentMessageWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				parentMessageWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));

				// parentMessageWrapper.studentName =
				// Utility.trim(resultSet.getString("StudentName"));
				// parentMessageWrapper.surname = Utility.trim(resultSet.getString("Surname"));
				parentMessageWrapper.userGroup = Utility.trim(resultSet.getString("UserGroup"));

				parentMessageWrapper.recordFound = true;

				parentMessageWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				parentMessageWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						parentMessageWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				parentMessageWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(parentMessageWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				parentMessageWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(parentMessageWrapper.sectionID,
						"MST_Section", usersProfileWrapper.schoolID);
				//System.out.println("fetchParentMessage Details  successful");

				vector.addElement(parentMessageWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.parentMessageWrapper = new ParentMessageWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.parentMessageWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				dataArrayWrapper.parentMessageWrapper = new ParentMessageWrapper[1];
				dataArrayWrapper.parentMessageWrapper[0] = parentMessageWrapper;
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

	// ----------------------- End fetchParentMessage-------------------
	// -----------------Generate MessageID-------------------------------
	public String generateMessageID(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		int messageID = 0;
		String finalMessageID = null;

		try {
			con = getConnection();

			sql = "SELECT MessageID from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));
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

			finalMessageID = "MSGP" + dmyFormat.format(new java.util.Date()).toUpperCase()
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

	public AbstractWrapper fetchMessengerServiceList(UsersWrapper usersProfileWrapper,
			MessengerServiceWrapper messengerServiceWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchSchoolMessage MessageID" +
		// parentMessageWrapper.messageID);

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords = null;
		String sql = null;

		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Queue Max Records--

			// ------------get MST_Parameter table details----
			// ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
			// .fetchParameters(usersProfileWrapper.schoolID);
			// queueMaxRecords = parameterWrapper.queueMaxRecords;

			// ----------

			// if (Utility.isEmpty(parentMessageWrapper.academicYearID) == true) {
			// // ------------get MST_Parameter table details----
			// ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
			// .fetchParameters(usersProfileWrapper.schoolID);
			// parentMessageWrapper.academicYearID = parameterWrapper.currentAcademicYear;
			//
			// }

			/*
			 * sql="SELECT AcademicYearID,RefNo,StudentID,GradeID,SectionID,MessageID,Message,MessageDateTime, Delivered, MakerID, "
			 * + " MakerDateTime  FROM ParentMessage";
			 */

			// sql = "SELECT
			// a.AcademicYearID,a.RefNo,a.StudentID,a.GradeID,a.SectionID,a.MessageID,a.Message,a.MessageDateTime,a.Delivered,a.MakerID,
			// "
			// + " a.MakerDateTime,a.UserGroup,b.StudentName,b.Surname, a.SchoolID"
			// + " FROM ParentMessage a JOIN StudentProfile b ON a.RefNo=b.RefNo WHERE
			// a.SchoolID=?";

			sql = "SELECT a.AcademicYearID,a.RefNo RefNo,a.StudentID,a.GradeID,a.SectionID,a.MessageID,a.Message,a.MessageDateTime,a.Delivered,a.MakerID, "
					+ " a.MakerDateTime,a.UserGroup, a.SchoolID "
					+ " FROM ParentMessage a INNER JOIN (SELECT RefNo, MAX(MessageDateTime) MaxDateTime from ParentMessage Group By RefNo) b "
					+ " ON a.RefNo=b.RefNo and a.MessageDateTime = b.MaxDateTime WHERE a.SchoolID=? ";

			if (!Utility.isEmpty(messengerServiceWrapper.studentID)) {
				sql = sql + "  AND  a.StudentID=? "; // AcademicYearID=? AND AND a.GradeID=? AND a.SectionID=?
			}

			sql = sql + " ORDER BY a.MessageDateTime DESC"; // LIMIT " + queueMaxRecords + "";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(messengerServiceWrapper.studentID)) {

				pstmt.setString(2, Utility.trim(messengerServiceWrapper.studentID));
			}

			// pstmt.setString(2, Utility.trim(parentMessageWrapper.academicYearID));
			// pstmt.setString(3, Utility.trim(parentMessageWrapper.refNo));
			// pstmt.setString(5, Utility.trim(parentMessageWrapper.gradeID));
			// pstmt.setString(6, Utility.trim(parentMessageWrapper.sectionID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				messengerServiceWrapper = new MessengerServiceWrapper();

				messengerServiceWrapper.id = Utility.trim(resultSet.getString("RefNo"));

				messengerServiceWrapper.last = new LastMessageWrapper[1];
				LastMessageWrapper lastMessageWrapper = new LastMessageWrapper();
				lastMessageWrapper.timestamp = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				lastMessageWrapper.message = Utility.trim(resultSet.getString("Message"));
				messengerServiceWrapper.last[0] = lastMessageWrapper;

				messengerServiceWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				messengerServiceWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				messengerServiceWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				messengerServiceWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				messengerServiceWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				messengerServiceWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				messengerServiceWrapper.message = Utility.trim(resultSet.getString("Message"));
				messengerServiceWrapper.messageDate = Utility.setDateMMM(resultSet.getString("MessageDateTime"));
				messengerServiceWrapper.messageDateTime = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				messengerServiceWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
				messengerServiceWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				messengerServiceWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				messengerServiceWrapper.userGroup = Utility.trim(resultSet.getString("UserGroup"));

				messengerServiceWrapper.recordFound = true;

				messengerServiceWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				messengerServiceWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						messengerServiceWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				messengerServiceWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(messengerServiceWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				messengerServiceWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						messengerServiceWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);

				// ----------get student name------
				PreparedStatement pstmtSub = con
						.prepareStatement("SELECT StudentName FROM StudentProfile WHERE RefNo=?");

				pstmtSub.setString(1, Utility.trim(messengerServiceWrapper.refNo));
				ResultSet resultSetSub = pstmtSub.executeQuery();
				if (resultSetSub.next()) {
					messengerServiceWrapper.name = Utility.trim((resultSetSub.getString("StudentName")));

				}
				resultSetSub.close();
				pstmtSub.close();
				// ----------------------

				// --------to get student image;
				
				messengerServiceWrapper.photo= Utility.trim(new StudentProfileHelper().fetchStudentImage(usersProfileWrapper, Utility.trim(messengerServiceWrapper.studentID)));
				// --------end of student image

				vector.addElement(messengerServiceWrapper);

				System.out.println("fetchParentMessage Details  successful");

			}

			if (vector.size() > 0) {
				dataArrayWrapper.messengerServiceWrapper = new MessengerServiceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.messengerServiceWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				dataArrayWrapper.messengerServiceWrapper = new MessengerServiceWrapper[1];
				dataArrayWrapper.messengerServiceWrapper[0] = messengerServiceWrapper;
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

	// ----------------------- End fetchParentMessage-------------------

	public AbstractWrapper fetchMessengerService(UsersWrapper usersProfileWrapper, MessengerServiceWrapper conversation)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchSchoolMessage MessageID" +
		// parentMessageWrapper.messageID);

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		// String queueMaxRecords = null;
		String sql = null;

		try {

			// PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			sql = "SELECT AcademicYearID,RefNo,StudentID,GradeID,SectionID,MessageID,Message,MessageDateTime,Delivered,MakerID, "
					+ " MakerDateTime,UserGroup, SchoolID, ImageFileFolder, StaffRefNo "
					+ " FROM ParentMessage WHERE SchoolID=? and RefNo=? and StudentID=? ";

			// if (!Utility.isEmpty(messengerServiceWrapper.studentID)) {
			// sql = sql + " AND StudentID=? "; //AcademicYearID=? AND AND a.GradeID=? AND
			// a.SectionID=?
			// }
			//
			sql = sql + " ORDER BY MessageDateTime DESC"; // LIMIT " + queueMaxRecords + "";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(2, Utility.trim(conversation.refNo));
			pstmt.setString(3, Utility.trim(conversation.studentID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				conversation = new MessengerServiceWrapper();

				// conversation.academicYearID =
				// Utility.trim(resultSet.getString("AcademicYearID"));
				conversation.refNo = Utility.trim(resultSet.getString("RefNo"));
				conversation.studentID = Utility.trim(resultSet.getString("StudentID"));
				conversation.gradeID = Utility.trim(resultSet.getString("GradeID"));
				conversation.sectionID = Utility.trim(resultSet.getString("SectionID"));
				// conversation.messageID = Utility.trim(resultSet.getString("MessageID"));
				// conversation.message = Utility.trim(resultSet.getString("Message"));
				// conversation.messageDate =
				// Utility.setDateMMM(resultSet.getString("MessageDateTime"));
				// conversation.messageDateTime =
				// Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				// conversation.delivered = Utility.trim(resultSet.getString("Delivered"));
				conversation.makerID = Utility.trim(resultSet.getString("MakerID"));
				// conversation.makerDateTime =
				// Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				conversation.userGroup = Utility.trim(resultSet.getString("UserGroup"));
				conversation.staffRefNo = Utility.trim(resultSet.getString("StaffRefNo"));

				
				conversation.id = Utility.trim(resultSet.getString("RefNo"));

				conversation.isMe = false;

				//for students
				if (conversation.userGroup.equals(Utility.student_type)) {
					
					if(Utility.trim(usersProfileWrapper.refNo).equals(conversation.refNo))
					{
						conversation.isMe = true;
					}
					
				} 
				
				//for staff
				if (conversation.userGroup.equals(Utility.staff_type)) {
					
					//if(Utility.trim(usersProfileWrapper.refNo).equals(conversation.staffRefNo))
					//{
						conversation.isMe = true;
					//}
					
				} 

				conversation.timestamp = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				conversation.messages = new String[] { Utility.trim(resultSet.getString("Message")) };
				conversation.photo = Utility.trim(resultSet.getString("ImageFileFolder"));

				
				conversation.recordFound = true;

				// conversation.academicYearIDValue = popoverHelper.fetchPopoverDesc(
				// conversation.academicYearID, "MST_AcademicYear",
				// usersProfileWrapper.schoolID);
				// conversation.gradeIDValue =
				// popoverHelper.fetchPopoverDesc(conversation.gradeID,
				// "MST_Grade", usersProfileWrapper.schoolID);
				// conversation.sectionIDValue =
				// popoverHelper.fetchPopoverDesc(conversation.sectionID,
				// "MST_Section", usersProfileWrapper.schoolID);
				System.out.println("fetchParentMessage Details  successful");

				vector.addElement(conversation);

			}

			if (vector.size() > 0) {

				MessengerServiceWrapper messengerServiceWrapper = new MessengerServiceWrapper();
				messengerServiceWrapper.id = conversation.refNo;
				messengerServiceWrapper.studentID = conversation.studentID;
				messengerServiceWrapper.recordFound = true;

				messengerServiceWrapper.conversation = new MessengerServiceWrapper[vector.size()];
				vector.copyInto(messengerServiceWrapper.conversation);

				dataArrayWrapper.messengerServiceWrapper = new MessengerServiceWrapper[1];
				dataArrayWrapper.messengerServiceWrapper[0] = messengerServiceWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				MessengerServiceWrapper messengerServiceWrapper = new MessengerServiceWrapper();
				messengerServiceWrapper.id = conversation.refNo;
				messengerServiceWrapper.studentID = conversation.studentID;
				messengerServiceWrapper.recordFound = false;

				dataArrayWrapper.messengerServiceWrapper = new MessengerServiceWrapper[1];
				dataArrayWrapper.messengerServiceWrapper[0] = messengerServiceWrapper;
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

	// ----------------------- End fetchParentMessage-------------------

}
