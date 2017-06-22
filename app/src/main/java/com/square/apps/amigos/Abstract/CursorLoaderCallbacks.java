package com.square.apps.amigos.Abstract;


import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.SimpleCursorAdapter;

/**
 * Created by YOEL on 11/29/2015.
 */
@SuppressWarnings("ALL")
public abstract class CursorLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {


    private final Context context;
    private final Uri CONTENT_URI;
    private final SimpleCursorAdapter listAdpater;
    @Nullable
    private final String[] projection;
    @Nullable
    private final String[] args;
    @Nullable
    private final String selection;
    @Nullable
    private final String sort;

    public CursorLoaderCallbacks(Context context, Uri CONTENT_URI, SimpleCursorAdapter listAdapter) {
        this.CONTENT_URI = CONTENT_URI;
        this.listAdpater = listAdapter;
        this.context = context;
        Bundle bundle = getParams();
        projection = bundle.getStringArray("projection");
        args = bundle.getStringArray("args");
        selection = bundle.getString("selection");
        sort = bundle.getString("sort");
    }

    @NonNull
    protected abstract Bundle getParams();

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] PROJECTION = (this.projection == null ? null : projection);
        String SELECTION = (this.selection== null ? null : selection);
        String[] ARGS = (this.args== null ? null : this.args);
        String SORT = (this.sort== null ? null : sort);

        CursorLoader cursorLoader = new CursorLoader(context
                , CONTENT_URI
                , PROJECTION, SELECTION, ARGS, SORT);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdpater.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdpater.swapCursor(null);
    }
}
