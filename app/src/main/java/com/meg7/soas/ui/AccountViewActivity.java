package com.meg7.soas.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.meg7.soas.R;

/**
 * @author Santosh Dhakal
 */
public class AccountViewActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_account_view;
    }

}
