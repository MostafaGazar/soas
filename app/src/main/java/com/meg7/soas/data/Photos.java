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

package com.meg7.soas.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data class representing a collection of Panoramio photos based on location and zoom level.
 *
 * It also holds a total count of photos in all pages and a flag to indicate if end is reached.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class Photos {

    public int count;

    @SerializedName("has_more")
    public boolean hasMore;

    @SerializedName("map_location")
    public Location location;

    public List<Photo> photos;

    /**
     * Class for what's related to photos request parameters.
     */
    public static class RequestParamsBuilder {

        private final Map<String, String> mParams = new HashMap<String, String>();

        /**
         * Add basic parameters to map.
         *
         * @param from Item to start from.
         * @param to   Item to end at.
         */
        public RequestParamsBuilder(int from, int to) {
            mParams.put("set", "public");// Default set.

            mParams.put("from", String.valueOf(from));
            mParams.put("to", String.valueOf(to));
        }

        /**
         * Add set parameter map.
         *
         * @param set Items set type.
         * @return Return self.
         */
        public RequestParamsBuilder set(String set) {
            mParams.put("set", set);
            return this;
        }

        /**
         * Returns map of request parameters.
         *
         * @return Map of request parameters.
         */
        public Map<String, String> build() {
            return mParams;
        }

    }

}
