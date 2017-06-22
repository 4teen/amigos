package com.square.apps.amigos.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.db.DataProvider;


@SuppressWarnings("deprecation")
public class FriendRequestFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_COURSE_ID = "null";

    /**
     * private TAG for Log purposes
     **/
    private static final String TAG = "CourseListFragment";

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
    public void onAttach(Activity activity) {
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

    @Nullable
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] PROJECTION = new String[]{
                Contract.NAME,
                Contract.PRIMARY_KEY,
        };

        return new CursorLoader(getActivity()
                , DataProvider.CONTENT_URI_FRIENDREQUESTS
                , PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        friendsAdapter.swapCursor(data);
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        friendsAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Nope, nobody likes you");
        setListShown(false);

        Log.d("okey", "sending the friendRequest now");


        friendsAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.main_list_item, null,
                new String[]{Contract.NAME},
                new int[]{R.id.main_list_item_text1}, 0);
        setListAdapter(friendsAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = (Cursor) friendsAdapter.getItem(position); //((CourseAdapter) getListAdapter()).getItem(position);
        if ((cursor.getCount() <= 0)) throw new AssertionError();
        String friendID = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ID));
        mCallbacks.onFriendRequestSelected(friendID);
    }

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onFriendRequestSelected(String friendID);
    }


}
