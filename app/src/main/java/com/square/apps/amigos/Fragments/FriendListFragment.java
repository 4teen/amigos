package com.square.apps.amigos.Fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.db.DataProvider;


public class FriendListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * listener
     */
    @Nullable
    private Callbacks mCallbacks;

    /**
     * Cursor Adapter
     */
    private SimpleCursorAdapter friendsAdapter;

    @NonNull
    public static FriendListFragment newInstance(String courseID){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Contract.COURSE_ID, courseID);
        FriendListFragment friendListFragment = new FriendListFragment();
        friendListFragment.setArguments(bundle);
        return friendListFragment;
    }

    /**
     * Assign the Activity in the fragment lifeCycle
     */
    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    /**
     * unassigns the activity because after this point we can not count on the
     * the Activity to continue to exists
     **/
    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String[] PROJECTION = new String[]{Contract.NAME, Contract.EMAIL,};

        int[] TO = new int[]{R.id.main_list_item_text1, R.id.main_list_item_text2,};

        friendsAdapter = new SimpleCursorAdapter(getActivity(), R.layout.main_list_item, null, PROJECTION, TO, 0);

        setEmptyText("OHH Brotha, Talk to somebody...");
        setListShown(false);
        setListAdapter(friendsAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String courseID = (String) getArguments().getSerializable(Contract.COURSE_ID);

        final String[] PROJECTION = new String[]{Contract.EMAIL, Contract.NAME, Contract.COURSE_ID, Contract.PRIMARY_KEY,};

        return new CursorLoader(getActivity(), DataProvider.CONTENT_URI_FRIENDS, PROJECTION, Contract.COURSE_ID + " = ?", new String[]{courseID}, Contract.NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        friendsAdapter.swapCursor(data);
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        friendsAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Cursor cursor = (Cursor) friendsAdapter.getItem(position);
        if ((cursor.getCount() <= 0)) throw new AssertionError();
        String friendID = cursor.getString(cursor.getColumnIndex(Contract.PRIMARY_KEY));
        assert mCallbacks != null;
        mCallbacks.onFriendSelected(friendID);
    }


    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onFriendSelected(String friendID);
    }


}
