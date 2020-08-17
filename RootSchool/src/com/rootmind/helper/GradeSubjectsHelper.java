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
import com.rootmind.wrapper.GradeSubjectsWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class GradeSubjectsHelper extends Helper {

	// ------------------------ Start fetchGradeSubjects-------------------------
	public AbstractWrapper fetchGradeSubjects(UsersWrapper usersProfileWrapper,
			GradeSubjectsWrapper gradeSubjectsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchGradeSubjects academicYearID is " + gradeSubjectsWrapper.academicYearID);

		System.out.println("fetchGradeSubjects Grade is " + gradeSubjectsWrapper.gradeID);

		System.out.println("fetchGradeSubjects SubjectID is " + gradeSubjectsWrapper.subjectID);

		Vector<Object> vector = new Vector<Object>();

		String sql = null;
		PreparedStatement pstmt = null;
		int n = 1;
		try {
			con = getConnection();

			PopoverHelper popoverHelper = new PopoverHelper();

			if (Utility.isEmpty(gradeSubjectsWrapper.academicYearID) == true) {

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				gradeSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------
			}

			sql = "SELECT  AcademicYearID,GradeID, SubjectID, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID "
					+ " FROM GradeSubjects  WHERE AcademicYearID=? and SchoolID=?";

			if (!Utility.isEmpty(gradeSubjectsWrapper.gradeID)) {
				sql = sql + " AND GradeID=?";
			}

			if (!Utility.isEmpty(gradeSubjectsWrapper.subjectID)) {
				sql = sql + " AND SubjectID=?";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, gradeSubjectsWrapper.academicYearID);
			pstmt.setString(++n, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(gradeSubjectsWrapper.gradeID)) {
				pstmt.setString(++n, Utility.trim(gradeSubjectsWrapper.gradeID));

			}

			if (!Utility.isEmpty(gradeSubjectsWrapper.subjectID)) {
				pstmt.setString(++n, Utility.trim(gradeSubjectsWrapper.subjectID));

			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				gradeSubjectsWrapper = new GradeSubjectsWrapper();

				gradeSubjectsWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				gradeSubjectsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));

				gradeSubjectsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));

				gradeSubjectsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				gradeSubjectsWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				gradeSubjectsWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				gradeSubjectsWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));
				gradeSubjectsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				gradeSubjectsWrapper.recordFound = true;

				gradeSubjectsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						gradeSubjectsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				gradeSubjectsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(gradeSubjectsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);

				gradeSubjectsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(gradeSubjectsWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);
				System.out.println("fetchGradeSubjects  successful");

				vector.addElement(gradeSubjectsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.gradeSubjectsWrapper = new GradeSubjectsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.gradeSubjectsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
			} else

			{
				dataArrayWrapper.gradeSubjectsWrapper = new GradeSubjectsWrapper[1];
				dataArrayWrapper.gradeSubjectsWrapper[0] = gradeSubjectsWrapper;
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

	public AbstractWrapper insertGradeSubjects(UsersWrapper usersProfileWrapper,
			GradeSubjectsWrapper gradeSubjectsWrapper) throws Exception {

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
			con = getConnection();

			if (Utility.isEmpty(gradeSubjectsWrapper.academicYearID) == true) {

				// ---- CurrentAcademicYear--

				// ------------get MST_Parameter table details----
				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				gradeSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

				// ----------
			}

			sql = " INSERT INTO GradeSubjects( AcademicYearID,GradeID,SubjectID,MakerID, MakerDateTime, SchoolID)  "
					+ "Values (?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, gradeSubjectsWrapper.academicYearID);
			pstmt.setString(2, Utility.trim(gradeSubjectsWrapper.gradeID));
			pstmt.setString(3, Utility.trim(gradeSubjectsWrapper.subjectID));
			pstmt.setString(4, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(5, Utility.getCurrentTime()); // maker date time
			pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid);

			pstmt.executeUpdate();
			pstmt.close();

			gradeSubjectsWrapper.recordFound = true;

			dataArrayWrapper.gradeSubjectsWrapper = new GradeSubjectsWrapper[1];
			dataArrayWrapper.gradeSubjectsWrapper[0] = gradeSubjectsWrapper;

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
	public AbstractWrapper updateGradeSubjects(UsersWrapper usersProfileWrapper,
			GradeSubjectsWrapper gradeSubjectsWrapper) throws Exception {

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

		try {
			con = getConnection();

			if (Utility.isEmpty(gradeSubjectsWrapper.academicYearID) == true) {

				// ---- CurrentAcademicYear--

				// ------------get MST_Parameter table details----
				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				gradeSubjectsWrapper.academicYearID = parameterWrapper.currentAcademicYear;

				// ----------

				// ----------
			}
			// ----------

			sql = "SELECT SubjectID FROM GradeSubjects WHERE AcademicYearID=? AND GradeID=? AND SubjectID=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			System.out.println(" currentAcademicYear   is " + gradeSubjectsWrapper.academicYearID);
			System.out.println(" Student gradeID   is " + gradeSubjectsWrapper.gradeID);
			System.out.println(" Subject ID  is " + gradeSubjectsWrapper.subjectID);

			pstmt.setString(1, gradeSubjectsWrapper.academicYearID);
			pstmt.setString(2, Utility.trim(gradeSubjectsWrapper.gradeID));
			pstmt.setString(3, Utility.trim(gradeSubjectsWrapper.subjectID));
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				if (!gradeSubjectsWrapper.deleteFlag.equals("Y")) {
					dataArrayWrapper = (DataArrayWrapper) insertGradeSubjects(usersProfileWrapper,
							gradeSubjectsWrapper);
				} else {
					// to show to no records available
					dataArrayWrapper.gradeSubjectsWrapper = new GradeSubjectsWrapper[1];
					dataArrayWrapper.gradeSubjectsWrapper[0] = gradeSubjectsWrapper;
					dataArrayWrapper.recordFound = true;
				}
			} else {
				resultSet.close();
				pstmt.close();

				if (gradeSubjectsWrapper.deleteFlag.equals("Y")) {

					pstmt = con.prepareStatement(
							"DELETE FROM GradeSubjects WHERE AcademicYearID=? AND GradeID=? AND SubjectID=? and SchoolID=?");

					pstmt.setString(1, gradeSubjectsWrapper.academicYearID);
					pstmt.setString(2, Utility.trim(gradeSubjectsWrapper.gradeID));
					pstmt.setString(3, Utility.trim(gradeSubjectsWrapper.subjectID));
					pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					System.out.println(" Grade Subject deleted ");

				} else {

					if (pstmt != null)
						pstmt.close();

					pstmt = con.prepareStatement("UPDATE GradeSubjects SET "
							+ "ModifierID=?, ModifierDateTime=? WHERE AcademicYearID=? AND GradeID=? AND SubjectID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(2, Utility.getCurrentTime()); // modifier date time

					pstmt.setString(3, gradeSubjectsWrapper.academicYearID);
					pstmt.setString(4, Utility.trim(gradeSubjectsWrapper.gradeID));
					pstmt.setString(5, Utility.trim(gradeSubjectsWrapper.subjectID));
					pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					System.out.println(" Grade Subject updated ");

				}

				gradeSubjectsWrapper.recordFound = true;

				dataArrayWrapper.gradeSubjectsWrapper = new GradeSubjectsWrapper[1];
				dataArrayWrapper.gradeSubjectsWrapper[0] = gradeSubjectsWrapper;
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
	// -----------------End updateGradeSubjects---------------------

}
