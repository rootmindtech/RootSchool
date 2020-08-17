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
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.BookCatalogueWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class BookCatalogueHelper extends Helper {

	// -----------------Start insertBook---------------------

	public AbstractWrapper insertBookDetails(UsersWrapper usersProfileWrapper,
			BookCatalogueWrapper bookCatalogueWrapper) throws Exception {

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

			sql = " INSERT INTO BookCatalog(BookRefNo, BookID, BookName, Author, Edition, Publisher, Price, YOP, BookCount, BookCategory, DOP, SchoolID, MakerID, MakerDateTime)"
					+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);
			bookCatalogueWrapper.bookRefNo = generateBookRefNo(usersProfileWrapper.schoolID);

			pstmt.setString(1, Utility.trim(bookCatalogueWrapper.bookRefNo));
			pstmt.setString(2, Utility.trim(bookCatalogueWrapper.bookID));
			pstmt.setString(3, Utility.trim(bookCatalogueWrapper.bookName));
			pstmt.setString(4, Utility.trim(bookCatalogueWrapper.author));
			pstmt.setString(5, Utility.trim(bookCatalogueWrapper.edition));
			pstmt.setString(6, Utility.trim(bookCatalogueWrapper.publisher));
			pstmt.setString(7, Utility.trim(bookCatalogueWrapper.price));
			pstmt.setString(8, Utility.trim(bookCatalogueWrapper.yop));
			pstmt.setString(9, Utility.trim(bookCatalogueWrapper.bookCount));
			pstmt.setString(10, Utility.trim(bookCatalogueWrapper.bookCategory));
			pstmt.setDate(11, Utility.getDate(bookCatalogueWrapper.dop));
			pstmt.setString(12, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(13, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(14, Utility.getCurrentTime()); // maker date time

			pstmt.executeUpdate();
			pstmt.close();

			bookCatalogueWrapper.recordFound = true;

			dataArrayWrapper.bookCatalogueWrapper = new BookCatalogueWrapper[1];
			dataArrayWrapper.bookCatalogueWrapper[0] = bookCatalogueWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into insertBook");

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

	// -----------------End insertBookDetails---------------------

	// -----------------Start updateBookDetails---------------------
	public AbstractWrapper updateBookDetails(UsersWrapper usersProfileWrapper,
			BookCatalogueWrapper bookCatalogueWrapper) throws Exception {

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

			pstmt = con.prepareStatement("SELECT BookRefNo FROM BookCatalog WHERE BookRefNo=? and SchoolID=?");

			System.out.println("Update Book Details BookRefNo is " + bookCatalogueWrapper.bookRefNo);

			pstmt.setString(1, bookCatalogueWrapper.bookRefNo); // --it may null
			pstmt.setString(2, usersProfileWrapper.schoolID); // --it may null

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertBookDetails(usersProfileWrapper, bookCatalogueWrapper);
			} else {
				resultSet.close();
				pstmt.close();
				pstmt = con.prepareStatement("UPDATE BookCatalog SET BookID=?, BookName=?, Author=?, Edition=?, "
						+ "Publisher=?, Price=?, YOP=?, BookCount=?, BookCategory=?, DOP=?, ModifierID=?, ModifierDateTime=?  WHERE BookRefNo=? and SchoolID=?");

				pstmt.setString(1, Utility.trim(bookCatalogueWrapper.bookID));
				pstmt.setString(2, Utility.trim(bookCatalogueWrapper.bookName));
				pstmt.setString(3, Utility.trim(bookCatalogueWrapper.author));
				pstmt.setString(4, Utility.trim(bookCatalogueWrapper.edition));
				pstmt.setString(5, Utility.trim(bookCatalogueWrapper.publisher));
				pstmt.setString(6, Utility.trim(bookCatalogueWrapper.price));
				pstmt.setString(7, Utility.trim(bookCatalogueWrapper.yop));
				pstmt.setString(8, Utility.trim(bookCatalogueWrapper.bookCount));
				pstmt.setString(9, Utility.trim(bookCatalogueWrapper.bookCategory));
				pstmt.setDate(10, Utility.getDate(bookCatalogueWrapper.dop));
				pstmt.setString(11, Utility.trim(bookCatalogueWrapper.modifierID));
				pstmt.setTimestamp(12, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(13, Utility.trim(bookCatalogueWrapper.bookRefNo));
				pstmt.setString(14, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				bookCatalogueWrapper.recordFound = true;
				dataArrayWrapper.bookCatalogueWrapper = new BookCatalogueWrapper[1];
				dataArrayWrapper.bookCatalogueWrapper[0] = bookCatalogueWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully Book details Updated");
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

	// -----------------Start fetchBookDetails---------------------

	public AbstractWrapper fetchBookDetails(UsersWrapper usersProfileWrapper, BookCatalogueWrapper bookCatalogueWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		String sql = null;

		try {

			con = getConnection();

			sql = "SELECT BookRefNo, BookID, BookName, Author, Edition, Publisher, "
					+ " Price, YOP, BookCount, BookCategory, DOP, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID FROM BookCatalog where SchoolID=?";

			if (bookCatalogueWrapper.bookRefNo != null && !bookCatalogueWrapper.bookRefNo.equals("")) {

				sql = sql + " AND BookRefNo=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(usersProfileWrapper.schoolID));

			if (bookCatalogueWrapper.bookRefNo != null && !bookCatalogueWrapper.bookRefNo.equals("")) {

				pstmt.setString(2, Utility.trim(bookCatalogueWrapper.bookRefNo));
			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				bookCatalogueWrapper = new BookCatalogueWrapper();

				bookCatalogueWrapper.bookRefNo = Utility.trim(resultSet.getString("BookRefNo"));
				bookCatalogueWrapper.bookID = Utility.trim(resultSet.getString("BookID"));
				bookCatalogueWrapper.bookName = Utility.trim(resultSet.getString("BookName"));
				bookCatalogueWrapper.author = Utility.trim(resultSet.getString("Author"));
				bookCatalogueWrapper.edition = Utility.trim(resultSet.getString("Edition"));
				bookCatalogueWrapper.publisher = Utility.trim(resultSet.getString("Publisher"));
				bookCatalogueWrapper.price = Utility.trim(resultSet.getString("Price"));
				bookCatalogueWrapper.yop = Utility.trim(resultSet.getString("YOP"));
				bookCatalogueWrapper.bookCount = Utility.trim(resultSet.getString("BookCount"));
				bookCatalogueWrapper.bookCategory = Utility.trim(resultSet.getString("BookCategory"));
				bookCatalogueWrapper.dop = Utility.setDate(resultSet.getString("DOP"));

				bookCatalogueWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				bookCatalogueWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				bookCatalogueWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				bookCatalogueWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				bookCatalogueWrapper.schoolID = Utility.setDate(resultSet.getString("SchoolID"));

				bookCatalogueWrapper.recordFound = true;

				System.out.println("fetchBookDetails  successful");

				vector.addElement(bookCatalogueWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.bookCatalogueWrapper = new BookCatalogueWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.bookCatalogueWrapper);
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
	// -----------------End fetchBookDetails---------------------

	// -----------------Start fetchBooksearch---------------------

	public AbstractWrapper fetchBookSearch(UsersWrapper usersProfileWrapper, BookCatalogueWrapper bookCatalogueWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		String sql = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		
		int n=1;

		try {

			PopoverHelper popoverHelper=new PopoverHelper();
			con = getConnection();

			sql = " WHERE SchoolID=? ";
			
			
			if (!Utility.isEmpty(bookCatalogueWrapper.bookID)) {
				sql = sql + " and (BookID =?  OR UPPER(BookName) LIKE ? OR UPPER(Author) LIKE ? OR UPPER(Publisher) LIKE ? OR UPPER(BookCategory) LIKE ?) ";

				System.out.println("bookCatalogueWrapper BookID " + sql);

			}

//			else if (bookCatalogueWrapper.bookName != null && !bookCatalogueWrapper.bookName.equals("")) {
//
//				sql = " WHERE UPPER(BookName) LIKE ? and SchoolID=?";
//
//				System.out.println(" Book Name " + bookCatalogueWrapper.bookName);
//
//			} else if (bookCatalogueWrapper.author != null && !bookCatalogueWrapper.author.equals("")) {
//
//				sql = " WHERE  UPPER(Author) LIKE ? and SchoolID=?";
//
//				System.out.println(" Author  " + bookCatalogueWrapper.author);
//
//			}
//
//			else if (bookCatalogueWrapper.publisher != null && !bookCatalogueWrapper.publisher.equals("")) {
//
//				sql = " WHERE UPPER(Publisher) LIKE ? and SchoolID=?";
//
//			} else if (bookCatalogueWrapper.bookCategory != null && !bookCatalogueWrapper.bookCategory.equals("")) {
//
//				sql = " WHERE UPPER(BookCategory) LIKE ? and SchoolID=?";
//
//				System.out.println("SQL in bookCategory is " + sql);
//
//			}

			PreparedStatement pstmt = con
					.prepareStatement("SELECT BookRefNo, BookID, BookName, Author, Edition, Publisher, "
							+ " Price, YOP, BookCount, BookCategory, DOP, MakerID, MakerDateTime, ModifierID, ModifierDateTime, SchoolID FROM BookCatalog "
							+ sql);

			pstmt.setString(n, usersProfileWrapper.schoolID);

			if (!Utility.isEmpty(bookCatalogueWrapper.bookID)) {
				pstmt.setString(++n, Utility.trim(bookCatalogueWrapper.bookID));
				pstmt.setString(++n, '%' + Utility.trim(bookCatalogueWrapper.bookID).toUpperCase() + '%');
				pstmt.setString(++n, '%' + Utility.trim(bookCatalogueWrapper.bookID).toUpperCase() + '%');
				pstmt.setString(++n, '%' + Utility.trim(bookCatalogueWrapper.bookID).toUpperCase() + '%');
				pstmt.setString(++n, '%' + Utility.trim(bookCatalogueWrapper.bookID).toUpperCase() + '%');

			}

//			else if (bookCatalogueWrapper.bookName != null && !bookCatalogueWrapper.bookName.trim().isEmpty()) {
//
//				pstmt.setString(1, '%' + bookCatalogueWrapper.bookName.trim().toUpperCase() + '%');
//				pstmt.setString(2, usersProfileWrapper.schoolID);
//
//			} else if (bookCatalogueWrapper.author != null && !bookCatalogueWrapper.author.trim().isEmpty()) {
//
//				pstmt.setString(1, '%' + bookCatalogueWrapper.author.trim().toUpperCase() + '%');
//				pstmt.setString(2, usersProfileWrapper.schoolID);
//
//			}
//
//			else if (bookCatalogueWrapper.publisher != null && !bookCatalogueWrapper.publisher.trim().isEmpty()) {
//
//				// pstmt.setString(1, bookCatalogueWrapper.gradeID.trim());
//
//				pstmt.setString(1, '%' + bookCatalogueWrapper.publisher.trim().toUpperCase() + '%');
//				pstmt.setString(2, usersProfileWrapper.schoolID);
//
//			} else if (bookCatalogueWrapper.bookCategory != null
//					&& !bookCatalogueWrapper.bookCategory.trim().isEmpty()) {
//				System.out.println("bookCategory is " + bookCatalogueWrapper.bookCategory.trim());
//				// pstmt.setString(1, bookCatalogueWrapper.sectionID.trim());
//				pstmt.setString(1, '%' + bookCatalogueWrapper.bookCategory.trim().toUpperCase() + '%');
//				pstmt.setString(2, usersProfileWrapper.schoolID);
//
//			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				bookCatalogueWrapper = new BookCatalogueWrapper();

				bookCatalogueWrapper.bookRefNo = Utility.trim(resultSet.getString("BookRefNo"));
				bookCatalogueWrapper.bookID = Utility.trim(resultSet.getString("BookID"));
				bookCatalogueWrapper.bookName = Utility.trim(resultSet.getString("BookName"));
				bookCatalogueWrapper.author = Utility.trim(resultSet.getString("Author"));
				bookCatalogueWrapper.edition = Utility.trim(resultSet.getString("Edition"));
				bookCatalogueWrapper.publisher = Utility.trim(resultSet.getString("Publisher"));
				bookCatalogueWrapper.price = Utility.trim(resultSet.getString("Price"));
				bookCatalogueWrapper.yop = Utility.trim(resultSet.getString("YOP"));
				bookCatalogueWrapper.bookCount = Utility.trim(resultSet.getString("BookCount"));
				bookCatalogueWrapper.bookCategory = Utility.trim(resultSet.getString("BookCategory"));
				bookCatalogueWrapper.dop = Utility.setDate(resultSet.getString("DOP"));

				bookCatalogueWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				bookCatalogueWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				bookCatalogueWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				bookCatalogueWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				bookCatalogueWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				
				
				bookCatalogueWrapper.bookCategoryValue = popoverHelper.fetchPopoverDesc(bookCatalogueWrapper.bookCategory,
						"MST_BookCategory", bookCatalogueWrapper.schoolID);

				bookCatalogueWrapper.recordFound = true;

				// bookCatalogueWrapper.gradeIDValue=popoverHelper.fetchPopoverDesc(bookCatalogueWrapper.gradeID,
				// "MST_Grade");
				// bookCatalogueWrapper.sectionIDValue=popoverHelper.fetchPopoverDesc(bookCatalogueWrapper.sectionID,
				// "MST_Section");
				// bookCatalogueWrapper.academicYearIDValue=popoverHelper.fetchPopoverDesc(bookCatalogueWrapper.academicYearID,
				// "MST_AcademicYear");

				System.out.println("Book Details Queue fetch successful");

				vector.addElement(bookCatalogueWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.bookCatalogueWrapper = new BookCatalogueWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.bookCatalogueWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}
			else
			{
				bookCatalogueWrapper.recordFound = false;
				dataArrayWrapper.bookCatalogueWrapper = new BookCatalogueWrapper[1];
				dataArrayWrapper.bookCatalogueWrapper[0]=bookCatalogueWrapper;
				dataArrayWrapper.recordFound=true;

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
	// -----------------End fetchBookSearch---------------------

	// -----------------generateBookRefNo-------------------------------
	public String generateBookRefNo(String schoolID) throws Exception {

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

		int bookRefNo = 0;
		String finalBookRefNo = null;
		String bookCode = null;

		try {

			con = getConnection();

			sql = "SELECT BookRefNo, BookCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {

				bookRefNo = resultSet.getInt("BookRefNo");
				System.out.println("BookRefNo " + bookRefNo);
				bookCode = resultSet.getString("BookCode");

			}

			resultSet.close();
			pstmt.close();

			if (bookRefNo == 0) {
				bookRefNo = 1;

			} else {

				bookRefNo = bookRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set BookRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, bookRefNo);
			pstmt.setString(2, schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 6;

			finalBookRefNo = bookCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", bookRefNo);

			System.out.println("Successfully generated BookRefNo " + finalBookRefNo);

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

		return finalBookRefNo;
	}

	// -----------------End generateBookRefNo---------------------------

}
