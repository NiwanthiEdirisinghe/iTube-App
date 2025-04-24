package com.example.itubeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "iTubeDatabase.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_USERS = "Users";
    private static final String TABLE_PLAYLISTS = "Playlists";

    // User table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";

    // Playlist table columns
    private static final String COLUMN_PLAYLIST_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_VIDEO_URL = "video_url";
    private static final String COLUMN_ADDED_DATE = "added_date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_FULL_NAME + " TEXT)";

        // Create Playlists table
        String createPlaylistsTable = "CREATE TABLE " + TABLE_PLAYLISTS + "("
                + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER NOT NULL, "
                + COLUMN_VIDEO_URL + " TEXT NOT NULL, "
                + COLUMN_ADDED_DATE + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES "
                + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createPlaylistsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User management methods
    public long insertUser(String username, String password, String fullName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FULL_NAME, fullName);

        return db.insert(TABLE_USERS, null, values);
    }

    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_USERS,
                null,
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );
    }

    // Playlist management methods
    public long addVideoToPlaylist(long userId, String videoUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_VIDEO_URL, videoUrl);
        values.put(COLUMN_ADDED_DATE, getCurrentDateTime());

        return db.insert(TABLE_PLAYLISTS, null, values);
    }

    public Cursor getUserPlaylist(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_PLAYLISTS,
                null,
                COLUMN_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                COLUMN_ADDED_DATE + " DESC"
        );
    }

    public boolean deleteVideoFromPlaylist(long playlistId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(
                TABLE_PLAYLISTS,
                COLUMN_PLAYLIST_ID + "=?",
                new String[]{String.valueOf(playlistId)}
        );

        return rowsAffected > 0;
    }

    private String getCurrentDateTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}
