package com.example.sportclub.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;

public final class SportClubContract {


    private SportClubContract(){ }

    public static final int DATA_BASE_VERSION = 1;
    public static final String DATA_BASE_NAME = "sport club";

    // Создание URI
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.sportclub.data";
    public static final String PATH_MEMBERS = "members";

    // Константы класса URI
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);





    // Создание таблицы базы данных
    public static final class MemberEntry implements BaseColumns {



        public static final String TABLE_NAME = "members";


        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SPORT = "sport";



        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + PATH_MEMBERS;
        public static final String SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + PATH_MEMBERS;

    }
}
