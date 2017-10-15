package com.squareapps.a4teen.amigos.Common;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


public class ContactsFirebaseLoader {

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    public static final String TAG = "ContactsFirebaseLoader";
    private ContentResolver contentResolver;
    private DatabaseReference reference;
    private int count;

    public ContactsFirebaseLoader(final ContentResolver cr) {
        contentResolver = cr;
        FirebaseUser firebaseUser =
                FirebaseAuth.getInstance().getCurrentUser();
        reference
                = FirebaseDatabase.getInstance().getReference()
                .child(USERS)
                .child(firebaseUser.getUid())
                .child("contacts");
    }


    public void begin() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        final Cursor cursor = getCursor(uri, null, null, null, null);
        count = cursor.getCount();
        uploadNumbers(cursor);
    }

    private void uploadNumbers(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String contactId = getStringContactId(cursor);
                getPhoneNumber(contactId);
            }
        }
        cursor.close();
    }

    private String getStringContactId(Cursor cursor) {
        int columnIndexForId =
                cursor.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId =
                cursor.getString(columnIndexForId);

        int columnIndexForHasPhone =
                cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        Integer hasPhoneNumber =
                Integer.parseInt(cursor.getString(columnIndexForHasPhone));

        //if contact has at least one phone number hasPhoneNumber=="1", "0" otherwise.
        if (hasPhoneNumber == 0) {
            Log.d(TAG, "no phone number for this contact");
        }
        return contactId;
    }

    private void getPhoneNumber(final String contactId) {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String SELECTION = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?";
        String[] selectionArgs = new String[]{contactId};

        final Cursor contactRow = getCursor(
                uri,
                null,
                SELECTION,
                selectionArgs,
                null);


        HashMap<String, Object> map = new HashMap<>();
        map.put("count", count);
        while (contactRow.moveToNext()) {
            String aNumber = contactRow.getString(contactRow.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
            aNumber = validateNumber(aNumber);
            if (aNumber.length() == 10)
                map.put(aNumber, true);
        }

        reference.updateChildren(map);
        contactRow.close();

    }


    @NonNull
    private Cursor getCursor(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = contentResolver.query(
                uri, //Uri
                projection, //String[]projection
                selection, //String selection
                selectionArgs, //String[] selectionArgs
                sortOrder //SortOrder
        );

        assert cursor != null;
        return cursor;
    }

    @NonNull
    private String validateNumber(String aNumber) {
        aNumber = aNumber.replace("(", "");
        aNumber = aNumber.replace(")", "");
        aNumber = aNumber.replace("-", "");
        aNumber = aNumber.replace("+", "");
        aNumber = aNumber.replace(" ", "");
        return aNumber.trim().toString();
    }


}
