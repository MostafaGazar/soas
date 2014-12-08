/*
 * Copyright 2014 Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meg7.soas.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.meg7.soas.R;

/**
 * Base Activity for showing Toolbar instead of ActionBar.
 * <p/>
 * A Child activity must have Toolbar in its xml layout.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public abstract class BaseActivity extends ActionBarActivity {

    /**
     * Lollipop new Toolbar.
     */
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Get child Activity layout resource id.
     *
     * @return layout resource id.
     */
    protected abstract int getLayoutResource();

    @Deprecated
    protected void setActionBarIcon(int iconRes) {
        mToolbar.setNavigationIcon(iconRes);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
