package com.example.voxflut;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.ca.Utils.CSDbFields;
import com.ca.wrapper.CSDataProvider;

import java.util.HashMap;
import java.util.Map;

public class helper {
    public static @NonNull Map<String, String> getStringStringMap() {
        Cursor contactsCursor = CSDataProvider.getContactsCursor();
        Map<String, String> contacts = new HashMap<>();
        int iterationsForContacts = contactsCursor.getCount();
        if (contactsCursor.getCount() > 0) {
            while (iterationsForContacts > 0) {
                contactsCursor.moveToNext();
                String name = contactsCursor.getString(contactsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CONTACT_NAME));
                String number = contactsCursor.getString(contactsCursor.getColumnIndexOrThrow(CSDbFields.KEY_CONTACT_NUMBER));
                contacts.put(number, name);
                iterationsForContacts = iterationsForContacts - 1;
            }
        }
        return contacts;
    }
}
