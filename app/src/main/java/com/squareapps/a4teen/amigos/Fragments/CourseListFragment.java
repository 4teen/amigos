package com.squareapps.a4teen.amigos.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.Activities.SearchFormActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.CourseListHolder;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;
import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class CourseListFragment extends FragmentBase implements CourseListHolder.Callbacks {

    private FirebaseRecyclerAdapter<Course, CourseListHolder> adapter;

    // Set up FirebaseRecyclerAdapter with the Querys
    private static final Query keysRef = getDataRef().child(USERS).child(getUid()).child(COURSES);
    private static final DatabaseReference dataRef = getDataRef().child(COURSES);

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        if (getUser() == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_list_fragment, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerForContextMenu(mRecyclerView);

        setupAdapter();

        return view;
    }

    @NonNull
    private FirebaseRecyclerAdapter<Course, CourseListHolder> newAdapter() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Course>()
                .setIndexedQuery(keysRef, dataRef, Course.class)
                .setLifecycleOwner((MainActivity) getActivity())
                .build();

        adapter = new FirebaseRecyclerAdapter<Course, CourseListHolder>(options) {
            @Override
            protected void onBindViewHolder(CourseListHolder holder, int position, Course model) {
                holder.bind(model);
            }

            @Override
            public CourseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CourseListHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.course_list_item, parent, false), CourseListFragment.this);
            }

            @Override
            public void onDataChanged() {
                if (progressBar != null && progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
            }
        };
        return adapter;
    }

    private void setupAdapter() {
        if (isAdded()) {
            progressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(newAdapter());
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_course:
                addCourse();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Course course = adapter.getItem(item.getOrder());
        switch (item.getItemId()) {
            case R.id.delete:
                deleteCourse(course);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void addCourse() {
        Intent searchForm = new Intent(getActivity(), SearchFormActivity.class);
        searchForm.setAction(SearchFormFragment.SEARCH);
        startActivity(searchForm);
    }


    private void deleteCourse(Course course) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(USERS + separator + getUid() + separator + COURSES + separator + course.getCode(), null);
        map.put(STUDENTS + separator + course.getCode() + separator + getUid(), null);
        getDataRef().updateChildren(map);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        menu.add(0, R.id.add_course, 2, "Add new course");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemSelected(int pos, View itemView) {
        //   adapter.toggleSelection(pos);
        //   itemView.setActivated(adapter.isSelected(pos));
    }


}

