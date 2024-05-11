package com.project.app.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.app.models.PhoneBook;
import com.project.app.models.ResponseCode;
import com.project.app.models.ResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhoneBookDatabaseHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "contacts.db";
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";

    // Create table SQL query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT NOT NULL,"
                    + COLUMN_PHONE + " TEXT NOT NULL,"
                    + COLUMN_ADDRESS + " TEXT NOT NULL"
                    + ")";

    public PhoneBookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    //delete contact based on id.
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //check if contact exist
    public boolean contactExists(String phoneNumber, String name, String address) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct the SQL query
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_PHONE + " = ? AND " + COLUMN_NAME + " = ? AND " + COLUMN_ADDRESS + "= ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{phoneNumber, name, address});

        // Check if the cursor has any results
        boolean exists = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return !exists;
    }

    //add contact into sqllite
    public ResponseModel addContact(String name, String phoneNumber, String address) {
        if (contactExists(phoneNumber, name, address)) {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PhoneBookDatabaseHelper.COLUMN_NAME, name);
            values.put(PhoneBookDatabaseHelper.COLUMN_PHONE, phoneNumber);
            values.put(PhoneBookDatabaseHelper.COLUMN_ADDRESS, address);

            db.insert(TABLE_NAME, null, values);

            db.close();
            return new ResponseModel(ResponseCode.SUCCESS, "Contact add successfully");
        } else {
            return new ResponseModel(ResponseCode.DATABASE_DUPLICATE_ENTRY, "Contact already exists");
        }
    }

    // Method to search for contacts by name and return a list of results
    @SuppressLint("Range")
    public List<PhoneBook> searchContactsByText(String value, String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_PHONE,
                COLUMN_ADDRESS
        };
        String selection = COLUMN_NAME + " LIKE ?";
        // Define the WHERE clause
        if (Objects.equals(type, "address")) {
            selection = COLUMN_ADDRESS + " LIKE ?";
        } else if (Objects.equals(type, "phone")) {
            selection = COLUMN_PHONE + " LIKE ?";
        }

        String[] selectionArgs = {"%" + value + "%"}; // Use wildcards to match partial names

        // Execute the query
        Cursor cursor = db.query(
                TABLE_NAME,         // The table to query
                projection,         // The columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );

        ArrayList<PhoneBook> contactList = new ArrayList<>();

        // Process the results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                PhoneBook contact = new PhoneBook();
                contact.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // Don't forget to close the cursor when you're done with it
        if (cursor != null) {
            cursor.close();
        }

        return contactList;
    }

    //update the contact with name, phone number and address in sqllite using id
    public ResponseModel editContact(String name, String phoneNumber, String address, int id) {
        if (contactExists(phoneNumber, name, address)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PhoneBookDatabaseHelper.COLUMN_NAME, name);
            values.put(PhoneBookDatabaseHelper.COLUMN_PHONE, phoneNumber);
            values.put(PhoneBookDatabaseHelper.COLUMN_ADDRESS, address);
            db.update(
                    TABLE_NAME,
                    values,
                    PhoneBookDatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});

            db.close();
            return new ResponseModel(ResponseCode.SUCCESS, "Contact update successfully");
        } else {
            return new ResponseModel(ResponseCode.DATABASE_DUPLICATE_ENTRY, "Contact already exists");
        }
    }

    //return all records of contacts in sqllite
    @SuppressLint("Range")
    public List<PhoneBook> getAllContacts() {
        List<PhoneBook> phoneBooksArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c != null && c.moveToFirst()) {
            do {
                PhoneBook phoneBook = new PhoneBook();
                phoneBook.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                phoneBook.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                phoneBook.setPhoneNumber(c.getString(c.getColumnIndex(COLUMN_PHONE)));
                phoneBook.setAddress(c.getString(c.getColumnIndex(COLUMN_ADDRESS)));

                // adding to list
                phoneBooksArrayList.add(phoneBook);
            } while (c.moveToNext());
        }
        if (c != null) {
            c.close();
        }
        return phoneBooksArrayList;
    }

}
