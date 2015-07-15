package com.app.avanstart;

import java.util.Locale;

import com.app.avanstart.util.AppUtils;
import com.app.beans.User;
import com.app.modals.DataOperations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
		"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	//private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.

	// UI references.
	private EditText mEmailField;
	private EditText mPasswordField;
	private EditText mNameField;
	private EditText mPhoneField;
	private EditText mIdField;
	private EditText mAddressField;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	AlertDialog alert;

	/// temp
	Locale myLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);


		/// changing styles
		//pushLanguageScreen(); commenting for temp reason
		User u = (User)DataOperations.getDataFromFile(AppUtils.USER_FILE_NAME, this);
		if(u != null) {
			String mob = u.getMobile();
			if(mob.length() == 10){
				//AppUtils.phoneNum = mob;
				showConfigScreen();
				finish();
			}
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getActionBar().setIcon(R.drawable.avaniheaderlogo);
		getActionBar().setTitle(R.string.logintitle);
		mNameField = (EditText)findViewById(R.id.name);
		mPhoneField = (EditText)findViewById(R.id.mobile);
		mIdField = (EditText)findViewById(R.id.productid);
		mAddressField = (EditText)findViewById(R.id.address);

		// Set up the login form.
		//		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		//		mEmailView = (EditText) findViewById(R.id.email);
		//		mEmailView.setText(mEmail);
		//		mEmailView.setHint(R.string.emailfield);
		//
		//		mPasswordView = (EditText) findViewById(R.id.password);
		//		mPasswordView
		//				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		//					@Override
		//					public boolean onEditorAction(TextView textView, int id,
		//							KeyEvent keyEvent) {
		//						if (id == R.id.login || id == EditorInfo.IME_NULL) {
		//							attemptLogin();
		//							return true;
		//						}
		//						return false;
		//					}
		//				});
		//		mPasswordView.setHint(R.string.passwordfield);
		//
		//		mLoginFormView = findViewById(R.id.login_form);
		//		mLoginStatusView = findViewById(R.id.login_status);
		//		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		Button signinbtn = (Button)findViewById(R.id.sign_in_button);
		signinbtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//attemptLogin();
						//showConfigScreen();
						checkLoginClicked();
					}
				});

		signinbtn.setText(R.string.loginbutton);
	}
	
	public void checkLoginClicked() {
		
		User u = new User();
		
		if(mNameField.getText().length() > 0) {
			u.setName(mNameField.getText().toString());
		}
		if(mIdField.getText().length() > 0) {
			u.setId(mIdField.getText().toString());
		}
		if(mAddressField.getText().length() > 0) {
			u.setAddress(mAddressField.getText().toString());
		}
		if(mPhoneField.getText().length() == 10 ) {
			u.setMobile(mPhoneField.getText().toString());
			AppUtils.phoneNum = u.getMobile();
			DataOperations.saveDataToFile(u, AppUtils.USER_FILE_NAME, this);
			showConfigScreen();
			finish();
		} else {
			showDialog("Login", "Ten Digit Mobile Number is Mandatory");
		}
	}
	
	public void showDialog( String titleName , String message ) {

		if(alert == null){

			alert = new AlertDialog.Builder(this).create();
			alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		}

		alert.setTitle(titleName);
		alert.setMessage(message);
		alert.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		this.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		pushLanguageScreen();
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */


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

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	//	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
	//		@Override
	//		protected Boolean doInBackground(Void... params) {
	//			// TODO: attempt authentication against a network service.
	//
	//			try {
	//				// Simulate network access.
	//				Thread.sleep(2000);
	//			} catch (InterruptedException e) {
	//				return false;
	//			}
	//
	//			for (String credential : DUMMY_CREDENTIALS) {
	//				String[] pieces = credential.split(":");
	//				if (pieces[0].equals(mEmail)) {
	//					// Account exists, return true if the password matches.
	//					return pieces[1].equals(mPassword);
	//				}
	//			}
	//
	//			// TODO: register the new account here.
	//			return true;
	//		}
	//
	//		@Override
	//		protected void onPostExecute(final Boolean success) {
	//			mAuthTask = null;
	//			showProgress(false);
	//
	//			if (success) {
	//				
	//				showConfigScreen();
	//				finish();
	//			} else {
	//				mPasswordView
	//						.setError(getString(R.string.error_incorrect_password));
	//				mPasswordView.requestFocus();
	//			}
	//		}
	//
	//		@Override
	//		protected void onCancelled() {
	//			mAuthTask = null;
	//			showProgress(false);
	//		}
	//	}
	private void pushLanguageScreen() {

		Intent i = new Intent(this , LanguageSelectionActivity.class);
		startActivityForResult(i, 400);

	}
	//// temporary , we will remove this later
	public void setLocale(String lang) {

		myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
	}

	private void showConfigScreen() {

		//Intent i = new Intent(this , CropSelectionActivity.class);
		//Intent i = new Intent(this , ElementsConfigurationActivity.class);

		//Intent i = new Intent(this , ConfigurationsList.class);
		Intent i = new Intent(this , DashBoardActivity.class);
		startActivity(i);

	}
}
