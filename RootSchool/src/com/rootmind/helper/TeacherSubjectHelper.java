package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.StudentAttendanceWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;
import com.rootmind.wrapper.TeacherSubjectWrapper;

public class TeacherSubjectHelper extends Helper {

	// ---------------------------Start
	// fetchTeacherSubject----------------------------

	/*
	 * public AbstractWrapper fetchTeacherSubject(StudentAttendanceWrapper
	 * studentAttendanceWrapper)throws Exception {
	 * 
	 * Connection con = null; ResultSet resultSet = null;
	 * 
	 * DataArrayWrapper dataArrayWrapper=new DataArrayWrapper();
	 * 
	 * 
	 * Vector<Object> vector = new Vector<Object>(); PreparedStatement pstmt =null;
	 * //String sql=null; String currentAcademicYear=null;
	 * 
	 * try { PopoverHelper popoverHelper = new PopoverHelper();
	 * 
	 * con = getConnection();
	 * 
	 * //----Current Academic Year--
	 * 
	 * pstmt =
	 * con.prepareStatement("SELECT CurrentAcademicYear from MST_Parameter");
	 * 
	 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
	 * 
	 * currentAcademicYear=resultSet.getString("CurrentAcademicYear");
	 * 
	 * }
	 * 
	 * resultSet.close(); pstmt.close();
	 * 
	 * //----------
	 * 
	 * 
	 * pstmt = con.
	 * prepareStatement("SELECT RefNo,Name FROM TeachersProfile WHERE Status='ACTIVE'"
	 * );
	 * 
	 * resultSet = pstmt.executeQuery(); while (resultSet.next()) {
	 * teacherSubjectWrapper=new TeacherSubjectWrapper();
	 * 
	 * studentAttendanceWrapper.refNo=Utility.trim(resultSet.getString("RefNo"));
	 * studentAttendanceWrapper.academicYearID=Utility.trim(resultSet.getString(
	 * "AcademicYearID"));
	 * studentAttendanceWrapper.studentID=Utility.trim(resultSet.getString(
	 * "StudentID"));
	 * studentAttendanceWrapper.gradeID=Utility.trim(resultSet.getString("GradeID"))
	 * ; studentAttendanceWrapper.sectionID=Utility.trim(resultSet.getString(
	 * "SectionID"));
	 * 
	 * 
	 * studentAttendanceWrapper.calendarDate=Utility.trim(resultSet.getString(
	 * "CalendarDate"));
	 * studentAttendanceWrapper.morningStatus=Utility.trim(resultSet.getString(
	 * "MorningStatus"));
	 * studentAttendanceWrapper.eveningStatus=Utility.trim(resultSet.getString(
	 * "EveningStatus"));
	 * studentAttendanceWrapper.messageID=Utility.trim(resultSet.getString(
	 * "MessageID"));
	 * studentAttendanceWrapper.message=Utility.trim(resultSet.getString("Message"))
	 * ; studentAttendanceWrapper.messageDate=
	 * Utility.trim(resultSet.getString("MessageDate"));
	 * studentAttendanceWrapper.delivered=
	 * Utility.trim(resultSet.getString("Delivered"));
	 * studentAttendanceWrapper.makerID=
	 * Utility.trim(resultSet.getString("MakerID"));
	 * studentAttendanceWrapper.makerDateTime=Utility.setDate(resultSet.getString(
	 * "MakerDateTime"));
	 * 
	 * studentAttendanceWrapper.studentName=Utility.trim(resultSet.getString(
	 * "StudentName"));
	 * studentAttendanceWrapper.surname=Utility.trim(resultSet.getString("Surname"))
	 * ;
	 * 
	 * studentAttendanceWrapper.academicYearIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.academicYearID, "MST_AcademicYear");
	 * studentAttendanceWrapper.gradeIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.gradeID, "MST_Grade");
	 * studentAttendanceWrapper.sectionIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.sectionID, "MST_Section");
	 * 
	 * studentAttendanceWrapper.recordFound=true;
	 * System.out.println("StudentAttendance Details fetch successful");
	 * 
	 * vector.addElement(studentAttendanceWrapper);
	 * 
	 * }
	 * 
	 * if (vector.size()>0) { dataArrayWrapper.studentAttendanceWrapper = new
	 * StudentAttendanceWrapper[vector.size()];
	 * vector.copyInto(dataArrayWrapper.studentAttendanceWrapper);
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("total trn. in fetch " + vector.size());
	 * 
	 * } else { StudentProfileHelper studentProfileHelper=new
	 * StudentProfileHelper(); StudentProfileWrapper studentProfileWrapper=new
	 * StudentProfileWrapper();
	 * 
	 * studentProfileWrapper.gradeID=studentAttendanceWrapper.gradeID;
	 * studentProfileWrapper.sectionID=studentAttendanceWrapper.sectionID;
	 * DataArrayWrapper
	 * subDataArrayWrapper=(DataArrayWrapper)studentProfileHelper.fetchClassStudents
	 * (studentProfileWrapper); if(subDataArrayWrapper.recordFound==true &&
	 * subDataArrayWrapper.studentProfileWrapper.length > 0) { vector.clear();
	 * for(int i=0; i<= subDataArrayWrapper.studentProfileWrapper.length-1;i++) {
	 * studentAttendanceWrapper=new StudentAttendanceWrapper();
	 * 
	 * studentAttendanceWrapper.academicYearID=subDataArrayWrapper.
	 * studentProfileWrapper[i].academicYearID;
	 * studentAttendanceWrapper.refNo=subDataArrayWrapper.studentProfileWrapper[i].
	 * refNo;
	 * studentAttendanceWrapper.gradeID=subDataArrayWrapper.studentProfileWrapper[i]
	 * .gradeID;
	 * studentAttendanceWrapper.sectionID=subDataArrayWrapper.studentProfileWrapper[
	 * i].sectionID;
	 * studentAttendanceWrapper.studentID=subDataArrayWrapper.studentProfileWrapper[
	 * i].studentID; studentAttendanceWrapper.studentName=subDataArrayWrapper.
	 * studentProfileWrapper[i].studentName;
	 * studentAttendanceWrapper.surname=subDataArrayWrapper.studentProfileWrapper[i]
	 * .surname;
	 * 
	 * 
	 * studentAttendanceWrapper.morningStatus="P";
	 * studentAttendanceWrapper.eveningStatus="P";
	 * 
	 * studentAttendanceWrapper.academicYearIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.academicYearID, "MST_AcademicYear");
	 * studentAttendanceWrapper.gradeIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.gradeID, "MST_Grade");
	 * studentAttendanceWrapper.sectionIDValue=popoverHelper.fetchPopoverDesc(
	 * studentAttendanceWrapper.sectionID, "MST_Section");
	 * 
	 * studentAttendanceWrapper.recordFound=true;
	 * 
	 * vector.addElement(studentAttendanceWrapper); } if (vector.size()>0) {
	 * dataArrayWrapper.studentAttendanceWrapper = new
	 * StudentAttendanceWrapper[vector.size()];
	 * vector.copyInto(dataArrayWrapper.studentAttendanceWrapper);
	 * dataArrayWrapper.recordFound=true;
	 * 
	 * System.out.println("total trn. in fetch " + vector.size());
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * if (resultSet!=null) resultSet.close(); pstmt.close();
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
	// ---------end fetchTeacherSubject-------
}
