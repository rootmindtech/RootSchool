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
import com.rootmind.wrapper.ExamCalendarWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ExamCalendarHelper extends Helper {

	// ------------------------ Start insertExamCalendar-------------------------

	public AbstractWrapper insertExamCalendar(UsersWrapper usersProfileWrapper, ExamCalendarWrapper examCalendarWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			sql = " INSERT INTO ExamCalendar(AcademicYearID, GradeID, TermID, SubjectID, ExamDate, StatusID,TargetMarks, MakerID, MakerDateTime, SchoolID) Values(?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(examCalendarWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(examCalendarWrapper.gradeID));
			pstmt.setString(3, Utility.trim(examCalendarWrapper.termID));
			pstmt.setString(4, Utility.trim(examCalendarWrapper.subjectID));
			pstmt.setDate(5, Utility.getDate(examCalendarWrapper.examDate));
			pstmt.setString(6, Utility.trim(examCalendarWrapper.statusID));
			pstmt.setString(7, Utility.trim(examCalendarWrapper.targetMarks));
			pstmt.setString(8, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(9, Utility.getCurrentTime());
			pstmt.setString(10, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			examCalendarWrapper.recordFound = true;

			dataArrayWrapper.examCalendarWrapper = new ExamCalendarWrapper[1];
			dataArrayWrapper.examCalendarWrapper[0] = examCalendarWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into Exam Calendar");

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
	// ------------------------ End insertExamCalendar-------------------------

	// ------------------------ Start updateExamCalendar-------------------------
	public AbstractWrapper updateExamCalendar(UsersWrapper usersProfileWrapper,
			ExamCalendarWrapper[] examCalendarWrapperArray) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		System.out.println("Exam Calendar Update ");

		try {

			con = getConnection();

			for (int i = 0; i <= examCalendarWrapperArray.length - 1; i++) {

				pstmt = con.prepareStatement("SELECT AcademicYearID,GradeID,TermID,SubjectID FROM ExamCalendar "
						+ " WHERE AcademicYearID=? AND GradeID=? AND TermID=? AND SubjectID=? AND SchoolID=?");

				pstmt.setString(1, Utility.trim(examCalendarWrapperArray[i].academicYearID));
				pstmt.setString(2, Utility.trim(examCalendarWrapperArray[i].gradeID));
				pstmt.setString(3, Utility.trim(examCalendarWrapperArray[i].termID));
				pstmt.setString(4, Utility.trim(examCalendarWrapperArray[i].subjectID));
				pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

				resultSet = pstmt.executeQuery();
				if (!resultSet.next()) {
					resultSet.close();
					pstmt.close();
					dataArrayWrapper = (DataArrayWrapper) insertExamCalendar(usersProfileWrapper,
							examCalendarWrapperArray[i]);
				} else {

					resultSet.close();
					pstmt.close();

					pstmt = con.prepareStatement("UPDATE ExamCalendar SET  ExamDate=?, StatusID=?,TargetMarks=?, "
							+ " ModifierID=?, ModifierDateTime=? WHERE AcademicYearID=? AND GradeID=? AND TermID=? AND SubjectID=? AND SchoolID=?");

					pstmt.setDate(1, Utility.getDate(examCalendarWrapperArray[i].examDate));
					pstmt.setString(2, Utility.trim(examCalendarWrapperArray[i].statusID));
					pstmt.setString(3, Utility.trim(examCalendarWrapperArray[i].targetMarks));
					pstmt.setString(4, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(5, Utility.getCurrentTime());

					pstmt.setString(6, Utility.trim(examCalendarWrapperArray[i].academicYearID));
					pstmt.setString(7, Utility.trim(examCalendarWrapperArray[i].gradeID));
					pstmt.setString(8, Utility.trim(examCalendarWrapperArray[i].termID));
					pstmt.setString(9, Utility.trim(examCalendarWrapperArray[i].subjectID));
					pstmt.setString(10, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					examCalendarWrapperArray[i].recordFound = true;

				}
			}
			dataArrayWrapper.examCalendarWrapper = new ExamCalendarWrapper[1];
			dataArrayWrapper.examCalendarWrapper[0] = examCalendarWrapperArray[0];
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully Exam Calendar Updated");
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
	// ------------------------ Start updateExamCalendar-------------------------

	// ------------------------ Start fetchExamCalendar-------------------------
	public AbstractWrapper fetchExamCalendar(UsersWrapper usersProfileWrapper, ExamCalendarWrapper examCalendarWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchExamCalendar Grade" + examCalendarWrapper.gradeID);

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;
		int n = 1;

		// String whereCondition=null;
		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(examCalendarWrapper.academicYearID) == true) {
				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				examCalendarWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------
			}

			// ----------
			sql = "SELECT AcademicYearID, GradeID, TermID, SubjectID, ExamDate, StatusID,TargetMarks, MakerID, MakerDateTime, ModifierID, "
					+ "ModifierDateTime, SchoolID FROM ExamCalendar WHERE AcademicYearID=? AND SchoolID=? ";

			if (Utility.isEmpty(examCalendarWrapper.gradeID) == false) {

				sql = sql + " AND GradeID=? ";
			}

			if (Utility.isEmpty(examCalendarWrapper.termID) == false) {

				sql = sql + " AND TermID=?";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, Utility.trim(examCalendarWrapper.academicYearID));
			pstmt.setString(++n, usersProfileWrapper.schoolID);

			if (Utility.isEmpty(examCalendarWrapper.gradeID) == false) {
				pstmt.setString(++n, Utility.trim(examCalendarWrapper.gradeID));
			}

			if (Utility.isEmpty(examCalendarWrapper.termID) == false) {
				pstmt.setString(++n, Utility.trim(examCalendarWrapper.termID));
			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				examCalendarWrapper = new ExamCalendarWrapper();

				examCalendarWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				examCalendarWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				examCalendarWrapper.termID = Utility.trim(resultSet.getString("TermID"));
				examCalendarWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));
				examCalendarWrapper.examDate = Utility.setDate(resultSet.getString("ExamDate"));
				examCalendarWrapper.examDateMMM = Utility.setDateMMM(resultSet.getString("ExamDate"));
				examCalendarWrapper.statusID = Utility.trim(resultSet.getString("StatusID"));
				examCalendarWrapper.targetMarks = Utility.trim(resultSet.getString("TargetMarks"));
				examCalendarWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				examCalendarWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				examCalendarWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				examCalendarWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));
				examCalendarWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				examCalendarWrapper.recordFound = true;

				examCalendarWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						examCalendarWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				examCalendarWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(examCalendarWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				examCalendarWrapper.termIDValue = popoverHelper.fetchPopoverDesc(examCalendarWrapper.termID, "MST_Term",
						usersProfileWrapper.schoolID);
				examCalendarWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(examCalendarWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);
				examCalendarWrapper.statusIDValue = popoverHelper.fetchPopoverDesc(examCalendarWrapper.statusID,
						"MST_ExamStatus", usersProfileWrapper.schoolID);
				System.out.println("ExamCalendar Details fetch successful");

				vector.addElement(examCalendarWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.examCalendarWrapper = new ExamCalendarWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.examCalendarWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
			} else

			{
				resultSet.close();
				pstmt.close();

				dataArrayWrapper.examCalendarWrapper = new ExamCalendarWrapper[1];
				dataArrayWrapper.examCalendarWrapper[0] = examCalendarWrapper;
				dataArrayWrapper.recordFound = true;

				// commented on 30th March, 2018
				/*
				 * System.out.println("records from gradesubjects"+examCalendarWrapper.gradeID);
				 * 
				 * sql="SELECT AcademicYearID, GradeID, SubjectID, SchoolID " +
				 * " FROM GradeSubjects  WHERE  AcademicYearID=? AND GradeID=? AND SchoolID=?";
				 * 
				 * 
				 * 
				 * pstmt = con.prepareStatement(sql);
				 * 
				 * pstmt.setString(1,Utility.trim(examCalendarWrapper.academicYearID));
				 * pstmt.setString(2,Utility.trim(examCalendarWrapper.gradeID));
				 * pstmt.setString(3,usersProfileWrapper.schoolID);
				 * 
				 * 
				 * resultSet = pstmt.executeQuery();
				 * 
				 * while (resultSet.next()) {
				 * 
				 * examCalendarWrapper=new ExamCalendarWrapper();
				 * 
				 * examCalendarWrapper.academicYearID=Utility.trim(resultSet.getString(
				 * "AcademicYearID"));
				 * examCalendarWrapper.gradeID=Utility.trim(resultSet.getString("GradeID"));
				 * 
				 * examCalendarWrapper.subjectID=Utility.trim(resultSet.getString("SubjectID"));
				 * examCalendarWrapper.schoolID=Utility.trim(resultSet.getString("SchoolID"));
				 * 
				 * 
				 * examCalendarWrapper.recordFound=true;
				 * 
				 * examCalendarWrapper.academicYearIDValue=popoverHelper.fetchPopoverDesc(
				 * examCalendarWrapper.academicYearID, "MST_AcademicYear",
				 * usersProfileWrapper.schoolID);
				 * examCalendarWrapper.gradeIDValue=popoverHelper.fetchPopoverDesc(
				 * examCalendarWrapper.gradeID, "MST_Grade", usersProfileWrapper.schoolID);
				 * 
				 * examCalendarWrapper.subjectIDValue=popoverHelper.fetchPopoverDesc(
				 * examCalendarWrapper.subjectID, "MST_Subject", usersProfileWrapper.schoolID);
				 * 
				 * System.out.println("ExamCalendar from grade subjects fetch successful");
				 * 
				 * vector.addElement(examCalendarWrapper);
				 * 
				 * } if (vector.size()>0) { dataArrayWrapper.examCalendarWrapper = new
				 * ExamCalendarWrapper[vector.size()];
				 * vector.copyInto(dataArrayWrapper.examCalendarWrapper);
				 * dataArrayWrapper.recordFound=true;
				 * 
				 * System.out.println("total trn. in fetch " + vector.size()); } else{
				 * 
				 * dataArrayWrapper.examCalendarWrapper = new ExamCalendarWrapper[1];
				 * dataArrayWrapper.examCalendarWrapper[0]= examCalendarWrapper;
				 * dataArrayWrapper.recordFound=true;
				 * 
				 * } resultSet.close(); pstmt.close();
				 */

			}

			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
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

		}

		finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return dataArrayWrapper;
	}
	// ------------------------ End fetchExamCalendar-------------------------

}
