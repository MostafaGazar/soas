package com.meg7.soas.accounts;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by laaptu on 1/7/15.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private Context context;


    public AccountAuthenticator(Context context) {
        super(context);
        this.context = context;
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


        Intent intent = new Intent(context, AuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                response);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authTokenType);
        if (options.containsKey(AuthActivity.CALL_FROM_ADD_ACCOUNT))
            intent.putExtra(AuthActivity.CALL_FROM_ADD_ACCOUNT, true);

        reply.putParcelable(AccountManager.KEY_INTENT, intent);
        System.out.println("AddAccount");
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
//  http://udinic.wordpress.com/2013/04/24/write-your-own-android-authenticator/
//        final AccountManager am = AccountManager.get(context);
//        String authToken = am.peekAuthToken(account, authTokenType);
//
//        // Lets give another try to authenticate the user
//        if (null != authToken) {
//            if (authToken.isEmpty()) {
//                final String password = am.getPassword(account);
//                if (password != null) {
//                    // if we have user name,password and so on you can call your server action
//
//                }
//            }
//        }
//        //authToken = "This is authToken";
//        // If we get an authToken - we return it
//        if (null != authToken) {
//            if (!authToken.isEmpty()) {
//                final Bundle result = new Bundle();
//                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
//                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
//                return result;
//            }
//        }
//
//        // If we get here, then we couldn't access the user's password - so we
//        // need to re-prompt them for their credentials. We do that by creating
//        // an intent to display our AuthenticatorActivity.
//        Bundle reply = new Bundle();
//
//
//        Intent intent = new Intent(context, AuthActivity.class);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
//                response);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
//        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authTokenType);
//
//        reply.putParcelable(AccountManager.KEY_INTENT, intent);
//        return reply;
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
