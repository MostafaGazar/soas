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

package com.meg7.soas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.meg7.soas.data.OfflineNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Test DataBase table to store offline notes to database.
 *
 * @author Mostafa Gazar <eng.mostafa.gazar@gmail.com>
 */
public class OfflineNotesDataSource {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    // Table name.
    public static final String TABLE_NAME = "offlineNotes";

    // Table fields.
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHOTO_ID = "photoId";
    public static final String COLUMN_NOTE = "note";

    // Database create SQL statement.
    protected static final String CREATE_TABLE_OFFLINE_NOTES =
            "CREATE TABLE " + TABLE_NAME
                    + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PHOTO_ID + " INTEGER NOT NULL, "
                    + COLUMN_NOTE + " TEXT NOT NULL"
                    + ");";

    // Database drop SQL statement.
    protected static final String DROP_TABLE_OFFLINE_NOTES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public OfflineNotesDataSource(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
    }

    /**
     * Create a new {@link OfflineNote}.
     *
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long createOfflineNote(Context context, int photoId, String note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, photoId);
        values.put(COLUMN_NOTE, note);

        // Insert row.
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * Get single {@link OfflineNote} with specified id.
     *
     * @return OfflineNote with specified id, or null if does not exist.
     */
    public OfflineNote getOfflineNote(Context context, long id) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor != null) {
            cursor.moveToFirst();

            OfflineNote offlineNote = new OfflineNote();
            offlineNote.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            offlineNote.photoId = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_ID));
            offlineNote.note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));

            return offlineNote;
        }

        return null;
    }

    /**
     * Get all {@link OfflineNote} assigned to specific photo.
     *
     * @return List of all OfflineNote with related to specified photoId, or empty list if non exist.
     */
    public List<OfflineNote> getAllOfflineNotesByPhotoId(Context context, int photoId) {
        List<OfflineNote> offlineNotes = new ArrayList<OfflineNote>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_PHOTO_ID + " = ?";

        Cursor cursor = mDatabase.rawQuery(selectQuery, new String[]{String.valueOf(photoId)});

        // Add rows and adding to list.
        if (cursor.moveToFirst()) {
            do {
                OfflineNote offlineNote = new OfflineNote();
                offlineNote.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                offlineNote.photoId = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO_ID));
                offlineNote.note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));

                // Add to offlineNotes list.
                offlineNotes.add(offlineNote);
            } while (cursor.moveToNext());
        }

        return offlineNotes;
    }

    /**
     * Update an {@link OfflineNote} with specified id.
     *
     * @return The number of rows affected
     */
    public int updateOfflineNote(Context context, long id, String updatedNote) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, updatedNote);

        return mDatabase.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Delete an {@link OfflineNote} with specified id.
     *
     * @return The number of rows affected if a whereClause is passed in, 0 otherwise.
     */
    public int deleteOfflineNote(Context context, long id) {
        return mDatabase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

}
