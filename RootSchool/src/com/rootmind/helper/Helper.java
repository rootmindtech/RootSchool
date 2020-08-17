package com.rootmind.helper;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import com.google.apphosting.api.ApiProxy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Helper {


	public final Connection getConnection() throws SQLException, NamingException, Exception {
		Connection con = null;

		String environment = "LOCAL";


		
		try {
			


			// -------enable for local development----

			if (environment.equals("LOCAL")) {

				Context initialContext = new InitialContext();
				String applicationId = "ROOTSCHOOL";
				DataSource dataSource = (DataSource) initialContext.lookup("java:/" + applicationId + "DSN");
				con = dataSource.getConnection();
			}
			// ---------end local development

			// -----------for Google Cloud Platform ----
			if (environment.equals("CLOUD")) {

				// ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
				// Map<String,Object> attr = env.getAttributes();
				// String hostname = (String)
				// attr.get("com.google.appengine.runtime.default_version_hostname");
				//
				// String url = hostname.contains("localhost:")
				// ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
				// System.out.println("connecting to: " + url);
				// con = DriverManager.getConnection(url);
				

				con = ConnectionProviderHikariCP.getInstance().getConnection();
				

			}
			// -----------end Google Cloud Platform

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.getSQLState() + " " + se.getMessage());
		}

		// -------enable for local development----
		// catch (NamingException ne) {
		// ne.printStackTrace();
		// throw new SQLException(ne.getMessage());
		// }

		catch (Exception se) {
			se.printStackTrace();
			throw new Exception(se.getMessage());
		}
		return con;
	}

	public void releaseConnection(ResultSet rs, Connection con) throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (con != null) {
			con.close();
		}

	}
}
