package com.rootmind.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;

import com.rootmind.wrapper.AbstractWrapper;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.ParameterWrapper;
import com.rootmind.wrapper.SchoolAcademicsWrapper;
import com.rootmind.wrapper.UsersWrapper;

public class SchoolAcademicsHelper extends Helper {

	// ---------------------------Start
	// fetchSchoolAcademics----------------------------

	public AbstractWrapper fetchSchoolAcademics(UsersWrapper usersProfileWrapper, SchoolAcademicsWrapper schoolAcademicsWrapper) throws Exception {

		Connection con = null;
		ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();

		Vector<Object> vector = new Vector<Object>();
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			
			PopoverHelper popoverHelper = new PopoverHelper();


			con = getConnection();

			if (Utility.isEmpty(schoolAcademicsWrapper.academicYearID)) {
				// -----get current academic year code--

				// ------------get MST_Parameter table details----
				ParameterWrapper parameterWrapper = (ParameterWrapper) popoverHelper
						.fetchParameters(usersProfileWrapper.schoolID);
				schoolAcademicsWrapper.academicYearID = parameterWrapper.currentAcademicYear;
			}

			sql = "SELECT Count(StudentID) as TotalStudents, AVG(SecuredMarks) as SecuredMarks, GradeID, SubjectID FROM StudentAcademics "
					+ " WHERE AcademicYearID =? and SchoolID=? ";
			
			if (!Utility.isEmpty(schoolAcademicsWrapper.gradeID)) {

				sql = " AND GradeID=? ";

				System.out.println("schoolAcademicsWrapper GradeID " + sql);
			}

			sql = sql + " GROUP BY GradeID, SubjectID";
			
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, Utility.trim(schoolAcademicsWrapper.academicYearID));
			pstmt.setString(2, Utility.trim(usersProfileWrapper.schoolID));

			if (!Utility.isEmpty(schoolAcademicsWrapper.gradeID)) {

				pstmt.setString(3, Utility.trim(schoolAcademicsWrapper.gradeID));

			}

			//System.out.println("pstmt " + pstmt);

			resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				schoolAcademicsWrapper = new SchoolAcademicsWrapper();
				schoolAcademicsWrapper.securedMarks = Utility.trim(resultSet.getString("SecuredMarks"));
				schoolAcademicsWrapper.totalStudents = Utility.trim(resultSet.getString("TotalStudents"));
				schoolAcademicsWrapper.gradeID = Utility.trim(resultSet.getString("GradeID"));
				schoolAcademicsWrapper.subjectID = Utility.trim(resultSet.getString("SubjectID"));
				
				
				schoolAcademicsWrapper.academicYearIDValue = popoverHelper.fetchPopoverDesc(
						schoolAcademicsWrapper.academicYearID, "MST_AcademicYear", usersProfileWrapper.schoolID);
				schoolAcademicsWrapper.gradeIDValue = popoverHelper.fetchPopoverDesc(schoolAcademicsWrapper.gradeID,
						"MST_Grade", usersProfileWrapper.schoolID);

				schoolAcademicsWrapper.subjectIDValue = popoverHelper.fetchPopoverDesc(schoolAcademicsWrapper.subjectID,
						"MST_Subject", usersProfileWrapper.schoolID);


				schoolAcademicsWrapper.recordFound = true;
				System.out.println("StudentAttendance Count morningStatus fetch successful");

				vector.addElement(schoolAcademicsWrapper);

			}

			if (vector.size() > 0) {
				dataArrayWrapper.schoolAcademicsWrapper = new SchoolAcademicsWrapper[vector.size()];
				vector.copyInto(dataArrayWrapper.schoolAcademicsWrapper);
				dataArrayWrapper.recordFound = true;

				System.out.println("total trn. in fetch " + vector.size());

			}

			resultSet.close();
			pstmt.close();

			if (resultSet != null)
				resultSet.close();
			if (pstmt != null)
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

	// ---------------------------End
	// fetchStudentAcademics----------------------------
}
