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

package com.meg7.soas.http.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.meg7.soas.http.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Class for all gson response parsed GET requests.
 *
 * @param <T> The type of parsed response this request expects.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class GsonGetRequest<T> extends Request<T> {

    private final Gson mGson = new Gson();

    /**
     * Class object of generic T.
     */
    private final Class<T> mClassOfT;

    /**
     * Callback object, which is notified of delivered response.
     */
    private final Response.Listener<T> mListener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url      URL of the request to make.
     * @param classOfT Relevant class object, for Gson's reflection.
     */
    public GsonGetRequest(String url, Class<T> classOfT,
                          Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mClassOfT = classOfT;
        mListener = listener;
    }

    // Only works with POST and PUT.
//    @Override
//    public Map<String, String> getParams() throws AuthFailureError {
//        return mParams != null ? mParams : super.getParams();
//    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            // NOTE :: Cache anyway regardless of server response cache headers.
            Cache.Entry cacheEntry = HttpHeaderParser.parseIgnoreCacheHeaders(response);

            return Response.success(mGson.fromJson(json, mClassOfT), cacheEntry);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}