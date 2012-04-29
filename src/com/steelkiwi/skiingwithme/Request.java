package com.steelkiwi.skiingwithme;

public enum Request {
	
	AUTH_REQUEST("http://skiingwith.me/api/login/facebook/", AC.TI.AUTH_SWM_THREAD),
	RESORTS_REQUEST("http://skiingwith.me/api/resort/", AC.TI.RESORTS_THREAD),
	LOCATION_REQUEST("http://skiingwith.me/api/location/", AC.TI.FOLLOWERS_THREAD),
	FOLLOWERS_REQUEST("", AC.TI.FOLLOWERS_THREAD);
	
	private String url;
	private int threadId;
	
	private Request(String url, int threadId) {
		this.url = url;
		this.threadId = threadId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	
	
	
	

}
