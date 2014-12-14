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

package com.meg7.soas.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.meg7.soas.R;

/**
 * Rounded corners fade-in NetworkImageView that maintains set scaleType.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class RoundedFadeInNetworkImageView extends BaseFadeInNetworkImageView {

    protected Context mContext;

    /**
     * Notify registered view on Bitmap change events.
     */
    private OnBitmapChangeListener mOnBitmapChangeListener;

    /**
     * Corner radius read from view xml attrs, 0 if not set.
     */
    private float mCornersRadius;

    public static interface OnBitmapChangeListener {

        /**
         * Notify registered view on Bitmap change events.
         *
         * @param bitmap New Bitmap.
         */
        void onBitmapChange(Bitmap bitmap);

    }

    public RoundedFadeInNetworkImageView(Context context) {
        super(context);

        sharedConstructor(context, null);
    }

    public RoundedFadeInNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        sharedConstructor(context, attrs);
    }

    public RoundedFadeInNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        sharedConstructor(context, attrs);
    }

    private void sharedConstructor(Context context, AttributeSet attrs) {
        mContext = context;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedFadeInNetworkImageView);

            if (a != null) {
                mCornersRadius = a.getDimension(R.styleable.RoundedFadeInNetworkImageView_corners_radius, 0);
                a.recycle();
            }
        }
    }

    @Override
    protected void drawClipMask(Canvas canvas, RectF rectF, Paint paint) {
        canvas.drawRoundRect(rectF, mCornersRadius, mCornersRadius, paint);
    }

    public void setOnBitmapChangeListener(OnBitmapChangeListener onBitmapChangeListener) {
        mOnBitmapChangeListener = onBitmapChangeListener;
    }

}
