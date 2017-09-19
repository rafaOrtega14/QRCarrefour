package com.example.mac.qrcode.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 15/10/16.
 */

public class CreateTable extends SQLiteOpenHelper{
    String sqlCreate = "CREATE TABLE Items (Item_id TEXT, product TEXT,calory INTEGER,price TEXT)";

    public CreateTable(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Items");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
