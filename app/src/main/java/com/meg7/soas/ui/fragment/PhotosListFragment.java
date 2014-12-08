package com.meg7.soas.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.meg7.soas.R;
import com.meg7.soas.data.Photo;
import com.meg7.soas.data.Photos;
import com.meg7.soas.ui.adapter.BaseEndlessScrollListener;
import com.meg7.soas.ui.adapter.PhotosAdapter;

import org.apache.http.HttpStatus;

/**
 * A list fragment representing a list of Photos. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link PhotoDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class PhotosListFragment extends SwipeRefreshListFragment {

    public static final String TAG = PhotosListFragment.class.getSimpleName();

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public static interface Callbacks {

        /**
         * Force refresh, reload first page.
         */
        public void refresh();

        /**
         * Callback for when next page can be loaded.
         *
         * @return Returns true if there are more pages to be loaded.
         */
        public void requestNextPage();

        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(View view, Photo photo);

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PhotosListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set divider to nothing but keep its height.
        getListView().setDividerHeight(0);

        // Set empty view.
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view, null);
        getListView().setEmptyView(emptyView);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    /**
     * Create new or or append to {@link PhotosAdapter} newly requested photos. It also shows retry
     * layout in case of failure.
     *
     * @param statusCode Response code {@link HttpStatus}
     * @param photos Photos to add or append to the adapter.
     */
    public void setPhotos(int statusCode, Photos photos) {
        if (statusCode == HttpStatus.SC_OK && photos != null) {
            PhotosAdapter adapter = (PhotosAdapter) getListAdapter();

            if (adapter == null) {
                adapter = new PhotosAdapter(getActivity(), photos.photos, photos.count);
                setListAdapter(adapter);

                // Attach listener to handle "swipe to refresh" gesture, SwipeRefreshLayout invokes
                // onRefresh method, same method should be called in response to the Refresh action
                // from the action bar.
                setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Execute the background task to refresh page.
                        mCallbacks.refresh();
                    }
                });

                // Attach listener to handle requesting new pages.
                getListView().setOnScrollListener(new BaseEndlessScrollListener() {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to your AdapterView
                        mCallbacks.requestNextPage();
                    }
                });
            } else {
                adapter.clear();
                adapter.addAll(photos.photos);

                // Stop the refreshing indicator.
                setRefreshing(false);
            }
        } else {
            // TODO :: Show retry layout.
            Toast.makeText(getActivity(), "Oops! " + statusCode, Toast.LENGTH_SHORT).show();

            // Stop the refreshing indicator.
            setRefreshing(false);
        }
    }

    /**
     * Append to {@link PhotosAdapter} newly requested photos. It could also show an indication of failure.
     *
     * @param statusCode Response code {@link HttpStatus}
     * @param photos Photos to append to the adapter.
     */
    public void appendPhotos(int statusCode, Photos photos) {
        if (statusCode == HttpStatus.SC_OK && photos != null) {
            PhotosAdapter adapter = (PhotosAdapter) getListAdapter();
            // Adapter should not be NULL at this point.
            adapter.addAll(photos.photos);
        } else {
            // TODO :: Update error message!
            Toast.makeText(getActivity(), "Oops! Could not load next page, " + statusCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(view, ((PhotosAdapter) getListAdapter()).getItem(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

}
