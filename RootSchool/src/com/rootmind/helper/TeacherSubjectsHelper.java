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
import com.rootmind.wrapper.TeacherSubjectsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TeacherSubjectsHelper extends Helper {

	// ------------------------ Start fetchGradeSubjects-------------------------
	public AbstractWrapper fetchTeacherSubjects(UsersWrapper usersProfileWrapper,
			TeacherSubjectsWrapper teacherSubjectsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchTeaherSubjects academicYearID is " + teacherSubjectsWrapper.academicYearID);

		System.out.println("fetchTeaherSubjects staffUserID is " + teacherSubjectsWrapper.staffUserID);
		System.out.println("fetchTeaherSubjects Grade is " + teacherSubjectsWrapper.gradeID);
		System.out.println("fetchTeaherSubjects subjectID is " + teacherSubjectsWrapper.subjectID);

		System.out.println("fetchTeaherSubjects schoolID is " + usersProfileWrapper.schoolID);

		Vector<Object> vector = new Vector<Object>();

		String sql = null;
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;
		int n = 1;
		try {
			con = getConnection();

			PopoverHelper popoverHelper = new PopoverHelper();

			if (Utility.isEmpty(teacherSubjectsWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				teacherSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

				// ----------
			}

			sql = "SELECT  AcademicYearID, UserID,GradeID, SubjectID, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID "
					+ " FROM TeacherSubjects  WHERE AcademicYearID=? and SchoolID=?";

			if (Utility.isEmpty(teacherSubjectsWrapper.staffUserID) == false) {
				sql = sql + " and Userid=? ";
			}

			if (Utility.isEmpty(teacherSubjectsWrapper.gradeID) == false) {
				sql = sql + " AND GradeID=?";
			}

			if (Utility.isEmpty(teacherSubjectsWrapper.subjectID) == false) {
				sql = sql + " AND SubjectID=?";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, teacherSubjectsWrapper.academicYearID);
			pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

			if (Utility.isEmpty(teacherSubjectsWrapper.staffUserID) == false) {
				pstmt.setString(++n, Utility.trim(teacherSubjectsWrapper.staffUserID));

			}

			if (Utility.isEmpty(teacherSubjectsWrapper.gradeID) == false) {
				pstmt.setString(++n, Utility.trim(teacherSubjectsWrapper.gradeID));

			}

			if (Utility.isEmpty(teacherSubjectsWrapper.subjectID) == false) {
				pstmt.setString(++n, Utility.trim(teacherSubjectsWrapper.subjectID));

			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				teacherSubjectsWrapper = new TeacherSubjectsWrapper();

				teacherSubjectsWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));

				teacherSubjectsWrapper.staffUserID = Utility.trim(resultSet.getString("UserID"));

				teacherSubjectsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));

				teacherSubjectsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));

				teacherSubjectsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				teacherSubjectsWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				teacherSubjectsWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				teacherSubjectsWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));
				teacherSubjectsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				teacherSubjectsWrapper.recordFound = true;

				teacherSubjectsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						teacherSubjectsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				teacherSubjectsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(teacherSubjectsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);

				teacherSubjectsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(teacherSubjectsWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);
				System.out.println("fetchGradeSubjects  successful");

				vector.addElement(teacherSubjectsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.teacherSubjectsWrapper = new TeacherSubjectsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.teacherSubjectsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
			} else

			{
				dataArrayWrapper.teacherSubjectsWrapper = new TeacherSubjectsWrapper[1];
				dataArrayWrapper.teacherSubjectsWrapper[0] = teacherSubjectsWrapper;
				dataArrayWrapper.recordFound = true;

			}

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

	// -----------------Start insertGradeSubjects---------------------

	public AbstractWrapper insertTeacherSubjects(UsersWrapper usersProfileWrapper,
			TeacherSubjectsWrapper teacherSubjectsWrapper) throws Exception {

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
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			if (Utility.isEmpty(teacherSubjectsWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				teacherSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

			}

			sql = " INSERT INTO TeacherSubjects( AcademicYearID,UserID, GradeID,SubjectID,MakerID, MakerDateTime, SchoolID)  "
					+ "Values (?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, teacherSubjectsWrapper.academicYearID);
			pstmt.setString(2, Utility.trim(teacherSubjectsWrapper.staffUserID));
			pstmt.setString(3, Utility.trim(teacherSubjectsWrapper.gradeID));
			pstmt.setString(4, Utility.trim(teacherSubjectsWrapper.subjectID));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(6, Utility.getCurrentTime()); // maker date time
			pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("teachersubjects Userid " + teacherSubjectsWrapper.staffUserID);

			pstmt.executeUpdate();
			pstmt.close();

			teacherSubjectsWrapper.recordFound = true;

			dataArrayWrapper.teacherSubjectsWrapper = new TeacherSubjectsWrapper[1];
			dataArrayWrapper.teacherSubjectsWrapper[0] = teacherSubjectsWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into GradeSubjects");

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

	// -----------------End insertGradeSubjects---------------------

	// -----------------Start updateGradeSubjects---------------------
	public AbstractWrapper updateTeacherSubjects(UsersWrapper usersProfileWrapper,
			TeacherSubjectsWrapper teacherSubjectsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;
		String sql = null;
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			if (Utility.isEmpty(teacherSubjectsWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				teacherSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

			}

			sql = "SELECT SubjectID FROM TeacherSubjects WHERE AcademicYearID=? AND UserID=? AND GradeID=? AND SubjectID=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			System.out.println(" currentAcademicYear   is " + teacherSubjectsWrapper.academicYearID);
			System.out.println(" staffUserID   is " + teacherSubjectsWrapper.staffUserID);
			System.out.println(" Student gradeID   is " + teacherSubjectsWrapper.gradeID);
			System.out.println(" Subject ID  is " + teacherSubjectsWrapper.subjectID);
			System.out.println(" schoolID ID  is " + usersProfileWrapper.schoolID);

			pstmt.setString(1, teacherSubjectsWrapper.academicYearID);
			pstmt.setString(2, Utility.trim(teacherSubjectsWrapper.staffUserID));
			pstmt.setString(3, Utility.trim(teacherSubjectsWrapper.gradeID));
			pstmt.setString(4, Utility.trim(teacherSubjectsWrapper.subjectID));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				if (!teacherSubjectsWrapper.deleteFlag.equals("Y")) {
					dataArrayWrapper = (DataArrayWrapper) insertTeacherSubjects(usersProfileWrapper,
							teacherSubjectsWrapper);
				} else {
					dataArrayWrapper.teacherSubjectsWrapper = new TeacherSubjectsWrapper[1];
					dataArrayWrapper.teacherSubjectsWrapper[0] = teacherSubjectsWrapper;
					dataArrayWrapper.recordFound = true;

				}
			} else {
				resultSet.close();
				pstmt.close();

				if (teacherSubjectsWrapper.deleteFlag.equals("Y")) {

					pstmt = con.prepareStatement("DELETE FROM TeacherSubjects WHERE AcademicYearID=? AND UserID=? AND "
							+ " GradeID=? AND SubjectID=? and SchoolID=?");

					pstmt.setString(1, teacherSubjectsWrapper.academicYearID);
					pstmt.setString(2, Utility.trim(teacherSubjectsWrapper.staffUserID));
					pstmt.setString(3, Utility.trim(teacherSubjectsWrapper.gradeID));
					pstmt.setString(4, Utility.trim(teacherSubjectsWrapper.subjectID));
					pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

				} else {

					if (resultSet != null)
						resultSet.close();
					if (pstmt != null)
						pstmt.close();

					pstmt = con.prepareStatement("UPDATE TeacherSubjects SET  "
							+ "ModifierID=?, ModifierDateTime=? WHERE AcademicYearID=? AND UserID=? AND GradeID=? AND SubjectID=? AND SchoolID=?");

					pstmt.setString(1, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(2, Utility.getCurrentTime()); // modifier date time

					pstmt.setString(3, Utility.trim(teacherSubjectsWrapper.academicYearID));
					pstmt.setString(4, Utility.trim(teacherSubjectsWrapper.staffUserID));
					pstmt.setString(5, Utility.trim(teacherSubjectsWrapper.gradeID));
					pstmt.setString(6, Utility.trim(teacherSubjectsWrapper.subjectID));
					pstmt.setString(7, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					teacherSubjectsWrapper.recordFound = true;
				}

				teacherSubjectsWrapper.recordFound = true;
				dataArrayWrapper.teacherSubjectsWrapper = new TeacherSubjectsWrapper[1];
				dataArrayWrapper.teacherSubjectsWrapper[0] = teacherSubjectsWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println(" Grade Subject already existed");
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
	// -----------------End updateGradeSubjects---------------------

	// ------------------------ Start fetchGradeSubjects-------------------------
	public boolean findTeacherSubjects(UsersWrapper usersProfileWrapper, TeacherSubjectsWrapper teacherSubjectsWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

		System.out.println("fetchTeaherSubjects Grade is " + teacherSubjectsWrapper.gradeID);

		// Vector<Object> vector = new Vector<Object>();

		String sql = null;
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;

		boolean recordFound = false;
		try {
			con = getConnection();

			//

			if (Utility.isEmpty(teacherSubjectsWrapper.academicYearID) == true) {

				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				teacherSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

				// ----------
			}

			sql = "SELECT  AcademicYearID, UserID,GradeID, SubjectID, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID "
					+ " FROM TeacherSubjects  WHERE AcademicYearID=? and UserID=? AND GradeID=? AND SubjectID=? and SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, teacherSubjectsWrapper.academicYearID);
			pstmt.setString(2, Utility.trim(teacherSubjectsWrapper.staffUserID));
			pstmt.setString(3, Utility.trim(teacherSubjectsWrapper.gradeID));
			pstmt.setString(4, Utility.trim(teacherSubjectsWrapper.subjectID));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				recordFound = true;

			}

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

		}

		finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return recordFound;
	}
	// ------------------------ End fetchExamCalendar-------------------------

}
