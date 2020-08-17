package com.rootmind.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

import javax.imageio.ImageIO;
import javax.naming.NamingException;

import org.apache.commons.codec.binary.Base64;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ImageDetailsWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.SchoolWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class StudentProfileHelper extends Helper {

	// -----------------Start insertStudentProfile---------------------

	public AbstractWrapper insertStudentProfile(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		String emailDomain = null;

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

			// ----Current Academic Year--

			PopoverHelper popoverHelper = new PopoverHelper();

			if (Utility.isEmpty(studentProfileWrapper.academicYearID) == true) {

				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentProfileWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------

			ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
					.fetchParameters(usersProfileWrapper.schoolID);
			emailDomain = parameterWrapper.emailDomain;

			String firstName = null;
			if (studentProfileWrapper.studentName.contains(" ")) {
				firstName = studentProfileWrapper.studentName.substring(0,
						studentProfileWrapper.studentName.indexOf(" "));
			} else {
				firstName = studentProfileWrapper.studentName;
			}

			/// ----------

			// ----------create userid with schoolid userid@schoolid

			studentProfileWrapper.refNo = generateRefNo(usersProfileWrapper);
			studentProfileWrapper.studentID = generateStudentID(usersProfileWrapper);

			usersProfileWrapper.schoolID = Utility.trim(Utility.removeSpaces(usersProfileWrapper.schoolID));

			// to create from app, studentID to be taken as userid
			if (Utility.isEmpty(studentProfileWrapper.userid) == true) {
				studentProfileWrapper.userid = studentProfileWrapper.studentID;
			} else {
				studentProfileWrapper.userid = Utility.trim(Utility.removeSpaces(studentProfileWrapper.userid));
			}

			String userid = studentProfileWrapper.userid;// this is used to send to create user profile without schoold
															// id
			studentProfileWrapper.userid = studentProfileWrapper.userid + "@" + usersProfileWrapper.schoolID;
			// -------------

			// ----------

			sql = " INSERT INTO StudentProfile(RefNo,SchoolID, BranchID, StudentID, StudentName, Surname, Address1,Address2, Address3, CityID, "
					+ "PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, PermPINCode, PermDistrictID, PermStateID, "
					+ "GradeID, SectionID, AcademicYearID, JoinDate, StudentPhotoID, DOB, Gender, FatherName, FatherSurname, FatherOccupation, "
					+ "FatherAge, FatherEducation, MotherName, MotherSurname, MotherOccupation, MotherAge, MotherEducation, PrimaryMobile, SecondaryMobile,PrimaryEmail, "
					+ "SecondaryEmail, Userid, Status, MakerID, MakerDateTime,ModifierID, ModifierDateTime,ImageID, ThumbnailID, ClassTeacher, RecordStatus, "
					+ "AadhaarNo,BusRouteNo, BusPickupPoint, DriverName ,DriverName2,DriverMobileNo,DriverMobileNo2, Religion, Caste, BloodGroup,EmiratesID, "
					+ "VisaNo,UIDNo,PassportNo,PassportExpiryDate,PassportIssueDate,PassportIssuePlace,StudentEmail,PhysicallyChallenged,IdentityMark1,IdentityMark2,"
					+ "DOBInWords,BankAccountNo,BankName,RationCardNo,FatherAadhaarNo,MotherAadhaarNo,ParentAnnualIncome,AdmissionNo,SameAddressFlag,CasteCategory)  "
					+ "Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(studentProfileWrapper.refNo));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));
			pstmt.setString(3, Utility.trim(studentProfileWrapper.branchID));
			pstmt.setString(4, Utility.trim(studentProfileWrapper.studentID));
			pstmt.setString(5, Utility.trim(studentProfileWrapper.studentName));
			pstmt.setString(6, Utility.trim(studentProfileWrapper.surname));
			pstmt.setString(7, Utility.trim(studentProfileWrapper.address1));
			pstmt.setString(8, Utility.trim(studentProfileWrapper.address2));
			pstmt.setString(9, Utility.trim(studentProfileWrapper.address3));
			pstmt.setString(10, Utility.trim(studentProfileWrapper.cityID));

			pstmt.setString(11, Utility.trim(studentProfileWrapper.pinCode));
			pstmt.setString(12, Utility.trim(studentProfileWrapper.districtID));
			pstmt.setString(13, Utility.trim(studentProfileWrapper.stateID));
			pstmt.setString(14, Utility.trim(studentProfileWrapper.permAddress1));
			pstmt.setString(15, Utility.trim(studentProfileWrapper.permAddress2));
			pstmt.setString(16, Utility.trim(studentProfileWrapper.permAddress3));
			pstmt.setString(17, Utility.trim(studentProfileWrapper.permCityID));
			pstmt.setString(18, Utility.trim(studentProfileWrapper.permPINCode));
			pstmt.setString(19, Utility.trim(studentProfileWrapper.permDistrictID));
			pstmt.setString(20, Utility.trim(studentProfileWrapper.permStateID));

			pstmt.setString(21, Utility.trim(studentProfileWrapper.gradeID));
			pstmt.setString(22, Utility.trim(studentProfileWrapper.sectionID));
			pstmt.setString(23, Utility.trim(studentProfileWrapper.academicYearID));
			pstmt.setDate(24, Utility.getDate(studentProfileWrapper.joinDate));
			pstmt.setString(25, Utility.trim(studentProfileWrapper.studentPhotoID));
			pstmt.setDate(26, Utility.getDate(studentProfileWrapper.dob));
			pstmt.setString(27, Utility.trim(studentProfileWrapper.gender));
			pstmt.setString(28, Utility.trim(studentProfileWrapper.fatherName));
			pstmt.setString(29, Utility.trim(studentProfileWrapper.fatherSurname));
			pstmt.setString(30, Utility.trim(studentProfileWrapper.fatherOccupation));

			pstmt.setString(31, Utility.trim(studentProfileWrapper.fatherAge));
			pstmt.setString(32, Utility.trim(studentProfileWrapper.fatherEducation));
			pstmt.setString(33, Utility.trim(studentProfileWrapper.motherName));
			pstmt.setString(34, Utility.trim(studentProfileWrapper.motherSurname));
			pstmt.setString(35, Utility.trim(studentProfileWrapper.motherOccupation));
			pstmt.setString(36, Utility.trim(studentProfileWrapper.motherAge));
			pstmt.setString(37, Utility.trim(studentProfileWrapper.motherEducation));
			pstmt.setString(38, Utility.trim(studentProfileWrapper.primaryMobile));
			pstmt.setString(39, Utility.trim(studentProfileWrapper.secondaryMobile));
			pstmt.setString(40, Utility.trim(studentProfileWrapper.primaryEmail));

			pstmt.setString(41, Utility.trim(studentProfileWrapper.secondaryEmail));
			pstmt.setString(42, Utility.trim(Utility.removeSpaces(studentProfileWrapper.userid)));
			pstmt.setString(43, Utility.trim(studentProfileWrapper.status));
			pstmt.setString(44, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(45, Utility.getCurrentTime()); // maker date time
			pstmt.setString(46, Utility.trim(studentProfileWrapper.modifierID));
			pstmt.setTimestamp(47, Utility.getCurrentTime()); // modifier date time
			pstmt.setString(48, Utility.trim(studentProfileWrapper.imageID));
			pstmt.setString(49, Utility.trim(studentProfileWrapper.thumbnailID));
			pstmt.setString(50, Utility.trim(studentProfileWrapper.classTeacher));

			pstmt.setString(51, Utility.trim(studentProfileWrapper.recordStatus));
			pstmt.setString(52, Utility.trim(studentProfileWrapper.aadhaarNo));
			pstmt.setString(53, Utility.trim(studentProfileWrapper.busRouteNo));
			pstmt.setString(54, Utility.trim(studentProfileWrapper.busPickupPoint));
			pstmt.setString(55, Utility.trim(studentProfileWrapper.driverName));
			pstmt.setString(56, Utility.trim(studentProfileWrapper.driverName2));
			pstmt.setString(57, Utility.trim(studentProfileWrapper.driverMobileNo));
			pstmt.setString(58, Utility.trim(studentProfileWrapper.driverMobileNo2));
			pstmt.setString(59, Utility.trim(studentProfileWrapper.religion));
			pstmt.setString(60, Utility.trim(studentProfileWrapper.caste));
			pstmt.setString(61, Utility.trim(studentProfileWrapper.bloodGroup));

			pstmt.setString(62, Utility.trim(studentProfileWrapper.emiratesID));
			pstmt.setString(63, Utility.trim(studentProfileWrapper.visaNo));
			pstmt.setString(64, Utility.trim(studentProfileWrapper.uidNo));
			pstmt.setString(65, Utility.trim(studentProfileWrapper.passportNo));
			pstmt.setDate(66, Utility.getDate(studentProfileWrapper.passportExpiryDate));
			pstmt.setDate(67, Utility.getDate(studentProfileWrapper.passportIssueDate));
			pstmt.setString(68, Utility.trim(studentProfileWrapper.passportIssuePlace));
			pstmt.setString(69, Utility.trim(studentProfileWrapper.email));
			// pstmt.setString(69, Utility.trim(firstName).toLowerCase() + "."
			// + Utility.trim(studentProfileWrapper.surname).toLowerCase() + emailDomain);
			pstmt.setString(70, Utility.trim(studentProfileWrapper.physicallyChallenged));
			pstmt.setString(71, Utility.trim(studentProfileWrapper.identityMark1));
			pstmt.setString(72, Utility.trim(studentProfileWrapper.identityMark2));
			pstmt.setString(73, new DateToWordsConversion().dateToWords(studentProfileWrapper.dob)); // date of birth
																										// inwords
			pstmt.setString(74, Utility.trim(studentProfileWrapper.bankAccountNo));
			pstmt.setString(75, Utility.trim(studentProfileWrapper.bankName));
			pstmt.setString(76, Utility.trim(studentProfileWrapper.rationCardNo));
			pstmt.setString(77, Utility.trim(studentProfileWrapper.fatherAadhaarNo));
			pstmt.setString(78, Utility.trim(studentProfileWrapper.motherAadhaarNo));
			pstmt.setString(79, Utility.trim(studentProfileWrapper.parentAnnualIncome));
			pstmt.setString(80, Utility.trim(studentProfileWrapper.admissionNo));
			pstmt.setString(81, Utility.trim(studentProfileWrapper.sameAddressFlag));
			pstmt.setString(82, Utility.trim(studentProfileWrapper.casteCategory));

			System.out.println("insert usersProfileWrapper Userid " + usersProfileWrapper.userid + " schoolID "
					+ usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			studentProfileWrapper.recordFound = true;

			dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[1];
			dataArrayWrapper.studentProfileWrapper[0] = studentProfileWrapper;

			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully inserted into StudentProfile");

			// ------Create Login Profile
			UsersHelper usersHelper = new UsersHelper();
			UsersWrapper usersWrapper = new UsersWrapper();
			usersWrapper.userid = userid;
			usersWrapper.studentID = studentProfileWrapper.studentID;
			usersWrapper.refNo = studentProfileWrapper.refNo;
			usersWrapper.schoolID = usersProfileWrapper.schoolID;

			usersWrapper.password = studentProfileWrapper.password; // this is introduced for login
			usersWrapper.email = studentProfileWrapper.email; // this is introduced for login
			usersWrapper.mobile = studentProfileWrapper.mobile; // this is introduced for login
			usersWrapper.firebaseID = studentProfileWrapper.firebaseID; // this is for app image

			usersHelper.updateLoginProfile(usersProfileWrapper, usersWrapper, Utility.student_type);
			// ------

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

	// -----------------Start updateStudentProfile---------------------
	public AbstractWrapper updateStudentProfile(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

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
		int n = 1;

		try {
			con = getConnection();

			pstmt = con.prepareStatement("SELECT RefNo FROM StudentProfile WHERE RefNo=? and SchoolID=?");

			System.out.println("Student Profile  RefNo is" + studentProfileWrapper.refNo);

			pstmt.setString(1, studentProfileWrapper.refNo.trim());
			pstmt.setString(2, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();
				dataArrayWrapper = (DataArrayWrapper) insertStudentProfile(usersProfileWrapper, studentProfileWrapper);
			} else {
				resultSet.close();
				pstmt.close();

				pstmt = con.prepareStatement(
						"UPDATE StudentProfile SET BranchID=?,  StudentName=?, Surname=?, Address1=?,Address2=?, Address3=?, CityID=?, PINCode=?,"
								+ " DistrictID=?, StateID=?, PermAddress1=?, PermAddress2=?, PermAddress3=?, PermCityID=?, PermPINCode=?, PermDistrictID=?, PermStateID=?, GradeID=?, "
								+ "SectionID=?,  JoinDate=?, StudentPhotoID=?, DOB=?, Gender=?, FatherName=?, FatherSurname=?, FatherOccupation=?, FatherAge=?, "
								+ "FatherEducation=?, MotherName=?, MotherSurname=?, MotherOccupation=?, MotherAge=?, MotherEducation=?, PrimaryMobile=?, SecondaryMobile=?,PrimaryEmail=?, SecondaryEmail=?, "
								+ "Status=?, ModifierID=?, ModifierDateTime=?, ImageID=?, ThumbnailID=?, ClassTeacher=?, RecordStatus=?, "
								+ "AadhaarNo=?,BusRouteNo=?, BusPickupPoint=?, DriverName=?,DriverName2=?,DriverMobileNo=?,DriverMobileNo2=?, Religion=?, Caste=?, BloodGroup=?,EmiratesID=?, "
								+ "VisaNo=?,UIDNo=?,PassportNo=?,PassportExpiryDate=?,PassportIssueDate=?,PassportIssuePlace=?,StudentEmail=?,PhysicallyChallenged=?,IdentityMark1=?,IdentityMark2=?, "
								+ "DOBInWords=?,BankAccountNo=?,BankName=?,RationCardNo=?,FatherAadhaarNo=?,MotherAadhaarNo=?,ParentAnnualIncome=?,AdmissionNo=?,SameAddressFlag=?, CasteCategory=? "
								+ " WHERE AcademicYearID=? AND RefNo=? AND StudentID=? and SchoolID=?");

				// pstmt.setString(n, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.setString(n, Utility.trim(studentProfileWrapper.branchID));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentName));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.surname));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.address1));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.address2));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.address3));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.cityID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.pinCode));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.districtID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.stateID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permAddress1));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permAddress2));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permAddress3));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permCityID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permPINCode));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permDistrictID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.permStateID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.gradeID));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.sectionID));

				pstmt.setDate(++n, Utility.getDate(studentProfileWrapper.joinDate));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentPhotoID));
				pstmt.setDate(++n, Utility.getDate(studentProfileWrapper.dob));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.gender));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherName));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherSurname));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherOccupation));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherAge));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherEducation));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherName));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherSurname));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherOccupation));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherAge));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherEducation));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.primaryMobile));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.secondaryMobile));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.primaryEmail));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.secondaryEmail));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.status));

				pstmt.setString(++n, Utility.trim(usersProfileWrapper.userid)); // modifier id
				pstmt.setTimestamp(++n, Utility.getCurrentTime()); // modifier date time
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.imageID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.thumbnailID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.classTeacher));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.recordStatus));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.aadhaarNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.busRouteNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.busPickupPoint));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.driverName));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.driverName2));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.driverMobileNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.driverMobileNo2));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.religion));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.caste));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.bloodGroup));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.emiratesID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.visaNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.uidNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.passportNo));
				pstmt.setDate(++n, Utility.getDate(studentProfileWrapper.passportExpiryDate));
				pstmt.setDate(++n, Utility.getDate(studentProfileWrapper.passportIssueDate));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.passportIssuePlace));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentEmail));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.physicallyChallenged));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.identityMark1));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.identityMark2));
				pstmt.setString(++n, new DateToWordsConversion().dateToWords(studentProfileWrapper.dob));// date of birt
																											// inwords
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.bankAccountNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.bankName));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.rationCardNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.fatherAadhaarNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.motherAadhaarNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.parentAnnualIncome));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.admissionNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.sameAddressFlag));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.casteCategory));

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.academicYearID));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.refNo));
				pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentID));
				pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();

				studentProfileWrapper.recordFound = true;
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[1];
				dataArrayWrapper.studentProfileWrapper[0] = studentProfileWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Successfully StudentProfile Updated");
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
	// -----------------End updateStudentProfile---------------------

	// -----------------Start fetchStudentProfile---------------------

	public AbstractWrapper fetchStudentProfile(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchStudentProfile RefNo" + studentProfileWrapper.refNo);

		Vector<Object> vector = new Vector<Object>();

		try {
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement(
					"SELECT RefNo,SchoolID, BranchID, StudentID, StudentName, Surname, Address1,Address2, Address3, CityID, "
							+ "PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, PermPINCode, PermDistrictID, PermStateID, "
							+ "GradeID, SectionID, AcademicYearID, JoinDate, StudentPhotoID, DOB, Gender, FatherName, FatherSurname, FatherOccupation, "
							+ "FatherAge, FatherEducation, MotherName, MotherSurname, MotherOccupation, MotherAge, MotherEducation, PrimaryMobile, SecondaryMobile,PrimaryEmail, "
							+ "SecondaryEmail, Userid, Status, MakerID, MakerDateTime,ModifierID, ModifierDateTime,ImageID, ThumbnailID, ClassTeacher, RecordStatus, "
							+ "AadhaarNo,BusRouteNo, BusPickupPoint, DriverName ,DriverName2,DriverMobileNo,DriverMobileNo2, Religion, Caste, BloodGroup,EmiratesID, "
							+ "VisaNo,UIDNo,PassportNo,PassportExpiryDate,PassportIssueDate,PassportIssuePlace,StudentEmail,PhysicallyChallenged,IdentityMark1,IdentityMark2, "
							+ "DOBInWords,BankAccountNo,BankName,RationCardNo,FatherAadhaarNo,MotherAadhaarNo,ParentAnnualIncome,AdmissionNo,SameAddressFlag,CasteCategory, SchoolID FROM StudentProfile WHERE RefNo=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(studentProfileWrapper.refNo));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				studentProfileWrapper.branchID = Utility.trim(resultSet.getString("BranchID"));
				studentProfileWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentProfileWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentProfileWrapper.surname = Utility.trim(resultSet.getString("Surname"));
				studentProfileWrapper.address1 = Utility.trim(resultSet.getString("Address1"));
				studentProfileWrapper.address2 = Utility.trim(resultSet.getString("Address2"));
				studentProfileWrapper.address3 = Utility.trim(resultSet.getString("Address3"));
				studentProfileWrapper.cityID = Utility.trim(resultSet.getString("CityID"));

				studentProfileWrapper.pinCode = Utility.trim(resultSet.getString("PINCode"));
				studentProfileWrapper.districtID = Utility.trim(resultSet.getString("DistrictID"));
				studentProfileWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				studentProfileWrapper.permAddress1 = Utility.trim(resultSet.getString("PermAddress1"));
				studentProfileWrapper.permAddress2 = Utility.trim(resultSet.getString("PermAddress2"));
				studentProfileWrapper.permAddress3 = Utility.trim(resultSet.getString("PermAddress3"));
				studentProfileWrapper.permCityID = Utility.trim(resultSet.getString("PermCityID"));
				studentProfileWrapper.permPINCode = Utility.trim(resultSet.getString("PermPINCode"));
				studentProfileWrapper.permDistrictID = Utility.trim(resultSet.getString("PermDistrictID"));
				studentProfileWrapper.permStateID = Utility.trim(resultSet.getString("PermStateID"));

				studentProfileWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentProfileWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentProfileWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentProfileWrapper.joinDate = Utility.setDate(resultSet.getString("JoinDate"));
				studentProfileWrapper.joinDateMMM = Utility.setDateMMM(resultSet.getString("JoinDate"));
				studentProfileWrapper.studentPhotoID = Utility.trim(resultSet.getString("StudentPhotoID"));
				studentProfileWrapper.dob = Utility.setDate(resultSet.getString("DOB"));
				studentProfileWrapper.dobMMM = Utility.setDateMMM(resultSet.getString("DOB"));
				studentProfileWrapper.gender = Utility.trim(resultSet.getString("Gender"));
				studentProfileWrapper.fatherName = Utility.trim(resultSet.getString("FatherName"));
				studentProfileWrapper.fatherSurname = Utility.trim(resultSet.getString("FatherSurname"));
				studentProfileWrapper.fatherOccupation = Utility.trim(resultSet.getString("FatherOccupation"));

				studentProfileWrapper.fatherAge = Utility.trim(resultSet.getString("FatherAge"));
				studentProfileWrapper.fatherEducation = Utility.trim(resultSet.getString("FatherEducation"));
				studentProfileWrapper.motherName = Utility.trim(resultSet.getString("MotherName"));
				studentProfileWrapper.motherSurname = Utility.trim(resultSet.getString("MotherSurname"));
				studentProfileWrapper.motherOccupation = Utility.trim(resultSet.getString("MotherOccupation"));
				studentProfileWrapper.motherAge = Utility.trim(resultSet.getString("MotherAge"));
				studentProfileWrapper.motherEducation = Utility.trim(resultSet.getString("MotherEducation"));
				studentProfileWrapper.primaryMobile = Utility.trim(resultSet.getString("PrimaryMobile"));
				studentProfileWrapper.secondaryMobile = Utility.trim(resultSet.getString("SecondaryMobile"));
				studentProfileWrapper.primaryEmail = Utility.trim(resultSet.getString("PrimaryEmail"));

				studentProfileWrapper.secondaryEmail = Utility.trim(resultSet.getString("SecondaryEmail"));
				studentProfileWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				studentProfileWrapper.status = Utility.trim(resultSet.getString("Status"));
				studentProfileWrapper.makerID = Utility.trim(resultSet.getString("MakerID"));
				studentProfileWrapper.makerDateTime = Utility.setDate(resultSet.getString("MakerDateTime"));
				studentProfileWrapper.modifierID = Utility.trim(resultSet.getString("ModifierID"));
				studentProfileWrapper.modifierDateTime = Utility.setDate(resultSet.getString("ModifierDateTime"));
				studentProfileWrapper.imageID = Utility.trim(resultSet.getString("ImageID"));
				studentProfileWrapper.thumbnailID = Utility.trim(resultSet.getString("ThumbnailID"));
				studentProfileWrapper.classTeacher = Utility.trim(resultSet.getString("ClassTeacher"));
				studentProfileWrapper.recordStatus = Utility.trim(resultSet.getString("RecordStatus"));

				studentProfileWrapper.aadhaarNo = Utility.trim(resultSet.getString("AadhaarNo"));
				studentProfileWrapper.busRouteNo = Utility.trim(resultSet.getString("BusRouteNo"));
				studentProfileWrapper.busPickupPoint = Utility.trim(resultSet.getString("BusPickupPoint"));
				studentProfileWrapper.driverName = Utility.trim(resultSet.getString("DriverName"));
				studentProfileWrapper.driverName2 = Utility.trim(resultSet.getString("DriverName2"));
				studentProfileWrapper.driverMobileNo = Utility.trim(resultSet.getString("DriverMobileNo"));
				studentProfileWrapper.driverMobileNo2 = Utility.trim(resultSet.getString("DriverMobileNo2"));
				studentProfileWrapper.religion = Utility.trim(resultSet.getString("Religion"));
				studentProfileWrapper.caste = Utility.trim(resultSet.getString("Caste"));
				studentProfileWrapper.bloodGroup = Utility.trim(resultSet.getString("BloodGroup"));

				studentProfileWrapper.emiratesID = Utility.trim(resultSet.getString("EmiratesID"));
				studentProfileWrapper.visaNo = Utility.trim(resultSet.getString("VisaNo"));
				studentProfileWrapper.uidNo = Utility.trim(resultSet.getString("UIDNo"));
				studentProfileWrapper.passportNo = Utility.trim(resultSet.getString("PassportNo"));
				studentProfileWrapper.passportExpiryDate = Utility.setDate(resultSet.getString("PassportExpiryDate"));
				studentProfileWrapper.passportExpiryDateMMM = Utility
						.setDateMMM(resultSet.getString("PassportExpiryDate"));
				studentProfileWrapper.passportIssueDate = Utility.setDate(resultSet.getString("PassportIssueDate"));
				studentProfileWrapper.passportIssueDateMMM = Utility
						.setDateMMM(resultSet.getString("PassportIssueDate"));
				studentProfileWrapper.passportIssuePlace = Utility.trim(resultSet.getString("PassportIssuePlace"));

				studentProfileWrapper.studentEmail = Utility.trim(resultSet.getString("StudentEmail"));
				studentProfileWrapper.physicallyChallenged = Utility.trim(resultSet.getString("PhysicallyChallenged"));
				studentProfileWrapper.identityMark1 = Utility.trim(resultSet.getString("IdentityMark1"));
				studentProfileWrapper.identityMark2 = Utility.trim(resultSet.getString("IdentityMark2"));
				studentProfileWrapper.dobInWords = Utility.trim(resultSet.getString("DOBInWords"));
				studentProfileWrapper.bankAccountNo = Utility.trim(resultSet.getString("BankAccountNo"));
				studentProfileWrapper.bankName = Utility.trim(resultSet.getString("BankName"));
				studentProfileWrapper.rationCardNo = Utility.trim(resultSet.getString("RationCardNo"));
				studentProfileWrapper.fatherAadhaarNo = Utility.trim(resultSet.getString("FatherAadhaarNo"));
				studentProfileWrapper.motherAadhaarNo = Utility.trim(resultSet.getString("MotherAadhaarNo"));
				studentProfileWrapper.parentAnnualIncome = Utility.trim(resultSet.getString("ParentAnnualIncome"));
				studentProfileWrapper.admissionNo = Utility.trim(resultSet.getString("AdmissionNo"));
				studentProfileWrapper.sameAddressFlag = Utility.trim(resultSet.getString("SameAddressFlag"));
				studentProfileWrapper.casteCategory = Utility.trim(resultSet.getString("CasteCategory"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentProfileWrapper.schoolIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.schoolID,
						"MST_School", studentProfileWrapper.schoolID);
				studentProfileWrapper.branchIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.branchID,
						"MST_Branch", studentProfileWrapper.schoolID);
				studentProfileWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.gradeID,
						"MST_Grade", studentProfileWrapper.schoolID);
				studentProfileWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.sectionID,
						"MST_Section", studentProfileWrapper.schoolID);
				studentProfileWrapper.cityIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.cityID,
						"MST_City", studentProfileWrapper.schoolID);
				studentProfileWrapper.districtIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.districtID,
						"MST_District", studentProfileWrapper.schoolID);
				studentProfileWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.academicYearID, "MST_AcademicYear", studentProfileWrapper.schoolID);
				studentProfileWrapper.stateIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.stateID,
						"MST_State", studentProfileWrapper.schoolID);
				studentProfileWrapper.genderValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.gender,
						"MST_Gender", studentProfileWrapper.schoolID);
				studentProfileWrapper.fatherOccupationValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.fatherOccupation, "MST_Occupation", studentProfileWrapper.schoolID);
				studentProfileWrapper.motherOccupationValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.motherOccupation, "MST_Occupation", studentProfileWrapper.schoolID);
				studentProfileWrapper.fatherEducationValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.fatherEducation, "MST_Education", studentProfileWrapper.schoolID);
				studentProfileWrapper.motherEducationValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.motherEducation, "MST_Education", studentProfileWrapper.schoolID);
				studentProfileWrapper.religionValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.religion,
						"MST_Religion", studentProfileWrapper.schoolID);
				studentProfileWrapper.physicallyChallengedValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.physicallyChallenged, "MST_Decision", studentProfileWrapper.schoolID);

				studentProfileWrapper.casteValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.caste,
						"MST_Caste", studentProfileWrapper.schoolID);
				studentProfileWrapper.bloodGroupValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.bloodGroup,
						"MST_BloodGroup", studentProfileWrapper.schoolID);
				studentProfileWrapper.bankNameValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.bankName,
						"MST_Bank", studentProfileWrapper.schoolID);
				studentProfileWrapper.busRouteNoValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.busRouteNo,
						"MST_BusRoute", studentProfileWrapper.schoolID);
				studentProfileWrapper.busPickupPointValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.busPickupPoint, "MST_BusPickupPoint", studentProfileWrapper.schoolID);
				studentProfileWrapper.casteCategoryValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.casteCategory, "MST_CasteCategory", studentProfileWrapper.schoolID);

				studentProfileWrapper.recordFound = true;
				System.out.println("StudentProfile Details fetch successful");

				vector.addElement(studentProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentProfileWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}
			resultSet.close();
			pstmt.close();

			if (dataArrayWrapper.studentProfileWrapper != null) {
				dataArrayWrapper = (DataArrayWrapper) fetchStudentImage(usersProfileWrapper, dataArrayWrapper);
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
	// -----------------End fetchStudentProfile---------------------

	// -----------------Generate RefNo-------------------------------
	public String generateRefNo(UsersWrapper usersProfileWrapper) throws Exception {

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
		String schoolCode = null;

		try {
			con = getConnection();

			sql = "SELECT RefNo,SchoolCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				refNo = resultSet.getInt("RefNo");
				System.out.println("RefNo" + refNo);
				schoolCode = resultSet.getString("SchoolCode");

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
			pstmt.setString(2, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 4;

			// int paddingSize=6-String.valueOf(refNo).length();

			// System.out.println("Savings Account " + studentProfileWrapper.accountType);

			// System.out.println("Savings Account " +
			// studentProfileWrapper.accountType.substring(0,2));

			// finalRefNo=schoolCode+dmyFormat.format(new
			// java.util.Date()).toUpperCase()+String.format("%0" + paddingSize +"d",refNo);

			finalRefNo = schoolCode.toUpperCase() + String.format("%0" + paddingSize + "d", refNo);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out
					.println("Successfully generated refno " + finalRefNo + "schoolID " + usersProfileWrapper.schoolID);

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

	// -----------------Generate StudentID-------------------------------
	public String generateStudentID(UsersWrapper usersProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;

		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);

		int studentID = 0;
		String finalStudentID = null;
		String schoolCode = null;

		try {
			con = getConnection();

			sql = "SELECT StudentID,SchoolCode from MST_Parameter where SchoolID=?";

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, usersProfileWrapper.schoolID);
			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				studentID = resultSet.getInt("StudentID");
				System.out.println("studentID" + studentID);
				schoolCode = resultSet.getString("SchoolCode");

			}

			resultSet.close();
			pstmt.close();

			if (studentID == 0) {
				studentID = 1;

			} else {

				studentID = studentID + 1;
			}

			sql = "UPDATE MST_Parameter set StudentID=? where SchoolID=?";

			System.out.println("sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, studentID);
			pstmt.setString(2, usersProfileWrapper.schoolID);

			pstmt.executeUpdate();
			pstmt.close();

			int paddingSize = 4;

			// int paddingSize=6-String.valueOf(studentID).length();

			// System.out.println("Savings Account " + studentProfileWrapper.accountType);

			// System.out.println("Savings Account " +
			// studentProfileWrapper.accountType.substring(0,2));

			// finalStudentID=schoolCode+dmyFormat.format(new
			// java.util.Date()).toUpperCase()+String.format("%0" +paddingSize
			// +"d",studentID);

			finalStudentID = String.format("%0" + paddingSize + "d", studentID);

			// studentProfileWrapper.recordFound=true;

			// dataArrayWrapper.studentProfileWrapper=new StudentProfileWrapper[1];
			// dataArrayWrapper.studentProfileWrapper[0]=studentProfileWrapper;
			// dataArrayWrapper.recordFound=true;

			System.out.println("Successfully generated studentid " + finalStudentID);

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

		return finalStudentID;
	}

	// -----------------End Generate RefNo---------------------------

	// ----------------Queue Students------------------------------

	// -----------------Start fetchStudent search---------------------

	public AbstractWrapper fetchStudentSearch(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		String sql = "";
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		int n = 1;

		try {

			PopoverHelper popoverHelper = new PopoverHelper();
			con = getConnection();

			sql = " WHERE SchoolID=? ";

			if (studentProfileWrapper.studentID != null && !Utility.isEmpty(studentProfileWrapper.studentID)) {

				// if (Utility.isStringInteger(studentProfileWrapper.studentID)) {
				// sql = sql + " and StudentID =? ";
				// System.out.println("studentProfileWrapper StudentID " + sql);
				//
				// } else {
				sql = sql + " and (StudentID LIKE ? or UPPER(StudentName) LIKE ? or UPPER(Surname) LIKE ? ) ";

				System.out.println("studentProfileWrapper name " + sql);

				// }
				// else if (studentProfileWrapper.studentName != null &&
				// !studentProfileWrapper.studentName.equals("")) {
				//
				// sql = " and UPPER(StudentName) LIKE ?";
				//
				// System.out.println(" Student Name " + studentProfileWrapper.studentName);
				//
				// } else if (studentProfileWrapper.surname != null &&
				// !studentProfileWrapper.surname.equals("")) {
				//
				// sql = " and UPPER(Surname) LIKE ?";
				//
				// System.out.println(" Surname " + studentProfileWrapper.surname);
				//
				// }

			}

			if (studentProfileWrapper.gradeID != null && !Utility.isEmpty(studentProfileWrapper.gradeID)) {

				sql = sql + " and GradeID = ?";

			}

			if (studentProfileWrapper.sectionID != null && !Utility.isEmpty(studentProfileWrapper.sectionID)) {

				sql = sql + " and SectionID = ?";

				System.out.println("SQL in SectionID is " + sql);

			}
			/*
			 * else if(studentProfileWrapper.academicYearID !=null &&
			 * !studentProfileWrapper.academicYearID.equals("") &&
			 * studentProfileWrapper.studentName !=null &&
			 * !studentProfileWrapper.studentName.equals("") &&
			 * studentProfileWrapper.surname !=null &&
			 * !studentProfileWrapper.surname.equals("")) {
			 * 
			 * sql=" WHERE AcademicYearID = ? AND UPPER(StudentName) LIKE ? OR UPPER(Surname) LIKE ?"
			 * ;
			 * 
			 * 
			 * }
			 * 
			 */
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT RefNo,SchoolID, BranchID, StudentID, StudentName, Surname, Address1,Address2, Address3, CityID,"
							+ "PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, PermPINCode, PermDistrictID, PermStateID,"
							+ "GradeID, SectionID, AcademicYearID, JoinDate, StudentPhotoID, DOB, Gender, FatherName, FatherSurname, FatherOccupation,"
							+ "FatherAge, FatherEducation, MotherName, MotherSurname, MotherOccupation, MotherAge, MotherEducation, PrimaryMobile, SecondaryMobile,PrimaryEmail,"
							+ "SecondaryEmail, Userid, Status, MakerID, MakerDateTime,ModifierID, ModifierDateTime,ImageID, ThumbnailID, ClassTeacher, RecordStatus, SchoolID FROM StudentProfile "
							+ sql);

			pstmt.setString(n, usersProfileWrapper.schoolID);

			if (studentProfileWrapper.studentID != null && !Utility.isEmpty(studentProfileWrapper.studentID)) {

				// if (Utility.isStringInteger(studentProfileWrapper.studentID)) {
				// } else {
				pstmt.setString(++n, '%' + Utility.trim(studentProfileWrapper.studentID).toUpperCase() + '%');
				pstmt.setString(++n, '%' + Utility.trim(studentProfileWrapper.studentID).toUpperCase() + '%');
				pstmt.setString(++n, '%' + Utility.trim(studentProfileWrapper.studentID).toUpperCase() + '%');
				// }

			}

			// else if (studentProfileWrapper.studentName != null &&
			// !studentProfileWrapper.studentName.trim().isEmpty()) {
			//
			// pstmt.setString(2, '%' +
			// studentProfileWrapper.studentName.trim().toUpperCase() + '%');
			//
			// } else if (studentProfileWrapper.surname != null &&
			// !studentProfileWrapper.surname.trim().isEmpty()) {
			//
			// pstmt.setString(2, '%' + studentProfileWrapper.surname.trim().toUpperCase() +
			// '%');
			//
			// }

			if (studentProfileWrapper.gradeID != null && !Utility.isEmpty(studentProfileWrapper.gradeID)) {

				pstmt.setString(++n, studentProfileWrapper.gradeID.trim());

			}
			if (studentProfileWrapper.sectionID != null && !Utility.isEmpty(studentProfileWrapper.sectionID)

			) {
				System.out.println("SectionID is " + studentProfileWrapper.sectionID.trim());
				pstmt.setString(++n, studentProfileWrapper.sectionID.trim());

			}

			/*
			 * else if(studentProfileWrapper.academicYearID !=null &&
			 * !studentProfileWrapper.academicYearID.equals("") &&
			 * studentProfileWrapper.studentName !=null &&
			 * !studentProfileWrapper.studentName.equals("") &&
			 * studentProfileWrapper.surname !=null &&
			 * !studentProfileWrapper.surname.equals("")) {
			 * 
			 * 
			 * pstmt.setString(1,studentProfileWrapper.academicYearID.trim());
			 * 
			 * pstmt.setString(2,studentProfileWrapper.studentName.trim());
			 * 
			 * pstmt.setString(3,studentProfileWrapper.surname.trim());
			 * 
			 * }
			 */
			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				studentProfileWrapper.branchID = Utility.trim(resultSet.getString("BranchID"));
				studentProfileWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
				/*
				 * studentProfileWrapper.address1=Utility.trim(resultSet.getString("Address1"));
				 * studentProfileWrapper.address2=Utility.trim(resultSet.getString("Address2"));
				 * studentProfileWrapper.address3=Utility.trim(resultSet.getString("Address3"));
				 */
				// studentProfileWrapper.pinCode=Utility.trim(resultSet.getString("PINCode"));
				studentProfileWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				studentProfileWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentProfileWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentProfileWrapper.surname = Utility.trim(resultSet.getString("Surname"));
				studentProfileWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentProfileWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentProfileWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentProfileWrapper.joinDate = Utility.setDate(resultSet.getString("JoinDate"));
				// studentProfileWrapper.studentPhotoID=Utility.trim(resultSet.getString("StudentPhotoID"));
				// studentProfileWrapper.dob=Utility.setDate(resultSet.getString("DOB"));
				studentProfileWrapper.gender = Utility.trim(resultSet.getString("Gender"));
				studentProfileWrapper.fatherName = Utility.trim(resultSet.getString("FatherName"));
				studentProfileWrapper.fatherSurname = Utility.trim(resultSet.getString("FatherSurname"));
				// studentProfileWrapper.fatherOccupation=Utility.trim(resultSet.getString("FatherOccupation"));
				// studentProfileWrapper.fatherAge=Utility.trim(resultSet.getString("FatherAge"));
				/*
				 * studentProfileWrapper.fatherEducation=Utility.trim(resultSet.getString(
				 * "FatherEducation"));
				 * studentProfileWrapper.motherName=Utility.trim(resultSet.getString(
				 * "MotherName"));
				 * studentProfileWrapper.motherSurname=Utility.trim(resultSet.getString(
				 * "MotherSurname"));
				 * studentProfileWrapper.motherOccupation=Utility.trim(resultSet.getString(
				 * "MotherOccupation"));
				 * studentProfileWrapper.motherAge=Utility.trim(resultSet.getString("MotherAge")
				 * ); studentProfileWrapper.motherEducation=Utility.trim(resultSet.getString(
				 * "MotherEducation"));
				 * studentProfileWrapper.primaryMobile=Utility.trim(resultSet.getString(
				 * "PrimaryMobile"));
				 * studentProfileWrapper.secondaryMobile=Utility.trim(resultSet.getString(
				 * "SecondaryMobile"));
				 * studentProfileWrapper.primaryEmail=Utility.trim(resultSet.getString(
				 * "PrimaryEmail"));
				 * studentProfileWrapper.secondaryEmail=Utility.trim(resultSet.getString(
				 * "SecondaryEmail"));
				 */

				studentProfileWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				studentProfileWrapper.status = Utility.trim(resultSet.getString("Status"));
				/*
				 * studentProfileWrapper.makerID=Utility.trim(resultSet.getString("MakerID"));
				 * studentProfileWrapper.makerDateTime=Utility.setDate(resultSet.getString(
				 * "MakerDateTime"));
				 * studentProfileWrapper.modifierID=Utility.trim(resultSet.getString(
				 * "ModifierID"));
				 * studentProfileWrapper.modifierDateTime=Utility.setDate(resultSet.getString(
				 * "ModifierDateTime"));
				 */
				studentProfileWrapper.avatar = "";

				studentProfileWrapper.recordFound = true;
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentProfileWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.gradeID,
						"MST_Grade", studentProfileWrapper.schoolID);
				studentProfileWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.sectionID,
						"MST_Section", studentProfileWrapper.schoolID);
				studentProfileWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						studentProfileWrapper.academicYearID, "MST_AcademicYear", studentProfileWrapper.schoolID);

				System.out.println("StudentProfile Queue fetch successful");

				vector.addElement(studentProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentProfileWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			} else {
				studentProfileWrapper.recordFound = false;
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[1];
				dataArrayWrapper.studentProfileWrapper[0] = studentProfileWrapper;
				dataArrayWrapper.recordFound = true;

			}

			resultSet.close();
			pstmt.close();

			if (dataArrayWrapper.studentProfileWrapper != null) {
				dataArrayWrapper = (DataArrayWrapper) fetchStudentImage(usersProfileWrapper, dataArrayWrapper);
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
	// -----------------End fetchStudentsSearch---------------------

	// -----------------Start fetchStudentsQueue---------------------

	public AbstractWrapper fetchStudentsQueue(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();

		try {

			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement(
					"SELECT RefNo,SchoolID, BranchID, StudentID, StudentName, Surname, Address1,Address2, Address3, CityID,"
							+ "PINCode, DistrictID, StateID, PermAddress1, PermAddress2, PermAddress3, PermCityID, PermPINCode, PermDistrictID, PermStateID,"
							+ "GradeID, SectionID, AcademicYearID, JoinDate, StudentPhotoID, DOB, Gender, FatherName, FatherSurname, FatherOccupation,"
							+ "FatherAge, FatherEducation, MotherName, MotherSurname, MotherOccupation, MotherAge, MotherEducation, PrimaryMobile, SecondaryMobile,PrimaryEmail,"
							+ "SecondaryEmail, Userid, Status, MakerID, MakerDateTime,ModifierID, ModifierDateTime,ImageID, ThumbnailID, ClassTeacher, RecordStatus, SchoolID FROM StudentProfile where SchoolID=?");

			pstmt.setString(1, usersProfileWrapper.schoolID);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				studentProfileWrapper.branchID = Utility.trim(resultSet.getString("BranchID"));
				studentProfileWrapper.cityID = Utility.trim(resultSet.getString("CityID"));
				/*
				 * studentProfileWrapper.address1=Utility.trim(resultSet.getString("Address1"));
				 * studentProfileWrapper.address2=Utility.trim(resultSet.getString("Address2"));
				 * studentProfileWrapper.address3=Utility.trim(resultSet.getString("Address3"));
				 */
				// studentProfileWrapper.pinCode=Utility.trim(resultSet.getString("PINCode"));
				studentProfileWrapper.stateID = Utility.trim(resultSet.getString("StateID"));
				studentProfileWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentProfileWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentProfileWrapper.surname = Utility.trim(resultSet.getString("Surname"));
				studentProfileWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentProfileWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentProfileWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentProfileWrapper.joinDate = Utility.setDate(resultSet.getString("JoinDate"));
				// studentProfileWrapper.studentPhotoID=Utility.trim(resultSet.getString("StudentPhotoID"));
				// studentProfileWrapper.dob=Utility.setDate(resultSet.getString("DOB"));
				studentProfileWrapper.gender = Utility.trim(resultSet.getString("Gender"));
				studentProfileWrapper.fatherName = Utility.trim(resultSet.getString("FatherName"));
				studentProfileWrapper.fatherSurname = Utility.trim(resultSet.getString("FatherSurname"));
				// studentProfileWrapper.fatherOccupation=Utility.trim(resultSet.getString("FatherOccupation"));
				// studentProfileWrapper.fatherAge=Utility.trim(resultSet.getString("FatherAge"));
				/*
				 * studentProfileWrapper.fatherEducation=Utility.trim(resultSet.getString(
				 * "FatherEducation"));
				 * studentProfileWrapper.motherName=Utility.trim(resultSet.getString(
				 * "MotherName"));
				 * studentProfileWrapper.motherSurname=Utility.trim(resultSet.getString(
				 * "MotherSurname"));
				 * studentProfileWrapper.motherOccupation=Utility.trim(resultSet.getString(
				 * "MotherOccupation"));
				 * studentProfileWrapper.motherAge=Utility.trim(resultSet.getString("MotherAge")
				 * ); studentProfileWrapper.motherEducation=Utility.trim(resultSet.getString(
				 * "MotherEducation"));
				 * studentProfileWrapper.primaryMobile=Utility.trim(resultSet.getString(
				 * "PrimaryMobile"));
				 * studentProfileWrapper.secondaryMobile=Utility.trim(resultSet.getString(
				 * "SecondaryMobile"));
				 * studentProfileWrapper.primaryEmail=Utility.trim(resultSet.getString(
				 * "PrimaryEmail"));
				 * studentProfileWrapper.secondaryEmail=Utility.trim(resultSet.getString(
				 * "SecondaryEmail"));
				 */
				/*
				 * studentProfileWrapper.userid=Utility.trim(resultSet.getString("Userid"));
				 * studentProfileWrapper.status=Utility.trim(resultSet.getString("Status"));
				 * studentProfileWrapper.makerID=Utility.trim(resultSet.getString("MakerID"));
				 * studentProfileWrapper.makerDateTime=Utility.setDate(resultSet.getString(
				 * "MakerDateTime"));
				 * studentProfileWrapper.modifierID=Utility.trim(resultSet.getString(
				 * "ModifierID"));
				 * studentProfileWrapper.modifierDateTime=Utility.setDate(resultSet.getString(
				 * "ModifierDateTime"));
				 */

				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentProfileWrapper.recordFound = true;

				System.out.println("StudentProfile Queue fetch successful");

				vector.addElement(studentProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentProfileWrapper);
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
	// -----------------End fetchStudentsQueue---------------------

	// -----------------Start fetchStudentProfile---------------------

	public AbstractWrapper fetchStudentID(UsersWrapper usersProfileWrapper, StudentProfileWrapper studentProfileWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("fetchStudentProfile RefNo" + studentProfileWrapper.refNo);

		Vector<Object> vector = new Vector<Object>();

		try {

			con = getConnection();

			PreparedStatement pstmt = con.prepareStatement(
					"SELECT RefNo,StudentID, SchoolID  FROM StudentProfile WHERE StudentID=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(studentProfileWrapper.studentID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));

				studentProfileWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentProfileWrapper.recordFound = true;

				System.out.println("StudentProfile Details fetch successful");

				vector.addElement(studentProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentProfileWrapper);
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

	// -----------------Start fetchClassStudents---------------------

	public AbstractWrapper fetchClassStudents(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		// String currentAcademicYear=null;

		String sql = null;
		int n = 1;

		try {
			System.out.println("in fetchClassStudents");
			PopoverHelper popoverHelper = new PopoverHelper();

			con = getConnection();

			// ----Current Academic Year--

			if (Utility.isEmpty(studentProfileWrapper.academicYearID)) {

				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				studentProfileWrapper.academicYearID = parameterWrapper.currentAcademicYear;

			}

			// ----------

			sql = "SELECT RefNo,StudentID, StudentName, Surname, GradeID, SectionID, "
					+ " AcademicYearID,Gender,ClassTeacher, SchoolID FROM StudentProfile WHERE AcademicYearID=?  AND SchoolID=? ";

			if (Utility.isEmpty(studentProfileWrapper.gradeID) == false) {

				sql = sql + " AND GradeID=?";
			}
			if (Utility.isEmpty(studentProfileWrapper.sectionID) == false) {

				sql = sql + " AND SectionID=?";
			}

			pstmt = con.prepareStatement(sql);

			System.out.println("currentAcademicYear " + studentProfileWrapper.academicYearID);

			pstmt.setString(n, Utility.trim(studentProfileWrapper.academicYearID));

			pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(studentProfileWrapper.gradeID)) {

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.gradeID));
			}
			if (!Utility.isEmpty(studentProfileWrapper.sectionID)) {

				pstmt.setString(++n, Utility.trim(studentProfileWrapper.sectionID));
			}

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				studentProfileWrapper = new StudentProfileWrapper();

				studentProfileWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));
				studentProfileWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				studentProfileWrapper.studentName = Utility.trim(resultSet.getString("StudentName"));
				studentProfileWrapper.surname = Utility.trim(resultSet.getString("Surname"));
				studentProfileWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				studentProfileWrapper.sectionID = Utility.trim(resultSet.getString("SectionID"));
				studentProfileWrapper.academicYearID = Utility.trim(resultSet.getString("AcademicYearID"));
				studentProfileWrapper.gender = Utility.trim(resultSet.getString("Gender"));
				studentProfileWrapper.classTeacher = Utility.trim(resultSet.getString("ClassTeacher"));
				studentProfileWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				studentProfileWrapper.recordFound = true;

				studentProfileWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);
				studentProfileWrapper.sectionIDValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.sectionID,
						"MST_Section", usersProfileWrapper.schoolID);
				studentProfileWrapper.genderValue = popoverHelper.fetchPopoverDesc(studentProfileWrapper.gender,
						"MST_Gender", usersProfileWrapper.schoolID);

				System.out.println("fetch ClassStudents successful");

				vector.addElement(studentProfileWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.studentProfileWrapper);
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
	// -----------------End fetchClassStudents---------------------

	// ----------------fetch student image for search
	public AbstractWrapper fetchStudentImage(UsersWrapper usersProfileWrapper, DataArrayWrapper dataArrayWrapper)
			throws Exception {

		try {
			// BufferedImage img = ImageIO.read(new File("C://testfile.jpg"+ ""));

			ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

			// "refNo" : sharedProperties.getRefNo(), //'SA21OCT2015156',
			// //sharedProperties.getRefNo() //SA28OCT201500001
			// "studentID": sharedProperties.getStudentID(),
			// //'SA21OCT2015156_25JAN2016173153751.jpg',
			// "docID" : 'DOC001'

			// System.out.println("Fetch Image Details Helper " + methodAction);

			for (int i = 0; i <= dataArrayWrapper.studentProfileWrapper.length - 1; i++) {

				ImageDetailsWrapper imageDetailsWrapper = new ImageDetailsWrapper();
				// imageDetailsWrapper.refNo = dataArrayWrapper.studentProfileWrapper[i].refNo;
				imageDetailsWrapper.studentID = dataArrayWrapper.studentProfileWrapper[i].studentID;
				imageDetailsWrapper.docID = "DOC001";

				DataArrayWrapper imgDataArrayWrapper = (DataArrayWrapper) imageDetailsHelper
						.fetchImageDetails(usersProfileWrapper, imageDetailsWrapper);

				if (imgDataArrayWrapper.recordFound == true && imgDataArrayWrapper.imageDetailsWrapper.length > 0) {

					imageDetailsWrapper = imgDataArrayWrapper.imageDetailsWrapper[0];

					System.out.println("file path to fetch " + imageDetailsWrapper.imageFileFolder + File.separator
							+ imageDetailsWrapper.imageFileName);
					// System.out.println(new File(
					// imageDetailsWrapper.imageFileFolder + File.separator +
					// imageDetailsWrapper.imageFileName)
					// .getCanonicalPath());

					// commented on 07-May-2019 because images are stored in Firebase
					// BufferedImage img = ImageIO.read(new File(
					// imageDetailsWrapper.imageFileFolder + File.separator +
					// imageDetailsWrapper.imageFileName));
					//
					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// ImageIO.write(img, "jpg", baos);
					// baos.flush();
					// byte[] imageInByte = baos.toByteArray();
					// // String encodedString = Base64.getEncoder().encodeToString(imageInByte);
					// String encodedString = new String(Base64.encodeBase64(imageInByte));
					// dataArrayWrapper.studentProfileWrapper[i].avatar = encodedString;
					// baos.close();
					// System.out.println("Image added to JSON");

					// imageDetailsWrapper.imageFoundStatus=true;
					// dataArrayWrapper.imageDetailsWrapper[0]=imageDetailsWrapper;

					dataArrayWrapper.studentProfileWrapper[i].avatar = imageDetailsWrapper.imageFileFolder;

					System.out.println("Fetch Image success in servlet ");

				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

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
			// try
			// {
			// releaseConnection(resultSet, con);
			// }
			// catch (SQLException se)
			// {
			// se.printStackTrace();
			// throw new Exception(se.getSQLState()+ " ; "+ se.getMessage());
			// }
		}

		return dataArrayWrapper;
	}

	public String fetchStudentImage(UsersWrapper usersProfileWrapper, String studentID) throws Exception {

		String avatar = null;
		try {

			ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();
			ImageDetailsWrapper imageDetailsWrapper = new ImageDetailsWrapper();
			imageDetailsWrapper.studentID = studentID;
			imageDetailsWrapper.docID = "DOC001";

			DataArrayWrapper imgDataArrayWrapper = (DataArrayWrapper) imageDetailsHelper
					.fetchImageDetails(usersProfileWrapper, imageDetailsWrapper);

			if (imgDataArrayWrapper.recordFound == true && imgDataArrayWrapper.imageDetailsWrapper.length > 0) {

				imageDetailsWrapper = imgDataArrayWrapper.imageDetailsWrapper[0];

				System.out.println("file path to fetch " + imageDetailsWrapper.imageFileFolder + File.separator
						+ imageDetailsWrapper.imageFileName);

				avatar = imageDetailsWrapper.imageFileFolder;

				System.out.println("fetchStudentImage");

			}

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());

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

		return avatar;
	}
	// ----------end of fetch image details

	// -----------------Start updateStudentProfile---------------------
	public AbstractWrapper migrateStudentProfile(UsersWrapper usersProfileWrapper,
			StudentProfileWrapper studentProfileWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;
		int n = 1;

		try {
			con = getConnection();

			usersProfileWrapper.schoolID = studentProfileWrapper.schoolID;

			// generate new ref no
			studentProfileWrapper.refNo = generateRefNo(usersProfileWrapper);
			// generate new student id
			studentProfileWrapper.studentID = generateStudentID(usersProfileWrapper);
			// generate new userid
			studentProfileWrapper.userid = studentProfileWrapper.studentID + "@" + usersProfileWrapper.schoolID;

			System.out.println(" migration " + studentProfileWrapper.primaryEmail + ":" + studentProfileWrapper.refNo);
			System.out.println(" migration1 " + studentProfileWrapper.studentID + ":" + studentProfileWrapper.userid);

			// update Student Profile table
			pstmt = con.prepareStatement(
					"UPDATE StudentProfile SET RefNo=?, SchoolID=?, StudentID=?, Userid=? " + " WHERE  PrimaryEmail=?");

			pstmt.setString(n, Utility.trim(studentProfileWrapper.refNo));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.schoolID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.userid));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.primaryEmail));

			pstmt.executeUpdate();
			pstmt.close();

			System.out.println(" migration student profile updated");

			// update Users table
			n = 1;
			pstmt = con.prepareStatement("UPDATE Users set RefNo=?, SchoolID=?, StudentID=?, userid=? where email=?");
			pstmt.setString(n, Utility.trim(studentProfileWrapper.refNo));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.schoolID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.userid));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.primaryEmail));

			pstmt.executeUpdate();
			pstmt.close();

			System.out.println(" migration users updated");

			// update UserStudentMap table
			n = 1;
			pstmt = con.prepareStatement(
					"UPDATE UserStudentMap set RefNo=?, SchoolID=?, StudentID=?, userid=? where email=?");
			pstmt.setString(n, Utility.trim(studentProfileWrapper.refNo));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.schoolID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.studentID));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.userid));
			pstmt.setString(++n, Utility.trim(studentProfileWrapper.primaryEmail));

			pstmt.executeUpdate();
			pstmt.close();

			System.out.println(" migration userstudentmap updated");

			// assign menu access rights
			PopoverHelper popoverHelper = new PopoverHelper();
			SchoolWrapper schoolWrapper = new SchoolWrapper();
			schoolWrapper.schoolID = studentProfileWrapper.schoolID;
			schoolWrapper.adminID = studentProfileWrapper.userid;
			popoverHelper.createUserMenu(schoolWrapper);

			System.out.println(" migration usermenu updated");

			studentProfileWrapper.recordFound = true;
			dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[1];
			dataArrayWrapper.studentProfileWrapper[0] = studentProfileWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("Successfully StudentProfile Migrated");

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
	// -----------------End updateStudentProfile---------------------

}
