package com.steelkiwi.skiingwithme.api;

import java.io.Serializable;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.steelkiwi.skiingwithme.AC;
import com.steelkiwi.skiingwithme.Request;

public class RequestRunnable implements Runnable {

	private final String TAG = this.getClass().getSimpleName();

	private Map<String, String> params;
	private Request requestType;
	private RequestSender sender;
	private Handler threadHandler;

	public RequestRunnable(Request requestType, Map<String, String> params, Handler threadHandler) {
		this.params = params;
		this.requestType = requestType;
		this.threadHandler=threadHandler;
		this.sender=new RequestSender();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Serializable response=null;
		Message threadMessage=this.threadHandler.obtainMessage();
		
		switch (requestType) {
		case AUTH_REQUEST:
			Log.i(TAG, "auth request");//*********************************************************
			response=this.sender.getUserParams(this.params);
			threadMessage.arg1=Request.AUTH_REQUEST.getThreadId();
			
			break;
			
		case RESORTS_REQUEST:
			Log.i(TAG, "resorts request");//******************************************************
			response=this.sender.getAllResorts();
			threadMessage.arg1=Request.RESORTS_REQUEST.getThreadId();
			break;
			
		case FOLLOWERS_REQUEST:
			Log.i(TAG, "followers request");//******************************************************
			String id=params.get("id");
			response=this.sender.getFollowers(id);
			threadMessage.arg1=Request.FOLLOWERS_REQUEST.getThreadId();
			break;
			
		case LOCATION_REQUEST:
			response=null;
			this.sender.sendLocation(params);
			threadMessage.arg1=Request.LOCATION_REQUEST.getThreadId();
			break;
			
		default:
			break;
		}
		
		threadMessage.what=(response==null) ? AC.TR.THREAD_ERROR:AC.TR.THREAD_SUCCESS;
		threadMessage.obj=response;
		this.threadHandler.sendMessage(threadMessage);
	}
}
