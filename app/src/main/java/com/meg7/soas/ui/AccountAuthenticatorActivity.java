package com.meg7.soas.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.meg7.soas.R;
import com.meg7.soas.account.SoasAccountManager;
import com.meg7.soas.data.User;

/**
 * @author Santosh Dhakal
 */
public class AccountAuthenticatorActivity extends BaseAccountAuthenticatorActivity {

    public static final String EXTRA_IS_CALL_FROM_ADD_ACCOUNT = "isCallFromAddButton";

    /**
     * Here you call your server make authentication and receive authentication token
     * or any other form of server calls for user authentication.
     * Here We are just assuming that server calls are made and
     * we have received valid auth token and key.
     */
    private final String mAuthToken = "bad18eba1ff45jk7858b8ae88a77fa30";

    private SoasAccountManager mAccountManager;
    private boolean mIsCalledFromAddAccount;

    private EditText mNameEdtTxt;
    private EditText mEmailEdtTxt;
    private EditText mPasswordEdtTxt;
    private EditText mUsernameEdtTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountManager = new SoasAccountManager(AccountManager.get(this));

        /**
         * Just a simple variable to know whether this
         * activity is called from AccountViewActivity or Fragment
         * so that we won't navigate to HomeDisplay after login i.e
         * we just simply finish this activity.
         */
        mIsCalledFromAddAccount = getIntent().hasExtra(EXTRA_IS_CALL_FROM_ADD_ACCOUNT);
        if (isUserLoggedIn(this)) {
            showSuccessLogin();
            return;
        }

        initViews();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_account_authenticator;
    }

    private void initViews() {
        mNameEdtTxt = (EditText) findViewById(R.id.txt_name);
        mEmailEdtTxt = (EditText) findViewById(R.id.txt_email);
        mUsernameEdtTxt = (EditText) findViewById(R.id.txt_uname);
        mPasswordEdtTxt = (EditText) findViewById(R.id.txt_pwd);
    }

    /**
     * Validate and submit inputs from Sign In or Sign Up form.
     */
    public void validateAndSubmit(View view) {
        if (TextUtils.isEmpty(mNameEdtTxt.getText().toString())) {
            setError(mNameEdtTxt, "Name can't be empty.");
            return;
        }

        if (TextUtils.isEmpty(mUsernameEdtTxt.getText().toString())) {
            setError(mUsernameEdtTxt, "Username can't be empty.");
            return;
        }

        if (TextUtils.isEmpty(mPasswordEdtTxt.getText().toString())) {
            setError(mPasswordEdtTxt, "Password can't be empty.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEdtTxt.getText().toString()).matches()) {
            setError(mEmailEdtTxt, "Invalid email.");
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                authenticationSuccess(mAuthToken);
            }
        });
    }

    /**
     * This method is called when there is login or sign up
     * success and user has the necessary authToken
     * which needs to be stored.
     */
    private void authenticationSuccess(String authToken) {
        User user = new User();
        user.username = mUsernameEdtTxt.getText().toString();
        user.name = mNameEdtTxt.getText().toString();
        user.email = mEmailEdtTxt.getText().toString();
        user.authToken = authToken;

        // Creating an account using Android Account Manager.
        mAccountManager.addAccount(user);

        /**
         * This method is crucial as it will notify the calling intent that
         * account creation has been successful and passes AUTH_TOKEN on Bundle which can
         * be received by calling Intent.
         */
        Bundle callbackResult = new Bundle();
        callbackResult.putString(AccountManager.KEY_ACCOUNT_NAME, user.email);
        callbackResult.putString(AccountManager.KEY_ACCOUNT_TYPE, SoasAccountManager.ACCOUNT_TYPE);
        callbackResult.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        setAccountAuthenticatorResult(callbackResult);

        showSuccessLogin();
    }

    private void setError(EditText view, String errorMsg) {
        view.requestFocus();

        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessLogin() {
        if (!mIsCalledFromAddAccount) {
            Intent intent = new Intent(this, PhotosListActivity.class);
            startActivity(intent);
        }

        Toast.makeText(this, "You are logged in", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * We normally store info on SharedPreferences
     * and on app update, there is chance that your shared preferences
     * get cleared. So when the shared preferences get cleared
     * user has to login again. It is a hassle. So, here first we check
     * on shared preferences, for stored data.
     * If not found on shared preferences, we check Account under
     * AccountManager and if there isn't any stored account , then
     * we are sure that user hasn't logged in.
     */
    public static boolean isUserLoggedIn(Context context) {
        User user = new SoasAccountManager(AccountManager.get(context)).getCurrentUser();

        return user != null;
    }


}
