package com.meg7.soas.espresso;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.google.android.apps.common.testing.ui.espresso.IdlingResource;
import com.google.common.base.Preconditions;
import com.meg7.soas.SoasApplication;

import java.lang.reflect.Field;
import java.util.Set;

// Based on https://github.com/bolhoso/espresso-volley-tests/blob/master/EspressoTest/src/com/example/espressovolley/test/MyTest.java
public class VolleyIdlingResource implements IdlingResource {

    private final String mResourceName;

    // Write on main thread, read from any thread.
    private volatile ResourceCallback mResourceCallback;

    private Field mCurrentRequests;
    private RequestQueue mVolleyRequestQueue;

    public VolleyIdlingResource(Activity activity, String resourceName) throws SecurityException, NoSuchFieldException {
        mResourceName = Preconditions.checkNotNull(resourceName);

        mVolleyRequestQueue = SoasApplication.getRequestQueue(activity);

        mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
        mCurrentRequests.setAccessible(true);
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        try {
            Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
            if (set != null) {
                int count = set.size();
                if (count == 0) {
                    VolleyLog.d("Volley is idle! With count :: " + count);
                    mResourceCallback.onTransitionToIdle();
                } else {
                    VolleyLog.d("Volley is Not idle! With count :: " + count);
                }

                return count == 0;
            }
        } catch (Exception e) {
            VolleyLog.e(e.getMessage());
        }

        VolleyLog.d("Something went wrong!");
        return true;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

}