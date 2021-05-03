package com.example.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sportclub.data.SportClubContract.MemberEntry;

public class SportClubContentProvider extends ContentProvider {

    SportClubDbOpenHelper dbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBERS_ID = 222;

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(SportClubContract.AUTHORITY, SportClubContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(SportClubContract.AUTHORITY, SportClubContract.PATH_MEMBERS + "/#", MEMBERS_ID);

    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new SportClubDbOpenHelper(getContext());
        return true;
    }

    // CRUD metods

    // Read
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // Create
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Валидация ввода данных
        String firsName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
        if (firsName == null){
            throw new IllegalArgumentException("You have to input first name" + uri);
        }
        String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("You have to input last name" + uri);
        }

        Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
        if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN
                || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)){
            throw new IllegalArgumentException("You have to input correct gender" + uri);
        }

        String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
        if (sport == null){
            throw new IllegalArgumentException("You have to input sport" + uri);
        }


        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("insertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri,null);

                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion of data in the table failed for " + uri);
        }
    }

    // Delete
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowDeleted;

        switch (match) {
            case MEMBERS:
                rowDeleted = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }

        if (rowDeleted !=0){getContext().getContentResolver().notifyChange(uri,null);}
        return rowDeleted;
    }

    // Update
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MemberEntry.COLUMN_FIRST_NAME)){
            String firsName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
            if (firsName == null){
                throw new IllegalArgumentException("You have to input first name" + uri);
            }
        }

        if (values.containsKey(MemberEntry.COLUMN_LAST_NAME)){
            String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null){
                throw new IllegalArgumentException("You have to input last name" + uri);
            }
        }


        if (values.containsKey(MemberEntry.COLUMN_GENDER)){
            Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == MemberEntry.GENDER_UNKNOWN
                    || gender == MemberEntry.GENDER_MALE || gender == MemberEntry.GENDER_FEMALE)){
                throw new IllegalArgumentException("You have to input correct gender" + uri);
            }
        }

        if (values.containsKey(MemberEntry.COLUMN_SPORT)){
            String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
            if (sport == null){
                throw new IllegalArgumentException("You have to input sport" + uri);
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowUpdated;

        switch (match) {
            case MEMBERS:
                rowUpdated = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdated = db.update(MemberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
        if (rowUpdated !=0){getContext().getContentResolver().notifyChange(uri, null);}

        return rowUpdated;
    }

    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:
                return MemberEntry.SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
    }
}