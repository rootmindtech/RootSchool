package com.rootmind.helper;

import java.io.File;
import java.io.IOException;
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
import com.rootmind.wrapper.ImageDetailsWrapper;
import com.rootmind.wrapper.TeachersProfileWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TeachersProfileHelper extends Helper {

	// -----------------Start insertTeachersProfile---------------------

	public AbstractWrapper insertTeachersProfile(UsersWrapper usersProfileWrapper,
			TeachersProfileWrapper teachersProfileWrapper) throws Exception {

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

			// ----------create userid with schoolid userid@schoolid
			usersProfileWrapper.schoolID = Utility.trim(Utility.removeSpaces(usersProfileWrapper.schoolID));
			teachersProfileWrapper.staffUserID = Utility.trim(Utility.removeSpaces(teachersProfileWrapper.staffUserID));
			String staffUserID = teachersProfileWrapper.staffUserID;// this is used to send to create user profile
																	// without schoold id
			teachersProfileWrapper.staffUserID = teachersProfileWrapper.staffUserID + "@"
					+ usersProfileWrapper.schoolID;
			// -------------

			sql = " INSERT INTO TeachersProfile(Userid,Code,Description,StaffRefNo,Header,ImageID, DocID, Name, "
					+ "Profile, MobileNo, EmailID, OrderNumber, Status, MakerID, MakerDateTime, SchoolID, StaffType)"
					+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			teachersProfileWrapper.status = "ACTIVE";

			pstmt.setString(1, Utility.trim(Utility.removeSpaces(teachersProfileWrapper.staffUserID)));
			pstmt.setString(2, Utility.trim(Utility.removeSpaces(teachersProfileWrapper.staffUserID)));
			pstmt.setString(3, Utility.trim(teachersProfileWrapper.name));

			teachersProfileWrapper.staffRefNo = generateStaffRefNo(usersProfileWrapper);

			pstmt.setString(4, teachersProfileWrapper.staffRefNo); // ref no for staff

			pstmt.setString(5, Utility.trim(teachersProfileWrapper.header));
			pstmt.setString(6, Utility.trim(teachersProfileWrapper.imageID));
			pstmt.setString(7, Utility.trim(teachersProfileWrapper.docID));
			pstmt.setString(8, Utility.trim(teachersProfileWrapper.name));
			pstmt.setString(9, Utility.trim(teachersProfileWrapper.profile));
			pstmt.setString(10, Utility.trim(teachersProfileWrapper.mobileNo));
			pstmt.setString(11, Utility.trim(teachersProfileWrapper.emailID));
			pstmt.setString(12, Utility.trim(teachersProfileWrapper.orderNumber));
			pstmt.setString(13, Utility.trim(teachersProfileWrapper.status));
			pstmt.setString(14, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(15, Utility.getCurrentTime()); // maker date time
			pstmt.setString(16, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(17, Utility.trim(teachersProfileWrapper.staffType));

			pstmt.executeUpdate();
			pstmt.close();

			teachersProfileWrapper.recordFound = true;

			dataArrayWrapper.teachersProfileWrapper = new TeachersProfileWrapper[1];
			dataArrayWrapper.teachersProfileWrapper[0] = teachersProfileWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into TeachersProfile");

			// ------Create Login Profile
			UsersHelper usersHelper = new UsersHelper();
			UsersWrapper usersWrapper = new UsersWrapper();
			usersWrapper.userid = staffUserID; // school id will be added in the updateLoginProfile, hence not adding
												// here.
			usersWrapper.refNo = teachersProfileWrapper.staffRefNo;
			usersWrapper.schoolID = usersProfileWrapper.schoolID;

			usersWrapper.password = teachersProfileWrapper.password; // this is introduced for login
			usersWrapper.email = teachersProfileWrapper.email; // this is introduced for login

			usersHelper.updateLoginProfile(usersProfileWrapper, usersWrapper, Utility.staff_type);
			// ------

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

	// -----------------End insertTeachersProfile---------------------

	// -----------------Start updateTeachersProfile---------------------
	public AbstractWrapper updateTeachersProfile(UsersWrapper usersProfileWrapper,
			TeachersProfileWrapper teachersProfileWrapper) throws Exception {

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

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT StaffRefNo FROM TeachersProfile WHERE StaffRefNo=? and SchoolID=?");

			System.out.println("Teachers Profile StaffRefNo is " + teachersProfileWrapper.staffRefNo);

			pstmt.setString(1, teachersProfileWrapper.staffRefNo); // --it may null
			pstmt.setString(2, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertTeachersProfile(usersProfileWrapper,
						teachersProfileWrapper);
			} else {
				resultSet.close();
				pstmt.close();
				pstmt = con.prepareStatement(
						"UPDATE TeachersProfile SET Description=?,Header=?, ImageID=?, DocID=?, Name=?, Profile=?,MobileNo=?, EmailID=?, OrderNumber=?, Status=?,"
								+ " ModifierID=?, ModifierDateTime=?, StaffType=? WHERE StaffRefNo=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(teachersProfileWrapper.name));
				pstmt.setString(2, Utility.trim(teachersProfileWrapper.header));
				pstmt.setString(3, Utility.trim(teachersProfileWrapper.imageID));
				pstmt.setString(4, Utility.trim(teachersProfileWrapper.docID));
				pstmt.setString(5, Utility.trim(teachersProfileWrapper.name));
				pstmt.setString(6, Utility.trim(teachersProfileWrapper.profile));
				pstmt.setString(7, Utility.trim(teachersProfileWrapper.mobileNo));
				pstmt.setString(8, Utility.trim(teachersProfileWrapper.emailID));
				pstmt.setString(9, Utility.trim(teachersProfileWrapper.orderNumber));
				pstmt.setString(10, Utility.trim(teachersProfileWrapper.status));
				pstmt.setString(11, Utility.trim(teachersProfileWrapper.modifierID));
				pstmt.setTimestamp(12, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(13, Utility.trim(teachersProfileWrapper.staffType));
				pstmt.setString(14, Utility.trim(teachersProfileWrapper.staffRefNo));
				pstmt.setString(15, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				teachersProfileWrapper.recordFound = true;
				dataArrayWrapper.teachersProfileWrapper = new TeachersProfileWrapper[1];
				dataArrayWrapper.teachersProfileWrapper[0] = teachersProfileWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully TeachersProfile Updated");
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
	// -----------------End updateTeachersProfile---------------------

	// -----------------Start fetchTeachersProfile---------------------

	public AbstractWrapper fetchTeachersProfile(UsersWrapper usersProfileWrapper,
			TeachersProfileWrapper teachersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		String sql = "";

		try {

			con = getConnection();
			PopoverHelper popoverHelper = new PopoverHelper();

			sql = "SELECT Userid,Code,Description,StaffRefNo, Header, ImageID, DocID, Name, Profile, MobileNo, "
					+ " EmailID, OrderNumber, Status, MakerID, MakerDateTime,ModifierID, ModifierDateTime, SchoolID, StaffType "
					+ " FROM TeachersProfile WHERE SchoolID=? ";

			if (!Utility.isEmpty(teachersProfileWrapper.staffRefNo)) {
				sql = sql + " and StaffRefNo=? ";
			}

			sql = sql + " Order By OrderNumber ASC";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(teachersProfileWrapper.staffRefNo)) {
				pstmt.setString(2, teachersProfileWrapper.staffRefNo);

			}
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				teachersProfileWrapper = new TeachersProfileWrapper();

				teachersProfileWrapper.staffUserID = Utility.trim(resultSet.getString("Userid"));
				teachersProfileWrapper.code = Utility.trim(resultSet.getString("Code"));
				teachersProfileWrapper.description = Utility.trim(resultSet.getString("Description"));
				teachersProfileWrapper.staffRefNo = Utility.trim(resultSet.getString("StaffRefNo"));
				teachersProfileWrapper.header = Utility.trim(resultSet.getString("Header"));
				teachersProfileWrapper.imageID = Utility.trim(resultSet.getString("ImageID"));
				teachersProfileWrapper.docID = Utility.trim(resultSet.getString("DocID"));

				teachersProfileWrapper.name = Utility.trim(resultSet.getString("Name"));
				teachersProfileWrapper.profile = Utility.trim(resultSet.getString("Profile"));
				teachersProfileWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
				teachersProfileWrapper.emailID = Utility.trim(resultSet.getString("EmailID"));
				teachersProfileWrapper.orderNumber = Utility.trim(resultSet.getString("OrderNumber"));
				teachersProfileWrapper.status = Utility.trim(resultSet.getString("Status"));

				teachersProfileWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				teachersProfileWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				teachersProfileWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				teachersProfileWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));

				teachersProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				teachersProfileWrapper.staffType = Utility.trim(resultSet.getString("StaffType"));

				teachersProfileWrapper.staffTypeValue = popoverHelper.fetchPopoverDesc(teachersProfileWrapper.staffType,
						"MST_StaffType", usersProfileWrapper.schoolID);

				teachersProfileWrapper.recordFound = true;

				System.out.println("TeachersProfile fetch successful");

				vector.addElement(teachersProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.teachersProfileWrapper = new TeachersProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.teachersProfileWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			if (resultSet != null)
				resultSet.close();
			pstmt.close();

			if (dataArrayWrapper.teachersProfileWrapper != null) {
				dataArrayWrapper = (DataArrayWrapper) fetchStaffImage(usersProfileWrapper, dataArrayWrapper);
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
	// -----------------End fetchTeachersProfile---------------------

	// -----------------Generate Staff RefNo-------------------------------
	public String generateStaffRefNo(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		int staffRefNo = 0;
		String finalStaffRefNo = null;
		String staffCode = null;

		try {

			con = getConnection();

			sql = "SELECT StaffRefNo, StaffCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				staffRefNo = resultSet.getInt("StaffRefNo");
				System.out.println("StaffRefNo " + staffRefNo);
				staffCode = resultSet.getString("StaffCode");

			}

			resultSet.close();
			pstmt.close();

			if (staffRefNo == 0) {
				staffRefNo = 1;

			} else {

				staffRefNo = staffRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set StaffRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, staffRefNo);
			pstmt.setString(2, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 4;

			// finalStaffRefNo=staffCode+dmyFormat.format(new
			// java.util.Date()).toUpperCase()+String.format("%0" + paddingSize
			// +"d",staffRefNo);

			finalStaffRefNo = staffCode.toUpperCase() + String.format("%0" + paddingSize + "d", staffRefNo);

			System.out.println("Successfully generated StaffRefNo " + finalStaffRefNo);

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

		return finalStaffRefNo;
	}

	// -----------------End Generate Staff RefNo---------------------------

	// ----------------fetch student image for search
	public AbstractWrapper fetchStaffImage(UsersWrapper usersProfileWrapper, DataArrayWrapper dataArrayWrapper)
			throws Exception {

		try {

			ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

			for (int i = 0; i <= dataArrayWrapper.teachersProfileWrapper.length - 1; i++) {

				ImageDetailsWrapper imageDetailsWrapper = new ImageDetailsWrapper();
				// imageDetailsWrapper.refNo = dataArrayWrapper.studentProfileWrapper[i].refNo;
				imageDetailsWrapper.studentID = dataArrayWrapper.teachersProfileWrapper[i].staffRefNo;
				imageDetailsWrapper.docID = "DOC004";

				DataArrayWrapper imgDataArrayWrapper = (DataArrayWrapper) imageDetailsHelper
						.fetchImageDetails(usersProfileWrapper, imageDetailsWrapper);

				if (imgDataArrayWrapper.recordFound == true && imgDataArrayWrapper.imageDetailsWrapper.length > 0) {

					imageDetailsWrapper = imgDataArrayWrapper.imageDetailsWrapper[0];

					System.out.println("file path to fetch " + imageDetailsWrapper.imageFileFolder + File.separator
							+ imageDetailsWrapper.imageFileName);

					dataArrayWrapper.teachersProfileWrapper[i].avatar = imageDetailsWrapper.imageFileFolder;

					System.out.println("teachersProfileWrapper Fetch Image success in servlet ");

				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

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

		return dataArrayWrapper;
	}

	// ----------------fetch student image for search
	public String fetchStaffImage(UsersWrapper usersProfileWrapper, String staffRefNo) throws Exception {

		String avatar = null;
		try {

			ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

			ImageDetailsWrapper imageDetailsWrapper = new ImageDetailsWrapper();
			imageDetailsWrapper.studentID = staffRefNo;
			imageDetailsWrapper.docID = "DOC004";

			DataArrayWrapper imgDataArrayWrapper = (DataArrayWrapper) imageDetailsHelper
					.fetchImageDetails(usersProfileWrapper, imageDetailsWrapper);

			if (imgDataArrayWrapper.recordFound == true && imgDataArrayWrapper.imageDetailsWrapper.length > 0) {

				imageDetailsWrapper = imgDataArrayWrapper.imageDetailsWrapper[0];

				System.out.println("file path to fetch " + imageDetailsWrapper.imageFileFolder + File.separator
						+ imageDetailsWrapper.imageFileName);

				avatar = imageDetailsWrapper.imageFileFolder;

				System.out.println("fetchStaffImage");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

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

		return avatar;
	}

	// ----------end of fetch image details

	// ----------------- Checking userid exist or not---------------
	// public boolean fetchUserid(String userid, String schoolID)throws Exception {
	//
	// Connection con = null;
	// ResultSet resultSet = null;
	// boolean useridFound=false;
	//
	// try {
	//
	//
	// //----------create userid with schoolid userid@schoolid
	// schoolID = Utility.trim(Utility.removeSpaces(schoolID));
	// userid = Utility.trim(Utility.removeSpaces(userid));
	// userid = userid+"@"+schoolID;
	// //-------------
	//
	// con = getConnection();
	//
	// PreparedStatement pstmt = con.prepareStatement("SELECT Userid FROM Users
	// WHERE Userid=? and SchoolID=?");
	//
	// pstmt.setString(1,userid);
	// pstmt.setString(2,schoolID);
	//
	// resultSet = pstmt.executeQuery();
	//
	// if(resultSet.next())
	// {
	// useridFound=true;
	// }
	//
	// resultSet.close();
	// pstmt.close();
	//
	// } catch (SQLException se) {
	//
	// se.printStackTrace();
	// throw new SQLException(se.getSQLState()+ " ; "+ se.getMessage());
	//
	// } catch (NamingException ne) {
	//
	// ne.printStackTrace();
	// throw new NamingException(ne.getMessage());
	// }
	// catch (Exception ex) {
	//
	// ex.printStackTrace();
	// throw new Exception(ex.getMessage());
	// }
	//
	// finally
	// {
	// try
	// {
	// releaseConnection(resultSet, con);
	// }
	// catch (SQLException se)
	// {
	// se.printStackTrace();
	// throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
	// }
	// }
	//
	// return useridFound;
	// }

	// -----------------End Checking userid exist or not---------------

}
