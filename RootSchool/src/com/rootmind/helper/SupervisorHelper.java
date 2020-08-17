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
import com.rootmind.wrapper.SupervisorWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class SupervisorHelper extends Helper {

	// -----------------Start insertSupervisor---------------------

	public AbstractWrapper insertSupervisor(UsersWrapper usersProfileWrapper, SupervisorWrapper supervisorWrapper)
			throws Exception {

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

			sql = " INSERT INTO MST_Supervisor(Code, Description, MobileNo, Email, PhotoID, MakerID, MakerDateTime, SchoolID) "
					+ "Values (?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(supervisorWrapper.code));
			pstmt.setString(2, Utility.trim(supervisorWrapper.desc));
			pstmt.setString(3, Utility.trim(supervisorWrapper.mobileNo));
			pstmt.setString(4, Utility.trim(supervisorWrapper.email));
			pstmt.setString(5, Utility.trim(supervisorWrapper.photoID));
			pstmt.setString(6, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(7, Utility.getCurrentTime()); // maker date time
			pstmt.setString(8, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			supervisorWrapper.recordFound = true;

			dataArrayWrapper.supervisorWrapper = new SupervisorWrapper[1];
			dataArrayWrapper.supervisorWrapper[0] = supervisorWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into Mst_Supervisor");

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

	// -----------------End insertSupervisor---------------------

	// -----------------Start updateSupervisor---------------------
	public AbstractWrapper updateSupervisor(UsersWrapper usersProfileWrapper, SupervisorWrapper supervisorWrapper)
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

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT Code FROM MST_Supervisor WHERE Code=? and SchoolID=?");

			System.out.println("MST_Supervisor SupervisorID is" + supervisorWrapper.code);

			pstmt.setString(1, supervisorWrapper.code); // --it may null
			pstmt.setString(2, usersProfileWrapper.schoolID); // --it may null

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertSupervisor(usersProfileWrapper, supervisorWrapper);
			} else {
				resultSet.close();
				pstmt.close();
				pstmt = con.prepareStatement(
						"UPDATE MST_Supervisor SET Description=?, MobileNo=?, Email=?, PhotoID=?,  ModifierID=?,"
								+ " ModifierDateTime=? WHERE Code=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(supervisorWrapper.desc));
				pstmt.setString(2, Utility.trim(supervisorWrapper.mobileNo));
				pstmt.setString(3, Utility.trim(supervisorWrapper.email));
				pstmt.setString(4, Utility.trim(supervisorWrapper.photoID));
				pstmt.setString(5, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(6, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(7, Utility.trim(supervisorWrapper.code));
				pstmt.setString(8, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				supervisorWrapper.recordFound = true;
				dataArrayWrapper.supervisorWrapper = new SupervisorWrapper[1];
				dataArrayWrapper.supervisorWrapper[0] = supervisorWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully MST_Supervisor Updated");
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
	// -----------------End updateSupervisor---------------------

	// -----------------Start fetchSupervisor---------------------

	public AbstractWrapper fetchSupervisor(UsersWrapper usersProfileWrapper, SupervisorWrapper supervisorWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {

			con = getConnection();

			PreparedStatement pstmt = con
					.prepareStatement("SELECT Code, Description, MobileNo, Email, PhotoID,"
							+ " MakerID, MakerDateTime, ModifierID, ModifierDateTime FROM MST_Supervisor where SchoolID=? "
							+ " Order By Code ASC");
			
			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				supervisorWrapper = new SupervisorWrapper();

				supervisorWrapper.code = Utility.trim(resultSet.getString("Code"));
				supervisorWrapper.desc = Utility.trim(resultSet.getString("Description"));
				supervisorWrapper.mobileNo = Utility.trim(resultSet.getString("MobileNo"));
				supervisorWrapper.email = Utility.trim(resultSet.getString("Email"));
				supervisorWrapper.photoID = Utility.trim(resultSet.getString("PhotoID"));
				supervisorWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				supervisorWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				supervisorWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				supervisorWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));

				supervisorWrapper.recordFound = true;

				System.out.println("MST_Supervisor fetch successful");

				vector.addElement(supervisorWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.supervisorWrapper = new SupervisorWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.supervisorWrapper);
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
	// -----------------End fetchSupervisor---------------------

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
