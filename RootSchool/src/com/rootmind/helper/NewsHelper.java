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
import com.rootmind.wrapper.NewsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class NewsHelper extends Helper {

	// ----------------------- Start insertNews-------------------

	public AbstractWrapper insertNews(UsersWrapper usersProfileWrapper, NewsWrapper newsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		try {
			con = getConnection();

			sql = " INSERT INTO News(NewsID, News, NewsDate, StartDate, EndDate, RecordStatus, MakerID, MakerDateTime, SchoolID) Values(?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			PreparedStatement pstmt = con.prepareStatement(sql);

			newsWrapper.newsID = generateNewsID();

			pstmt.setString(1, Utility.trim(newsWrapper.newsID));
			pstmt.setString(2, Utility.trim(newsWrapper.news));
			pstmt.setTimestamp(3, Utility.getCurrentTime());
			pstmt.setDate(4, Utility.getDate(newsWrapper.startDate));
			pstmt.setDate(5, Utility.getDate(newsWrapper.endDate));
			pstmt.setString(6, Utility.trim(newsWrapper.status));
			pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(8, Utility.getCurrentTime());
			pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			newsWrapper.recordFound = true;

			dataArrayWrapper.newsWrapper = new NewsWrapper[1];
			dataArrayWrapper.newsWrapper[0] = newsWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into News");

			/*
			 * GCMNotification gcmNotification = new GCMNotification("NEWS");
			 * gcmNotification.start();
			 * 
			 * System.out.println("GCM notification NEWS started");
			 */

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
	// ----------------------- End insertNews-------------------

	/*
	 * //----------------------- Start updateSchoolMessage------------------- public
	 * AbstractWrapper updateSchoolMessage(SchoolMessageWrapper
	 * schoolMessageWrapper)throws Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper = new DataArrayWrapper(); //String
	 * sql=null;
	 * 
	 * //SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	 * 
	 * DecimalFormat formatter = (DecimalFormat)
	 * NumberFormat.getInstance(Locale.US); DecimalFormatSymbols symbols =
	 * formatter.getDecimalFormatSymbols(); symbols.setGroupingSeparator(',');
	 * formatter.applyPattern("###,###,###,##0.00");
	 * formatter.setDecimalFormatSymbols(symbols); PreparedStatement pstmt=null;
	 * 
	 * 
	 * 
	 * try { con = getConnection();
	 * 
	 * pstmt = con.
	 * prepareStatement("UPDATE SchoolMessage SET Message=?, MessageDateTime=?, Delivered=?, GradeList=?, MakerID=?, MakerDateTime=? WHERE MessageID=?"
	 * );
	 * 
	 * 
	 * pstmt.setString(1,Utility.trim(schoolMessageWrapper.message));
	 * pstmt.setTimestamp(2,Utility.getCurrentTime());
	 * 
	 * pstmt.setString(3,Utility.trim(schoolMessageWrapper.delivered));
	 * pstmt.setString(4,Utility.trim(schoolMessageWrapper.gradeList));
	 * 
	 * pstmt.setString(5,Utility.trim(schoolMessageWrapper.makerID));
	 * pstmt.setTimestamp(6,Utility.getCurrentTime());
	 * 
	 * pstmt.setString(7,Utility.trim(schoolMessageWrapper.messageID));
	 * 
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * schoolMessageWrapper.recordFound=true;
	 * 
	 * dataArrayWrapper.schoolMessageWrapper=new SchoolMessageWrapper[1];
	 * dataArrayWrapper.schoolMessageWrapper[0]=schoolMessageWrapper;
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("Successfully School Message Updated");
	 * 
	 * GCMNotification gcmNotification = new GCMNotification("SCHOOL_MESSAGE");
	 * gcmNotification.start();
	 * 
	 * System.out.println("GCM notification started");
	 * 
	 * 
	 * 
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
	 * return dataArrayWrapper; } //----------------------- Start
	 * insertSchoolMessage-------------------
	 * 
	 */

	// ----------------------- Start fetchNews-------------------
	public AbstractWrapper fetchNews(UsersWrapper usersProfileWrapper, NewsWrapper newsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchNews NewsID" + newsWrapper.newsID);

		Vector<Object> vector = new Vector<Object>();

		String sql = null;

		PreparedStatement pstmt = null;

		int queueMaxRecords = 0;
		int mQueueMaxRecords = 0;

		int n = 0;

		try {

			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Queue Max Records--

			// -------- Fetch QueueMaxRecords Parameter
			pstmt = con.prepareStatement("SELECT QueueMaxRecords,MQueueMaxRecords FROM MST_Parameter");

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				queueMaxRecords = resultSet.getInt("QueueMaxRecords");
				mQueueMaxRecords = resultSet.getInt("MQueueMaxRecords");
				System.out.println(" queueMaxRecords " + queueMaxRecords);

			}

			resultSet.close();
			pstmt.close();

			// -------

			// ----------
			sql = "SELECT NewsID, News, NewsDate, StartDate, EndDate, RecordStatus, MakerID, "
					+ " MakerDateTime FROM News WHERE 1=1";

			if (newsWrapper.newsID != null && !newsWrapper.newsID.equals("")) {

				sql = sql + " AND NewsID=?";
			} else if (newsWrapper.searchStartDate != null && !newsWrapper.searchStartDate.trim().isEmpty()) {

				sql = sql + " AND StartDate >= ?";

			} else if (newsWrapper.searchEndDate != null && !newsWrapper.searchEndDate.trim().isEmpty()) {

				sql = sql + " AND EndDate <= ?";

			}
			if (newsWrapper.searchCode != null && newsWrapper.searchCode.equals("SUPERVISOR_MAINT_QUEUE")) {

				// mysql
				// sql=sql + " ORDER BY MakerDateTime DESC LIMIT " + queueMaxRecords;

				// Oracle
				sql = sql + " AND ROWNUM<= " + queueMaxRecords + " ORDER BY MakerDateTime DESC ";

			}

			if (newsWrapper.searchCode != null && newsWrapper.searchCode.equals("STUDENT_DEVICE_QUEUE")) {

				// MYSQL
				// sql=sql + " ORDER BY MakerDateTime DESC LIMIT " + mQueueMaxRecords;

				// Oracle
				sql = sql + " AND ROWNUM<= " + mQueueMaxRecords + " ORDER BY MakerDateTime DESC ";
			}

			if (newsWrapper.searchCode != null && newsWrapper.searchCode.equals("NEWS_SEARCH")) {
				// MYSQL
				// sql=sql + " ORDER BY MakerDateTime DESC LIMIT " + queueMaxRecords;

				// Oracle
				sql = sql + " AND ROWNUM<= " + queueMaxRecords + " ORDER BY MakerDateTime DESC ";

			}

			System.out.println("final SQL " + sql);

			pstmt = con.prepareStatement(sql);

			if (newsWrapper.newsID != null && !newsWrapper.newsID.equals("")) {
				pstmt.setString(++n, Utility.trim(newsWrapper.newsID));
			}

			if (newsWrapper.searchStartDate != null && !newsWrapper.searchStartDate.equals("")) {
				pstmt.setDate(++n, Utility.getDate(newsWrapper.searchStartDate.trim()));
			}

			if (newsWrapper.searchEndDate != null && !newsWrapper.searchEndDate.equals("")) {
				pstmt.setDate(++n, Utility.getDate(newsWrapper.searchEndDate.trim()));
			}

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				newsWrapper = new NewsWrapper();

				newsWrapper.newsID = Utility.trim(resultSet.getString("NewsID"));
				newsWrapper.news = Utility.trim(resultSet.getString("News"));
				newsWrapper.newsDate = Utility.setDate(resultSet.getString("NewsDate"));
				newsWrapper.startDate = Utility.setDate(resultSet.getString("StartDate"));
				newsWrapper.endDate = Utility.setDate(resultSet.getString("EndDate"));
				newsWrapper.status = Utility.trim(resultSet.getString("RecordStatus"));
				newsWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				newsWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				newsWrapper.recordFound = true;

				newsWrapper.statusValue = popoverHelper.fetchPopoverDesc(newsWrapper.status, "MST_Status", usersProfileWrapper.schoolID);
				newsWrapper.startDateAMPM = Utility.setDateAMPM(resultSet.getString("StartDate"));
				newsWrapper.endDateAMPM = Utility.setDateAMPM(resultSet.getString("EndDate"));
				newsWrapper.makerDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));

				System.out.println("News Details fetch successful");

				vector.addElement(newsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.newsWrapper = new NewsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.newsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else

			{
				dataArrayWrapper.newsWrapper = new NewsWrapper[1];
				dataArrayWrapper.newsWrapper[0] = newsWrapper;
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
	// ----------------------- End fetchNews-------------------

	// -----------------Generate NewsID-------------------------------
	public String generateNewsID() throws Exception {

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

		int newsID = 0;
		String finalNewsID = null;

		try {
			con = getConnection();

			sql = "SELECT ServiceTicketRefNo from MST_Parameter";

			PreparedStatement pstmt = con.prepareStatement(sql);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				newsID = resultSet.getInt("ServiceTicketRefNo");
				System.out.println("NewsID" + newsID);

			}

			resultSet.close();
			pstmt.close();

			if (newsID == 0) {
				newsID = 1;

			} else {

				newsID = newsID + 1;
			}

			sql = "UPDATE MST_Parameter set ServiceTicketRefNo=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, newsID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 5;// 6-String.valueOf(messageID).length();

			// System.out.println("Savings Account " + studentProfileWrapper.accountType);

			// System.out.println("Savings Account " +
			// studentProfileWrapper.accountType.substring(0,2));

			finalNewsID = "NEWS" + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", newsID);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated  newsID " + finalNewsID);

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

		return finalNewsID;
	}

	// -----------------End Generate MessageID---------------------------

}
