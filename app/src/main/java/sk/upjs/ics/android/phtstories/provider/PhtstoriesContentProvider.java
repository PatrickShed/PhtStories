package sk.upjs.ics.android.phtstories.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import sk.upjs.ics.android.phtstories.Constants;

/**
 * Created by Patrik on 6.6.2017.
 */

public class PhtstoriesContentProvider extends ContentProvider {
    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(PhtStoriesContract.Pstory.TABLE_NAME,
                Constants.ALL_COLUMNS,
                Constants.NO_SELECTION,
                Constants.NO_SELECTION_ARGS,
                Constants.NO_GROUP_BY,
                Constants.NO_HAVING,
                Constants.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), PhtStoriesContract.Pstory.CONTENT_URI);
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
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

        long id = db.insert(PhtStoriesContract.Pstory.TABLE_NAME, Constants.NO_NULL_COLUMN_HACK, contentValues);

        getContext().getContentResolver().notifyChange(PhtStoriesContract.Pstory.CONTENT_URI, Constants.NO_CONTENT_OBSERVER);
        return Uri.withAppendedPath(PhtStoriesContract.Pstory.CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        String id = uri.getLastPathSegment();
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();
        String[] whereArgs = {id};

        int affectedRows = db.delete(PhtStoriesContract.Pstory.TABLE_NAME, PhtStoriesContract.Pstory._ID + "=?", whereArgs);

        getContext().getContentResolver().notifyChange(PhtStoriesContract.Pstory.CONTENT_URI,Constants.NO_CONTENT_OBSERVER);

        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
