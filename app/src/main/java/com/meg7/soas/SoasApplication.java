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

package com.meg7.soas;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.meg7.soas.util.BitmapLruCache;

/**
 * Setup application caches and request queues.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class SoasApplication extends Application {

    /**
     * Default request tag, will be used if request tag was empty or null.
     */
    public static final String DEFAULT_REQUEST_TAG = "SoasRequest";

    /**
     * Memory cache for downloaded images.
     */
    private static BitmapLruCache mBitmapCache = new BitmapLruCache();

    /**
     * Http request queue for Volley.
     */
    private static RequestQueue mRequestQueue;

    /**
     * Images request queue for Volley.
     */
    private static ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Initialize two request queues, one for normal Http requests and another one for images.
     *
     * @param context A context used for creating cache dir.
     */
    private static synchronized void initVolleyQueues(Context context) {
        if (mRequestQueue != null && mImageLoader != null) {
            return;
        }

        // Http request queue.
        mRequestQueue = Volley.newRequestQueue(context);

        // Separate request queue for images.
        mImageLoader = new ImageLoader(Volley.newRequestQueue(context), mBitmapCache);
    }

    /**
     * Get Http request queue.
     *
     * @param context A context used for creating cache dir.
     * @return The Volley Http request queue, the queue will be created if it is null.
     */
    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            initVolleyQueues(context);
        }

        return mRequestQueue;
    }

    /**
     * Get images request queue.
     *
     * @param context A context used for creating cache dir.
     * @return The Volley images request queue, the queue will be created if it is null.
     */
    public static ImageLoader getImageLoader(Context context) {
        if (mImageLoader == null) {
            initVolleyQueues(context);
        }

        return mImageLoader;
    }

    /**
     * Adds the specified request to the http queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param context A context used for creating cache dir.
     * @param request Request to be executed.
     * @param tag Request tag.
     */
    public static <T> void addToRequestQueue(Context context, Request<T> request, String tag) {
        // Use default request tag if tag is empty.
        request.setTag(TextUtils.isEmpty(tag) ? DEFAULT_REQUEST_TAG : tag);

        VolleyLog.d("Adding request to queue: %s", request.getUrl());

        getRequestQueue(context).add(request);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param context A context used for creating cache dir.
     * @param request Request to be executed.
     */
    public static <T> void addToRequestQueue(Context context, Request<T> request) {
        // Use default request tag.
        request.setTag(DEFAULT_REQUEST_TAG);

        getRequestQueue(context).add(request);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag Tag to be used to cancel all requests in its queue.
     */
    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}