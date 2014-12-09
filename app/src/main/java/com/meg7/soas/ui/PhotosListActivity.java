package com.meg7.soas.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.meg7.soas.R;
import com.meg7.soas.data.Photo;
import com.meg7.soas.data.Photos;
import com.meg7.soas.ui.fragment.PhotoDetailFragment;
import com.meg7.soas.ui.fragment.PhotosListFragment;
import com.meg7.soas.ui.fragment.retained.PhotosListTaskFragment;

import org.parceler.Parcels;

/**
 * An activity representing a list of Repos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PhotoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.meg7.soas.ui.fragment.PhotosListFragment} and the item details
 * (if present) is a {@link com.meg7.soas.ui.fragment.PhotoDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link com.meg7.soas.ui.fragment.PhotosListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class PhotosListActivity extends BaseActivity
        implements PhotosListFragment.Callbacks, PhotosListTaskFragment.TaskCallbacks {

    private static final String TAG_TASK_FRAGMENT_PHOTOS_LIST = "photosListTaskFragment";

    private ActionBarDrawerToggle mDrawerToggle;

    private FragmentManager mFragmentManager;

    private PhotosListTaskFragment mPhotosListTaskFragment;
    private PhotosListFragment mPhotosListFragment;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mIsTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerLayout.setStatusBarBackground(R.color.material_color_primary_dark);

        // Tie the DrawerLayout with the Toolbar.
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                getToolbar(), R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);

        mFragmentManager = getFragmentManager();

        mPhotosListFragment = (PhotosListFragment) mFragmentManager.findFragmentById(R.id.photosList);

        if (findViewById(R.id.photoDetailContainer) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mIsTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            mPhotosListFragment.setActivateOnItemClick(true);
        }

        // Clear Back Stack on recreate.
        // mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // Or do nothing and make onBackPressed is handled normally.
        if (mIsTwoPane && mFragmentManager.getBackStackEntryCount() > 0) {
            findViewById(R.id.photosNoItemSelected).setVisibility(View.GONE);
        }

        // Initiate tasks.
        mPhotosListTaskFragment = (PhotosListTaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT_PHOTOS_LIST);
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mPhotosListTaskFragment == null) {
            mPhotosListTaskFragment = new PhotosListTaskFragment();
            mFragmentManager.beginTransaction().
                    add(mPhotosListTaskFragment, TAG_TASK_FRAGMENT_PHOTOS_LIST).commit();
        } else {
            mPhotosListFragment.setPhotos(
                    mPhotosListTaskFragment.getLastRequestedPhotosStatusCode(),
                    mPhotosListTaskFragment.getLastRequestedPhotos());
        }

        // TODO: If exposing deep links into your app, handle intents here.

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_photos_list;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refresh() {
        mPhotosListTaskFragment.refresh();
    }

    @Override
    public void requestNextPage() {
        mPhotosListTaskFragment.requestNextPage();
    }

    /**
     * Callback method from {@link com.meg7.soas.ui.fragment.PhotosListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(View view, Photo photo) {
        if (mIsTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(PhotoDetailFragment.ARG_PHOTO, Parcels.wrap(photo));
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            fragment.setArguments(arguments);
            mFragmentManager.beginTransaction()
                    .replace(R.id.photoDetailContainer, fragment)
                    // Add this transaction to the back stack.
                    .addToBackStack(photo.photoTitle)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            findViewById(R.id.photosNoItemSelected).setVisibility(View.GONE);
            mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (mFragmentManager.getBackStackEntryCount() == 0) {
                        findViewById(R.id.photosNoItemSelected).setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.

            // Start it using ActivityCompat.startActivity
            PhotoDetailActivity.launch(PhotosListActivity.this, view, photo);

            // Or

            // Open DetailsActivity with default transition animation.
            // Intent detailIntent = new Intent(this, RepoDetailActivity.class);
            // detailIntent.putExtra(RepoDetailFragment.ARG_PHOTO, photo);
            // startActivity(detailIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsTwoPane && mFragmentManager.getBackStackEntryCount() > 0){
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResponse(PhotosListTaskFragment.Task task, int statusCode, Photos photos) {
        switch (task) {
            case REFRESH:
                mPhotosListFragment.setPhotos(statusCode, photos);
                break;
            case REQUEST_NEXT:
                mPhotosListFragment.appendPhotos(statusCode, photos);
                break;
        }
    }

}
