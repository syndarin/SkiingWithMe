package com.steelkiwi.skiingwithme;

public class AC {
	
	public class TR{
		public final static int THREAD_SUCCESS=0;
		public final static int THREAD_ERROR=1;
	}
	
	public class TI{
		public final static int AUTH_SWM_THREAD=0;
		public final static int RESORTS_THREAD=1;
		public final static int FOLLOWERS_THREAD=2;
		public final static int LOCATION_THREAD=3;
	}
	
	public class HttpParamName{
		public final static String ACCESS_TOKEN="access_token";
		public final static String FIRST_NAME="first_name";
		public final static String LAST_NAME="last_name";
		public final static String FB_ID="id";
		public final static String USER_PICTURE="picture";
		public final static String LATITUDE="latitude";
		public final static String LONGITUDE="longitude";
	}
	
	public class ServerKeys{
		// auth SWM server keys
		public final static String AUTH_PICTURE="picture";
		public final static String AUTH_FIRST_NAME="first_name";
		public final static String AUTH_LAST_NAME="last_name";
		public final static String AUTH_LONGITUDE="longitude";
		public final static String AUTH_LATITUDE="latitude";
		public final static String AUTH_ID="id";
		
		// resort
		public final static String RESORT_ID="pk";
		public final static String RESORT_TITLE="title";
		public final static String RESORT_COUNTRY="country";
		public final static String RESORT_MAP="map_points";
		public final static String RESORT_SLOPES="slopes";
		
		// slope
		public final static String SLOPE_ID="id";
		public final static String SLOPE_TITLE="title";
		public final static String SLOPE_DIFFICULTY="difficulty";
		public final static String SLOPE_COLOR="color";
		public final static String SLOPE_IMAGE="image";
		public final static String SLOPE_LENGTH="length";
		public final static String SLOPE_COORDINATES="map_points";
		
		// coords
		public final static String COORD_LAT="lat";
		public final static String COORD_LONG="long";
		
		//  followers
		public final static String FOL_ID="id";
		public final static String FOL_FIRST_NAME="first_name";
		public final static String FOL_LAST_NAME="last_name";
		public final static String FOL_AVATAR="picture";
		public final static String FOL_LOCATION="location";
		public final static String FOL_LAT="latitude";
		public final static String FOL_LONG="longitude";
		
	}

}
