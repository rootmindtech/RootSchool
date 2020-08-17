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
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.StudentAcademicsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentAcademicsHelper extends Helper {

	// ---------------------------Start
	// insertStudentAcademics----------------------------

	public AbstractWrapper insertStudentAcademics(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper studentAcademicsWrapper) throws Exception {

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

		PreparedStatement pstmt = null;
		String groupTerm = null;
		String grade = null;

		try {
			
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(studentAcademicsWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}


			
			// ---fetch Group Term

			sql = "SELECT GroupTerm FROM MST_Term WHERE Code=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, Utility.trim(studentAcademicsWrapper.termID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				groupTerm = Utility.trim(resultSet.getString("GroupTerm"));

			}
			resultSet.close();
			pstmt.close();

			// ---end---

			// --fetch Grade
			if (studentAcademicsWrapper.securedMarks != null) {
				grade = fetchGrade(studentAcademicsWrapper.securedMarks, usersProfileWrapper.schoolID);

				/*
				 * sql="SELECT Grade FROM MST_GradingScale WHERE MinMarks<="+Integer.parseInt(
				 * studentAcademicsWrapper.securedMarks) +" " +" AND MaxMarks>="+
				 * Integer.parseInt(studentAcademicsWrapper.securedMarks)+"";
				 * 
				 * pstmt = con.prepareStatement(sql);
				 * 
				 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
				 * 
				 * grade=Utility.trim(resultSet.getString("Grade"));
				 * //gradePoint=Utility.trim(resultSet.getString("GradePoint"));
				 * 
				 * 
				 * } resultSet.close(); pstmt.close();
				 */

			}

			// ----------

			sql = "INSERT INTO StudentAcademics( RefNo, StudentID, GradeID, SectionID, AcademicYearID, TermID, SubjectID, TargetMarks, "
					+ " SecuredMarks, Percentage,RankID,GroupTerm,Grade, MakerID,MakerDateTime,SchoolID) "
					+ " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(studentAcademicsWrapper.refNo));
			pstmt.setString(2, Utility.trim(studentAcademicsWrapper.studentID));
			pstmt.setString(3, Utility.trim(studentAcademicsWrapper.gradeID));
			pstmt.setString(4, Utility.trim(studentAcademicsWrapper.sectionID));
			pstmt.setString(5, Utility.trim(studentAcademicsWrapper.academicYearID));
			pstmt.setString(6, Utility.trim(studentAcademicsWrapper.termID));
			pstmt.setString(7, Utility.trim(studentAcademicsWrapper.subjectID));
			pstmt.setString(8, Utility.trim(studentAcademicsWrapper.targetMarks));
			pstmt.setString(9, Utility.trim(studentAcademicsWrapper.securedMarks));
			pstmt.setString(10, Utility.trim(studentAcademicsWrapper.percentage));
			pstmt.setString(11, Utility.trim(studentAcademicsWrapper.rankID));
			pstmt.setString(12, groupTerm);
			pstmt.setString(13, grade);
			pstmt.setString(14, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(15, Utility.getCurrentTime()); // makerdatetime
			pstmt.setString(16, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid);

			pstmt.executeUpdate();
			pstmt.close();

			studentAcademicsWrapper.recordFound = true;

			dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
			dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentAcademics");

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
	// insertStudentAcademics----------------------------

	// ---------------------------Start
	// updateStudentAcademics----------------------------
	public AbstractWrapper updateStudentAcademics(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper studentAcademicsWrapper) throws Exception {

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
		String sql = null;
		String groupTerm = null;
		String grade = null;

		try {


			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(studentAcademicsWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			
			pstmt = con.prepareStatement(
					"SELECT RefNo FROM StudentAcademics WHERE RefNo=? AND StudentID=? AND AcademicYearID=? "
							+ " AND GradeID=? AND SectionID=?  AND TermID=? AND SubjectID=? and SchoolID=?");

			System.out.println("Student Academics  RefNo is" + studentAcademicsWrapper.refNo);

			pstmt.setString(1, Utility.trim(studentAcademicsWrapper.refNo));
			pstmt.setString(2, Utility.trim(studentAcademicsWrapper.studentID));
			pstmt.setString(3, Utility.trim(studentAcademicsWrapper.academicYearID));
			pstmt.setString(4, Utility.trim(studentAcademicsWrapper.gradeID));
			pstmt.setString(5, Utility.trim(studentAcademicsWrapper.sectionID));
			pstmt.setString(6, Utility.trim(studentAcademicsWrapper.termID));
			pstmt.setString(7, Utility.trim(studentAcademicsWrapper.subjectID));
			pstmt.setString(8, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertStudentAcademics(usersProfileWrapper,
						studentAcademicsWrapper);
			} else {

				resultSet.close();
				pstmt.close();

				// ---fetch Group Term

				sql = "SELECT GroupTerm FROM MST_Term WHERE Code=? and SchoolID=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, Utility.trim(studentAcademicsWrapper.termID));
				pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					groupTerm = Utility.trim(resultSet.getString("GroupTerm"));

				}
				resultSet.close();
				pstmt.close();

				// ---end---

				// --fetch Grade
				if (studentAcademicsWrapper.securedMarks != null) {

					grade = fetchGrade(studentAcademicsWrapper.securedMarks, usersProfileWrapper.schoolID);

					/*
					 * sql="SELECT Grade FROM MST_GradingScale WHERE MinMarks<="+Integer.parseInt(
					 * studentAcademicsWrapper.securedMarks) +" " +" AND MaxMarks>="+
					 * Integer.parseInt(studentAcademicsWrapper.securedMarks)+"";
					 * 
					 * pstmt = con.prepareStatement(sql);
					 * 
					 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
					 * 
					 * grade=Utility.trim(resultSet.getString("Grade"));
					 * //gradePoint=Utility.trim(resultSet.getString("GradePoint"));
					 * 
					 * 
					 * } resultSet.close(); pstmt.close();
					 */

				}

				// ----------

				pstmt = con.prepareStatement(
						"UPDATE StudentAcademics SET TargetMarks=?,SecuredMarks=?,Percentage=?,RankID=?, "
								+ " GroupTerm=?, Grade=?, ModifierID=?,ModifierDateTime=? "
								+ " WHERE RefNo=? AND StudentID=? AND AcademicYearID=? "
								+ " AND GradeID=? AND SectionID=?  AND TermID=? AND SubjectID=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(studentAcademicsWrapper.targetMarks));
				pstmt.setString(2, Utility.trim(studentAcademicsWrapper.securedMarks));
				pstmt.setString(3, Utility.trim(studentAcademicsWrapper.percentage));
				pstmt.setString(4, Utility.trim(studentAcademicsWrapper.rankID));
				pstmt.setString(5, groupTerm);
				pstmt.setString(6, grade);
				pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(8, Utility.getCurrentTime()); // modifierdatetime

				pstmt.setString(9, Utility.trim(studentAcademicsWrapper.refNo));
				pstmt.setString(10, Utility.trim(studentAcademicsWrapper.studentID));
				pstmt.setString(11, Utility.trim(studentAcademicsWrapper.academicYearID));
				pstmt.setString(12, Utility.trim(studentAcademicsWrapper.gradeID));
				pstmt.setString(13, Utility.trim(studentAcademicsWrapper.sectionID));
				pstmt.setString(14, Utility.trim(studentAcademicsWrapper.termID));
				pstmt.setString(15, Utility.trim(studentAcademicsWrapper.subjectID));
				pstmt.setString(16, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				studentAcademicsWrapper.recordFound = true;

				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
				dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully StudentAcademics Updated");
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
	// ---------------------------End
	// updateStudentAcademics----------------------------

	// ---------------------------Start
	// fetchStudentAcademics----------------------------

	public AbstractWrapper fetchStudentAcademics(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper studentAcademicsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchStudentAcademics RefNo " + studentAcademicsWrapper.refNo);

		System.out.println("fetchStudentAcademics studentID" + studentAcademicsWrapper.studentID);

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		// String sql=null;
		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(studentAcademicsWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------
			pstmt = con.prepareStatement("SELECT RefNo,StudentID,GradeID,SectionID,AcademicYearID,TermID,SubjectID, "
					+ "TargetMarks,SecuredMarks,Percentage,RankID,GroupTerm,Grade,MakerID, "
					+ "MakerDateTime,ModifierID,ModifierDateTime, SchoolID FROM StudentAcademics "
					+ "WHERE AcademicYearID=? AND RefNo=? AND StudentID=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(studentAcademicsWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(studentAcademicsWrapper.refNo));
			pstmt.setString(3, Utility.trim(studentAcademicsWrapper.studentID));
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

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
				studentAcademicsWrapper.grade = Utility.trim(resultSet.getString("Grade"));
				studentAcademicsWrapper.groupTerm = Utility.trim(resultSet.getString("GroupTerm"));
				studentAcademicsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentAcademicsWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));

				studentAcademicsWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				studentAcademicsWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				studentAcademicsWrapper.recordFound = true;
				studentAcademicsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAcademicsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.termID,
						"MST_Term", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.subjectID, "MST_Subject", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.rankIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.rankID,
						"MST_Rank", usersProfileWrapper.schoolID);
				System.out.println("StudentAcademics Details fetch successful");

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

	// ---------------------------Start
	// fetchStudentMarks----------------------------

	public AbstractWrapper fetchStudentMarks(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper studentAcademicsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetch Student Marks");

		Vector<Object> vector = new Vector<Object>();
		String sql = null;
		PreparedStatement pstmt = null;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			if (Utility.isEmpty(studentAcademicsWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			pstmt = con.prepareStatement(
					"SELECT a.Refno, a.Studentid, a.Gradeid, a.Sectionid, a.Academicyearid,a.TermID,a.Subjectid, "
							+ "a.TargetMarks,a.SecuredMarks,a.Percentage,a.RankID,a.MakerID,a.MakerDateTime,a.ModifierID,a.ModifierDateTime,b.Studentname, "
							+ "b.Surname,a.Grade,a.GroupTerm, a.SchoolID as SchoolID from StudentAcademics a JOIN StudentProfile b ON a.RefNo=b.RefNo where a.AcademicYearID=? and a.GradeID=? "
							+ "and a.SectionID=? and a.TermID=? and a.Subjectid=? and a.SchoolID=?");

			System.out.println("current academic yearID " + studentAcademicsWrapper.academicYearID);
			pstmt.setString(1, Utility.trim(studentAcademicsWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(studentAcademicsWrapper.gradeID));
			pstmt.setString(3, Utility.trim(studentAcademicsWrapper.sectionID));
			pstmt.setString(4, Utility.trim(studentAcademicsWrapper.termID));
			pstmt.setString(5, Utility.trim(studentAcademicsWrapper.subjectID));
			pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));
			
			System.out.println("studentAcademicsWrapper.gradeID " + studentAcademicsWrapper.gradeID);
			System.out.println("studentAcademicsWrapper.sectionID " + studentAcademicsWrapper.sectionID);
			System.out.println("studentAcademicsWrapper.termID " + studentAcademicsWrapper.termID);
			System.out.println("studentAcademicsWrapper.subjectID " + studentAcademicsWrapper.subjectID);
			System.out.println("usersProfileWrapper.schoolID " + usersProfileWrapper.schoolID);


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
				studentAcademicsWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentAcademicsWrapper.surname = Utility.trim(resultSet.getString("Surname"));

				studentAcademicsWrapper.grade = Utility.trim(resultSet.getString("Grade"));
				studentAcademicsWrapper.groupTerm = Utility.trim(resultSet.getString("GroupTerm"));

				studentAcademicsWrapper.recordFound = true;

				studentAcademicsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentAcademicsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.termID,
						"MST_Term", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.subjectID, "MST_Subject", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentAcademicsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				studentAcademicsWrapper.rankIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.rankID,
						"MST_Rank", usersProfileWrapper.schoolID);
				System.out.println("Student Marks Details fetch successful");

				vector.addElement(studentAcademicsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentAcademicsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				resultSet.close();
				pstmt.close();
				
				
				System.out.println("studentAcademicsWrapper.gradeID1 " + studentAcademicsWrapper.gradeID);
				System.out.println("studentAcademicsWrapper.sectionID1 " + studentAcademicsWrapper.sectionID);
				System.out.println("studentAcademicsWrapper.termID1 " + studentAcademicsWrapper.termID);
				System.out.println("studentAcademicsWrapper.subjectID1 " + studentAcademicsWrapper.subjectID);
				System.out.println("usersProfileWrapper.schoolID1 " + usersProfileWrapper.schoolID);


				String academicYearID = studentAcademicsWrapper.academicYearID;
				String gradeID = studentAcademicsWrapper.gradeID;
				String sectionID = studentAcademicsWrapper.sectionID;
				String termID = studentAcademicsWrapper.termID;
				String subjectID = studentAcademicsWrapper.subjectID;
				String schoolID = usersProfileWrapper.schoolID;

				String targetMarks = "0";

				// -----get Target marks from exam calendar---

				sql = "SELECT TargetMarks FROM ExamCalendar "
						+ " WHERE AcademicYearID=? AND GradeID=? AND TermID=? AND SubjectID=? and SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(academicYearID));
				pstmt.setString(2, Utility.trim(gradeID));
				pstmt.setString(3, Utility.trim(termID));
				pstmt.setString(4, Utility.trim(subjectID));
				pstmt.setString(5, Utility.trim(schoolID));

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					targetMarks = Utility.trim(resultSet.getString("TargetMarks"));

				}

				if (targetMarks == null) {
					targetMarks = "0";
				}

				resultSet.close();
				pstmt.close();

				// ------------

				sql = "SELECT RefNo, StudentID, GradeID, SectionID, AcademicyearID, StudentName, SchoolID from StudentProfile "
						+ "where AcademicYearID=? and GradeID=?  and SectionID=? and SchoolID=?";

				System.out.println("sql " + sql);

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(academicYearID));
				pstmt.setString(2, Utility.trim(gradeID));
				pstmt.setString(3, Utility.trim(sectionID));
				pstmt.setString(4, Utility.trim(schoolID));

				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {
					studentAcademicsWrapper = new StudentAcademicsWrapper();

					studentAcademicsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
					studentAcademicsWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
					studentAcademicsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
					studentAcademicsWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
					studentAcademicsWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));

					studentAcademicsWrapper.termID = termID;
					studentAcademicsWrapper.subjectID = subjectID;
					studentAcademicsWrapper.targetMarks = targetMarks;
					studentAcademicsWrapper.securedMarks = "0";
					studentAcademicsWrapper.percentage = "0";

					studentAcademicsWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));

					studentAcademicsWrapper.recordFound = true;

					studentAcademicsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

					studentAcademicsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(studentAcademicsWrapper.termID,
							"MST_Term", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.subjectID, "MST_Subject", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.gradeID, "MST_Grade", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
					System.out.println("Student Marks Details fetch successful");

					vector.addElement(studentAcademicsWrapper);

				}

				if (vector.size() > 0) {
					dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[vector.size()];
					vector.copyInto(dataArrayWrapper.studentAcademicsWrapper);
					dataArrayWrapper.recordFound = true;

					System.out.println("total trn. in fetch " + vector.size());

				} else {
					dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
					dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
					dataArrayWrapper.recordFound = true;

				}

				resultSet.close();
				pstmt.close();

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

	// ---------------------------End fetch Student
	// Marks----------------------------

	// ---------------------------Start
	// updateStudentMarks----------------------------
	public AbstractWrapper updateStudentMarks(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper[] studentAcademicsWrapperArray) throws Exception {

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

		PreparedStatement pstmt = null;
		String groupTerm = null;
		String grade = null;
		// String gradePoint=null;

		try {
			con = getConnection();

			for (int i = 0; i <= studentAcademicsWrapperArray.length - 1; i++) {

				pstmt = con.prepareStatement("SELECT RefNo,StudentID FROM StudentAcademics "
						+ "WHERE AcademicYearID=? AND RefNo=? AND StudentID=? AND GradeID=? "
						+ "AND SectionID=? AND TermID=? AND SubjectID=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(studentAcademicsWrapperArray[i].academicYearID));
				pstmt.setString(2, Utility.trim(studentAcademicsWrapperArray[i].refNo));
				pstmt.setString(3, Utility.trim(studentAcademicsWrapperArray[i].studentID));
				pstmt.setString(4, Utility.trim(studentAcademicsWrapperArray[i].gradeID));
				pstmt.setString(5, Utility.trim(studentAcademicsWrapperArray[i].sectionID));
				pstmt.setString(6, Utility.trim(studentAcademicsWrapperArray[i].termID));
				pstmt.setString(7, Utility.trim(studentAcademicsWrapperArray[i].subjectID));
				pstmt.setString(8, Utility.trim(studentAcademicsWrapperArray[i].schoolID));

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {
					resultSet.close();
					pstmt.close();

					groupTerm = null;
					grade = null;
					// gradePoint=null;

					// ---fetch Group Term

					sql = "SELECT GroupTerm FROM MST_Term WHERE Code=? and SchoolID=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, Utility.trim(studentAcademicsWrapperArray[i].termID));
					pstmt.setString(2, Utility.trim(studentAcademicsWrapperArray[i].schoolID));

					resultSet = pstmt.executeQuery();
					if (resultSet.next()) {

						groupTerm = Utility.trim(resultSet.getString("GroupTerm"));

					}
					resultSet.close();
					pstmt.close();

					// ---end---

					// --fetch Grade
					if (studentAcademicsWrapperArray[i].securedMarks != null) {

						grade = fetchGrade(studentAcademicsWrapperArray[i].securedMarks, usersProfileWrapper.schoolID);

//						sql = "SELECT Grade FROM MST_GradingScale WHERE MinMarks<="
//								+ Integer.parseInt(studentAcademicsWrapperArray[i].securedMarks) + " "
//								+ " AND MaxMarks>=" + Integer.parseInt(studentAcademicsWrapperArray[i].securedMarks)
//								+ " AND SchoolID=?";
//
//						pstmt = con.prepareStatement(sql);
//						pstmt.setString(1, studentAcademicsWrapperArray[i].schoolID);
//
//						resultSet = pstmt.executeQuery();
//						if (resultSet.next()) {
//
//							grade = Utility.trim(resultSet.getString("Grade"));
//							// gradePoint=Utility.trim(resultSet.getString("GradePoint"));
//
//						}
//						resultSet.close();
//						pstmt.close();

					}

					// ----------

					pstmt = con.prepareStatement(
							"UPDATE StudentAcademics SET TargetMarks=?,SecuredMarks=?,Percentage=?,RankID=?, "
									+ "GroupTerm=?,Grade=?,ModifierID=?,ModifierDateTime=? "
									+ " WHERE AcademicYearID=? AND RefNo=? AND StudentID=? AND GradeID=? "
									+ "AND SectionID=? AND TermID=? AND SubjectID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(studentAcademicsWrapperArray[i].targetMarks));
					pstmt.setString(2, Utility.trim(studentAcademicsWrapperArray[i].securedMarks));
					pstmt.setString(3, Utility.trim(studentAcademicsWrapperArray[i].percentage));
					pstmt.setString(4, Utility.trim(studentAcademicsWrapperArray[i].rankID));
					pstmt.setString(5, groupTerm);
					pstmt.setString(6, grade);
					pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(8, Utility.getCurrentTime());
					pstmt.setString(9, Utility.trim(studentAcademicsWrapperArray[i].academicYearID));
					pstmt.setString(10, Utility.trim(studentAcademicsWrapperArray[i].refNo));
					pstmt.setString(11, Utility.trim(studentAcademicsWrapperArray[i].studentID));
					pstmt.setString(12, Utility.trim(studentAcademicsWrapperArray[i].gradeID));
					pstmt.setString(13, Utility.trim(studentAcademicsWrapperArray[i].sectionID));
					pstmt.setString(14, Utility.trim(studentAcademicsWrapperArray[i].termID));
					pstmt.setString(15, Utility.trim(studentAcademicsWrapperArray[i].subjectID));
					pstmt.setString(16, Utility.trim(studentAcademicsWrapperArray[i].schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					studentAcademicsWrapperArray[i].recordFound = true;

				} else {
					resultSet.close();
					pstmt.close();
					dataArrayWrapper = (DataArrayWrapper) insertStudentAcademics(usersProfileWrapper,
							studentAcademicsWrapperArray[i]);

				}

			}

			dataArrayWrapper.studentAcademicsWrapper = studentAcademicsWrapperArray;
			dataArrayWrapper.recordFound = true;

			System.out.println(" Student Marks Updated Successfully");

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
	// ---------------------------End updateStudentMarks----------------------------

	// ---------------------------Start
	// fetchStudentAcademicsByGrade----------------------------

	public AbstractWrapper fetchStudentAcademicScholastic(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper studentAcademicsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		ResultSet resultSetMain = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		PreparedStatement pstmtMain = null;
		String sql = null;

		try {

			con = getConnection();
			PopoverHelper popoverHelper = new PopoverHelper();

			if (Utility.isEmpty(studentAcademicsWrapper.academicYearID) == true) {
				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ---fetch Term-1- FA1---

			sql = "SELECT SubjectID FROM GradeSubjects Where GradeID=? and SchoolID=?";

			pstmtMain = con.prepareStatement(sql);

			pstmtMain.setString(1, Utility.trim(studentAcademicsWrapper.gradeID));
			pstmtMain.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("GradeID is " + Utility.trim(studentAcademicsWrapper.gradeID));

			resultSetMain = pstmtMain.executeQuery();

			String academicYearID = Utility.trim(studentAcademicsWrapper.academicYearID);
			// String refNo=Utility.trim(studentAcademicsWrapper.refNo);;
			String studentID = Utility.trim(studentAcademicsWrapper.studentID);
			;
			String schoolID = Utility.trim(usersProfileWrapper.schoolID);
			;
			String subjectID = null;

			while (resultSetMain.next()) {

				subjectID = Utility.trim(resultSetMain.getString("SubjectID"));

				System.out.println("Subject ID is " + subjectID);

				sql = "SELECT a.RefNo,a.StudentID,a.GradeID,a.SectionID,a.AcademicYearID,a.TermID,a.SubjectID,a.SecuredMarks,"
						+ " a.GroupTerm,a.Grade,b.Description,b.PercentDist,b.TermType,b.TotalDist, a.SchoolID FROM StudentAcademics a "
						+ " LEFT JOIN MST_Term b ON a.TermID=b.Code WHERE a.AcademicYearID=? AND SubjectID=? and a.SchoolID=?";

				if (!Utility.isEmpty(studentID)) {
					sql = sql + "  AND StudentID=?";
				}

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, academicYearID);
				// pstmt.setString(2,refNo);

				pstmt.setString(2, subjectID);

				pstmt.setString(3, schoolID);

				if (Utility.isEmpty(studentID) == false) {
					pstmt.setString(4, studentID);

				}

				resultSet = pstmt.executeQuery();

				if (!resultSet.next()) {
					resultSet.close();
					pstmt.close();
					System.out.println("No records found for Student Scholastic");
				} else {
					studentAcademicsWrapper = new StudentAcademicsWrapper();

					do {
						//System.out.println("fetchStudentAcademicScholastic sub while ");

						studentAcademicsWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));

						studentAcademicsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));

						//System.out.println("fetchStdAcdScholastic subwhile RefNo " + studentAcademicsWrapper.refNo);

						studentAcademicsWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));

						studentAcademicsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));

						studentAcademicsWrapper.termID = Utility.trim(resultSet.getString("TermID"));

						studentAcademicsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));

						studentAcademicsWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));

						studentAcademicsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

						if (studentAcademicsWrapper.termID.equals("FA1001")) {
							studentAcademicsWrapper.fa1SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.fa1Grade = fetchGrade(studentAcademicsWrapper.fa1SecuredMarks,
									usersProfileWrapper.schoolID);
							studentAcademicsWrapper.fa1PercentDist = fetchPercentDist("FA1001",
									studentAcademicsWrapper.fa1SecuredMarks, usersProfileWrapper.schoolID);
						}

						if (studentAcademicsWrapper.termID.equals("FA2002")) {
							studentAcademicsWrapper.fa2SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.fa2Grade = fetchGrade(studentAcademicsWrapper.fa2SecuredMarks,
									usersProfileWrapper.schoolID);
							studentAcademicsWrapper.fa2PercentDist = fetchPercentDist("FA2002",
									studentAcademicsWrapper.fa2SecuredMarks, usersProfileWrapper.schoolID);
						}
						if (studentAcademicsWrapper.termID.equals("SA1003")) {
							studentAcademicsWrapper.sa1SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.sa1Grade = fetchGrade(studentAcademicsWrapper.sa1SecuredMarks,
									usersProfileWrapper.schoolID);
							studentAcademicsWrapper.sa1PercentDist = fetchPercentDist("SA1003",
									studentAcademicsWrapper.sa1SecuredMarks, usersProfileWrapper.schoolID);
						}
						if (studentAcademicsWrapper.termID.equals("FA3005")) {
							studentAcademicsWrapper.fa3SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.fa3Grade = fetchGrade(studentAcademicsWrapper.fa3SecuredMarks,
									usersProfileWrapper.schoolID);
							studentAcademicsWrapper.fa3PercentDist = fetchPercentDist("FA3005",
									studentAcademicsWrapper.fa3SecuredMarks, usersProfileWrapper.schoolID);
						}
						if (studentAcademicsWrapper.termID.equals("FA4006")) {
							studentAcademicsWrapper.fa4SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.fa4Grade = fetchGrade(studentAcademicsWrapper.fa4SecuredMarks,
									studentAcademicsWrapper.schoolID);
							studentAcademicsWrapper.fa4PercentDist = fetchPercentDist("FA4006",
									studentAcademicsWrapper.fa4SecuredMarks, studentAcademicsWrapper.schoolID);
						}
						if (studentAcademicsWrapper.termID.equals("SA2004")) {
							studentAcademicsWrapper.sa2SecuredMarks = Utility.trim(resultSet.getString("SecuredMarks"));
							studentAcademicsWrapper.sa2Grade = fetchGrade(studentAcademicsWrapper.sa2SecuredMarks,
									usersProfileWrapper.schoolID);
							studentAcademicsWrapper.sa2PercentDist = fetchPercentDist("SA2004",
									studentAcademicsWrapper.sa2SecuredMarks, usersProfileWrapper.schoolID);

						}

					} while (resultSet.next());
					// sub while close

					resultSet.close();
					pstmt.close();

					studentAcademicsWrapper.term1Total = Math.round(studentAcademicsWrapper.fa1PercentDist
							+ studentAcademicsWrapper.fa2PercentDist + studentAcademicsWrapper.sa1PercentDist);

					studentAcademicsWrapper.term2Total = Math.round(studentAcademicsWrapper.fa3PercentDist
							+ studentAcademicsWrapper.fa4PercentDist + studentAcademicsWrapper.sa2PercentDist);

					studentAcademicsWrapper.finalFATotal = Math
							.round(studentAcademicsWrapper.fa1PercentDist + studentAcademicsWrapper.fa2PercentDist
									+ studentAcademicsWrapper.fa3PercentDist + studentAcademicsWrapper.fa4PercentDist);

					studentAcademicsWrapper.finalSATotal = Math
							.round(studentAcademicsWrapper.sa1PercentDist + studentAcademicsWrapper.sa2PercentDist);

					studentAcademicsWrapper.finalOverallTotal = Math
							.round(studentAcademicsWrapper.finalFATotal + studentAcademicsWrapper.finalSATotal);

					Double finalOverallTotalValue = new Double(studentAcademicsWrapper.finalOverallTotal);
					// int i = d.intValue();

					// String value=Integer.toString((d.intValue()));

					studentAcademicsWrapper.gradePoint = fetchGrade(
							Integer.toString((finalOverallTotalValue.intValue())), studentID);

					studentAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.subjectID, "MST_Subject", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.gradeID, "MST_Grade", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.sectionID, "MST_Section", usersProfileWrapper.schoolID);
					studentAcademicsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(
							studentAcademicsWrapper.termID, "MST_Term", usersProfileWrapper.schoolID);

					studentAcademicsWrapper.recordFound = true;
					vector.addElement(studentAcademicsWrapper);
				}

			} // main while close
			if (vector.size() > 0) {
				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentAcademicsWrapper);
				dataArrayWrapper.recordFound = true;
				System.out.println("total trn. in fetch " + vector.size());

				// --------insert scholastic
				if (resultSetMain != null)
					resultSetMain.close();
				if (pstmtMain != null)
					pstmtMain.close();

				insertStudentScholastic(usersProfileWrapper, dataArrayWrapper.studentAcademicsWrapper);

			} else

			{
				dataArrayWrapper.studentAcademicsWrapper = new StudentAcademicsWrapper[1];
				dataArrayWrapper.studentAcademicsWrapper[0] = studentAcademicsWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("fetchStudentAcademicScholastic No Records found ");

			}

			if (resultSetMain != null)
				resultSetMain.close();
			if (pstmtMain != null)
				pstmtMain.close();

			System.out.println("fetchStudentAcademicScholastic successfully done ");

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

	// ---------------------------Start fetchGrade----------------------------

	public String fetchGrade(String securedMarks, String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

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

	// ---------------------------Start Percentage
	// Distribution----------------------------

	public double fetchPercentDist(String code, String securedMarks, String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		double percentDist = 0;

		try {
			con = getConnection();

			// --fetch Grade
			if (securedMarks != null) {

				sql = "SELECT PercentDist FROM MST_Term WHERE Code=? and SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, code);
				pstmt.setString(2, schoolID);

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					//System.out.println(" fetchPercentDist PercentDist " + resultSet.getInt("PercentDist"));

					percentDist = (double) (Integer.parseInt(securedMarks)
							* ((double) resultSet.getInt("PercentDist") / (double) 100));

					//System.out.println(" percentDist " + percentDist);

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

		return percentDist;
	}
	// ---------------------------End fetchGrade----------------------------

	// ---------------------------Start
	// insertStudentScholastic----------------------------

	public void insertStudentScholastic(UsersWrapper usersProfileWrapper,
			StudentAcademicsWrapper[] studentAcademicsWrapperArray) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
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

			for (int i = 0; i <= studentAcademicsWrapperArray.length - 1; i++) {
				sql = "DELETE FROM StudentScholastic WHERE AcademicYearID=? AND RefNo=? AND StudentID=? and SchoolID=?";
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(studentAcademicsWrapperArray[i].academicYearID));
				pstmt.setString(2, Utility.trim(studentAcademicsWrapperArray[i].refNo));
				pstmt.setString(3, Utility.trim(studentAcademicsWrapperArray[i].studentID));
				pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				;
				pstmt.close();

			}

			for (int i = 0; i <= studentAcademicsWrapperArray.length - 1; i++) {
				sql = "INSERT INTO StudentScholastic( RefNo, StudentID, GradeID, SectionID, AcademicYearID, SubjectID, FA1SecuredMarks, "
						+ "FA1Grade,FA1PercentDist,FA2SecuredMarks, FA2Grade,FA2PercentDist,SA1SecuredMarks,SA1Grade, "
						+ "SA1PercentDist, Term1Total,FA3SecuredMarks,FA3Grade,FA3PercentDist,FA4SecuredMarks,FA4Grade, "
						+ "FA4PercentDist,SA2SecuredMarks,SA2Grade,SA2PercentDist,Term2Total,FinalFATotal,FinalSATotal, "
						+ "FinalOverallTotal,GradePoint, MakerID,MakerDateTime, SchoolID) "
						+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				//System.out.println("sql " + sql);

				pstmt = con.prepareStatement(sql);

				//System.out.println("insertStudentScholastic Refno " + Utility.trim(studentAcademicsWrapperArray[i].refNo));

				pstmt.setString(1, Utility.trim(studentAcademicsWrapperArray[i].refNo));
				pstmt.setString(2, Utility.trim(studentAcademicsWrapperArray[i].studentID));
				pstmt.setString(3, Utility.trim(studentAcademicsWrapperArray[i].gradeID));
				pstmt.setString(4, Utility.trim(studentAcademicsWrapperArray[i].sectionID));
				pstmt.setString(5, Utility.trim(studentAcademicsWrapperArray[i].academicYearID));

				pstmt.setString(6, Utility.trim(studentAcademicsWrapperArray[i].subjectID));
				pstmt.setString(7, Utility.trim(studentAcademicsWrapperArray[i].fa1SecuredMarks));
				pstmt.setString(8, Utility.trim(studentAcademicsWrapperArray[i].fa1Grade));
				pstmt.setDouble(9, studentAcademicsWrapperArray[i].fa1PercentDist);

				pstmt.setString(10, Utility.trim(studentAcademicsWrapperArray[i].fa2SecuredMarks));
				pstmt.setString(11, Utility.trim(studentAcademicsWrapperArray[i].fa2Grade));
				pstmt.setDouble(12, studentAcademicsWrapperArray[i].fa2PercentDist);

				pstmt.setString(13, Utility.trim(studentAcademicsWrapperArray[i].sa1SecuredMarks));
				pstmt.setString(14, Utility.trim(studentAcademicsWrapperArray[i].sa1Grade));
				pstmt.setDouble(15, studentAcademicsWrapperArray[i].sa1PercentDist);

				pstmt.setDouble(16, studentAcademicsWrapperArray[i].term1Total);
				pstmt.setString(17, Utility.trim(studentAcademicsWrapperArray[i].fa3SecuredMarks));
				pstmt.setString(18, Utility.trim(studentAcademicsWrapperArray[i].fa3Grade));
				pstmt.setDouble(19, studentAcademicsWrapperArray[i].fa3PercentDist);

				pstmt.setString(20, Utility.trim(studentAcademicsWrapperArray[i].fa4SecuredMarks));
				pstmt.setString(21, Utility.trim(studentAcademicsWrapperArray[i].fa4Grade));
				pstmt.setDouble(22, studentAcademicsWrapperArray[i].fa4PercentDist);

				pstmt.setString(23, Utility.trim(studentAcademicsWrapperArray[i].sa2SecuredMarks));
				pstmt.setString(24, Utility.trim(studentAcademicsWrapperArray[i].sa2Grade));
				pstmt.setDouble(25, studentAcademicsWrapperArray[i].sa2PercentDist);

				pstmt.setDouble(26, studentAcademicsWrapperArray[i].term2Total);
				pstmt.setDouble(27, studentAcademicsWrapperArray[i].finalFATotal);
				pstmt.setDouble(28, studentAcademicsWrapperArray[i].finalSATotal);
				pstmt.setDouble(29, studentAcademicsWrapperArray[i].finalOverallTotal);
				pstmt.setString(30, Utility.trim(studentAcademicsWrapperArray[i].gradePoint));
				pstmt.setString(31, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(32, Utility.getCurrentTime()); // makerdatetime
				pstmt.setString(33, Utility.trim(usersProfileWrapper.schoolID));

				studentAcademicsWrapperArray[i].recordFound = true;

				pstmt.executeUpdate();
				pstmt.close();

				//System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid);

			}

			System.out.println("Successfully inserted into StudentScholastic");

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

	}
	// ---------------------------End
	// insertStudentScholastic----------------------------

}
