package com.example.safeguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "safeguard_user.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CONTACT1 = "contact1";
    private static final String COLUMN_CONTACT2 = "contact2";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CONTACT1 + " TEXT, " +
                COLUMN_CONTACT2 + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addUser(String name, String email, String password, String contact1, String contact2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_CONTACT1, contact1);
        contentValues.put(COLUMN_CONTACT2, contact2);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result != -1) {
            return name; // Return the username if the user was added successfully
        } else {
            return null; // Return null if the user addition failed
        }
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM your_table_name WHERE username = ?", new String[]{username});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean checkPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM your_table_name WHERE username = ? AND password = ?", new String[]{username, password});
        boolean matchFound = (cursor.getCount() > 0);
        cursor.close();
        return matchFound;
    }
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve from the database
        String[] columns = {COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_CONTACT1, COLUMN_CONTACT2};

        // Query the database to retrieve all contact records
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null) {
            try {
                // Iterate over the cursor to populate the list of contacts
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                    String contact1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT1));
                    String contact2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT2));

                    // Create a new Contact object and add it to the list
                    Contact contact = new Contact(name, email, password, contact1, contact2);
                    contacts.add(contact);
                }
            } finally {
                // Close the cursor when done
                cursor.close();
            }
        }

        return contacts;
    }

    // Method to retrieve the stored password
    public String getPassword() {
        String password = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PASSWORD}; // Specify the column to retrieve
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
        }
        cursor.close();
        return password;
    }

    public String getUsername(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NAME}; // Specify the column to retrieve
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String username = "";
        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            cursor.close();
        }
        return username;
    }
}


