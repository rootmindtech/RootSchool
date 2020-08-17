package com.rootmind.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.rootmind.wrapper.AbstractWrapper;

public class JasperReports {

	/*
	 * public AbstractWrapper invokeReports(){
	 * 
	 * String result=null;
	 * 
	 * //http://192.168.1.110:8070/jasperserver/flow.html?_flowId=viewReportFlow&
	 * standAlone=true&_flowId=viewReportFlow&ParentFolderUri=%2FRootSchool&
	 * reportUnit=%2FSwarnandhra%2FSubject_Marks
	 * 
	 * JsonObject mainJsonObj = new JsonObject(); JsonObject childJsonObj = new
	 * JsonObject(); //childJsonObj.addProperty("body",body);
	 * //childJsonObj.addProperty("title",title);
	 * //childJsonObj.addProperty("icon",icon);
	 * mainJsonObj.add("notification",childJsonObj); String jsonFormattedString =
	 * mainJsonObj.toString(); String urlParameters =
	 * jsonFormattedString.replaceAll("\\\\", "");
	 * 
	 * System.out.println("urlParameters is = " + urlParameters);
	 * 
	 * 
	 * byte[] postData = urlParameters.getBytes("UTF-8"); int postDataLength =
	 * postData.length; String request =
	 * "http://rootmindpc1:8080/jasperserver/flow.html?_flowId=viewReportFlow&reportUnit=/School/StudentProfile&standAlone=true&ParentFolderUri=/School&j_username=jasperadmin&j_password=jasperadmin"
	 * ;//"https://gcm-http.googleapis.com/gcm/send"; URL url = new URL( request );
	 * HttpURLConnection conn= (HttpURLConnection) url.openConnection();
	 * conn.setDoOutput( true ); conn.setInstanceFollowRedirects( false );
	 * //conn.setRequestProperty("Authorization","key="+gcmKey);
	 * //AIzaSyChya7IJ7KWKIlJbQIxv-apjxtcuStIBUg conn.setRequestMethod("POST");
	 * conn.setRequestProperty("Content-Type", "application/json");
	 * conn.setRequestProperty("charset", "UTF-8");
	 * conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
	 * conn.setUseCaches(false); // POST DataOutputStream writer = new
	 * DataOutputStream(conn.getOutputStream());
	 * System.out.println("before write "+postData.toString());
	 * writer.write(postData); System.out.println("after write "); writer.flush();
	 * 
	 * 
	 * 
	 * String line; BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream()));
	 * 
	 * while ((line = reader.readLine()) != null) { result += line; }
	 * 
	 * writer.close(); reader.close(); System.out.println("result "+result);
	 * 
	 * }
	 * 
	 * return result;
	 */

}
