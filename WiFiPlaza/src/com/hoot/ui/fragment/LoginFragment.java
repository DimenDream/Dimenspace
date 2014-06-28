package com.hoot.ui.fragment;

import org.apache.http.HttpStatus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hoot.manager.AuthManager;
import com.hoot.module.RequestInfo;
import com.hoot.module.RequestInfo.Api;
import com.hoot.module.UserEngine;
import com.hoot.pojo.Response;
import com.hoot.pojo.User;
import com.hoot.pojo.UserResponse;
import com.hoot.ui.fragment.AppConstants.AuthConst;
import com.hoot.ui.fragment.AppConstants.InentConst;
import com.hoot.util.XLog;
import com.hoot.wifiplaza.R;
import com.umeng.analytics.MobclickAgent;

public class LoginFragment extends BaseFragment {
	private static final String TAG = "LoginActivity";

	private boolean isCreateUser;

	private String mAccount;
	private String mPassword;

	// UI references.
	private EditText mAccountView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Button mSubmitBtn;
	private TextView mSwitchFuncView;
	
	private final String mPageName = "LoginFragmentPage";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, null);

		// Set up the login form.
		mAccountView = (EditText) rootView.findViewById(R.id.email);
		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = rootView.findViewById(R.id.login_form);
		mLoginStatusView = rootView.findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) rootView
				.findViewById(R.id.login_status_message);

		mSubmitBtn = (Button) rootView.findViewById(R.id.sign_in_button);
		mSubmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		mSwitchFuncView = (TextView) rootView.findViewById(R.id.switchLink);
		mSwitchFuncView.setText(Html
				.fromHtml(getString(R.string.regist_user_link)));
		mSwitchFuncView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickDelegate(v);
			}
		});
		return rootView;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		Object tag = mSwitchFuncView.getTag();
		isCreateUser = tag == null ? false : (Boolean) tag;
		// Reset errors.
		mAccountView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mAccount = mAccountView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(
					getString(R.string.error_field_required, getString(R.string.regist_passwd)));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mAccount)) {
			mAccountView.setError(getString(R.string.error_field_required, getString(R.string.regist_account_phone)));
			focusView = mAccountView;
			cancel = true;
		} else if (/*!mAccount.contains("@")*/mAccount.trim().length() != 11) {
			mAccountView.setError(getString(R.string.error_invalid_email));
			focusView = mAccountView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			UserEngine.getInstance().register(this);
			RequestInfo info = getRequestInfo();
			UserEngine.getInstance().send(info);
		}
	}

	private RequestInfo getRequestInfo() {
		RequestInfo info = new RequestInfo();
		User user = new com.hoot.pojo.User();
		user.setUsername(mAccount);
		user.setPasswd(mPassword);
		info.reqest = user;
		info.api = isCreateUser ? Api.createUser : Api.AUTH;
		return info;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	public void onDataLoaded(int seq, int code, Response data) {
		if (code == HttpStatus.SC_OK && data != null) {
			showProgress(false);

			UserResponse response = (UserResponse) data;
			if (response.getHead() != null) {
				int returnCode = response.getHead().getCode();
				if (returnCode == AuthConst.ERROR_AUTH_PASSWD) {
					// sign in password error
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				} else if (returnCode == AuthConst.ERROR_USER_EXIST) {
					// register tip
					mAccountView
							.setError(getString(R.string.error_email_exist));
					mAccountView.requestFocus();
				} else if (returnCode == AuthConst.AUTH_OK
						|| returnCode == AuthConst.REGISTER_OK) {
					String hint;
					if (isCreateUser) {
						hint = "注册成功";
					} else {
						hint = "登录成功";
					}
					Toast.makeText(getActivity(), hint, Toast.LENGTH_SHORT)
							.show();
					AuthManager.getInstance()
							.saveSession(response.getSession());
					AuthManager.getInstance().setAuthUser(
							((UserResponse) data).getUser());
					backToPrePage((UserResponse) data);
				} else {
					XLog.v(TAG, "unkown");
				}
			}

		} else {
			showProgress(false);
			mPasswordView
					.setError(getString(R.string.error_incorrect_password));
			mPasswordView.requestFocus();
		}
	}

	private void backToPrePage(UserResponse data) {
		Intent intent = getActivity().getIntent();
		intent.putExtra(InentConst.USER_EXTRA, data.getUser());
		AuthManager.getInstance().setAuthUser(data.getUser());
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}

	private void onClickDelegate(View v) {
		XLog.v(TAG, "login frag click me");
		Object tag = mSwitchFuncView.getTag();
		isCreateUser = tag == null ? false : (Boolean) tag;
		if (isCreateUser) {
			mSwitchFuncView.setText(Html
					.fromHtml(getString(R.string.regist_user_link)));
			mSubmitBtn.setText(R.string.sign_in_btn_text);
			isCreateUser = false;
		} else {
			isCreateUser = true;

			mSwitchFuncView.setText(Html
					.fromHtml(getString(R.string.sign_in_link)));
			mSubmitBtn.setText(R.string.regist_user_btn_text);
		}
		mSwitchFuncView.setTag(isCreateUser);
	}
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart(mPageName); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(mPageName);  // 统计页面结束
	}
}
