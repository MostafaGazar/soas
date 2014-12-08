package com.meg7.soas.unit;

import android.app.Activity;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.meg7.soas.ui.PhotosListActivity;

public class PhotosListActivityUnitTest extends ActivityUnitTestCase<PhotosListActivity> {

    private Activity mActivity;

    public PhotosListActivityUnitTest() {
        super(PhotosListActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
    }

    @SmallTest
    public void testFoo() {
        assertEquals(1, 1);
    }

}