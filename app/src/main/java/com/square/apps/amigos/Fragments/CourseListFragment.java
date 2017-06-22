package com.square.apps.amigos.Fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.db.DataProvider;


@SuppressWarnings("deprecation")
public class CourseListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * listener
     */
    @Nullable
    private Callbacks mCallbacks;

    /**
     * Cursor Adapter
     */
    private SimpleCursorAdapter courseAdapter;

    /**
     * Assign the Activity in the fragment lifeCycle
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
        Log.d("onAttached", "was called");
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
     //  Log.d("oncreate", "was called");
        final String[] projection = new String[]{
                Contract.SUBJECT_CRS,
                Contract.TITLE,
                Contract.TIME,
                Contract.PRIMARY_KEY,
        };

        return new CursorLoader(getActivity()
                , DataProvider.CONTENT_URI_COURSES
                , projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
    //    Log.d("onLoadfinished", "was called");
        courseAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        courseAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
  //      Log.d("onActivityCreated", "was called");
        setEmptyText("Loading Courses...");
        setListShown(false);
        setHasOptionsMenu(true);

        final String[] PROJECTION = new String[]{
                Contract.SUBJECT_CRS,
                Contract.TITLE,
                Contract.TIME,
        };

        final int[] TO = new int[]{
                R.id.textViewSubject,
                R.id.textViewTitle,
                R.id.textViewTime,
        };

        courseAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.course_list_fragment, null,
                PROJECTION,
                TO, 0);
        setListAdapter(courseAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = (Cursor) courseAdapter.getItem(position);
        if ((cursor.getCount() <= 0)) throw new AssertionError();
        //cursor.move(position);
        String CourseID = cursor.getString(cursor.getColumnIndex(DataProvider.COL_ID));
        mCallbacks.onCourseSelected(CourseID);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_course:
                mCallbacks.onAddCourse();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_course, menu);
    }

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onCourseSelected(String CourseID);

        void onAddCourse();
    }

}