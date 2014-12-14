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

package com.meg7.soas.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.meg7.soas.R;
import com.meg7.soas.ui.widget.RoundedFadeInNetworkImageView;

/**
 * Custom view for showing RoundedNetworkView and TextView on top of it with Palette used to
 * generate the TextView background and foreground colors.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class PhotoView extends FrameLayout implements RoundedFadeInNetworkImageView.OnBitmapChangeListener {

    @SuppressWarnings("unused")
    private Context mContext;

    private RoundedFadeInNetworkImageView mPhoto;
    private TextView mPhotoName;

    public PhotoView(Context context) {
        super(context);

        sharedConstructor(context, null);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        sharedConstructor(context, attrs);
    }

    public PhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        sharedConstructor(context, attrs);
    }

    private void sharedConstructor(Context context, AttributeSet attrs) {
        mContext = context;

        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_photo, this, true);

        mPhoto = (RoundedFadeInNetworkImageView) getChildAt(0);
        mPhoto.setOnBitmapChangeListener(this);
        mPhotoName = (TextView) getChildAt(1);
    }

    /**
     * Generate label background and foreground colors using Palette base on downloaded image colors.
     *
     * @param bitmap Download bitmap.
     */
    @Override
    public void onBitmapChange(Bitmap bitmap) {
        if (bitmap == null) { return; }

        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @SuppressWarnings("deprecation")
            public void onGenerated(Palette palette) {
                Resources res = getResources();
                int photoNameColorBg = palette.getDarkMutedColor(res.getColor(R.color.list_item_photo_name_bg));
                int photoNameColorFg = palette.getLightMutedColor(res.getColor(R.color.view_photo_name_fg));

                ColorFilter photoNameColorFilter = new LightingColorFilter(photoNameColorBg, 1);
                Drawable photoNameDrawableBg = res.getDrawable(R.drawable.view_photo_name_bg);
                photoNameDrawableBg.setColorFilter(photoNameColorFilter);
                mPhotoName.setBackgroundDrawable(photoNameDrawableBg);

                mPhotoName.setTextColor(photoNameColorFg);
            }
        });
    }

    public void hideName() {
        mPhotoName.setVisibility(GONE);
    }

    /**
     * Use default photo name background and foreground colors.
     */
    public void resetColors() {
        // Set default backgrounds without using Palette.
        mPhotoName.setBackgroundResource(R.drawable.view_photo_name_bg);
        mPhotoName.setTextColor(getResources().getColor(R.color.view_photo_name_fg));
    }

    /**
     * Set image url to be requested. Consider calling {@code resetColors()} first in case view was
     * used in a {@link android.widget.ListView}.
     *
     * @param url Image url.
     * @param imageLoader Image loader queue.
     */
    public void setImageUrl(String url, ImageLoader imageLoader) {
        mPhoto.setImageUrl(url, imageLoader);
    }

    public void setDefaultImageResId(int defaultImage) {
        mPhoto.setDefaultImageResId(defaultImage);
    }

    public void setErrorImageResId(int errorImage) {
        mPhoto.setErrorImageResId(errorImage);
    }

    public void setText(CharSequence text) {
        mPhotoName.setText(text);
    }

}
