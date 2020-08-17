package com.rootmind.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



public class SMSNotification extends Helper{

	private String threadName = null;
	private String schoolID = null;

	private String toTokenId = null;
	private String userid = null;

	
	
	public SMSNotification(String name, String schoolID, String userid, String toTokenId) {


		threadName = name;
		this.schoolID = schoolID;
		this.userid = userid;
		this.toTokenId = toTokenId;
		System.out.println("Creating SMS Thread " + threadName);
	}

	public String sendSMS(String mobileNo, String sms) {
		try {
			// Construct data
			String apiKey = "apikey=" + "JRXKxytnBgI-4Jar5cnzfD8cHD5aFYsmx9ENF4jIHG";
			String message = "&message=" + sms;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + mobileNo; //"918123456789";
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return "Error "+e;
		}
	}
	
}
