package com.rootmind.wrapper;

import java.math.BigDecimal;

public class UsersWrapper extends AbstractWrapper {
	
	
	public String userid=null;
	public String password=null;
	public String status=null;
	public String studentID=null;
	public String lastLoginDate=null;
	public String deviceToken=null;
	public BigDecimal trnLimit=null;
	public BigDecimal trnDayLimit=null;	
	public int noLoginRetry=0;
	public int maxRetry=0;
	public String sessionid=null;
	public String pwdChgDate=null;
	public int otp=0;
	public String businessUnit=null;
	public int sessionExpiryTime=0;
	public String userGroup=null;
	public String oldPassword=null;
	public String ipAddress=null;
	public boolean validUser=false;
	public boolean passwordChanged=false;
	public boolean oldPasswordRepeat=false;
	public boolean invalidOldPassword=false;
	public String refNo=null;
	public StudentProfileWrapper[] studentProfileWrapper=null;
	public String staffUserID=null;
	public String admin=null;
	public boolean sessionTimeOut=false;
	
	public UserMenuWrapper[] userMenuWrapper=null;
	public TeachersProfileWrapper teachersProfileWrapper=null;
	
	public String schoolID=null;
	public String makerID=null;  
	public String makerDateTime=null;
	public String modifierID=null;  
	public String modifierDateTime=null;
	
	public String schoolName=null;
	public String currentAcademicYear=null;

	public String email=null; //this is introduced for login purpose
	public String mobile=null; //this is introduced for login purpose
	public boolean validUserID=false; //this is to do auto login from Signin
	public boolean validPassword=false;  //this is to do auto login from Signin
	public String firebaseID=null; //to store images in firebase

	
	public boolean recordFound=false;
			
}
