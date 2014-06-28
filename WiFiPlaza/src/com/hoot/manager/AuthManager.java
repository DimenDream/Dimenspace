package com.hoot.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.hoot.app.App;
import com.hoot.pojo.User;

public class AuthManager {
	private static final String USER_NAME = "user_name";
	private static final String AUTH_SESSION = "auth_session";
	private User mCurUser;
	private static AuthManager mInstance;
	private final static String SESSION_PREF = "session";
	private static final String EXPIRE = "expire";
	
	private SharedPreferences mPref;

	private AuthManager() {
		mPref = App.GlobalContext.getSharedPreferences(SESSION_PREF,
				Context.MODE_APPEND);
	}

	public static final synchronized AuthManager getInstance() {
		if (mInstance == null) {
			mInstance = new AuthManager();
		}
		return mInstance;
	}

	public User getAuthUser() {
		return mCurUser;
	}

	public void setAuthUser(User user) {
		this.mCurUser = user;
		mPref.edit().putString(USER_NAME, user.getUsername()).commit();
	}

	public String getUsername() {
		return mPref.getString(USER_NAME, "");
	}

	public boolean saveSession(String session) {
		return mPref.edit().putString(AUTH_SESSION, session).commit();
	}

	public String getSession() {
		return mPref.getString(AUTH_SESSION, "");
	}
	
	public long getExpired() {
		return mPref.getLong(EXPIRE, 0);
	}
}
