package com.meg7.soas.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.meg7.soas.R;
import com.meg7.soas.SoasApplication;

import java.util.Collection;
import java.util.List;

/**
 * A child class shall subclass this Adapter and
 * implement method getDataRow(int position, View convertView, ViewGroup parent),
 * which supplies a View present data in a ListRow.
 * <p/>
 * This parent Adapter takes care of displaying ProgressBar in a row or
 * indicating that it has reached the last row.
 */
// Based on https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
public abstract class BaseEndlessAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected final LayoutInflater mInflater;
    protected final ImageLoader mImageLoader;

    /**
     * The main data list to save loaded data.
     */
    protected List<T> mItems;

    /**
     * Lock used to modify the content of {@link #mItems}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();

    // The serverListSize is the total number of items on the server side,
    // which should be returned from the web request results.
    protected int mServerListSize = -1;

    // Two view types which will be used to determine whether a row should be displaying 
    // data or a Progressbar.
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;

    public BaseEndlessAdapter(Context context, List<T> list, int serverListSize) {
        mContext = context;
        mItems = list;
        mServerListSize = serverListSize;

        mInflater = LayoutInflater.from(context);
        mImageLoader = SoasApplication.getImageLoader(context);
    }

    public void setServerListSize(int serverListSize) {
        mServerListSize = serverListSize;
    }

    /**
     * Disable click events on indicating rows.
     *
     * @param position Index of the item.
     * @return True if the item is not a separator.
     */
    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    /**
     * One type is normal data row, the other type is ProgressBar.
     *
     * @return The number of types of Views that will be created by this adapter.
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * The size of the List plus one, the one is the last row, which displays a Progressbar.
     *
     * @return The size of the List plus one for the ProgressBar.
     */
    @Override
    public int getCount() {
        return mItems.size() + 1;
    }

    /**
     * Return the type of the row,
     * the last row indicates the user that the ListView is loading more data.
     *
     * @return Row type.
     */
    @Override
    public int getItemViewType(int position) {
        return (position >= mItems.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public T getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mItems
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position : -1;
    }

    /**
     * Returns the correct view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, convertView, parent);
        }
        View dataRow = convertView;
        dataRow = getDataRow(position, convertView, parent);

        return dataRow;
    }

    /**
     * A subclass should override this method to supply the data row.
     */
    public abstract View getDataRow(int position, View convertView, ViewGroup parent);

    /**
     * Returns a View to be displayed in the last row.
     */
    public View getFooterView(int position, View convertView, ViewGroup parent) {
        if (position >= mServerListSize && mServerListSize > 0) {
            // The ListView has reached the last row.
            TextView tvLastRow = new TextView(mContext);
            tvLastRow.setHint(R.string.list_last_row_reached);
            tvLastRow.setGravity(Gravity.CENTER);
            return tvLastRow;
        }

        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(
                    R.layout.list_footer_progress, parent, false);
        }

        return row;
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            mItems.addAll(collection);
        }

        notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            mItems.clear();
        }

        notifyDataSetChanged();
    }

}