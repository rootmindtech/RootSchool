package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.StudentDiaryWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentDiaryHelper extends Helper {

	// ---------------------- Start insertStudentDiary-----------------

	public AbstractWrapper insertStudentDiary(UsersWrapper usersProfileWrapper, StudentDiaryWrapper studentDiaryWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;
		String sql = null;
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			PopoverHelper popoverHelper = new PopoverHelper();

			if (Utility.isEmpty(studentDiaryWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentDiaryWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

				// ----------
			}

			sql = "INSERT INTO StudentDiary(AcademicYearID,DiaryDate,GradeID,SectionID, "
					+ "SubjectID,MessageID,Message,MessageDateTime,Delivered,MakerID,MakerDateTime,SchoolID)  Values(?,?,?,?,?,?,?,?,?,?,?,?)";

			if (studentDiaryWrapper.studentID != null && !studentDiaryWrapper.studentID.equals("")) {
				sql = "INSERT INTO StudentDiary(AcademicYearID,DiaryDate,GradeID,SectionID, "
						+ "SubjectID,MessageID,Message,MessageDateTime,Delivered,MakerID,MakerDateTime,SchoolID,RefNo,StudentID)  Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}

			// -------update diary------
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, studentDiaryWrapper.academicYearID);
			pstmt.setDate(2, Utility.getDate(studentDiaryWrapper.diaryDate));
			pstmt.setString(3, Utility.trim(studentDiaryWrapper.gradeID));
			pstmt.setString(4, Utility.trim(studentDiaryWrapper.sectionID));
			pstmt.setString(5, Utility.trim(studentDiaryWrapper.subjectID));
			pstmt.setString(6, generateMessageID(usersProfileWrapper.schoolID));
			pstmt.setString(7, Utility.trim(studentDiaryWrapper.message));
			pstmt.setTimestamp(8, Utility.getCurrentTime());
			pstmt.setString(9, Utility.trim(studentDiaryWrapper.delivered));
			pstmt.setString(10, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(11, Utility.getCurrentTime());
			pstmt.setString(12, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {
				pstmt.setString(13, Utility.trim(studentDiaryWrapper.refNo));
				pstmt.setString(14, Utility.trim(studentDiaryWrapper.studentID));
			}
			pstmt.executeUpdate();
			pstmt.close();

			studentDiaryWrapper.recordFound = true;

			dataArrayWrapper.studentDiaryWrapper = new StudentDiaryWrapper[1];
			dataArrayWrapper.studentDiaryWrapper[0] = studentDiaryWrapper;
			dataArrayWrapper.recordFound = true;

//			FCMNotification fcmNotification = new FCMNotification("DIARY_MESSAGE", usersProfileWrapper.schoolID,null,null);
//			fcmNotification.sendDiaryMessage(usersProfileWrapper.schoolID);

			System.out.println("GCM notification DIARY_MESSAGE started");

			/*
			 * sql="SELECT RefNo, AcademicYearID ,StudentID FROM StudentProfile WHERE AcademicYearID=? AND GradeID=? AND SectionID=?"
			 * ;
			 * 
			 * 
			 * if(studentDiaryWrapper.studentID !=null &&
			 * !studentDiaryWrapper.studentID.equals("")) {
			 * 
			 * System.out.println("Fetch Diary StudentID " + studentDiaryWrapper.studentID);
			 * sql= sql+" AND StudentID=?"; }
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * pstmt.setString(1,currentAcademicYear);
			 * pstmt.setString(2,Utility.trim(studentDiaryWrapper.gradeID));
			 * pstmt.setString(3,Utility.trim(studentDiaryWrapper.sectionID));
			 * 
			 * if(studentDiaryWrapper.studentID !=null &&
			 * !studentDiaryWrapper.studentID.equals("")) {
			 * pstmt.setString(4,studentDiaryWrapper.studentID.trim()); }
			 * 
			 * resultSet = pstmt.executeQuery();
			 * 
			 * while(resultSet.next()) {
			 * 
			 * studentDiaryWrapper.refNo=Utility.trim(resultSet.getString("RefNo"));
			 * System.out.println("Ref No "+studentDiaryWrapper.refNo);
			 * studentDiaryWrapper.academicYearID=Utility.trim(resultSet.getString(
			 * "AcademicYearID"));
			 * System.out.println("academic YearID "+studentDiaryWrapper.academicYearID);
			 * studentDiaryWrapper.studentID=Utility.trim(resultSet.getString("StudentID"));
			 * System.out.println("Student ID "+studentDiaryWrapper.studentID);
			 * 
			 * 
			 * 
			 * 
			 * PreparedStatement pstmt1=con.
			 * prepareStatement("INSERT INTO StudentDiary(RefNo,AcademicYearID,StudentID,DiaryDate,GradeID,SectionID, "
			 * +
			 * "SubjectID,MessageID,Message,MessageDateTime,Delivered,MakerID,MakerDateTime)  Values(?,?,?,?,?,?,?,?,?,?,?,?,?)"
			 * );
			 * 
			 * 
			 * 
			 * 
			 * pstmt1.setString(1,Utility.trim(studentDiaryWrapper.refNo));
			 * pstmt1.setString(2,Utility.trim(studentDiaryWrapper.academicYearID));
			 * pstmt1.setString(3,Utility.trim(studentDiaryWrapper.studentID));
			 * pstmt1.setDate(4,Utility.getDate(studentDiaryWrapper.diaryDate));
			 * 
			 * pstmt1.setString(5,Utility.trim(studentDiaryWrapper.gradeID));
			 * pstmt1.setString(6,Utility.trim(studentDiaryWrapper.sectionID));
			 * pstmt1.setString(7,Utility.trim(studentDiaryWrapper.subjectID));
			 * pstmt1.setString(8,generateMessageID());
			 * pstmt1.setString(9,Utility.trim(studentDiaryWrapper.message));
			 * pstmt1.setTimestamp(10,Utility.getCurrentTime());
			 * pstmt1.setString(11,Utility.trim(studentDiaryWrapper.delivered));
			 * pstmt1.setString(12,Utility.trim(usersProfileWrapper.userid));
			 * pstmt1.setTimestamp(13,Utility.getCurrentTime());
			 * 
			 * pstmt1.executeUpdate(); pstmt1.close();
			 * 
			 * studentDiaryWrapper.recordFound=true;
			 * 
			 * 
			 * 
			 * System.out.println("Successfully Student Diary Inserted");
			 * 
			 * 
			 * }
			 * 
			 * pstmt.close();
			 */

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
	// ---------------------- End insertStudentDiary-----------------

	// ---------------------- Start updateStudentDiary-----------------
	public AbstractWrapper updateStudentDiary(UsersWrapper usersProfileWrapper, StudentDiaryWrapper studentDiaryWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;
		String sql = null;
		String currentAcademicYear = null;

		try {
			con = getConnection();

			// -----get current academic year code--

			sql = "SELECT CurrentAcademicYear from MST_Parameter where SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				currentAcademicYear = Utility.trim(resultSet.getString("CurrentAcademicYear"));

			}

			System.out.println("CurrentAcademicYear " + currentAcademicYear);

			resultSet.close();
			pstmt.close();

			// ------------

//			sql = "SELECT MessageID FROM StudentDiary WHERE AcademicYearID=? AND GradeID=? AND SectionID=? AND SubjectID=? "
//					+ "AND DiaryDate=? AND MessageID=? and SchoolID=?";

			sql = "SELECT MessageID FROM StudentDiary WHERE AcademicYearID=? AND MessageID=? and SchoolID=?";

			if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {

				sql = "SELECT MessageID FROM StudentDiary WHERE AcademicYearID=? AND MessageID=? and SchoolID=? AND StudentID=?";

			}
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, currentAcademicYear);
//			pstmt.setString(2, Utility.trim(studentDiaryWrapper.gradeID));
//			pstmt.setString(3, Utility.trim(studentDiaryWrapper.sectionID));
//			pstmt.setString(4, Utility.trim(studentDiaryWrapper.subjectID));
//
//			System.out.println("diaryDate is " + studentDiaryWrapper.diaryDate);
//			pstmt.setDate(5, Utility.getDate(studentDiaryWrapper.diaryDate));
			pstmt.setString(2, Utility.trim(studentDiaryWrapper.messageID));
			pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {
				pstmt.setString(4, Utility.trim(studentDiaryWrapper.studentID));
			}

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {

				resultSet.close();
				pstmt.close();

				dataArrayWrapper = (DataArrayWrapper) insertStudentDiary(usersProfileWrapper, studentDiaryWrapper);

			} else {
				resultSet.close();
				pstmt.close();

				// -------update diary------
//				sql = "UPDATE StudentDiary SET Message=?, MessageDateTime=?, ModifierID=?, ModifierDateTime=?  WHERE AcademicYearID=? AND GradeID=? "
//						+ " AND SectionID=? AND SubjectID=? AND DiaryDate=? and MessageId=? and SchoolID=?";

				sql = "UPDATE StudentDiary SET Message=?, MessageDateTime=?, ModifierID=?, ModifierDateTime=?  WHERE AcademicYearID=? and MessageId=? and SchoolID=?";

				if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {
					sql = "UPDATE StudentDiary SET Message=?, MessageDateTime=?, ModifierID=?, ModifierDateTime=?  WHERE AcademicYearID=? and MessageId=? and SchoolID=? and StudentId=?";
				}

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(studentDiaryWrapper.message));
				pstmt.setTimestamp(2, Utility.getCurrentTime());
				pstmt.setString(3, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(4, Utility.getCurrentTime());
				pstmt.setString(5, currentAcademicYear);
//				pstmt.setString(6, Utility.trim(studentDiaryWrapper.gradeID));
//				pstmt.setString(7, Utility.trim(studentDiaryWrapper.sectionID));
//				pstmt.setString(8, Utility.trim(studentDiaryWrapper.subjectID));
//				pstmt.setDate(9, Utility.getDate(studentDiaryWrapper.diaryDate));
				pstmt.setString(6, Utility.trim(studentDiaryWrapper.messageID));
				pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

				if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {
					pstmt.setString(8, Utility.trim(studentDiaryWrapper.studentID));
				}
				studentDiaryWrapper.recordFound = true;

				pstmt.executeUpdate();
				pstmt.close();

				dataArrayWrapper.studentDiaryWrapper = new StudentDiaryWrapper[1];
				dataArrayWrapper.studentDiaryWrapper[0] = studentDiaryWrapper;
				dataArrayWrapper.recordFound = true;
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
	// ---------------------- End updateStudentDiary-----------------

	// ---------------------- Start fetchStudentDiary-----------------
	public AbstractWrapper fetchStudentDiary(UsersWrapper usersProfileWrapper, StudentDiaryWrapper studentDiaryWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchStudentDiary DiaryDate" + studentDiaryWrapper.diaryDate);

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String queueMaxRecords = null;
		// String currentAcademicYear=null;
		String sql = null;

		int n=1;
		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Queue Max Records, CurrentAcademicYear--

			if (Utility.isEmpty(studentDiaryWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentDiaryWrapper.academicYearID = parameterWrapper.currentAcademicYear;
				queueMaxRecords = parameterWrapper.queueMaxRecords;

				// ----------

				// ----------
			}

			// ----------

			/*
			 * sql="SELECT TOP "+ queueMaxRecords
			 * +" RefNo, AcademicYearID, GradeID, SectionID, StudentID, DiaryDate,SubjectID, MessageID, Message, MessageDateTime, "
			 * +
			 * "Delivered,MakerID,MakerDateTime FROM StudentDiary WHERE AcademicYearID=?  AND GradeID=? AND SectionID=?"
			 * ;
			 */

			if (Utility.isEmpty(studentDiaryWrapper.messageID)) {
				
				sql = "SELECT RefNo, AcademicYearID, GradeID, SectionID, StudentID, DiaryDate,SubjectID, MessageID, Message, MessageDateTime, "
						+ "Delivered,MakerID,MakerDateTime, SchoolID FROM StudentDiary WHERE AcademicYearID=? AND SchoolID=? AND GradeID=? AND SectionID=? ";
				
				if (Utility.isEmpty(studentDiaryWrapper.studentID)) {

					sql = sql + " AND StudentID IS NULL ";
				}
				else
				{

					sql = sql + " AND StudentID=? ";
					
				}  

				if (!Utility.isEmpty(studentDiaryWrapper.subjectID)) {
					
					sql = sql + " AND SubjectID=?";
					
				}  
				
				if (!Utility.isEmpty(studentDiaryWrapper.diaryDate)) {
					
					sql = sql + " AND DiaryDate=?";
				}

			}
			else
			{
				sql = "SELECT RefNo, AcademicYearID, GradeID, SectionID, StudentID, DiaryDate,SubjectID, MessageID, Message, MessageDateTime, "
						+ "Delivered,MakerID,MakerDateTime, SchoolID FROM StudentDiary WHERE AcademicYearID=? and SchoolID=? AND MessageID=?  ";
				
			}

			

			sql = sql + " ORDER BY MessageDateTime DESC LIMIT " + queueMaxRecords + "";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, studentDiaryWrapper.academicYearID);

			pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));
			
			if (Utility.isEmpty(studentDiaryWrapper.messageID)) {
				
				pstmt.setString(++n, Utility.trim(studentDiaryWrapper.gradeID));
				pstmt.setString(++n, Utility.trim(studentDiaryWrapper.sectionID));
				
				if (!Utility.isEmpty(studentDiaryWrapper.studentID)) {
					
					pstmt.setString(++n, Utility.trim(studentDiaryWrapper.studentID));
				}

				if (!Utility.isEmpty(studentDiaryWrapper.subjectID)) {
				
					
					pstmt.setString(++n, Utility.trim(studentDiaryWrapper.subjectID));
					
				} 
				
				if (!Utility.isEmpty(studentDiaryWrapper.diaryDate)) {
					
					pstmt.setString(++n, Utility.trim(studentDiaryWrapper.diaryDate)); // not set in dateString, because date
																						// format is 2016-04-02 to compare
				}

			}
			else
			{
				pstmt.setString(++n, Utility.trim(studentDiaryWrapper.messageID));
				
			}
			
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				studentDiaryWrapper = new StudentDiaryWrapper();

				studentDiaryWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentDiaryWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentDiaryWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentDiaryWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentDiaryWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentDiaryWrapper.diaryDate = Utility.setDate(resultSet.getString("DiaryDate"));
				studentDiaryWrapper.diaryDateMMM = Utility.setDateMMM(resultSet.getString("DiaryDate"));
				studentDiaryWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));
				studentDiaryWrapper.message = Utility.trim(resultSet.getString("Message"));
				studentDiaryWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				studentDiaryWrapper.messageDateTime = Utility.setDateAMPM(resultSet.getString("MessageDateTime"));
				studentDiaryWrapper.delivered = Utility.trim(resultSet.getString("Delivered"));
				studentDiaryWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentDiaryWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				studentDiaryWrapper.recordFound = true;
				studentDiaryWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentDiaryWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentDiaryWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				studentDiaryWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentDiaryWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentDiaryWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(studentDiaryWrapper.sectionID,
						"MST_Section", usersProfileWrapper.schoolID);
				studentDiaryWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(studentDiaryWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);

				vector.addElement(studentDiaryWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentDiaryWrapper = new StudentDiaryWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentDiaryWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

				System.out.println("StudentDiary Details fetch successful");

			} else

			{
				dataArrayWrapper.studentDiaryWrapper = new StudentDiaryWrapper[1];
				dataArrayWrapper.studentDiaryWrapper[0] = studentDiaryWrapper;
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
	// ---------------------- End fetchStudentDiary-----------------

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

			finalMessageID = "MSGD" + dmyFormat.format(new java.util.Date()).toUpperCase()
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

	// ---------------------- Start insertStudentDiary-----------------

	public AbstractWrapper insertStudentNotification(UsersWrapper usersProfileWrapper,
			StudentDiaryWrapper studentDiaryWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
//
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		
		PreparedStatement pstmt = null;
		String sql = null;
		String currentAcademicYear = null;
		String birthdayMessage = null;
		System.out.println("insertStudentNotification start");

		try {

			// Date date; // your date
			Calendar cal = Calendar.getInstance();
			// cal.setTime(date);
			// int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1; // because month starts from zero
			int day = cal.get(Calendar.DAY_OF_MONTH);

			con = getConnection();

			// -----get current academic year code--

			sql = "SELECT CurrentAcademicYear,BirthdayMessage from MST_Parameter where SchoolID=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				currentAcademicYear = Utility.trim(resultSet.getString("CurrentAcademicYear"));
				birthdayMessage = Utility.trim(resultSet.getString("BirthdayMessage"));
			}

			System.out.println("CurrentAcademicYear " + currentAcademicYear);

			resultSet.close();
			pstmt.close();

			// ------------

			pstmt = con.prepareStatement("SELECT AcademicYearID,RefNo,StudentID,GradeID,SectionID, SchoolID "
					+ "FROM StudentProfile WHERE DATEPART(DD,dob)=? and DATEPART(mm,dob)=? and SchoolID=?");

			System.out.println("insertStudentNotification day " + day);
			System.out.println("insertStudentNotification new month " + month);
			pstmt.setInt(1, day);
			pstmt.setInt(2, month);
			pstmt.setString(3, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				studentDiaryWrapper = new StudentDiaryWrapper();
				studentDiaryWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentDiaryWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentDiaryWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentDiaryWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentDiaryWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentDiaryWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				/*
				 * studentAttendanceWrapper.message=Utility.trim(resultSet.getString("Message"))
				 * ; studentAttendanceWrapper.delivered=
				 * Utility.trim(resultSet.getString("Delivered"));
				 */

				studentDiaryWrapper.recordFound = true;

				sql = "INSERT INTO StudentNotification(AcademicYearID,RefNo,StudentID,GradeID,SectionID, "
						+ "MessageID,Message,MessageDateTime,Delivered,MakerID,MakerDateTime, SchoolID)  Values(?,?,?,?,?,?,?,?,?,?,?,?)";

				// -------update diary------
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(studentDiaryWrapper.academicYearID));
				pstmt.setString(2, Utility.trim(studentDiaryWrapper.refNo));
				pstmt.setString(3, Utility.trim(studentDiaryWrapper.studentID));
				pstmt.setString(4, Utility.trim(studentDiaryWrapper.gradeID));
				pstmt.setString(5, Utility.trim(studentDiaryWrapper.sectionID));
				pstmt.setString(6, generateMessageID(usersProfileWrapper.schoolID));
				pstmt.setString(7, birthdayMessage);
				pstmt.setTimestamp(8, Utility.getCurrentTime());
				pstmt.setString(9, "N");
				pstmt.setString(10, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(11, Utility.getCurrentTime());
				pstmt.setString(12, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				studentDiaryWrapper.recordFound = true;

				dataArrayWrapper.studentDiaryWrapper = new StudentDiaryWrapper[1];
				dataArrayWrapper.studentDiaryWrapper[0] = studentDiaryWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("successfully inserted into student notification");

			}

			FCMNotification fcmNotification = new FCMNotification("BIRTHDAY_MESSAGE", usersProfileWrapper.schoolID,null,null);
			fcmNotification.sendBirthdayMessage(usersProfileWrapper.schoolID);
			
			System.out.println("GCM notification BIRTHDAY_MESSAGE started");

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
	// ---------------------- End insertStudentDiary-----------------

}
