package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.GroupMenuWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class GroupMenuHelper extends Helper {

	
	public AbstractWrapper fetchGroupMenu(UsersWrapper usersProfileWrapper, GroupMenuWrapper groupMenuWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		String sql = null;
		int n = 1;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			sql = "SELECT GroupID, MenuID, Assigned, SchoolID, MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM MST_GroupMenu WHERE SchoolID=?";

			if (!Utility.isEmpty(groupMenuWrapper.groupID)) {
				sql = sql + " AND GroupID=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(n, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(groupMenuWrapper.groupID)) {
				pstmt.setString(++n, Utility.trim(groupMenuWrapper.groupID));
			}

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				groupMenuWrapper = new GroupMenuWrapper();

				groupMenuWrapper.groupID = Utility.trim(resultSet.getString("GroupID"));
				// System.out.println("Userid" + groupMenuWrapper.userid);

				groupMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));
				
				groupMenuWrapper.assignFlag = Utility.trim(resultSet.getString("Assigned"));

				// System.out.println("MenuID" + groupMenuWrapper.menuID);

				groupMenuWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				groupMenuWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				groupMenuWrapper.makerDateTime = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));
				groupMenuWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				groupMenuWrapper.modifierDateTime = Utility.setDateAMPM(resultSet.getString("ModifierDateTime"));

				groupMenuWrapper.menuIDValue = popoverHelper.fetchPopoverDesc(groupMenuWrapper.menuID, "MST_Menu",
						usersProfileWrapper.schoolID);
				groupMenuWrapper.groupIDValue = popoverHelper.fetchPopoverDesc(groupMenuWrapper.groupID, "MST_UserGroup",
						usersProfileWrapper.schoolID);

				groupMenuWrapper.recordFound = true;

				vector.addElement(groupMenuWrapper);

			}
			if (vector.size() > 0) {
				dataArrayWrapper.groupMenuWrapper = new GroupMenuWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.groupMenuWrapper);
				dataArrayWrapper.recordFound = true;

				// System.out.println("total trn. in fetch " + vector.size());

				// System.out.println("Fetch UserMenu Details Successful" );
			} else {
				dataArrayWrapper.groupMenuWrapper = new GroupMenuWrapper[1];
				dataArrayWrapper.groupMenuWrapper[0] = groupMenuWrapper;
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

	public AbstractWrapper updateGroupMenu(UsersWrapper usersProfileWrapper, GroupMenuWrapper groupMenuWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;
		String sql = null;
		// String currentAcademicYear=null;

		try {
			con = getConnection();

			sql = "SELECT GroupID FROM MST_GroupMenu WHERE GroupID=? AND MenuID=? and SchoolID=?";
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(groupMenuWrapper.groupID));
			pstmt.setString(2, Utility.trim(groupMenuWrapper.menuID));
			pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();

				if (!groupMenuWrapper.deleteFlag.equals("Y")) {

					sql = " INSERT INTO MST_GroupMenu( GroupID, MenuID, Assigned, MakerID, MakerDateTime, SchoolID)  "
							+ "Values (?,?,?,?,?,?)";

					System.out.println("sql " + sql);

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(groupMenuWrapper.groupID));
					pstmt.setString(2, Utility.trim(groupMenuWrapper.menuID));
					pstmt.setString(3, Utility.trim(groupMenuWrapper.assignFlag));
					pstmt.setString(4, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(5, Utility.getCurrentTime()); // maker date time
					pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));

					System.out.println("groupmenu groupid  " + groupMenuWrapper.groupID);

					pstmt.executeUpdate();
					pstmt.close();

					groupMenuWrapper.recordFound = true;

					dataArrayWrapper.groupMenuWrapper = new GroupMenuWrapper[1];
					dataArrayWrapper.groupMenuWrapper[0] = groupMenuWrapper;

					dataArrayWrapper.recordFound = true;
				} else {
					dataArrayWrapper.groupMenuWrapper = new GroupMenuWrapper[1];
					dataArrayWrapper.groupMenuWrapper[0] = groupMenuWrapper;
					dataArrayWrapper.recordFound = true;

				}

			} else {
				resultSet.close();
				pstmt.close();

				if (groupMenuWrapper.deleteFlag.equals("Y")) {

					pstmt = con.prepareStatement("DELETE FROM MST_GroupMenu WHERE GroupID=? AND MenuID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(groupMenuWrapper.groupID));
					pstmt.setString(2, Utility.trim(groupMenuWrapper.menuID));
					pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

				} else {

					if (resultSet != null)
						resultSet.close();

					if (pstmt != null)
						pstmt.close();

					pstmt = con.prepareStatement("UPDATE MST_GroupMenu SET Assigned=?, "
							+ "ModifierID=?, ModifierDateTime=? WHERE GroupID=? AND MenuID=? and SchoolID=?");

					pstmt.setString(1, Utility.trim(groupMenuWrapper.assignFlag));
					pstmt.setString(2, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(3, Utility.getCurrentTime()); // modifier date time

					pstmt.setString(4, Utility.trim(groupMenuWrapper.groupID));
					pstmt.setString(5, Utility.trim(groupMenuWrapper.menuID));
					pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));

					pstmt.executeUpdate();
					pstmt.close();

					groupMenuWrapper.recordFound = true;
				}

				groupMenuWrapper.recordFound = true;
				dataArrayWrapper.groupMenuWrapper = new GroupMenuWrapper[1];
				dataArrayWrapper.groupMenuWrapper[0] = groupMenuWrapper;

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
