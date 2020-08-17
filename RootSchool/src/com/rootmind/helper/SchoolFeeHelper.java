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
import com.rootmind.wrapper.SchoolFeeWrapper;

import com.rootmind.wrapper.UsersWrapper;

public class SchoolFeeHelper extends Helper {

	// -----------------Start updateSchoolFee---------------------
	public AbstractWrapper updateSchoolFee(UsersWrapper usersProfileWrapper, SchoolFeeWrapper schoolFeeWrapper)
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
		String currentAcademicYear = null;
		System.out.println("Start updateSchoolFee " + schoolFeeWrapper.feeType);

		try {
			con = getConnection();

			// -----current Academic Year code--

			pstmt = con.prepareStatement("SELECT CurrentAcademicYear from MST_Parameter where SchoolID=?");

			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				currentAcademicYear = resultSet.getString("CurrentAcademicYear");

			}

			resultSet.close();
			pstmt.close();

			// ----------

			pstmt = con.prepareStatement(
					"SELECT InvoiceNo FROM SchoolFee WHERE AcademicYearID=? AND InvoiceNo=? AND RefNo=? AND StudentID=? and SchoolID=?");

			System.out.println("School Fee  RefNo is" + schoolFeeWrapper.refNo);

			pstmt.setString(1, currentAcademicYear);
			pstmt.setString(2, Utility.trim(schoolFeeWrapper.invoiceNo));
			pstmt.setString(3, Utility.trim(schoolFeeWrapper.refNo));
			pstmt.setString(4, Utility.trim(schoolFeeWrapper.studentID));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertSchoolFee(usersProfileWrapper, schoolFeeWrapper);
			} else {
				resultSet.close();
				pstmt.close();

				pstmt = con.prepareStatement(
						"UPDATE SchoolFee SET FeeType=?,FeeAmount=?,PaymentDate=?,ModifierID=?,ModifierDateTime=? "
								+ "WHERE AcademicYearID=? AND InvoiceNo=?  AND RefNo=?  AND  StudentID=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(schoolFeeWrapper.feeType));
				pstmt.setString(2, Utility.trim(schoolFeeWrapper.feeAmount));
				pstmt.setDate(3, Utility.getDate(schoolFeeWrapper.paymentDate));
				pstmt.setString(4, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(5, Utility.getCurrentTime());

				pstmt.setString(6, currentAcademicYear);
				pstmt.setString(7, Utility.trim(schoolFeeWrapper.invoiceNo));
				pstmt.setString(8, Utility.trim(schoolFeeWrapper.refNo));
				pstmt.setString(9, Utility.trim(schoolFeeWrapper.studentID));
				pstmt.setString(10, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				schoolFeeWrapper.recordFound = true;
				dataArrayWrapper.schoolFeeWrapper = new SchoolFeeWrapper[1];
				dataArrayWrapper.schoolFeeWrapper[0] = schoolFeeWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully School Fee Updated");
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
	// -----------------End updateSchoolFee---------------------

	// -----------------Start insertSchoolFee---------------------

	public AbstractWrapper insertSchoolFee(UsersWrapper usersProfileWrapper, SchoolFeeWrapper schoolFeeWrapper)
			throws Exception {

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
		String currentAcademicYear = null;

		System.out.println("Start insertSchoolFee " + schoolFeeWrapper.feeType);

		try {
			con = getConnection();

			// -----country code--

			sql = "SELECT CurrentAcademicYear from MST_Parameter where SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				currentAcademicYear = resultSet.getString("CurrentAcademicYear");

			}

			resultSet.close();
			pstmt.close();

			// ----------

			sql = " INSERT INTO SchoolFee(AcademicYearID,RefNo,InvoiceNo, StudentID, GradeID, SectionID,FeeType ,FeeAmount,PaymentDate, "
					+ " MakerID, MakerDateTime, SchoolID) Values (?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			schoolFeeWrapper.invoiceNo = generateInvoiceNo(usersProfileWrapper.schoolID);

			pstmt.setString(1, currentAcademicYear);
			pstmt.setString(2, Utility.trim(schoolFeeWrapper.refNo));
			pstmt.setString(3, Utility.trim(schoolFeeWrapper.invoiceNo));
			pstmt.setString(4, Utility.trim(schoolFeeWrapper.studentID));
			pstmt.setString(5, Utility.trim(schoolFeeWrapper.gradeID));
			pstmt.setString(6, Utility.trim(schoolFeeWrapper.sectionID));
			System.out.println("Fee Type " + schoolFeeWrapper.feeType);
			pstmt.setString(7, Utility.trim(schoolFeeWrapper.feeType));
			pstmt.setString(8, Utility.trim(schoolFeeWrapper.feeAmount));
			pstmt.setDate(9, Utility.getDate(schoolFeeWrapper.paymentDate));
			pstmt.setString(10, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(11, Utility.getCurrentTime()); // maker date time
			pstmt.setString(12, Utility.trim(usersProfileWrapper.schoolID));

			System.out.println("insert SchoolFeer Userid " + usersProfileWrapper.userid);

			pstmt.executeUpdate();
			pstmt.close();

			schoolFeeWrapper.recordFound = true;

			dataArrayWrapper.schoolFeeWrapper = new SchoolFeeWrapper[1];
			dataArrayWrapper.schoolFeeWrapper[0] = schoolFeeWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into SchoolFee");

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

	// -----------------End insertSchoolFee---------------------

	// -----------------Start fetchSchoolFee---------------------

	public AbstractWrapper fetchSchoolFee(UsersWrapper usersProfileWrapper, SchoolFeeWrapper schoolFeeWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchSchoolFee RefNo" + schoolFeeWrapper.refNo);

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		String currentAcademicYear = null;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// -----currentAcademicYear code--

			pstmt = con.prepareStatement("SELECT CurrentAcademicYear from MST_Parameter where SchoolID=?");

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				currentAcademicYear = Utility.trim(resultSet.getString("CurrentAcademicYear"));

			}

			resultSet.close();
			pstmt.close();

			// ----------

			pstmt = con.prepareStatement("SELECT AcademicYearID,RefNo,InvoiceNo,StudentID, "
					+ "GradeID, SectionID, FeeType,FeeAmount,PaymentDate,MakerID,MakerDateTime,ModifierID,ModifierDateTime, SchoolID "
					+ " FROM SchoolFee WHERE AcademicYearID=? AND RefNo=? AND StudentID=? and SchoolID=?");

			pstmt.setString(1, currentAcademicYear);
			pstmt.setString(2, Utility.trim(schoolFeeWrapper.refNo));
			pstmt.setString(3, Utility.trim(schoolFeeWrapper.studentID));
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				schoolFeeWrapper = new SchoolFeeWrapper();

				schoolFeeWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				schoolFeeWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				schoolFeeWrapper.invoiceNo = Utility.trim(resultSet.getString("InvoiceNo"));
				schoolFeeWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));

				schoolFeeWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				schoolFeeWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				schoolFeeWrapper.feeType = Utility.trim(resultSet.getString("FeeType"));
				schoolFeeWrapper.feeAmount = Utility.trim(resultSet.getString("FeeAmount"));
				schoolFeeWrapper.paymentDate = Utility.setDate(resultSet.getString("PaymentDate"));

				schoolFeeWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				schoolFeeWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				schoolFeeWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				schoolFeeWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));
				schoolFeeWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				schoolFeeWrapper.recordFound = true;

				schoolFeeWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(schoolFeeWrapper.academicYearID,
						"MST_AcademicYear", schoolFeeWrapper.schoolID);
				schoolFeeWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(schoolFeeWrapper.gradeID, "MST_Grade",
						schoolFeeWrapper.schoolID);
				schoolFeeWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(schoolFeeWrapper.sectionID,
						"MST_Section", schoolFeeWrapper.schoolID);
				schoolFeeWrapper.feeTypeValue = popoverHelper.fetchPopoverDesc(schoolFeeWrapper.feeType, "MST_FeeType",
						schoolFeeWrapper.schoolID);

				System.out.println("School Fee Details fetch successful");

				vector.addElement(schoolFeeWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.schoolFeeWrapper = new SchoolFeeWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.schoolFeeWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else {
				dataArrayWrapper.schoolFeeWrapper = new SchoolFeeWrapper[1];
				dataArrayWrapper.schoolFeeWrapper[0] = schoolFeeWrapper;
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
	// -----------------End fetchSchoolFee---------------------

	// -----------------Generate InvoiceNo-------------------------------
	public String generateInvoiceNo(String schoolID) throws Exception {

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

		int invoiceNo = 0;
		String finalInvoiceNo = null;
		String invoiceCode = null;

		try {
			con = getConnection();

			sql = "SELECT InvoiceNo,InvoiceCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				invoiceNo = resultSet.getInt("InvoiceNo");
				System.out.println("invoiceNo=" + invoiceNo);
				invoiceCode = resultSet.getString("InvoiceCode");

			}

			resultSet.close();
			pstmt.close();

			if (invoiceNo == 0) {
				invoiceNo = 1;

			} else {

				invoiceNo = invoiceNo + 1;
			}

			sql = "UPDATE MST_Parameter set InvoiceNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, invoiceNo);
			pstmt.setString(2, schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			finalInvoiceNo = invoiceCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", invoiceNo);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated finalInvoiceNo " + finalInvoiceNo);

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

		return finalInvoiceNo;
	}

	// -----------------End Generate InvoiceNo---------------------------

}
