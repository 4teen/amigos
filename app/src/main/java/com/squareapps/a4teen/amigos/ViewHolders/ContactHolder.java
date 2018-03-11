package com.squareapps.a4teen.amigos.ViewHolders;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.HolderBase;
import com.squareapps.a4teen.amigos.Common.Utils.ImageLoader;
import com.squareapps.a4teen.amigos.Common.Utils.QueryPreferences;
import com.squareapps.a4teen.amigos.Fragments.SearchUsersFragment2;

import java.util.Locale;


public class ContactHolder extends HolderBase {

    private TextView text1;
    private TextView text2;
    private QuickContactBadge icon;


    public ContactHolder(View view) {
        super(view);
        text1 = itemView.findViewById(android.R.id.text1);
        text2 = itemView.findViewById(android.R.id.text2);
        icon = itemView.findViewById(android.R.id.icon);
    }


    public void bind(Cursor mcursor, ImageLoader mImageLoader, TextAppearanceSpan highlightTextSpan) {

        // For Android 3.0 and later, gets the thumbnail image Uri from the current Cursor row.
        // For platforms earlier than 3.0, this isn't necessary, because the thumbnail is
        // generated from the other fields in the row.

        int mThumbnailColumn =
                mcursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);

        final String photoUri = mcursor.getString(mThumbnailColumn);
        //final String photoUri = mcursor.getString(SearchUsersFragment2.ContactsQuery.PHOTO_THUMBNAIL_DATA);

        final String displayName = mcursor.getString(SearchUsersFragment2.ContactsQuery.DISPLAY_NAME);

        final int startIndex = indexOfSearchQuery(displayName);

        if (startIndex == -1) {
            // If the user didn't do a search, or the search string didn't match a display
            // name, show the display name without highlighting
            text1.setText(displayName);

            if (TextUtils.isEmpty(QueryPreferences.getPrefSearchQuery(itemView.getContext()))) {
                // If the search search is empty, hide the second line of text
                text2.setVisibility(View.GONE);
            } else {
                // Shows a second line of text that indicates the search string matched
                // something other than the display name
                text2.setVisibility(View.VISIBLE);
            }
        } else {
            // If the search string matched the display name, applies a SpannableString to
            // highlight the search string with the displayed display name

            // Wraps the display name in the SpannableString
            final SpannableString highlightedName = new SpannableString(displayName);

            // Sets the span to start at the starting point of the match and end at "length"
            // characters beyond the starting point
            highlightedName.setSpan(highlightTextSpan, startIndex,
                    startIndex + QueryPreferences.getPrefSearchQuery(itemView.getContext()).length(), 0);

            // Binds the SpannableString to the display name View object
            text1.setText(highlightedName);

            // Since the search string matched the name, this hides the secondary message
            text2.setVisibility(View.GONE);
        }

        // Processes the QuickContactBadge. A QuickContactBadge first appears as a contact's
        // thumbnail image with styling that indicates it can be touched for additional
        // information. When the user clicks the image, the badge expands into a dialog box
        // containing the contact's details and icons for the built-in apps that can handle
        // each detail type.

        // Generates the contact lookup Uri
        final Uri contactUri = ContactsContract.Contacts.getLookupUri(
                mcursor.getLong(SearchUsersFragment2.ContactsQuery.ID),
                mcursor.getString(SearchUsersFragment2.ContactsQuery.LOOKUP_KEY));

        // Binds the contact's lookup Uri to the QuickContactBadge
        icon.assignContactUri(contactUri);

        // Loads the thumbnail image pointed to by photoUri into the QuickContactBadge in a
        // background worker thread
        mImageLoader.loadImage(photoUri, icon);
    }

    /**
     * Identifies the start of the search string in the display name column of a Cursor row.
     * E.g. If displayName was "Adam" and search query (mSearchTerm) was "da" this would
     * return 1.
     *
     * @param displayName The contact display name.
     * @return The starting position of the search string in the display name, 0-based. The
     * method returns -1 if the string is not found in the display name, or if the search
     * string is empty or null.
     */
    private int indexOfSearchQuery(String displayName) {

        if (!TextUtils.isEmpty(QueryPreferences.getPrefSearchQuery(itemView.getContext()))) {

            return displayName.toLowerCase(Locale.getDefault()).indexOf(

                    QueryPreferences
                            .getPrefSearchQuery(itemView.getContext())
                            .toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

}
