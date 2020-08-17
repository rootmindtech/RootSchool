package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;

import com.rootmind.wrapper.SchoolAttendanceWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class SchoolAttendanceHelper extends Helper {

	// ---------------------------Start
	// fetchSchoolAttendance----------------------------

	public AbstractWrapper fetchSchoolAttendance(UsersWrapper usersProfileWrapper, SchoolAttendanceWrapper schoolAttendanceWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;

		String orderBy = null;
		String groupName = null;

		try {

			con = getConnection();

			//gradeID = schoolAttendanceWrapper.gradeID;

			if (Utility.isEmpty(schoolAttendanceWrapper.academicYearID)) {
				// -----get current academic year code--

				sql = "SELECT CurrentAcademicYear from MST_Parameter where SchoolID=?";

				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, usersProfileWrapper.schoolID);
				resultSet = pstmt.executeQuery();
				if (resultSet.next()) {

					schoolAttendanceWrapper.academicYearID = resultSet.getString("CurrentAcademicYear");

				}

				System.out.println("CurrentAcademicYear " + schoolAttendanceWrapper.academicYearID);

				resultSet.close();
				pstmt.close();
			}

			
			//------------Morning status-----
			
			groupName="DAYNAME";
			orderBy="DAY";
			
			if(!Utility.isEmpty(schoolAttendanceWrapper.duration))
			{
				if(schoolAttendanceWrapper.duration.equals("YEAR"))
				{
					groupName="MONTHNAME";
					orderBy="MONTH";
				}
			}

				
			sql = " SELECT COUNT(MorningStatus) as MorningStatus, "+ groupName + "(CalendarDate) as CalendarDate FROM StudentAttendance "
					+ " where AcademicYearID=? AND MorningStatus='P' and SchoolID=? ";



			if (!Utility.isEmpty(schoolAttendanceWrapper.gradeID)) {

				sql = sql + " and GradeID=? ";
			}
			
			if(!Utility.isEmpty(schoolAttendanceWrapper.duration))
			{
				if(schoolAttendanceWrapper.duration.equals("24HRS"))
				{
					sql = sql + " and CalendarDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY) ";
				}
				else if(schoolAttendanceWrapper.duration.equals("7DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 7 DAY) ";
					
				}
				else if(schoolAttendanceWrapper.duration.equals("30DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 30 DAY) ";
					
				}
			}

					
			sql = sql + " Group BY "+groupName +"(CalendarDate)";// ORDER BY "+ orderBy +"(CalendarDate)";
			// ------------------Attendance Present---------------------

			System.out.println(" sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolAttendanceWrapper.academicYearID);
			pstmt.setString(2, usersProfileWrapper.schoolID);


			
			if (!Utility.isEmpty(schoolAttendanceWrapper.gradeID)) {
				pstmt.setString(3, schoolAttendanceWrapper.gradeID);

			} 
			
			//System.out.println("pstmt " + pstmt);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				SchoolAttendanceWrapper schoolAttendanceWrapperMoring = new SchoolAttendanceWrapper();

				schoolAttendanceWrapperMoring.morningStatus = Utility.trim(resultSet.getString("MorningStatus"));
				schoolAttendanceWrapperMoring.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				schoolAttendanceWrapperMoring.recordFound = true;
				System.out.println("SchoolAttendance Count morningStatus fetch successful");

				vector.addElement(schoolAttendanceWrapperMoring);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.morningAttendancePresentWrapper = new SchoolAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.morningAttendancePresentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();
			
			//--------------Evening Status----

			sql = " SELECT COUNT(EveningStatus) as EveningStatus, "+groupName+"(CalendarDate) as CalendarDate FROM StudentAttendance "
					+ " where AcademicYearID=? AND EveningStatus='P' and SchoolID=? ";



			if (!Utility.isEmpty(schoolAttendanceWrapper.gradeID)) {

				sql = sql + " and GradeID=? ";
			}
					
			if(!Utility.isEmpty(schoolAttendanceWrapper.duration))
			{
				if(schoolAttendanceWrapper.duration.equals("24HRS"))
				{
					sql = sql + " and CalendarDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY) ";
				}
				else if(schoolAttendanceWrapper.duration.equals("7DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 7 DAY) ";
					
				}
				else if(schoolAttendanceWrapper.duration.equals("30DAYS"))
				{
					sql = sql + " and CalendarDate >= (DATE(NOW()) - INTERVAL 30 DAY) ";
					
				}
			}

					
			sql = sql + " Group BY "+groupName+"(CalendarDate)";// ORDER BY "+ orderBy +"(CalendarDate)";
			// ------------------Attendance Present---------------------

			System.out.println(" sql " + sql);

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, schoolAttendanceWrapper.academicYearID);
			pstmt.setString(2, usersProfileWrapper.schoolID);
			
			if (!Utility.isEmpty(schoolAttendanceWrapper.gradeID)) {
				pstmt.setString(3, schoolAttendanceWrapper.gradeID);

			} 

			resultSet = pstmt.executeQuery();
			vector.clear();
			while (resultSet.next()) {
				SchoolAttendanceWrapper schoolAttendanceWrapperEvening = new SchoolAttendanceWrapper();

				schoolAttendanceWrapperEvening.eveningStatus = Utility.trim(resultSet.getString("EveningStatus"));
				schoolAttendanceWrapperEvening.calendarDate = Utility.trim(resultSet.getString("CalendarDate"));

				schoolAttendanceWrapperEvening.recordFound = true;
				System.out.println("SchoolAttendance Count EveningStatus fetch successful");

				
				vector.addElement(schoolAttendanceWrapperEvening);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.eveningAttendancePresentWrapper = new SchoolAttendanceWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.eveningAttendancePresentWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			// if (resultSet != null)
			// resultSet.close();
			// if (pstmt != null)
			// pstmt.close();

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

	// ---------------------------End
	// fetchSchoolAttendance----------------------------

}
