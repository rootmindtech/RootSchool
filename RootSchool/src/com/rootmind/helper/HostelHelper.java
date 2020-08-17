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
import com.rootmind.wrapper.HostelWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class HostelHelper extends Helper {

	// -----------------Start insertHostel---------------------

	public AbstractWrapper insertHostel(UsersWrapper usersProfileWrapper, HostelWrapper hostelWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// String countryCode=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		PreparedStatement pstmt = null;

		try {
			con = getConnection();

			sql = " INSERT INTO MST_Hostel(Code, Description, TotalRooms, VacantRooms, SupervisorID, HostelGroup, MakerID, MakerDateTime, SchoolID) "
					+ "Values (?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			// HostelWrapper.status="ACTIVE";

			pstmt.setString(1, Utility.trim(hostelWrapper.code));
			pstmt.setString(2, Utility.trim(hostelWrapper.desc));
			pstmt.setString(3, Utility.trim(hostelWrapper.totalRooms));
			pstmt.setString(4, Utility.trim(hostelWrapper.vacantRooms));
			pstmt.setString(5, Utility.trim(hostelWrapper.supervisorID));
			pstmt.setString(6, Utility.trim(hostelWrapper.hostelGroup));
			pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(8, Utility.getCurrentTime()); // maker date time
			pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			hostelWrapper.recordFound = true;

			dataArrayWrapper.hostelWrapper = new HostelWrapper[1];
			dataArrayWrapper.hostelWrapper[0] = hostelWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into MST_Hostel");

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

	// -----------------End insertHostel---------------------

	// -----------------Start updateHostel---------------------
	public AbstractWrapper updateHostel(UsersWrapper usersProfileWrapper, HostelWrapper hostelWrapper)
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

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT Code FROM MST_Hostel WHERE Code=? and SchoolID=?");

			System.out.println("MST_Hostel Code is " + hostelWrapper.code);

			pstmt.setString(1, hostelWrapper.code); // --it may null
			pstmt.setString(2, usersProfileWrapper.schoolID); // --it may null

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertHostel(usersProfileWrapper, hostelWrapper);
			} else {
				resultSet.close();
				pstmt.close();
				pstmt = con.prepareStatement(
						"UPDATE MST_Hostel SET Description=?, TotalRooms=?, VacantRooms=?, SupervisorID=?, HostelGroup=?,  ModifierID=?,"
								+ "  ModifierDateTime=? WHERE Code=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(hostelWrapper.desc));
				pstmt.setString(2, Utility.trim(hostelWrapper.totalRooms));
				pstmt.setString(3, Utility.trim(hostelWrapper.vacantRooms));
				pstmt.setString(4, Utility.trim(hostelWrapper.supervisorID));
				pstmt.setString(5, Utility.trim(hostelWrapper.hostelGroup));
				pstmt.setString(6, Utility.trim(usersProfileWrapper.userid)); // modifierID
				pstmt.setTimestamp(7, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(8, Utility.trim(hostelWrapper.code));
				pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				hostelWrapper.recordFound = true;
				dataArrayWrapper.hostelWrapper = new HostelWrapper[1];
				dataArrayWrapper.hostelWrapper[0] = hostelWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully MST_Hostel Updated");
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
	// -----------------End updateHostel---------------------

	// -----------------Start fetchHostel---------------------

	public AbstractWrapper fetchHostel(UsersWrapper usersProfileWrapper, HostelWrapper hostelWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {
			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			PreparedStatement pstmt = con
					.prepareStatement("SELECT Code, Description, TotalRooms, VacantRooms, SupervisorID,"
							+ " HostelGroup, MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM MST_Hostel where SchoolID=? "
							+ " Order By Code ASC");

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			
			while (resultSet.next()) {

				hostelWrapper = new HostelWrapper();

				hostelWrapper.code = Utility.trim(resultSet.getString("Code"));
				hostelWrapper.desc = Utility.trim(resultSet.getString("Description"));
				hostelWrapper.totalRooms = Utility.trim(resultSet.getString("TotalRooms"));
				hostelWrapper.vacantRooms = Utility.trim(resultSet.getString("VacantRooms"));
				hostelWrapper.supervisorID = Utility.trim(resultSet.getString("SupervisorID"));
				hostelWrapper.hostelGroup = Utility.trim(resultSet.getString("HostelGroup"));
				hostelWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				hostelWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				hostelWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				hostelWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));

				hostelWrapper.supervisorIDValue = popoverHelper.fetchPopoverDesc(hostelWrapper.supervisorID,
						"MST_Supervisor", usersProfileWrapper.schoolID);
				hostelWrapper.hostelGroupValue = popoverHelper.fetchPopoverDesc(hostelWrapper.hostelGroup,
						"MST_UserGroup", usersProfileWrapper.schoolID);

				hostelWrapper.recordFound = true;

				System.out.println("MST_Hostel fetch successful");

				vector.addElement(hostelWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.hostelWrapper = new HostelWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.hostelWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

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
	// -----------------End fetchHostel---------------------

	// -----------------Generate Staff RefNo-------------------------------
	/*
	 * public String generateStaffRefNo(HostelWrapper hostelWrapper)throws Exception
	 * {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * //DataArrayWrapper dataArrayWrapper = new DataArrayWrapper(); String
	 * sql=null;
	 * 
	 * SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");
	 * 
	 * DecimalFormat formatter = (DecimalFormat)
	 * NumberFormat.getInstance(Locale.US); DecimalFormatSymbols symbols =
	 * formatter.getDecimalFormatSymbols(); symbols.setGroupingSeparator(',');
	 * formatter.applyPattern("###,###,###,##0.00");
	 * formatter.setDecimalFormatSymbols(symbols);
	 * 
	 * int staffRefNo=0; String finalStaffRefNo=null; String staffCode=null;
	 * 
	 * try {
	 * 
	 * 
	 * con = getConnection();
	 * 
	 * 
	 * sql="SELECT StaffRefNo, StaffCode from MST_Parameter";
	 * 
	 * PreparedStatement pstmt = con.prepareStatement(sql);
	 * 
	 * resultSet = pstmt.executeQuery();
	 * 
	 * if (resultSet.next()) {
	 * 
	 * staffRefNo=resultSet.getInt("StaffRefNo"); System.out.println("StaffRefNo " +
	 * staffRefNo); staffCode=resultSet.getString("StaffCode");
	 * 
	 * }
	 * 
	 * resultSet.close(); pstmt.close();
	 * 
	 * if(staffRefNo==0) { staffRefNo=1;
	 * 
	 * } else {
	 * 
	 * staffRefNo=staffRefNo+1; }
	 * 
	 * sql="UPDATE MST_Parameter set StaffRefNo=?";
	 * 
	 * 
	 * System.out.println("sql " + sql);
	 * 
	 * pstmt = con.prepareStatement(sql);
	 * 
	 * pstmt.setInt(1,staffRefNo);
	 * 
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * int paddingSize=6;
	 * 
	 * finalStaffRefNo=staffCode+dmyFormat.format(new
	 * java.util.Date()).toUpperCase()+String.format("%0" + paddingSize
	 * +"d",staffRefNo);
	 * 
	 * System.out.println("Successfully generated StaffRefNo " + finalStaffRefNo);
	 * 
	 * } catch (SQLException se) { se.printStackTrace(); throw new
	 * SQLException(se.getSQLState()+ " ; "+ se.getMessage()); } catch
	 * (NamingException ne) { ne.printStackTrace(); throw new
	 * NamingException(ne.getMessage()); } catch (Exception ex) {
	 * ex.printStackTrace(); throw new Exception(ex.getMessage()); } finally { try {
	 * releaseConnection(resultSet, con); } catch (SQLException se) {
	 * se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
	 * se.getMessage()); } }
	 * 
	 * return finalStaffRefNo; }
	 */

	// -----------------End Generate Staff RefNo---------------------------

}
