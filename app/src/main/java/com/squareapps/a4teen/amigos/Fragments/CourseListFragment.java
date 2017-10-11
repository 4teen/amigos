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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareapps.a4teen.amigos.Abstract.FireBaseIndexRecyclerAdapterSelector;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
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

    //[START declare_RecyclerView]
    @BindView(R.id.course_recycler_view)
    RecyclerView mRecyclerView;
    //[END declare_RecyclerView]

    private FireBaseIndexRecyclerAdapterSelector<Course, CourseListHolder> adapter;

    // FirebaseRecyclerAdapter<Course, CourseListHolder> recyclerAdapter;

    private FirebaseUser user;

    private DatabaseReference databaseReference;


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        }

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_list_recycler_view, container, false);
        ButterKnife.bind(this, view);

        //[START initialize RecyclerView]
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //[END initialize_RecyclerView]

        adapter = new FireBaseIndexRecyclerAdapterSelector<Course, CourseListHolder>(
                Course.class,
                R.layout.course_list_item,
                CourseListHolder.class,
                databaseReference.child(USERS).child(user.getUid()).child(COURSES),
                databaseReference.child(COURSES)
        ) {
            @Override
            protected void populateViewHolder(CourseListHolder viewHolder, Course model, int position) {
                viewHolder.bind(model);
            }

            @Override
            public CourseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
                return new CourseListHolder(itemView, CourseListFragment.this);
            }
        };

        setupAdapter();
        registerForContextMenu(mRecyclerView);

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(adapter);
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

