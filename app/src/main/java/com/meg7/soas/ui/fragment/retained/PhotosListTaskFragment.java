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

package com.meg7.soas.ui.fragment.retained;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.meg7.soas.SoasApplication;
import com.meg7.soas.data.Photos;
import com.meg7.soas.http.HttpConstants;
import com.meg7.soas.http.request.GsonGetRequest;
import com.meg7.soas.http.util.UrlBuilder;

import org.apache.http.HttpStatus;

/**
 * This Fragment manages a single background task and retains
 * itself across configuration changes.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class PhotosListTaskFragment extends Fragment {

    public static final String TAG = PhotosListTaskFragment.class.getSimpleName();

    public static enum Task { REFRESH, REQUEST_NEXT }

    private int mLastRequestedPhotosStatusCode;
    private Photos mLastRequestedPhotos;

    private int mNextItemToLoad = 0;
    private static final int PAGE_SIZE = 14;

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public static interface TaskCallbacks {

        void onResponse(Task task, int statusCode, Photos photos);

    }

    private TaskCallbacks mCallbacks;

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // First initial request.
        refresh();
    }

    public void refresh() {
        mLastRequestedPhotos = null;
        mNextItemToLoad = 0;

        String url = getRequestUrl();

        Cache cache = SoasApplication.getRequestQueue(getActivity()).getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            // Invalidate cache and reload.
            SoasApplication.getRequestQueue(getActivity()).getCache().invalidate(url, true);
        }

        GsonGetRequest<Photos> mainRequest = new GsonGetRequest<Photos>
                (url, Photos.class,
                        new Response.Listener<Photos>() {
                            @Override
                            public void onResponse(Photos photos) {
                                populateResponse(Task.REFRESH, photos);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                populateResponse(Task.REFRESH, error);
                            }
                        }
                );
        mainRequest.setShouldCache(true);
        SoasApplication.addToRequestQueue(getActivity(), mainRequest, TAG);
    }

    public void requestNextPage() {
        // Create and execute the background task.
        String url = getRequestUrl();

        Cache cache = SoasApplication.getRequestQueue(getActivity()).getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            // Soft invalidate cache because we forced it earlier.
            // TODO :: Comment out if cache was based on server response cache headers.
            SoasApplication.getRequestQueue(getActivity()).getCache().invalidate(url, false);
        }

        GsonGetRequest<Photos> mainRequest = new GsonGetRequest<Photos>
                (url, Photos.class,
                        new Response.Listener<Photos>() {
                            @Override
                            public void onResponse(Photos photos) {
                                populateResponse(Task.REQUEST_NEXT, photos);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                populateResponse(Task.REQUEST_NEXT, error);
                            }
                        }
                );
        mainRequest.setShouldCache(true);
        SoasApplication.addToRequestQueue(getActivity(), mainRequest, TAG);
    }

    private String getRequestUrl() {
        return UrlBuilder.appendGetParams(HttpConstants.PHOTOS_URL,
                new Photos.RequestParamsBuilder(mNextItemToLoad, mNextItemToLoad + PAGE_SIZE).build());
    }

    private void populateResponse(Task task, Photos photos) {
        mLastRequestedPhotosStatusCode = HttpStatus.SC_OK;
        mLastRequestedPhotos = photos;
        mNextItemToLoad += PAGE_SIZE + 1;
        mCallbacks.onResponse(task, mLastRequestedPhotosStatusCode, mLastRequestedPhotos);
    }

    private void populateResponse(Task task, VolleyError error) {
        if (error != null && error.networkResponse != null) {
            mLastRequestedPhotosStatusCode = error.networkResponse.statusCode;
        } else {
            mLastRequestedPhotosStatusCode = -1;
        }
        mLastRequestedPhotos = null;
        mCallbacks.onResponse(task, mLastRequestedPhotosStatusCode, mLastRequestedPhotos);
    }

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof TaskCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        // Cancel any running Http requests associated with this fragment TAG.
        SoasApplication.cancelPendingRequests(TAG);
    }

    public int getLastRequestedPhotosStatusCode() {
        return mLastRequestedPhotosStatusCode;
    }

    public Photos getLastRequestedPhotos() {
        return mLastRequestedPhotos;
    }

}
