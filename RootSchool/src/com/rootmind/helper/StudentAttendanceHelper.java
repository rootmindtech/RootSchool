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
import com.rootmind.wrapper.StudentAttendanceWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentAttendanceHelper extends Helper {

	// ---------------------- Start updateStudentAttendance-----------------
	public AbstractWrapper updateStudentAttendance(UsersWrapper usersProfileWrapper,
			StudentAttendanceWrapper[] studentAttendanceWrapperArray) throws Exception {

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

		String sql = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			for (int i = 0; i <= studentAttendanceWrapperArray.length - 1; i++) {

				sql = "SELECT RefNo,StudentID FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? AND StudentID=? "
						+ "AND GradeID=? AND SectionID=? AND CalendarDate=? and SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(studentAttendanceWrapperArray[i].academicYearID));
				pstmt.setString(2, Utility.trim(studentAttendanceWrapperArray[i].refNo));
				pstmt.setString(3, Utility.trim(studentAttendanceWrapperArray[i].studentID));
				pstmt.setString(4, Utility.trim(studentAttendanceWrapperArray[i].gradeID));
				pstmt.setString(5, Utility.trim(studentAttendanceWrapperArray[i].sectionID));
				pstmt.setDate(6, Utility.getDate(studentAttendanceWrapperArray[i].calendarDate));
				pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {
					resultSet.close();
					pstmt.close();

					sql = "UPDATE StudentAttendance SET MorningStatus=?,EveningStatus=? WHERE AcademicYearID=? AND RefNo=? "
							+ "AND StudentID=? AND GradeID=? AND SectionID=? AND CalendarDate=? and SchoolID=?";

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(studentAttendanceWrapperArray[i].morningStatus));
					pstmt.setString(2, Utility.trim(studentAttendanceWrapperArray[i].eveningStatus));
					pstmt.setString(3, Utility.trim(studentAttendanceWrapperArray[i].academicYearID));
					pstmt.setString(4, Utility.trim(studentAttendanceWrapperArray[i].refNo));
					pstmt.setString(5, Utility.trim(studentAttendanceWrapperArray[i].studentID));
					pstmt.setString(6, Utility.trim(studentAttendanceWrapperArray[i].gradeID));
					pstmt.setString(7, Utility.trim(studentAttendanceWrapperArray[i].sectionID));
					pstmt.setDate(8, Utility.getDate(studentAttendanceWrapperArray[i].calendarDate));
					pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					// -- it is for insert into the studentAbsentMessage table when morning or
					// evening status is absent
					if (Utility.trim(studentAttendanceWrapperArray[i].morningStatus).equals("A")
							|| Utility.trim(studentAttendanceWrapperArray[i].eveningStatus).equals("A")) {
						insertStudentAttenMessage(usersProfileWrapper, studentAttendanceWrapperArray[i]);
					}

				} else {

					resultSet.close();
					pstmt.close();

					sql = "INSERT INTO StudentAttendance(RefNo,AcademicYearID,StudentID,GradeID,SectionID, "
							+ "CalendarDate,MorningStatus,EveningStatus,MakerID,MakerDateTime, SchoolID)  Values(?,?,?,?,?,?,?,?,?,?,?)";

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(studentAttendanceWrapperArray[i].refNo));
					pstmt.setString(2, Utility.trim(studentAttendanceWrapperArray[i].academicYearID));
					pstmt.setString(3, Utility.trim(studentAttendanceWrapperArray[i].studentID));
					pstmt.setString(4, Utility.trim(studentAttendanceWrapperArray[i].gradeID));
					pstmt.setString(5, Utility.trim(studentAttendanceWrapperArray[i].sectionID));
					pstmt.setDate(6, Utility.getDate(studentAttendanceWrapperArray[i].calendarDate));
					pstmt.setString(7, Utility.trim(studentAttendanceWrapperArray[i].morningStatus));
					pstmt.setString(8, Utility.trim(studentAttendanceWrapperArray[i].eveningStatus));
					// pstmt.setString(9,Utility.trim(studentAttendanceWrapperArray[i].messageID));
					// pstmt.setString(10,Utility.trim(studentAttendanceWrapperArray[i].message));
					// pstmt.setDate(11,Utility.getDate(studentAttendanceWrapperArray[i].messageDate));
					// pstmt.setDate(12,Utility.getDate(studentAttendanceWrapperArray[i].delivered));
					pstmt.setString(9, Utility.trim(usersProfileWrapper.userid)); // makerid
					pstmt.setTimestamp(10, Utility.getCurrentTime());
					pstmt.setString(11, Utility.trim(usersProfileWrapper.schoolID)); // makerid

					pstmt.executeUpdate();
					pstmt.close();

					if (Utility.trim(studentAttendanceWrapperArray[i].morningStatus).equals("A")
							|| Utility.trim(studentAttendanceWrapperArray[i].eveningStatus).equals("A")) {
						insertStudentAttenMessage(usersProfileWrapper, studentAttendanceWrapperArray[i]);
					}

				}

				// studentAttendanceWrapper[i].recordFound=true;

			}

			FCMNotification fcmNotification = new FCMNotification("ATTENDANCE_MESSAGE", usersProfileWrapper.schoolID,null,null);
			fcmNotification.sendAttendanceMessage(usersProfileWrapper.schoolID);

			System.out.println("GCM notification ATTENDANCE_MESSAGE started");

			dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
			// dataArrayWrapper.studentAttendanceWrapper[0]=studentAttendanceWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully updateStudentAttendance Updated");

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
	// ---------------------- End updateStudentAttendance-----------------

	// ---------------------------Start
	// fetchStudentAttendance----------------------------

	public AbstractWrapper fetchStudentAttendance(UsersWrapper usersProfileWrapper,
			StudentAttendanceWrapper studentAttendanceWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;
		// String currentAcademicYear=null;

		int n = 1;
		String calendarDate = null;
		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Current Academic Year--

			if (Utility.isEmpty(studentAttendanceWrapper.academicYearID) == true) {

				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAttendanceWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------

			/*
			 * pstmt = con.prepareStatement("SELECT RefNo,StudentID, GradeID, SectionID " +
			 * "  FROM StudentAttendance WHERE AcademicYearID=? AND GradeID=? AND SectionID=? AND CalendarDate=?"
			 * );
			 * 
			 * 
			 * pstmt.setString(1,currentAcademicYear.trim());
			 * pstmt.setString(2,Utility.trim(studentAttendanceWrapper.gradeID));
			 * pstmt.setString(3,Utility.trim(studentAttendanceWrapper.sectionID));
			 * pstmt.setString(4,Utility.trim(studentAttendanceWrapper.calendarDate));
			 * 
			 * resultSet = pstmt.executeQuery();
			 * 
			 * while(resultSet.next()) { resultSet.close(); pstmt.close();
			 * 
			 * sql="INSERT INTO StudentAttendance (RefNo,AcademicYearID,StudentID,GradeID,SectionID,CalendarDate) "
			 * 
			 * +"SELECT RefNo,AcademicYearID,StudentID,GradeID,SectionID,? FROM StudentProfile WHERE RefNo NOT IN "
			 * +"(SELECT REFNO FROM StudentAttendance WHERE AcademicYearID=? AND GradeID=? AND SectionID=? AND CalendarDate=?)"
			 * ;
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * //studentAttendanceWrapper.refNo=Utility.trim(resultSet.getString("RefNo"));
			 * 
			 * pstmt.setString(1,Utility.trim(studentAttendanceWrapper.refNo));
			 * pstmt.setString(2,Utility.trim(studentAttendanceWrapper.academicYearID));
			 * pstmt.setString(3,Utility.trim(studentAttendanceWrapper.studentID));
			 * pstmt.setString(4,Utility.trim(studentAttendanceWrapper.gradeID));
			 * pstmt.setString(5,Utility.trim(studentAttendanceWrapper.sectionID));
			 * pstmt.setDate(6,Utility.getDate(studentAttendanceWrapper.calendarDate));
			 * 
			 * pstmt.setString(7,Utility.trim(studentAttendanceWrapper.refNo));
			 * pstmt.setString(8,Utility.trim(studentAttendanceWrapper.academicYearID));
			 * pstmt.setString(9,Utility.trim(studentAttendanceWrapper.gradeID));
			 * pstmt.setString(10,Utility.trim(studentAttendanceWrapper.sectionID));
			 * pstmt.setDate(11,Utility.getDate(studentAttendanceWrapper.calendarDate));
			 * 
			 * pstmt.executeUpdate(); pstmt.close();
			 * 
			 * }
			 */
			// ---------------------

			sql = "SELECT a.RefNo,a.AcademicYearID,a.StudentID,a.GradeID,a.SectionID, "
					+ "a.CalendarDate,a.MorningStatus,a.EveningStatus,a.MakerID, "
					+ "a.MakerDateTime, b.StudentName as StudentName, b.Surname as Surname, a.SchoolID as SchoolID FROM StudentAttendance a LEFT JOIN StudentProfile b "
					+ "ON a.RefNo=b.RefNo WHERE a.AcademicYearID=?  AND a.SchoolID=?";

			if (Utility.isEmpty(studentAttendanceWrapper.gradeID) == false) {

				sql = sql + " AND a.GradeID=? ";

			}
			if (Utility.isEmpty(studentAttendanceWrapper.sectionID) == false) {

				sql = sql + " AND a.SectionID=?";
			}
			if (Utility.isEmpty(studentAttendanceWrapper.calendarDate) == false) {

				sql = sql + " AND a.CalendarDate=?";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, Utility.trim(studentAttendanceWrapper.academicYearID));

			pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

			if (Utility.isEmpty(studentAttendanceWrapper.gradeID) == false) {
				pstmt.setString(++n, Utility.trim(studentAttendanceWrapper.gradeID));
			}
			if (Utility.isEmpty(studentAttendanceWrapper.sectionID) == false) {
				pstmt.setString(++n, Utility.trim(studentAttendanceWrapper.sectionID));

			}
			if (Utility.isEmpty(studentAttendanceWrapper.calendarDate) == false) {
				System.out.println("Calendar Date " + studentAttendanceWrapper.calendarDate);

				calendarDate = studentAttendanceWrapper.calendarDate;
				pstmt.setString(++n, studentAttendanceWrapper.calendarDate);

			}

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentAttendanceWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentAttendanceWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentAttendanceWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentAttendanceWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));

				studentAttendanceWrapper.calendarDate = Utility.setDate(resultSet.getString("CalendarDate"));
				studentAttendanceWrapper.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
				studentAttendanceWrapper.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
				/*
				 * studentAttendanceWrapper.messageID=Utility.trim(resultSet.getString(
				 * "MessageID"));
				 * studentAttendanceWrapper.message=Utility.trim(resultSet.getString("Message"))
				 * ; studentAttendanceWrapper.messageDate=
				 * Utility.trim(resultSet.getString("MessageDate"));
				 * studentAttendanceWrapper.delivered=
				 * Utility.trim(resultSet.getString("Delivered"));
				 */
				studentAttendanceWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentAttendanceWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));

				studentAttendanceWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentAttendanceWrapper.surname = Utility.trim(resultSet.getString("Surname"));

				studentAttendanceWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAttendanceWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				studentAttendanceWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAttendanceWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentAttendanceWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance Details fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentAttendanceWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else {
				StudentProfileHelper studentProfileHelper = new StudentProfileHelper();
				StudentProfileWrapper studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.gradeID = studentAttendanceWrapper.gradeID;
				studentProfileWrapper.sectionID = studentAttendanceWrapper.sectionID;
				DataArrayWrapper subDataArrayWrapper = (DataArrayWrapper) studentProfileHelper
						.fetchClassStudents(usersProfileWrapper, studentProfileWrapper);
				if (subDataArrayWrapper.recordFound == true && subDataArrayWrapper.studentProfileWrapper.length > 0) {
					vector.clear();
					for (int i = 0; i <= subDataArrayWrapper.studentProfileWrapper.length - 1; i++) {
						studentAttendanceWrapper = new StudentAttendanceWrapper();

						studentAttendanceWrapper.academicYearID = subDataArrayWrapper.studentProfileWrapper[i].academicYearID;
						studentAttendanceWrapper.refNo = subDataArrayWrapper.studentProfileWrapper[i].refNo;
						studentAttendanceWrapper.gradeID = subDataArrayWrapper.studentProfileWrapper[i].gradeID;
						studentAttendanceWrapper.sectionID = subDataArrayWrapper.studentProfileWrapper[i].sectionID;
						studentAttendanceWrapper.studentID = subDataArrayWrapper.studentProfileWrapper[i].studentID;
						studentAttendanceWrapper.calendarDate = calendarDate;
						studentAttendanceWrapper.studentName = subDataArrayWrapper.studentProfileWrapper[i].studentName;
						studentAttendanceWrapper.surname = subDataArrayWrapper.studentProfileWrapper[i].surname;

						studentAttendanceWrapper.schoolID = subDataArrayWrapper.studentProfileWrapper[i].schoolID;

						studentAttendanceWrapper.morningStatus = "";
						studentAttendanceWrapper.eveningStatus = "";

						studentAttendanceWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
								studentAttendanceWrapper.academicYearID, "MST_AcademicYear",
								usersProfileWrapper.schoolID);
						studentAttendanceWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(
								studentAttendanceWrapper.gradeID, "MST_Grade", usersProfileWrapper.schoolID);
						studentAttendanceWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
								studentAttendanceWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);

						studentAttendanceWrapper.recordFound = true;

						vector.addElement(studentAttendanceWrapper);
					}
					if (vector.size() > 0) {
						dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[vector.size()];
						vector.copyInto(dataArrayWrapper.studentAttendanceWrapper);
						dataArrayWrapper.recordFound = true;

						System.out.println("total trn. in fetch " + vector.size());

					}
				} else {
					dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
					dataArrayWrapper.studentAttendanceWrapper[0] = studentAttendanceWrapper;
					dataArrayWrapper.recordFound = true;

				}

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

	// ---------------------------End
	// fetchStudentAcademics----------------------------

	// ---------------------------Start
	// fetchStudentAttendanceByStudent----------------------------

	public AbstractWrapper fetchAttendanceByStudent(UsersWrapper usersProfileWrapper,
			StudentAttendanceWrapper studentAttendanceWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		// String sql=null;
		// String currentAcademicYear=null;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Current Academic Year--

			if (Utility.isEmpty(studentAttendanceWrapper.academicYearID) == true) {

				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAttendanceWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------

			pstmt = con.prepareStatement("SELECT RefNo, AcademicYearID, StudentID, GradeID, SectionID, "
					+ "CalendarDate, MorningStatus, EveningStatus,MakerID, "
					+ "MakerDateTime, SchoolID FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? "
					+ "AND GradeID=? AND SectionID=? and SchoolID=? ORDER BY CalendarDate DESC ");

			System.out.println("refNo " + studentAttendanceWrapper.refNo);
			// System.out.println("currentAcademicYear "+currentAcademicYear);
			System.out.println("gradeID " + studentAttendanceWrapper.gradeID);
			System.out.println("sectionID " + studentAttendanceWrapper.sectionID);
			System.out.println("schoolID " + usersProfileWrapper.schoolID);

			pstmt.setString(1, Utility.trim(studentAttendanceWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(studentAttendanceWrapper.refNo));
			pstmt.setString(3, Utility.trim(studentAttendanceWrapper.gradeID));
			pstmt.setString(4, Utility.trim(studentAttendanceWrapper.sectionID));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("pstmt " + pstmt);

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentAttendanceWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentAttendanceWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentAttendanceWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentAttendanceWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));

				studentAttendanceWrapper.calendarDate = Utility.setDate(resultSet.getString("CalendarDate"));
				studentAttendanceWrapper.calendarDateMMM = Utility.setDateMMM(resultSet.getString("CalendarDate"));

				studentAttendanceWrapper.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
				studentAttendanceWrapper.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
				/*
				 * studentAttendanceWrapper.messageID=Utility.trim(resultSet.getString(
				 * "MessageID"));
				 * studentAttendanceWrapper.message=Utility.trim(resultSet.getString("Message"))
				 * ; studentAttendanceWrapper.messageDate=
				 * Utility.setDate(resultSet.getString("MessageDate"));
				 * studentAttendanceWrapper.delivered=
				 * Utility.trim(resultSet.getString("Delivered"));
				 */
				studentAttendanceWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentAttendanceWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				studentAttendanceWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAttendanceWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				studentAttendanceWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAttendanceWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentAttendanceWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance By Student Details fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentAttendanceWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else {
				dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
				dataArrayWrapper.studentAttendanceWrapper[0] = studentAttendanceWrapper;
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

	// ---------------------------End
	// fetchStudentAcademicsByStudent----------------------------

	// ---------------------- Start insertStudentAttenMessage-----------------
	public AbstractWrapper insertStudentAttenMessage(UsersWrapper usersProfileWrapper,
			StudentAttendanceWrapper studentAttendanceWrapper) throws Exception {

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

		String sql = null;
		PreparedStatement pstmt = null;

		String absentMessage = null;

		try {

			con = getConnection();

			// -----AbsentMessage code--

			PopoverHelper popoverHelper = new PopoverHelper();
			ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
					.fetchParameters(usersProfileWrapper.schoolID);
			absentMessage = parameterWrapper.absentMessage;

			// ------------

			sql = "INSERT INTO StudentAttenMessage(RefNo,AcademicYearID,StudentID,GradeID,SectionID, "
					+ "CalendarDate,MorningStatus,EveningStatus,MessageID,Message,Delivered,MakerID,MakerDateTime, SchoolID) "
					+ "  Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(studentAttendanceWrapper.refNo));
			pstmt.setString(2, Utility.trim(studentAttendanceWrapper.academicYearID));
			pstmt.setString(3, Utility.trim(studentAttendanceWrapper.studentID));
			pstmt.setString(4, Utility.trim(studentAttendanceWrapper.gradeID));
			pstmt.setString(5, Utility.trim(studentAttendanceWrapper.sectionID));
			pstmt.setDate(6, Utility.getDate(studentAttendanceWrapper.calendarDate));
			pstmt.setString(7, Utility.trim(studentAttendanceWrapper.morningStatus));
			pstmt.setString(8, Utility.trim(studentAttendanceWrapper.eveningStatus));
			pstmt.setString(9, generateMessageID(usersProfileWrapper.schoolID));
			pstmt.setString(10, absentMessage);
			pstmt.setString(11, "N");
			pstmt.setString(12, Utility.trim(usersProfileWrapper.userid)); // makerid
			pstmt.setTimestamp(13, Utility.getCurrentTime());
			pstmt.setString(14, Utility.trim(usersProfileWrapper.schoolID)); // makerid

			pstmt.executeUpdate();
			pstmt.close();

			System.out.println("Successfully inserted into Student Attendance Message ");

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
	// ---------------------- End insertStudentAttenMessage-----------------

	// ---------------------- Start updateStudentAttenMessage-----------------
	/*
	 * public AbstractWrapper updateStudentAttenMessage(UsersWrapper
	 * usersProfileWrapper,StudentAttendanceWrapper studentAttendanceWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	 * 
	 * 
	 * //SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	 * 
	 * DecimalFormat formatter = (DecimalFormat)
	 * NumberFormat.getInstance(Locale.US); DecimalFormatSymbols symbols =
	 * formatter.getDecimalFormatSymbols(); symbols.setGroupingSeparator(',');
	 * formatter.applyPattern("###,###,###,##0.00");
	 * formatter.setDecimalFormatSymbols(symbols);
	 * 
	 * String sql=null; PreparedStatement pstmt=null;
	 * 
	 * try { con = getConnection();
	 * 
	 * 
	 * sql="UPDATE StudentAttenMessage SET MorningStatus=?,EveningStatus=? WHERE AcademicYearID=? AND RefNo=? "
	 * + "AND StudentID=? AND GradeID=? AND SectionID=? AND CalendarDate=?";
	 * 
	 * pstmt=con.prepareStatement(sql);
	 * 
	 * pstmt.setString(1,Utility.trim(studentAttendanceWrapper.morningStatus));
	 * pstmt.setString(2,Utility.trim(studentAttendanceWrapper.eveningStatus));
	 * pstmt.setString(3,Utility.trim(studentAttendanceWrapper.academicYearID));
	 * pstmt.setString(4,Utility.trim(studentAttendanceWrapper.refNo));
	 * pstmt.setString(5,Utility.trim(studentAttendanceWrapper.studentID));
	 * pstmt.setString(6,Utility.trim(studentAttendanceWrapper.gradeID));
	 * pstmt.setString(7,Utility.trim(studentAttendanceWrapper.sectionID));
	 * pstmt.setDate(8,Utility.getDate(studentAttendanceWrapper.calendarDate));
	 * 
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * System.out.println("Successfully Student Attendance Message updated");
	 * 
	 * 
	 * 
	 * 
	 * dataArrayWrapper.studentAttendanceWrapper=new StudentAttendanceWrapper[1];
	 * //dataArrayWrapper.studentAttendanceWrapper[0]=studentAttendanceWrapper;
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * 
	 * 
	 * System.out.println("Successfully updateStudentAttendance Updated");
	 * 
	 * } catch (SQLException se) { se.printStackTrace(); throw new
	 * SQLException(se.getSQLState()+ " ; "+ se.getMessage()); } catch
	 * (NamingException ne) { ne.printStackTrace(); throw new
	 * NamingException(ne.getMessage()); } catch (Exception ex) {
	 * ex.printStackTrace(); throw new Exception(ex.getMessage()); } finally { try {
	 * releaseConnection(resultSet, con); } catch (SQLException se) {
	 * se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
	 * se.getMessage()); } }
	 * 
	 * return dataArrayWrapper; }
	 */
	// ---------------------- End updateStudentAttenMessage-----------------

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

			pstmt.setString(2, Utility.trim(schoolID));
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

			System.out.println("Successfully generated Attendance messageID " + finalMessageID);

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

	// ---------------------------Start
	// fetchStudentAttendanceCount----------------------------

	public AbstractWrapper fetchStudentAttendanceCount(UsersWrapper usersProfileWrapper,
			StudentAttendanceWrapper studentAttendanceWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;

		String academicYearID = null;
		String refNo = null;
		String studentID = null;
		//String schoolID = null;
		String sql=null;

		//academicYearID = Utility.trim(studentAttendanceWrapper.academicYearID);
		//schoolID = Utility.trim(usersProfileWrapper.schoolID);
		
		String orderBy = null;
		String groupName = null;

		try {

			refNo = Utility.trim(studentAttendanceWrapper.refNo);
			studentID = Utility.trim(studentAttendanceWrapper.studentID);

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();
			
			// ----Current Academic Year--

			if (Utility.isEmpty(studentAttendanceWrapper.academicYearID) == true) {

				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------
			
			groupName="DAYNAME";
			orderBy="DAY";
			
			if(!Utility.isEmpty(studentAttendanceWrapper.duration))
			{
				if(studentAttendanceWrapper.duration.equals("YEAR"))
				{
					groupName="MONTHNAME";
					orderBy="MONTH";
				}
			}


			// ------------------Attendance Present---------------------

			sql="SELECT COUNT(MorningStatus) as MorningStatus, "+ groupName + "(CalendarDate) as CalendarDate "
					+ "FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? AND StudentID=? "
					+ "AND MorningStatus='P' and SchoolID=? ";
			
			if(!Utility.isEmpty(studentAttendanceWrapper.duration))
			{
				if(studentAttendanceWrapper.duration.equals("24HRS"))
				{
					sql = sql + " and CalendarDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY) ";
				}
				else if(studentAttendanceWrapper.duration.equals("7DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 7 DAY) ";
					
				}
				else if(studentAttendanceWrapper.duration.equals("30DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 30 DAY) ";
					
				}
			}

					
			sql = sql + " Group BY "+groupName +"(CalendarDate)";// ORDER BY "+ orderBy +"(CalendarDate)";
			
			pstmt = con.prepareStatement(sql);
					

			System.out.println("sql " + sql);
			//System.out.println("AcademicYear " + studentAttendanceWrapper.academicYearID);

			pstmt.setString(1, academicYearID);
			pstmt.setString(2, refNo);
			pstmt.setString(3, studentID);
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));


			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
				studentAttendanceWrapper.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance Count morningStatus fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.morningStatusPresentWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.morningStatusPresentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			sql= "SELECT COUNT(EveningStatus) as EveningStatus, " + groupName +" (CalendarDate) as CalendarDate "
					+ "FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? AND StudentID=? "
					+ "AND EveningStatus='P' AND SchoolID=? ";
			
			if(!Utility.isEmpty(studentAttendanceWrapper.duration))
			{
				if(studentAttendanceWrapper.duration.equals("24HRS"))
				{
					sql = sql + " and CalendarDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY) ";
				}
				else if(studentAttendanceWrapper.duration.equals("7DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 7 DAY) ";
					
				}
				else if(studentAttendanceWrapper.duration.equals("30DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 30 DAY) ";
					
				}
			}

					
			sql = sql + " Group BY "+groupName +"(CalendarDate)";// ORDER BY "+ orderBy +"(CalendarDate)";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, academicYearID);
			pstmt.setString(2, refNo);
			pstmt.setString(3, studentID);
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));


			resultSet = pstmt.executeQuery();
			vector.clear();
			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
				studentAttendanceWrapper.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance Count EveningStatus fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.eveningStatusPresentWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.eveningStatusPresentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			// ------------------Attendance Absent---------------------

			pstmt = con.prepareStatement(
					"SELECT COUNT(MorningStatus) as MorningStatus, MONTHNAME(CalendarDate) as CalendarDate "
							+ "FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? AND StudentID=? "
							+ "AND MorningStatus='A'  and SchoolID=? GROUP BY MONTH(CalendarDate)");// ORDER BY MONTH(CalendarDate)");

			System.out.println("refNo " + studentAttendanceWrapper.refNo);
			//System.out.println("AcademicYear " + studentAttendanceWrapper.academicYearID);

			pstmt.setString(1, academicYearID);
			pstmt.setString(2, refNo);
			pstmt.setString(3, studentID);
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));


			resultSet = pstmt.executeQuery();
			vector.clear();

			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
				studentAttendanceWrapper.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance Count morningStatus fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.morningStatusAbsentWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.morningStatusAbsentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			pstmt = con.prepareStatement(
					"SELECT COUNT(EveningStatus) as EveningStatus, MONTHNAME(CalendarDate) as CalendarDate "
							+ "FROM StudentAttendance WHERE AcademicYearID=? AND RefNo=? AND StudentID=? "
							+ "AND EveningStatus='A' and SchoolID=? GROUP BY MONTH(CalendarDate)");// ORDER BY MONTH(CalendarDate)");

			pstmt.setString(1, academicYearID);
			pstmt.setString(2, refNo);
			pstmt.setString(3, studentID);
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));


			resultSet = pstmt.executeQuery();
			vector.clear();
			while (resultSet.next()) {
				studentAttendanceWrapper = new StudentAttendanceWrapper();

				studentAttendanceWrapper.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
				studentAttendanceWrapper.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				studentAttendanceWrapper.recordFound = true;
				System.out.println("StudentAttendance Count EveningStatus fetch successful");

				vector.addElement(studentAttendanceWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.eveningStatusAbsentWrapper = new StudentAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.eveningStatusAbsentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

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

	// ---------------------------End
	// fetchStudentAttendanceCount----------------------------

}
