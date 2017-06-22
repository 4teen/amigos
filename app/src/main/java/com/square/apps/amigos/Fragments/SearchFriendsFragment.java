package com.square.apps.amigos.Fragments;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.db.DataProvider;
import com.square.apps.amigos.common.common.db.MySuggestionProvider;


public class SearchFriendsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Spinner spinner;
    private SimpleCursorAdapter tempFriendAdapter;
    @Nullable
    private Callbacks mListener;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mListener = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("friendsSummary onCreate", "was called");

        return new CursorLoader(getActivity()
                , DataProvider.CONTENT_URI_FRIENDS_BUFFER
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tempFriendAdapter.swapCursor(data);
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tempFriendAdapter.swapCursor(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Search Friends");

        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getActivity().getIntent() != null) {
            handleIntent(getActivity().getIntent());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Loading Friends");
        setListShown(false);

        String[] PROJECTION = new String[]{
                Contract.NAME,
                Contract.EMAIL,
                Contract.PRIMARY_KEY,
        };
        int[] TO = new int[]{
                android.R.id.text1,
                android.R.id.text2,
        };
        tempFriendAdapter = new android.support.v4.widget.SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null,
                PROJECTION,
                TO, 0);
        setListAdapter(tempFriendAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_friend_menu, menu);
        // Associate searchable configuration with the SearchView
        MenuItem spinnerMenuItem = menu.findItem(R.id.menu_spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(spinnerMenuItem);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Search_friends_spinner_list, R.layout.spinner_item);

        spinner.setAdapter(spinnerAdapter);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchViewAction = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchViewAction.setSearchableInfo(searchableInfo);
        searchViewAction.setQueryRefinementEnabled(true);

    }

    public void handleIntent(@NonNull Intent intent) {

        /*adding search to the suggestions**/
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

        getActivity().getContentResolver().delete(DataProvider.CONTENT_URI_FRIENDS_BUFFER, null, null);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchValue = intent.getStringExtra(SearchManager.QUERY);
            String searchKey = spinner.getSelectedItem().toString();

            Bundle bundle = new Bundle();
            bundle.putString("searchValue", searchValue);
            bundle.putString("searchKey", searchKey);

           //SearchIntent.putExtras(bundle);
           // getActivity().startService(SearchIntent);

            getLoaderManager().restartLoader(0, null, this);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) tempFriendAdapter.getItem(position);
        if ((cursor.getCount() <= 0)) throw new AssertionError();
        String CourseID = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ID));
        mListener.onSelectedFriend(CourseID);
    }

    /**
     * Required interface for hosting activities.
     */

    public interface Callbacks{
        void onSelectedFriend(String FriendID);
    }

}
