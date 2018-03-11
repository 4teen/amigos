package com.squareapps.a4teen.amigos.Common.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Common.Contract;

import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.User.PHONE_NUMBER;
import static java.io.File.separator;


public class FirebaseContactsLoader extends ContextWrapper {

    public static final String TAG = "FirebaseContactsLoader";

    private DatabaseReference reference;

    private ContactsCursor contactsCursor;

    public FirebaseContactsLoader(Context base) {
        super(base);
        reference = FirebaseDatabase.getInstance().getReference();

        ContentResolver contentResolver = getContentResolver();
        Uri contactContentUri = ContactsContract.Contacts.CONTENT_URI;

        contactsCursor = new ContactsCursor(contentResolver, contactContentUri, null, null, null, null);
    }


    public void uploadNumbers() {

        if (contactsCursor.moveToFirst()) {
            while (contactsCursor.moveToNext()) {
                ContactsCursor contact = contactsCursor.getContactsCursor();
                getContactInfo(contact);
            }
        }
        contactsCursor.close();
    }

    private void getContactInfo(final ContactsCursor contact) {

        while (contact.moveToNext()) {

            String phoneNumber = contact.getPhoneNumber();

            String displayName = contact.getDisplayName();

            if (phoneNumber.length() == 10) {
                HashMap<String, Object> updates = new HashMap<>();
                updates.put(Contract.USERS + separator + FirebaseAuth.getInstance().getUid() + separator + "contactsCount", contact.getCount());
                updates.put("numbers" + separator + phoneNumber + separator + NAME, displayName);
                updates.put("numbers" + separator + phoneNumber + separator + PHONE_NUMBER, phoneNumber);
                reference.updateChildren(updates);
            }
        }

        contact.close();
    }

    public ContactsCursor getContactsCursor() {
        return contactsCursor;
    }

}
