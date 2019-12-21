package com.example.wordsbook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class Wordsbook extends ContentProvider {
    public Wordsbook() {
    }
    public static final int BOOK_DIR=0;
    public static final int BOOK_ITEM=1;
    public static final String AUTHORITY="com.example.wordsbook.provider";
    public static UriMatcher uriMatcher;
    private MyDataBaseHelper dbHelper;
    static{
        uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"WordsBook",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"WordsBook/#",BOOK_ITEM);
    }
    @Override
    public boolean onCreate(){
        dbHelper= new MyDataBaseHelper(getContext(),"wordsbook.db",1);
        return true;
    }





    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int deletedRows=0;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                deletedRows=db.delete("WordsBook",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId= uri.getPathSegments().get(1);
                deletedRows =db.delete("WordsBook","id=?",new String[]{bookId});
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                return"vnd.android.cursor.dir/vnd.com.example.wordsbook.MyContentProvider";
            case BOOK_ITEM:
                return"vnd.android.cursor.item/vnd.com.example.wordsbook.MyContentProvider";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Uri uriReturn= null;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert("WordsBook",null,values);
                uriReturn =Uri.parse("content://"+AUTHORITY+"/WordsBook/"+newBookId);
                break;

        }
        return uriReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor=db.query("WordsBook",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId =uri.getPathSegments().get(1);
                cursor=db.query("WordsBook",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;

        }
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int updatedRows=0;
        switch(uriMatcher.match(uri)){
            case BOOK_DIR:
                updatedRows=db.update("WordsBook",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                updatedRows =db.update("WordsBook",values,"id=?",new String[]{bookId});
                break;
        }
        return updatedRows;
    }
}
