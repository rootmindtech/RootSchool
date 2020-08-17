package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.UserMenuWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class UserMenuHelper extends Helper {

	public AbstractWrapper fetchUserMenu(UsersWrapper usersProfileWrapper, UserMenuWrapper userMenuWrapper)
			throws Exception {

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

		Vector<Object> vector = new Vector<Object>();

		String sql = null;
		int n = 1;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			sql = "SELECT Userid, MenuID, SchoolID, MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM MST_UserMenu WHERE SchoolID=?";

			if (Utility.isEmpty(userMenuWrapper.userid) == false) {
				sql = sql + " AND Userid=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(n, Utility.trim(usersProfileWrapper.schoolID));

			if (Utility.isEmpty(userMenuWrapper.userid) == false) {
				pstmt.setString(++n, Utility.trim(userMenuWrapper.userid));
			}

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				userMenuWrapper = new UserMenuWrapper();

				userMenuWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				// System.out.println("Userid" + userMenuWrapper.userid);

				userMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));
				// System.out.println("MenuID" + userMenuWrapper.menuID);

				userMenuWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				userMenuWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				userMenuWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				userMenuWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				userMenuWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));

				userMenuWrapper.menuIDValue = popoverHelper.fetchPopoverDesc(userMenuWrapper.menuID, "MST_Menu",
						usersProfileWrapper.schoolID);

				userMenuWrapper.recordFound = true;

				vector.addElement(userMenuWrapper);

			}
			if (vector.size() > 0) {
				dataArrayWrapper.userMenuWrapper = new UserMenuWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.userMenuWrapper);
				dataArrayWrapper.recordFound = true;

				// System.out.println("total trn. in fetch " + vector.size());

				// System.out.println("Fetch UserMenu Details Successful" );
			} else {
				dataArrayWrapper.userMenuWrapper = new UserMenuWrapper[1];
				dataArrayWrapper.userMenuWrapper[0] = userMenuWrapper;
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

	public AbstractWrapper updateUserMenu(UsersWrapper usersProfileWrapper, UserMenuWrapper userMenuWrapper)
			throws Exception {

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
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			sql = "SELECT UserID FROM MST_UserMenu WHERE UserID=? AND MenuID=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(userMenuWrapper.userid));
			pstmt.setString(2, Utility.trim(userMenuWrapper.menuID));
			pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();

				if (!userMenuWrapper.deleteFlag.equals("Y")) {

					sql = " INSERT INTO MST_UserMenu( UserID, MenuID,MakerID, MakerDateTime, SchoolID)  "
							+ "Values (?,?,?,?,?)";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(userMenuWrapper.userid));
					pstmt.setString(2, Utility.trim(userMenuWrapper.menuID));
					pstmt.setString(3, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(4, Utility.getCurrentTime()); // maker date time
					pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

					System.out.println("usermenu Userid " + userMenuWrapper.userid);

					pstmt.executeUpdate();
					pstmt.close();

					userMenuWrapper.recordFound = true;

					dataArrayWrapper.userMenuWrapper = new UserMenuWrapper[1];
					dataArrayWrapper.userMenuWrapper[0] = userMenuWrapper;

					dataArrayWrapper.recordFound = true;
				} else {
					dataArrayWrapper.userMenuWrapper = new UserMenuWrapper[1];
					dataArrayWrapper.userMenuWrapper[0] = userMenuWrapper;
					dataArrayWrapper.recordFound = true;

				}

			} else {
				resultSet.close();
				pstmt.close();

				if (userMenuWrapper.deleteFlag.equals("Y")) {

					pstmt = con.prepareStatement("DELETE FROM MST_UserMenu WHERE UserID=? AND MenuID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(userMenuWrapper.userid));
					pstmt.setString(2, Utility.trim(userMenuWrapper.menuID));
					pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

				} else {

					if (resultSet != null)
						resultSet.close();

					if (pstmt != null)
						pstmt.close();

					pstmt = con.prepareStatement("UPDATE MST_UserMenu SET  "
							+ "ModifierID=?, ModifierDateTime=? WHERE UserID=? AND MenuID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(2, Utility.getCurrentTime()); // modifier date time

					pstmt.setString(3, Utility.trim(userMenuWrapper.userid));
					pstmt.setString(4, Utility.trim(userMenuWrapper.menuID));
					pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					userMenuWrapper.recordFound = true;
				}

				userMenuWrapper.recordFound = true;
				dataArrayWrapper.userMenuWrapper = new UserMenuWrapper[1];
				dataArrayWrapper.userMenuWrapper[0] = userMenuWrapper;

				dataArrayWrapper.recordFound = true;

				System.out.println(" User Menu already existed");
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

}
