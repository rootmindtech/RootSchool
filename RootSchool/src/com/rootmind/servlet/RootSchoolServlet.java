package com.rootmind.servlet;

//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.RenderingHints;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

//import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

//import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rootmind.controller.FileUploadController;
import com.rootmind.controller.StudentAcademicsController;
import com.rootmind.controller.StudentMarksController;
import com.rootmind.controller.StudentProfileController;
import com.rootmind.controller.TeacherReportsController;
import com.rootmind.controller.TeachersProfileController;
import com.rootmind.controller.UsersController;
import com.rootmind.helper.BookIssueHelper;
import com.rootmind.helper.CalendarActivitiesHelper;
import com.rootmind.helper.Constants;
import com.rootmind.helper.DashboardHelper;
import com.rootmind.helper.ExamCalendarHelper;
import com.rootmind.helper.FileUploadHelper;
import com.rootmind.helper.GradeSubjectsHelper;
import com.rootmind.helper.GroupMenuHelper;
import com.rootmind.helper.HostelHelper;
import com.rootmind.helper.HostelInOutHelper;
import com.rootmind.helper.ImageDetailsHelper;
import com.rootmind.helper.BookCatalogueHelper;
import com.rootmind.helper.OnlinePaymentHelper;
import com.rootmind.helper.ParentMessageHelper;
import com.rootmind.helper.PopoverHelper;
import com.rootmind.helper.SchoolAcademicsHelper;
import com.rootmind.helper.SchoolAttendanceHelper;
import com.rootmind.helper.SchoolFeeHelper;
import com.rootmind.helper.SchoolMessageHelper;
import com.rootmind.helper.ServiceTicketsHelper;
import com.rootmind.helper.SpeakerRecognition;
import com.rootmind.helper.StudentAcademicsHelper;
import com.rootmind.helper.StudentAttendanceHelper;
import com.rootmind.helper.StudentDiaryHelper;
import com.rootmind.helper.StudentMessageHelper;
import com.rootmind.helper.StudentProfileHelper;
import com.rootmind.helper.SupervisorHelper;
import com.rootmind.helper.TeacherReportsHelper;
import com.rootmind.helper.TeacherSubjectsHelper;
import com.rootmind.helper.TeachersProfileHelper;
import com.rootmind.helper.TransportHelper;
import com.rootmind.helper.UserGroupHelper;
//import com.rootmind.helper.UserAuditHelper;
import com.rootmind.helper.UserMenuHelper;
import com.rootmind.helper.UsersHelper;
import com.rootmind.helper.Utility;
import com.rootmind.wrapper.BookIssueWrapper;
import com.rootmind.wrapper.CalendarActivitiesWrapper;
import com.rootmind.wrapper.DashboardWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ExamCalendarWrapper;
import com.rootmind.wrapper.FileUploadWrapper;
import com.rootmind.wrapper.GradeSubjectsWrapper;
import com.rootmind.wrapper.GroupMenuWrapper;
import com.rootmind.wrapper.HostelInOutWrapper;
import com.rootmind.wrapper.HostelWrapper;
import com.rootmind.wrapper.ImageDetailsWrapper;
import com.rootmind.wrapper.MessengerServiceWrapper;
import com.rootmind.wrapper.BookCatalogueWrapper;
import com.rootmind.wrapper.OnlinePaymentWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.ParentMessageWrapper;
import com.rootmind.wrapper.PopoverWrapper;
import com.rootmind.wrapper.SchoolAcademicsWrapper;
import com.rootmind.wrapper.SchoolAttendanceWrapper;
import com.rootmind.wrapper.SchoolFeeWrapper;
import com.rootmind.wrapper.SchoolMessageWrapper;
import com.rootmind.wrapper.SchoolWrapper;
import com.rootmind.wrapper.ServiceTicketsWrapper;
import com.rootmind.wrapper.StudentAcademicsWrapper;
import com.rootmind.wrapper.StudentAttendanceWrapper;
import com.rootmind.wrapper.StudentDiaryWrapper;
import com.rootmind.wrapper.StudentMessageWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.SupervisorWrapper;
import com.rootmind.wrapper.TeacherReportsWrapper;
import com.rootmind.wrapper.TeacherSubjectsWrapper;
import com.rootmind.wrapper.TeachersProfileWrapper;
import com.rootmind.wrapper.TransportWrapper;
import com.rootmind.wrapper.UserGroupWrapper;
import com.rootmind.wrapper.UserMenuWrapper;
import com.rootmind.wrapper.UsersWrapper;

/**
 * Servlet implementation class RootSchoolServlet
 */
@WebServlet("/RootSchool")
@MultipartConfig
public class RootSchoolServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public RootSchoolServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String methodAction = request.getParameter("methodAction");

		if (methodAction == null)

		{

			PrintWriter errorOut = response.getWriter();

			response.setContentType("text/html");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			response.setHeader("Access-Control-Max-Age", "86400");

			System.out.println("Request received at GET Root School but methodAction null");

			String msg = "<html><body><h1>Invalid request received at Root School</h1></body></html>";

			errorOut.println(msg);
			errorOut.flush();
			errorOut.close();

			return;

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		try {

			String userProfile = request.getParameter("userProfile");
			System.out.println("param userProfile :" + userProfile);

			String message = request.getParameter("message");
			System.out.println("message :" + message);

			String methodAction = request.getParameter("methodAction");
			// String customerCode = request.getParameter("customerCode");

			System.out.println("method action in the servlet with maven " + methodAction);
			// System.out.println("customer code in the servlet " + customerCode);

			// String tableName = request.getParameter("tableName");

			StudentProfileWrapper studentProfileWrapper = null;
			StudentAcademicsWrapper studentAcademicsWrapper = null;
			StudentAcademicsWrapper[] studentAcademicsWrapperArray = null;
			StudentMessageWrapper studentMessageWrapper = null;
			SchoolMessageWrapper schoolMessageWrapper = null;
			StudentDiaryWrapper studentDiaryWrapper = null;
			ExamCalendarWrapper examCalendarWrapper = null;
			ExamCalendarWrapper[] examCalendarWrapperArray = null;
			GradeSubjectsWrapper gradeSubjectsWrapper = null;
			ImageDetailsWrapper imageDetailsWrapper = null;
			StudentAttendanceWrapper studentAttendanceWrapper = null;
			StudentAttendanceWrapper[] studentAttendanceWrapperArray = null;
			SchoolFeeWrapper schoolFeeWrapper = null;
			TeachersProfileWrapper teachersProfileWrapper = null;
			ParentMessageWrapper parentMessageWrapper = null;
			TeacherSubjectsWrapper teacherSubjectsWrapper = null;
			TeacherReportsWrapper teacherReportsWrapper = null;
			SchoolAcademicsWrapper schoolAcademicsWrapper = null;
			SchoolAttendanceWrapper schoolAttendanceWrapper = null;
			UserMenuWrapper userMenuWrapper = null;
			GroupMenuWrapper groupMenuWrapper = null;

			PopoverWrapper popoverWrapper = null;
			PopoverWrapper[] popoverWrapperArray = null;
			UsersController usersController = null;
			// PasswordWrapper passwordWrapper=null;
			UsersWrapper usersWrapper = null;

			FileUploadWrapper fileUploadWrapper = null;
			BookCatalogueWrapper bookCatalogueWrapper = null;
			BookIssueWrapper bookIssueWrapper = null;

			OnlinePaymentWrapper onlinePaymentWrapper = null;

			SchoolWrapper schoolWrapper = null;
			ParameterWrapper parameterWrapper = null;

			CalendarActivitiesWrapper calendarActivitiesWrapper = null;
			UserGroupWrapper userGroupWrapper = null;
			UserGroupWrapper userGroupWrapperArray[] = null;
			HostelWrapper hostelWrapper = null;
			SupervisorWrapper supervisorWrapper = null;
			HostelInOutWrapper hostelInOutWrapper = null;
			HostelInOutWrapper[] hostelInOutWrapperArray = null;
			ServiceTicketsWrapper serviceTicketsWrapper = null;
			TransportWrapper transportWrapper = null;
			DashboardWrapper dashboardWrapper = null;

			MessengerServiceWrapper messengerServiceWrapper=null;
			
			String errorCode = null;
			String errorDescription = null;

			ServletContext application = getServletConfig().getServletContext();

			System.out.println("min version  " + application.getMinorVersion());

			System.out.println("max version  " + application.getMajorVersion());

			System.out.println("content type" + request.getContentType());

			System.out.println("content length  " + request.getContentLength());

			UsersWrapper usersProfileWrapper = new UsersWrapper();
			DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

			UsersHelper usersHelper = new UsersHelper();

			// To suppress Audit for Cloud installation to reduce usaage of database
			// UserAuditHelper userAuditHelper=new UserAuditHelper();

			System.out.println("methodaction RootSchool  " + methodAction);

			System.out.println("before gson");
			Gson gson = new Gson();
			JsonObject mainJsonObj = new JsonObject();
			JsonElement elementObj = null;

			HttpSession session = null;
			dataArrayWrapper.usersWrapper = new UsersWrapper[1];

			// String localUserid=null;
			String localSessionid = null;

			session = request.getSession();
			// session.setAttribute("userid",usersWrapper.userid);
			// setting session to expiry in 30 mins
			localSessionid = (String) session.getId();
			System.out.println("local sessionid :" + localSessionid);
			boolean validSession = false;

			// ----------Customer section---//
			String schoolName = "School";

			// ---------

			if (methodAction == null)

			{

				PrintWriter errorOut = response.getWriter();

				response.setContentType("text/html");
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "POST");
				response.setHeader("Access-Control-Allow-Headers", "Content-Type");
				response.setHeader("Access-Control-Max-Age", "86400");

				System.out.println("Request received at POST " + schoolName + " but methodAction null");

				String msg = "<html><body><h1>Invalid request received at " + schoolName + " </h1></body></html>";

				errorOut.println(msg);
				errorOut.flush();
				errorOut.close();

				return;

			}

			// schoolRegister and schoolList - don't do validation
			if (userProfile != null && !methodAction.equals("schoolRegister") && !methodAction.equals("fetchSchoolList")
					&& !methodAction.equals("createStudentProfile") && !methodAction.equals("migrateStudentProfile")) {

				usersProfileWrapper = gson.fromJson(userProfile, UsersWrapper.class);

				if (Utility.isEmpty(usersProfileWrapper.schoolID) == true) {
					if (usersProfileWrapper.userid.indexOf("@") >= 0
							&& (usersProfileWrapper.userid.length() >= usersProfileWrapper.userid.indexOf("@") + 1)) {
						usersProfileWrapper.schoolID = usersProfileWrapper.userid
								.substring(usersProfileWrapper.userid.indexOf("@") + 1);
					} else {
						usersProfileWrapper.schoolID = Constants.bootSchool;
					}
				}

				usersProfileWrapper.ipAddress = request.getRemoteAddr();
				usersHelper.updateUserDetails(usersProfileWrapper.userid, usersProfileWrapper.noLoginRetry,
						localSessionid, usersProfileWrapper.deviceToken, usersProfileWrapper.schoolID);

				// To suppress Audit for Cloud installation to reduce usaage of database
				// userAuditHelper.updateUserAudit(usersProfileWrapper.userid, localSessionid,
				// methodAction, schoolName, message, usersProfileWrapper.schoolID);

				System.out.println("schoolID in userProfile check " + usersProfileWrapper.schoolID);

			}

			if (methodAction.equals("schoolRegister") || methodAction.equals("fetchSchoolList")
					|| methodAction.equals("createStudentProfile") || methodAction.equals("migrateStudentProfile")) {
				// usersWrapper= gson.fromJson(message,UsersWrapper.class);

				// usersWrapper.ipAddress=request.getRemoteAddr();
				// usersHelper.updateSessionDetails(usersWrapper.userid,
				// usersProfileWrapper.noLoginRetry, localSessionid, usersWrapper.deviceToken,
				// usersWrapper.schoolID);

				System.out.println("in schoolRegister");

				// //usersWrapper =
				// (UsersWrapper)usersHelper.validateCredentials(usersWrapper.userid,usersWrapper.password,"",true);
				// usersController = new UsersController();
				// usersWrapper = (UsersWrapper)usersController.validate(usersWrapper);
				//
				//
				// //session.setMaxInactiveInterval(usersWrapper.sessionExpiryTime);
				// usersWrapper.sessionid=localSessionid;
				//
				// dataArrayWrapper.usersWrapper[0]=usersWrapper;

				validSession = true;

				/*
				 * if(usersWrapper.recordFound==true && usersWrapper.validUser==true) {
				 * 
				 * dataArrayWrapper.recordFound=true;
				 * 
				 * } else { System.out.println("usersWrapper.validUser :" +
				 * usersWrapper.validUser ); dataArrayWrapper.recordFound=true; }
				 */

				dataArrayWrapper.validSession = validSession;
				dataArrayWrapper.recordFound = true;

				elementObj = gson.toJsonTree(dataArrayWrapper);

				mainJsonObj.add(methodAction, elementObj);

				if (dataArrayWrapper.recordFound == true) {
					mainJsonObj.addProperty("success", true);
				} else {
					mainJsonObj.addProperty("success", false);
				}

			} else if (methodAction.equals("validateUser")) {
				usersWrapper = gson.fromJson(message, UsersWrapper.class);

				// if login with email then no schoolID during validateUser
				if (Utility.isEmpty(usersWrapper.email) == false) {
					usersWrapper.schoolID = null;

				} else if (usersWrapper.userid.indexOf("@") >= 0
						&& (usersWrapper.userid.length() >= usersWrapper.userid.indexOf("@") + 1)) {
					usersWrapper.schoolID = usersWrapper.userid.substring(usersWrapper.userid.indexOf("@") + 1);
				} else {
					usersWrapper.schoolID = Constants.bootSchool;
				}

				System.out.println("in validateUser");

				// usersWrapper =
				// (UsersWrapper)usersHelper.validateCredentials(usersWrapper.userid,usersWrapper.password,"",true);
				usersController = new UsersController();
				usersWrapper = (UsersWrapper) usersController.validate(usersWrapper);

				// session.setMaxInactiveInterval(usersWrapper.sessionExpiryTime);
				usersWrapper.sessionid = localSessionid;

				// after getting school Id do update
				usersWrapper.ipAddress = request.getRemoteAddr();
				usersHelper.updateSessionDetails(usersWrapper.userid, usersProfileWrapper.noLoginRetry,
						usersWrapper.sessionid, usersWrapper.deviceToken, usersWrapper.schoolID);

				dataArrayWrapper.usersWrapper[0] = usersWrapper;

				validSession = true;

				/*
				 * if(usersWrapper.recordFound==true && usersWrapper.validUser==true) {
				 * 
				 * dataArrayWrapper.recordFound=true;
				 * 
				 * } else { System.out.println("usersWrapper.validUser :" +
				 * usersWrapper.validUser ); dataArrayWrapper.recordFound=true; }
				 */

				dataArrayWrapper.validSession = validSession;
				dataArrayWrapper.recordFound = true;

				elementObj = gson.toJsonTree(dataArrayWrapper);

				mainJsonObj.add(methodAction, elementObj);

				if (dataArrayWrapper.recordFound == true) {
					mainJsonObj.addProperty("success", true);
				} else {
					mainJsonObj.addProperty("success", false);
				}

			} else {

				System.out.println("usersProfileWrapper.schoolID " + usersProfileWrapper.schoolID);

				UsersWrapper userSessionWrapper = (UsersWrapper) usersHelper
						.fetchSessionDetails(usersProfileWrapper.userid, usersProfileWrapper.schoolID);

				System.out.println("usersProfileWrapper.sessionid " + usersProfileWrapper.sessionid);
				System.out.println("userSessionWrapper.sessionid " + userSessionWrapper.sessionid);

				if (usersProfileWrapper.sessionid == null || userSessionWrapper.sessionid == null
						|| !usersProfileWrapper.sessionid.equals(userSessionWrapper.sessionid)) {
					validSession = false;

					System.out.println("Invalid session");

					dataArrayWrapper.validSession = validSession;
					dataArrayWrapper.recordFound = true;

					elementObj = gson.toJsonTree(dataArrayWrapper);

					mainJsonObj.add(methodAction, elementObj);

					if (dataArrayWrapper.recordFound == true) {
						mainJsonObj.addProperty("success", true);
					} else {
						mainJsonObj.addProperty("success", false);
					}

				} else {
					validSession = true;
				}

			}

			if (message != null && validSession == true) {

				switch (methodAction) {

				case "insertStudentProfile":
				case "updateStudentProfile":
				case "fetchStudentProfile": {

					studentProfileWrapper = gson.fromJson(message, StudentProfileWrapper.class);
					break;
				}
				case "insertStudentAcademics":
				case "updateStudentAcademics":
				case "fetchStudentAcademics":
				case "fetchStudentMarks": {

					studentAcademicsWrapper = gson.fromJson(message, StudentAcademicsWrapper.class);
					break;
				}
				case "updateStudentMarks": {

					studentAcademicsWrapperArray = gson.fromJson(message, StudentAcademicsWrapper[].class);
					break;
				}
				case "insertStudentMessage":
				case "updateStudentMessage":
				case "fetchStudentMessage": {

					studentMessageWrapper = gson.fromJson(message, StudentMessageWrapper.class);
					break;
				}
				case "updateStudentDiary":
				case "fetchStudentDiary": {

					studentDiaryWrapper = gson.fromJson(message, StudentDiaryWrapper.class);
					break;
				}
				case "insertSchoolMessage":
				case "updateSchoolMessage":
				case "fetchSchoolMessage": {

					schoolMessageWrapper = gson.fromJson(message, SchoolMessageWrapper.class);
					break;
				}
				case "insertExamCalendar":
				case "fetchExamCalendar": {

					examCalendarWrapper = gson.fromJson(message, ExamCalendarWrapper.class);
					break;
				}
				case "updateExamCalendar": {

					examCalendarWrapperArray = gson.fromJson(message, ExamCalendarWrapper[].class);
					break;
				}
				case "fetchMultiPopoverData": {

					popoverWrapperArray = gson.fromJson(message, PopoverWrapper[].class);
					break;
				}
				case "fetchMasterData":
				case "fetchTableNames":
				case "updateMasterData": {

					popoverWrapper = gson.fromJson(message, PopoverWrapper.class);
					break;
				}
				case "fetchStudentsQueue":
				case "fetchStudentSearch": {

					studentProfileWrapper = gson.fromJson(message, StudentProfileWrapper.class);
					break;
				}
				case "uploadImageDetails":
				case "updateImageDetails":
				case "fetchImageDetails": {
					imageDetailsWrapper = gson.fromJson(message, ImageDetailsWrapper.class);
					break;

				}
				// case "fetchImageFileNames":
				//
				// {
				// imageDetailsWrapper = gson.fromJson(message, ImageDetailsWrapper.class);
				// break;
				//
				// }
				case "updateImageStatus": {
					imageDetailsWrapper = gson.fromJson(message, ImageDetailsWrapper.class);
					break;

				}
				case "fetchClassStudents": {
					studentProfileWrapper = gson.fromJson(message, StudentProfileWrapper.class);
					break;

				}
				case "fetchStudentAttendance":
				case "fetchAttendanceByStudent": {

					studentAttendanceWrapper = gson.fromJson(message, StudentAttendanceWrapper.class);
					break;

				}
				case "updateStudentAttendance": {

					studentAttendanceWrapperArray = gson.fromJson(message, StudentAttendanceWrapper[].class);
					break;

				}
				case "fetchSchoolFee":
				case "updateSchoolFee": {

					schoolFeeWrapper = gson.fromJson(message, SchoolFeeWrapper.class);
					break;

				}
				case "updateGradeSubjects":
				case "fetchGradeSubjects": {

					gradeSubjectsWrapper = gson.fromJson(message, GradeSubjectsWrapper.class);
					break;

				}
				case "updateTeacherSubjects":
				case "fetchTeacherSubjects": {

					teacherSubjectsWrapper = gson.fromJson(message, TeacherSubjectsWrapper.class);
					break;

				}
				case "updateTeacherReports":
				case "fetchTeacherReports":
				case "validateReportAccess": {

					teacherReportsWrapper = gson.fromJson(message, TeacherReportsWrapper.class);
					break;

				}
				case "updateUserMenu":
				case "fetchUserMenu": {

					userMenuWrapper = gson.fromJson(message, UserMenuWrapper.class);
					break;

				}
				case "updateGroupMenu":
				case "fetchGroupMenu": {

					groupMenuWrapper = gson.fromJson(message, GroupMenuWrapper.class);
					break;

				}
				case "updateLoginProfile":
				case "fetchLoginProfile": {

					usersWrapper = gson.fromJson(message, UsersWrapper.class);
					break;

				}
				case "insertTeachersProfile":
				case "updateTeachersProfile":
				case "fetchTeachersProfile": {

					teachersProfileWrapper = gson.fromJson(message, TeachersProfileWrapper.class);
					break;

				}
				case "insertParentMessage":
				case "fetchParentMessage": {

					parentMessageWrapper = gson.fromJson(message, ParentMessageWrapper.class);
					break;

				}
				case "fetchUsersStaff":
				case "changePassword": {

					usersWrapper = gson.fromJson(message, UsersWrapper.class);
					break;

				}
				case "fetchStudentAcademicScholastic": {

					studentAcademicsWrapper = gson.fromJson(message, StudentAcademicsWrapper.class);
					break;

				}
				case "fetchStudentAttendanceCount": {

					studentAttendanceWrapper = gson.fromJson(message, StudentAttendanceWrapper.class);
					break;

				}
				case "fetchSchoolAcademics": {

					schoolAcademicsWrapper = gson.fromJson(message, SchoolAcademicsWrapper.class);
					break;

				}
				case "fetchSchoolAttendance": {

					schoolAttendanceWrapper = gson.fromJson(message, SchoolAttendanceWrapper.class);
					break;

				}
				case "dataFileUpload":
				case "dataFileUploadDelete":
				case "dataFileUploadView": {

					fileUploadWrapper = gson.fromJson(message, FileUploadWrapper.class);
					break;

				}
				case "updateStudentAttendanceUpload":
				case "updateStudentMarksUpload": {

					studentAcademicsWrapperArray = gson.fromJson(message, StudentAcademicsWrapper[].class);
					break;

				}
				case "insertBookDetails":
				case "updateBookDetails":
				case "fetchBookDetails":
				case "fetchBookSearch": {

					bookCatalogueWrapper = gson.fromJson(message, BookCatalogueWrapper.class);
					break;

				}
				case "insertBookIssue":
				case "updateBookIssue":
				case "fetchBookIssue": {

					bookIssueWrapper = gson.fromJson(message, BookIssueWrapper.class);
					break;

				}
				case "insertOnlinePayment":
				case "fetchOnlinePayment": {

					onlinePaymentWrapper = gson.fromJson(message, OnlinePaymentWrapper.class);
					break;

				}
				case "schoolRegister":
				case "fetchSchoolRegister":
				case "updateSchoolRegister":
				case "fetchSchoolList": {

					schoolWrapper = gson.fromJson(message, SchoolWrapper.class);
					break;

				}
				case "updateParameters": {

					parameterWrapper = gson.fromJson(message, ParameterWrapper.class);
					break;

				}
				case "createStudentProfile": // to create without login
				case "migrateStudentProfile": {

					studentProfileWrapper = gson.fromJson(message, StudentProfileWrapper.class);
					break;

				}
				case "updateCalendarActivities":
				case "fetchCalendarActivities": {

					calendarActivitiesWrapper = gson.fromJson(message, CalendarActivitiesWrapper.class);
					break;

				}
				case "fetchUserGroupList": {

					userGroupWrapper = gson.fromJson(message, UserGroupWrapper.class);
					break;

				}
				case "updateUserGroupList": {

					userGroupWrapperArray = gson.fromJson(message, UserGroupWrapper[].class);
					break;

				}
				case "fetchHostel":
				case "updateHostel": {

					hostelWrapper = gson.fromJson(message, HostelWrapper.class);
					break;

				}
				case "fetchSupervisor":
				case "updateSupervisor": {

					supervisorWrapper = gson.fromJson(message, SupervisorWrapper.class);
					break;

				}
				case "fetchHostelInOut": {

					hostelInOutWrapper = gson.fromJson(message, HostelInOutWrapper.class);
					break;

				}
				case "updateHostelInOut": {

					hostelInOutWrapperArray = gson.fromJson(message, HostelInOutWrapper[].class);
					break;

				}
				case "insertServiceTicket":
				case "updateServiceTicket":
				case "fetchServiceTicket": {

					serviceTicketsWrapper = gson.fromJson(message, ServiceTicketsWrapper.class);
					break;

				}
				case "insertTransport":
				case "updateTransport":
				case "updateTransportResponse":
				case "fetchTransport": {

					transportWrapper = gson.fromJson(message, TransportWrapper.class);
					break;

				}

				case "fetchDashboardCount": {

					dashboardWrapper = gson.fromJson(message, DashboardWrapper.class);
					break;

				}
				case "fetchMessengerServiceList":
				case "fetchMessengerService":
				{

					messengerServiceWrapper = gson.fromJson(message, MessengerServiceWrapper.class);
					break;

				}

				}

			} // --if message not null condition close

			// ------end of JSON parsing

			PrintWriter out = response.getWriter();

			System.out.println("methodaction  " + methodAction);

			System.out.println("before gson :");

			// if validSession then start process
			if (validSession == true) {

				switch (methodAction) {

				case "insertStudentProfile": {

					System.out.println("Insert Student profile   " + methodAction);

					StudentProfileController studentProfileController = new StudentProfileController();

					dataArrayWrapper = (DataArrayWrapper) studentProfileController.validate(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}

				case "updateStudentProfile": {

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println("update student profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper.updateStudentProfile(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}
				case "fetchStudentProfile": {

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println("fetch student profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper.fetchStudentProfile(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}
				case "updateStudentAcademics": {

					System.out.println("update student academics   " + methodAction);

					StudentAcademicsController studentAcademicsController = new StudentAcademicsController();
					dataArrayWrapper = (DataArrayWrapper) studentAcademicsController.validate(usersProfileWrapper,
							studentAcademicsWrapper);
					break;

				}

				case "fetchStudentAcademics": {

					StudentAcademicsHelper studentAcademicsHelper = new StudentAcademicsHelper();

					System.out.println("fetch Student Academics   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentAcademicsHelper
							.fetchStudentAcademics(usersProfileWrapper, studentAcademicsWrapper);
					break;

				}

				case "fetchStudentMarks": {

					StudentAcademicsHelper studentAcademicsHelper = new StudentAcademicsHelper();

					System.out.println("fetch Student Marks   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentAcademicsHelper.fetchStudentMarks(usersProfileWrapper,
							studentAcademicsWrapper);
					break;

				}
				case "updateStudentMarks": {

					System.out.println("update Student Marks   " + methodAction);
					StudentMarksController studentMarksController = new StudentMarksController();

					dataArrayWrapper = (DataArrayWrapper) studentMarksController.validate(usersProfileWrapper,
							studentAcademicsWrapperArray);
					break;

				}
				case "insertStudentMessage": {

					StudentMessageHelper studentMessageHelper = new StudentMessageHelper();

					System.out.println("insert studentMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentMessageHelper.insertStudentMessage(usersProfileWrapper,
							studentMessageWrapper);
					break;

				}
				case "updateStudentMessage": {

					StudentMessageHelper studentMessageHelper = new StudentMessageHelper();

					System.out.println("update studentMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentMessageHelper.updateStudentMessage(usersProfileWrapper,
							studentMessageWrapper);
					break;

				}
				case "fetchStudentMessage": {

					StudentMessageHelper studentMessageHelper = new StudentMessageHelper();

					System.out.println("fetch studentMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentMessageHelper.fetchStudentMessage(usersProfileWrapper,
							studentMessageWrapper);
					break;

				}

				case "updateStudentDiary": {

					StudentDiaryHelper studentDiaryHelper = new StudentDiaryHelper();

					System.out.println("update Student Diary    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentDiaryHelper.updateStudentDiary(usersProfileWrapper,
							studentDiaryWrapper);
					break;

				}
				case "fetchStudentDiary": {

					StudentDiaryHelper studentDiaryHelper = new StudentDiaryHelper();

					System.out.println("fetch StudentDiary    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentDiaryHelper.fetchStudentDiary(usersProfileWrapper,
							studentDiaryWrapper);
					break;

				}
				case "insertSchoolMessage": {

					SchoolMessageHelper schoolMessageHelper = new SchoolMessageHelper();

					System.out.println("insert schoolMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) schoolMessageHelper.insertSchoolMessage(usersProfileWrapper,
							schoolMessageWrapper);
					break;

				}

				case "updateSchoolMessage": {

					SchoolMessageHelper schoolMessageHelper = new SchoolMessageHelper();

					System.out.println("update schoolMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) schoolMessageHelper.updateSchoolMessage(usersProfileWrapper,
							schoolMessageWrapper);

				}

				case "fetchSchoolMessage": {

					SchoolMessageHelper schoolMessageHelper = new SchoolMessageHelper();

					System.out.println("fetch schoolMessage    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) schoolMessageHelper.fetchSchoolMessage(usersProfileWrapper,
							schoolMessageWrapper);
					break;

				}
				case "updateExamCalendar": {

					ExamCalendarHelper examCalendarHelper = new ExamCalendarHelper();

					System.out.println("update exam Calendar    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) examCalendarHelper.updateExamCalendar(usersProfileWrapper,
							examCalendarWrapperArray);
					break;

				}
				case "fetchExamCalendar": {

					ExamCalendarHelper examCalendarHelper = new ExamCalendarHelper();

					System.out.println("fetch exam Calendar    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) examCalendarHelper.fetchExamCalendar(usersProfileWrapper,
							examCalendarWrapper);
					break;

				}

				case "fetchMultiPopoverData": {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetch MultiPopoverController  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchMultiPopoverData(usersProfileWrapper,
							popoverWrapperArray);
					break;

				}

				case "fetchTableNames": {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetchTableNames PopoverData  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchTableNames(usersProfileWrapper,
							popoverWrapper);
					break;

				}
				case "fetchMasterData": {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("fetchMasterData   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchMasterData(usersProfileWrapper,
							popoverWrapper);
					break;

				}
				case "updateMasterData": {

					PopoverHelper popoverHelper = new PopoverHelper();

					System.out.println("update PopoverData  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) popoverHelper.updateMasterData(usersProfileWrapper,
							popoverWrapper);
					break;

				}
				case "fetchStudentsQueue": {

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println("fetch students queue   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper.fetchStudentsQueue(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}
				case "fetchStudentSearch": {

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println(" fetchStudentSearch    " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper.fetchStudentSearch(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}

				case "fetchGradeSubjects": {

					GradeSubjectsHelper gradeSubjectsHelper = new GradeSubjectsHelper();

					System.out.println("fetchGradeSubjects first the  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) gradeSubjectsHelper.fetchGradeSubjects(usersProfileWrapper,
							gradeSubjectsWrapper);
					break;

				}

				case "insertTeachersProfile": {

					System.out.println("Insert Teachers profile   " + methodAction);
					TeachersProfileController teachersProfileController = new TeachersProfileController();
					dataArrayWrapper = (DataArrayWrapper) teachersProfileController.validate(usersProfileWrapper,
							teachersProfileWrapper);
					break;

				}

				case "updateTeachersProfile": {

					TeachersProfileHelper teachersProfileHelper = new TeachersProfileHelper();

					System.out.println("update teachers profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teachersProfileHelper
							.updateTeachersProfile(usersProfileWrapper, teachersProfileWrapper);
					break;

				}
				case "fetchTeachersProfile": {

					TeachersProfileHelper teachersProfileHelper = new TeachersProfileHelper();

					System.out.println("fetch teachers profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teachersProfileHelper
							.fetchTeachersProfile(usersProfileWrapper, teachersProfileWrapper);
					break;

				}

				case "uploadImageDetails": {

					// /*
					// * String password = "password"; String passwordEnc =
					// * AESEncryption.encrypt(password);
					// */
					//
					// // Create path components to save the file
					// String path = request.getParameter("destination");
					// String originalFilePath = path;
					//
					// final Part filePart = request.getPart("file");
					//
					// final String fileName = getSubmittedFileName(filePart);
					//
					// OutputStream outputStream = null;
					// InputStream filecontent = null;
					// OutputStream outputStreamThumbnail = null;
					// InputStream filecontentThumbnail = null;
					//
					// // final PrintWriter writer = response.getWriter();
					//
					// try {
					//
					// System.out.println("path " + path);
					// System.out.println("filePart " + filePart);
					// System.out.println("fileName " + fileName);
					//
					// ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();
					// path = imageDetailsHelper.fetchImagePath(usersProfileWrapper.schoolID) +
					// path;
					//
					// File folderPath = new File(path);
					//
					// if (!folderPath.exists()) {
					// System.out.println("creating directory: " + path);
					// boolean result = false;
					//
					// try {
					// folderPath.mkdirs();
					// result = true;
					// } catch (SecurityException se) {
					// // handle it
					// se.printStackTrace();
					// }
					// if (result) {
					// System.out.println("DIR created");
					// }
					// }
					//
					// outputStream = new FileOutputStream(new File(path + File.separator +
					// fileName));
					//
					// filecontent = filePart.getInputStream();
					//
					// int read = 0;
					// final byte[] bytes = new byte[16384];
					//
					// while ((read = filecontent.read(bytes)) != -1) {
					// outputStream.write(bytes, 0, read);
					//
					// // System.out.println("writing file " + bytes);
					// }
					//
					// // -------create thumbnail from profile image
					// if (imageDetailsWrapper.docID.trim().equals("DOC001": {
					//
					// outputStreamThumbnail = new FileOutputStream(
					// new File(path + File.separator + "thumbnail_" + fileName));
					// filecontentThumbnail = filePart.getInputStream();
					//
					// int readThumbnail = 0;
					// final byte[] bytesThumbnail = new byte[16384];
					//
					// while ((readThumbnail = filecontentThumbnail.read(bytesThumbnail)) != -1) {
					// outputStreamThumbnail.write(bytesThumbnail, 0, readThumbnail);
					// // System.out.println("writing file " + bytes);
					// }
					//
					// Image image = javax.imageio.ImageIO
					// .read(new File(path + File.separator + "thumbnail_" + fileName));
					//
					// int thumbWidth = 70;
					// int thumbHeight = 70;
					// // int quality=80;
					// double thumbRatio = (double) thumbWidth / (double) thumbHeight;
					// int imageWidth = image.getWidth(null);
					// int imageHeight = image.getHeight(null);
					// double imageRatio = (double) imageWidth / (double) imageHeight;
					// if (thumbRatio < imageRatio) {
					// thumbHeight = (int) (thumbWidth / imageRatio);
					// } else {
					// thumbWidth = (int) (thumbHeight * imageRatio);
					// }
					//
					// if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
					// thumbWidth = imageWidth;
					// thumbHeight = imageHeight;
					// } else if (imageWidth < thumbWidth)
					// thumbWidth = imageWidth;
					// else if (imageHeight < thumbHeight)
					// thumbHeight = imageHeight;
					//
					// BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
					// BufferedImage.TYPE_INT_RGB);
					// Graphics2D graphics2D = thumbImage.createGraphics();
					// graphics2D.setBackground(Color.WHITE);
					// graphics2D.setPaint(Color.WHITE);
					// graphics2D.fillRect(0, 0, thumbWidth, thumbHeight);
					// graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					// graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
					//
					// javax.imageio.ImageIO.write(thumbImage, "JPG",
					// new File(path + File.separator + "thumbnail_" + fileName));
					// }
					// // -------
					//
					// imageDetailsWrapper.imageFileName = fileName;
					// imageDetailsWrapper.imageFileFolder = originalFilePath;
					//
					// imageDetailsWrapper.imageUploadStatus = true;
					// // writer.println("New file " + fileName + " created at " + path);
					// // LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
					// // new Object[]{fileName, path});
					//
					// System.out.println("bytes " + bytes.length);
					//
					// } catch (FileNotFoundException fne) {
					//
					// System.out.println("File Not Found ");
					//
					// fne.printStackTrace();
					//
					// // writer.println("You either did not specify a file to upload or are "
					// // + "trying to upload a file to a protected or nonexistent "
					// // + "location.");
					// // writer.println("<br/> ERROR: " + fne.getMessage());
					//
					// // LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
					// // new Object[]{fne.getMessage()});
					// } finally {
					// if (outputStream != null) {
					// outputStream.close();
					// }
					// if (filecontent != null) {
					// filecontent.close();
					// }
					// if (outputStreamThumbnail != null) {
					// outputStreamThumbnail.close();
					// }
					// if (filecontentThumbnail != null) {
					// filecontentThumbnail.close();
					// }
					// // if (writer != null) {
					// // writer.close();
					// // }
					// }

					ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

					System.out.println("Upload Image Details Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.uploadImageDetails(usersProfileWrapper,
							imageDetailsWrapper);
					break;

				}
				case "updateImageDetails": {

					ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

					System.out.println("updateImageDetails Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.updateImageDetails(usersProfileWrapper,
							imageDetailsWrapper);
					break;

				}
				case "fetchImageDetails": {

					// try {
					// BufferedImage img = ImageIO.read(new File("C://testfile.jpg"+ "":;

					ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

					System.out.println("Fetch Image Details Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.fetchImageDetails(usersProfileWrapper,
							imageDetailsWrapper);
					break;

					// commented on 13-May-2019 because retrieving image from Firebase storage
					// if (dataArrayWrapper.recordFound == true &&
					// dataArrayWrapper.imageDetailsWrapper.length > 0) {
					//
					// imageDetailsWrapper = dataArrayWrapper.imageDetailsWrapper[0];
					//
					// System.out.println("file path to fetch " +
					// imageDetailsWrapper.imageFileFolder
					// + File.separator + imageDetailsWrapper.imageFileName);
					// System.out.println(new File(imageDetailsWrapper.imageFileFolder +
					// File.separator
					// + imageDetailsWrapper.imageFileName).getCanonicalPath());
					//
					// BufferedImage img = ImageIO.read(new File(imageDetailsWrapper.imageFileFolder
					// + File.separator + imageDetailsWrapper.imageFileName));
					//
					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// ImageIO.write(img, "jpg", baos);
					// baos.flush();
					// byte[] imageInByte = baos.toByteArray();
					// // String encodedString = Base64.getEncoder().encodeToString(imageInByte);
					// String encodedString = new String(Base64.encodeBase64(imageInByte));
					// mainJsonObj.addProperty("image", encodedString);
					// baos.close();
					// System.out.println("Image added to JSON");
					// imageDetailsWrapper.imageFoundStatus = true;
					//
					// dataArrayWrapper.imageDetailsWrapper[0] = imageDetailsWrapper;
					//
					// System.out.println("Fetch Image success in servlet ");
					// }
					// } catch (IOException e) {
					// e.printStackTrace();
					// }

				}

				// case "updateImageStatus": {
				//
				// ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();
				//
				// System.out.println("Image Details Helper " + methodAction);
				//
				// dataArrayWrapper = (DataArrayWrapper)
				// imageDetailsHelper.updateImageStatus(usersProfileWrapper,
				// imageDetailsWrapper);
				// break;
				//
				//
				// }
				// case "fetchImageFileNames": {
				//
				// ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();
				//
				// System.out.println("Image filenames " + methodAction);
				//
				// dataArrayWrapper = (DataArrayWrapper)
				// imageDetailsHelper.fetchImageFileNames(usersProfileWrapper,
				// imageDetailsWrapper);
				//
				// }
				case "updateImageStatus": {

					ImageDetailsHelper imageDetailsHelper = new ImageDetailsHelper();

					System.out.println("Image Details Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) imageDetailsHelper.updateImageStatus(usersProfileWrapper,
							imageDetailsWrapper);
					break;

				}

				case "fetchClassStudents": {

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println("fetch Class Students   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper.fetchClassStudents(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}

				case "fetchStudentAttendance": {

					StudentAttendanceHelper studentAttendanceHelper = new StudentAttendanceHelper();

					System.out.println("fetch Student Attendance  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentAttendanceHelper
							.fetchStudentAttendance(usersProfileWrapper, studentAttendanceWrapper);
					break;

				}
				case "updateStudentAttendance": {

					StudentAttendanceHelper studentAttendanceHelper = new StudentAttendanceHelper();

					System.out.println("update Student Attendance  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentAttendanceHelper
							.updateStudentAttendance(usersProfileWrapper, studentAttendanceWrapperArray);
					break;

				}

				case "fetchSchoolFee": {

					SchoolFeeHelper schoolFeeHelper = new SchoolFeeHelper();

					System.out.println("fetch School Fee   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) schoolFeeHelper.fetchSchoolFee(usersProfileWrapper,
							schoolFeeWrapper);
					break;

				}
				case "updateSchoolFee": {

					SchoolFeeHelper schoolFeeHelper = new SchoolFeeHelper();

					System.out.println("update School Fee  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) schoolFeeHelper.updateSchoolFee(usersProfileWrapper,
							schoolFeeWrapper);
					break;

				}

				case "fetchAttendanceByStudent": {

					StudentAttendanceHelper studentAttendanceHelper = new StudentAttendanceHelper();

					System.out.println("fetchAttendanceByStudent  " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentAttendanceHelper
							.fetchAttendanceByStudent(usersProfileWrapper, studentAttendanceWrapper);
					break;

				}

				case "updateGradeSubjects": {

					GradeSubjectsHelper gradeSubjectsHelper = new GradeSubjectsHelper();

					System.out.println("update Grade Subjects   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) gradeSubjectsHelper.updateGradeSubjects(usersProfileWrapper,
							gradeSubjectsWrapper);
					break;

				}

				// case "fetchGradeSubjects":
				// {
				//
				//
				// GradeSubjectsHelper gradeSubjectsHelper=new GradeSubjectsHelper();
				//
				//
				// System.out.println("fetch Grade Subjects " + methodAction);
				// dataArrayWrapper =
				// (DataArrayWrapper)gradeSubjectsHelper.fetchGradeSubjects(usersProfileWrapper,
				// gradeSubjectsWrapper);
				//
				//
				//
				// }

				case "updateTeacherSubjects": {

					TeacherSubjectsHelper teacherSubjectsHelper = new TeacherSubjectsHelper();

					System.out.println("update Grade Subjects   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teacherSubjectsHelper
							.updateTeacherSubjects(usersProfileWrapper, teacherSubjectsWrapper);
					break;

				}

				case "fetchTeacherSubjects": {

					TeacherSubjectsHelper teacherSubjectsHelper = new TeacherSubjectsHelper();

					System.out.println("fetch Grade Subjects   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teacherSubjectsHelper
							.fetchTeacherSubjects(usersProfileWrapper, teacherSubjectsWrapper);
					break;

				}

				case "updateTeacherReports": {

					TeacherReportsHelper teacherReportsHelper = new TeacherReportsHelper();

					System.out.println("update  Reports   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teacherReportsHelper.updateTeacherReports(usersProfileWrapper,
							teacherReportsWrapper);
					break;

					// TeacherReportsController teacherReportsController=new
					// TeacherReportsController();
					// dataArrayWrapper =
					// (DataArrayWrapper)teacherReportsController.validate(usersProfileWrapper,teacherReportsWrapper);

				}
				case "validateReportAccess": {

					System.out.println("update  Reports   " + methodAction);

					TeacherReportsController teacherReportsController = new TeacherReportsController();
					dataArrayWrapper = (DataArrayWrapper) teacherReportsController.validate(usersProfileWrapper,
							teacherReportsWrapper);
					break;

				}

				case "fetchTeacherReports": {

					TeacherReportsHelper teacherReportsHelper = new TeacherReportsHelper();

					System.out.println("fetch  Reports   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) teacherReportsHelper
							.fetchTeacherReports(teacherReportsWrapper);
					break;

				}

				case "updateUserMenu": {

					UserMenuHelper userMenuHelper = new UserMenuHelper();

					System.out.println("updateUserMenu   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) userMenuHelper.updateUserMenu(usersProfileWrapper,
							userMenuWrapper);
					break;

				}

				case "fetchUserMenu": {

					UserMenuHelper userMenuHelper = new UserMenuHelper();

					System.out.println("fetchUserMenu   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) userMenuHelper.fetchUserMenu(usersProfileWrapper,
							userMenuWrapper);
					break;

				}
				case "updateGroupMenu": {

					GroupMenuHelper groupMenuHelper = new GroupMenuHelper();

					System.out.println("updateGroupMenu   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) groupMenuHelper.updateGroupMenu(usersProfileWrapper,
							groupMenuWrapper);
					break;

				}

				case "fetchGroupMenu": {

					GroupMenuHelper groupMenuHelper = new GroupMenuHelper();

					System.out.println("fetchGroupMenu   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) groupMenuHelper.fetchGroupMenu(usersProfileWrapper,
							groupMenuWrapper);
					break;

				}
				case "updateLoginProfile": {

					UsersHelper usersHelperLoginProfile = new UsersHelper();

					System.out.println("update Login profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) usersHelperLoginProfile
							.updateLoginProfile(usersProfileWrapper, usersWrapper, "");
					break;

				}

				case "fetchLoginProfile": {

					UsersHelper usersHelperFetchLogin = new UsersHelper();

					System.out.println("fetch Login profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) usersHelperFetchLogin.fetchLoginProfile(usersProfileWrapper,
							usersWrapper);
					break;

				}
				case "insertParentMessage": {

					ParentMessageHelper parentMessageHelper = new ParentMessageHelper();

					System.out.println("insertParentMessage  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) parentMessageHelper.insertParentMessage(usersProfileWrapper,
							parentMessageWrapper);
					break;

				}
				case "fetchParentMessage": {

					ParentMessageHelper parentMessageHelper = new ParentMessageHelper();

					System.out.println("fetchParentMessage  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) parentMessageHelper.fetchParentMessage(usersProfileWrapper,
							parentMessageWrapper);
					break;

				}

				case "fetchUsersStaff": {

					UsersHelper usersHelperfetch = new UsersHelper();

					System.out.println("fetchUsersStaff  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) usersHelperfetch
							.fetchUsersStaff(usersProfileWrapper.schoolID);
					break;

				}

				case "changePassword": {

					UsersHelper usersHelperfetch = new UsersHelper();

					System.out.println("fetchUsersStaff  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) usersHelperfetch.changePassword(usersProfileWrapper,
							usersWrapper);
					break;

				}

				case "fetchStudentAcademicScholastic": {

					StudentAcademicsHelper studentAcademicsHelper = new StudentAcademicsHelper();

					System.out.println("fetchStudentAcademicScholastic  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) studentAcademicsHelper
							.fetchStudentAcademicScholastic(usersProfileWrapper, studentAcademicsWrapper);
					break;

				}

				case "fetchStudentAttendanceCount": {

					StudentAttendanceHelper StudentAttendanceHelper = new StudentAttendanceHelper();

					System.out.println("FetchStudentAttendanceCount  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) StudentAttendanceHelper
							.fetchStudentAttendanceCount(usersProfileWrapper, studentAttendanceWrapper);
					break;

				}
				case "fetchSchoolAcademics": {

					SchoolAcademicsHelper schoolAcademicsHelper = new SchoolAcademicsHelper();

					System.out.println("FetchSchoolAcademics  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) schoolAcademicsHelper
							.fetchSchoolAcademics(usersProfileWrapper,schoolAcademicsWrapper);
					break;

				}

				case "fetchSchoolAttendance": {

					SchoolAttendanceHelper schoolAttendanceHelper = new SchoolAttendanceHelper();

					System.out.println("fetchSchoolAttendance  " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) schoolAttendanceHelper
							.fetchSchoolAttendance(usersProfileWrapper, schoolAttendanceWrapper);
					break;

				}

				// --------data file upload-----------

				case "dataFileUpload": {

					/*
					 * String password = "password"; String passwordEnc =
					 * AESEncryption.encrypt(password);
					 */

					// Create path components to save the file
					String path = request.getParameter("destination");
					String originalFilePath = path;

					final Part filePart = request.getPart("file");

					final String fileName = getSubmittedFileName(filePart);

					OutputStream outputStream = null;
					InputStream filecontent = null;

					// final PrintWriter writer = response.getWriter();

					try {

						System.out.println("path " + path);
						System.out.println("filePart " + filePart);
						System.out.println("fileName " + fileName);

						FileUploadHelper fileUploadHelper = new FileUploadHelper();
						fileUploadWrapper = fileUploadHelper.fetchDataFileTemplate(usersProfileWrapper,
								fileUploadWrapper);
						path = fileUploadWrapper.destinationPath + path;

						File folderPath = new File(path);

						// file creation date time
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						System.out.println("file creation date time : " + sdf.format(folderPath.lastModified()));

						if (!folderPath.exists()) {
							System.out.println("creating directory: " + path);
							boolean result = false;

							try {
								folderPath.mkdirs();
								result = true;
							} catch (SecurityException se) {
								// handle it
								se.printStackTrace();
							}
							if (result) {
								System.out.println("DIR created");
							}
						}

						outputStream = new FileOutputStream(new File(path + File.separator + fileName));

						filecontent = filePart.getInputStream();

						int read = 0;
						final byte[] bytes = new byte[16384];

						while ((read = filecontent.read(bytes)) != -1) {
							outputStream.write(bytes, 0, read);

							// System.out.println("writing file " + bytes);
						}

						fileUploadWrapper.dataFileName = fileName;
						fileUploadWrapper.dataFileFolder = originalFilePath;

						fileUploadWrapper.dataFileDateTime = sdf.format(folderPath.lastModified());
						fileUploadWrapper.destinationPath = path + File.separator + fileName;
						fileUploadWrapper.fileUploadStatus = true;
						// writer.println("New file " + fileName + " created at " + path);
						// LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
						// new Object[]{fileName, path});

						System.out.println("bytes " + bytes.length);

					} catch (FileNotFoundException fne) {

						System.out.println("File Not Found ");

						fne.printStackTrace();

						// writer.println("You either did not specify a file to upload or are "
						// + "trying to upload a file to a protected or nonexistent "
						// + "location.");
						// writer.println("<br/> ERROR: " + fne.getMessage());

						// LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
						// new Object[]{fne.getMessage()});
					} finally {
						if (outputStream != null) {
							outputStream.close();
						}
						if (filecontent != null) {
							filecontent.close();
						}

						// if (writer != null) {
						// writer.close();
						// }
					}

					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("File Upload Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileProcess(usersProfileWrapper,
							fileUploadWrapper);
					break;

				}
				// --------end data file upload-------

				case "dataFileUploadView": {

					// StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper();
					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("File Upload Helper " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileUploadView(usersProfileWrapper,
							fileUploadWrapper);
					break;

				}
				case "dataFileUploadDelete": {

					// StudentAcademicsHelper studentAcademicsHelper=new StudentAcademicsHelper();
					FileUploadController fileUploadController = new FileUploadController();

					System.out.println("dataFileUploadDelete " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) fileUploadController.dataFileUploadDelete(usersProfileWrapper,
							fileUploadWrapper);
					break;

				}
				case "updateStudentAttendanceUpload": {

					FileUploadController fileUploadController = new FileUploadController();

					dataArrayWrapper = (DataArrayWrapper) fileUploadController
							.updateStudentAttendanceUpload(usersProfileWrapper, studentAttendanceWrapperArray);
					break;

					// StudentAttendanceHelper studentAttendanceHelper=new
					// StudentAttendanceHelper();

					// System.out.println("update Student Attendance " + methodAction);
					// dataArrayWrapper =
					// (DataArrayWrapper)studentAttendanceHelper.updateStudentAttendance(usersProfileWrapper,studentAttendanceWrapperArray);

				}
				case "updateStudentMarksUpload": {

					FileUploadController fileUploadController = new FileUploadController();

					dataArrayWrapper = (DataArrayWrapper) fileUploadController
							.updateStudentMarksUpload(usersProfileWrapper, studentAcademicsWrapperArray);
					break;

					// StudentMarksController studentMarksController=new StudentMarksController();

					// dataArrayWrapper =
					// (DataArrayWrapper)studentMarksController.validate(usersProfileWrapper,studentAcademicsWrapperArray);

				}
				case "insertBookDetails": {

					BookCatalogueHelper bookCatalogueHelper = new BookCatalogueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookCatalogueHelper.insertBookDetails(usersProfileWrapper,
							bookCatalogueWrapper);
					break;

				}
				case "updateBookDetails": {

					BookCatalogueHelper bookCatalogueHelper = new BookCatalogueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookCatalogueHelper.updateBookDetails(usersProfileWrapper,
							bookCatalogueWrapper);
					break;

				}
				case "fetchBookDetails": {

					BookCatalogueHelper bookCatalogueHelper = new BookCatalogueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookCatalogueHelper.fetchBookDetails(usersProfileWrapper,
							bookCatalogueWrapper);
					break;

				}
				case "fetchBookSearch": {

					BookCatalogueHelper bookCatalogueHelper = new BookCatalogueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookCatalogueHelper.fetchBookSearch(usersProfileWrapper,
							bookCatalogueWrapper);
					break;

				}
				case "speakerCreateProfile": {

					SpeakerRecognition speakerRecognition = new SpeakerRecognition();

					dataArrayWrapper = (DataArrayWrapper) speakerRecognition.speakerCreateProfile();
					break;

				}
				case "insertBookIssue": {

					BookIssueHelper bookIssueHelper = new BookIssueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookIssueHelper.insertBookIssue(usersProfileWrapper,
							bookIssueWrapper);
					break;

				}
				case "updateBookIssue": {

					BookIssueHelper bookIssueHelper = new BookIssueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookIssueHelper.updateBookIssue(usersProfileWrapper,
							bookIssueWrapper);
					break;

				}
				case "fetchBookIssue": {

					BookIssueHelper bookIssueHelper = new BookIssueHelper();

					dataArrayWrapper = (DataArrayWrapper) bookIssueHelper.fetchBookIssue(usersProfileWrapper,
							bookIssueWrapper);
					break;

				}
				case "insertOnlinePayment": {

					OnlinePaymentHelper onlinePaymentHelper = new OnlinePaymentHelper();

					dataArrayWrapper = (DataArrayWrapper) onlinePaymentHelper.insertOnlinePayment(usersProfileWrapper,
							onlinePaymentWrapper);
					break;

				}
				case "fetchOnlinePayment": {

					OnlinePaymentHelper onlinePaymentHelper = new OnlinePaymentHelper();

					dataArrayWrapper = (DataArrayWrapper) onlinePaymentHelper.fetchOnlinePayment(usersProfileWrapper,
							onlinePaymentWrapper);
					break;

				}
				case "schoolRegister": {

					PopoverHelper popoverHelper = new PopoverHelper();

					dataArrayWrapper = (DataArrayWrapper) popoverHelper.createSchoolRegister(schoolWrapper);
					break;

				}
				case "fetchSchoolRegister": {

					PopoverHelper popoverHelper = new PopoverHelper();

					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchSchoolRegister(usersProfileWrapper,
							schoolWrapper);
					break;

				}
				case "updateSchoolRegister": {

					PopoverHelper popoverHelper = new PopoverHelper();

					dataArrayWrapper = (DataArrayWrapper) popoverHelper.updateSchoolRegister(usersProfileWrapper,
							schoolWrapper);
					break;

				}
				case "updateParameters": {

					PopoverHelper popoverHelper = new PopoverHelper();

					dataArrayWrapper = (DataArrayWrapper) popoverHelper.updateParameters(usersProfileWrapper,
							parameterWrapper);
					break;

				}
				case "fetchSchoolList": {

					PopoverHelper popoverHelper = new PopoverHelper();

					dataArrayWrapper = (DataArrayWrapper) popoverHelper.fetchSchoolRegister(usersProfileWrapper,
							schoolWrapper);
					break;

				}

				case "createStudentProfile": // to create student profile without login
				{

					System.out.println("Create Student profile   " + methodAction);
					// dataArrayWrapper =
					// (DataArrayWrapper)studentProfileHelper.insertStudentProfile(usersProfileWrapper,studentProfileWrapper);

					StudentProfileController studentProfileController = new StudentProfileController();

					// assign schoolID from student profile wrapper since usersProfile is not
					// available
					usersProfileWrapper.schoolID = studentProfileWrapper.schoolID;

					dataArrayWrapper = (DataArrayWrapper) studentProfileController.validate(usersProfileWrapper,
							studentProfileWrapper);
					break;

				}
				case "migrateStudentProfile": // to create student profile without login
				{

					StudentProfileHelper studentProfileHelper = new StudentProfileHelper();

					System.out.println("migrate student profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) studentProfileHelper
							.migrateStudentProfile(usersProfileWrapper, studentProfileWrapper);
					break;

				}
				case "updateCalendarActivities": {

					CalendarActivitiesHelper calendarActivitiesHelper = new CalendarActivitiesHelper();

					System.out.println("calendarActivitiesHelper  profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) calendarActivitiesHelper
							.updateCalendarActivities(usersProfileWrapper, calendarActivitiesWrapper);
					break;

				}
				case "fetchCalendarActivities": {

					CalendarActivitiesHelper calendarActivitiesHelper = new CalendarActivitiesHelper();

					System.out.println("fetchCalendarActivities  profile   " + methodAction);
					dataArrayWrapper = (DataArrayWrapper) calendarActivitiesHelper
							.fetchCalendarActivities(usersProfileWrapper, calendarActivitiesWrapper);
					break;
				}
				case "fetchUserGroupList": {

					UserGroupHelper userGroupHelper = new UserGroupHelper();

					System.out.println("fetchUserGroup method action " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) userGroupHelper.fetchUserGroupList(usersProfileWrapper,
							userGroupWrapper);
					break;
				}
				case "updateUserGroupList": {

					UserGroupHelper userGroupHelper = new UserGroupHelper();

					System.out.println("updateUserGroup method action " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) userGroupHelper.updateUserGroupList(usersProfileWrapper,
							userGroupWrapperArray);
					break;
				}
				case "fetchHostel": {

					System.out.println("fetchHostel " + methodAction);

					HostelHelper hostelHelper = new HostelHelper();

					dataArrayWrapper = (DataArrayWrapper) hostelHelper.fetchHostel(usersProfileWrapper, hostelWrapper);
					break;
				}
				case "updateHostel": {

					System.out.println("updateHostel " + methodAction);

					HostelHelper hostelHelper = new HostelHelper();

					dataArrayWrapper = (DataArrayWrapper) hostelHelper.updateHostel(usersProfileWrapper, hostelWrapper);
					break;
				}
				case "fetchSupervisor": {

					System.out.println("fetchSupervisor " + methodAction);

					SupervisorHelper supervisorHelper = new SupervisorHelper();

					dataArrayWrapper = (DataArrayWrapper) supervisorHelper.fetchSupervisor(usersProfileWrapper,
							supervisorWrapper);
					break;
				}
				case "updateSupervisor": {

					System.out.println("updateSupervisor " + methodAction);

					SupervisorHelper supervisorHelper = new SupervisorHelper();

					dataArrayWrapper = (DataArrayWrapper) supervisorHelper.updateSupervisor(usersProfileWrapper,
							supervisorWrapper);
					break;
				}
				case "fetchHostelInOut": {

					HostelInOutHelper hostelInOutHelper = new HostelInOutHelper();

					System.out.println("fetchHostelInOut method action " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) hostelInOutHelper.fetchHostelInOut(usersProfileWrapper,
							hostelInOutWrapper);
					break;
				}
				case "updateHostelInOut": {

					HostelInOutHelper hostelInOutHelper = new HostelInOutHelper();

					System.out.println("updateHostelInOut method action " + methodAction);

					dataArrayWrapper = (DataArrayWrapper) hostelInOutHelper.updateHostelInOut(usersProfileWrapper,
							hostelInOutWrapperArray);
					break;
				}
				case "insertServiceTicket": {

					System.out.println("insertServiceTicket " + methodAction);

					ServiceTicketsHelper serviceTicketsHelper = new ServiceTicketsHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceTicketsHelper.insertServiceTicket(usersProfileWrapper,
							serviceTicketsWrapper);
					break;
				}
				case "updateServiceTicket": {

					System.out.println("updateServiceTicket " + methodAction);

					ServiceTicketsHelper serviceTicketsHelper = new ServiceTicketsHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceTicketsHelper.updateServiceTicket(usersProfileWrapper,
							serviceTicketsWrapper);
					break;
				}
				case "fetchServiceTicket": {

					System.out.println("fetchServiceTicket is" + methodAction);

					ServiceTicketsHelper serviceTicketsHelper = new ServiceTicketsHelper();

					dataArrayWrapper = (DataArrayWrapper) serviceTicketsHelper.fetchServiceTicket(usersProfileWrapper,
							serviceTicketsWrapper);
					break;
				}
				case "insertTransport": {

					System.out.println("insertTransport " + methodAction);

					TransportHelper transportHelper = new TransportHelper();

					dataArrayWrapper = (DataArrayWrapper) transportHelper.insertTransport(usersProfileWrapper,
							transportWrapper);
					break;
				}
				case "updateTransport": {

					System.out.println("updateTransport " + methodAction);

					TransportHelper transportHelper = new TransportHelper();

					dataArrayWrapper = (DataArrayWrapper) transportHelper.updateTransport(usersProfileWrapper,
							transportWrapper);
					break;
				}
				case "updateTransportResponse": {

					System.out.println("updateTransportResponse " + methodAction);

					TransportHelper transportHelper = new TransportHelper();

					dataArrayWrapper = (DataArrayWrapper) transportHelper.updateTransportResponse(usersProfileWrapper,
							transportWrapper);
					break;
				}
				case "fetchTransport": {

					System.out.println("fetchTransport " + methodAction);

					TransportHelper transportHelper = new TransportHelper();

					dataArrayWrapper = (DataArrayWrapper) transportHelper.fetchTransport(usersProfileWrapper,
							transportWrapper);
					break;
				}
				case "fetchDashboardCount": {

                	System.out.println("fetchDashboardCount " + methodAction);
	        		
                	DashboardHelper dashboardHelper=new DashboardHelper();
		        	
		        	dataArrayWrapper=(DataArrayWrapper)dashboardHelper.fetchDashboardCount(usersProfileWrapper,dashboardWrapper);
					break;
				}
				case "fetchMessengerServiceList": {

					System.out.println("fetchMessageServiceList  " + methodAction);

					ParentMessageHelper parentMessageHelper = new ParentMessageHelper();

					dataArrayWrapper = (DataArrayWrapper) parentMessageHelper.fetchMessengerServiceList(usersProfileWrapper, messengerServiceWrapper);
					break;
				}
				case "fetchMessengerService": {

					System.out.println("fetchMessageService  " + methodAction);

					ParentMessageHelper parentMessageHelper = new ParentMessageHelper();

					dataArrayWrapper = (DataArrayWrapper) parentMessageHelper.fetchMessengerService(usersProfileWrapper, messengerServiceWrapper);
					break;
				}
				
				

				}

				dataArrayWrapper.validSession = validSession;
				elementObj = gson.toJsonTree(dataArrayWrapper);

				mainJsonObj.add(methodAction, elementObj);

				if (dataArrayWrapper.recordFound == true) {
					mainJsonObj.addProperty("success", true);
				} else {
					mainJsonObj.addProperty("success", false);
				}

			} // ----end of validSession condition

			mainJsonObj.addProperty("errorCode", errorCode);
			mainJsonObj.addProperty("errorDescription", errorDescription);
			mainJsonObj.addProperty("userid", usersProfileWrapper.userid);
			// mainJsonObj.addProperty("password",paramPassword);
			// mainJsonObj.addProperty("sessionid",paramSessionid);
			// mainJsonObj.addProperty("cifNumber",usersWrapper.cifNumber);

			response.setContentType("application/json");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			response.setHeader("Access-Control-Max-Age", "86400");

			System.out.println("user id is: " + usersProfileWrapper.userid);

			out.println(mainJsonObj.toString());
			out.flush();
			out.close();

			// case "fetchImageDetails")) {
			// mainJsonObj.remove("image");
			// System.out.println("image removed from user audit");
			//
			// // To suppress Audit for Cloud installation to reduce usaage of database
			// // userAuditHelper.updateUserAudit(usersProfileWrapper.userid,
			// localSessionid,
			// // methodAction, schoolName, mainJsonObj.toString(),
			// // usersProfileWrapper.schoolID);
			// } else {
			// // To suppress Audit for Cloud installation to reduce usaage of database
			// // userAuditHelper.updateUserAudit(usersProfileWrapper.userid,
			// localSessionid,
			// // methodAction, schoolName, mainJsonObj.toString(),
			// // usersProfileWrapper.schoolID);
			// }
			System.out.println("out close");

		} catch (JsonParseException jse) {
			jse.printStackTrace();
			System.out.println("parse exc " + jse.getMessage());

		}

		catch (Exception jse) {
			jse.printStackTrace();
			System.out.print("main exc " + jse.getMessage());
		}

	}

	private static String getSubmittedFileName(Part part) {

		System.out.println("part header " + part.getHeader("content-disposition"));

		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {

				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");

				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE
																													// fix.
			}
		}
		return null;
	}

}
