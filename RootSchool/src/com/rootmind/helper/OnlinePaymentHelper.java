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
import com.rootmind.wrapper.OnlinePaymentWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class OnlinePaymentHelper extends Helper {

	// -----------------Start insertOnlinePayment---------------------

	public AbstractWrapper insertOnlinePayment(UsersWrapper usersProfileWrapper,
			OnlinePaymentWrapper onlinePaymentWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;

		System.out.println("insertOnlinePayment");

		try {

			con = getConnection();

			// ----------

			sql = " INSERT INTO OnlinePayment(PaymentRefNo, RefNo, StudentID, GradeID, SectionID, PaymentType, "
					+ "BankName, CardType, CardIssuer, CardNo, NameOnCard, CVV, ExpiryMonth, ExpiryYear, Amount, "
					+ " MakerID, MakerDateTime,Status, SchoolID)" + " Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			onlinePaymentWrapper.paymentRefNo = generateRefNo(usersProfileWrapper.schoolID);

			pstmt.setString(1, Utility.trim(onlinePaymentWrapper.paymentRefNo));
			pstmt.setString(2, Utility.trim(onlinePaymentWrapper.refNo));
			pstmt.setString(3, Utility.trim(onlinePaymentWrapper.studentID));
			pstmt.setString(4, Utility.trim(onlinePaymentWrapper.gradeID));
			pstmt.setString(5, Utility.trim(onlinePaymentWrapper.sectionID));
			pstmt.setString(6, Utility.trim(onlinePaymentWrapper.paymentType));
			pstmt.setString(7, Utility.trim(onlinePaymentWrapper.bankName));
			pstmt.setString(8, Utility.trim(onlinePaymentWrapper.cardType));
			pstmt.setString(9, Utility.trim(onlinePaymentWrapper.cardIssuer));
			pstmt.setString(10, Utility.trim(onlinePaymentWrapper.cardNo));
			pstmt.setString(11, Utility.trim(onlinePaymentWrapper.nameOnCard));

			pstmt.setString(12, Utility.trim(onlinePaymentWrapper.cvv));
			pstmt.setString(13, Utility.trim(onlinePaymentWrapper.expiryMonth));
			pstmt.setString(14, Utility.trim(onlinePaymentWrapper.expiryYear));
			pstmt.setString(15, Utility.trim(onlinePaymentWrapper.amount));

			pstmt.setString(16, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(17, Utility.getCurrentTime()); // maker date time
			pstmt.setString(18, "Success");
			pstmt.setString(19, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			onlinePaymentWrapper.recordFound = true;

			dataArrayWrapper.onlinePaymentWrapper = new OnlinePaymentWrapper[1];
			dataArrayWrapper.onlinePaymentWrapper[0] = onlinePaymentWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into OnlinePayment");

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

	// -----------------End insertStudentProfile---------------------

	// -----------------Start fetchOnlinePayment---------------------

	public AbstractWrapper fetchOnlinePayment(UsersWrapper usersProfileWrapper,
			OnlinePaymentWrapper onlinePaymentWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchOnlinePayment RefNo" + onlinePaymentWrapper.refNo);

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;

		//int queueMaxRecords = 0;
		// int mQueueMaxRecords=0;

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

//			// -------- Fetch QueueMaxRecords Parameter
//			pstmt = con.prepareStatement("SELECT QueueMaxRecords FROM MST_Parameter where SchoolID=?");
//
//			pstmt.setString(1, usersProfileWrapper.schoolID);
//
//			resultSet = pstmt.executeQuery();
//			if (resultSet.next()) {
//				queueMaxRecords = resultSet.getInt("QueueMaxRecords");
//				// mQueueMaxRecords=resultSet.getInt("MQueueMaxRecords");
//				System.out.println(" queueMaxRecords " + queueMaxRecords);
//
//			}
//
//			resultSet.close();
//			pstmt.close();

			// -------

			sql = "SELECT PaymentRefNo, RefNo, StudentID, GradeID, SectionID, PaymentType, BankName, CardType, "
					+ "CardIssuer, CardNo, NameOnCard, CVV, ExpiryMonth, ExpiryYear, "
					+ "Amount, MakerID, MakerDateTime,Status, SchoolID FROM OnlinePayment WHERE SchoolID=? ";

			if (!Utility.isEmpty(onlinePaymentWrapper.refNo)) {
				sql = sql + " AND RefNo=? ";
			}
			if (!Utility.isEmpty(onlinePaymentWrapper.paymentRefNo)) {
				sql = sql + " AND PaymentRefNo=? ";
			}

			sql = sql + " ORDER BY MakerDateTime DESC"; // LIMIT " + queueMaxRecords;

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			
			if (!Utility.isEmpty(onlinePaymentWrapper.refNo)) {
				pstmt.setString(2, Utility.trim(onlinePaymentWrapper.refNo));

			}
			if (!Utility.isEmpty(onlinePaymentWrapper.paymentRefNo)) {
				
				pstmt.setString(2, Utility.trim(onlinePaymentWrapper.paymentRefNo));

			}
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				onlinePaymentWrapper = new OnlinePaymentWrapper();

				onlinePaymentWrapper.paymentRefNo = Utility.trim(resultSet.getString("PaymentRefNo"));
				onlinePaymentWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				onlinePaymentWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				onlinePaymentWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				onlinePaymentWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				onlinePaymentWrapper.paymentType = Utility.trim(resultSet.getString("PaymentType"));
				onlinePaymentWrapper.bankName = Utility.trim(resultSet.getString("BankName"));
				onlinePaymentWrapper.cardType = Utility.trim(resultSet.getString("CardType"));
				onlinePaymentWrapper.cardIssuer = Utility.trim(resultSet.getString("CardIssuer"));
				onlinePaymentWrapper.cardNo = Utility.trim(resultSet.getString("CardNo"));
				onlinePaymentWrapper.nameOnCard = Utility.trim(resultSet.getString("NameOnCard"));
				onlinePaymentWrapper.cvv = Utility.trim(resultSet.getString("CVV"));
				onlinePaymentWrapper.expiryMonth = Utility.trim(resultSet.getString("ExpiryMonth"));

				onlinePaymentWrapper.expiryYear = Utility.trim(resultSet.getString("ExpiryYear"));
				onlinePaymentWrapper.amount = Utility.trim(resultSet.getString("Amount"));

				onlinePaymentWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				onlinePaymentWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));

				onlinePaymentWrapper.makerDateTimeAMPM = Utility.setDateAMPM(resultSet.getString("MakerDateTime"));

				onlinePaymentWrapper.status = Utility.trim(resultSet.getString("Status"));

				onlinePaymentWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

//				onlinePaymentWrapper.bankNameValue = popoverHelper.fetchPopoverDesc(onlinePaymentWrapper.bankName,
//						"MST_Bank", onlinePaymentWrapper.schoolID);
//				onlinePaymentWrapper.expiryMonthValue = popoverHelper.fetchPopoverDesc(onlinePaymentWrapper.bankName,
//						"MST_Months", onlinePaymentWrapper.schoolID);
//				onlinePaymentWrapper.expiryYearValue = popoverHelper.fetchPopoverDesc(onlinePaymentWrapper.bankName,
//						"MST_Years", onlinePaymentWrapper.schoolID);

				onlinePaymentWrapper.recordFound = true;
				System.out.println("fetchOnlinePayment Details fetch successful");

				vector.addElement(onlinePaymentWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.onlinePaymentWrapper = new OnlinePaymentWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.onlinePaymentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

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
	// -----------------End fetchStudentProfile---------------------

	// -----------------Generate PaymentRefNo-------------------------------
	public String generateRefNo(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("ddMMMyyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		int refNo = 0;
		String finalRefNo = null;
		//String schoolCode = null;

		try {
			con = getConnection();

			sql = "SELECT RefNo,SchoolCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				refNo = resultSet.getInt("RefNo");
				System.out.println("RefNo" + refNo);
				//schoolCode = resultSet.getString("SchoolCode");

			}

			resultSet.close();
			pstmt.close();

			if (refNo == 0) {
				refNo = 1;

			} else {

				refNo = refNo + 1;
			}

			sql = "UPDATE MST_Parameter set RefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, refNo);

			pstmt.setString(2, Utility.trim(schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			// int paddingSize=6-String.valueOf(refNo).length();

			// System.out.println("Savings Account " + onlinePaymentWrapper.accountType);

			// System.out.println("Savings Account " +
			// onlinePaymentWrapper.accountType.substring(0,2));

			finalRefNo = "ONLP" + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", refNo);

			// onlinePaymentWrapper.recordFound=true;

			// dataArrayWrapper.onlinePaymentWrapper=new onlinePaymentWrapper[1];
			// dataArrayWrapper.onlinePaymentWrapper[0]=onlinePaymentWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated OnlinePayment refno " + finalRefNo);

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

		return finalRefNo;
	}

	// -----------------End Generate RefNo---------------------------

}
