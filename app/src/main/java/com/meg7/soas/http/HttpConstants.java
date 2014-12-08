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

package com.meg7.soas.http;

/**
 * A class for all external links used in the app.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class HttpConstants {

    // http://www.panoramio.com/api/data/api.html
    // http://www.panoramio.com/map/get_panoramas.php?set=public&from=0&to=20
    public static final String BASE_URL = "http://www.panoramio.com/map/get_panoramas.php";// ?set=public&from=0&to=20

    public static final String PHOTOS_URL = BASE_URL;

    public static final String LOGIN_PATH = "auth";

}
