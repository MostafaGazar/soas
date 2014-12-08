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

package com.meg7.soas.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Bitmap memory cache.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
	
	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		return cacheSize;
	}

	public BitmapLruCache() {
		this(getDefaultLruCacheSize());
	}

	public BitmapLruCache(int sizeInKiloBytes) {
		super(sizeInKiloBytes);
	}

	@SuppressLint("NewApi")
	@Override
	protected int sizeOf(String key, Bitmap bitmap) {
		// The cache size will be measured in kilobytes rather than
        // number of items.
    	if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
    		return bitmap.getAllocationByteCount() / 1024;
    	} else if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount() / 1024;
    	} else {
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
}
