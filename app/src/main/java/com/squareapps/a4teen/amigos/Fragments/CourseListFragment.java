package com.squareapps.a4teen.amigos.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.CourseDetailActivity;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.Activities.SearchFormActivity;
import com.squareapps.a4teen.amigos.BR;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.CourseListHolder;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_CODE;
import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static java.io.File.separator;


public class CourseListFragment extends FragmentBase implements View.OnClickListener {

    @BindView(R.id.recycler_view)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private FirebaseRecyclerAdapter<Course, CourseListHolder> adapter;

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
        View view = inflater.inflate(R.layout.empty_recycler_view, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);


        mRecyclerView.addItemDecoration(itemDecoration);

        registerForContextMenu(mRecyclerView);

        setupAdapter();

        return view;
    }

    @NonNull
    private FirebaseRecyclerAdapter<Course, CourseListHolder> newAdapter() {

        // Set up FirebaseRecyclerAdapter with the Querys
        final Query keysRef = getDataRef().child(USERS).child(getUid()).child(COURSES);
        final DatabaseReference dataRef = getDataRef().child(COURSES);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Course>()
                .setIndexedQuery(keysRef, dataRef, Course.class)
                .setLifecycleOwner((MainActivity) getActivity())
                .build();

        adapter = new FirebaseRecyclerAdapter<Course, CourseListHolder>(options) {
            @Override
            protected void onBindViewHolder(CourseListHolder holder, int position, Course model) {
                holder.getBinding().setVariable(BR.course, model);
                holder.getBinding().executePendingBindings();
            }

            @Override
            public CourseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = newItemView(parent, R.layout.course_list_item);
                itemView.setOnClickListener(CourseListFragment.this);
                return new CourseListHolder(itemView);
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
            emptyView.setText("No courses");
            mRecyclerView.setEmptyView(emptyView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        menu.add(0, R.id.add_course, 2, "Add new course");
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onClick(View v) {
        // CourseListHolder holder = (CourseListHolder) mRecyclerView.findContainingViewHolder(v);
        Course course = ((FirebaseRecyclerAdapter<Course, CourseListHolder>) mRecyclerView.getAdapter()).getItem(mRecyclerView.getChildAdapterPosition(v));
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
        intent.putExtra(COURSE_CODE, course.getCode());
        startActivity(intent);
    }
}

