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
import com.rootmind.wrapper.ImageDetailsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class ImageDetailsHelper extends Helper {

	public AbstractWrapper uploadImageDetails(UsersWrapper userProfileWrapper, ImageDetailsWrapper imageDetailsWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		// int fileLength;
		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		// File file=new File(imageDetailsWrapper.imageFile);
		// FileInputStream fis = new FileInputStream(file);
		// fileLength = (int)file.length();
		// System.out.println("Image File Length "+fileLength);

		int n = 1;

		try {

			if (imageDetailsWrapper.imageUploadStatus == true) {
				con = getConnection();

				sql = "INSERT INTO StudentImages(RefNo, StudentID, ImageId, ImageFileName, ImageFileFolder, ImageFileType, ImageContentType, "
						+ "UploadUserId, UploadDateTime, ImageStatus,DocID, SchoolID, FileTitleRefNo, FileTitle, GradeID, SectionID, "
						+ " SubjectID, TermID, ChapterStartDate, ChapterEndDate, ViewStartDate, ViewEndDate) "
						+ " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("sql " + sql);
				System.out.println("Image File Name " + imageDetailsWrapper.imageFileName);
				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(n, generateFileRefNo(userProfileWrapper.schoolID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.studentID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageId));

				// pstmt.setBinaryStream(4, fis, fileLength);
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileName));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileFolder));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileType));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageContentType));
				pstmt.setString(++n, Utility.trim(userProfileWrapper.userid));
				pstmt.setTimestamp(++n, Utility.getCurrentTime()); // uploaddateTime
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageStatus));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.docID));
				System.out.println("DOC ID " + imageDetailsWrapper.docID);
				pstmt.setString(++n, Utility.trim(userProfileWrapper.schoolID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.fileTitleRefNo));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.fileTitle));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.gradeID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.sectionID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.subjectID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.termID));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.chapterStartDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.chapterEndDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.viewStartDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.viewEndDate));

				pstmt.executeUpdate();
				pstmt.close();

				imageDetailsWrapper.imageFoundStatus = true;
				imageDetailsWrapper.recordFound = true;
			}

			dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[1];
			dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Image uploaded successflly");
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

	public AbstractWrapper updateImageDetails(UsersWrapper usersProfileWrapper, ImageDetailsWrapper imageDetailsWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper(); // String
		String sql = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		// File file = new File(imageDetailsWrapper.imageFile);
		// FileInputStream fis = new FileInputStream(file);
		// int fileLength = (int) file.length();

		System.out.println("Update Image Details");

		PreparedStatement pstmt = null;

		int n = 1;

		try {
			con = getConnection();

			sql = "SELECT RefNo, StudentID, ImageId from StudentImages ";

			if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.imageId)) {
				sql = sql + "WHERE RefNo=? AND ImageId=?  and SchoolID=?";
			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.docID)) {
				sql = sql + "WHERE RefNo=? AND DocID=? and SchoolID=?";
			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID) && !Utility.isEmpty(imageDetailsWrapper.docID)) {
				sql = sql + "WHERE StudentID=? AND DocID=? and SchoolID=?";
			}

			pstmt = con.prepareStatement(sql);

			System.out.println("Image Details RefNo is" + imageDetailsWrapper.refNo);

			if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.imageId)) {
				pstmt.setString(1, Utility.trim(imageDetailsWrapper.refNo));
				pstmt.setString(2, Utility.trim(imageDetailsWrapper.imageId));
				pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.docID)) {
				pstmt.setString(1, Utility.trim(imageDetailsWrapper.refNo));
				pstmt.setString(2, Utility.trim(imageDetailsWrapper.docID));
				pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID) && !Utility.isEmpty(imageDetailsWrapper.docID)) {

				pstmt.setString(1, Utility.trim(imageDetailsWrapper.studentID));
				pstmt.setString(2, Utility.trim(imageDetailsWrapper.docID));
				pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			}

			// pstmt.setString(1, Utility.trim(imageDetailsWrapper.refNo));
			// pstmt.setString(2, Utility.trim(imageDetailsWrapper.studentID));
			// pstmt.setString(3, Utility.trim(imageDetailsWrapper.imageId));
			// pstmt.setString(4, Utility.trim(imageDetailsWrapper.docID));
			// pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				// image ref number not available
				if (!Utility.isEmpty(imageDetailsWrapper.studentID) && !Utility.isEmpty(imageDetailsWrapper.docID)) {

					imageDetailsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				}

				resultSet.close();
				pstmt.close();

				// if new file not uploaded in UPDATE mode then don't overwrite existing file
				// details
				if (imageDetailsWrapper.imageUploadStatus == true) {

					sql = "UPDATE StudentImages SET "
							+ " FileTitleRefNo=?, FileTitle=?, GradeID=?, SectionID=?, SubjectID=?, TermID=?, ChapterStartDate=?, ChapterEndDate=?, "
							+ " ViewStartDate=?, ViewEndDate=?, "
							+ " ImageFileName=?, ImageFileFolder=?, ImageFileType=?, ImageContentType=?, ModifiedUserId=?, ModifiedDateTime=? "
							+ " WHERE RefNo=? and SchoolID=? ";
				} else {

					sql = "UPDATE StudentImages SET "
							+ " FileTitleRefNo=?, FileTitle=?, GradeID=?, SectionID=?, SubjectID=?, TermID=?, ChapterStartDate=?, ChapterEndDate=?, "
							+ " ViewStartDate=?, ViewEndDate=?, ModifiedUserId=?, ModifiedDateTime=? "
							+ " WHERE RefNo=? and SchoolID=? ";

				}

				pstmt = con.prepareStatement(sql); // and StudentID=? and ImageId=? and DocID=? and SchoolID=?");

				System.out.println("updateImageDetails " + imageDetailsWrapper.imageFileName);

				pstmt.setString(n, Utility.trim(imageDetailsWrapper.fileTitleRefNo));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.fileTitle));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.gradeID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.sectionID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.subjectID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.termID));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.chapterStartDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.chapterEndDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.viewStartDate));
				pstmt.setDate(++n, Utility.getDate(imageDetailsWrapper.viewEndDate));

				if (imageDetailsWrapper.imageUploadStatus == true) {
					pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileName));
					pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileFolder));
					pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageFileType));
					pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageContentType));
				}

				pstmt.setString(++n, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(++n, Utility.getCurrentTime()); // uploaddateTime

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.refNo));
				// pstmt.setString(6, Utility.trim(imageDetailsWrapper.studentID));
				// pstmt.setString(7, Utility.trim(imageDetailsWrapper.imageId));
				// pstmt.setString(8, Utility.trim(imageDetailsWrapper.docID));
				pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				imageDetailsWrapper.imageFoundStatus = true;
				imageDetailsWrapper.recordFound = true;

				dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[1];
				dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
				dataArrayWrapper.recordFound = true;
			} else {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) uploadImageDetails(usersProfileWrapper, imageDetailsWrapper);

			}

			System.out.println("Image details updated successfully");

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

	public AbstractWrapper fetchImageDetails(UsersWrapper usersProfileWrapper, ImageDetailsWrapper imageDetailsWrapper)
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

		Vector<Object> vector = new Vector<Object>();

		// byte[] fileBytes;
		// boolean imageFoundStatus = imageDetailsWrapper.imageFoundStatus;
		int n = 1;

		try {
			// String imagesPath = fetchImagePath(usersProfileWrapper.schoolID);
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();
			String sql = "SELECT RefNo, StudentID, ImageId, ImageFile, ImageFileName, ImageFileFolder, ImageFileType, ImageContentType, "
					+ " UploadUserId, UploadDateTime,ImageStatus, "
					+ " ModifiedUserId, ModifiedDateTime,DocID, SchoolID, FileTitleRefNo, FileTitle, GradeID, SectionID, "
					+ " SubjectID, TermID, ChapterStartDate, ChapterEndDate, ViewStartDate, ViewEndDate FROM StudentImages WHERE SchoolID=? ";

			if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.imageId)) {

				sql = sql + "and RefNo=? AND ImageId=? ";
			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.docID)) {

				sql = sql + "and RefNo=? AND DocID=? ";
			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID) && !Utility.isEmpty(imageDetailsWrapper.docID)) {

				sql = sql + "and StudentID=? AND DocID=? ";
			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo)) {

				sql = sql + " and RefNo=? ";

			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID)) {

				sql = sql + " and StudentID=? ";

			} else if (!Utility.isEmpty(imageDetailsWrapper.docID)) {

				sql = sql + " and DocID=? ";

			}

			// --------additional filters
			if (!Utility.isEmpty(imageDetailsWrapper.gradeID)) {

				sql = sql + " and gradeID=? ";

			}

			if (!Utility.isEmpty(imageDetailsWrapper.sectionID)) {

				sql = sql + " and sectionID=? ";

			}
			if (!Utility.isEmpty(imageDetailsWrapper.subjectID)) {

				sql = sql + " and subjectID=? ";

			}
			if (!Utility.isEmpty(imageDetailsWrapper.termID)) {

				sql = sql + " and termID=? ";

			}
			if (!Utility.isEmpty(imageDetailsWrapper.chapterStartDate)) {

				sql = sql + " and ChapterStartDate>=? ";

			}
			if (!Utility.isEmpty(imageDetailsWrapper.chapterEndDate)) {

				sql = sql + " and ChapterEndDate<=? ";

			}

			// activate only for users not for admin
			// sql = sql + " and ImageStatus='ACTIVE' and (ViewStartDate is null or
			// ViewStartDate<='" + Utility.getCurrentDate() +"') and (ViewEndDate is null or
			// ViewEndDate>='"+ Utility.getCurrentDate() + "')";

			PreparedStatement pstmt = con.prepareStatement(sql);

			System.out.println("Image Details RefNo is" + imageDetailsWrapper.refNo);

			System.out.println("Image Details sql" + sql);

			pstmt.setString(n, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.imageId)) {
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.refNo));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.imageId));

			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo) && !Utility.isEmpty(imageDetailsWrapper.docID)) {
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.refNo));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.docID));

			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID) && !Utility.isEmpty(imageDetailsWrapper.docID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.studentID));
				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.docID));
			} else if (!Utility.isEmpty(imageDetailsWrapper.refNo)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.refNo));

			} else if (!Utility.isEmpty(imageDetailsWrapper.studentID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.studentID));

			} else if (!Utility.isEmpty(imageDetailsWrapper.docID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.docID));

			}

			if (!Utility.isEmpty(imageDetailsWrapper.gradeID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.gradeID));

			}

			if (!Utility.isEmpty(imageDetailsWrapper.sectionID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.sectionID));

			}
			if (!Utility.isEmpty(imageDetailsWrapper.subjectID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.subjectID));

			}
			if (!Utility.isEmpty(imageDetailsWrapper.termID)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.termID));

			}
			if (!Utility.isEmpty(imageDetailsWrapper.chapterStartDate)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.chapterStartDate));

			}
			if (!Utility.isEmpty(imageDetailsWrapper.chapterEndDate)) {

				pstmt.setString(++n, Utility.trim(imageDetailsWrapper.chapterStartDate));

			}

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				imageDetailsWrapper = new ImageDetailsWrapper();

				imageDetailsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				System.out.println("RefNo" + imageDetailsWrapper.refNo);

				imageDetailsWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				System.out.println("studentID " + imageDetailsWrapper.studentID);

				imageDetailsWrapper.imageId = Utility.trim(resultSet.getString("ImageId"));

				// fileBytes = resultSet.getBytes("ImageFile");
				// OutputStream targetFile= new
				// FileOutputStream("D://RetriveImages//NewImageFromServer.JPG");

				// targetFile.write(fileBytes);
				// targetFile.close();

				// imageDetailsWrapper.accountType=Utility.trim(resultSet.getString("AccountType"));

				imageDetailsWrapper.imageFileName = Utility.trim(resultSet.getString("ImageFileName"));
				imageDetailsWrapper.imageFileFolder = Utility.trim(resultSet.getString("ImageFileFolder")); // imagesPath
																											// +
				imageDetailsWrapper.imageFileType = Utility.trim(resultSet.getString("ImageFileType")); // imagesPath +
				imageDetailsWrapper.imageContentType = Utility.trim(resultSet.getString("ImageContentType")); // imagesPath
																												// +

				imageDetailsWrapper.uploadUserId = Utility.trim(resultSet.getString("UploadUserId"));

				imageDetailsWrapper.uploadDateTime = Utility.setDate(resultSet.getString("UploadDateTime"));
				imageDetailsWrapper.imageStatus = Utility.trim(resultSet.getString("ImageStatus"));

				imageDetailsWrapper.modifiedUserId = Utility.trim(resultSet.getString("ModifiedUserId"));

				imageDetailsWrapper.modifiedDateTime = Utility.trim(resultSet.getString("ModifiedDateTime"));
				imageDetailsWrapper.docID = Utility.trim(resultSet.getString("DocID"));
				imageDetailsWrapper.imageFoundStatus = true; // imageFoundStatus;
				imageDetailsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				imageDetailsWrapper.fileTitleRefNo = Utility.trim(resultSet.getString("FileTitleRefNo"));
				imageDetailsWrapper.fileTitle = Utility.trim(resultSet.getString("FileTitle"));
				imageDetailsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				imageDetailsWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				imageDetailsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));
				imageDetailsWrapper.termID = Utility.trim(resultSet.getString("TermID"));
				imageDetailsWrapper.chapterStartDate = Utility.setDate(resultSet.getString("ChapterStartDate"));
				imageDetailsWrapper.chapterEndDate = Utility.setDate(resultSet.getString("ChapterEndDate"));
				imageDetailsWrapper.viewStartDate = Utility.setDate(resultSet.getString("ViewStartDate"));
				imageDetailsWrapper.viewEndDate = Utility.setDate(resultSet.getString("ViewEndDate"));

				imageDetailsWrapper.docIDValue = popoverHelper.fetchPopoverDesc(imageDetailsWrapper.docID,
						"MST_DocChecklistMaster", usersProfileWrapper.schoolID);

				//System.out.println("Fetch Image Details:docID ImageID " + imageDetailsWrapper.docID);
				//System.out.println("Fetch Image Details:docIDValue ImageValue" + imageDetailsWrapper.docIDValue);

				imageDetailsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(imageDetailsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				imageDetailsWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(imageDetailsWrapper.sectionID,
						"MST_Section", usersProfileWrapper.schoolID);
				imageDetailsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(imageDetailsWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);
				imageDetailsWrapper.termIDValue = popoverHelper.fetchPopoverDesc(imageDetailsWrapper.termID, "MST_Term",
						usersProfileWrapper.schoolID);

				imageDetailsWrapper.recordFound = true;

				vector.addElement(imageDetailsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.imageDetailsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else {
				dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[1];
				imageDetailsWrapper.recordFound = false;
				dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
				dataArrayWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();

			System.out.println("Fetch Image Details Successful");

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

	// public AbstractWrapper fetchImageFileNames(UsersWrapper usersProfileWrapper,
	// ImageDetailsWrapper imageDetailsWrapper) throws Exception {
	//
	// Connection con = null;
	// ResultSet resultSet = null;
	//
	// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
	//
	// // SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	//
	// // DecimalFormat formatter = (DecimalFormat)
	// // NumberFormat.getInstance(Locale.US);
	// // DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
	// // symbols.setGroupingSeparator(',');
	// // formatter.applyPattern("###,###,###,##0.00");
	// // formatter.setDecimalFormatSymbols(symbols);
	//
	// Vector<Object> vector = new Vector<Object>();
	//
	// // byte[] fileBytes;
	//
	// try {
	// PopoverHelper popoverHelper = new PopoverHelper();
	//
	// con = getConnection();
	//
	// String sql = "SELECT RefNo, StudentID, ImageId, ImageFile, ImageFileName,
	// ImageFileFolder, UploadUserId, UploadDateTime, "
	// + " ImageStatus, ModifiedUserId, ModifiedDateTime, FileTitleRefNo, FileTitle,
	// GradeID, SectionID, "
	// + " SubjectID, TermID, ChapterStartDate, ChapterEndDate, ViewStartDate,
	// ViewEndDate, "
	// + " DocID , SchoolID "
	// + " FROM StudentImages "; //a LEFT JOIN MST_DocChecklistMaster b ON
	// a.DOCID=b.Code; b.Description as DocName,
	//
	// if (!Utility.isEmpty(imageDetailsWrapper.refNo)) {
	// sql = sql + " WHERE RefNo=? and ImageStatus='ACTIVE' and SchoolID=?";
	// } else if (!Utility.isEmpty(imageDetailsWrapper.studentID)){
	// sql = sql + " WHERE StudentID=? and ImageStatus='ACTIVE' and SchoolID=?";
	// }
	// else if (!Utility.isEmpty(imageDetailsWrapper.docID)){
	// sql = sql + " WHERE DocID=? and ImageStatus='ACTIVE' and (ViewStartDate is
	// null or ViewStartDate<='" + Utility.getCurrentDate() +"') and (ViewEndDate is
	// null or ViewEndDate>='"+ Utility.getCurrentDate() + "') and SchoolID=?";
	// }
	//
	// System.out.println("fetchImageFileNames sql" + sql);
	//
	//
	// PreparedStatement pstmt = con.prepareStatement(sql);
	//
	// System.out.println("Image Details RefNo is" + imageDetailsWrapper.refNo);
	//
	// if (!Utility.isEmpty(imageDetailsWrapper.refNo)) {
	// pstmt.setString(1, Utility.trim(imageDetailsWrapper.refNo));
	// pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));
	//
	// } else if (!Utility.isEmpty(imageDetailsWrapper.studentID)){
	// pstmt.setString(1, Utility.trim(imageDetailsWrapper.studentID));
	// pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));
	//
	// }
	// else if (!Utility.isEmpty(imageDetailsWrapper.docID)){
	// pstmt.setString(1, Utility.trim(imageDetailsWrapper.docID));
	// pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));
	//
	// }
	//
	//
	// resultSet = pstmt.executeQuery();
	//
	// while (resultSet.next()) {
	//
	// imageDetailsWrapper = new ImageDetailsWrapper();
	//
	// imageDetailsWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
	// System.out.println("RefNo" + imageDetailsWrapper.refNo);
	//
	// imageDetailsWrapper.studentID =
	// Utility.trim(resultSet.getString("StudentID"));
	// System.out.println("StudentID " + imageDetailsWrapper.studentID);
	//
	// imageDetailsWrapper.imageId = Utility.trim(resultSet.getString("ImageId"));
	//
	// // fileBytes = resultSet.getBytes("ImageFile");
	// // OutputStream targetFile= new
	// // FileOutputStream("D://RetriveImages//NewImageFromServer.JPG");
	//
	// // targetFile.write(fileBytes);
	// // targetFile.close();
	//
	// //
	// imageDetailsWrapper.accountType=Utility.trim(resultSet.getString("AccountType"));
	//
	// imageDetailsWrapper.imageFileName =
	// Utility.trim(resultSet.getString("ImageFileName"));
	//
	// System.out.println("imageFileName " + imageDetailsWrapper.imageFileName);
	//
	//
	// imageDetailsWrapper.imageFileFolder =
	// Utility.trim(resultSet.getString("ImageFileFolder"));
	//
	// System.out.println("imageFileFolder " + imageDetailsWrapper.imageFileFolder);
	//
	//
	// imageDetailsWrapper.uploadUserId =
	// Utility.trim(resultSet.getString("UploadUserId"));
	//
	// imageDetailsWrapper.uploadDateTime =
	// Utility.trim(resultSet.getString("UploadDateTime"));
	//
	// imageDetailsWrapper.imageStatus =
	// Utility.trim(resultSet.getString("ImageStatus"));
	//
	// imageDetailsWrapper.modifiedUserId =
	// Utility.trim(resultSet.getString("ModifiedUserId"));
	//
	// imageDetailsWrapper.modifiedDateTime =
	// Utility.trim(resultSet.getString("ModifiedDateTime"));
	//
	// imageDetailsWrapper.fileTitleRefNo =
	// Utility.trim(resultSet.getString("FileTitleRefNo"));
	// imageDetailsWrapper.fileTitle =
	// Utility.trim(resultSet.getString("FileTitle"));
	// imageDetailsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
	// imageDetailsWrapper.sectionID =
	// Utility.trim(resultSet.getString("SectionID"));
	// imageDetailsWrapper.subjectID =
	// Utility.trim(resultSet.getString("SubjectID"));
	// imageDetailsWrapper.termID = Utility.trim(resultSet.getString("TermID"));
	// imageDetailsWrapper.chapterStatDate =
	// Utility.setDate(resultSet.getString("ChapterStartDate"));
	// imageDetailsWrapper.chapterEndDate =
	// Utility.setDate(resultSet.getString("ChapterEndDate"));
	// imageDetailsWrapper.viewStatDate =
	// Utility.setDate(resultSet.getString("ViewStartDate"));
	// imageDetailsWrapper.viewEndDate =
	// Utility.setDate(resultSet.getString("ViewEndDate"));
	//
	//
	// imageDetailsWrapper.docID = Utility.trim(resultSet.getString("DocID"));
	//
	// //imageDetailsWrapper.docName = Utility.trim(resultSet.getString("DocName"));
	//
	// imageDetailsWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
	//
	// //System.out.println("Doc Name " + imageDetailsWrapper.docName);
	//
	// //
	// imageDetailsWrapper.docIDValue=popoverHelper.fetchPopoverDesc(imageDetailsWrapper.docID,"IMAGEDOC");
	//
	// // System.out.println("Fetch Image FileNames:docID
	// "+imageDetailsWrapper.docID);
	// // System.out.println("Fetch Image FileNames:docIDValue
	// // "+imageDetailsWrapper.docIDValue);
	//
	// imageDetailsWrapper.docIDValue =
	// popoverHelper.fetchPopoverDesc(imageDetailsWrapper.docID,
	// "MST_DocChecklistMaster", usersProfileWrapper.schoolID);
	//
	// //System.out.println("Fetch Image Details:docID ImageID " +
	// imageDetailsWrapper.docID);
	// //System.out.println("Fetch Image Details:docIDValue ImageValue" +
	// imageDetailsWrapper.docIDValue);
	//
	// imageDetailsWrapper.gradeIDValue =
	// popoverHelper.fetchPopoverDesc(imageDetailsWrapper.gradeID,
	// "MST_Grade", usersProfileWrapper.schoolID);
	// imageDetailsWrapper.sectionIDValue =
	// popoverHelper.fetchPopoverDesc(imageDetailsWrapper.sectionID,
	// "MST_Section", usersProfileWrapper.schoolID);
	// imageDetailsWrapper.subjectIDValue =
	// popoverHelper.fetchPopoverDesc(imageDetailsWrapper.subjectID,
	// "MST_Subject", usersProfileWrapper.schoolID);
	// imageDetailsWrapper.termIDValue =
	// popoverHelper.fetchPopoverDesc(imageDetailsWrapper.termID,
	// "MST_Term", usersProfileWrapper.schoolID);
	//
	// imageDetailsWrapper.imageFoundStatus = true;
	// imageDetailsWrapper.recordFound = true;
	//
	// vector.addElement(imageDetailsWrapper);
	//
	// }
	//
	// if (vector.size() > 0) {
	// dataArrayWrapper.imageDetailsWrapper = new
	// ImageDetailsWrapper[vector.size()];
	// vector.copyInto(dataArrayWrapper.imageDetailsWrapper);
	// dataArrayWrapper.recordFound = true;
	//
	// System.out.println("total trn. in fetch " + vector.size());
	//
	// } else {
	// dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[1];
	// dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
	// dataArrayWrapper.recordFound = true;
	//
	// }
	//
	// resultSet.close();
	// pstmt.close();
	//
	// System.out.println("Fetch Image FileNames Successful");
	//
	// } catch (SQLException se) {
	// se.printStackTrace();
	// throw new SQLException(se.getSQLState() + " ; " + se.getMessage());
	// } catch (NamingException ne) {
	// ne.printStackTrace();
	// throw new NamingException(ne.getMessage());
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// throw new Exception(ex.getMessage());
	// } finally {
	// try {
	// releaseConnection(resultSet, con);
	// } catch (SQLException se) {
	// se.printStackTrace();
	// throw new Exception(se.getSQLState() + " ; " + se.getMessage());
	// }
	// }
	//
	// return dataArrayWrapper;
	// }

	public AbstractWrapper updateImageStatus(UsersWrapper usersProfileWrapper, ImageDetailsWrapper imageDetailsWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);

		/*
		 * File file=new File(imageDetailsWrapper.imageFile); FileInputStream fis = new
		 * FileInputStream(file); int fileLength = (int)file.length();
		 */

		System.out.println("Update Image Status");

		try {
			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement("UPDATE StudentImages SET ImageStatus=? WHERE RefNo=? "); // and
																														// SchoolID=?

			pstmt.setString(1, Utility.trim(imageDetailsWrapper.imageStatus));
			pstmt.setString(2, Utility.trim(imageDetailsWrapper.refNo));
			// pstmt.setString(3, Utility.trim(imageDetailsWrapper.imageId)); //and
			// ImageId=?
			// pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();

			pstmt.close();

			imageDetailsWrapper.recordFound = true;

			dataArrayWrapper.imageDetailsWrapper = new ImageDetailsWrapper[1];
			dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Image Status updated successfully");

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

	// ---------------------- Start fetchImagePath-----------------
	public String fetchImagePath(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		String imagePath = null;

		try {
			con = getConnection();
			// -----country code--

			PreparedStatement pstmt = con.prepareStatement("SELECT ImagesPath from MST_Parameter where SchoolID=?");

			pstmt.setString(1, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				imagePath = resultSet.getString("ImagesPath");

				System.out.println("Images Path " + imagePath);

			}

			resultSet.close();
			pstmt.close();

			// ----------

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

		return imagePath;
	}
	// ---------------------- End fetchImagePath-----------------

	// -----------------Generate MessageID-------------------------------
	public String generateFileRefNo(String schoolID) throws Exception {

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

		int fileRefNo = 0;
		String finalFileRefNo = null;
		String fileCode = null;

		try {
			con = getConnection();

			sql = "SELECT FileRefNo, FileCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				fileRefNo = resultSet.getInt("FileRefNo");
				fileCode = resultSet.getString("FileCode");
				System.out.println("FileCode" + fileCode);

			}

			resultSet.close();
			pstmt.close();

			if (fileRefNo == 0) {
				fileRefNo = 1;

			} else {

				fileRefNo = fileRefNo + 1;
			}

			sql = "UPDATE MST_Parameter set FileRefNo=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, fileRefNo);
			pstmt.setString(2, schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 5;// 6-String.valueOf(messageID).length();

			// System.out.println("Savings Account " + studentProfileWrapper.accountType);

			// System.out.println("Savings Account " +
			// studentProfileWrapper.accountType.substring(0,2));

			finalFileRefNo = fileCode + dmyFormat.format(new java.util.Date()).toUpperCase()
					+ String.format("%0" + paddingSize + "d", fileRefNo);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated school finalFileRefNo " + finalFileRefNo);

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

		return finalFileRefNo;
	}
}
