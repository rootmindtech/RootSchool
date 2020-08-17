package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DashboardWebWrapper;
import com.rootmind.wrapper.DashboardWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class DashboardHelper extends Helper {

	// -----------------Start ApplicationsCount count---------------------

	public AbstractWrapper fetchDashboardCount(UsersWrapper usersProfileWrapper, DashboardWrapper dashboardWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("Dashboard count ");

		// Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;
		// DashboardWrapper dashboardWrapperSub = new DashboardWrapper();

		try {

			con = getConnection();

			// -----ServiceTicketsTotalCount-----------

			sql = "SELECT Count(STRefNo) as ServiceTicketsCount FROM ServiceTickets WHERE SchoolID=? ";

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				sql = sql + "AND StudentID=? ";
			}

			// sql = sql + " GROUP BY RecordStatus ORDER BY RecordStatus";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				pstmt.setString(2, dashboardWrapper.studentID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.serviceTicketsCount = Utility.trim(resultSet.getString("ServiceTicketsCount"));

				// dashboardWrapper.requestType =
				// Utility.trim(resultSet.getString("RequestType"));

				// dashboardWrapper.recordStatus =
				// Utility.trim(resultSet.getString("RecordStatus"));

				System.out.println("serviceTicketsCount " + dashboardWrapper.serviceTicketsCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// -------------------end

			// -----ServiceTicketsOpenCount-----------

			sql = "SELECT Count(STRefNo) as ServiceTicketsOpenCount FROM ServiceTickets WHERE SchoolID=? and RecordStatus<>'CLOS003' ";

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				sql = sql + "AND StudentID=? ";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				pstmt.setString(2, dashboardWrapper.studentID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.serviceTicketsOpenCount = Utility.trim(resultSet.getString("ServiceTicketsOpenCount"));

				System.out.println("serviceTicketsOpenCount " + dashboardWrapper.serviceTicketsOpenCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// -------------------end

			// --------ServiceTicketsCloseCount---

			sql = "SELECT Count(STRefNo) as ServiceTicketsCloseCount FROM ServiceTickets WHERE SchoolID=? AND RecordStatus='CLOS003' ";

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				sql = sql + "AND StudentID=? ";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				pstmt.setString(2, dashboardWrapper.studentID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.serviceTicketsCloseCount = Utility
						.trim(resultSet.getString("ServiceTicketsCloseCount"));

				System.out.println("serviceTicketsCloseCount " + dashboardWrapper.serviceTicketsCloseCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// ------------------end

			// -----ServiceTicketsUnassignedCount-----------

			sql = "SELECT Count(STRefNo) as ServiceTicketsUnassignedCount FROM ServiceTickets WHERE SchoolID=? and RecordStatus IS NULL ";

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				sql = sql + "AND StudentID=? ";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				pstmt.setString(2, dashboardWrapper.studentID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.serviceTicketsUnassignedCount = Utility
						.trim(resultSet.getString("ServiceTicketsUnassignedCount"));

				System.out.println("serviceTicketsUnassignedCount " + dashboardWrapper.serviceTicketsUnassignedCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// ------------------end

			//
			// // --------ComplaintsCount---
			//
			// sql = "SELECT Count(STRefNo) as ComplaintsCount FROM ServiceTickets WHERE
			// RequestType='COMPLAINT' AND StudentID=?";
			//
			// pstmt = con.prepareStatement(sql);
			//
			// pstmt.setString(1, dashboardWrapper.studentID);
			//
			// resultSet = pstmt.executeQuery();
			//
			// if (resultSet.next()) {
			//
			// dashboardWrapperSub.complaintCount =
			// Utility.trim(resultSet.getString("ComplaintsCount"));
			//
			// System.out.println("complaintsCount " + dashboardWrapperSub.complaintCount);
			// dashboardWrapper.recordFound = true;
			//
			// }
			//
			// resultSet.close();
			// pstmt.close();
			//
			// // --------RoomCount---
			//
			// sql = "SELECT Count(STRefNo) as RoomCount FROM ServiceTickets WHERE
			// RequestType='ROOM' AND StudentID=?";
			//
			// pstmt = con.prepareStatement(sql);
			//
			// pstmt.setString(1, dashboardWrapper.studentID);
			//
			// resultSet = pstmt.executeQuery();
			//
			// if (resultSet.next()) {
			//
			// dashboardWrapperSub.roomCount =
			// Utility.trim(resultSet.getString("RoomCount"));
			//
			// System.out.println("roomCount " + dashboardWrapperSub.roomCount);
			// dashboardWrapper.recordFound = true;
			//
			// }
			//
			// resultSet.close();
			// pstmt.close();

			// --------TransportCount---

			sql = "SELECT Count(TSRefNo) as TransportCount FROM Transport WHERE SchoolID=? ";

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				sql = sql + "AND StudentID=? ";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.studentID)) {
				pstmt.setString(2, dashboardWrapper.studentID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.transportCount = Utility.trim(resultSet.getString("TransportCount"));

				System.out.println("TransportCount  " + dashboardWrapper.transportCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// ---------------------end

			// --------CalendarActivitiesCount---

			sql = "SELECT Count(CalendarRefNo) as CalendarActivitiesCount FROM CalendarActivities WHERE SchoolID=? ";

			if (!Utility.isEmpty(dashboardWrapper.hostelID)) {
				sql = sql + " AND HostelID=? ";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(dashboardWrapper.hostelID)) {
				pstmt.setString(2, dashboardWrapper.hostelID);
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.calendarActivitiesCount = Utility.trim(resultSet.getString("CalendarActivitiesCount"));

				System.out.println("calendarActivitiesCount  " + dashboardWrapper.calendarActivitiesCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// ----------------------end

			// --------Students Count---

			sql = "SELECT Count(RefNo) as StudentCount FROM StudentProfile WHERE SchoolID=? ";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.studentCount = Utility.trim(resultSet.getString("StudentCount"));

				System.out.println("studentCount  " + dashboardWrapper.studentCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// --------------------end

			// --------Teachers Count---

			sql = "SELECT Count(StaffRefNo) as TeacherCount FROM TeachersProfile WHERE SchoolID=? and StaffType='TEACHER' ";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.teacherCount = Utility.trim(resultSet.getString("TeacherCount"));

				System.out.println("teacherCount  " + dashboardWrapper.teacherCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// --------------------end

			// --------Staff Count---

			sql = "SELECT Count(StaffRefNo) as StaffCount FROM TeachersProfile WHERE SchoolID=? and StaffType<>'TEACHER' ";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				dashboardWrapper.staffCount = Utility.trim(resultSet.getString("StaffCount"));

				System.out.println("staffCount  " + dashboardWrapper.staffCount);
				dashboardWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();
			// --------------------end

			dataArrayWrapper.dashboardWrapper = new DashboardWrapper[1];
			dataArrayWrapper.dashboardWrapper[0] = dashboardWrapper;
			dataArrayWrapper.recordFound = true;

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
	// -----------------End ---------------------

	// -----------------Start fetchDashboardWeb---------------------

	public AbstractWrapper fetchDashboardWeb(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchDashboardWeb");

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;
		// DashboardWrapper dashboardWrapperSub=new DashboardWrapper();
		DashboardWebWrapper dashboardWebWrapper = null;

		String hostelIDs = null;
		try {

			con = getConnection();

			UserGroupHelper userGroupHelper = new UserGroupHelper();
			PopoverHelper popoverHelper = new PopoverHelper();

			hostelIDs = userGroupHelper.fetchSupervisorHostels(usersProfileWrapper);

			System.out.println("DashboardWeb hostelIDs " + hostelIDs);

			// if hostel is assigned to user than only he will get the dashboardwebcount
			if (hostelIDs != null && !hostelIDs.isEmpty()) {

				// --------This is for ServiceRequest,COMPLAINT,ROOM-------

				/*
				 * sql="SELECT RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM ServiceTickets WHERE "
				 * + " HostelID IN "+userGroupHelper.fetchSupervisorHostels(usersProfileWrapper)
				 * + " GROUP BY RequestType,RecordStatus";
				 */

				sql = "SELECT RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM ServiceTickets WHERE "
						+ " HostelID IN " + hostelIDs + " GROUP BY RequestType,RecordStatus";

				System.out.println("ServiceTickets SQL " + sql);

				pstmt = con.prepareStatement(sql);

				resultSet = pstmt.executeQuery();

				while (resultSet.next()) {
					dashboardWebWrapper = new DashboardWebWrapper();

					dashboardWebWrapper.requestType = Utility.trim(resultSet.getString("RequestType"));
					dashboardWebWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));

					dashboardWebWrapper.recordStatusCount = resultSet.getInt("RecordStatusCount");

					dashboardWebWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(
							dashboardWebWrapper.recordStatus, "MST_Status", usersProfileWrapper.schoolID);

					dashboardWebWrapper.recordFound = true;
					vector.addElement(dashboardWebWrapper);

				}

				resultSet.close();
				pstmt.close();

				// --------This is for Transport-------

				/*
				 * sql="SELECT 'TRANSPORT' as RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM Transport WHERE "
				 * + " HostelID IN "+userGroupHelper.fetchSupervisorHostels(usersProfileWrapper)
				 * + " GROUP BY RequestType,RecordStatus";
				 */

				// mySQL
				/*
				 * sql="SELECT 'TRANSPORT' as RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM Transport WHERE "
				 * + " HostelID IN "+hostelIDs + " GROUP BY RequestType,RecordStatus";
				 */

				// Oracle
				sql = "SELECT  RecordStatus, Count(RecordStatus) as RecordStatusCount FROM Transport WHERE "
						+ " HostelID IN " + hostelIDs + " GROUP BY RecordStatus";

				System.out.println("TransportSQL " + sql);

				pstmt = con.prepareStatement(sql);

				resultSet = pstmt.executeQuery();

				while (resultSet.next()) {
					dashboardWebWrapper = new DashboardWebWrapper();

					// dashboardWebWrapper.requestType=Utility.trim(resultSet.getString("RequestType"));
					dashboardWebWrapper.requestType = "TRANSPORT";
					dashboardWebWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));
					dashboardWebWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(
							dashboardWebWrapper.recordStatus, "MST_Status", usersProfileWrapper.schoolID);

					dashboardWebWrapper.recordStatusCount = resultSet.getInt("RecordStatusCount");

					dashboardWebWrapper.recordFound = true;
					vector.addElement(dashboardWebWrapper);

				}

				resultSet.close();
				pstmt.close();

			}

			// --------This is for CalendarActivities-------

			// mysql
			/*
			 * sql="SELECT 'CALENDAR' as RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM CalendarActivities "
			 * + " GROUP BY RequestType,RecordStatus";
			 */

			// Oracle
			sql = "SELECT  RecordStatus, Count(RecordStatus) as RecordStatusCount FROM CalendarActivities "
					+ " GROUP BY RecordStatus";

			System.out.println("CalendarSQL " + sql);

			pstmt = con.prepareStatement(sql);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				dashboardWebWrapper = new DashboardWebWrapper();

				// dashboardWebWrapper.requestType=Utility.trim(resultSet.getString("RequestType"));
				dashboardWebWrapper.requestType = "CALENDAR";
				dashboardWebWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));
				dashboardWebWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(dashboardWebWrapper.recordStatus,
						"MST_Status", usersProfileWrapper.schoolID);
				dashboardWebWrapper.recordStatusCount = resultSet.getInt("RecordStatusCount");

				dashboardWebWrapper.recordFound = true;
				vector.addElement(dashboardWebWrapper);

			}

			resultSet.close();
			pstmt.close();

			// --------This is for News-------

			// mysql
			/*
			 * sql="SELECT 'NEWS' as RequestType, RecordStatus, Count(RecordStatus) as RecordStatusCount FROM News"
			 * + " GROUP BY RequestType,RecordStatus";
			 */

			// Oracle
			sql = "SELECT  RecordStatus, Count(RecordStatus) as RecordStatusCount FROM News" + " GROUP BY RecordStatus";

			System.out.println("NewsSQL " + sql);

			pstmt = con.prepareStatement(sql);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				dashboardWebWrapper = new DashboardWebWrapper();

				// dashboardWebWrapper.requestType=Utility.trim(resultSet.getString("RequestType"));
				dashboardWebWrapper.requestType = "NEWS";
				dashboardWebWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));
				dashboardWebWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(dashboardWebWrapper.recordStatus,
						"MST_Status", usersProfileWrapper.schoolID);
				dashboardWebWrapper.recordStatusCount = resultSet.getInt("RecordStatusCount");

				dashboardWebWrapper.recordFound = true;
				vector.addElement(dashboardWebWrapper);

			}

			resultSet.close();
			pstmt.close();

			if (vector.size() > 0) {
				dataArrayWrapper.dashboardWebWrapper = new DashboardWebWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.dashboardWebWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

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
	// -----------------End ---------------------

}
