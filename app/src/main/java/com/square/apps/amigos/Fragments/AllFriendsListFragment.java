package com.square.apps.amigos.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.Camera.PictureUtils;
import com.square.apps.amigos.common.common.db.DataProvider;


public class AllFriendsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * listener
     */
    @Nullable
    private Callbacks mCallbacks;
    /**
     * Cursor Adapter
     */
    private SimpleCursorAdapter friendsAdapter;

    /**
     * Assign the Activity in the fragment lifeCycle
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    /**
     * unassigns the activity because after this point we can not count on the
     * the Activity to continue to exists
     **/
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] PROJECTION = new String[]{
                Contract.NAME,
                Contract.EMAIL,
                Contract.COUNT,
                Contract.PROFILE_IMAGE,

        };

        int[] TO = new int[]{
                R.id.main_list_item_text1,
                R.id.main_list_item_text2,
                R.id.messages_count,
                R.id.avatar,
        };

        friendsAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.main_list_item, null,
                PROJECTION,
                TO, 0);

        friendsAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(@NonNull View view, @NonNull Cursor cursor, int columnIndex) {
                String cursorValue = cursor.getString(columnIndex);
                TextView tempView;
                if (cursorValue == null)
                    return false;

                switch (view.getId()) {
                    case R.id.avatar:
                        ImageView img = (ImageView) view.findViewById(R.id.avatar);
                        Bitmap image = PictureUtils.decodeBase64(cursorValue);
                        img.setImageBitmap(image);
                        break;

                    case R.id.main_list_item_text1:
                        tempView = (TextView) view.findViewById(R.id.main_list_item_text1);
                        tempView.setText(cursorValue);
                        break;

                    case R.id.main_list_item_text2:
                        tempView = (TextView) view.findViewById(R.id.main_list_item_text2);
                        tempView.setText(cursorValue);
                        break;

                    case R.id.messages_count:
                        tempView = (TextView) view.findViewById(R.id.messages_count);
                        int count = cursor.getInt(columnIndex);
                        if (count == 0)
                            tempView.setVisibility(TextView.INVISIBLE);
                        else {
                            tempView.setVisibility(TextView.VISIBLE);
                            tempView.setText(Integer.toString(count));
                        }
                        break;
                }
                return true;
            }
        });


        setEmptyText("OHH Brotha, Talk to somebody...");
        setListShown(false);
        setHasOptionsMenu(true);
        setListAdapter(friendsAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] projection = new String[]{
                Contract.NAME,
                Contract.EMAIL,
                Contract.PROFILE_IMAGE,
                Contract.COUNT,
                Contract.PRIMARY_KEY,
        };

        return new CursorLoader(getActivity()
                , DataProvider.CONTENT_URI_ALL_FRIENDS
                , projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        friendsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        friendsAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = (Cursor) friendsAdapter.getItem(position);
        String friendID = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ID));
        mCallbacks.onListFriendSelected(friendID);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_friends:
                mCallbacks.onAddFriend("t");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_all_friends, menu);
    }

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onListFriendSelected(String friendID);
        void onAddFriend(String friendID);
    }

}
