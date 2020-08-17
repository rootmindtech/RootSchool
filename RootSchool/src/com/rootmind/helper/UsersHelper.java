package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;


import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.controller.AES128Crypto;
import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.PasswordWrapper;
import com.rootmind.wrapper.SchoolWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.TeachersProfileWrapper;
import com.rootmind.wrapper.UserMenuWrapper;
import com.rootmind.wrapper.UserStudentMapWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class UsersHelper extends Helper {

	/*
	 * public AbstractWrapper getCIFNumber(String userid)throws Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null; UsersWrapper usersWrapper=
	 * new UsersWrapper();
	 * 
	 * SimpleDateFormat dmyFormat = new
	 * SimpleDateFormat("EEE, dd-MMM-yyyy, hh:mm:ss");
	 * 
	 * 
	 * System.out.println("userid :"+ userid);
	 * 
	 * try { con = getConnection("MOBILEROOT"); PreparedStatement pstmt =
	 * con.prepareStatement("SELECT userid,password,status,CIFNumber," +
	 * "Lastlogindate,Devicetoken,NoLoginRetry from Users  where userid=?");
	 * 
	 * pstmt.setString(1,userid.trim()); resultSet = pstmt.executeQuery(); if
	 * (resultSet.next()) {
	 * 
	 * 
	 * usersWrapper.userid=resultSet.getString("userid");
	 * System.out.println("userid " + usersWrapper.userid);
	 * 
	 * usersWrapper.password=resultSet.getString("password");
	 * System.out.println("password " + usersWrapper.password);
	 * 
	 * usersWrapper.status=resultSet.getString("status");
	 * 
	 * usersWrapper.cifNumber=resultSet.getString("CIFNumber");
	 * System.out.println("CIFNumber " + usersWrapper.cifNumber);
	 * 
	 * System.out.println("last login date " + resultSet.getDate("Lastlogindate"));
	 * 
	 * usersWrapper.lastLoginDate=
	 * (resultSet.getDate("Lastlogindate")==null?"":dmyFormat.format(resultSet.
	 * getTimestamp("Lastlogindate")));
	 * 
	 * usersWrapper.deviceToken=resultSet.getString("Devicetoken");
	 * System.out.println("Devicetoken " + usersWrapper.deviceToken);
	 * 
	 * usersWrapper.noLoginRetry=resultSet.getInt("NoLoginRetry");
	 * 
	 * usersWrapper.recordFound=true;
	 * 
	 * System.out.println("usersWrapper " + usersWrapper.lastLoginDate);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * pstmt.close();
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
	 * return usersWrapper; }
	 */

	/*
	 * public AbstractWrapper updateUserAudit(String userid, String sessionid,String
	 * activity,String screenName,String message)throws Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null; UsersWrapper usersWrapper=
	 * new UsersWrapper();
	 * 
	 * 
	 * System.out.println("userid :"+ userid);
	 * 
	 * try { con = getConnection(); PreparedStatement pstmt =
	 * con.prepareStatement("INSERT INTO UserAudit(userid,sessionid,Activity," +
	 * "Screenname,Datestamp,Message) values (?,?,?,?,?,?) ");
	 * 
	 * pstmt.setString(1,(userid==null?null:userid.trim()));
	 * pstmt.setString(2,(sessionid==null?null:sessionid.trim()));
	 * pstmt.setString(3,(activity==null?null:activity.trim()));
	 * pstmt.setString(4,screenName.trim());
	 * pstmt.setTimestamp(5,Utility.getCurrentTime());
	 * pstmt.setString(6,(message==null?null:message.trim()));
	 * pstmt.executeUpdate();
	 * 
	 * pstmt.close();
	 * 
	 * UsersWrapper tmpUsersWrapper= new UsersWrapper();
	 * tmpUsersWrapper=(UsersWrapper)getCIFNumber(userid);
	 * 
	 * pstmt = con.
	 * prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=? where userid=?"
	 * );
	 * 
	 * pstmt.setTimestamp(1,Utility.getCurrentTime());
	 * pstmt.setInt(2,tmpUsersWrapper.noLoginRetry+1);
	 * pstmt.setString(3,userid.trim());
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * pstmt.close();
	 * 
	 * usersWrapper.recordFound=true;
	 * 
	 * System.out.println("user audit table updated " );
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
	 * return usersWrapper; }
	 */

	public void updateUserDetails(String userid, int noLoginRetry, String sessionid, String deviceToken,
			String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		// UsersWrapper usersWrapper= new UsersWrapper();

		System.out.println("userid :" + userid);
		String sql = null;
		PreparedStatement pstmt = null;
		String userGroup = null;

		try {
			con = getConnection();

			// ----- code--

			sql = "SELECT UserGroup from Users WHERE Userid=? and SchoolID=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userid.trim());
			pstmt.setString(2, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				userGroup = Utility.trim(resultSet.getString("UserGroup"));

			}

			resultSet.close();
			pstmt.close();

			// System.out.println("user group is "+userGroup.trim() );

			// ----------do not update Device id for Staff and admin, update only for
			// Students when not null
			if (userGroup != null && userGroup.trim().equals(Utility.student_type) && deviceToken != null
					&& !deviceToken.trim().equals("")) {
				sql = "UPDATE Users set Lastlogindate=?, NoLoginRetry=?,DeviceToken=?  where userid=? and SchoolID=?";
			} else {
				sql = "UPDATE Users set Lastlogindate=?, NoLoginRetry=?  where userid=? and SchoolID=?";
			}

			pstmt = con.prepareStatement(sql);

			pstmt.setTimestamp(1, Utility.getCurrentTime());
			pstmt.setInt(2, noLoginRetry + 1);

			if (userGroup != null && userGroup.trim().equals(Utility.student_type) && deviceToken != null
					&& !deviceToken.trim().equals("")) {
				pstmt.setString(3, (Utility.trim(deviceToken)));
				pstmt.setString(4, (Utility.trim(userid)));
				pstmt.setString(5, (schoolID.trim()));

			} else {
				pstmt.setString(3, (Utility.trim(userid)));
				pstmt.setString(4, (schoolID.trim()));
			}

			pstmt.executeUpdate();

			pstmt.close();
			// usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
			// usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
			// usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());

			pstmt.close();

			// usersWrapper.recordFound=true;

			System.out.println("user table device updated ");

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

		// return usersWrapper;
	}

	public AbstractWrapper validateCredentials(UsersWrapper usersWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// UsersWrapper usersWrapper= new UsersWrapper();
		usersWrapper.validUser = false;
		usersWrapper.recordFound = false;
		SimpleDateFormat dmyFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy, hh:mm:ss");

		PreparedStatement pstmt = null;
		Vector<Object> vector = new Vector<Object>();
		// DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		String sql = null;
		System.out.println("userid :" + usersWrapper.userid);
		UserStudentMapWrapper userStudentMapWrapper = null;
		UserStudentMapWrapper[] userStudentMapWrapperArray = null;
		try {

			con = getConnection();


			sql = "SELECT userid,password,status,StudentID, "
					+ "Lastlogindate,Devicetoken,NoLoginRetry,MaxRetry,sessionid,PwdChgDate,OTP,BusinessUnit,"
					+ " SessionExpirytime,UserGroup,Admin, SchoolID,email, mobile,firebaseID, RefNo from Users  ";

			//if email as userid then it should be unique across schools
			if(Utility.isEmpty(Utility.trim(usersWrapper.email))==true)
			{
				sql = sql + " WHERE userid=? and SchoolID=?";
			}
			else
			{
				sql = sql + " WHERE email=?";
				
			}
			
			pstmt = con.prepareStatement(sql);

			if(Utility.isEmpty(Utility.trim(usersWrapper.email))==true)
			{
				pstmt.setString(1, Utility.trim(usersWrapper.userid));
				pstmt.setString(2, usersWrapper.schoolID);

			}
			else
			{
				pstmt.setString(1, Utility.trim(usersWrapper.email)); //this is introduced for login purpose
				
			}

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				usersWrapper = new UsersWrapper();

				usersWrapper.userid = Utility.trim(resultSet.getString("userid"));
				System.out.println("userid " + usersWrapper.userid);

				usersWrapper.password = Utility.trim(resultSet.getString("password"));
				System.out.println("password " + usersWrapper.password);

				usersWrapper.status = Utility.trim(resultSet.getString("status"));

				usersWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
				System.out.println("StudentID " + usersWrapper.studentID);

				System.out.println("last login date " + resultSet.getDate("Lastlogindate"));

				usersWrapper.lastLoginDate = Utility.setDateAMPM(resultSet.getString("Lastlogindate"));// (resultSet.getDate("Lastlogindate")==null?"":dmyFormat.format(resultSet.getTimestamp("Lastlogindate")));

				usersWrapper.deviceToken = Utility.trim(resultSet.getString("Devicetoken"));
				System.out.println("Devicetoken " + usersWrapper.deviceToken);

				usersWrapper.noLoginRetry = resultSet.getInt("NoLoginRetry");

				usersWrapper.maxRetry = resultSet.getInt("MaxRetry");

				usersWrapper.sessionid = Utility.trim(resultSet.getString("sessionid"));

				usersWrapper.pwdChgDate = (resultSet.getDate("PwdChgDate") == null ? ""
						: dmyFormat.format(resultSet.getTimestamp("PwdChgDate")));

				usersWrapper.otp = resultSet.getInt("OTP");

				usersWrapper.businessUnit = Utility.trim(resultSet.getString("BusinessUnit"));

				usersWrapper.sessionExpiryTime = resultSet.getInt("SessionExpirytime");

				usersWrapper.userGroup = Utility.trim(resultSet.getString("UserGroup"));
				usersWrapper.admin = Utility.trim(resultSet.getString("Admin"));

				usersWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));
				usersWrapper.email = Utility.trim(resultSet.getString("email")); //this is introduced for login purpose
				usersWrapper.mobile = Utility.trim(resultSet.getString("mobile")); //this is introduced for login purpose
				usersWrapper.firebaseID = Utility.trim(resultSet.getString("firebaseID")); //this is for user image store

				usersWrapper.refNo = Utility.trim(resultSet.getString("RefNo"));

				
				usersWrapper.recordFound = true;

				System.out.println("usersWrapper " + usersWrapper.lastLoginDate);

			}

			resultSet.close();
			pstmt.close();

			// -----setting user values for mobile app--

			if (!Utility.isEmpty(usersWrapper.userGroup) && usersWrapper.userGroup.trim().equals(Utility.student_type)) {

				if (!Utility.isEmpty(usersWrapper.studentID)) {

					// -----StudentID for user student map--

					sql = "SELECT StudentID,SchoolID from UserStudentMap WHERE Userid=? and SchoolID=?";

					pstmt = con.prepareStatement(sql);

					pstmt.setString(1, Utility.trim(usersWrapper.userid));
					pstmt.setString(2, Utility.trim(usersWrapper.schoolID));

					resultSet = pstmt.executeQuery();
					while (resultSet.next()) {
						userStudentMapWrapper = new UserStudentMapWrapper();

						userStudentMapWrapper.studentID = Utility.trim(resultSet.getString("StudentID"));
						userStudentMapWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

						System.out.println("StudentID for userstudentmap " + userStudentMapWrapper.studentID);
						userStudentMapWrapper.recordFound = true;

						vector.addElement(userStudentMapWrapper);
					}
					if (vector.size() > 0) {
						userStudentMapWrapperArray = new UserStudentMapWrapper[vector.size()];
						vector.copyInto(userStudentMapWrapperArray);

						System.out.println("total trn. in fetch " + vector.size());

					}

					resultSet.close();
					pstmt.close();

					// ----------
					if (userStudentMapWrapperArray != null && userStudentMapWrapperArray.length > 0) {

						usersWrapper.studentProfileWrapper = new StudentProfileWrapper[userStudentMapWrapperArray.length];
						PopoverHelper popoverHelper = new PopoverHelper();

						for (int i = 0; i <= userStudentMapWrapperArray.length - 1; i++) {
							System.out.println(
									"Student for loop userstudentmap " + userStudentMapWrapperArray[i].studentID);

							pstmt = con.prepareStatement(
									"SELECT RefNo,StudentID,AcademicYearID,SchoolID,GradeID,SectionID,StudentName,"
											+ " Surname, SchoolID from StudentProfile WHERE StudentID=? and SchoolID=?");

							pstmt.setString(1, Utility.trim(userStudentMapWrapperArray[i].studentID));
							pstmt.setString(2, Utility.trim(userStudentMapWrapperArray[i].schoolID));

							resultSet = pstmt.executeQuery();
							if (resultSet.next()) {

								usersWrapper.studentProfileWrapper[i] = new StudentProfileWrapper();
								usersWrapper.studentProfileWrapper[i].refNo = Utility
										.trim(resultSet.getString("RefNo"));
								usersWrapper.studentProfileWrapper[i].studentID = Utility
										.trim(resultSet.getString("StudentID"));
								usersWrapper.studentProfileWrapper[i].academicYearID = Utility
										.trim(resultSet.getString("AcademicYearID"));
								usersWrapper.studentProfileWrapper[i].schoolID = Utility
										.trim(resultSet.getString("SchoolID"));
								usersWrapper.studentProfileWrapper[i].gradeID = Utility
										.trim(resultSet.getString("GradeID"));
								usersWrapper.studentProfileWrapper[i].sectionID = Utility
										.trim(resultSet.getString("SectionID"));
								usersWrapper.studentProfileWrapper[i].studentName = Utility
										.trim(resultSet.getString("StudentName"));
								usersWrapper.studentProfileWrapper[i].surname = Utility
										.trim(resultSet.getString("Surname"));
								usersWrapper.studentProfileWrapper[i].schoolID = Utility
										.trim(resultSet.getString("SchoolID"));

								usersWrapper.studentProfileWrapper[i].academicYearIDValue = popoverHelper
										.fetchPopoverDesc(usersWrapper.studentProfileWrapper[i].academicYearID,
												"MST_AcademicYear", usersWrapper.studentProfileWrapper[i].schoolID);
								usersWrapper.studentProfileWrapper[i].schoolIDValue = popoverHelper.fetchPopoverDesc(
										usersWrapper.studentProfileWrapper[i].schoolID, "MST_School",
										usersWrapper.studentProfileWrapper[i].schoolID);
								usersWrapper.studentProfileWrapper[i].gradeIDValue = popoverHelper.fetchPopoverDesc(
										usersWrapper.studentProfileWrapper[i].gradeID, "MST_Grade",
										usersWrapper.studentProfileWrapper[i].schoolID);
								usersWrapper.studentProfileWrapper[i].sectionIDValue = popoverHelper.fetchPopoverDesc(
										usersWrapper.studentProfileWrapper[i].sectionID, "MST_Section",
										usersWrapper.studentProfileWrapper[i].schoolID);
								


							}
							resultSet.close();
							pstmt.close();
							
							
							
							//--------to get student image;

							usersWrapper.studentProfileWrapper[i].avatar=Utility.trim(new StudentProfileHelper().fetchStudentImage(usersWrapper, Utility.trim(usersWrapper.studentProfileWrapper[i].studentID)));
							
							//--------end of student image
							
							
						}
					}
				}

			}
			// ----------

			if (usersWrapper.recordFound == true && !Utility.isEmpty(usersWrapper.userGroup) && usersWrapper.userGroup.trim().equals("STAFF")) {
				
				
				
				TeachersProfileHelper teachersProfileHelper = new TeachersProfileHelper();
				TeachersProfileWrapper teachersProfileWrapper = new TeachersProfileWrapper();
				teachersProfileWrapper.staffRefNo = usersWrapper.refNo;

				System.out.println("teachersProfileHelper " + teachersProfileWrapper.staffRefNo);

				DataArrayWrapper staffDataArrayWrapper = (DataArrayWrapper) teachersProfileHelper.fetchTeachersProfile(usersWrapper, teachersProfileWrapper);
				usersWrapper.teachersProfileWrapper = staffDataArrayWrapper.teachersProfileWrapper[0];

				
			}

			
			// ------------User Menu ----------------

			if (usersWrapper.recordFound == true) {
				
				//-------------select menuid from Groups----------
				UserMenuWrapper userMenuWrapper = null; // new UserMenuWrapper();

				sql = "SELECT b.Userid as Userid,a.MenuID as MenuID, a.SchoolID as SchoolID FROM MST_GroupMenu a join MSTSEC_UserGroup b "
				+ " on a.GroupID=b.GroupID WHERE a.SchoolID=? and b.userid=?  and a.Assigned='Y' and b.Assigned='Y' ";

				//for student & parents restrict for only PARENT Group only...so that if any additional access will not go
				if (!Utility.isEmpty(usersWrapper.userGroup) && Utility.trim(usersWrapper.userGroup).equals(Utility.student_type)) 
				{
					sql = "SELECT '"+Utility.trim(usersWrapper.userid)+"' as Userid,MenuID, SchoolID FROM MST_GroupMenu "
							+ " WHERE SchoolID=? and Assigned='Y' and GroupID='PARENTGRP'";
				}
					
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, usersWrapper.schoolID);


				//if not student type then pass userid parameter
				if (!Utility.isEmpty(usersWrapper.userGroup) && !Utility.trim(usersWrapper.userGroup).equals(Utility.student_type)) 
				{
					pstmt.setString(2, Utility.trim(usersWrapper.userid));

				}
				

				resultSet = pstmt.executeQuery();

				vector.clear();

				while (resultSet.next()) {

					userMenuWrapper = new UserMenuWrapper();

					userMenuWrapper.userid = Utility.trim(resultSet.getString("Userid"));
					//System.out.println("userid" + userMenuWrapper.userid);

					userMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));
					//System.out.println("menuID" + userMenuWrapper.menuID);
					userMenuWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

					userMenuWrapper.recordFound = true;

					vector.addElement(userMenuWrapper);


				}

//				if (vector.size() > 0) {
//
//					usersWrapper.userMenuWrapper = new UserMenuWrapper[vector.size()];
//					vector.copyInto(usersWrapper.userMenuWrapper);
//
//					System.out.println("Fetch UserMenu Details Successful");
//
//				}
				resultSet.close();
				pstmt.close();
				//--------------end of Group menu---
				
				//-------------select menuid from each user-------------
				 // new UserMenuWrapper();
				pstmt = con.prepareStatement(
						"SELECT Userid,MenuID,SchoolID FROM MST_UserMenu WHERE userid=? and SchoolID=?");
				pstmt.setString(1, Utility.trim(usersWrapper.userid));
				pstmt.setString(2, usersWrapper.schoolID);

				resultSet = pstmt.executeQuery();

				//vector.clear();

				while (resultSet.next()) {

					userMenuWrapper = new UserMenuWrapper();

					userMenuWrapper.userid = Utility.trim(resultSet.getString("Userid"));
					//System.out.println("userid" + userMenuWrapper.userid);

					userMenuWrapper.menuID = Utility.trim(resultSet.getString("MenuID"));
					//System.out.println("menuID" + userMenuWrapper.menuID);
					userMenuWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

					userMenuWrapper.recordFound = true;

					vector.addElement(userMenuWrapper);


				}

				if (vector.size() > 0) {

					usersWrapper.userMenuWrapper = new UserMenuWrapper[vector.size()];
					vector.copyInto(usersWrapper.userMenuWrapper);

					System.out.println("Fetch UserMenu Details Successful");

				}
				resultSet.close();
				pstmt.close();
				//-----------end of user menu-----

				// ------------get MST_Parameter table details----

				PopoverHelper popoverHelper = new PopoverHelper();
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersWrapper.schoolID);
				usersWrapper.currentAcademicYear = parameterWrapper.currentAcademicYear;
				// -------

				// --------- get MST_School
				SchoolWrapper schoolWrapper = new SchoolWrapper();
				DataArrayWrapper schoolDataArrayWrapper = (DataArrayWrapper) popoverHelper
						.fetchSchoolRegister(usersWrapper, schoolWrapper);
				schoolWrapper = schoolDataArrayWrapper.schoolWrapper[0];
				usersWrapper.schoolName = schoolWrapper.schoolName;

				/// ---------------

			}
			// -----------User Menu end------

			/*
			 * dataArrayWrapper.usersWrapper=new UsersWrapper[1];
			 * dataArrayWrapper.usersWrapper[0]=usersWrapper;
			 */

			/*
			 * //--user not found validation --- if(usersWrapper.recordFound==false) {
			 * System.out.println("usersWrapper.recordFound :"+ usersWrapper.recordFound);
			 * usersWrapper.validUser=false;
			 * //dataArrayWrapper.usersWrapper[0]=usersWrapper;
			 * //dataArrayWrapper.recordFound=true; return usersWrapper; } else if
			 * (usersWrapper.recordFound==true) { //--inactive user validation
			 * if(!usersWrapper.status.trim().equals("ACTIVE")) {
			 * System.out.println("usersWrapper.status :"+ usersWrapper.status);
			 * usersWrapper.validUser=false;
			 * //dataArrayWrapper.usersWrapper[0]=usersWrapper;
			 * //dataArrayWrapper.recordFound=true; return usersWrapper; } //--invalid
			 * session id validation else if(newSession==false &&
			 * !sessionid.trim().equals(usersWrapper.sessionid)) {
			 * System.out.println("sessionid :"+ sessionid);
			 * System.out.println("usersWrapper.sessionid :"+ usersWrapper.sessionid);
			 * usersWrapper.validUser=false;
			 * //dataArrayWrapper.usersWrapper[0]=usersWrapper;
			 * //dataArrayWrapper.recordFound=true; return usersWrapper; } else {
			 * //System.out.println("update usersWrapper.noLoginRetry :"+
			 * usersWrapper.noLoginRetry);
			 * 
			 * if(newSession==true) { pstmt = con.
			 * prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=?,sessionid=?,DeviceToken=?  where userid=?"
			 * );
			 * 
			 * pstmt.setTimestamp(1,Utility.getCurrentTime());
			 * pstmt.setInt(2,usersWrapper.noLoginRetry+1);
			 * pstmt.setString(3,(sessionid==null?"":sessionid.trim()));
			 * pstmt.setString(4,(deviceToken==null?"":deviceToken.trim()));
			 * pstmt.setString(5,(userid==null?"":userid.trim()));
			 * 
			 * pstmt.executeUpdate();
			 * 
			 * pstmt.close(); usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
			 * usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
			 * usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim()); }
			 * 
			 * usersWrapper.validUser=true; //dataArrayWrapper.usersWrapper[0]=usersWrapper;
			 * //dataArrayWrapper.recordFound=true;
			 * System.out.println("validate credentials end"); }
			 * 
			 * }
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

		return usersWrapper;

	}

	public AbstractWrapper fetchPasswordPolicy(PasswordWrapper passwordWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);

		try {

			con = getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("SELECT MinLength, MaxLength, CapitalLetter, SpecialCharacter,"
							+ " OldPasswordRepeat, SchoolID FROM RMT_PasswordPolicy where SchoolID=?");

			pstmt.setString(1, passwordWrapper.schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				passwordWrapper = new PasswordWrapper();

				passwordWrapper.minLength = Utility.trim(resultSet.getString("MinLength"));
				System.out.println("minLength " + passwordWrapper.minLength);
				passwordWrapper.maxLength = Utility.trim(resultSet.getString("MaxLength"));
				passwordWrapper.capitalLetter = Utility.trim(resultSet.getString("CapitalLetter"));
				passwordWrapper.specialCharacter = Utility.trim(resultSet.getString("SpecialCharacter"));
				passwordWrapper.oldPasswordRepeat = Utility.trim(resultSet.getString("OldPasswordRepeat"));
				passwordWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				passwordWrapper.recordFound = true;
				passwordWrapper.policyAvailable = true;

				System.out.println("PasswordWrapper");

			}

			dataArrayWrapper.passwordWrapper = new PasswordWrapper[1];
			dataArrayWrapper.passwordWrapper[0] = passwordWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("total trn. in fetchPasswordPolicy ");

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

	/*
	 * public AbstractWrapper changePassword(UsersWrapper usersWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null; DataArrayWrapper
	 * dataArrayWrapper = new DataArrayWrapper();
	 * 
	 * 
	 * System.out.println("userid :"+ usersWrapper.userid); String
	 * oldPasswordRepeat=null; try {
	 * 
	 * con = getConnection(); PreparedStatement pstmt=null;
	 * 
	 * //-----check password valid or not pstmt =
	 * con.prepareStatement("SELECT Password FROM Users WHERE USERID=?");
	 * pstmt.setString(1, usersWrapper.userid); resultSet = pstmt.executeQuery();
	 * if(resultSet.next()){
	 * 
	 * 
	 * if(!usersWrapper.oldPassword.trim().equals(Utility.trim(resultSet.getString(
	 * "Password")))) { usersWrapper.invalidOldPassword=true;
	 * usersWrapper.recordFound=true; }
	 * 
	 * 
	 * } resultSet.close(); pstmt.close(); //-------
	 * 
	 * if(usersWrapper.invalidOldPassword==false) {
	 * 
	 * 
	 * 
	 * //------------repeat password verification pstmt =
	 * con.prepareStatement("SELECT OldPasswordRepeat FROM RMT_PasswordPolicy");
	 * 
	 * resultSet = pstmt.executeQuery(); if(resultSet.next()){
	 * 
	 * oldPasswordRepeat=Utility.trim(resultSet.getString("OldPasswordRepeat"));
	 * 
	 * System.out.println("oldPasswordRepeat "+oldPasswordRepeat ); }
	 * 
	 * resultSet.close(); pstmt.close();
	 * 
	 * 
	 * if(oldPasswordRepeat !=null && !oldPasswordRepeat.trim().equals("")) { pstmt
	 * = con.
	 * prepareStatement("SELECT OldPassword FROM RMT_PasswordHistory Where UserId=?"
	 * );
	 * 
	 * pstmt.setString(1,Utility.trim(usersWrapper.userid));
	 * 
	 * resultSet = pstmt.executeQuery();
	 * 
	 * ArrayList<String> oldPassword = new ArrayList<String>();
	 * while(resultSet.next()){
	 * 
	 * oldPassword.add(Utility.trim(resultSet.getString("OldPassword")));
	 * 
	 * System.out.println("OldPassword "+Utility.trim(resultSet.getString(
	 * "OldPassword"))); }
	 * 
	 * resultSet.close(); pstmt.close();
	 * 
	 * for(int i=0;i<= Integer.parseInt(oldPasswordRepeat)-1;i++){
	 * 
	 * System.out.println("PASSWORD "+usersWrapper.password.trim());
	 * System.out.println("OldPassword "+oldPassword.get(i).trim());
	 * 
	 * if(usersWrapper.password.trim().equals(oldPassword.get(i).trim())){
	 * 
	 * usersWrapper.oldPasswordRepeat=true; usersWrapper.recordFound=true; } } }
	 * 
	 * if(usersWrapper.oldPasswordRepeat==false){
	 * 
	 * pstmt = con.prepareStatement("UPDATE Users set Password=? where userid=?");
	 * pstmt.setString(1,Utility.trim(usersWrapper.password));
	 * pstmt.setString(2,Utility.trim(usersWrapper.userid));
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * pstmt.close();
	 * 
	 * 
	 * System.out.println("Change password details updated " );
	 * 
	 * 
	 * pstmt = con.
	 * prepareStatement("INSERT INTO RMT_PasswordHistory (UserId,OldPassword,PasswordChngDT,IPAddress) values (?,?,?,?)"
	 * );
	 * 
	 * pstmt.setString(1,Utility.trim(usersWrapper.userid));
	 * pstmt.setString(2,Utility.trim(usersWrapper.oldPassword));
	 * pstmt.setTimestamp(3,Utility.getCurrentTime());
	 * pstmt.setString(4,Utility.trim(usersWrapper.ipAddress));
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * pstmt.close();
	 * 
	 * System.out.println("PasswordHistory Details Inserted" );
	 * 
	 * usersWrapper.recordFound=true; usersWrapper.passwordChanged=true;
	 * 
	 * }
	 * 
	 * } dataArrayWrapper.usersWrapper=new UsersWrapper[1];
	 * dataArrayWrapper.usersWrapper[0]=usersWrapper;
	 * 
	 * dataArrayWrapper.recordFound=true;
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
	 * return dataArrayWrapper; }
	 */

	public AbstractWrapper changePassword(UsersWrapper usersProfileWrapper, UsersWrapper usersWrapper)
			throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		System.out.println("userid :" + usersProfileWrapper.userid);
		System.out.println("staffUserID :" + usersWrapper.staffUserID);
		System.out.println("schoolID :" + usersProfileWrapper.schoolID);
		System.out.println("status :" + usersWrapper.status);
		
		String sql=null;
		int n=1;

		try {

			con = getConnection();
			PreparedStatement pstmt = null;

			AES128Crypto aes128Crypto = new AES128Crypto();

			if(!Utility.isEmpty(usersWrapper.password))
			{
				sql = "UPDATE Users set Status=?, ModifierID=?, ModifierDateTime=?, Password=?, PWDChgDate=? where userid=? and SchoolID=?";
			}
			else
			{
				sql = "UPDATE Users set Status=?, ModifierID=?, ModifierDateTime=? where userid=? and SchoolID=?";
				
			}
			// ------update users tables
			pstmt = con.prepareStatement(sql);

			pstmt.setString(n, Utility.trim(usersWrapper.status));
			pstmt.setString(++n, Utility.trim(usersProfileWrapper.userid));
			pstmt.setTimestamp(++n, Utility.getCurrentTime());

			if(!Utility.isEmpty(usersWrapper.password))
			{
				pstmt.setString(++n, aes128Crypto.md5DB(usersWrapper.password, usersProfileWrapper.schoolID));
				pstmt.setTimestamp(++n, Utility.getCurrentTime());
			}

			pstmt.setString(++n, Utility.trim(usersWrapper.staffUserID));
			pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Change password details updated ");
			// ------
			
			// ------update staff tables
			pstmt = con.prepareStatement(
					"UPDATE TeachersProfile set Status=? where userid=? and SchoolID=?");
			
			pstmt.setString(1, Utility.trim(usersWrapper.status));

			pstmt.setString(2, Utility.trim(usersWrapper.staffUserID));
			pstmt.setString(3, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Change password details updated in teachers tables");
			// ------
			

			// -------
			pstmt = con.prepareStatement("INSERT INTO PasswordHistory(UserId,OldPassword,PasswordChngDT,"
					+ "IPAddress,ModifierID, SchoolID) values (?,?,?,?,?,?)");

			pstmt.setString(1, Utility.trim(usersWrapper.staffUserID));
			pstmt.setString(2, Utility.trim(usersWrapper.oldPassword));
			pstmt.setTimestamp(3, Utility.getCurrentTime());
			pstmt.setString(4, Utility.trim(usersWrapper.ipAddress));
			pstmt.setString(5, Utility.trim(usersProfileWrapper.userid));
			pstmt.setString(6, Utility.trim(usersProfileWrapper.schoolID));

			pstmt.executeUpdate();

			pstmt.close();

			System.out.println("PasswordHistory Details Inserted");

			usersWrapper.recordFound = true;
			usersWrapper.passwordChanged = true;

			// ------

			dataArrayWrapper.usersWrapper = new UsersWrapper[1];
			dataArrayWrapper.usersWrapper[0] = usersWrapper;

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

	public AbstractWrapper fetchLoginProfile(UsersWrapper usersProfileWrapper, UsersWrapper usersWrapper)
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

		try {

			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT Userid,Password,Status, SchoolID FROM Users WHERE StudentID=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(usersWrapper.studentID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				usersWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				System.out.println("minLength " + usersWrapper.userid);
				usersWrapper.password = Utility.trim(resultSet.getString("Password"));
				usersWrapper.status = Utility.trim(resultSet.getString("Status"));
				usersWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				usersWrapper.recordFound = true;

			}

			dataArrayWrapper.usersWrapper = new UsersWrapper[1];
			dataArrayWrapper.usersWrapper[0] = usersWrapper;
			dataArrayWrapper.recordFound = true;

			System.out.println("total trn. in fetchUserid ");

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

	/*
	 * //-----------------Start insertUserid---------------------
	 * 
	 * public AbstractWrapper insertUserid(UsersWrapper usersWrapper)throws
	 * Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper = new DataArrayWrapper(); String sql=null;
	 * //String countryCode=null;
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
	 * try { con = getConnection();
	 * 
	 * 
	 * sql=" INSERT INTO StudentProfile(Userid,Password, StudentID,) Values(?,?,?)";
	 * 
	 * System.out.println("sql " + sql);
	 * 
	 * pstmt = con.prepareStatement(sql);
	 * 
	 * pstmt.setString(1,Utility.trim(usersWrapper.userid));
	 * pstmt.setString(2,Utility.trim(usersWrapper.password));
	 * pstmt.setString(3,Utility.trim(usersWrapper.studentID));
	 * 
	 * System.out.println("insert  Userid "+usersWrapper.userid);
	 * 
	 * pstmt.executeUpdate(); pstmt.close();
	 * 
	 * usersWrapper.recordFound=true;
	 * 
	 * dataArrayWrapper.usersWrapper=new UsersWrapper[1];
	 * dataArrayWrapper.usersWrapper[0]=usersWrapper;
	 * 
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("insertUserid is Successfully ");
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
	 * return dataArrayWrapper; }
	 * 
	 * //-----------------End insertUserid---------------------
	 */

	public AbstractWrapper updateLoginProfile(UsersWrapper usersProfileWrapper, UsersWrapper usersWrapper,
			String userGroup) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		String defaultPassword = null;
		String sql = null;

		int n=1;
		
		try {
			AES128Crypto aes128Crypto = new AES128Crypto();

			con = getConnection();

			pstmt = con.prepareStatement("SELECT RefNo FROM Users WHERE RefNo=? and SchoolID=?");

			// System.out.println(" RefNo is" + usersWrapper.refNo);

			pstmt.setString(1, Utility.trim(usersWrapper.refNo));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();

				
				if(Utility.isEmpty(usersWrapper.password)==true)
				{
				
					// -----DefaultPassword --
					PopoverHelper popoverHelper = new PopoverHelper();
					ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
							.fetchParameters(usersProfileWrapper.schoolID);
					defaultPassword = parameterWrapper.defaultPassword;

				}
				else
				{
					defaultPassword = aes128Crypto.md5DB(usersWrapper.password, usersProfileWrapper.schoolID);
				}
				
				// ----------create userid with schoolid userid@schoolid
				usersProfileWrapper.schoolID = Utility.trim(Utility.removeSpaces(usersProfileWrapper.schoolID));
				usersWrapper.userid = Utility.trim(Utility.removeSpaces(usersWrapper.userid));
				usersWrapper.userid = usersWrapper.userid + "@" + usersProfileWrapper.schoolID;
				// -------------

				// ----------

				pstmt = con.prepareStatement(
						"INSERT INTO Users(Userid,Password,Status,StudentID,UserGroup,RefNo,MakerID,MakerDateTime,SchoolID,"
						+ "PWDChgDate,email, mobile, firebaseID)"
								+ " Values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

				System.out.println("Userid is " + Utility.trim(usersWrapper.userid));

				pstmt.setString(1, Utility.trim(usersWrapper.userid));
				pstmt.setString(2, defaultPassword);
				pstmt.setString(3, "ACTIVE");
				pstmt.setString(4, Utility.trim(usersWrapper.studentID));
				pstmt.setString(5, userGroup); // --ex:"STUDENT","STAFF"
				pstmt.setString(6, Utility.trim(usersWrapper.refNo));
				pstmt.setString(7, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(8, Utility.getCurrentTime());
				pstmt.setString(9, Utility.trim(usersProfileWrapper.schoolID));
				pstmt.setTimestamp(10, Utility.getCurrentTime());
				pstmt.setString(11, Utility.trim(usersWrapper.email));
				pstmt.setString(12, Utility.trim(usersWrapper.mobile));
				pstmt.setString(13, Utility.trim(usersWrapper.firebaseID));

				pstmt.executeUpdate();
				pstmt.close();

				usersWrapper.recordFound = true;

				if (userGroup.equals(Utility.student_type)) {
					// ----------insert of userid and studentid for UserStudentMap-----------

					pstmt = con.prepareStatement(
							"INSERT INTO UserStudentMap(Userid,StudentID,RefNo,SchoolID,MakerID,MakerDateTime, email)"
									+ " Values(?,?,?,?,?,?,?)");

					System.out.println("Userid is " + Utility.trim(usersWrapper.userid));

					pstmt.setString(1, Utility.trim(usersWrapper.userid));
					pstmt.setString(2, Utility.trim(usersWrapper.studentID));
					pstmt.setString(3, Utility.trim(usersWrapper.refNo));
					pstmt.setString(4, Utility.trim(usersProfileWrapper.schoolID));
					pstmt.setString(5, Utility.trim(usersProfileWrapper.userid));
					pstmt.setTimestamp(6, Utility.getCurrentTime());
					pstmt.setString(7, Utility.trim(usersWrapper.email));


					pstmt.executeUpdate();
					pstmt.close();
					// ---------end-----------
				}

				dataArrayWrapper.usersWrapper = new UsersWrapper[1];
				dataArrayWrapper.usersWrapper[0] = usersWrapper;
				dataArrayWrapper.recordFound = true;

			} else {

				resultSet.close();
				pstmt.close();
							

				if(!Utility.isEmpty(usersWrapper.password))
				{

					sql = "UPDATE Users SET Status=?,ModifierID=?,ModifierDateTime=?,  Password=?, PWDChgDate=?  where RefNo=? and StudentID=? "
							+ " and Userid=? and SchoolID=?";

				}
				else
				{

					sql = "UPDATE Users SET Status=?,ModifierID=?,ModifierDateTime=? where RefNo=? and StudentID=? "
							+ " and Userid=? and SchoolID=?";

				}
				
				//--------------update users table
				pstmt = con.prepareStatement(sql);

				System.out.println("UPDATE Users  " + usersWrapper.refNo +" : " +usersWrapper.studentID+":"+usersWrapper.userid+":"+usersProfileWrapper.schoolID+" : "+usersWrapper.status);

				pstmt.setString(n, Utility.trim(usersWrapper.status));
				pstmt.setString(++n, Utility.trim(usersProfileWrapper.userid));
				pstmt.setTimestamp(++n, Utility.getCurrentTime());
				
				if(!Utility.isEmpty(usersWrapper.password))
				{
					pstmt.setString(++n,
						aes128Crypto.md5DB(Utility.trim(usersWrapper.password), usersProfileWrapper.schoolID));
					pstmt.setTimestamp(++n, Utility.getCurrentTime());

				}

				pstmt.setString(++n, Utility.trim(usersWrapper.refNo));
				pstmt.setString(++n, Utility.trim(usersWrapper.studentID));
				pstmt.setString(++n, Utility.trim(usersWrapper.userid));
				pstmt.setString(++n, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();
				
				
				//---------------update student profile
				pstmt = con.prepareStatement(
						"UPDATE StudentProfile SET Status=?  where RefNo=? and StudentID=? "
								+ " and Userid=? and SchoolID=?");

				System.out.println("UPDATE StudentProfile  " + usersWrapper.refNo +" : " +usersWrapper.studentID+":"+usersWrapper.userid+":"+usersProfileWrapper.schoolID+" : "+usersWrapper.status);

				
				pstmt.setString(1, Utility.trim(usersWrapper.status));
				pstmt.setString(2, Utility.trim(usersWrapper.refNo));
				pstmt.setString(3, Utility.trim(usersWrapper.studentID));
				pstmt.setString(4, Utility.trim(usersWrapper.userid));
				pstmt.setString(5, Utility.trim(usersProfileWrapper.schoolID));

				pstmt.executeUpdate();
				pstmt.close();
				//----------
				

				usersWrapper.recordFound = true;

				dataArrayWrapper.usersWrapper = new UsersWrapper[1];
				dataArrayWrapper.usersWrapper[0] = usersWrapper;
				dataArrayWrapper.recordFound = true;

				System.out.println("Login Profile updated ");

			}

			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

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

	public AbstractWrapper fetchUsersStaff(String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		UsersWrapper usersWrapper = null;
		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// DecimalFormat formatter = (DecimalFormat)
		// NumberFormat.getInstance(Locale.US);
		// DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		// symbols.setGroupingSeparator(',');
		// formatter.applyPattern("###,###,###,##0.00");
		// formatter.setDecimalFormatSymbols(symbols);
		Vector<Object> vector = new Vector<Object>();

		try {

			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT Userid,Status,SchoolID FROM Users WHERE UserGroup='STAFF' and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				usersWrapper = new UsersWrapper();

				usersWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				System.out.println("minLength " + usersWrapper.userid);

				usersWrapper.status = Utility.trim(resultSet.getString("Status"));
				usersWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				usersWrapper.recordFound = true;
				vector.addElement(usersWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.usersWrapper = new UsersWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.usersWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetchStaff " + vector.size());

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

	public AbstractWrapper fetchSessionDetails(String userid, String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		UsersWrapper usersWrapper = null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		PreparedStatement pstmt = null;
		int sessionTimeOut = 0;

		try {

			con = getConnection();

			// --------

			pstmt = con.prepareStatement("SELECT SessionTimeOut from MST_Parameter where SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolID));

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {

				sessionTimeOut = resultSet.getInt("SessionTimeOut");

			}

			resultSet.close();
			pstmt.close();
			// ----------

			pstmt = con.prepareStatement(
					"SELECT Userid,Lastlogindate,Sessionid, SchoolID FROM Users WHERE Userid=? and SchoolID=?");

			pstmt.setString(1, userid);
			pstmt.setString(2, schoolID);

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				usersWrapper = new UsersWrapper();

				usersWrapper.userid = Utility.trim(resultSet.getString("Userid"));
				System.out.println("minLength " + usersWrapper.userid);

				usersWrapper.lastLoginDate = resultSet.getString("Lastlogindate");

				usersWrapper.sessionid = Utility.trim(resultSet.getString("Sessionid"));
				usersWrapper.schoolID = Utility.trim(resultSet.getString("SchoolID"));

				usersWrapper.recordFound = true;

				/*
				 * System.out.println("Lastlogindate  "+usersWrapper.lastLoginDate);
				 * System.out.println("Time diff "+Utility.timeDifference(usersWrapper.
				 * lastLoginDate)); System.out.println("SessionTimeOut "+sessionTimeOut);
				 * 
				 * if(Utility.timeDifference(usersWrapper.lastLoginDate)>sessionTimeOut) {
				 * usersWrapper.sessionTimeOut=true; System.out.println("Session Timed Out "); }
				 */

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
		} finally {
			try {
				releaseConnection(resultSet, con);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new Exception(se.getSQLState() + " ; " + se.getMessage());
			}
		}

		return usersWrapper;
	}

	public void updateSessionDetails(String userid, int noLoginRetry, String sessionid, String deviceToken,
			String schoolID) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		// UsersWrapper usersWrapper= new UsersWrapper();

		System.out.println("userid :" + userid);
		String sql = null;
		PreparedStatement pstmt = null;
		//String userGroup = null;

		try {
			con = getConnection();

			// ----- code--

//			sql = "SELECT UserGroup from Users WHERE Userid=? and SchoolID=?";
//
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, userid.trim());
//			pstmt.setString(2, Utility.trim(schoolID));
//
//			resultSet = pstmt.executeQuery();
//			if (resultSet.next()) {
//
//				userGroup = Utility.trim(resultSet.getString("UserGroup"));
//
//			}
//
//			resultSet.close();
//			pstmt.close();

			// System.out.println("user group is "+userGroup.trim() );

			// ----------do not update Device id for Staff and admin, update only for
			// Students when not null
//			if (userGroup != null && userGroup.trim().equals("STUDENT") && deviceToken != null
//					&& !deviceToken.trim().equals("")) {

			if(Utility.isEmpty(deviceToken)==true)
			{
				sql = "UPDATE Users set  NoLoginRetry=?,sessionid=?  where userid=? and SchoolID=?";
			}
			else
			{
				sql = "UPDATE Users set  NoLoginRetry=?,sessionid=?,DeviceToken=?  where userid=? and SchoolID=?";
			}

			pstmt = con.prepareStatement(sql);

			if(Utility.isEmpty(deviceToken)==true)
			{

				pstmt.setInt(1, noLoginRetry + 1);
				pstmt.setString(2, (Utility.trim(sessionid)));
				pstmt.setString(3, (Utility.trim(userid)));
				pstmt.setString(4, (Utility.trim(schoolID)));

			}
			else
			{

				pstmt.setInt(1, noLoginRetry + 1);
				pstmt.setString(2, (Utility.trim(sessionid)));
				pstmt.setString(3, (Utility.trim(deviceToken)));
				pstmt.setString(4, (Utility.trim(userid)));
				pstmt.setString(5, (Utility.trim(schoolID)));

			}


			pstmt.executeUpdate();

			pstmt.close();
			// usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
			// usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
			// usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());

			//pstmt.close();

			// usersWrapper.recordFound=true;

			System.out.println("user table session updated ");

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

		// return usersWrapper;
	}

	// ----------create administrator profile
	public AbstractWrapper createAdminProfile(SchoolWrapper schoolWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		PreparedStatement pstmt = null;

		// String defaultPassword=null;
		// String sql=null;

		// usersWrapper = null;

		try {
			AES128Crypto aes128Crypto = new AES128Crypto();

			con = getConnection();

			pstmt = con.prepareStatement("SELECT SchoolID FROM Users WHERE UserID=? and SchoolID=?");

			pstmt.setString(1, Utility.trim(schoolWrapper.adminID));
			pstmt.setString(2, Utility.trim(schoolWrapper.schoolID));

			resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				resultSet.close();
				pstmt.close();

				// ----------

				pstmt = con.prepareStatement(
						"INSERT INTO Users(Userid,Password,Status,UserGroup,Admin,MakerID,MakerDateTime,SchoolID,PWDChgDate)"
								+ " Values(?,?,?,?,?,?,?,?,?)");

				System.out.println("Userid is " + Utility.trim(schoolWrapper.adminID));

				System.out.println("password is " + Utility.trim(schoolWrapper.password));

				pstmt.setString(1, Utility.trim(schoolWrapper.adminID));
				pstmt.setString(2, aes128Crypto.md5DB(Utility.trim(schoolWrapper.password), schoolWrapper.schoolID));
				pstmt.setString(3, "ACTIVE");
				pstmt.setString(4, Utility.staff_type); // --ex:"STUDENT","STAFF"
				pstmt.setString(5, "Y"); // Admin flag
				pstmt.setString(6, schoolWrapper.adminID); // makerid
				pstmt.setTimestamp(7, Utility.getCurrentTime()); // maker date and time
				pstmt.setString(8, Utility.trim(schoolWrapper.schoolID));
				pstmt.setTimestamp(9, Utility.getCurrentTime()); // pwd date

				pstmt.executeUpdate();
				pstmt.close();

				System.out.println("Admin Login Profile Created " + schoolWrapper.adminID);

			} else {

				resultSet.close();
				pstmt.close();

				System.out.println("Admin Login Profile Creation failed " + schoolWrapper.adminID);

			}

			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
				pstmt.close();

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

	// ----------------- Checking userid exist or not---------------
	public boolean fetchUserid(String userid, String schoolID, String email) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;
		boolean useridFound = false;

		String sql=null;
		
		try {

			// ----------create userid with schoolid userid@schoolid
			schoolID = Utility.trim(Utility.removeSpaces(schoolID));
			userid = Utility.trim(Utility.removeSpaces(userid));
			userid = userid + "@" + schoolID;
			// -------------

			con = getConnection();
			
			//if email as userid then it should be unique across schools
			if(Utility.isEmpty(email)==true)
			{
				sql = "SELECT Userid  FROM Users WHERE Userid=? and SchoolID=?";

			}
			else
			{
				sql= "SELECT Userid  FROM Users WHERE email=?";
			}

			PreparedStatement pstmt = con.prepareStatement(sql);

			if(Utility.isEmpty(email)==true)
			{

				pstmt.setString(1, userid);
				pstmt.setString(2, schoolID);

			}
			else
			{
				pstmt.setString(1, Utility.trim(email));
				
			}

			resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				useridFound = true;
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

		return useridFound;
	}

	// -----------------End Checking userid exist or not---------------
}
