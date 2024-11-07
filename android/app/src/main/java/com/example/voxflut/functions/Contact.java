package com.example.voxflut.functions;

import android.database.Cursor;

import com.ca.Utils.CSConstants;
import com.ca.Utils.CSDbFields;
import com.ca.dao.CSContact;
import com.ca.wrapper.CSClient;
import com.ca.wrapper.CSDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contact {
    public static boolean addContact(CSClient CSClientObj, String name, String number) {
        CSContact contact = new CSContact(
                name,
                number,
                CSConstants.CONTACTTYPE_MOBILE,
                number,
                false
        );
        ArrayList<CSContact> contactsList = new ArrayList<>();
        contactsList.add(contact);

        try {
            return CSClientObj.addContacts(contactsList);
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, String> getContacts() {
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

    public static boolean deleteContact(CSClient CSClientObj, String number) {
        try {
            return CSClientObj.deleteContact(number);
        } catch (Exception e) {
            return false;
        }
    }
}
