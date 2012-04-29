package com.steelkiwi.skiingwithme;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.steelkiwi.skiingwithme.api.RequestRunnable;
import com.steelkiwi.skiingwithme.entities.AuthResponseData;
import com.steelkiwi.skiingwithme.fb.FBConst;
import com.steelkiwi.skiingwithme.fb.FBSessionStore;
import com.steelkiwi.skiingwithme.fb.FBUser;

public class SkiingWithMe extends Activity implements OnClickListener, DialogListener {
	/** Called when the activity is first created. */

	// ==================== CONSTANTS ======================================
	private final String TAG = this.getClass().getSimpleName();

	// ===================== CLASS ATRIBUTES ===============================
	private Facebook facebookClient;
	private FBUser fbUserData;
	private ProgressDialog pd;

	// ===== OVERRIDEN =======

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		this.initializeWidgets();

		this.facebookClient = new Facebook(FBConst.FACEBOOK_APPID);

		FBSessionStore.restore(facebookClient, this);

		if (facebookClient.isSessionValid()) {

			FBUser user = this.getFBUserData();
			if (user != null) {
				this.launchSWMAuthThread(user);
			} else {
				Toast.makeText(this, R.string.authActivityGetFBInfoFailed, Toast.LENGTH_LONG).show();
			}

		}
	}

	private void initializeWidgets() {
		Button authFBButton = (Button) findViewById(R.id.buttonAuthenticateFB);
		authFBButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonAuthenticateFB:

			String[] permissions = { FBConst.FACEBOOK_PERMISSION_PUBLISH_STREAM, FBConst.FACEBOOK_PERMISSION_OFFLINE, FBConst.FACEBOOK_PERMISSION_USER_ABOUT_ME };

			facebookClient.authorize(this, permissions, -1, this);
			break;

		default:
			break;
		}

	}

	// ===== FACEBOOK OVERRIFEN METHODS =====

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		Log.i(TAG, "auth fb complete");
		if (values.isEmpty()) {
			return;
		} else {
			FBSessionStore.save(facebookClient, getApplicationContext());
			Toast.makeText(this, R.string.authActivityAuthSuccessfully, Toast.LENGTH_SHORT).show();// **********

			FBUser user = this.getFBUserData();
			if (user != null) {
				this.launchSWMAuthThread(user);
			} else {
				Toast.makeText(this, R.string.authActivityGetFBInfoFailed, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onFacebookError(FacebookError e) {
		// TODO Auto-generated method stub
		Log.i(TAG, "fb error");
	}

	@Override
	public void onError(DialogError e) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onError");
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		Log.i(TAG, "authCancel");
		Toast.makeText(this, R.string.authActivityAuthWasCancelled, Toast.LENGTH_LONG).show();
	}

	// ===== CLASS METHODS =====
	private void launchSWMAuthThread(FBUser userData) {
		this.fbUserData = userData;

		Map<String, String> params = new HashMap<String, String>();
		params.put(AC.HttpParamName.ACCESS_TOKEN, this.facebookClient.getAccessToken());
		params.put(AC.HttpParamName.FB_ID, userData.getId());
		params.put(AC.HttpParamName.FIRST_NAME, userData.getFirstName());
		params.put(AC.HttpParamName.LAST_NAME, userData.getLastName());
		params.put(AC.HttpParamName.USER_PICTURE, userData.getAvatarUrl());

		RequestRunnable authSWM = new RequestRunnable(Request.AUTH_REQUEST, params, threadHandler);
		Thread t = new Thread(authSWM);
		t.start();
		
		this.pd=new ProgressDialog(this);
		this.pd.setMessage(getResources().getString(R.string.authActivityProgressAuthenticcate));
		this.pd.show();
	}

	private FBUser getFBUserData() {
		try {
			Bundle params = new Bundle();
			params.putString("fields", "id,first_name,last_name");
			String userDataString = this.facebookClient.request("me/", params, "GET");
			JSONObject userJSON = new JSONObject(userDataString);
			String userId = userJSON.getString("id");
			String userFirstName = userJSON.getString("first_name");
			String userLastName = userJSON.getString("last_name");
			String userPhotoUrl = "http://graph.facebook.com/" + userId + "/picture";

			Log.i(TAG, userDataString);

			return new FBUser(userId, userFirstName, userLastName, userPhotoUrl);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void onSWMAuthSuccess(AuthResponseData userData) {
		if (this.fbUserData.getId().equals(userData.getId())) {
			// start main activity
			Intent mainIntent = new Intent(this, SWMMainActivity.class);
			mainIntent.putExtra("userdata", userData);
			startActivity(mainIntent);
		} else {
			// error
			Toast.makeText(this, R.string.authActivityUnknownAuthError, Toast.LENGTH_LONG).show();
		}
	}

	private void onSWMAuthFailed() {
		Toast.makeText(this, R.string.authActivityRequestParamsFailed, Toast.LENGTH_LONG).show();
	}

	// arg1 - thread id
	// what - success or fail
	private Handler threadHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			
			pd.dismiss();
			
			switch (msg.arg1) {

			case AC.TI.AUTH_SWM_THREAD:
				if (msg.what == AC.TR.THREAD_SUCCESS) {
					Log.i(TAG, "auth thread successfully completed");
					onSWMAuthSuccess((AuthResponseData) msg.obj);
				} else {
					Log.i(TAG, "auth thread failed!");
					onSWMAuthFailed();
				}

				break;

			default:
				Log.i(TAG, "some another thread executed");
				break;

			}
		}

	};
}