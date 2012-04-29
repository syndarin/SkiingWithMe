package com.steelkiwi.skiingwithme.fb;

public class FBConst {

	/* define sharedpreferences name */
	public static String PREFERENCE_NAME = "SKIING_FB";
	
	/* Facebook constants */
	
	/* get this key when registering the app at facebook */
	public static final String FACEBOOK_APPID = "143252899073551";
	
	/* define permission to post messages on the facebook wall */
	public static final String FACEBOOK_PERMISSION_PUBLISH_STREAM = "publish_stream";
	public static final String FACEBOOK_PERMISSION_OFFLINE = "offline_access";
	public static final String FACEBOOK_PERMISSION_USER_ABOUT_ME = "user_about_me";
	
	
	/* define info to store in the app sharedpreferences */
	public static final String PREF_FACEBOOK_TOKEN = "access_token";
	public static final String PREF_FACEBOOK_EXPIRES = "expires_in";
	
}
