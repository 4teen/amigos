package com.square.apps.amigos.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.square.apps.amigos.AppControl;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.UpdateCountService;
import com.square.apps.amigos.common.common.db.DataProvider;


public class ChatFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {


    @Nullable
    private callBacks mListener;
    private SimpleCursorAdapter adapter;

    @NonNull
    public static ChatFragment newInstance(String friendID) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataProvider.COL_ID, friendID);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (callBacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.activity_chat_list_item,
                null,
                new String[]{DataProvider.COL_MSG, DataProvider.COL_AT},
                new int[]{R.id.chat_item_text1, R.id.chat_item_text2},
                0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(@NonNull View view, @NonNull Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.chat_item_text1:
                        LinearLayout root = (LinearLayout) view.getParent().getParent();
                        String email = null;
                        if (cursor.getCount() > 0) {
                            cursor.move(columnIndex);
                            email = cursor.getString(cursor.getColumnIndex(DataProvider.COL_FROM));
                        }

                        String myEmail = AppControl.getInstance(getActivity()).getUserEmail();

                        if (email != null && email.equals(myEmail)) {
                            root.setGravity(Gravity.RIGHT);
                            root.setPadding(50, 10, 10, 10);
                        } else {
                            root.setGravity(Gravity.LEFT);
                            root.setPadding(10, 10, 50, 10);
                        }
                        break;
                }
                return false;
            }
        });

        /*resets message count in database**/
        String friendID = (String) getArguments().getSerializable(DataProvider.COL_ID);
        UpdateCountService.startActionUpdateCount(getActivity(), friendID, "0");


        setEmptyText("no messages");
        Bundle args = new Bundle();
        args.putSerializable(DataProvider.COL_EMAIL, mListener.getFriendEmail());
        getLoaderManager().initLoader(0, args, this);
        setListAdapter(adapter);
        getListView().setStackFromBottom(true);
        getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, @NonNull Bundle args) {
        final String[] projection = new String[]{
                DataProvider.COL_MSG,
                DataProvider.COL_AT,
                DataProvider.COL_TO,
                DataProvider.COL_FROM,
                DataProvider.COL_ID,
        };

        String email = (String) args.getSerializable(DataProvider.COL_EMAIL);

        return new CursorLoader(getActivity()
                , DataProvider.CONTENT_URI_MESSAGES
                , projection, "col_from = '" + email + "' OR col_to = '" + email + "'", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void displayBackgroundImageOnEmpty(){
        ImageView imageview = new ImageView(getActivity());
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageview.setImageResource(R.mipmap.backgroung_material_desing);
        imageview.setAdjustViewBounds(true);
        imageview.setVisibility(View.GONE);

        ((ViewGroup)getListView().getParent()).addView(imageview);
        getListView().setEmptyView(imageview);
    }

    public interface callBacks {
        String getFriendEmail();
    }
}
