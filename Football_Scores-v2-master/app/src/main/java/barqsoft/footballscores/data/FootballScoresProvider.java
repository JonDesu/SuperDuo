package barqsoft.footballscores.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;

import barqsoft.footballscores.model.DatabaseContract;
import barqsoft.footballscores.helper.DatabaseHelper;

public class FootballScoresProvider extends ContentProvider {

    private static final String LOG_TAG = FootballScoresProvider.class.getSimpleName();

    private DatabaseHelper mDatabaseHelper;

    public static final Uri TEAMS_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("teams").build();
    public static final Uri FIXTURES_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("fixtures").build();
    public static final Uri FIXTURES_AND_TEAMS_URI = DatabaseContract.BASE_CONTENT_URI.buildUpon().appendPath("fixtures_teams").build();

    private static final int TEAMS_URI_CODE = 100;
    private static final int FIXTURES_URI_CODE = 101;
    private static final int FIXTURES_AND_TEAMS_URI_CODE = 102;

    private UriMatcher mUriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "teams" , TEAMS_URI_CODE);
        matcher.addURI(authority, "fixtures" , FIXTURES_URI_CODE);
        matcher.addURI(authority, "fixtures_teams" , FIXTURES_AND_TEAMS_URI_CODE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(LOG_TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ", selection=" + selection + ", selectionArgs=" + Arrays.toString(selectionArgs) +")");

        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case TEAMS_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.TEAMS_TABLE);
                cursor = db.query(DatabaseContract.TEAMS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FIXTURES_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.FIXTURES_TABLE);
                cursor = db.query(DatabaseContract.FIXTURES_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case FIXTURES_AND_TEAMS_URI_CODE:
                Log.d(LOG_TAG, DatabaseContract.FIXTURES_TEAMS_VIEW);
                cursor = db.query(DatabaseContract.FIXTURES_TEAMS_VIEW, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                Log.e(LOG_TAG, "No implementation for " + uri);
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.v(LOG_TAG, "insert(uri=" + uri + ", values=" + contentValues.toString() + ")");

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        long rowId = -1;
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case TEAMS_URI_CODE:
                rowId = db.insertWithOnConflict(DatabaseContract.TEAMS_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            case FIXTURES_URI_CODE:
                rowId = db.insertWithOnConflict(DatabaseContract.FIXTURES_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                Log.e(LOG_TAG, "No implementation for " + uri);
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowId != -1){
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, rowId);
        }else{
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}
