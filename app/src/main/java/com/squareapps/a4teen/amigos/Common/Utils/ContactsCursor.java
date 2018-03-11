package com.squareapps.a4teen.amigos.Common.Utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import static android.provider.Telephony.Mms.Addr.CONTACT_ID;

/**
 * Created by y-pol on 1/11/2018.s
 */

public class ContactsCursor extends CursorWrapper {

    // The column index for the _ID column
    private static final String _ID = ContactsContract.Contacts._ID;
    private static final Uri contactPhoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    private ContentResolver contentResolver;


    public ContactsCursor(ContentResolver resolver, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        this(resolver.query(
                uri, //Uri
                projection, //String[]projection
                selection, //String selection
                selectionArgs, //String[] selectionArgs
                sortOrder //SortOrder
        ));

        contentResolver = resolver;


    }

    private ContactsCursor(Cursor cursor) {
        super(cursor);
    }

    ContactsCursor getContactsCursor() {

        String contactId = getStringContactId();
        String SELECTION = CONTACT_ID + "= ?";
        String[] selectionArgs = new String[]{contactId};

        return new ContactsCursor(contentResolver,
                contactPhoneContentUri,
                null,
                SELECTION,
                selectionArgs,
                null);
    }

    public String getPhoneNumber() {
        String phoneNumber = this.getString(this.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER));
        return validateNumber(phoneNumber);
    }

    String getDisplayName() {
        return this.getString(this.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }

    private String getStringContactId() {
        int columnIndexForId =
                this.getColumnIndex(_ID);
        return this.getString(columnIndexForId);
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
