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
import com.rootmind.wrapper.TeacherReportsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TeacherReportsHelper extends Helper {

	// ------------------------ Start fetchTeacherReports-------------------------
	public AbstractWrapper fetchTeacherReports(TeacherReportsWrapper teacherReportsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchTeaherReports Grade is " + teacherReportsWrapper.reportID);

		Vector<Object> vector = new Vector<Object>();

		String sql = null;
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;
		try {
			con = getConnection();

			PopoverHelper popoverHelper = new PopoverHelper();

			/*
			 * //-----get current academic year code--
			 * 
			 * sql="SELECT CurrentAcademicYear from MST_Parameter";
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * currentAcademicYear=Utility.trim(resultSet.getString("CurrentAcademicYear"));
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */

			sql = "SELECT  Userid,ReportID, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID "
					+ " FROM TeacherReports  WHERE UserID=? and SchoolID=?";

			if (teacherReportsWrapper.reportID != null && !teacherReportsWrapper.reportID.trim().equals("")) {
				sql = "SELECT  Userid, ReportID,  MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID "
						+ " FROM TeacherReports  WHERE Userid=? and SchoolID=? AND ReportID=? ";
			}
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(teacherReportsWrapper.staffUserID));
			pstmt.setString(2, Utility.trim(teacherReportsWrapper.schoolID));

			if (teacherReportsWrapper.reportID != null && !teacherReportsWrapper.reportID.trim().equals("")) {
				pstmt.setString(2, Utility.trim(teacherReportsWrapper.reportID));

			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				teacherReportsWrapper = new TeacherReportsWrapper();

				teacherReportsWrapper.staffUserID = Utility.trim(resultSet.getString("UserID"));

				teacherReportsWrapper.reportID = Utility.trim(resultSet.getString("ReportID"));

				teacherReportsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				teacherReportsWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				teacherReportsWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				teacherReportsWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				teacherReportsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				teacherReportsWrapper.recordFound = true;

				teacherReportsWrapper.reportIDValue = popoverHelper.fetchPopoverDesc(teacherReportsWrapper.reportID,
						"MST_Reports", teacherReportsWrapper.schoolID);

				System.out.println("fetchTeacherReports  successful");

				vector.addElement(teacherReportsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.teacherReportsWrapper = new TeacherReportsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.teacherReportsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
			} else

			{
				dataArrayWrapper.teacherReportsWrapper = new TeacherReportsWrapper[1];
				dataArrayWrapper.teacherReportsWrapper[0] = teacherReportsWrapper;
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
	// ------------------------ End fetchTeacherReports-------------------------

	// -----------------Start insertTeacherReports---------------------

	public AbstractWrapper insertTeacherReports(UsersWrapper usersProfileWrapper,
			TeacherReportsWrapper teacherReportsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// String countryCode=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			/*
			 * //---- CurrentAcademicYear--
			 * 
			 * pstmt =
			 * con.prepareStatement("SELECT  CurrentAcademicYear from MST_Parameter");
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * 
			 * currentAcademicYear=Utility.trim(resultSet.getString("CurrentAcademicYear"));
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */
			sql = " INSERT INTO TeacherReports(UserID, ReportID,MakerID, MakerDateTime, SchoolID)  "
					+ "Values (?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(teacherReportsWrapper.staffUserID));
			pstmt.setString(2, Utility.trim(teacherReportsWrapper.reportID));

			pstmt.setString(3, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(4, Utility.getCurrentTime()); // maker date time
			pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("teacherReports Userid " + teacherReportsWrapper.staffUserID);

			pstmt.executeUpdate();
			pstmt.close();

			teacherReportsWrapper.recordFound = true;

			dataArrayWrapper.teacherReportsWrapper = new TeacherReportsWrapper[1];
			dataArrayWrapper.teacherReportsWrapper[0] = teacherReportsWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into TeacherReports");

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

	// -----------------End insertTeacherReports---------------------

	// -----------------Start updateTeacherReports---------------------
	public AbstractWrapper updateTeacherReports(UsersWrapper usersProfileWrapper,
			TeacherReportsWrapper teacherReportsWrapper) throws Exception {

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

			/*
			 * //-----get current academic year code--
			 * 
			 * sql="SELECT CurrentAcademicYear from MST_Parameter";
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * currentAcademicYear=Utility.trim(resultSet.getString("CurrentAcademicYear"));
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */

			sql = "SELECT ReportID FROM TeacherReports WHERE UserID=? AND ReportID=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			System.out.println(" Student gradeID   is " + teacherReportsWrapper.reportID);

			pstmt.setString(1, Utility.trim(teacherReportsWrapper.staffUserID));
			pstmt.setString(2, Utility.trim(teacherReportsWrapper.reportID));
			pstmt.setString(3, Utility.trim(teacherReportsWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertTeacherReports(usersProfileWrapper, teacherReportsWrapper);
			} else {
				resultSet.close();
				pstmt.close();

				if (teacherReportsWrapper.deleteFlag.equals("Y")) {

					pstmt = con.prepareStatement(
							"DELETE FROM TeacherReports WHERE UserID=? AND ReportID=? AND SchoolID=?");

					pstmt.setString(1, Utility.trim(teacherReportsWrapper.staffUserID));
					pstmt.setString(2, Utility.trim(teacherReportsWrapper.reportID));
					pstmt.setString(3, Utility.trim(teacherReportsWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

				}
				/*
				 * else {
				 * 
				 * if (resultSet!=null) resultSet.close();
				 * 
				 * if(pstmt!=null) pstmt.close();
				 * 
				 * pstmt = con.prepareStatement("UPDATE TeacherSubjects SET SubjectID=?, " +
				 * "ModifierID=?, ModifierDateTime=? WHERE AcademicYearID=? AND UserID=? AND GradeID=?"
				 * );
				 * 
				 * 
				 * pstmt.setString(1,Utility.trim(teacherSubjectsWrapper.subjectID));
				 * pstmt.setString(2,Utility.trim(teacherSubjectsWrapper.modifierID));
				 * pstmt.setTimestamp(3,Utility.getCurrentTime()); // modifier date time
				 * 
				 * 
				 * pstmt.setString(4,Utility.trim(teacherSubjectsWrapper.academicYearID));
				 * pstmt.setString(5,Utility.trim(teacherSubjectsWrapper.gradeID));
				 * pstmt.setString(5,Utility.trim(teacherSubjectsWrapper.gradeID));
				 * 
				 * pstmt.executeUpdate(); pstmt.close();
				 * 
				 * teacherSubjectsWrapper.recordFound=true; }
				 */

				teacherReportsWrapper.recordFound = true;
				dataArrayWrapper.teacherReportsWrapper = new TeacherReportsWrapper[1];
				dataArrayWrapper.teacherReportsWrapper[0] = teacherReportsWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println(" Report already existed");
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
	// -----------------End updateGradeReports---------------------

	// ------------------------ Start fetchTeacherReports-------------------------
	public boolean findTeacherReports(TeacherReportsWrapper teacherReportsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();

		System.out.println("fetchTeaherReports  is " + teacherReportsWrapper.reportID);

		// Vector<Object> vector = new Vector<Object>();

		String sql = null;
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;

		boolean recordFound = false;
		try {
			con = getConnection();

			// PopoverHelper popoverHelper = new PopoverHelper();

			/*
			 * //-----get current academic year code--
			 * 
			 * sql="SELECT CurrentAcademicYear from MST_Parameter";
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * currentAcademicYear=Utility.trim(resultSet.getString("CurrentAcademicYear"));
			 * 
			 * }
			 * 
			 * resultSet.close(); pstmt.close();
			 * 
			 * //----------
			 */

			sql = "SELECT UserID,ReportID FROM TeacherReports WHERE UserID=? AND ReportID=? and SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(teacherReportsWrapper.staffUserID));
			pstmt.setString(2, Utility.trim(teacherReportsWrapper.reportID));
			pstmt.setString(3, Utility.trim(teacherReportsWrapper.schoolID));

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
	// ------------------------ End fetchTeacherReports-------------------------

}
