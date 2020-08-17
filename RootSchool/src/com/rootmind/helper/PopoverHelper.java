package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
//import java.util.Arrays;
//import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.controller.AES128Crypto;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ErrorWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.PopoverWrapper;
import com.rootmind.wrapper.SchoolFeeWrapper;
import com.rootmind.wrapper.SchoolWrapper;
import com.rootmind.wrapper.StudentAcademicsWrapper;
import com.rootmind.wrapper.UserMenuWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class PopoverHelper extends Helper {

	private static final SchoolWrapper schoolWrapper = null;

	public AbstractWrapper fetchPopoverData(UsersWrapper usersProfileWrapper, PopoverWrapper popoverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;
		String filter = null;
		String tableName = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {
			con = getConnection();

			filter = popoverWrapper.filter;
			tableName = popoverWrapper.tableName;

			System.out.println("fetchPopoverData in try Table name is " + popoverWrapper.tableName + " schoolID "
					+ usersProfileWrapper.schoolID);

			// sql="SELECT Code, Description FROM [" + popoverWrapper.tableName + "]";
			sql = "SELECT Code, Description, SchoolID FROM " + popoverWrapper.tableName + " WHERE SchoolID=?";

			if (filter != null && !filter.isEmpty()) {
				// sql="SELECT Code, Description,Filter FROM [" + popoverWrapper.tableName + "]
				// WHERE Filter=?";

				sql = "SELECT Code, Description,Filter, SchoolID FROM " + popoverWrapper.tableName
						+ " WHERE SchoolID=? and Filter=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			if (filter != null && !filter.isEmpty()) {
				pstmt.setString(1, usersProfileWrapper.schoolID);
				pstmt.setString(2, filter.trim());
			} else {
				pstmt.setString(1, usersProfileWrapper.schoolID);

			}
			// pstmt.setString(1,tableName);

			// System.out.println("SQL statement is " + pstmt);

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				popoverWrapper = new PopoverWrapper();

				popoverWrapper.code = Utility.trim(resultSet.getString("Code"));

				// System.out.println("Popover Code " + popoverWrapper.code);

				popoverWrapper.desc = Utility.trim(resultSet.getString("Description"));
				// System.out.println("Popover Desc" + popoverWrapper.desc);

				if (filter != null && !filter.isEmpty()) {
					popoverWrapper.filter = Utility.trim(resultSet.getString("Filter"));
				}

				popoverWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				popoverWrapper.tableName = tableName;
				popoverWrapper.recordFound = true;

				vector.addElement(popoverWrapper);

			}

			if (vector.size() > 0) {

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.popoverWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}
			resultSet.close();
			pstmt.close();

			System.out.println("fetchPopover Data completed ");

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

	// --------------updatePopoverWrapper data-------------

	public AbstractWrapper updateMasterData(UsersWrapper usersProfileWrapper, PopoverWrapper popoverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		PreparedStatement pstmt = null;

		String sql = null;

		try {
			con = getConnection();

			System.out.println("Popover insert table schoolID " + usersProfileWrapper.schoolID);

			// pstmt = con.prepareStatement("SELECT Code FROM [" + popoverWrapper.tableName
			// + "] WHERE Code=?");

			pstmt = con
					.prepareStatement("SELECT Code FROM " + popoverWrapper.tableName + " WHERE SchoolID=? and Code=?");

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(2, Utility.trim(Utility.removeSpaces(popoverWrapper.code)));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();

				System.out.println("Popover insert table " + popoverWrapper.tableName);
				// sql="INSERT INTO [" + popoverWrapper.tableName + "] (Code,Description)
				// VALUES(?,?)";

				sql = "INSERT INTO " + popoverWrapper.tableName
						+ " (Code,Description, SchoolID, MakerID, MakerDateTime) VALUES(?,?,?,?,?)";

				if (Utility.isEmpty(popoverWrapper.filter) == false) {

					// sql="INSERT INTO [" + popoverWrapper.tableName + "](Code,Description,Filter)
					// VALUES(?,?,?)";
					sql = "INSERT INTO " + popoverWrapper.tableName
							+ " (Code,Description, SchoolID, MakerID, MakerDateTime, Filter) VALUES(?,?,?,?,?,?)";
				}

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(Utility.removeSpaces(popoverWrapper.code)));
				pstmt.setString(2, Utility.trim(popoverWrapper.desc));
				pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.setString(4, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(5, Utility.getCurrentTime()); // maker date time

				if (Utility.isEmpty(popoverWrapper.filter) == false) {

					pstmt.setString(6, Utility.trim(popoverWrapper.filter));
				}

				pstmt.executeUpdate();
				pstmt.close();

				popoverWrapper.recordFound = true;

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[1];
				dataArrayWrapper.popoverWrapper[0] = popoverWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully " + popoverWrapper.tableName + " inserted");

			} else {

				resultSet.close();
				pstmt.close();

				System.out.println("Popover update table " + popoverWrapper.tableName);
				// sql="UPDATE [" + popoverWrapper.tableName + "] SET Description=? WHERE
				// Code=?";

				sql = "UPDATE " + popoverWrapper.tableName
						+ " SET Description=?, ModifierID=?, ModifierDateTime=? WHERE SchoolID=? and Code=?";

				if (popoverWrapper.filter != null && !popoverWrapper.filter.equals("")) {

					// sql="UPDATE [" + popoverWrapper.tableName + "] SET Description=?, Filter=?
					// WHERE Code=?";
					sql = "UPDATE " + popoverWrapper.tableName
							+ " SET Description=?, ModifierID=?, ModifierDateTime=? Filter=? WHERE SchoolID=? and Code=?";
				}

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(popoverWrapper.desc));
				pstmt.setString(2, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(3, Utility.getCurrentTime()); // maker date time

				if (popoverWrapper.filter != null && !popoverWrapper.filter.equals("")) {
					pstmt.setString(4, Utility.trim(popoverWrapper.filter));
					pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));
					pstmt.setString(6, Utility.trim(popoverWrapper.code));
				} else {
					pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));
					pstmt.setString(5, Utility.trim(popoverWrapper.code));
				}

				pstmt.executeUpdate();
				pstmt.close();

				popoverWrapper.recordFound = true;

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[1];
				dataArrayWrapper.popoverWrapper[0] = popoverWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully " + popoverWrapper.tableName + " Updated");
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

	// --------------end updatePopoverWrapper data---------

	// --------------fetchMultiPopoverData-------
	public AbstractWrapper fetchMultiPopoverData(UsersWrapper usersProfileWrapper, PopoverWrapper[] popoverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;
		String filter = null;
		String tableName = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PopoverWrapper recordPopoverWrapper = null;

		try {
			con = getConnection();
			PreparedStatement pstmt = null;
			for (int i = 0; i <= popoverWrapper.length - 1; i++) {

				System.out.println("popoverWrapper.length schoolID" + usersProfileWrapper.schoolID);

				filter = "";
				tableName = "";

				filter = popoverWrapper[i].filter;
				tableName = popoverWrapper[i].tableName;

				System.out.println("fetchMultiPopoverData in try Table name is " + popoverWrapper[i].tableName);

				// sql="SELECT Code, Description FROM [" + popoverWrapper[i].tableName + "]";

				sql = "SELECT Code, Description, SchoolID FROM " + popoverWrapper[i].tableName + " WHERE SchoolID=?";

				// System.out.println("fetchMultiPopoverData SQL is " + sql);

				if (filter != null && !filter.equals("")) {
					// sql="SELECT Code, Description,Filter FROM [" + popoverWrapper[i].tableName +
					// "] WHERE Filter=?";
					sql = "SELECT Code, Description, SchoolID, Filter FROM " + popoverWrapper[i].tableName
							+ " WHERE SchoolID=? and Filter=?";

				}

				pstmt = con.prepareStatement(sql);

				if (filter != null && !filter.equals("")) {
					pstmt.setString(1, usersProfileWrapper.schoolID);
					pstmt.setString(2, filter.trim());
				} else {
					pstmt.setString(1, usersProfileWrapper.schoolID);

				}
				// pstmt.setString(1,tableName);

				// System.out.println("SQL statement is " + pstmt);

				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {

					recordPopoverWrapper = new PopoverWrapper();

					recordPopoverWrapper.code = Utility.trim(resultSet.getString("Code"));

					// System.out.println("Popover Code " + recordPopoverWrapper.code);

					recordPopoverWrapper.desc = Utility.trim(resultSet.getString("Description"));
					// System.out.println("Popover Desc" + recordPopoverWrapper.desc);

					recordPopoverWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

					if (filter != null && !filter.equals("")) {
						recordPopoverWrapper.filter = Utility.trim(resultSet.getString("Filter"));
					}

					recordPopoverWrapper.tableName = tableName;
					recordPopoverWrapper.recordFound = true;

					vector.addElement(recordPopoverWrapper);

				}

				resultSet.close();
				pstmt.close();
			}
			if (vector.size() > 0) {

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.popoverWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total master data in fetch " + vector.size());

			}

			System.out.println("fetch MultiPopover Data completed ");

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

	public String fetchPopoverDesc(String code, String tableName, String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String desc = null;

		try {
			con = getConnection();

			// System.out.println("fetchPopoverDesc in try Table name is" + tableName);

			// PreparedStatement pstmt = con.prepareStatement("SELECT Code, Description FROM
			// [" + tableName + "] WHERE Code=?");

			PreparedStatement pstmt = con.prepareStatement(
					"SELECT Code, Description, SchoolID FROM " + tableName + " WHERE SchoolID=? and Code=?");
			// pstmt.setString(1,tableName);

			pstmt.setString(1, Utility.trim(schoolID));
			pstmt.setString(2, Utility.trim(code));

			// System.out.println("SQL statement is " + pstmt);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				desc = Utility.trim(resultSet.getString("Description"));

				// System.out.println("Desc" + desc);

				// System.out.println("fetchPopover Desc completed " );

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
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return desc;
	}

	public AbstractWrapper fetchTableNames(UsersWrapper usersProfileWrapper, PopoverWrapper popoverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {
			con = getConnection();

			sql = "SELECT Code, Description, SchoolID, Filter FROM MST_TableNames WHERE SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				popoverWrapper = new PopoverWrapper();

				popoverWrapper.code = Utility.trim(resultSet.getString("Code"));

				// System.out.println("Popover Code " + popoverWrapper.code);

				popoverWrapper.desc = Utility.trim(resultSet.getString("Description"));
				// System.out.println("Popover Desc" + popoverWrapper.desc);

				popoverWrapper.tableFilter = Utility.trim(resultSet.getString("Filter"));

				popoverWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				popoverWrapper.recordFound = true;

				vector.addElement(popoverWrapper);

			}

			if (vector.size() > 0) {

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.popoverWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetchTableNames " + vector.size());

			}
			resultSet.close();
			pstmt.close();

			System.out.println("Popover info  fetchTableNames completed ");

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

	public AbstractWrapper fetchMasterData(UsersWrapper usersProfileWrapper, PopoverWrapper popoverWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String sql = null;
		String filter = null;
		String tableName = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {
			con = getConnection();

			filter = popoverWrapper.filter;
			tableName = popoverWrapper.tableName;

			System.out.println("fetchMasterData in try Table name is " + popoverWrapper.tableName);

			// sql="SELECT Code, Description FROM [" + popoverWrapper.tableName + "]";

			sql = "SELECT Code, Description, SchoolID, MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM "
					+ popoverWrapper.tableName + " WHERE SchoolID=?";

			System.out.println("SQL statement is SchoolID" + usersProfileWrapper.schoolID);

			if (filter != null && filter.equals("Y")) {
				// sql="SELECT Code, Description,Filter FROM [" + popoverWrapper.tableName + "]
				// ";
				sql = "SELECT Code, Description, SchoolID, Filter, MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM "
						+ popoverWrapper.tableName + " WHERE SchoolID=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			// System.out.println("SQL statement is " + pstmt);

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				popoverWrapper = new PopoverWrapper();

				popoverWrapper.code = Utility.trim(resultSet.getString("Code"));

				// System.out.println("Popover Code " + popoverWrapper.code);

				popoverWrapper.desc = Utility.trim(resultSet.getString("Description"));
				// System.out.println("Popover Desc" + popoverWrapper.desc);

				if (filter != null && filter.equals("Y")) {
					popoverWrapper.filter = Utility.trim(resultSet.getString("Filter"));
				}

				popoverWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				popoverWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				popoverWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				popoverWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				popoverWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));

				popoverWrapper.tableName = tableName;
				popoverWrapper.recordFound = true;

				vector.addElement(popoverWrapper);

			}

			if (vector.size() > 0) {

				dataArrayWrapper.popoverWrapper = new PopoverWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.popoverWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			System.out.println("Popover info  fetchMasterData completed ");

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

	// --------------create School Wrapper Registration-------------

	public AbstractWrapper createSchoolRegister(SchoolWrapper schoolWrapper) throws Exception {

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
		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		String sql = null;

		boolean duplicateCheck = false;

		try {
			con = getConnection();

			schoolWrapper.code = Utility.trim(Utility.removeSpaces(schoolWrapper.code));
			schoolWrapper.schoolID = Utility.trim(Utility.removeSpaces(schoolWrapper.schoolID));
			schoolWrapper.adminID = Utility.trim(Utility.removeSpaces(schoolWrapper.adminID));

			schoolWrapper.adminID = schoolWrapper.adminID + "@" + schoolWrapper.schoolID;

			duplicateCheck = duplicateDataCheck("SELECT Code FROM MST_School WHERE Code=?",
					Utility.trim(Utility.removeSpaces(schoolWrapper.code)));
			if (duplicateCheck == true) {
				ErrorWrapper errorWrapper = new ErrorWrapper();
				errorWrapper.errorCode = "ERR001";
				errorWrapper.errorDesc = "School ID " + schoolWrapper.schoolID
						+ " is not allowed, please assign new School ID";
				vector.add(errorWrapper);
			}

			if (duplicateCheck == false) {
				// Check for email id duplicate during school register
				duplicateCheck = duplicateDataCheck("SELECT Code FROM MST_School WHERE Email=?",
						Utility.trim(Utility.removeSpaces(schoolWrapper.email)));
				if (duplicateCheck == true) {
					ErrorWrapper errorWrapper = new ErrorWrapper();
					errorWrapper.errorCode = "ERR001";
					errorWrapper.errorDesc = "Email ID " + Utility.trim(Utility.removeSpaces(schoolWrapper.email))
							+ " is not allowed, please assign new Email ID";
					vector.add(errorWrapper);
				}
			}

			if (duplicateCheck == false) {
				// Check for mobile number duplicate during school register
				duplicateCheck = duplicateDataCheck("SELECT Code FROM MST_School WHERE Mobile=?",
						Utility.trim(Utility.removeSpaces(schoolWrapper.mobile)));
				if (duplicateCheck == true) {
					ErrorWrapper errorWrapper = new ErrorWrapper();
					errorWrapper.errorCode = "ERR001";
					errorWrapper.errorDesc = "Mobile Number " + Utility.trim(Utility.removeSpaces(schoolWrapper.mobile))
							+ " is not allowed, please assign new mobile number";
					vector.add(errorWrapper);
				}
			}

			if (duplicateCheck == false) {

				sql = "INSERT INTO MST_School (Code,Description, SchoolID, SchoolName, Address1, Address2, Address3,"
						+ "City, State, Country, Email, Mobile, Phone, AdminID, "
						+ "Active, EmailVerified, MobileVerified, FirebaseID, Pincode, District, "
						+ "MakerID, MakerDateTime) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(Utility.removeSpaces(schoolWrapper.code)));
				pstmt.setString(2, Utility.trim(schoolWrapper.desc));
				pstmt.setString(3, Utility.trim(Utility.removeSpaces(schoolWrapper.code)));
				pstmt.setString(4, Utility.trim(schoolWrapper.desc));
				pstmt.setString(5, Utility.trim(schoolWrapper.address1));
				pstmt.setString(6, Utility.trim(schoolWrapper.address2));
				pstmt.setString(7, Utility.trim(schoolWrapper.address3));
				pstmt.setString(8, Utility.trim(schoolWrapper.city));
				pstmt.setString(9, Utility.trim(schoolWrapper.state));
				pstmt.setString(10, Utility.trim(schoolWrapper.country));
				pstmt.setString(11, Utility.trim(Utility.removeSpaces(schoolWrapper.email)));
				pstmt.setString(12, Utility.trim(Utility.removeSpaces(schoolWrapper.mobile)));
				pstmt.setString(13, Utility.trim(schoolWrapper.phone));
				pstmt.setString(14, schoolWrapper.adminID);
				pstmt.setString(15, Utility.trim(schoolWrapper.active));
				pstmt.setString(16, Utility.trim(schoolWrapper.emailVerified));
				pstmt.setString(17, Utility.trim(schoolWrapper.mobileVerified));
				pstmt.setString(18, Utility.trim(schoolWrapper.firebaseID));
				pstmt.setString(19, Utility.trim(schoolWrapper.pinCode));
				pstmt.setString(20, Utility.trim(schoolWrapper.district));
				pstmt.setString(21, schoolWrapper.adminID);
				pstmt.setTimestamp(22, Utility.getCurrentTime()); // maker date time

				pstmt.executeUpdate();
				pstmt.close();

				schoolWrapper.recordFound = true;

				dataArrayWrapper.schoolWrapper = new SchoolWrapper[1];
				dataArrayWrapper.schoolWrapper[0] = schoolWrapper;

				dataArrayWrapper.recordFound = true;

				// ----create MST_Parameter table for new school
				createSchoolParameter(schoolWrapper);

				// ----create Admin profile for new school
				UsersHelper usersHelper = new UsersHelper();
				usersHelper.createAdminProfile(schoolWrapper);

				// ------create UserMenu for new school
				createUserMenu(schoolWrapper);

				// -------create Master Table Names----
				createMasterTableNames(schoolWrapper);

				// --------create Master Table Date--
				createMasterTableData(schoolWrapper);

				System.out.println("Successfully " + Utility.trim(Utility.removeSpaces(schoolWrapper.code)) + " -"
						+ schoolWrapper.schoolName + " inserted");

			}
			// else
			// {

			// resultSet.close();
			// pstmt.close();

			if (duplicateCheck == true) {
				System.out.println("schoolWrapper already exists table " + schoolWrapper.code);

				dataArrayWrapper.errorWrapper = new ErrorWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.errorWrapper);
				schoolWrapper.recordFound = false;

				dataArrayWrapper.schoolWrapper = new SchoolWrapper[1];
				dataArrayWrapper.schoolWrapper[0] = schoolWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println(
						"Rejected " + schoolWrapper.schoolID + " -" + schoolWrapper.schoolName + " already exists");
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

	// --------------end create School Register data---------

	// --------------update School Wrapper Registration-------------

	public AbstractWrapper updateSchoolRegister(UsersWrapper usersProfileWrapper, SchoolWrapper schoolWrapper)
			throws Exception {

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

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		String sql = null;

		boolean duplicateCheck = false;

		try {
			con = getConnection();

			if (duplicateCheck == false) {
				// Check for email id duplicate during school register
				duplicateCheck = duplicateDataCheck(
						"SELECT Code FROM MST_School WHERE Email=? AND Code <> '"
								+ Utility.trim(usersProfileWrapper.schoolID) + "'",
						Utility.trim(Utility.removeSpaces(schoolWrapper.email)));
				if (duplicateCheck == true) {
					ErrorWrapper errorWrapper = new ErrorWrapper();
					errorWrapper.errorCode = "ERR001";
					errorWrapper.errorDesc = "Email ID " + Utility.trim(Utility.removeSpaces(schoolWrapper.email))
							+ " is not allowed, please assign new Email ID";
					vector.add(errorWrapper);
				}
			}

			if (duplicateCheck == false) {
				// Check for mobile number duplicate during school register
				duplicateCheck = duplicateDataCheck(
						"SELECT Code FROM MST_School WHERE Mobile=? AND Code <> '"
								+ Utility.trim(usersProfileWrapper.schoolID) + "'",
						Utility.trim(Utility.removeSpaces(schoolWrapper.mobile)));
				if (duplicateCheck == true) {
					ErrorWrapper errorWrapper = new ErrorWrapper();
					errorWrapper.errorCode = "ERR001";
					errorWrapper.errorDesc = "Mobile Number " + Utility.trim(Utility.removeSpaces(schoolWrapper.mobile))
							+ " is not allowed, please assign new mobile number";
					vector.add(errorWrapper);
				}
			}

			if (duplicateCheck == false) {

				System.out.println("schoolWrapper update table " + schoolWrapper.code);

				sql = "UPDATE MST_School SET Description=?,  SchoolName=?, Address1=?, Address2=?, Address3=?,"
						+ "City=?, State=?, Country=?, Email=?, Mobile=?, Phone=?, "
						+ "Pincode=?, District=?, ModifierID=?, ModifierDateTime=? WHERE Code=? and SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, Utility.trim(schoolWrapper.desc));
				pstmt.setString(2, Utility.trim(schoolWrapper.desc));
				pstmt.setString(3, Utility.trim(schoolWrapper.address1));
				pstmt.setString(4, Utility.trim(schoolWrapper.address2));
				pstmt.setString(5, Utility.trim(schoolWrapper.address3));
				pstmt.setString(6, Utility.trim(schoolWrapper.city));
				pstmt.setString(7, Utility.trim(schoolWrapper.state));
				pstmt.setString(8, Utility.trim(schoolWrapper.country));
				pstmt.setString(9, Utility.trim(schoolWrapper.email));
				pstmt.setString(10, Utility.trim(schoolWrapper.mobile));
				pstmt.setString(11, Utility.trim(schoolWrapper.phone));
				pstmt.setString(12, Utility.trim(schoolWrapper.pinCode));
				pstmt.setString(13, Utility.trim(schoolWrapper.district));
				pstmt.setString(14, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(15, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(16, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.setString(17, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.executeUpdate();
				pstmt.close();

				schoolWrapper.recordFound = true;

				dataArrayWrapper.schoolWrapper = new SchoolWrapper[1];
				dataArrayWrapper.schoolWrapper[0] = schoolWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully " + schoolWrapper.schoolName + " Updated");
			}

			if (duplicateCheck == true) {
				System.out.println("update schoolWrapper already exists table " + schoolWrapper.code);

				dataArrayWrapper.errorWrapper = new ErrorWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.errorWrapper);

				schoolWrapper.recordFound = false;
				dataArrayWrapper.schoolWrapper = new SchoolWrapper[1];
				dataArrayWrapper.schoolWrapper[0] = schoolWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println(
						"Rejected " + schoolWrapper.schoolID + " -" + schoolWrapper.schoolName + " already exists");
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

	// --------------end update School Registraiton data---------

	// --------------update School Wrapper Registration-------------

	public AbstractWrapper fetchSchoolRegister(UsersWrapper usersProfileWrapper, SchoolWrapper schoolWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;
		
		String sql = null;

		try {
			con = getConnection();

			sql = "SELECT Code,Description, SchoolID, SchoolName, Address1, Address2, Address3, "
					+ " City, State, Country, Email, Mobile, Phone, AdminID,"
					+ " Active, EmailVerified, MobileVerified, FirebaseID, Pincode, District,"
					+ " MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM MST_School ";
					
			if(Utility.isEmpty(usersProfileWrapper.schoolID)==false)
			{
				sql = sql +  " WHERE Code=? and SchoolID=?";
			}
			
			pstmt = con.prepareStatement(sql);

			if(Utility.isEmpty(usersProfileWrapper.schoolID)==false)
			{

				pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			}
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				schoolWrapper = new SchoolWrapper();

				schoolWrapper.code = Utility.trim(resultSet.getString("Code"));
				schoolWrapper.desc = Utility.trim(resultSet.getString("Description"));
				schoolWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				schoolWrapper.schoolName = Utility.trim(resultSet.getString("SchoolName"));
				schoolWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
				schoolWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
				schoolWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
				schoolWrapper.city = Utility.trim(resultSet.getString("City"));
				schoolWrapper.state = Utility.trim(resultSet.getString("State"));
				schoolWrapper.country = Utility.trim(resultSet.getString("Country"));
				schoolWrapper.email = Utility.trim(resultSet.getString("Email"));
				schoolWrapper.mobile = Utility.trim(resultSet.getString("Mobile"));
				schoolWrapper.phone = Utility.trim(resultSet.getString("Phone"));
				schoolWrapper.pinCode = Utility.trim(resultSet.getString("Pincode"));
				schoolWrapper.district = Utility.trim(resultSet.getString("District"));
				schoolWrapper.adminID = Utility.trim(resultSet.getString("AdminID"));
				schoolWrapper.active = Utility.trim(resultSet.getString("Active"));
				schoolWrapper.emailVerified = Utility.trim(resultSet.getString("EmailVerified"));
				schoolWrapper.mobileVerified = Utility.trim(resultSet.getString("MobileVerified"));
				schoolWrapper.firebaseID = Utility.trim(resultSet.getString("FirebaseID"));
				schoolWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				schoolWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				schoolWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				schoolWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));

				schoolWrapper.recordFound = true;

				System.out.println("schoolWrapper fetch successful");

				vector.addElement(schoolWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.schoolWrapper = new SchoolWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.schoolWrapper);
				dataArrayWrapper.recordFound = true;

			} else {
				dataArrayWrapper.schoolWrapper = new SchoolWrapper[1];
				dataArrayWrapper.schoolWrapper[0] = schoolWrapper;
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

	// --------------end fetch School Registraiton data---------

	// --------------create Parameter record for new school-------------

	public AbstractWrapper createSchoolParameter(SchoolWrapper schoolWrapper) throws Exception {

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
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		String sql = null;

		ParameterWrapper parameterWrapper = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT SchoolID FROM MST_Parameter WHERE SchoolID=?");
			pstmt.setString(1, Utility.trim(schoolWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {

				resultSet.close();
				pstmt.close();

				// get from BOOT
				pstmt = con.prepareStatement("SELECT RefNo,StaffRefNo,StudentID,SchoolCode,StaffCode,OtpExpLimit, "
						+ " OTPLength,QueueMaxRecords,CountryCode,CurrentAcademicYear,MessageID, "
						+ " ImagesPath,AbsentMessage,GCMKey,GCMActivate, "
						+ " GCMURL,InvoiceNo,InvoiceCode,DashboardImagePath, "
						+ " GradeAllCode,DefaultPassword,BirthdayMessage, " + " AESEncryptKey,AESSaltKey,SchoolName, "
						+ " EmailDomain,ReportsPath,SessionTimeOut,BookRefNo,BookCode,SchoolID, "
						+ " MakerID,MakerDateTime,ModifierID, ModifierDateTime "
						+ " FROM MST_Parameter WHERE SchoolID='" + Constants.bootSchool + "'");

				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					parameterWrapper = new ParameterWrapper();

					parameterWrapper.refNo = "0"; // Utility.trim(resultSet.getString("RefNo"));
					parameterWrapper.staffRefNo = "0"; // Utility.trim(resultSet.getString("StaffRefNo"));
					parameterWrapper.studentID = "0"; // Utility.trim(resultSet.getString("StudentID"));
					parameterWrapper.schoolCode = schoolWrapper.schoolID; // Utility.trim(resultSet.getString("SchoolCode"));
					parameterWrapper.staffCode = Utility.trim(resultSet.getString("StaffCode"));
					parameterWrapper.otpExpLimit = Utility.trim(resultSet.getString("OtpExpLimit"));
					parameterWrapper.otpLength = Utility.trim(resultSet.getString("OTPLength"));
					parameterWrapper.queueMaxRecords = Utility.trim(resultSet.getString("QueueMaxRecords"));
					parameterWrapper.countryCode = Utility.trim(resultSet.getString("CountryCode"));
					parameterWrapper.currentAcademicYear = Utility.trim(resultSet.getString("CurrentAcademicYear"));
					parameterWrapper.messageID = "0";// Utility.trim(resultSet.getString("MessageID"));
					parameterWrapper.imagesPath = Utility.trim(Utility.replaceWord(resultSet.getString("ImagesPath"),
							"rootschool", schoolWrapper.schoolID));
					parameterWrapper.absentMessage = Utility.trim(resultSet.getString("AbsentMessage"));
					parameterWrapper.gcmKey = Utility.trim(resultSet.getString("GCMKey"));
					parameterWrapper.gcmActivate = Utility.trim(resultSet.getString("GCMActivate"));
					parameterWrapper.gcmURL = Utility.trim(resultSet.getString("GCMURL"));
					parameterWrapper.invoiceNo = Utility.trim(resultSet.getString("InvoiceNo"));
					parameterWrapper.invoiceCode = Utility.trim(resultSet.getString("InvoiceCode"));
					parameterWrapper.dashboardImagePath = Utility.trim(Utility.replaceWord(
							resultSet.getString("DashboardImagePath"), "rootschool", schoolWrapper.schoolID));
					parameterWrapper.gradeAllCode = Utility.trim(resultSet.getString("GradeAllCode"));
					parameterWrapper.defaultPassword = Utility.trim(resultSet.getString("DefaultPassword"));
					parameterWrapper.birthdayMessage = Utility.trim(resultSet.getString("BirthdayMessage"));
					parameterWrapper.aesEncryptKey = Utility.trim(resultSet.getString("AESEncryptKey"));
					parameterWrapper.aesSaltKey = Utility.trim(resultSet.getString("AESSaltKey"));
					parameterWrapper.schoolName = schoolWrapper.schoolName; // Utility.trim(resultSet.getString("SchoolName"));
					parameterWrapper.emailDomain = Utility.trim(resultSet.getString("EmailDomain"));
					parameterWrapper.reportsPath = Utility.trim(Utility.replaceWord(resultSet.getString("ReportsPath"),
							"rootschool", schoolWrapper.schoolID));
					parameterWrapper.sessionTimeOut = Utility.trim(resultSet.getString("SessionTimeOut"));
					parameterWrapper.bookRefNo = Utility.trim(resultSet.getString("BookRefNo"));
					parameterWrapper.bookCode = Utility.trim(resultSet.getString("BookCode"));
					parameterWrapper.schoolID = schoolWrapper.schoolID; // Utility.trim(resultSet.getString("SchoolID"));

					resultSet.close();
					pstmt.close();

					sql = "INSERT INTO MST_Parameter (RefNo,StaffRefNo,StudentID,SchoolCode,StaffCode,OtpExpLimit,"
							+ " OTPLength,QueueMaxRecords,CountryCode,CurrentAcademicYear,MessageID,"
							+ " ImagesPath,AbsentMessage,GCMKey,GCMActivate,"
							+ " GCMURL,InvoiceNo,InvoiceCode,DashboardImagePath,"
							+ " GradeAllCode,DefaultPassword,BirthdayMessage," + " AESEncryptKey,AESSaltKey,SchoolName,"
							+ " EmailDomain,ReportsPath,SessionTimeOut,BookRefNo,BookCode,SchoolID,"
							+ " MakerID,MakerDateTime)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(parameterWrapper.refNo));
					pstmt.setString(2, Utility.trim(parameterWrapper.staffRefNo));
					pstmt.setString(3, Utility.trim(parameterWrapper.studentID));
					pstmt.setString(4, Utility.trim(parameterWrapper.schoolCode));
					pstmt.setString(5, Utility.trim(parameterWrapper.staffCode));
					pstmt.setString(6, Utility.trim(parameterWrapper.otpExpLimit));
					pstmt.setString(7, Utility.trim(parameterWrapper.otpLength));
					pstmt.setString(8, Utility.trim(parameterWrapper.queueMaxRecords));
					pstmt.setString(9, Utility.trim(parameterWrapper.countryCode));
					pstmt.setString(10, Utility.trim(parameterWrapper.currentAcademicYear));
					pstmt.setString(11, Utility.trim(parameterWrapper.messageID));
					pstmt.setString(12, Utility.trim(parameterWrapper.imagesPath));
					pstmt.setString(13, Utility.trim(parameterWrapper.absentMessage));
					pstmt.setString(14, Utility.trim(parameterWrapper.gcmKey));
					pstmt.setString(15, Utility.trim(parameterWrapper.gcmActivate));
					pstmt.setString(16, Utility.trim(parameterWrapper.gcmURL));
					pstmt.setString(17, Utility.trim(parameterWrapper.invoiceNo));
					pstmt.setString(18, Utility.trim(parameterWrapper.invoiceCode));
					pstmt.setString(19, Utility.trim(parameterWrapper.dashboardImagePath));
					pstmt.setString(20, Utility.trim(parameterWrapper.gradeAllCode));
					pstmt.setString(21, Utility.trim(parameterWrapper.defaultPassword));
					pstmt.setString(22, Utility.trim(parameterWrapper.birthdayMessage));
					pstmt.setString(23, Utility.trim(parameterWrapper.aesEncryptKey));
					pstmt.setString(24, Utility.trim(parameterWrapper.aesSaltKey));
					pstmt.setString(25, Utility.trim(parameterWrapper.schoolName));
					pstmt.setString(26, Utility.trim(parameterWrapper.emailDomain));
					pstmt.setString(27, Utility.trim(parameterWrapper.reportsPath));
					pstmt.setString(28, Utility.trim(parameterWrapper.sessionTimeOut));
					pstmt.setString(29, Utility.trim(parameterWrapper.bookRefNo));
					pstmt.setString(30, Utility.trim(parameterWrapper.bookCode));
					pstmt.setString(31, Utility.trim(parameterWrapper.schoolID));
					pstmt.setString(32, Utility.trim(schoolWrapper.adminID));
					pstmt.setTimestamp(33, Utility.getCurrentTime()); // maker date time

					pstmt.executeUpdate();
					pstmt.close();

					parameterWrapper.recordFound = true;

					dataArrayWrapper.parameterWrapper = new ParameterWrapper[1];
					dataArrayWrapper.parameterWrapper[0] = parameterWrapper;

					dataArrayWrapper.recordFound = true;

					System.out.println("MIT_Parameter Successfully " + schoolWrapper.schoolID + " -"
							+ schoolWrapper.schoolName + " inserted");

				} else {

					System.out.println("MST_Parameter creation failed " + schoolWrapper.schoolID);

				}
			}
			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

			// end of check in MST_Parameter

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

	// --------------end create School Register data---------

	public AbstractWrapper fetchParameters(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		// String sql=null;

		ParameterWrapper parameterWrapper = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT RefNo,StaffRefNo,StudentID,SchoolCode,StaffCode,OtpExpLimit, "
					+ " OTPLength,QueueMaxRecords,CountryCode,CurrentAcademicYear,MessageID, "
					+ " ImagesPath,AbsentMessage,GCMKey,GCMActivate, "
					+ " GCMURL,InvoiceNo,InvoiceCode,DashboardImagePath, "
					+ " GradeAllCode,DefaultPassword,BirthdayMessage, " + " AESEncryptKey,AESSaltKey,SchoolName, "
					+ " EmailDomain,ReportsPath,SessionTimeOut,BookRefNo,BookCode,SchoolID, "
					+ " MakerID,MakerDateTime,ModifierID, ModifierDateTime " + " FROM MST_Parameter WHERE SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				parameterWrapper = new ParameterWrapper();

				parameterWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				parameterWrapper.staffRefNo = Utility.trim(resultSet.getString("StaffRefNo"));
				parameterWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				parameterWrapper.schoolCode = Utility.trim(resultSet.getString("SchoolCode"));
				parameterWrapper.staffCode = Utility.trim(resultSet.getString("StaffCode"));
				parameterWrapper.otpExpLimit = Utility.trim(resultSet.getString("OtpExpLimit"));
				parameterWrapper.otpLength = Utility.trim(resultSet.getString("OTPLength"));
				parameterWrapper.queueMaxRecords = Utility.trim(resultSet.getString("QueueMaxRecords"));
				parameterWrapper.countryCode = Utility.trim(resultSet.getString("CountryCode"));
				parameterWrapper.currentAcademicYear = Utility.trim(resultSet.getString("CurrentAcademicYear"));
				parameterWrapper.messageID = Utility.trim(resultSet.getString("MessageID"));
				parameterWrapper.imagesPath = Utility.trim(resultSet.getString("ImagesPath"));
				parameterWrapper.absentMessage = Utility.trim(resultSet.getString("AbsentMessage"));
				parameterWrapper.gcmKey = Utility.trim(resultSet.getString("GCMKey"));
				parameterWrapper.gcmActivate = Utility.trim(resultSet.getString("GCMActivate"));
				parameterWrapper.gcmURL = Utility.trim(resultSet.getString("GCMURL"));
				parameterWrapper.invoiceNo = Utility.trim(resultSet.getString("InvoiceNo"));
				parameterWrapper.invoiceCode = Utility.trim(resultSet.getString("InvoiceCode"));
				parameterWrapper.dashboardImagePath = Utility.trim(resultSet.getString("DashboardImagePath"));
				parameterWrapper.gradeAllCode = Utility.trim(resultSet.getString("GradeAllCode"));
				parameterWrapper.defaultPassword = Utility.trim(resultSet.getString("DefaultPassword"));
				parameterWrapper.birthdayMessage = Utility.trim(resultSet.getString("BirthdayMessage"));
				parameterWrapper.aesEncryptKey = Utility.trim(resultSet.getString("AESEncryptKey"));
				parameterWrapper.aesSaltKey = Utility.trim(resultSet.getString("AESSaltKey"));
				parameterWrapper.schoolName = Utility.trim(resultSet.getString("SchoolName"));
				parameterWrapper.emailDomain = Utility.trim(resultSet.getString("EmailDomain"));
				parameterWrapper.reportsPath = Utility.trim(resultSet.getString("ReportsPath"));
				parameterWrapper.sessionTimeOut = Utility.trim(resultSet.getString("SessionTimeOut"));
				parameterWrapper.bookRefNo = Utility.trim(resultSet.getString("BookRefNo"));
				parameterWrapper.bookCode = Utility.trim(resultSet.getString("BookCode"));
				parameterWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				resultSet.close();
				pstmt.close();

				parameterWrapper.recordFound = true;

			}
			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

			// end of check in MST_Parameter

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

		return parameterWrapper;
	}

	/// --------------

	public AbstractWrapper updateParameters(UsersWrapper usersProfileWrapper, ParameterWrapper parameterWrapper)
			throws Exception {

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
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		// String sql=null;

		try {
			con = getConnection();

			AES128Crypto aes128Crypto = new AES128Crypto();

			pstmt = con.prepareStatement("UPDATE MST_Parameter SET DefaultPassword=?, ModifierID=?, ModifierDateTime=? "
					+ " WHERE SchoolID=?");

			pstmt.setString(1, aes128Crypto.md5DB(parameterWrapper.defaultPassword, usersProfileWrapper.schoolID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(3, Utility.getCurrentTime());
			pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Change password details updated ");

			parameterWrapper.recordFound = true;
			dataArrayWrapper.parameterWrapper = new ParameterWrapper[1];
			dataArrayWrapper.parameterWrapper[0] = parameterWrapper;

			dataArrayWrapper.recordFound = true;

			// end of check in MST_Parameter

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

	/// --------------

	// --------------create User menu record for new school-------------

	public AbstractWrapper createUserMenu(SchoolWrapper schoolWrapper) throws Exception {

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
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		String sql = null;

		UserMenuWrapper userMenuWrapper = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT SchoolID FROM MST_UserMenu WHERE SchoolID=? and Userid=?");
			pstmt.setString(1, Utility.trim(schoolWrapper.schoolID));
			pstmt.setString(2, Utility.trim(schoolWrapper.adminID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {

				resultSet.close();
				pstmt.close();

				// get from BOOT
				pstmt = con.prepareStatement("SELECT Userid,MenuID,SchoolID FROM MST_UserMenu WHERE SchoolID='"
						+ Constants.bootSchool + "'");

				resultSet = pstmt.executeQuery();
				while (resultSet.next()) {

					userMenuWrapper = new UserMenuWrapper();

					userMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));

					sql = "INSERT INTO MST_UserMenu (Userid, MenuID, SchoolID," + " MakerID,MakerDateTime)"
							+ " VALUES(?,?,?,?,?)";

					PreparedStatement pstmt1 = con.prepareStatement(sql);

					pstmt1.setString(1, schoolWrapper.adminID);
					pstmt1.setString(2, Utility.trim(userMenuWrapper.menuID));
					pstmt1.setString(3, Utility.trim(schoolWrapper.schoolID));
					pstmt1.setString(4, Utility.trim(schoolWrapper.adminID));
					pstmt1.setTimestamp(5, Utility.getCurrentTime()); // maker date time

					pstmt1.executeUpdate();
					pstmt1.close();

					userMenuWrapper.recordFound = true;

					// dataArrayWrapper.parameterWrapper=new ParameterWrapper[1];
					// dataArrayWrapper.parameterWrapper[0]=parameterWrapper;
					//
					// dataArrayWrapper.recordFound=true;
					//
					System.out.println("MST_UserMenu Successfully " + schoolWrapper.schoolID + " -"
							+ schoolWrapper.schoolName + " inserted");

				}

				resultSet.close();
				pstmt.close();

				// else
				// {
				//
				// System.out.println("MST_UserMenu creation failed "+schoolWrapper.schoolID);
				//
				// }
			}
			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

			// end of check in MST_Parameter

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

	// --------------end create usermenu Register data---------

	// --------------create MasterTable Names record for new school-------------

	public AbstractWrapper createMasterTableNames(SchoolWrapper schoolWrapper) throws Exception {

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
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		String sql = null;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT SchoolID FROM MST_TableNames WHERE SchoolID=?");
			pstmt.setString(1, Utility.trim(schoolWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {

				resultSet.close();
				pstmt.close();

				// get from BOOT
				pstmt = con.prepareStatement(
						"INSERT INTO MST_TableNames (Code,Description, Filter,SchoolID, MakerID, MakerDateTime) SELECT Code,Description,Filter,?,?,? FROM MST_TableNames WHERE SchoolID='"
								+ Constants.bootSchool + "'");

				pstmt.setString(1, Utility.trim(schoolWrapper.schoolID));
				pstmt.setString(2, Utility.trim(schoolWrapper.adminID));
				pstmt.setTimestamp(3, Utility.getCurrentTime()); // maker date time
				pstmt.executeUpdate();
				pstmt.close();

				System.out.println("MST_TableNames Successfully " + schoolWrapper.schoolID + " -"
						+ schoolWrapper.schoolName + " inserted");

			}

			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

			// end of check in MST_TableNames

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

	// --------------end create usermenu Register data---------

	// --------------create Master Data from BOOT records for new
	// school-------------

	public AbstractWrapper createMasterTableData(SchoolWrapper schoolWrapper) throws Exception {

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
		// Vector<Object> vector = new Vector<Object>();

		PreparedStatement pstmt = null;

		// String sql=null;

		try {

			String[] tableNames = { "MST_AcademicYear", "MST_Bank", "MST_BloodGroup", "MST_Branch",
					"MST_BusPickupPoint", "MST_BusRoute", "MST_CardIssuer", "MST_Caste", "MST_CasteCategory",
					"MST_City", "MST_ClassTeacher", "MST_DataFileTemplate", "MST_Decision", "MST_District",
					"MST_DocChecklistMaster", "MST_Education", "MST_ExamStatus", "MST_FeeType", "MST_Gender",
					"MST_Grade", "MST_Menu", "MST_Months", "MST_Occupation", "MST_Rank", "MST_Religion", "MST_Reports",
					"MST_Section", "MST_State", "MST_Subject", "MST_Term" };

			con = getConnection();

			for (int i = 0; i <= tableNames.length - 1; i++) {

				System.out.println(
						"master table data before insert date " + schoolWrapper.schoolID + " - " + tableNames[i]);

				// get from BOOT
				pstmt = con.prepareStatement("INSERT INTO  " + tableNames[i]
						+ "(Code,Description,SchoolID, MakerID, MakerDateTime) SELECT Code,Description,?,?,? FROM "
						+ tableNames[i] + " WHERE SchoolID='" + Constants.bootSchool + "'");

				pstmt.setString(1, Utility.trim(schoolWrapper.schoolID));
				pstmt.setString(2, Utility.trim(schoolWrapper.adminID));
				pstmt.setTimestamp(3, Utility.getCurrentTime()); // maker date time
				pstmt.executeUpdate();
				pstmt.close();
				// --------------
			}

			System.out.println("createMasterTableData Successfully " + schoolWrapper.schoolID + " -"
					+ schoolWrapper.schoolName + " inserted");

			// end of check in MST_TableNames

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

	// --------------end create usermenu Register data---------

	// -------duplicate data check for school register
	public boolean duplicateDataCheck(String SQL, String whereValue) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		PreparedStatement pstmt = null;

		boolean result = false;

		try {
			con = getConnection();

			pstmt = con.prepareStatement(SQL);

			pstmt.setString(1, Utility.trim(Utility.removeSpaces(whereValue)));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				result = true;
				System.out.println("Rejected " + SQL + " " + whereValue + " already exists");
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
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return result;
	}

	// --------------end duplicate data check data---------

}
