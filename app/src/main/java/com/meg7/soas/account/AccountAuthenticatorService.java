package com.meg7.soas.account;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Santosh Dhakal
 */
public class AccountAuthenticatorService extends Service {

    private static AccountAuthenticator sAccountAuthenticator;

    /**
     * Account creation and other related Action when called in the app
     * this Service is called. Then this Service, calls relevant AccountAuthenticator
     * as defined onBind
     */
    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = null;
        if (intent.getAction().equals(AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
            binder = getAccountAuthenticator().getIBinder();
        }

        return binder;
    }

    private AccountAuthenticator getAccountAuthenticator() {
        if (sAccountAuthenticator == null) {
            sAccountAuthenticator = new AccountAuthenticator(this);
        }

        return sAccountAuthenticator;
    }

}
