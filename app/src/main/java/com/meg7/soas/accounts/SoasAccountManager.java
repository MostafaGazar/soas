package com.meg7.soas.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laaptu on 1/7/15.
 */
public class SoasAccountManager {

    /**
     * Keep in mind ACCOUNT_TYPE value declared here
     * must be same  as xml/account_authenticator android:accountType
     * It is not necessary that you give your Application package name as done in here,
     * you can give your own AccountType
     * but keep in mind it should be same value defined on xml/account_authenticator
     */
    public static final String ACCOUNT_TYPE = "com.meg7.soas";
    /**
     * Your own Account Auth Type like OAuth,OAuth2.0 and so on
     */
    public static final String ACCOUNT_AUTH_TYPE = "soas_auth";
    public static final String USER_EMAIL = "email";
    public static final String USER_NAME = "name";
    public static final String USER_UNAME = "uname";
    public static final String USER_AUTH = "authToken";

    private final AccountManager mAccountManager;

    public SoasAccountManager(AccountManager accountManager) {
        if (accountManager == null)
            throw new NullPointerException("AccountManager cannot be null");
        mAccountManager = accountManager;
    }

    /**
     * Listing all the accounts by our Account type
     * ie. ACCOUNT_TYPE="com.meg7.soas
     * right now we are only taking a single account,but
     * multiple accounts can be created or added
     * as per the need of the app
     */
    public List<Account> getAccounts() {
        final Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        ArrayList<Account> soasAccounts = new ArrayList<>(accounts.length);

        for (int i = 0; i < accounts.length; i++) {
            final Account account = accounts[i];
            soasAccounts.add(account);
        }

        return soasAccounts;
    }

    /**
     * Method to create a new Account
     */
    public void addAccount(User user) {

        Account account = new Account(user.email, ACCOUNT_TYPE);
        Bundle userData = new Bundle();
        userData.putString(USER_UNAME, user.uname);
        userData.putString(USER_EMAIL, user.email);
        userData.putString(USER_NAME, user.name);
        userData.putString(USER_AUTH, user.authToken);
        mAccountManager.addAccountExplicitly(account, "Add Password If you Like", userData);
        /**
         * This is a crucial step. If you just call above statement and leave as it
         * is, AccountAuthenticator getAuthToken will be called
         * and there you have to have a proper mechanism to get Auth Token from server
         * Right now we are not using it. Instead we are directly
         * setting the auth token here
         * As per documentation, it says that it caches token here and can be
         * retrieved mAccountManager.peekAuthToken(account, ACCOUNT_AUTH_TYPE);
         * but , while calling mAccountManager.peekAuthToken(account, ACCOUNT_AUTH_TYPE);
         * there isn't any cached token
         * And lot of other implementation have saved password and accessed
         * password from AccountAuthenticator getAuthToken...
         * So for right now , we are saving authtoken on user data
         * above*/
        mAccountManager.setAuthToken(account, user.authToken,
                ACCOUNT_AUTH_TYPE);

    }

    /**
     * Removing all the accounts of ACCOUNT_TYPE
     */
    public void removeAllAccounts() {

        final List<Account> availableAccounts = getAccounts();
        for (Account account : availableAccounts) {
            mAccountManager.removeAccount(account, null, null);

        }
    }

    /**
     * Method to retrieve information
     * saved on Account if available
     */
    public User getCurrentUser() {
        User user = null;
        String userInfo = null;
        final List<Account> availableAccounts = getAccounts();
        if (availableAccounts.size() > 0) {
            /**
             * Right now we are only using a single account i.e. only one user per app
             *
             */
            System.out.println("Account has some length");
            Account account = availableAccounts.get(0);
            user = new User();
            user.email = mAccountManager.getUserData(account, USER_EMAIL);
            user.name = mAccountManager.getUserData(account, USER_NAME);
            user.uname = mAccountManager.getUserData(account, USER_UNAME);
            user.authToken = mAccountManager.getUserData(account, USER_AUTH);
        }
        return user;
    }
}
