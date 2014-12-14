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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.meg7.soas.R;

/**
 * Fade-in downloaded image upon download while maintaining the scaleType.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public abstract class BaseFadeInNetworkImageView extends NetworkImageView {

    public BaseFadeInNetworkImageView(Context context) {
        super(context);
    }

    public BaseFadeInNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFadeInNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        // For configureBounds to be called and quick set.
        super.setImageBitmap(bitmap);

        if (bitmap != null) {
            Resources res = getResources();
            Drawable[] layers = new Drawable[2];

            // Layer 0.
            layers[0] = res.getDrawable(R.drawable.default_photo);

            // Layer 1.
            // For masking the Bitmap after scale_type is used.
            layers[1] = new BitmapDrawable(res, clipBitmap(bitmap));

            TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
            transitionDrawable.setCrossFadeEnabled(true);
            setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(400);
        }
    }

    /**
     * Clipped downloaded Bitmap based on view set radius and scaleType.
     *
     * @param bitmap Downloaded Bitmap.
     * @return Clipped Bitmap.
     */
    protected Bitmap clipBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap clippedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clippedBitmap);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        Matrix matrix = getImageMatrix();
        float[] values = new float[9];
        matrix.getValues(values);
        float absTransX = Math.abs(values[Matrix.MTRANS_X]) * (1 / values[Matrix.MSCALE_X]);
        float absTransY = Math.abs(values[Matrix.MTRANS_Y]) * (1 / values[Matrix.MSCALE_Y]);
        RectF rectF = new RectF(absTransX, absTransY, width - absTransX, height - absTransY);
        drawClipMask(canvas, rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return clippedBitmap;
    }

    protected abstract void drawClipMask(Canvas canvas, RectF rectF, Paint paint);

}
