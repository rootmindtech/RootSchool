package com.rootmind.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.naming.NamingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rootmind.wrapper.DataArrayWrapper;
import com.rootmind.wrapper.StudentProfileWrapper;

public class SpeakerRecognition {

	public DataArrayWrapper speakerCreateProfile() throws Exception {

		// Connection con = null;
		// ResultSet resultSet = null;

		DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		// String sql=null;

		// SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.applyPattern("###,###,###,##0.00");
		formatter.setDecimalFormatSymbols(symbols);
		// PreparedStatement pstmt=null;

		// String gcmKey=null;
		// String gcmActivate=null;
		// String gcmURL=null;
		String result = "";

		try {
			/*
			 * con = getConnection();
			 * 
			 * //-----GCMKey GCMActivate code--
			 * 
			 * sql="SELECT GCMKey,GCMActivate,GCMURL from MST_Parameter";
			 * 
			 * pstmt = con.prepareStatement(sql);
			 * 
			 * resultSet = pstmt.executeQuery(); if (resultSet.next()) {
			 * 
			 * gcmKey=Utility.trim(resultSet.getString("GCMKey"));
			 * gcmActivate=Utility.trim(resultSet.getString("GCMActivate"));
			 * gcmURL=Utility.trim(resultSet.getString("GCMURL")); }
			 * 
			 * resultSet.close(); pstmt.close();
			 */

			// ----------
			/*
			 * if(gcmActivate !=null && gcmActivate.equals("Y")) {
			 */
			JsonObject mainJsonObj = new JsonObject();
			// JsonObject childJsonObj = new JsonObject();
			// childJsonObj.addProperty("body",body);
			// childJsonObj.addProperty("title",title);
			// childJsonObj.addProperty("icon",icon);
			mainJsonObj.addProperty("locale", "en-us");

			/*
			 * if(toTokenId !=null && !toTokenId.equals("")) { mainJsonObj.addProperty("to",
			 * toTokenId); } else {
			 * 
			 * 
			 * 
			 * Gson gson = new GsonBuilder().create(); JsonArray registration_ids_array =
			 * gson.toJsonTree(registration_ids).getAsJsonArray();
			 * 
			 * System.out.println("registration_ids_array is = " + registration_ids_array);
			 * 
			 * mainJsonObj.add("registration_ids", registration_ids_array);
			 * 
			 * 
			 * 
			 * }
			 */

			// "d7WX4aaCHIA:APA91bE1vbhCltXu7BDaul7EcPV8XKY5RuP05rUNsJjzakZJ1h8eEXrOZAqyb-1q3-2hyCYXuc69_sA85V5JOi7maPeoBodCSnurn1E4lygvOib7ZWmAPouPrOOaP7eDiYWomtGqB6EH");

			String jsonFormattedString = mainJsonObj.toString();
			String urlParameters = jsonFormattedString.replaceAll("\\\\", "");

			System.out.println("urlParameters is = " + urlParameters);

			byte[] postData = urlParameters.getBytes("UTF-8");
			int postDataLength = postData.length;
			String request = "https://api.projectoxford.ai/spid/v1.0/verificationProfiles"; // "https://gcm-http.googleapis.com/gcm/send";
			URL url = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Ocp-Apim-Subscription-Key", "cd6200b084504aac9604beb059bf1c34"); // AIzaSyChya7IJ7KWKIlJbQIxv-apjxtcuStIBUg
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "UTF-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			// POST
			DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
			System.out.println("before write " + postData.toString());
			writer.write(postData);
			System.out.println("after write ");
			writer.flush();

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = reader.readLine()) != null) {
				result += line;
			}

			writer.close();
			reader.close();
			System.out.println("Speaker Profile Creation " + result);

			// }
			StudentProfileWrapper studentProfileWrapper = new StudentProfileWrapper();
			studentProfileWrapper.recordFound = true;
			dataArrayWrapper.studentProfileWrapper = new StudentProfileWrapper[1];
			dataArrayWrapper.studentProfileWrapper[0] = studentProfileWrapper;
			dataArrayWrapper.recordFound = true;

		} /*
			 * catch (SQLException se) { se.printStackTrace(); throw new
			 * SQLException(se.getSQLState()+ " ; "+ se.getMessage()); } catch
			 * (NamingException ne) { ne.printStackTrace(); throw new
			 * NamingException(ne.getMessage()); }
			 */
		catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			/*
			 * try { releaseConnection(resultSet, con); } catch (SQLException se) {
			 * se.printStackTrace(); throw new Exception(se.getSQLState()+ " ; "+
			 * se.getMessage()); }
			 */
		}

		return dataArrayWrapper;
	}

}
