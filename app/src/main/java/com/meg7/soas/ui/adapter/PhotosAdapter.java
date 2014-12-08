package com.meg7.soas.ui.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meg7.soas.R;
import com.meg7.soas.data.Photo;
import com.meg7.soas.ui.view.PhotoView;

import java.util.List;

public class PhotosAdapter extends BaseEndlessAdapter<Photo> {

    private final int mOrientation;

    private static class ViewHolder {
        public PhotoView photo;
        public TextView owner;
        public TextView uploadDate;
    }

    public PhotosAdapter(Context context, List<Photo> list, int serverListSize) {
        super(context, list, serverListSize);

        mOrientation = context.getResources().getConfiguration().orientation;
    }

    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Reuse views.
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_photo, parent, false);

            // Configure view holder.
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.photo = (PhotoView) view.findViewById(R.id.photo);
            viewHolder.photo.setDefaultImageResId(R.drawable.default_rounded_photo);
            viewHolder.owner = (TextView) view.findViewById(R.id.photoOwner);
            viewHolder.uploadDate = (TextView) view.findViewById(R.id.photoUploadDate);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewHolder.photo.hideName();
                view.findViewById(R.id.photoDetailsContainer).setVisibility(View.GONE);
            }

            view.setTag(viewHolder);
        }

        Photo item = getItem(position);

        // Fill data.
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.photo.setImageUrl(item.photoFileUrl, mImageLoader);
        holder.photo.setText(item.ownerName);

        if (holder.owner != null) {
            holder.owner.setText(Html.fromHtml("<a href=\"" + item.ownerUrl + "\">"
                    + item.ownerName + "</a> "));
            // Making url clickable.
            holder.owner.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (holder.uploadDate != null) { holder.uploadDate.setText(item.uploadDate); }

        return view;
    }

}