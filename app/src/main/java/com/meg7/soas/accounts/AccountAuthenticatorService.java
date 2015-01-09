package com.meg7.soas.accounts;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by laaptu on 1/7/15.
 */
public class AccountAuthenticatorService extends Service {
    private static AccountAuthenticator accountAuthenticator;

    /**
     * Account creation and other related Action when called in the app
     * this Service is called. Then this Service, calls relevant AccountAuthenticator
     * as defined onBind
     */

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        IBinder binder = null;
        if (intent.getAction().equals(
                AccountManager.ACTION_AUTHENTICATOR_INTENT))
            binder = getAccountAuthenticator().getIBinder();
        return binder;
    }

    private AccountAuthenticator getAccountAuthenticator() {
        if (accountAuthenticator == null)
            accountAuthenticator = new AccountAuthenticator(this);
        return accountAuthenticator;
    }
}
