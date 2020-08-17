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
import com.rootmind.wrapper.BookCatalogueWrapper;
import com.rootmind.wrapper.BookIssueWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class BookIssueHelper extends Helper {

	// -----------------Start insertBookIssue---------------------

	public AbstractWrapper insertBookIssue(UsersWrapper usersProfileWrapper, BookIssueWrapper bookIssueWrapper)
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

			sql = " INSERT INTO BookIssue(IssueRefNo, StudentID, BookID, IssueDate, ReturnDate, DamageFine, "
					+ " LateFine, TotalFine, DueDays, BookStatus, MakerID, MakerDateTime, SchoolID)"
					+ " Values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);
			bookIssueWrapper.issueRefNo = generateIssueRefNo(usersProfileWrapper.schoolID);

			pstmt.setString(1, Utility.trim(bookIssueWrapper.issueRefNo));
			pstmt.setString(2, Utility.trim(bookIssueWrapper.studentID));
			pstmt.setString(3, Utility.trim(bookIssueWrapper.bookID));
			pstmt.setDate(4, Utility.getDate(bookIssueWrapper.issueDate));
			pstmt.setDate(5, Utility.getDate(bookIssueWrapper.returnDate));
			pstmt.setString(6, Utility.trim(bookIssueWrapper.damageFine));
			pstmt.setString(7, Utility.trim(bookIssueWrapper.lateFine));
			pstmt.setString(8, Utility.trim(bookIssueWrapper.totalFine));
			pstmt.setString(9, Utility.trim(bookIssueWrapper.dueDays));
			pstmt.setString(10, Utility.trim(bookIssueWrapper.bookStatus));
			pstmt.setString(11, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(12, Utility.getCurrentTime()); // maker date time
			pstmt.setString(13, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();

			bookIssueWrapper.recordFound = true;

			dataArrayWrapper.bookIssueWrapper = new BookIssueWrapper[1];
			dataArrayWrapper.bookIssueWrapper[0] = bookIssueWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into BookIssue");

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

	// -----------------End insertBookIssue---------------------

	// -----------------Start updateBookIssue---------------------
	public AbstractWrapper updateBookIssue(UsersWrapper usersProfileWrapper, BookIssueWrapper bookIssueWrapper)
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

			pstmt = con.prepareStatement("SELECT IssueRefNo FROM BookIssue WHERE IssueRefNo=? and SchoolID=?");

			System.out.println("Update issue Details BookRefNo is " + bookIssueWrapper.issueRefNo);

			pstmt.setString(1, bookIssueWrapper.issueRefNo); // --it may null
			pstmt.setString(2, usersProfileWrapper.schoolID); // --it may null

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertBookIssue(usersProfileWrapper, bookIssueWrapper);
			} else {
				resultSet.close();
				pstmt.close();
				pstmt = con.prepareStatement("UPDATE BookIssue SET StudentID=?, BookID=?, "
						+ " IssueDate=?, ReturnDate=?, DamageFine=?, LateFine=?, TotalFine=?, DueDays=?, "
						+ " BookStatus=?, ModifierID=?, ModifierDateTime=?  WHERE IssueRefNo=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(bookIssueWrapper.studentID));
				pstmt.setString(2, Utility.trim(bookIssueWrapper.bookID));
				pstmt.setDate(3, Utility.getDate(bookIssueWrapper.issueDate));
				pstmt.setDate(4, Utility.getDate(bookIssueWrapper.returnDate));
				pstmt.setString(5, Utility.trim(bookIssueWrapper.damageFine));
				pstmt.setString(6, Utility.trim(bookIssueWrapper.lateFine));
				pstmt.setString(7, Utility.trim(bookIssueWrapper.totalFine));
				pstmt.setString(8, Utility.trim(bookIssueWrapper.dueDays));
				pstmt.setString(9, Utility.trim(bookIssueWrapper.bookStatus));
				pstmt.setString(10, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(11, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(12, Utility.trim(bookIssueWrapper.issueRefNo));
				pstmt.setString(13, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				bookIssueWrapper.recordFound = true;
				dataArrayWrapper.bookIssueWrapper = new BookIssueWrapper[1];
				dataArrayWrapper.bookIssueWrapper[0] = bookIssueWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Book Issue details Updated");
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
	// -----------------End updateBookDetails--------------------

	// -----------------Start fetchBookIssue---------------------

	public AbstractWrapper fetchBookIssue(UsersWrapper usersProfileWrapper, BookIssueWrapper bookIssueWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		String sql = null;

		try {

			PopoverHelper popoverHelper=new PopoverHelper();
			con = getConnection();

			sql = "SELECT IssueRefNo, StudentID, BookID, IssueDate, ReturnDate, DamageFine, "
					+ " LateFine, TotalFine, DueDays, BookStatus, MakerID, MakerDateTime,"
					+ " ModifierID, ModifierDateTime, SchoolID FROM BookIssue WHERE SchoolID=? ";
			
			if(!Utility.isEmpty(bookIssueWrapper.issueRefNo))
			{
				sql = sql + " and (IssueRefNo LIKE ? OR StudentID LIKE ? OR BookID LIKE ?)";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			if(!Utility.isEmpty(bookIssueWrapper.issueRefNo))
			{
				pstmt.setString(2, '%' + Utility.trim(bookIssueWrapper.issueRefNo) + '%');
				pstmt.setString(3, '%' + Utility.trim(bookIssueWrapper.issueRefNo) + '%');
				pstmt.setString(4, '%' + Utility.trim(bookIssueWrapper.issueRefNo) + '%');
				
			}
			
			
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				bookIssueWrapper = new BookIssueWrapper();

				bookIssueWrapper.issueRefNo = Utility.trim(resultSet.getString("IssueRefNo"));
				bookIssueWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				bookIssueWrapper.bookID = Utility.trim(resultSet.getString("BookID"));
				bookIssueWrapper.issueDate = Utility.setDate(resultSet.getString("IssueDate"));
				bookIssueWrapper.returnDate = Utility.setDate(resultSet.getString("ReturnDate"));
				bookIssueWrapper.damageFine = Utility.trim(resultSet.getString("DamageFine"));
				bookIssueWrapper.lateFine = Utility.trim(resultSet.getString("LateFine"));
				bookIssueWrapper.totalFine = Utility.trim(resultSet.getString("TotalFine"));
				bookIssueWrapper.dueDays = Utility.trim(resultSet.getString("DueDays"));
				bookIssueWrapper.bookStatus = Utility.trim(resultSet.getString("BookStatus"));
				bookIssueWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				bookIssueWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				bookIssueWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				bookIssueWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				bookIssueWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				
				
				bookIssueWrapper.bookStatusValue = popoverHelper.fetchPopoverDesc(bookIssueWrapper.bookStatus,
						"MST_BookStatus", bookIssueWrapper.schoolID);

				bookIssueWrapper.recordFound = true;

				vector.addElement(bookIssueWrapper);
				
				System.out.println("fetchBookIssue fetch successful");

			}

			
			if (vector.size() > 0) {
				
				dataArrayWrapper.bookIssueWrapper = new BookIssueWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.bookIssueWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}
			else
			{
				bookIssueWrapper.recordFound = false;
				dataArrayWrapper.bookIssueWrapper = new BookIssueWrapper[1];
				dataArrayWrapper.bookIssueWrapper[0]=bookIssueWrapper;
				dataArrayWrapper.recordFound=true;

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
	// -----------------End fetchBookIssue---------------------

	// -----------------generatIssueRefNo-------------------------------
	public String generateIssueRefNo(String schoolID) throws Exception {

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

		int issueRefNo = 0;
		String finalIssueRefNo = null;
		String issueCode = null;

		try {

			con = getConnection();

			sql = "SELECT IssueRefNo,IssueCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				issueRefNo = resultSet.getInt("IssueRefNo");
				System.out.println("IssueRefNo " + issueRefNo);
				issueCode = resultSet.getString("IssueCode");

			}

			resultSet.close();
			pstmt.close();

			if (issueRefNo == 0) {
				issueRefNo = 1;

			} else {

				issueRefNo = issueRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set IssueRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, issueRefNo);
			pstmt.setString(2, schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			finalIssueRefNo = issueCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", issueRefNo);

			System.out.println("Successfully generated IssueRefNo " + finalIssueRefNo);

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

		return finalIssueRefNo;
	}

	// -----------------End generateIssueRefNo---------------------------

}
