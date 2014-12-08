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

package com.meg7.soas.http.util;

import android.net.Uri;

import java.util.Map;

/**
 * Build URLs with parameters.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class UrlBuilder {

    /**
     * Append get parameters to an existing url.
     *
     * @param url Base url.
     * @param params Map representing parameters, key and values.
     * @return Valid url with get parameters appended to it.
     */
    public static String appendGetParams(String url, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.build().toString();
    }

}
