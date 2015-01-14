package com.meg7.soas.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.meg7.soas.ui.AccountAuthActivity;

/**
 * @author Santosh Dhakal
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    /**
     * Whenever AddAccount action is called from anywhere, through
     * AccountAuthenticatorService ,this method is invoked, here we need
     * to define the class which handles Login or Sign Up like AuthActivity
     * in our case. AuthActivity is responsible for validating user by communicating
     * through server. AuthActivity must be subclass of AccountAuthenticatorActivity.
     * If you want more control over AccountAuthenticatorActivity, look upon
     * the source code and implement your own Activity class resembling AccountAuthenticatorActivity
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Bundle reply = new Bundle();


        Intent intent = new Intent(mContext, AccountAuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authTokenType);
        if (options.containsKey(AccountAuthActivity.CALL_FROM_ADD_ACCOUNT)) {
            intent.putExtra(AccountAuthActivity.CALL_FROM_ADD_ACCOUNT, true);
        }

        reply.putParcelable(AccountManager.KEY_INTENT, intent);

        return reply;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * Whenever getAuthToken of AccountManager is called, this
     * method is invoked where you have to implement necessary logic to
     * to get Auth Token
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // http://udinic.wordpress.com/2013/04/24/write-your-own-android-authenticator/
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

}
