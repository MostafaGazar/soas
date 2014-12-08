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

import org.parceler.Parcel;

/**
 * A data class representing a Panoramio photo.
 *
 * <p>A photo consist of information about its dimensions, where it was taken, and other information
 * such as photo owner details, when the photo was taken and its url.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
@Parcel
public class Photo {

    public int width;
    public int height;

    public double latitude;
    public double longitude;

    @SerializedName("owner_id")
    public int ownerId;
    @SerializedName("owner_name")
    public String ownerName;
    @SerializedName("owner_url")
    public String ownerUrl;

    @SerializedName("photo_id")
    public int photoId;
    @SerializedName("photo_title")
    public String photoTitle;
    @SerializedName("photo_url")
    public String photoUrl;
    @SerializedName("photo_file_url")
    public String photoFileUrl;

    @SerializedName("upload_date")
    public String uploadDate;

    @Override
    public String toString() {
        return photoTitle + ", " + photoUrl;
    }
}
