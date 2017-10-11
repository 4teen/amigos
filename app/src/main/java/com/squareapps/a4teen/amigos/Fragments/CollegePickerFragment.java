package com.squareapps.a4teen.amigos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ProfileActivity;
import com.squareapps.a4teen.amigos.Common.Objects.School;
import com.squareapps.a4teen.amigos.Common.QueryPreferences;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.SchoolHolder;

import static com.firebase.ui.auth.ResultCodes.OK;
import static com.squareapps.a4teen.amigos.Common.Contract.COLLEGE;
import static com.squareapps.a4teen.amigos.Common.Contract.SCHOOLS;


public class CollegePickerFragment extends FragmentBase implements SchoolHolder.Callback {


    private static final String TAG = "College picker fragment";

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FirebaseRecyclerAdapter<School, SchoolHolder> adapter;
    private ProgressBar progressBar;


    public static CollegePickerFragment newInstance(Bundle bundle) {

        CollegePickerFragment fragment = new CollegePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.cleanup();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        // setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        adapter = getFirebaseRecyclerAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    private FirebaseRecyclerAdapter<School, SchoolHolder> getFirebaseRecyclerAdapter() {
        String query = QueryPreferences.getPrefSearchQuery(getContext());
        if (query == null) {
            return null;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            return new FirebaseRecyclerAdapter<School, SchoolHolder>(School.class,
                    R.layout.course_list_item,
                    SchoolHolder.class,
                    getDataRef().child(SCHOOLS).orderByChild("Institution_Name")
                            .startAt(query)
                            .endAt(query + "\uf8ff")
                            .limitToFirst(10)) {
                @Override
                protected void populateViewHolder(SchoolHolder viewHolder, School model, int position) {
                    viewHolder.bind(model);
                }

                @Override
                protected void onDataChanged() {
                    if (progressBar != null && progressBar.isShown())
                        progressBar.setVisibility(View.GONE);
                }



                @Override
                public SchoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    View itemView = inflater.inflate(R.layout.course_list_item, parent, false);
                    return new SchoolHolder(itemView, CollegePickerFragment.this);

                }
            };
        }
    }


    @Override
    public void onItemClicked(View itemView, int pos) {
        TextView schoolName = (TextView) itemView.findViewById(R.id.course_list_item_text1);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(COLLEGE, schoolName.getText());
        getActivity().setResult(OK, intent);
        getActivity().finish();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.school_search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        searchView.setIconified(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "QuerryTextSubmit: " + query);
                QueryPreferences.setPrefSearchQuery(getContext(), query);
                updateSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "QuerryTextChanged: " + newText);
                if (!newText.isEmpty()) {
                    QueryPreferences.setPrefSearchQuery(getContext(), newText);
                    updateSearch();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setPrefSearchQuery(getContext(), null);
                updateSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void updateSearch() {
        adapter = getFirebaseRecyclerAdapter();
        recyclerView.swapAdapter(adapter, true);

    }
}
