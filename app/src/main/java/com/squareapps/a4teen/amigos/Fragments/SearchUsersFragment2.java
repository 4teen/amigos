package com.squareapps.a4teen.amigos.Fragments;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.BuildConfig;
import com.squareapps.a4teen.amigos.Common.Utils.ImageLoader;
import com.squareapps.a4teen.amigos.Common.Utils.QueryPreferences;
import com.squareapps.a4teen.amigos.Common.Utils.Utils;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.ContactHolder;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchUsersFragment2 extends FragmentBase implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private final String TAG = FragmentBase.getSimpleName(this);

    private static ImageLoader mImageLoader; // Handles loading the contact image in a background thread


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static SearchUsersFragment2 newInstance(Bundle bunlde) {
        SearchUsersFragment2 fragment = new SearchUsersFragment2();
        fragment.setArguments(bunlde);
        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        QueryPreferences.setPrefSearchQuery(getContext(), null);

    }

    @Override
    public void onPause() {
        super.onPause();
        mImageLoader.setPauseWork(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        /*
         * An ImageLoader object loads and resizes an image in the background and binds it to the
         * QuickContactBadge in each item layout of the ListView. ImageLoader implements memory
         * caching for each image, which substantially improves refreshes of the ListView as the
         * user scrolls through it.
         *
         * To learn more about downloading images asynchronously and caching the results, read the
         * Android training class Displaying Bitmaps Efficiently.
         *
         * http://developer.android.com/training/displaying-bitmaps/
         */
        mImageLoader = new ImageLoader(getActivity(), getListPreferredItemHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                // This gets called in a background thread and passed the data from
                // ImageLoader.loadImage().
                Bitmap bitmap = loadContactPhotoThumbnail((String) data, getImageSize());
                if (BuildConfig.DEBUG && bitmap != null) {
                    Log.d(TAG, "Contact photo thumbnail bitmap is not null");
                } else if (bitmap == null) {
                    Log.d(TAG, "Contact photo thumbnail bitmap is null");
                }

                return bitmap;
            }
        };

        // Set a placeholder loading image for the image loader
        mImageLoader.setLoadingImage(R.drawable.ic_tag_faces_black_24dp);

        // Add a cache to the image loader
        mImageLoader.addImageCache(getFragmentManager(), 0.1f);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new ContactsAdapter(getContext(), null));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // Pause image loader to ensure smoother scrolling when flinging
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "MimageLoader Working...");
                    mImageLoader.setPauseWork(false);
                } else {
                    mImageLoader.setPauseWork(true);
                    Log.d(TAG, "MimageLoader Paused");
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialize the loader, and create a loader identified by ContactsQuery.QUERY_ID
        getLoaderManager().initLoader(ContactsQuery.QUERY_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Set listeners for SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Nothing needs to happen when the user submits the search string
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the action bar search text has changed.  Updates
                // the search filter, and restarts the loader to do a new query
                // using the new search string.
                String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
                String oldFilter = QueryPreferences.getPrefSearchQuery(getContext());

                // Don't do anything if the filter is empty
                if (oldFilter == null && newFilter == null) {
                    return true;
                }

                // Don't do anything if the new filter is the same as the current filter
                if (oldFilter != null && oldFilter.equals(newFilter)) {
                    return true;
                }

                QueryPreferences.setPrefSearchQuery(getContext(), newFilter);
                getLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, SearchUsersFragment2.this);
                return true;


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setPrefSearchQuery(getContext(), null);
                getLoaderManager().restartLoader(ContactsQuery.QUERY_ID, null, this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // If this is the loader for finding contacts in the Contacts Provider
        // (the only one supported)
        if (i == ContactsQuery.QUERY_ID) {
            Uri baseUri;
            String searchString = QueryPreferences.getPrefSearchQuery(getContext());

            // There are two types of searches, one which displays all contacts and
            // one which filters contacts by a search query. If mSearchTerm is set
            // then a search query has been entered and the latter should be used.
            if (searchString != null) {

                // Since there's no search string, use the content URI that searches the entire
                // Contacts table
                baseUri = Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode(searchString));
            } else {
                baseUri = ContactsQuery.CONTENT_URI;
            }

            // Returns a new CursorLoader for querying the Contacts table. No arguments are used
            // for the selection clause. The search string is either encoded onto the content URI,
            // or no contacts search string is used. The other search criteria are constants. See
            // the ContactsQuery interface.
            return new CursorLoader(getActivity(),
                    baseUri,
                    ContactsQuery.PROJECTION,
                    ContactsQuery.SELECTION,
                    null,
                    ContactsQuery.SORT_ORDER);
        }

        Log.e(TAG, "onCreateLoader - incorrect ID provided (" + i + ")");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ContactsAdapter contactsAdapter = (ContactsAdapter) recyclerView.getAdapter();
        contactsAdapter.changeCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ContactsQuery.QUERY_ID) {
            ContactsAdapter contactsAdapter = (ContactsAdapter) recyclerView.getAdapter();
            contactsAdapter.changeCursor(null);
        }
    }

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after accounting for
     * screen density. ImageLoader uses this value to resize thumbnail images to match the ListView
     * item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    private int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        getActivity().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new DisplayMetrics object
        final DisplayMetrics metrics = new android.util.DisplayMetrics();

        // Populate the DisplayMetrics
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }

    /**
     * Decodes and scales a contact's image from a file pointed to by a Uri in the contact's data,
     * and returns the result as a Bitmap. The column that contains the Uri varies according to the
     * platform version.
     *
     * @param photoData For platforms prior to Android 3.0, provide the Contact._ID column value.
     *                  For Android 3.0 and later, provide the Contact.PHOTO_THUMBNAIL_URI value.
     * @param imageSize The desired target width and height of the output image in pixels.
     * @return A Bitmap containing the contact's image, resized to fit the provided image size. If
     * no thumbnail exists, returns null.
     */
    private Bitmap loadContactPhotoThumbnail(String photoData, int imageSize) {

        // Ensures the Fragment is still added to an activity. As this method is called in a
        // background thread, there's the possibility the Fragment is no longer attached and
        // added to an activity. If so, no need to spend resources loading the contact photo.
        if (!isAdded() || getActivity() == null) {
            return null;
        }

        // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
        // ContentResolver can return an AssetFileDescriptor for the file.
        AssetFileDescriptor afd = null;

        // This "try" block catches an Exception if the file descriptor returned from the Contacts
        // Provider doesn't point to an existing file.
        try {
            Uri thumbUri;
            // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.

            thumbUri = Uri.parse(photoData);

            if (BuildConfig.DEBUG && thumbUri != null) {
                Log.d(TAG, "Contact photo thumbnail found for contact " + photoData + thumbUri);
            }

            // Retrieves a file descriptor from the Contacts Provider. To learn more about this
            // feature, read the reference documentation for
            // ContentResolver#openAssetFileDescriptor.
            afd = getActivity().getContentResolver().openAssetFileDescriptor(thumbUri, "r");

            // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
            // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
            assert afd != null;
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            if (BuildConfig.DEBUG && afd != null) {
                Log.d(TAG, "Contact photo thumbnail found for contact " + photoData + thumbUri);
            }

            if (fileDescriptor != null) {
                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                // to the specified width and height
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromDescriptor(
                        fileDescriptor, imageSize, imageSize);

                if (BuildConfig.DEBUG && bitmap == null) {
                    Log.d("Bitmap found for contact is null ", bitmap.toString());
                }

                return bitmap;
            }
        } catch (FileNotFoundException e) {
            // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
            // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
            // FileNotFoundException.
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Contact photo thumbnail not found for contact " + photoData
                        + ": " + e.toString());
            }
        } finally {
            // If an AssetFileDescriptor was returned, try to close it
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    // Closing a file descriptor might cause an IOException if the file is
                    // already closed. Nothing extra is needed to handle this.
                }
            }
        }

        // If the decoding failed, returns null
        return null;
    }

    @Override
    public void onClick(View view) {

    }


    private class ContactsAdapter extends RecyclerView.Adapter<ContactHolder> implements SectionIndexer {

        private Cursor mCursor;

        private boolean mDataValid;

        private int mRowIdColumn;

        private DataSetObserver mDataSetObserver;


        private AlphabetIndexer mAlphabetIndexer; // Stores the AlphabetIndexer instance
        private TextAppearanceSpan highlightTextSpan;

        ContactsAdapter(Context context, Cursor cursor) {

            mCursor = cursor;
            mDataValid = cursor != null;
            mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
            mDataSetObserver = new NotifyingDataSetObserver();
            if (mCursor != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }


            // Defines a span for highlighting the part of a display name that matches the search
            // string
            highlightTextSpan = new TextAppearanceSpan(getActivity(), R.style.searchTextHiglight);

            // Loads a string containing the English alphabet. To fully localize the app, provide a
            // strings.xml file in res/values-<x> directories, where <x> is a locale. In the file,
            // define a string with android:name="alphabet" and contents set to all of the
            // alphabetic characters in the language in their proper sort order, in upper case if
            // applicable.
            final String alphabet = getContext().getString(R.string.alphabet);

            // Instantiates a new AlphabetIndexer bound to the column used to sort contact names.
            // The cursor is left null, because it has not yet been retrieved.
            mAlphabetIndexer = new AlphabetIndexer(null, ContactsQuery.SORT_KEY, alphabet);
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = newItemView(parent, R.layout.contacts_list_item);
            itemView.setOnClickListener(SearchUsersFragment2.this);
            return new ContactHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }

            holder.bind(mCursor, mImageLoader, highlightTextSpan);
        }


        /**
         * An override of getCount that simplifies accessing the Cursor. If the Cursor is null,
         * getCount returns zero. As a result, no test for Cursor == null is needed.
         */
        @Override
        public int getItemCount() {
            if (mDataValid && mCursor != null) {
                return mCursor.getCount();
            }
            return 0;
        }


        @Override
        public void setHasStableIds(boolean hasStableIds) {
            super.setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIdColumn);
            }
            return 0;
        }

        /**
         * Defines the SectionIndexer.getSections() interface.
         */
        @Override
        public Object[] getSections() {
            return mAlphabetIndexer.getSections();
        }

        /**
         * Defines the SectionIndexer.getPositionForSection() interface.
         */
        @Override
        public int getPositionForSection(int i) {
            return getCursor() == null ? 0 : mAlphabetIndexer.getPositionForSection(i);
        }

        /**
         * Defines the SectionIndexer.getSectionForPosition() interface.
         */
        @Override
        public int getSectionForPosition(int i) {
            return getCursor() == null ? 0 : mAlphabetIndexer.getSectionForPosition(i);
        }

        Cursor getCursor() {
            return mCursor;
        }

        /**
         * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
         * closed.
         */
        void changeCursor(Cursor cursor) {
            Cursor old = swapCursor(cursor);
            if (old != null) {
                old.close();
            }
            // Update the AlphabetIndexer with new cursor as well
            mAlphabetIndexer.setCursor(cursor);
        }

        /**
         * Swap in a new Cursor, returning the old Cursor.  Unlike
         * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
         * closed.
         */
        Cursor swapCursor(Cursor newCursor) {
            if (newCursor == mCursor) {
                return null;
            }
            final Cursor oldCursor = mCursor;
            if (oldCursor != null && mDataSetObserver != null) {
                oldCursor.unregisterDataSetObserver(mDataSetObserver);
            }
            mCursor = newCursor;
            if (mCursor != null) {
                if (mDataSetObserver != null) {
                    mCursor.registerDataSetObserver(mDataSetObserver);
                }
                mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
                mDataValid = true;
                notifyDataSetChanged();
            } else {
                mRowIdColumn = -1;
                mDataValid = false;
                notifyDataSetChanged();
                //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
            }
            return oldCursor;
        }

        private class NotifyingDataSetObserver extends DataSetObserver {
            @Override
            public void onChanged() {
                super.onChanged();
                mDataValid = true;
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                mDataValid = false;
                notifyDataSetChanged();
                //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
            }
        }

    }

    /**
     * This interface defines constants for the Cursor and CursorLoader, based on constants defined
     * in the {@link android.provider.ContactsContract.Contacts} class.
     */
    public interface ContactsQuery {

        // An identifier for the loader
        int QUERY_ID = 1;

        // A content URI for the Contacts table
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        // The search/filter query Uri
        Uri FILTER_URI = ContactsContract.Contacts.CONTENT_FILTER_URI;

        // The selection clause for the CursorLoader query. The search criteria defined here
        // restrict results to contacts that have a display name and are linked to visible groups.
        // Notice that the search on the string provided by the user is implemented by appending
        // the search string to CONTENT_FILTER_URI.
        @SuppressLint("InlinedApi")
        String SELECTION =
                (Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME) +
                        "<>''" + " AND " + ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1";

        // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
        // sort key allows for localization. In earlier versions. use the display name as the sort
        // key.
        @SuppressLint("InlinedApi")
        String SORT_ORDER =
                Utils.hasHoneycomb() ? ContactsContract.Contacts.SORT_KEY_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

        // The projection for the CursorLoader query. This is a list of columns that the Contacts
        // Provider should return in the Cursor.
        @SuppressLint("InlinedApi")
        String[] PROJECTION = {

                // The contact's row id
                ContactsContract.Contacts._ID,

                // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                // a contact's current _ID value and LOOKUP_KEY, the Contacts Provider can generate
                // a "permanent" contact URI.
                ContactsContract.Contacts.LOOKUP_KEY,

                // In platform version 3.0 and later, the Contacts table contains
                // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                // some other useful identifier such as an email address. This column isn't
                // available in earlier versions of Android, so you must use Contacts.DISPLAY_NAME
                // instead.
                Utils.hasHoneycomb() ? ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME,

                // In Android 3.0 and later, the thumbnail image is pointed to by
                // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                // you generate the pointer from the contact's ID value and constants defined in
                // android.provider.ContactsContract.Contacts.
                Utils.hasHoneycomb() ? ContactsContract.Contacts.PHOTO_THUMBNAIL_URI : ContactsContract.Contacts._ID,

                // The sort order column for the returned Cursor, used by the AlphabetIndexer
                SORT_ORDER,
        };


        // The query column numbers which map to each value in the projection
        int ID = 0;
        int LOOKUP_KEY = 1;
        int DISPLAY_NAME = 2;
        int PHOTO_THUMBNAIL_DATA = 3;
        int SORT_KEY = 4;
    }
}