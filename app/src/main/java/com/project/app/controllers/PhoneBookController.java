package com.project.app.controllers;

import android.content.Context;

import com.project.app.models.PhoneBook;
import com.project.app.models.ResponseModel;
import com.project.app.repository.PhoneBookDatabaseHelper;

import java.util.List;


public class PhoneBookController {
    private static PhoneBookDatabaseHelper dbHelper;

    public PhoneBookController(final Context mainContext) {
        dbHelper = new PhoneBookDatabaseHelper(mainContext);
    }

    public List<PhoneBook> searchContactsByText(String value, String type) {
        return dbHelper.searchContactsByText(value, type);
    }

    public List<PhoneBook> getAllContacts()  {
        return dbHelper.getAllContacts();
    }
    public ResponseModel addContact(String name, String phoneNumber, String address) {
        return dbHelper.addContact(name, phoneNumber, address);
    }

    public ResponseModel editContact(String name, String phoneNumber, String address, int id) {
       return dbHelper.editContact(name, phoneNumber, address, id);
    }

    public void deleteContact(int id) {

        dbHelper.deleteContact(id);
    }

}
