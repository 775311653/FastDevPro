package com.mohe.fastdevpro.study.sqliteDBDemo.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.mohe.fastdevpro.study.sqliteDBDemo.dbTable.CompanyTHelper;

public class DBContentProvider extends ContentProvider {

    //所有对外提供的数据uri地址
    private UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    public static final String AUTHORITY="com.mohe.fastdevpro.dbContentProvider";

    public static final Uri URI_COMPANY =Uri.parse("content://"+AUTHORITY+"/company");

    private static final int MATCH_COMPANY=1;
    private CompanyTHelper companyTHelper;

    public DBContentProvider() {
    }


    @Override
    public boolean onCreate() {
        uriMatcher.addURI(AUTHORITY,"company",MATCH_COMPANY);
        companyTHelper=new CompanyTHelper(getContext());
        return false;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case MATCH_COMPANY:
                count= companyTHelper.getWritableDatabase().delete(CompanyTHelper.COMPANY_TABLE_NAME,selection,selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri resultUri = null;
        switch (uriMatcher.match(uri)){
            case MATCH_COMPANY:
                long insertId= companyTHelper.getWritableDatabase().insert(CompanyTHelper.COMPANY_TABLE_NAME,null,values);
                if (insertId>0){
                    //把插入后的数据的地址转成uri返回
                    resultUri= ContentUris.withAppendedId(URI_COMPANY,insertId);
                }
                break;
        }
        return resultUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case MATCH_COMPANY:
                cursor=companyTHelper.getWritableDatabase()
                        .query(CompanyTHelper.COMPANY_TABLE_NAME
                                ,projection,selection,selectionArgs,null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)){
            case MATCH_COMPANY:
                count= companyTHelper.getWritableDatabase()
                        .update(CompanyTHelper.COMPANY_TABLE_NAME,values,selection,selectionArgs);
                break;
        }
        //通知观测这个数据库的应用，数据有变
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
