package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.TransportWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class TransportHelper extends Helper {

	public AbstractWrapper insertTransport(UsersWrapper usersProfileWrapper, TransportWrapper transportWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		String sql = null;

		try {

			con = getConnection();

			sql = "INSERT INTO Transport(TSRefNo, StudentID, HostelID, BlockNo, RoomNo, Reason, Destination, NoOfPersons, JourneyDateTime, ReturnDateTime, AllocationDate,"
					+ "Description,  RecordStatus, RaisedBy, RaisedDateTime, TripType, SchoolID) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			transportWrapper.tsRefNo = generateTSRefNo(usersProfileWrapper.schoolID);

			System.out.println(" StudentID " + Utility.trim(transportWrapper.studentID));
			System.out.println("HostelID  " + Utility.trim(transportWrapper.hostelID));

			pstmt.setString(1, Utility.trim(transportWrapper.tsRefNo)); // TransportService Ref Number
			pstmt.setString(2, Utility.trim(transportWrapper.studentID));
			pstmt.setString(3, Utility.trim(transportWrapper.hostelID));
			pstmt.setString(4, Utility.trim(transportWrapper.blockNo));
			pstmt.setString(5, Utility.trim(transportWrapper.roomNo));
			pstmt.setString(6, Utility.trim(transportWrapper.reason));
			pstmt.setString(7, Utility.trim(transportWrapper.destination));
			pstmt.setString(8, Utility.trim(transportWrapper.noOfPersons));
			pstmt.setDate(9, Utility.getDate(transportWrapper.journeyDateTime));
			pstmt.setDate(10, Utility.getDate(transportWrapper.returnDateTime));
			pstmt.setDate(11, Utility.getDate(transportWrapper.allocationDate));
			pstmt.setString(12, Utility.trim(transportWrapper.desc));

			pstmt.setString(13, Utility.trim(transportWrapper.recordStatus));
			pstmt.setString(14, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(15, Utility.getCurrentTime());
			/*
			 * pstmt.setString(16,Utility.trim(transportWrapper.supervisorID));
			 * pstmt.setDate(17,Utility.getDate(transportWrapper.supervisorDateTime));
			 */
			System.out.println(" tripType " + Utility.trim(transportWrapper.tripType));
			pstmt.setString(16, Utility.trim(transportWrapper.tripType));
			pstmt.setString(17, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			transportWrapper.recordFound = true;

			dataArrayWrapper.transportWrapper = new TransportWrapper[1];
			dataArrayWrapper.transportWrapper[0] = transportWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Transport inserted for StudentID " + transportWrapper.studentID);

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

	// ---------------End InsertTransportRequest-------------------------------

	// ---------------Start UpdateTransportRequest-------------------------------

	public AbstractWrapper updateTransport(UsersWrapper usersProfileWrapper, TransportWrapper transportWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		String sql = null;

		try {

			con = getConnection();

			System.out.println("update for tsRefNo " + Utility.trim(transportWrapper.tsRefNo));

			sql = "UPDATE Transport SET Reason=?, Destination=?, NoOfPersons=?, JourneyDateTime=?, ReturnDateTime=?, RaisedBy=?, RaisedDateTime=? "
					+ " WHERE  TSRefNo=? and SchoolID=?"; //StudentID=? AND //, RequestStatus=?

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(transportWrapper.reason));
			pstmt.setString(2, Utility.trim(transportWrapper.destination));
			pstmt.setString(3, Utility.trim(transportWrapper.noOfPersons));
			pstmt.setDate(4, Utility.getDate(transportWrapper.journeyDateTime));
			pstmt.setDate(5, Utility.getDate(transportWrapper.returnDateTime));
			pstmt.setString(6, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(7, Utility.getCurrentTime());
			//pstmt.setString(8, Utility.trim(transportWrapper.requestStatus));
			pstmt.setString(8, Utility.trim(transportWrapper.tsRefNo));
			pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));
			
			pstmt.executeUpdate();
			pstmt.close();

			transportWrapper.recordFound = true;

			dataArrayWrapper.transportWrapper = new TransportWrapper[1];
			dataArrayWrapper.transportWrapper[0] = transportWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("updateTransportRequest updated ");

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

	// ---------------End UpdateTransportRequest-------------------------------

	// -----------------Start fetchTransportRequest---------------------

	public AbstractWrapper fetchTransport(UsersWrapper usersProfileWrapper, TransportWrapper transportWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		String sql = null;
		//int n = 0;

		String hostelID = null;

		System.out.println("transportWrapper.studentID " + transportWrapper.studentID);

//		int queueMaxRecords = 0;
//		int mQueueMaxRecords = 0;
		PreparedStatement pstmt = null;
		int targetTAT = 0;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// -------- Fetch Target TAT From MST_Parameter
			pstmt = con.prepareStatement("SELECT  TargetTAT FROM MST_Parameter where SchoolID=?");

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));
			
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				targetTAT = resultSet.getInt("TargetTAT");
				transportWrapper.targetTAT = Utility.getTimeformat(targetTAT);

				System.out.println(" transportWrapper.targetTAT " + transportWrapper.targetTAT);

			}
			resultSet.close();
			pstmt.close();
			// -------

//			// --if user is not student then fetch Hostel ID's (user is supervisor)
//			if (usersProfileWrapper.student != null && !usersProfileWrapper.student.equals("Y")) {
//				UserGroupHelper userGroupHelper = new UserGroupHelper();
//				hostelID = userGroupHelper.fetchSupervisorHostels(usersProfileWrapper);
//
//			}

			System.out.println(" hostelID  " + hostelID);

//			// -------- Fetch QueueMaxRecords Parameter
//			pstmt = con.prepareStatement("SELECT QueueMaxRecords,MQueueMaxRecords FROM MST_Parameter");
//
//			resultSet = pstmt.executeQuery();
//			if (resultSet.next()) {
//				queueMaxRecords = resultSet.getInt("QueueMaxRecords");
//				mQueueMaxRecords = resultSet.getInt("MQueueMaxRecords");
//				System.out.println(" queueMaxRecords " + queueMaxRecords);
//
//			}
//
//			resultSet.close();
//			pstmt.close();

			// -------

//			sql = "SELECT a.TSRefNo, a.StudentID, a.HostelID, a.BlockNo, a.RoomNo, a.Reason, a.Destination, a.NoOfPersons, a.JourneyDateTime, a.ReturnDateTime,a.AllocationDate, "
//					+ "a.Description, a.RequestStatus, a.RecordStatus, a.RaisedBy, a.RaisedDateTime, a.SupervisorID, a.SupervisorDateTime, a.TripType, "
//					+ " b.StudentName FROM Transport a LEFT JOIN StudentProfile b ON a.StudentID=b.StudentID WHERE 1=1";

			sql = "SELECT TSRefNo, StudentID, HostelID, BlockNo, RoomNo, Reason, Destination, NoOfPersons, JourneyDateTime, ReturnDateTime,AllocationDate, "
					+ "Description, RequestStatus, RecordStatus, RaisedBy, RaisedDateTime, SupervisorID, SupervisorDateTime, TripType "
					+ " FROM Transport WHERE SchoolID=? "; 

			
//			// it is for student
//			if (transportWrapper.studentID != null && !transportWrapper.studentID.equals("")) {
//				sql = sql + "  AND StudentID=?";
//			}
			
			// it is for supervisor
			if (!Utility.isEmpty(transportWrapper.tsRefNo)) {
				sql = sql + "  AND TSRefNo=?";
			}

			//filter for hostel records
			if (Utility.isEmpty(transportWrapper.hostelID)) {
				sql = sql + "  AND HostelID IS NULL ";
			}
			else
			{
				sql = sql + "  AND HostelID IS NOT NULL ";
			}

//			if (transportWrapper.studentName != null && !transportWrapper.studentName.trim().isEmpty()) {
//
//				sql = sql + " AND (UPPER(StudentName) LIKE ? OR UPPER(SurName) LIKE ?)";
//
//				System.out.println("fetchTransport studentName " + transportWrapper.studentName);
//
//			}
//
//			else if (transportWrapper.searchStartDate != null && !transportWrapper.searchStartDate.trim().isEmpty()) {
//
//				sql = sql + " AND RaisedDateTime >= ?";
//
//			}
//
//			else if (transportWrapper.searchEndDate != null && !transportWrapper.searchEndDate.trim().isEmpty()) {
//
//				sql = sql + " AND RaisedDateTime <= ?";
//
//			}
//
//			if (transportWrapper.hostelID != null && !transportWrapper.hostelID.trim().isEmpty()) {
//				sql = sql + " AND HostelID = ? ";
//			}
//
//			// ---user is supervisor then
//			if (usersProfileWrapper.student != null && !usersProfileWrapper.student.equals("Y")) {
//				sql = sql + " AND HostelID IN " + hostelID; // ('HST001','HST002');
//			}
//
//			if (transportWrapper.searchCode != null && transportWrapper.searchCode.equals("SUPERVISOR_MAINT_QUEUE")) {
//
//				// STS003 - Closed
//				// Mysql
//				// sql=sql + " AND a.RecordStatus NOT IN ('STS003') ORDER BY RaisedDateTime DESC
//				// LIMIT " + queueMaxRecords;
//
//				// Oracle
//				sql = sql + " AND a.RecordStatus NOT IN ('STS003')" + " AND ROWNUM<= " + queueMaxRecords
//						+ " ORDER BY RaisedDateTime DESC ";
//
//			}
//			if (transportWrapper.searchCode != null && transportWrapper.searchCode.equals("STUDENT_DEVICE_QUEUE")) {
//
//				// Mysql
//				// sql=sql + " ORDER BY RaisedDateTime DESC LIMIT " + mQueueMaxRecords;
//
//				// Oracle
//				sql = sql + " AND ROWNUM<= " + mQueueMaxRecords + " ORDER BY RaisedDateTime DESC ";
//			}
//
//			if (transportWrapper.searchCode != null && transportWrapper.searchCode.equals("TRANSPORT_SEARCH")) {
//
//				// Mysql
//				// sql=sql + " ORDER BY RaisedDateTime DESC LIMIT " + queueMaxRecords;
//
//				// Oracle
//				sql = sql + " AND ROWNUM<= " + queueMaxRecords + " ORDER BY RaisedDateTime DESC ";
//
//			}

			System.out.println("final SQL " + sql);
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			
//			if (transportWrapper.studentID != null && !transportWrapper.studentID.equals("")) {
//				n = n + 1;
//				pstmt.setString(n, Utility.trim(transportWrapper.studentID));
//			}
			
			if (!Utility.isEmpty(transportWrapper.tsRefNo)) {

				pstmt.setString(2, Utility.trim(transportWrapper.tsRefNo));
			}
//			if (transportWrapper.studentName != null && !transportWrapper.studentName.equals("")) {
//				pstmt.setString(++n, Utility.trim(transportWrapper.studentName));
//				pstmt.setString(++n, Utility.trim(transportWrapper.studentName));
//			}
//
//			if (transportWrapper.searchStartDate != null && !transportWrapper.searchStartDate.equals("")) {
//				pstmt.setDate(++n, Utility.getDate(transportWrapper.searchStartDate.trim()));
//			}
//
//			if (transportWrapper.searchEndDate != null && !transportWrapper.searchEndDate.equals("")) {
//				pstmt.setDate(++n, Utility.getDate(transportWrapper.searchEndDate.trim()));
//			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				transportWrapper = new TransportWrapper();

				transportWrapper.tsRefNo = Utility.trim(resultSet.getString("TSRefNo"));
				transportWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				transportWrapper.studentName = ""; //Utility.trim(resultSet.getString("StudentName"));
				transportWrapper.hostelID = Utility.trim(resultSet.getString("HostelID"));
				transportWrapper.blockNo = Utility.trim(resultSet.getString("BlockNo"));
				transportWrapper.roomNo = Utility.trim(resultSet.getString("RoomNo"));
				transportWrapper.reason = Utility.trim(resultSet.getString("Reason"));
				transportWrapper.destination = Utility.trim(resultSet.getString("Destination"));
				transportWrapper.noOfPersons = Utility.trim(resultSet.getString("NoOfPersons"));
				transportWrapper.journeyDateTime = Utility.setDate(resultSet.getString("JourneyDateTime"));
				transportWrapper.returnDateTime = Utility.setDate(resultSet.getString("ReturnDateTime"));
				transportWrapper.allocationDate = Utility.setDate(resultSet.getString("AllocationDate"));
				transportWrapper.desc = Utility.trim(resultSet.getString("Description"));
				transportWrapper.requestStatus = Utility.trim(resultSet.getString("RequestStatus"));
				transportWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));
				transportWrapper.raisedBy = Utility.trim(resultSet.getString("RaisedBy"));
				transportWrapper.raisedDateTime = resultSet.getString("RaisedDateTime");
				transportWrapper.supervisorID = Utility.trim(resultSet.getString("SupervisorID"));
				transportWrapper.supervisorDateTime = Utility.setDateAMPM(resultSet.getString("supervisorDateTime"));

				transportWrapper.tripType = Utility.trim(resultSet.getString("TripType"));

				transportWrapper.journeyDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("JourneyDateTime"));
				transportWrapper.returnDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("ReturnDateTime"));
				transportWrapper.allocationDateAMPM = Utility.setDateAMPM(resultSet.getString("AllocationDate"));
				transportWrapper.raisedDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("RaisedDateTime"));

				transportWrapper.recordStatusValue = popoverHelper.fetchPopoverDesc(transportWrapper.recordStatus,
						"MST_Status", usersProfileWrapper.schoolID);
				
				transportWrapper.requestStatusValue = popoverHelper.fetchPopoverDesc(transportWrapper.requestStatus,
						"MST_TransportStatus", usersProfileWrapper.schoolID);

				transportWrapper.tripTypeValue = popoverHelper.fetchPopoverDesc(transportWrapper.tripType,
						"MST_TripType", usersProfileWrapper.schoolID);

				transportWrapper.hostelIDValue = popoverHelper.fetchPopoverDesc(transportWrapper.hostelID,
						"MST_Hostel", usersProfileWrapper.schoolID);

				
				// --tat calculation
				transportWrapper.tat = Utility
						.getTimeformat(Utility.timeDifference(transportWrapper.raisedDateTime, "M"));
				transportWrapper.tatColor = Utility
						.getTATColor(Utility.timeDifference(transportWrapper.raisedDateTime, "M"), targetTAT);
				System.out.println("transportWrapper.tat " + transportWrapper.tat);

				transportWrapper.recordFound = true;

				vector.addElement(transportWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.transportWrapper = new TransportWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.transportWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());
				System.out.println("fetchTransport successful");

			} else {

				dataArrayWrapper.transportWrapper = new TransportWrapper[1];
				transportWrapper.recordFound = false;
				dataArrayWrapper.transportWrapper[0] = transportWrapper;
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

	// -----------------Generate generateSTRefNo-------------------------------
	public String generateTSRefNo(String schoolID) throws Exception {

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

		int tsRefNo = 0; // transportRefNo
		String finalTSRefNo = null;

		// String serviceTicketCode=null;
		String transportCode = "TRS";

		try {
			con = getConnection();

			sql = "SELECT ServiceTicketRefNo from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				tsRefNo = resultSet.getInt("ServiceTicketRefNo");
				System.out.println("ServiceTicketRefNo " + tsRefNo);
				// serviceTicketCode=resultSet.getString("ServiceTicketCode");

			}

			resultSet.close();
			pstmt.close();

			if (tsRefNo == 0) {
				tsRefNo = 1;

			} else {

				tsRefNo = tsRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set ServiceTicketRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, tsRefNo);
			pstmt.setString(2, Utility.trim(schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			finalTSRefNo = transportCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", tsRefNo);

			System.out.println("Successfully generated finalTSRefNo  " + finalTSRefNo);

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

		return finalTSRefNo;
	}

	// -----------------End generateSTRefNo ---------------------------

	// ---------------Start UpdateTransportResp-------------------------------

	public AbstractWrapper updateTransportResponse(UsersWrapper usersProfileWrapper, TransportWrapper transportWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		String sql = null;

		try {

			con = getConnection();

			System.out.println("UpdateTransport for tsRefNo " + Utility.trim(transportWrapper.tsRefNo));

			sql = "UPDATE Transport SET  Description=?, AllocationDate=?, RequestStatus=?, RecordStatus=?, SupervisorID=?, SupervisorDateTime=? "
					+ " WHERE TSRefNo=? and SchoolID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(transportWrapper.desc));
			pstmt.setDate(2, Utility.getDate(transportWrapper.allocationDate));
			pstmt.setString(3, Utility.trim(transportWrapper.requestStatus));
			pstmt.setString(4, Utility.trim(transportWrapper.recordStatus));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(6, Utility.getCurrentTime());
			pstmt.setString(7, Utility.trim(transportWrapper.tsRefNo));
			pstmt.setString(8, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			transportWrapper.recordFound = true;

			dataArrayWrapper.transportWrapper = new TransportWrapper[1];
			dataArrayWrapper.transportWrapper[0] = transportWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("UpdateTransportResp updated ");

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

	// ---------------End UpdateTransportResp-------------------------------

}
