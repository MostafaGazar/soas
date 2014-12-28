package com.meg7.soas.database.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.meg7.soas.database.DatabaseHelper;
import com.meg7.soas.database.OfflineNotesDataSource;

import java.util.Arrays;
import java.util.HashSet;

/**
 * This class provides the ability to query the offline notes database.
 */
public class OfflineNotesProvider extends ContentProvider {

    // Used for the UriMatcher.
    private static final int OFFLINE_NOTES = 1;
    private static final int OFFLINE_NOTE_ID = 2;

    private static final String BASE_PATH = "offline_notes";

    public static final Uri CONTENT_URI = Uri.parse("content://" + ProviderConstants.AUTHORITY_OFFLINE_NOTES
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/offline_notes";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/offline_note";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ProviderConstants.AUTHORITY_OFFLINE_NOTES, BASE_PATH, OFFLINE_NOTES);
        sUriMatcher.addURI(ProviderConstants.AUTHORITY_OFFLINE_NOTES, BASE_PATH + "/#", OFFLINE_NOTE_ID);
    }

    // Database helper.
    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Make sure requested columns exist.
        checkColumns(projection);

        // Set table.
        queryBuilder.setTables(OfflineNotesDataSource.TABLE_NAME);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case OFFLINE_NOTES:
                break;
            case OFFLINE_NOTE_ID:
                // Query by OfflineNote Id
                queryBuilder.appendWhere(OfflineNotesDataSource.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);

        // Register to watch a content URI for changes.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        long id;
        switch (uriType) {
            case OFFLINE_NOTES:
                id = database.insert(OfflineNotesDataSource.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // Notify registered observers that a row was updated.
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        int rowsDeleted;
        switch (uriType) {
            case OFFLINE_NOTES:
                rowsDeleted = database.delete(OfflineNotesDataSource.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case OFFLINE_NOTE_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(OfflineNotesDataSource.TABLE_NAME,
                            OfflineNotesDataSource.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = database.delete(OfflineNotesDataSource.TABLE_NAME,
                            OfflineNotesDataSource.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // Notify registered observers that a row was deleted.
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        int rowsUpdated;
        switch (uriType) {
            case OFFLINE_NOTES:
                rowsUpdated = database.update(OfflineNotesDataSource.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case OFFLINE_NOTE_ID:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(OfflineNotesDataSource.TABLE_NAME, values,
                            OfflineNotesDataSource.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = database.update(OfflineNotesDataSource.TABLE_NAME, values,
                            OfflineNotesDataSource.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // Notify registered observers that a row was updated.
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            // Get all OfflineNote records.
            case OFFLINE_NOTES:
                return "vnd.android.cursor.dir/offline_notes";
            // Get a particular OfflineNote.
            case OFFLINE_NOTE_ID:
                return "vnd.android.cursor.item/offline_notes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private void checkColumns(String[] projection) {
        String[] available = {OfflineNotesDataSource.COLUMN_PHOTO_ID,
                OfflineNotesDataSource.COLUMN_NOTE,
                OfflineNotesDataSource.COLUMN_ID};

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            // Check if all columns which are requested are available.
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
