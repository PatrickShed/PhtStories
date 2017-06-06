package sk.upjs.ics.android.phtstories.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.upjs.ics.android.phtstories.Constants;

/**
 * Created by Patrik on 6.6.2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "phtstories";

    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, "picstories", Constants.DEFAULT_CURSOR_FACTORY, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql());
    }

    private String createTableSql(){
        String sqlTemplate = "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL," +
                "%s TEXT," +
                "%s TEXT" +
                ")";
        return String.format(sqlTemplate,
                PhtStoriesContract.Pstory.TABLE_NAME,
                PhtStoriesContract.Pstory._ID,
                PhtStoriesContract.Pstory.HEADER,
                PhtStoriesContract.Pstory.TALE,
                PhtStoriesContract.Pstory.PICTURE
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void insertEntry(SQLiteDatabase db, String header, String tale, byte[] picture, String path){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhtStoriesContract.Pstory.HEADER, header);
        contentValues.put(PhtStoriesContract.Pstory.TALE, tale);
        contentValues.put(PhtStoriesContract.Pstory.PICTURE, picture);
        db.insert(PhtStoriesContract.Pstory.TABLE_NAME, Constants.NO_NULL_COLUMN_HACK, contentValues);
    }
}
