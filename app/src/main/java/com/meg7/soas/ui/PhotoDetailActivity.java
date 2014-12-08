package com.meg7.soas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import com.meg7.soas.R;
import com.meg7.soas.data.Photo;
import com.meg7.soas.ui.fragment.PhotoDetailFragment;

import org.parceler.Parcels;

/**
 * An activity representing a single Repo detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PhotosListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link com.meg7.soas.ui.fragment.PhotoDetailFragment}.
 */
// Based on http://www.vogella.com/tutorials/AndroidFragments/article.html
public class PhotoDetailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(PhotoDetailFragment.ARG_PHOTO,
                    getIntent().getParcelableExtra(PhotoDetailFragment.ARG_PHOTO));
            PhotoDetailFragment fragment = new PhotoDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.photoDetailContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PhotosListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(BaseActivity activity, View transitionView, Photo photo) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, PhotoDetailFragment.ARG_PHOTO);
        Intent intent = new Intent(activity, PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailFragment.ARG_PHOTO, Parcels.wrap(photo));
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

}
