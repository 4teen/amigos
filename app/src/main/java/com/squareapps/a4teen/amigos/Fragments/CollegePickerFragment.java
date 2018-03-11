package com.squareapps.a4teen.amigos.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Common.POJOS.School;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.SchoolHolder;

import java.util.ArrayList;
import java.util.List;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COLLEGE;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;

/**
 * Created by y-pol on 1/7/2018.s
 */

public class CollegePickerFragment extends FragmentBase implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<SchoolHolder> adapter;
    private List<School> schoolList = new ArrayList<>();
    private SearchView searchView;
    private Toolbar toolbar;
   /* private Client client;
    private Index index;
    private Query query;*/

    public static CollegePickerFragment newInstance(Bundle bundle) {

        CollegePickerFragment fragment = new CollegePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getActivity().getIntent());
        setHasOptionsMenu(true);

       /* client = new Client(getString(R.string.ALGOLIA_APP_ID), getString(R.string.ALGOLIA_SEARCH_API_KEY));
        index = client.getIndex(getString(R.string.ALGOLIA_INDEX_NAME));

        // Pre-build query.

        query = new Query();

        query.setAttributesToRetrieve("Campus_Address", "Campus_City", "Campus_State", "Campus_Zip",
                "Institution_Address", "Institution_City", "Institution_Name", "Institution_Phone",
                "Institution_State", "Institution_Web_Address", "Institution_Zip", "Campus_Name");

        query.setAttributesToHighlight("Campus_City", "Institution_Name", "Institution_City", "Campus_Name");

        query.setHitsPerPage(10);*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_tool_bar, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recycler_view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        adapter = newAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search("");//initial query

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_college_picker, menu);
        // Configure search view.

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setImeOptions(IME_ACTION_SEARCH);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return true;
            }
        });
    }

    public RecyclerView.Adapter<SchoolHolder> newAdapter() {

        return new RecyclerView.Adapter<SchoolHolder>() {
            @Override
            public SchoolHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = newItemView(parent, R.layout.school_list_item);
                view.setOnClickListener(CollegePickerFragment.this);
                return new SchoolHolder(view);
            }

            @Override
            public void onBindViewHolder(SchoolHolder holder, int position) {
                School school = schoolList.get(position);
                holder.getBinding().setSchool(school);
                holder.getBinding().executePendingBindings();
            }

            @Override
            public int getItemCount() {
                return schoolList.size();
            }
        };


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void search(String mquery) {

     /*   query.setQuery(mquery);
        Log.d("search Query", mquery);

        index.searchAsync(query, new CompletionHandler() {

            @Override

            public void requestCompleted(JSONObject content, AlgoliaException error) {

                if (content != null && error == null) {
                    JSONArray jsonArray = new JSONArray();

                    try {
                        jsonArray = content.getJSONArray("hits");

                    } catch (JSONException je) {
                        Log.d("Json Execption", je.getMessage());
                    }


                    GsonBuilder gsonBuilder = new GsonBuilder();

                    Gson gson = gsonBuilder.create();

                    Type SchoolListType = new TypeToken<List<School>>() {
                    }.getType();

                    Log.d("results", content.toString());

                    schoolList = gson.fromJson(jsonArray.toString(), SchoolListType);

                    if (adapter != null)
                        adapter.notifyDataSetChanged();

                    // Scroll the list back to the top.

                    recyclerView.smoothScrollToPosition(0);

                }

            }

        });
*/
    }

    public void handleIntent(Intent intent) {
    /*    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.d("new Search", "processing" + query);
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }*/
    }

    @Override
    public void onClick(View view) {
        School school = schoolList.get(recyclerView.getChildAdapterPosition(view));
        getDataRef().child(USERS)
                .child(getUid())
                .child(COLLEGE)
                .setValue(school);
        getActivity().finish();

        /*TextView view1 = new TextView(getActivity());
        view1.setBackground(getResources().getDrawable(R.drawable.red_circle));
        view1.setText(school.getInstitution_Name());
        searchView.addView(view1);
        searchView.*/

    }
}
