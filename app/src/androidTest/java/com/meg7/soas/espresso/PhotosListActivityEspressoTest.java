package com.meg7.soas.espresso;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.contrib.DrawerActions;
import com.google.android.apps.common.testing.ui.espresso.contrib.DrawerMatchers;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;
import com.meg7.soas.R;
import com.meg7.soas.ui.PhotosListActivity;

public class PhotosListActivityEspressoTest extends ActivityInstrumentationTestCase2<PhotosListActivity> {

    private Activity mActivity;

    public PhotosListActivityEspressoTest() {
        super(PhotosListActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
    }

    @MediumTest
    public void testLoadingPhotos() throws InterruptedException {
        VolleyIdlingResource volleyResources;
        try {
            volleyResources = new VolleyIdlingResource(mActivity, "VolleyCalls");
            Espresso.registerIdlingResources(volleyResources);
        } catch (SecurityException | NoSuchFieldException e) {
            fail(e.getMessage());
        }

        Espresso.onView(ViewMatchers.withId(android.R.id.list))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @MediumTest
    public void testOnPhotoItemClick() {
//        Espresso.onData(LongListMatchers.withItemContent("item: 5"))
//                .perform(ViewActions.click());
    }

    @MediumTest
    public void testOpenAndCloseDrawer() {
        // Drawer should not be open to start.
        Espresso.onView(ViewMatchers.withId(R.id.drawer)).check(ViewAssertions.matches(DrawerMatchers.isClosed()));

        DrawerActions.openDrawer(R.id.drawer);

        // The drawer should now be open.
        Espresso.onView(ViewMatchers.withId(R.id.drawer)).check(ViewAssertions.matches(DrawerMatchers.isOpen()));

        DrawerActions.closeDrawer(R.id.drawer);

        // Drawer should be closed again.
        Espresso.onView(ViewMatchers.withId(R.id.drawer)).check(ViewAssertions.matches(DrawerMatchers.isClosed()));
    }

}