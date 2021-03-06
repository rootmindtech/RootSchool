package com.rootmind.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.Locale;

import com.rootmind.helper.FCMNotification;
import com.rootmind.helper.SMSNotification;
//import com.rootmind.helper.GCMNotification;
import com.rootmind.helper.UsersHelper;
import com.rootmind.helper.Utility;
import com.rootmind.wrapper.AbstractWrapper;

import com.rootmind.wrapper.UsersWrapper;


public class UsersController {
	
	public AbstractWrapper validate(UsersWrapper usersWrapper)throws Exception {
		
		
		//DataArrayWrapper dataArrayWrapper = new DataArrayWrapper();
		
		
		//SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//		symbols.setGroupingSeparator(',');
//		formatter.applyPattern("###,###,###,##0.00");
//		formatter.setDecimalFormatSymbols(symbols);
		
		UsersWrapper usersWrapperSub=null;
		
		//UserMenuWrapper userMenuWrappersub=null;
		
		//AES128Crypto aes128Crypto=new AES128Crypto();
		//Vector<Object> vector = new Vector<Object>();
		//ErrorWrapper errorWrapper=null;
		
		try {
					if(Utility.isEmpty(usersWrapper.userid)==false)
					{
					
						//call helper
						UsersHelper	usersHelper=new UsersHelper(); 
		
			        	System.out.println("User Controller: Validating user  " + usersWrapper.schoolID);
			        	usersWrapperSub = (UsersWrapper)usersHelper.validateCredentials(usersWrapper);

//			        	System.out.println("Before SMS  " + usersWrapper.schoolID);
//			        	SMSNotification smsNotification = new SMSNotification(null, null, null, null);
//			        	smsNotification.sendSMS("919849342551", "This is first test message from API");
//			        	System.out.println("After SMS  " + usersWrapper.schoolID);
			        	
			        	//--user not found validation ---
			    		if(usersWrapperSub.recordFound==false)
			    		{
			    			System.out.println("usersWrapper.recordFound :"+ usersWrapper.recordFound);
			    			usersWrapper.validUser=false;
			    			usersWrapper.validUserID=false;
			    			usersWrapper.validPassword=false;
			    			

//			    			//commenting on 09-Mar-2019 for School Testing
//			    			if(Utility.isEmpty(usersWrapper.deviceToken)==false)
//			    			{
//				    			System.out.println("Going for FCM notificaiton :"+ usersWrapper.userid + " " + usersWrapper.deviceToken );
//								FCMNotification fcmNotification = new FCMNotification("LOGIN_FAILURE_ALERT", usersWrapper.schoolID, usersWrapper.userid, usersWrapper.deviceToken);
//								fcmNotification.sendLoginFailureMessage(usersWrapper.userid, usersWrapper.deviceToken);
//			    			}
			    			
			    			
			    			//dataArrayWrapper.usersWrapper[0]=usersWrapper;
			    			//dataArrayWrapper.recordFound=true;
    						usersWrapperSub.password=null;
			    			return usersWrapperSub;
			    		}
			    		else if (usersWrapperSub.recordFound==true)
			    		{
			    			//--inactive user validation
			    			if(!Utility.isEmpty(usersWrapperSub.status) && !usersWrapperSub.status.equals("ACTIVE"))
			    			{
			    				System.out.println("usersWrapper.status :"+ usersWrapper.status);
			    				usersWrapper.validUser=false;
				    			usersWrapper.validUserID=true; //userid exists
				    			usersWrapper.validPassword=false;

			    				//dataArrayWrapper.usersWrapper[0]=usersWrapper;
			    				//dataArrayWrapper.recordFound=true;

			    				usersWrapperSub.password=null;
			    				return usersWrapperSub;
			    			}
			    			
			    			//do not check for usergroup since allow staff from app also
//			    			if(!usersWrapperSub.userGroup.trim().equals(usersWrapper.userGroup.trim()))
//			    			{
//			    				
//			    				System.out.println("usersWrappersub.userGroup :"+ usersWrapperSub.userGroup);
//			    				System.out.println("usersWrapper.userGroup :"+ usersWrapper.userGroup);
//			    				usersWrapper.validUser=false;
//			    				//dataArrayWrapper.usersWrapper[0]=usersWrapper;
//			    				//dataArrayWrapper.recordFound=true;
//			    				return usersWrapperSub;
//			    			}
			    			//--invalid session id validation
			    			/*else if(newSession==false && !sessionid.trim().equals(usersWrapper.sessionid))
			    			{
			    				
			    				
			    				System.out.println("sessionid :"+ sessionid);
			    				System.out.println("usersWrapper.sessionid :"+ usersWrapper.sessionid);
			    				usersWrapper.validUser=false;
			    				//dataArrayWrapper.usersWrapper[0]=usersWrapper;
			    				//dataArrayWrapper.recordFound=true;
			    				return usersWrapper;
			    			}*/
			    			else
			    			{
			    				//System.out.println("update usersWrapper.noLoginRetry :"+ usersWrapper.noLoginRetry);
			    				
			    				/*if(newSession==true)
			    				{
			    					pstmt = con.prepareStatement("UPDATE Users set Lastlogindate=?, NoLoginRetry=?,sessionid=?,DeviceToken=?  where userid=?");
			    					
			    					pstmt.setTimestamp(1,new java.sql.Timestamp(System.currentTimeMillis()));
			    					pstmt.setInt(2,usersWrapper.noLoginRetry+1);
			    					pstmt.setString(3,(sessionid==null?"":sessionid.trim()));
			    					pstmt.setString(4,(deviceToken==null?"":deviceToken.trim()));
			    					pstmt.setString(5,(userid==null?"":userid.trim()));

			    					pstmt.executeUpdate();

			    					pstmt.close();
			    					usersWrapper.noLoginRetry=usersWrapper.noLoginRetry+1;
			    					usersWrapper.sessionid=(sessionid==null?"":sessionid.trim());
			    					usersWrapper.deviceToken=(deviceToken==null?"":deviceToken.trim());
			    				}*/
			    				
			    				//byte[] password = hexStringToByteArray(usersWrapper.password);
			    				//byte[] password = hexStringToByteArray("6uQs6iKN+GpXadLiX6KLkQ==");
			    				
			    				//System.out.println("Password After Converting "+password);
			    				//byte[] userPassword = hexStringToByteArray(usersWrapperSub.password);
			    				//byte[] userPassword = hexStringToByteArray("f9e1d2074c780ddb27d8e54b48f82590377222e061a924c591cd9c27ea163ed4");
			    				
			    				
			    				//System.out.println("userPassword After Converting "+userPassword);
			    				//if (usersWrapper.password != null && !usersWrapper.password.trim().equals("") && usersWrapperSub.password != null && !usersWrapperSub.password.trim().equals("") )

			    				if (Utility.isEmpty(usersWrapper.password)==false && Utility.isEmpty(usersWrapperSub.password)==false)
			    				{
			    					
			    					
			    					AES128Crypto aes128Crypto=new AES128Crypto();
			    					
			    					System.out.println("usersWrapper.password "+Utility.trim(usersWrapper.password));
			    		
			    					//if login with email then use schoolID from usersWrapperSub
			    					String md5Password=aes128Crypto.md5DB(Utility.trim(usersWrapper.password), (Utility.isEmpty(usersWrapper.schoolID))?usersWrapperSub.schoolID:usersWrapper.schoolID);
			    					
			    					System.out.println("md5Password "+md5Password.trim());
			    					System.out.println("usersWrapperSub.password "+Utility.trim(usersWrapperSub.password));

			    					
			    					//if(usersWrapper.password.equals(usersWrapperSub.password)){
			    					if(usersWrapperSub.password.trim().equals(md5Password.trim()))
			    					{
			    					
			    						System.out.println("User Validation Success");
			    						usersWrapperSub.validUser=true;
			    						usersWrapperSub.password=null;
						    			usersWrapper.validUserID=true;
						    			usersWrapper.validPassword=true;

			    					}
			    					else
			    					{
			    						
			    						usersWrapperSub.validUser=false;
			    						usersWrapperSub.password=null;
						    			usersWrapper.validUserID=true;
						    			usersWrapper.validPassword=false;


			    					}
			    				    			    				    
			    				}
			    				else
			    				{
			    					usersWrapperSub.validUser=false;
		    						usersWrapperSub.password=null;
					    			usersWrapper.validUserID=true;
					    			usersWrapper.validPassword=false;


			    				}

			    				//dataArrayWrapper.usersWrapper[0]=usersWrapper;
			    				//dataArrayWrapper.recordFound=true;
			    				System.out.println("validate credentials end");
			    			}
			    			
			    		}
			    		
			        	
					}

			
		} 
		 catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		finally
		{
			/*try
			{
				
			} 
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}*/
		}

		return usersWrapperSub;
	}
	

    // Method for converting Hex String to Byte array
/*	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}*/
}
