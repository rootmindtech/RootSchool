package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.text.NumberFormat;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ServiceTicketsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ServiceTicketsHelper extends Helper {

	public AbstractWrapper insertServiceTicket(UsersWrapper usersProfileWrapper,
			ServiceTicketsWrapper serviceTicketsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		String sql = null;

		try {

			con = getConnection();

			sql = "INSERT INTO ServiceTickets(STRefNo, StudentID, HostelID, BlockNo, RoomNo, ServiceID, Severity, Description, RequestType,"
					+ " ProposedRoomNo, RecordStatus, RaisedBy, RaisedDateTime, StudentName, SchoolID) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			serviceTicketsWrapper.stRefNo = generateSTRefNo(serviceTicketsWrapper, usersProfileWrapper.schoolID);

			System.out.println(" StudentID " + Utility.trim(serviceTicketsWrapper.studentID));
			System.out.println("HostelID  " + Utility.trim(serviceTicketsWrapper.hostelID));

			pstmt.setString(1, Utility.trim(serviceTicketsWrapper.stRefNo));
			pstmt.setString(2, Utility.trim(serviceTicketsWrapper.studentID));
			pstmt.setString(3, Utility.trim(serviceTicketsWrapper.hostelID));
			pstmt.setString(4, Utility.trim(serviceTicketsWrapper.blockNo));
			pstmt.setString(5, Utility.trim(serviceTicketsWrapper.roomNo));
			pstmt.setString(6, Utility.trim(serviceTicketsWrapper.serviceID));
			pstmt.setString(7, Utility.trim(serviceTicketsWrapper.severity));
			pstmt.setString(8, Utility.trim(serviceTicketsWrapper.desc));
			pstmt.setString(9, Utility.trim(serviceTicketsWrapper.requestType));
			pstmt.setString(10, Utility.trim(serviceTicketsWrapper.proposedRoomNo));
			pstmt.setString(11, Utility.trim(serviceTicketsWrapper.recordStatus));
			// pstmt.setString(12,Utility.trim(serviceTicketsWrapper.requestStatus));
			pstmt.setString(12, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(13, Utility.getCurrentTime());
			pstmt.setString(14, Utility.trim(serviceTicketsWrapper.studentName));
			pstmt.setString(15, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			serviceTicketsWrapper.recordFound = true;

			dataArrayWrapper.serviceTicketsWrapper = new ServiceTicketsWrapper[1];
			dataArrayWrapper.serviceTicketsWrapper[0] = serviceTicketsWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("serviceTicket inserted for StudentID " + serviceTicketsWrapper.studentID);

		}

		catch (SQLException se) {
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

	public AbstractWrapper updateServiceTicket(UsersWrapper usersProfileWrapper,
			ServiceTicketsWrapper serviceTicketsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		String sql = null;

		try {

			con = getConnection();

			System.out.println("update for stRefNo " + Utility.trim(serviceTicketsWrapper.stRefNo));

			sql = "UPDATE ServiceTickets SET ProposedRoomNo=?, ResponseDescription=?,"
					+ " ExpectedResolutionDate=?, ResolutionDate=?, RequestStatus=?, RecordStatus=?, SupervisorID=?, SupervisorDateTime=? "
					+ " WHERE STRefNo=? and SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(serviceTicketsWrapper.proposedRoomNo));
			pstmt.setString(2, Utility.trim(serviceTicketsWrapper.responseDesc));
			pstmt.setDate(3, Utility.getDate(serviceTicketsWrapper.expectedResolutionDate));
			pstmt.setDate(4, Utility.getDate(serviceTicketsWrapper.resolutionDate));
			pstmt.setString(5, Utility.trim(serviceTicketsWrapper.requestStatus));
			pstmt.setString(6, Utility.trim(serviceTicketsWrapper.recordStatus));
			pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(8, Utility.getCurrentTime());
			pstmt.setString(9, Utility.trim(serviceTicketsWrapper.stRefNo));
			pstmt.setString(10, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			serviceTicketsWrapper.recordFound = true;

			dataArrayWrapper.serviceTicketsWrapper = new ServiceTicketsWrapper[1];
			dataArrayWrapper.serviceTicketsWrapper[0] = serviceTicketsWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("updateServiceTicket updated ");

			// --Update Banner

			/*
			 * if (serviceTicketsWrapper.requestType.equals("ROOM"))
			 * 
			 * { Connection bannercon=getBannerConnection();
			 * 
			 * write update staement;
			 * 
			 * resut.close; pstmt.close(); bannercon.close(); }
			 */

		}

		catch (SQLException se) {
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

	// -----------------Generate generateSTRefNo-------------------------------
	public String generateSTRefNo(ServiceTicketsWrapper serviceTicketsWrapper, String schoolID) throws Exception {

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

		int stRefNo = 0; // serviceTicketRefNo
		String finalSTRefNo = null;
		String serviceTicketCode = null;

		try {
			con = getConnection();

			sql = "SELECT ServiceTicketRefNo,ServiceTicketCode FROM MST_Parameter where SchoolID=?"; // ,ComplaintCode,RoomCode

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				stRefNo = resultSet.getInt("ServiceTicketRefNo");
				System.out.println("ServiceTicketRefNo " + stRefNo);

				// if (serviceTicketsWrapper.requestType.equals("SERVICES")) {
				serviceTicketCode = resultSet.getString("ServiceTicketCode");
				// }
				// if (serviceTicketsWrapper.requestType.equals("COMPLAINT")) {
				// serviceTicketCode = resultSet.getString("ComplaintCode");
				// }
				// if (serviceTicketsWrapper.requestType.equals("ROOM")) {
				// serviceTicketCode = resultSet.getString("RoomCode");
				// }

				System.out.println("serviceTicketCode " + serviceTicketCode);

			}

			resultSet.close();
			pstmt.close();

			if (stRefNo == 0) {
				stRefNo = 1;

			} else {

				stRefNo = stRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set ServiceTicketRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, stRefNo);
			pstmt.setString(2, Utility.trim(schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			finalSTRefNo = serviceTicketCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", stRefNo);

			System.out.println("Successfully generated STRefNo  " + finalSTRefNo);

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

		return finalSTRefNo;
	}

	// -----------------End generateSTRefNo ---------------------------

	// -----------------Start fetchServiceTickets---------------------

	public AbstractWrapper fetchServiceTicket(UsersWrapper usersProfileWrapper,
			ServiceTicketsWrapper serviceTicketsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		String sql = null;
		// int queueMaxRecords = 0;
		// int mQueueMaxRecords = 0;

		PreparedStatement pstmt = null;

		// PreparedStatement pstmtSub =null;
		// ResultSet resultSetSub = null;
		int targetTAT = 0;

		// String hostelID = null;

		System.out.println("serviceTicketsWrapper.studentID " + serviceTicketsWrapper.studentID);
		int n = 1;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			// -------- Fetch Target TAT From MST_Parameter
			pstmt = con.prepareStatement("SELECT  TargetTAT FROM MST_Parameter where SchoolID=?");

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				targetTAT = resultSet.getInt("TargetTAT");
				serviceTicketsWrapper.targetTAT = Utility.getTimeformat(targetTAT);

				System.out.println(" serviceTicketsWrapper.targetTAT " + serviceTicketsWrapper.targetTAT);

			}
			resultSet.close();
			pstmt.close();
			// -------

			// // --if user is not student then fetch Hostel ID's (user is supervisor)
			// if (usersProfileWrapper.student != null &&
			// !usersProfileWrapper.student.equals("Y")) {
			// System.out.println("Supervisor Hostel IDs ");
			// UserGroupHelper userGroupHelper = new UserGroupHelper();
			// hostelID = userGroupHelper.fetchSupervisorHostels(usersProfileWrapper);
			//
			// }
			//
			// System.out.println(" hostelID " + hostelID);

			// -------- Fetch QueueMaxRecords Parameter
			// pstmt = con.prepareStatement("SELECT QueueMaxRecords,MQueueMaxRecords FROM
			// MST_Parameter");
			//
			// resultSet = pstmt.executeQuery();
			// if (resultSet.next()) {
			// queueMaxRecords = resultSet.getInt("QueueMaxRecords");
			// mQueueMaxRecords = resultSet.getInt("MQueueMaxRecords");
			// System.out.println(" queueMaxRecords " + queueMaxRecords);
			//
			// }
			//
			// resultSet.close();
			// pstmt.close();

			// -------

			/*
			 * sql="SELECT STRefNo, StudentID, HostelID, BlockNo, RoomNo, ServiceID,Severity, Description, "
			 * +
			 * " RaisedBy, RaisedDateTime, Status, RequestType, ProposedRoomNo FROM ServiceTickets WHERE StudentID=? AND RequestType=?"
			 * ;
			 */

			// sql="SELECT a.STRefNo, a.StudentID, a.HostelID, a.BlockNo, a.RoomNo,
			// a.ServiceID, a.Severity, a.Description, "
			// + " a.RaisedBy, a.RaisedDateTime, a.RequestType, a.ProposedRoomNo,
			// a.ResponseDescription, "
			// + " a.ExpectedResolutionDate, a.ResolutionDate, a.RecordStatus,
			// a.RequestStatus, b.STUDENT_NAME, a.SupervisorID, "
			// + " a.SupervisorDateTime "
			// + " FROM ServiceTickets a LEFT JOIN STUDENT_HOSTEL b ON
			// a.StudentID=b.STUDENT_ID "
			// + " WHERE RequestType=?";
			//
			sql = "SELECT STRefNo, StudentID, HostelID, BlockNo, RoomNo, ServiceID, Severity, Description, "
					+ " RaisedBy, RaisedDateTime, RequestType, ProposedRoomNo, ResponseDescription, "
					+ " ExpectedResolutionDate, ResolutionDate, RecordStatus, RequestStatus,  StudentName, SupervisorID, "
					+ " SupervisorDateTime FROM ServiceTickets WHERE SchoolID=?  ";

			if (!Utility.isEmpty(serviceTicketsWrapper.studentID)) {

				sql = sql + " AND StudentID=? ";

			}
			// if (!Utility.isEmpty(serviceTicketsWrapper.requestType)) {
			//
			// sql = sql + " and RequestType = ?";
			// }

			if (!Utility.isEmpty(serviceTicketsWrapper.stRefNo)) {

				sql = sql + " AND STRefNo = ?";
			}

			// else if (serviceTicketsWrapper.studentName != null &&
			// !serviceTicketsWrapper.studentName.trim().isEmpty()) {
			//
			// sql = sql + " AND (UPPER(StudentName) LIKE ? OR UPPER(SurName) LIKE ?)";
			//
			// System.out.println("fetchServiceTicket studentName " +
			// serviceTicketsWrapper.studentName);
			//
			// } else if (serviceTicketsWrapper.searchStartDate != null
			// && !serviceTicketsWrapper.searchStartDate.trim().isEmpty()) {
			//
			// sql = sql + " AND RaisedDateTime >= ?";
			//
			// }
			//
			// else if (serviceTicketsWrapper.searchEndDate != null
			// && !serviceTicketsWrapper.searchEndDate.trim().isEmpty()) {
			//
			// sql = sql + " AND RaisedDateTime <= ?";
			//
			// }

			// ---user is supervisor then
			// if (usersProfileWrapper.student != null &&
			// !usersProfileWrapper.student.equals("Y")) {
			// sql = sql + " AND HostelID IN " + hostelID; // ('HST001','HST002');
			// }

			// System.out.println(" serviceTicketsWrapper.searchCode " +
			// serviceTicketsWrapper.searchCode);
			//
			// if (serviceTicketsWrapper.searchCode != null
			// && serviceTicketsWrapper.searchCode.equals("SUPERVISOR_MAINT_QUEUE")) {
			//
			// // MYSQL DB
			// // sql=sql + " AND a.RecordStatus NOT IN ('STS003') ORDER BY RaisedDateTime
			// DESC
			// // LIMIT " + queueMaxRecords; //('CLOSED'-STS003)
			//
			// // Oracle DB
			// sql = sql + " AND a.RecordStatus NOT IN ('STS003')" + " AND ROWNUM<= " +
			// queueMaxRecords
			// + " ORDER BY RaisedDateTime DESC";
			// }
			//
			// if (serviceTicketsWrapper.searchCode != null
			// && serviceTicketsWrapper.searchCode.equals("STUDENT_DEVICE_QUEUE")) {
			//
			// // sql=sql + " ORDER BY RaisedDateTime DESC LIMIT " + mQueueMaxRecords;
			// //mysql
			// // DB
			//
			// // Oracle DB
			// sql = sql + " AND ROWNUM<= " + mQueueMaxRecords + " ORDER BY RaisedDateTime
			// DESC";
			//
			// }
			//
			// if (serviceTicketsWrapper.searchCode != null
			// && serviceTicketsWrapper.searchCode.equals("SERVICE_TICKET_SEARCH")) {
			// // MYSQL DB
			// // sql=sql + " ORDER BY RaisedDateTime DESC LIMIT " + queueMaxRecords;
			//
			// // Oracle DB
			// sql = sql + " AND ROWNUM<= " + queueMaxRecords + " ORDER BY RaisedDateTime
			// DESC";
			//
			// }

			System.out.println("final SQL " + sql);
			pstmt = con.prepareStatement(sql);

			/*
			 * pstmt.setString(1,Utility.trim(serviceTicketsWrapper.studentID));
			 * pstmt.setString(2,Utility.trim(serviceTicketsWrapper.reqType));
			 */

			System.out.println("Request Type is " + serviceTicketsWrapper.requestType);

			pstmt.setString(n, Utility.trim(usersProfileWrapper.schoolID));
			// pstmt.setString(2, Utility.trim(serviceTicketsWrapper.requestType));

			// pstmt.setString(2,Utility.trim(serviceTicketsWrapper.studentID));

			if (!Utility.isEmpty(serviceTicketsWrapper.studentID)) {
				pstmt.setString(++n, Utility.trim(serviceTicketsWrapper.studentID));
			}

			if (!Utility.isEmpty(serviceTicketsWrapper.stRefNo)) {
				pstmt.setString(++n, Utility.trim(serviceTicketsWrapper.stRefNo));
			}

			// else if (serviceTicketsWrapper.studentName != null &&
			// !serviceTicketsWrapper.studentName.equals("")) {
			// pstmt.setString(++n, Utility.trim(serviceTicketsWrapper.studentName));
			// pstmt.setString(++n, Utility.trim(serviceTicketsWrapper.studentName));
			// } else if (serviceTicketsWrapper.searchStartDate != null
			// && !serviceTicketsWrapper.searchStartDate.equals("")) {
			// pstmt.setDate(++n,
			// Utility.getDate(serviceTicketsWrapper.searchStartDate.trim()));
			// }
			//
			// else if (serviceTicketsWrapper.searchEndDate != null &&
			// !serviceTicketsWrapper.searchEndDate.equals("")) {
			// pstmt.setDate(++n,
			// Utility.getDate(serviceTicketsWrapper.searchEndDate.trim()));
			// }

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				serviceTicketsWrapper = new ServiceTicketsWrapper();

				serviceTicketsWrapper.stRefNo = Utility.trim(resultSet.getString("STRefNo"));
				serviceTicketsWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				serviceTicketsWrapper.hostelID = Utility.trim(resultSet.getString("HostelID"));
				serviceTicketsWrapper.blockNo = Utility.trim(resultSet.getString("BlockNo"));
				serviceTicketsWrapper.roomNo = Utility.trim(resultSet.getString("RoomNo"));
				serviceTicketsWrapper.serviceID = Utility.trim(resultSet.getString("ServiceID"));
				serviceTicketsWrapper.severity = Utility.trim(resultSet.getString("Severity"));
				serviceTicketsWrapper.desc = Utility.trim(resultSet.getString("Description"));
				serviceTicketsWrapper.raisedDateTime = resultSet.getString("RaisedDateTime");

				serviceTicketsWrapper.raisedBy = Utility.trim(resultSet.getString("RaisedBy"));
				serviceTicketsWrapper.requestType = Utility.trim(resultSet.getString("RequestType"));
				serviceTicketsWrapper.proposedRoomNo = Utility.trim(resultSet.getString("ProposedRoomNo"));
				serviceTicketsWrapper.responseDesc = Utility.trim(resultSet.getString("ResponseDescription"));
				serviceTicketsWrapper.expectedResolutionDate = Utility
						.setDate(resultSet.getString("ExpectedResolutionDate"));
				serviceTicketsWrapper.expectedResolutionDateAMPM = Utility
						.setDateAMPM(resultSet.getString("ExpectedResolutionDate"));

				serviceTicketsWrapper.resolutionDate = Utility.setDate(resultSet.getString("ResolutionDate"));
				serviceTicketsWrapper.resolutionDateAMPM = Utility.setDateAMPM(resultSet.getString("ResolutionDate"));
				serviceTicketsWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));
				serviceTicketsWrapper.requestStatus = Utility.trim(resultSet.getString("RequestStatus"));

				serviceTicketsWrapper.severityValue = popoverHelper.fetchPopoverDesc(serviceTicketsWrapper.severity,
						"MST_Severity", usersProfileWrapper.schoolID);
				serviceTicketsWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(
						serviceTicketsWrapper.recordStatus, "MST_Status", usersProfileWrapper.schoolID);
				serviceTicketsWrapper.hostelIDValue = popoverHelper.fetchPopoverDesc(serviceTicketsWrapper.hostelID,
						"MST_Hostel", usersProfileWrapper.schoolID);

				serviceTicketsWrapper.requestTypeValue = popoverHelper.fetchPopoverDesc(serviceTicketsWrapper.serviceID,
						"MST_RequestType", usersProfileWrapper.schoolID);

				serviceTicketsWrapper.serviceIDValue = popoverHelper.fetchPopoverDesc(serviceTicketsWrapper.serviceID,
						"MST_ServiceID", usersProfileWrapper.schoolID);

				serviceTicketsWrapper.requestStatusValue = popoverHelper.fetchPopoverDesc(
						serviceTicketsWrapper.requestStatus, "MST_RequestStatus", usersProfileWrapper.schoolID);

				//
				// if (!Utility.isEmpty(serviceTicketsWrapper.requestType)) {
				// switch (serviceTicketsWrapper.requestType) {
				// case "COMPLAINT": {
				//
				// serviceTicketsWrapper.requestStatusValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.requestStatus, "MST_ComplaintStatus",
				// usersProfileWrapper.schoolID);
				//
				// serviceTicketsWrapper.serviceIDValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.serviceID, "MST_Complaint",
				// usersProfileWrapper.schoolID);
				// break;
				// }
				// case "SERVICES": {
				// serviceTicketsWrapper.requestStatusValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.requestStatus, "MST_MaintenanceStatus",
				// usersProfileWrapper.schoolID);
				// serviceTicketsWrapper.serviceIDValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.serviceID, "MST_ServiceID",
				// usersProfileWrapper.schoolID);
				// break;
				// }
				// case "ROOM": {
				//
				// serviceTicketsWrapper.requestStatusValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.requestStatus, "MST_RoomStatus",
				// usersProfileWrapper.schoolID);
				// serviceTicketsWrapper.serviceIDValue = popoverHelper.fetchPopoverDesc(
				// serviceTicketsWrapper.serviceID, "MST_ServiceID",
				// usersProfileWrapper.schoolID);
				// break;
				//
				// }
				// }
				//
				// }

				serviceTicketsWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));

				serviceTicketsWrapper.supervisorID = Utility.trim(resultSet.getString("SupervisorID"));
				serviceTicketsWrapper.supervisorDateTime = Utility
						.setDateAMPM(resultSet.getString("SupervisorDateTime"));

				serviceTicketsWrapper.raisedDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("RaisedDateTime"));

				// --tat calculation
				serviceTicketsWrapper.tat = Utility
						.getTimeformat(Utility.timeDifference(serviceTicketsWrapper.raisedDateTime, "M"));
				serviceTicketsWrapper.tatColor = Utility
						.getTATColor(Utility.timeDifference(serviceTicketsWrapper.raisedDateTime, "M"), targetTAT);
				System.out.println(" serviceTicketsWrapper.tat " + serviceTicketsWrapper.tat);

				serviceTicketsWrapper.recordFound = true;

				vector.addElement(serviceTicketsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.serviceTicketsWrapper = new ServiceTicketsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.serviceTicketsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
				System.out.println("fetchServiceTickets successful");

			} else {

				dataArrayWrapper.serviceTicketsWrapper = new ServiceTicketsWrapper[1];
				serviceTicketsWrapper.recordFound = false;
				dataArrayWrapper.serviceTicketsWrapper[0] = serviceTicketsWrapper;
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
	// -----------------End fetchUsersProfile---------------------

	/*
	 * //---------------------------------Start Insert
	 * ServiceTicketResponse-----------------------------------------
	 * 
	 * 
	 * public AbstractWrapper insertServiceTicketRes(UsersWrapper
	 * usersProfileWrapper,ServiceTicketsWrapper serviceTicketsWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	 * 
	 * PreparedStatement pstmt=null;
	 * 
	 * //String defaultPassword=null; String sql=null;
	 * 
	 * try {
	 * 
	 * 
	 * con = getConnection();
	 * 
	 * 
	 * sql="INSERT INTO ServiceTicketsRes(STRefNo, StudentID, ResponseDescription,Status "
	 * + " SupervisorID, SupervisorDateTime) Values(?,?,?,?,?,?)";
	 * 
	 * pstmt = con.prepareStatement(sql);
	 * 
	 * serviceTicketsWrapper.stRefNo=generateSTRefNo();
	 * 
	 * 
	 * System.out.println(" StudentID "+Utility.trim(serviceTicketsWrapper.studentID
	 * ));
	 * 
	 * pstmt.setString(1,Utility.trim(serviceTicketsWrapper.stRefNo));
	 * pstmt.setString(2,Utility.trim(serviceTicketsWrapper.studentID));
	 * pstmt.setString(3,Utility.trim(serviceTicketsWrapper.resDesc));
	 * pstmt.setString(4,Utility.trim(serviceTicketsWrapper.status));
	 * pstmt.setString(5,Utility.trim(usersProfileWrapper.userid)); //supervisorID
	 * pstmt.setTimestamp(6,Utility.getCurrentTime()); // SupervisorDateTime
	 * 
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * serviceTicketsWrapper.recordFound=true;
	 * 
	 * dataArrayWrapper.serviceTicketsWrapper=new ServiceTicketsWrapper[1];
	 * dataArrayWrapper.serviceTicketsWrapper[0]=serviceTicketsWrapper;
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("serviceTicketRes inserted for StudentID " +
	 * serviceTicketsWrapper.studentID);
	 * 
	 * 
	 * 
	 * }
	 * 
	 * catch (SQLException se) { se.printStackTrace(); throw new
	 * SQLException(se.getSQLState()+ " ; "+ se.getMessage()); } catch
	 * (NamingException ne) { ne.printStackTrace(); throw new
	 * NamingException(ne.getMessage()); } catch (Exception ex) {
	 * ex.printStackTrace(); throw new Exception(ex.getMessage()); } finally { try {
	 * releaseConnection(resultSet, con); } catch (SQLException se) {
	 * se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
	 * se.getMessage()); } }
	 * 
	 * return dataArrayWrapper; }
	 * 
	 * 
	 * //---------------------------------End
	 * ServiceTicketResponse-----------------------------------------
	 */
	// ---------------------------------Start update
	// ServiceTicket--------------------------------

	/*
	 * public AbstractWrapper updateServiceTicket(UsersWrapper
	 * usersProfileWrapper,ServiceTicketsWrapper serviceTicketsWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * 
	 * DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	 * 
	 * PreparedStatement pstmt=null;
	 * 
	 * //String defaultPassword=null; String sql=null;
	 * 
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * con = getConnection();
	 * 
	 * System.out.println("update for stRefNo "+
	 * Utility.trim(serviceTicketsWrapper.stRefNo));
	 * 
	 * 
	 * sql="UPDATE ServiceTickets SET ResponseDescription=?, RequestStatus=?, RecordStatus=?, ExpectedResolutionDate=?, ResolutionDate=?, "
	 * + " SupervisorID=?, SupervisorDateTime=? WHERE STRefNo=? AND RequestType=?";
	 * 
	 * 
	 * pstmt = con.prepareStatement(sql);
	 * 
	 * pstmt.setString(1,Utility.trim(serviceTicketsWrapper.respDesc));
	 * //ResponseDescription
	 * pstmt.setString(2,Utility.trim(serviceTicketsWrapper.requestStatus));
	 * pstmt.setString(3,Utility.trim(serviceTicketsWrapper.recordStatus));
	 * pstmt.setDate(4,Utility.getDate(serviceTicketsWrapper.expResolDate));
	 * pstmt.setDate(5,Utility.getDate(serviceTicketsWrapper.resolDate));
	 * pstmt.setString(6,Utility.trim(usersProfileWrapper.userid));
	 * pstmt.setTimestamp(7,Utility.getCurrentTime());
	 * pstmt.setString(8,Utility.trim(serviceTicketsWrapper.stRefNo));
	 * pstmt.setString(9,Utility.trim(serviceTicketsWrapper.requestType));
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * serviceTicketsWrapper.recordFound=true;
	 * 
	 * dataArrayWrapper.serviceTicketsWrapper=new ServiceTicketsWrapper[1];
	 * dataArrayWrapper.serviceTicketsWrapper[0]=serviceTicketsWrapper;
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * 
	 * System.out.println("updateServiceTicketResp updated ");
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * catch (SQLException se) { se.printStackTrace(); throw new
	 * SQLException(se.getSQLState()+ " ; "+ se.getMessage()); } catch
	 * (NamingException ne) { ne.printStackTrace(); throw new
	 * NamingException(ne.getMessage()); } catch (Exception ex) {
	 * ex.printStackTrace(); throw new Exception(ex.getMessage()); } finally { try {
	 * releaseConnection(resultSet, con); } catch (SQLException se) {
	 * se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
	 * se.getMessage()); } }
	 * 
	 * return dataArrayWrapper; }
	 */

	// --------------------------------- End update
	// ServiceTicketResponse----------------------------------

	/*
	 * //-----------------Start fetchServiceTicketRequests---------------------
	 * 
	 * public AbstractWrapper fetchServiceTicketReq(UsersWrapper
	 * usersProfileWrapper, ServiceTicketsWrapper serviceTicketsWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	 * 
	 * Vector<Object> vector = new Vector<Object>(); String sql=null;
	 * 
	 * System.out.println("fetchServiceTicketReq");
	 * 
	 * 
	 * try { //PopoverHelper popoverHelper = new PopoverHelper(); con =
	 * getConnection();
	 * 
	 * sql="SELECT STRefNo, StudentID, HostelID, BlockNo, RoomNo, ServiceID,Severity, Description, "
	 * +
	 * " RaisedBy, RaisedDateTime, Status, RequestType, ProposedRoomNo FROM ServiceTickets WHERE RequestType=?"
	 * ;
	 * 
	 * 
	 * if(serviceTicketsWrapper.stRefNo !=null &&
	 * !serviceTicketsWrapper.stRefNo.equals("")){
	 * 
	 * sql=sql + " AND STRefNo=?"; }
	 * 
	 * 
	 * PreparedStatement pstmt = con.prepareStatement(sql);
	 * 
	 * 
	 * 
	 * //pstmt.setString(1,Utility.trim(serviceTicketsWrapper.studentID));
	 * pstmt.setString(1,Utility.trim(serviceTicketsWrapper.reqType));
	 * 
	 * if(serviceTicketsWrapper.stRefNo !=null &&
	 * !serviceTicketsWrapper.stRefNo.equals("")){
	 * 
	 * pstmt.setString(2,Utility.trim(serviceTicketsWrapper.stRefNo)); }
	 * 
	 * resultSet = pstmt.executeQuery();
	 * 
	 * while (resultSet.next()) {
	 * 
	 * serviceTicketsWrapper=new ServiceTicketsWrapper();
	 * 
	 * serviceTicketsWrapper.stRefNo=Utility.trim(resultSet.getString("STRefNo"));
	 * serviceTicketsWrapper.studentID=Utility.trim(resultSet.getString("StudentID")
	 * );
	 * serviceTicketsWrapper.hostelID=Utility.trim(resultSet.getString("HostelID"));
	 * serviceTicketsWrapper.blockNo=Utility.trim(resultSet.getString("BlockNo"));
	 * serviceTicketsWrapper.roomNo=Utility.trim(resultSet.getString("RoomNo"));
	 * serviceTicketsWrapper.serviceID=Utility.trim(resultSet.getString("ServiceID")
	 * );
	 * serviceTicketsWrapper.severity=Utility.trim(resultSet.getString("Severity"));
	 * serviceTicketsWrapper.desc=Utility.trim(resultSet.getString("Description"));
	 * serviceTicketsWrapper.raisedDateTime=Utility.setDate(resultSet.getString(
	 * "RaisedDateTime"));
	 * serviceTicketsWrapper.raisedBy=Utility.trim(resultSet.getString("RaisedBy"));
	 * serviceTicketsWrapper.status=Utility.trim(resultSet.getString("Status"));
	 * serviceTicketsWrapper.reqType=Utility.trim(resultSet.getString("RequestType")
	 * ); serviceTicketsWrapper.proposedRoomNo=Utility.trim(resultSet.getString(
	 * "ProposedRoomNo")); serviceTicketsWrapper.recordFound=true;
	 * 
	 * vector.addElement(serviceTicketsWrapper);
	 * //serviceTicketsWrapper.mailOptionValue=popoverHelper.fetchPopoverDesc(
	 * contactDetailsWrapper.mailOption, "MailOption"); }
	 * 
	 * if (vector.size()>0) { dataArrayWrapper.serviceTicketsWrapper = new
	 * ServiceTicketsWrapper[vector.size()];
	 * vector.copyInto(dataArrayWrapper.serviceTicketsWrapper);
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("total trn. in fetch " + vector.size());
	 * System.out.println("fetchServiceTicketReq successful");
	 * 
	 * } else{
	 * 
	 * dataArrayWrapper.serviceTicketsWrapper = new ServiceTicketsWrapper[1];
	 * dataArrayWrapper.serviceTicketsWrapper[0]=serviceTicketsWrapper;
	 * dataArrayWrapper.recordFound=true; }
	 * 
	 * if (resultSet!=null) resultSet.close(); pstmt.close();
	 * 
	 * } catch (SQLException se) {
	 * 
	 * se.printStackTrace(); throw new SQLException(se.getSQLState()+ " ; "+
	 * se.getMessage());
	 * 
	 * } catch (NamingException ne) {
	 * 
	 * ne.printStackTrace(); throw new NamingException(ne.getMessage()); } catch
	 * (Exception ex) {
	 * 
	 * ex.printStackTrace(); throw new Exception(ex.getMessage()); }
	 * 
	 * finally { try { releaseConnection(resultSet, con); } catch (SQLException se)
	 * { se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
	 * se.getMessage()); } }
	 * 
	 * return dataArrayWrapper; } //-----------------End
	 * fetchUsersProfile---------------------
	 */
}