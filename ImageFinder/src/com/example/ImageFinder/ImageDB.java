package com.example.ImageFinder;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ImageDB {

    public void SaveImageBase(ArrayList<Image> images, Context ctx) {

        DBHelper dbHelper = new DBHelper(ctx);
        ContentValues cv;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("imagestable", null, null);

        for(Image img:images){

            cv = new ContentValues();

            cv.put("title", img.getTitle());
            cv.put("url", img.getUrl().toString());
            cv.put("tburl", img.getTbUrl().toString());
            cv.put("visibleurl", img.getVisibleUrl());
            cv.put("width",img.getWidth());
            cv.put("height",img.getHeight());
            cv.put("tbwidth",img.getTbWidth());
            cv.put("tbheight",img.getTbHeight());

            db.insert("imagestable", null, cv);
        }
        dbHelper.close();
    }

    public ArrayList<Image> LoadImageBase(Context ctx) {

        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Image> images = new ArrayList<Image>();

        Cursor c = db.query("imagestable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            int titleColIndex = c.getColumnIndex("title");
            int urlColIndex = c.getColumnIndex("url");
            int tburlColIndex = c.getColumnIndex("tburl");
            int visurlColIndex = c.getColumnIndex("visibleurl");
            int widthColIndex = c.getColumnIndex("width");
            int tbwidthColIndex = c.getColumnIndex("tbwidth");
            int heightColIndex = c.getColumnIndex("height");
            int tbheightColIndex = c.getColumnIndex("tbheight");

            Image img;
            URL url;
            URL tburl;

            do {
                try {
                    img = new Image(c.getString(titleColIndex),new URL(c.getString(urlColIndex)),new URL(c.getString(tburlColIndex)),
                            c.getString(visurlColIndex),c.getInt(widthColIndex),c.getInt(heightColIndex),c.getInt(tbwidthColIndex),
                            c.getInt(tbheightColIndex));
                    images.add(img);
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
        return images;
    }
}

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "imagesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table imagestable (id integer primary key autoincrement,title text,url text,tburl text,visibleurl text,"
                + "width integer,height integer,tbwidth integer,tbheight integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
