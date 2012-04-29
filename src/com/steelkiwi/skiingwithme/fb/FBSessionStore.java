package com.steelkiwi.skiingwithme.fb;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.android.Facebook;

public class FBSessionStore {
    /* save facebook session data in shared preferences */
	public static boolean save(Facebook session, Context context) {
    	SharedPreferences pref = context.getSharedPreferences(FBConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
        editor.putString(FBConst.PREF_FACEBOOK_TOKEN, session.getAccessToken());
        editor.putLong(FBConst.PREF_FACEBOOK_EXPIRES, session.getAccessExpires());
        return editor.commit();
    }

	/* restore saved facebook session data from shared preferences */
    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(FBConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(FBConst.PREF_FACEBOOK_TOKEN, null));
        session.setAccessExpires(savedSession.getLong(FBConst.PREF_FACEBOOK_EXPIRES, 0));
        return session.isSessionValid();
    }

    /* clear shared preferences from saved facebook session data */
    public static void clear(Context context) {
    	SharedPreferences pref = context.getSharedPreferences(FBConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(FBConst.PREF_FACEBOOK_TOKEN);
		editor.remove(FBConst.PREF_FACEBOOK_EXPIRES);
        editor.commit();
    }
    
    public static String getAccessToken(Context context){
    	SharedPreferences pref = context.getSharedPreferences(FBConst.PREFERENCE_NAME, Context.MODE_PRIVATE);
    	return pref.getString(FBConst.PREF_FACEBOOK_TOKEN, null);
    }
}
