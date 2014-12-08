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

/**
 * A data class representing a geographic location.
 * <p/>
 * A location can consist of a latitude, longitude, and other information such as zoom level.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class Location {

    public double lat;
    public double lon;

    @SerializedName("panoramio_zoom")
    public int zoom;

}
