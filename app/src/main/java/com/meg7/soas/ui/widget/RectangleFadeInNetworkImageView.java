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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Square fade-in network ImageView.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class RectangleFadeInNetworkImageView extends BaseFadeInNetworkImageView {

    public RectangleFadeInNetworkImageView(Context context) {
        super(context);
    }

    public RectangleFadeInNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleFadeInNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void drawClipMask(Canvas canvas, RectF rectF, Paint paint) {
        canvas.drawRect(rectF, paint);
    }
}
