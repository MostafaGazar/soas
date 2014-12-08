package com.meg7.soas;

import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<SoasApplication> {

    public ApplicationTest() {
        super(SoasApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();
    }

    @SmallTest
    public void testRequestQueuesCreation() throws Exception {
        RequestQueue requestQueue = SoasApplication.getRequestQueue(getContext());
        assertNotNull(requestQueue);

        ImageLoader imageLoader = SoasApplication.getImageLoader(getContext());
        assertNotNull(imageLoader);
    }
}