package kz.edu.sdu.galix.categories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context ctx){
        super(ctx, "categories", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table categories (" +
                "_id integer primary key autoincrement," +
                "name text)" );
        db.execSQL("create table products (" +
                "_id integer primary key autoincrement," +
                "name text," +
                "cat_id integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}