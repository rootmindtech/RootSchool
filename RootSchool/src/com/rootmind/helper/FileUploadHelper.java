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
import com.rootmind.wrapper.FileUploadWrapper;
import com.rootmind.wrapper.StudentAcademicsWrapper;
import com.rootmind.wrapper.StudentAttendanceWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class FileUploadHelper extends Helper {

	// ---------------------------Start
	// fetchStudentAcademicsTemp----------------------------

	public AbstractWrapper fetchStudentAcademicsTemp() throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchStudentAcademics RefNo " +
		// studentAcademicsWrapper.refNo);

		// System.out.println("fetchStudentAcademics studentID" +
		// studentAcademicsWrapper.studentID);

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		StudentAcademicsWrapper studentAcademicsWrapper = null;
		// String sql=null;
		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			System.out.println("fetch StudentAcademics Temp");

			// ----------
			pstmt = con
					.prepareStatement("SELECT Refno, Studentid, Gradeid, Sectionid, Academicyearid, TermID, Subjectid, "
							+ " TargetMarks, SecuredMarks, Percentage, RankID, MakerID, MakerDateTime, ModifierID, ModifierDateTime, "
							+ " Grade, GroupTerm, SchoolID from StudentAcademicsTemp Where SchoolID=?");

			/*
			 * System.out.println("current academic yearID "+studentAcademicsWrapper.
			 * academicYearID);
			 * pstmt.setString(1,Utility.trim(studentAcademicsWrapper.academicYearID));
			 * pstmt.setString(2,Utility.trim(studentAcademicsWrapper.gradeID));
			 * pstmt.setString(3,Utility.trim(studentAcademicsWrapper.sectionID));
			 * pstmt.setString(4,Utility.trim(studentAcademicsWrapper.termID));
			 * pstmt.setString(5,Utility.trim(studentAcademicsWrapper.subjectID));
			 */

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				studentAcademicsWrapper = new StudentAcademicsWrapper();

				studentAcademicsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentAcademicsWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentAcademicsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentAcademicsWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentAcademicsWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentAcademicsWrapper.termID = Utility.trim(resultSet.getString("TermID"));
				studentAcademicsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));
				studentAcademicsWrapper.targetMarks = Utility.trim(resultSet.getString("TargetMarks"));
				studentAcademicsWrapper.securedMarks = Utility.trim(resultSet.getString("SecuredMarks"));
				studentAcademicsWrapper.percentage = Utility.trim(resultSet.getString("Percentage"));
				studentAcademicsWrapper.rankID = Utility.trim(resultSet.getString("RankID"));
				studentAcademicsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentAcademicsWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));

				studentAcademicsWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				studentAcademicsWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				// studentAcademicsWrapper.studentName=
				// Utility.trim(resultSet.getString("StudentName"));
				// studentAcademicsWrapper.surname=
				// Utility.trim(resultSet.getString("Surname"));

				studentAcademicsWrapper.grade = Utility.trim(resultSet.getString("Grade"));
				studentAcademicsWrapper.groupTerm = Utility.trim(resultSet.getString("GroupTerm"));
				studentAcademicsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAcademicsWrapper.recordFound = true;

				studentAcademicsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.termID,
						"MST_Term", studentAcademicsWrapper.schoolID);
				studentAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.subjectID, "MST_Subject", studentAcademicsWrapper.schoolID);
				studentAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.gradeID,
						"MST_Grade", studentAcademicsWrapper.schoolID);
				studentAcademicsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.sectionID, "MST_Section", studentAcademicsWrapper.schoolID);
				studentAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.academicYearID, "MST_AcademicYear", studentAcademicsWrapper.schoolID);
				studentAcademicsWrapper.rankIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.rankID,
						"MST_Rank", studentAcademicsWrapper.schoolID);
				System.out.println("fetch StudentAcademics Temp  successful");

				vector.addElement(studentAcademicsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentAcademicsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
				dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
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
	// fetchStudentAcademics----------------------------

	public AbstractWrapper insertStudentAcademicsTemp(UsersWrapper usersProfileWrapper, String[] lineData)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			sql = "INSERT INTO StudentAcademicsTemp(RefNo, StudentID, GradeID, SectionID, AcademicYearID, TermID, SubjectID, TargetMarks, "
					+ " SecuredMarks, Percentage, RankID, GroupTerm, Grade, MakerID, MakerDateTime, UploadStatus, UploadRemarks, SchoolID) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(lineData[0])); // refNo
			pstmt.setString(2, Utility.trim(lineData[1]));// studentID
			pstmt.setString(3, Utility.trim(lineData[2]));// gradeID
			pstmt.setString(4, Utility.trim(lineData[3]));// sectionID
			pstmt.setString(5, Utility.trim(lineData[4]));// academicYearID
			pstmt.setString(6, Utility.trim(lineData[5]));// termID
			pstmt.setString(7, Utility.trim(lineData[6]));// subjectID
			pstmt.setString(8, Utility.trim(lineData[7]));// targetMarks
			pstmt.setString(9, Utility.trim(lineData[8]));// securedMarks
			pstmt.setString(10, Utility.trim(lineData[9]));// percentage
			pstmt.setString(11, Utility.trim(lineData[10]));// rankID
			pstmt.setString(12, lineData[11]);// groupTerm
			pstmt.setString(13, lineData[12]);// grade
			pstmt.setString(14, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(15, Utility.getCurrentTime()); // makerdatetime
			pstmt.setString(16, "I");// "I"
			pstmt.setString(17, "Upload Remarks");// Upload Remarks
			pstmt.setString(18, Utility.trim(usersProfileWrapper.schoolID));// rankID

			System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid);

			pstmt.executeUpdate();
			pstmt.close();

			/*
			 * studentAcademicsWrapper.recordFound=true;
			 * dataArrayWrapper.studentAcademicsWrapper=new StudentAcademicsWrapper[1];
			 * dataArrayWrapper.studentAcademicsWrapper[0]=studentAcademicsWrapper;
			 */

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentAcademicsTemp");

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
	// insertStudentAcademicsTemp----------------------------

	// ---------------------------Start
	// fetchStudentAttendanceTemp----------------------------

	public AbstractWrapper fetchStudentAttendanceTemp(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		StudentAttendanceWrapper studentAttendanceWrapper = null;

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		// String sql=null;
		// String currentAcademicYear=null;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			System.out.println("fetch StudentAttendance Temp");

			/*
			 * //----Current Academic Year--
			 * 
			 * pstmt =
			 * con.prepareStatement("SELECT CurrentAcademicYear from MST_Parameter");
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * currentAcademicYear=resultSet.getString("CurrentAcademicYear");
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */

			pstmt = con.prepareStatement("SELECT RefNo, AcademicYearID, StudentID, GradeID, SectionID, "
					+ " CalendarDate, MorningStatus, EveningStatus, MakerID, "
					+ " MakerDateTime, SchoolID FROM StudentAttendanceTemp where SchoolID=?");

			/*
			 * pstmt.setString(1,currentAcademicYear.trim());
			 * pstmt.setString(2,Utility.trim(studentAttendanceWrapper.gradeID));
			 * pstmt.setString(3,Utility.trim(studentAttendanceWrapper.sectionID));
			 * System.out.println("Calendar Date "+studentAttendanceWrapper.calendarDate);
			 */
			pstmt.setString(1, schoolID);

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

				// studentAttendanceWrapper.studentName=Utility.trim(resultSet.getString("StudentName"));
				studentAttendanceWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAttendanceWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.academicYearID, "MST_AcademicYear", studentAttendanceWrapper.schoolID);
				studentAttendanceWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAttendanceWrapper.gradeID,
						"MST_Grade", studentAttendanceWrapper.schoolID);
				studentAttendanceWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAttendanceWrapper.sectionID, "MST_Section", studentAttendanceWrapper.schoolID);

				studentAttendanceWrapper.recordFound = true;
				System.out.println("fetchStudentAttendanceTemp Details fetch successful");

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
	// fetchStudentAttendanceTemp----------------------------

	// ---------------------- Start insertStudentAttendanceTemp-----------------
	public AbstractWrapper insertStudentAttendanceTemp(UsersWrapper usersProfileWrapper, String[] lineData)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		String sql = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			sql = "INSERT INTO StudentAttendanceTemp(RefNo,AcademicYearID,StudentID,GradeID,SectionID, "
					+ "CalendarDate,MorningStatus,EveningStatus,MakerID,MakerDateTime, UploadStatus, UploadRemarks, SchoolID)  "
					+ "Values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(lineData[0])); // refNo
			pstmt.setString(2, Utility.trim(lineData[1]));// academicYearID
			pstmt.setString(3, Utility.trim(lineData[2])); // studentID
			pstmt.setString(4, Utility.trim(lineData[3])); // gradeID
			pstmt.setString(5, Utility.trim(lineData[4])); // sectionID
			pstmt.setString(6, lineData[5]); // calendarDate
			pstmt.setString(7, Utility.trim(lineData[6])); // morningStatus
			pstmt.setString(8, Utility.trim(lineData[7])); // eveningStatus
			pstmt.setString(9, Utility.trim(usersProfileWrapper.userid)); // makerid
			pstmt.setTimestamp(10, Utility.getCurrentTime());
			pstmt.setString(11, "I");// "I"
			pstmt.setString(12, "Upload Remarks");// Upload Remarks
			pstmt.setString(13, Utility.trim(usersProfileWrapper.schoolID)); // makerid

			pstmt.executeUpdate();
			pstmt.close();

			// studentAttendanceWrapper[i].recordFound=true;

			// AY1617
			// AY1622 - Invalid AcademicYearID, UploadStatus='R'
			// StudentId - SIS201600001, SHS20220005 - UploadRemarks - Invalid
			// AcademicYearID,InvalidStudentID
			dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
			// dataArrayWrapper.studentAttendanceWrapper[0]=studentAttendanceWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted StudentAttendanceTemp ");

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
	// ---------------------- End insertStudentAttendanceTemp-----------------

	public AbstractWrapper fileUploadManager(UsersWrapper userProfileWrapper, FileUploadWrapper fileUploadWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// int fileLength;
		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		// File file=new File(imageDetailsWrapper.imageFile);
		// FileInputStream fis = new FileInputStream(file);
		// fileLength = (int)file.length();
		// System.out.println("Image File Length "+fileLength);

		try {

			if (fileUploadWrapper.fileUploadStatus == true) {
				con = getConnection();

				sql = "INSERT INTO FileUploadManager(FileRefNo, FileTemplate, DataFileName, DataFileDateTime, "
						+ "MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID) Values(?,?,?,?,?,?,?,?,?)";

				System.out.println("sql " + sql);
				System.out.println("Image File Name " + fileUploadWrapper.dataFileName);
				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(1, generateFileRefNo(userProfileWrapper.schoolID)); // fileRefNo
				pstmt.setString(2, Utility.trim(fileUploadWrapper.fileTemplateID));
				pstmt.setString(3, Utility.trim(fileUploadWrapper.dataFileName));

				// pstmt.setBinaryStream(4, fis, fileLength);
				pstmt.setString(4, Utility.trim(fileUploadWrapper.dataFileDateTime));

				pstmt.setString(5, Utility.trim(userProfileWrapper.userid));
				pstmt.setTimestamp(6, Utility.getCurrentTime()); // makerdatetime

				pstmt.setString(7, Utility.trim(userProfileWrapper.userid));
				pstmt.setTimestamp(8, Utility.getCurrentTime()); // ModifierDateTime
				pstmt.setString(9, Utility.trim(userProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				fileUploadWrapper.recordFound = true;

				// insertStudentAcademicsTemp();

				System.out.println("file upload manager successflly");
			}

			dataArrayWrapper.fileUploadWrapper = new FileUploadWrapper[1];
			dataArrayWrapper.fileUploadWrapper[0] = fileUploadWrapper;
			dataArrayWrapper.recordFound = true;

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

	// ---------------------------Start fetchGrade----------------------------

	public String fetchGrade(String securedMarks, String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		String grade = null;

		try {
			con = getConnection();

			// --fetch Grade
			if (securedMarks != null) {

				sql = "SELECT Grade FROM MST_GradingScale WHERE MinMarks<=" + Integer.parseInt(securedMarks) + " "
						+ " AND MaxMarks>=" + Integer.parseInt(securedMarks) + " and SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, schoolID);

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					grade = Utility.trim(resultSet.getString("Grade"));

				}
				resultSet.close();
				pstmt.close();

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

		return grade;
	}
	// ---------------------------End fetchGrade----------------------------

	// -----------------Generate File RefNo-------------------------------
	public String generateFileRefNo(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		int fileRefNo = 0;
		String finalFileRefNo = null;
		String schoolCode = null;

		try {
			con = getConnection();

			sql = "SELECT RefNo,SchoolCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				fileRefNo = resultSet.getInt("RefNo");
				System.out.println("RefNo" + fileRefNo);
				schoolCode = resultSet.getString("SchoolCode");

			}

			resultSet.close();
			pstmt.close();

			if (fileRefNo == 0) {
				fileRefNo = 1;

			} else {

				fileRefNo = fileRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set RefNo=? WHERE SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, fileRefNo);

			pstmt.setString(2, schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			// int paddingSize=6-String.valueOf(refNo).length();

			// System.out.println("Savings Account " + studentProfileWrapper.accountType);

			// System.out.println("Savings Account " +
			// studentProfileWrapper.accountType.substring(0,2));

			finalFileRefNo = schoolCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", fileRefNo);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated File Ref no " + finalFileRefNo);

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

		return finalFileRefNo;
	}

	// -----------------End Generate File RefNo---------------------------

	// ---------------------- Start fetchDataFileTemplate-----------------
	public FileUploadWrapper fetchDataFileTemplate(UsersWrapper usersProfileWrapper,
			FileUploadWrapper fileUploadWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("SELECT Code, Description, FileOperation, FileSeparator, "
					+ "SourcePath, DestinationPath, BackupPath from MST_DataFileTemplate WHERE Code=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(fileUploadWrapper.fileTemplateID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				fileUploadWrapper.fileTemplateName = Utility.trim(resultSet.getString("Description"));
				fileUploadWrapper.fileOperation = Utility.trim(resultSet.getString("FileOperation"));
				fileUploadWrapper.fileSeperator = Utility.trim(resultSet.getString("FileSeparator"));
				fileUploadWrapper.sourcePath = Utility.trim(resultSet.getString("SourcePath"));
				fileUploadWrapper.destinationPath = Utility.trim(resultSet.getString("DestinationPath"));
				fileUploadWrapper.backupPath = Utility.trim(resultSet.getString("BackupPath"));

			}

			resultSet.close();
			pstmt.close();

			// ----------

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

		return fileUploadWrapper;
	}
	// ---------------------- End fetchDataFileTemplate-----------------

	// -----insert into Student Attendance Temp History------------------

	public AbstractWrapper insertStudentAttendanceTempHistory(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;
		StudentAttendanceWrapper studentAttendanceWrapper = new StudentAttendanceWrapper();

		try {
			// ----insert into studentAttendance Temp History

			con = getConnection();

			sql = "INSERT INTO StudentAttendanceTempHistory(RefNo,AcademicYearID,StudentID,GradeID,SectionID, "
					+ "CalendarDate,MorningStatus,EveningStatus,MakerID,MakerDateTime, UploadStatus, UploadRemarks, SchoolID) SELECT RefNo, AcademicYearID, "
					+ "StudentID, GradeID, SectionID, CalendarDate, MorningStatus, EveningStatus, MakerID, "
					+ " MakerDateTime, 'U', UploadRemarks, SchoolID FROM StudentAttendanceTemp WHERE UploadStatus='I' and SchoolID=?"; //

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			// UPDATE StudentAttendanceTempHistory set UploadStatus='U' where
			// UploadStatus='I';
			// DELETE FROM StudentAttendanceTemp Where UploadStatus='I';

			/*
			 * sql="UPDATE StudentAttendanceTempHistory SET UploadStatus='U' WHERE UploadStatus='I'"
			 * ; pstmt=con.prepareStatement(sql); pstmt.executeUpdate(); pstmt.close();
			 */

			sql = "DELETE FROM StudentAttendanceTemp WHERE UploadStatus='I' and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			studentAttendanceWrapper.recordFound = true;

			dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
			dataArrayWrapper.studentAttendanceWrapper[0] = studentAttendanceWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentAttendanceTempHistory");

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

	// -----end student attendance temp history--------------------------

	// -----insert into Student Academics Temp History------------------

	public AbstractWrapper insertStudentAcademicsTempHistory(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;
		StudentAcademicsWrapper studentAcademicsWrapper = new StudentAcademicsWrapper();

		try {
			// ----insert into StudentAcademics Temp History

			con = getConnection();

			sql = "INSERT INTO StudentAcademicsTempHistory(RefNo, StudentID, GradeID, SectionID, AcademicYearID, TermID, SubjectID, TargetMarks, "
					+ " SecuredMarks, Percentage, RankID, GroupTerm, Grade, MakerID, MakerDateTime, UploadStatus, UploadRemarks, SchoolID) SELECT RefNo, Studentid, Gradeid, "
					+ " Sectionid, Academicyearid,TermID,Subjectid, TargetMarks, SecuredMarks, Percentage, RankID, GroupTerm, Grade , MakerID, "
					+ " MakerDateTime, 'U', UploadRemarks, SchoolID from StudentAcademicsTemp WHERE UploadStatus='I' and SchoolID=?";

			System.out.println("sql " + sql);
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			/*
			 * sql="UPDATE StudentAcademicsTempHistory SET UploadStatus='U' WHERE UploadStatus='I'"
			 * ; pstmt=con.prepareStatement(sql); pstmt.executeUpdate(); pstmt.close();
			 */

			sql = "DELETE FROM StudentAcademicsTemp WHERE UploadStatus='I' and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			studentAcademicsWrapper.recordFound = true;

			dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
			dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentAcademicsTempHistory");

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

	// ---------------------------Start
	// deleteStudentAcademicsTemp----------------------------

	public AbstractWrapper deleteStudentAcademicsTemp(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchStudentAcademics RefNo " +
		// studentAcademicsWrapper.refNo);

		// System.out.println("fetchStudentAcademics studentID" +
		// studentAcademicsWrapper.studentID);

		PreparedStatement pstmt = null;
		StudentAcademicsWrapper studentAcademicsWrapper = new StudentAcademicsWrapper();

		try {

			con = getConnection();

			System.out.println("delete StudentAcademics Temp");

			pstmt = con.prepareStatement("DELETE from StudentAcademicsTemp where SchoolID=?");

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			studentAcademicsWrapper.recordFound = true;

			dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
			dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
			dataArrayWrapper.recordFound = true;

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
	// deleteStudentAcademicsTemp----------------------------

	// ---------------------------Start
	// deleteStudentAttendanceTemp----------------------------

	public AbstractWrapper deleteStudentAttendanceTemp(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// System.out.println("fetchStudentAcademics RefNo " +
		// studentAcademicsWrapper.refNo);

		// System.out.println("fetchStudentAcademics studentID" +
		// studentAcademicsWrapper.studentID);

		PreparedStatement pstmt = null;
		StudentAttendanceWrapper studentAttendanceWrapper = new StudentAttendanceWrapper();

		try {

			con = getConnection();

			System.out.println("delete StudentAttendance Temp");

			pstmt = con.prepareStatement("DELETE from StudentAttendanceTemp where SchoolID=?");

			pstmt.setString(1, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();

			pstmt.close();

			studentAttendanceWrapper.recordFound = true;

			dataArrayWrapper.studentAttendanceWrapper = new StudentAttendanceWrapper[1];
			dataArrayWrapper.studentAttendanceWrapper[0] = studentAttendanceWrapper;
			dataArrayWrapper.recordFound = true;

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
	// deleteStudentAcademicsTemp----------------------------
	// -----end student academics temp history--------------------------

	// New Method
	// Existing Code - UpdateStudentAttendance
	// INSERT INTO StudentAttendanceTempHistory (RefNo,) SELECT RefNo from
	// StudentAttendanceTemp;
	// UPDATE StudentAttendanceTempHistory set UploadStatus='U' where
	// UploadStatus='I';
	// DELETE FROM StudentAttendanceTemp Where UploadStatus='I';
	// Screen - clear table
}
