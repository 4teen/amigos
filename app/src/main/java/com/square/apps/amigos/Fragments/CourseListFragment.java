package com.square.apps.amigos.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.square.apps.amigos.Activities.DetailActivity;
import com.square.apps.amigos.Activities.LoginActivity;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.Course;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.square.apps.amigos.Contract.COURSES;
import static com.square.apps.amigos.Contract.USERS;


public class CourseListFragment extends Fragment {

    //[START declare_RecyclerView]
    @BindView(R.id.course_recycler_view)
    RecyclerView mRecyclerView;
    //[END declare_RecyclerView]

    FirebaseRecyclerAdapter<Course, CourseHolder> recyclerAdapter;

    private FirebaseUser user;

    private DatabaseReference databaseReference;

    /**
     * listener
     */
    @Nullable
    private Callbacks mCallbacks;


    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
    }

    /**
     * Assign the Activity in the fragment lifeCycle
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_list_recycler_view, container, false);
        ButterKnife.bind(this, view);

        //[START initialize RecyclerView]
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //[END initialize_RecyclerView]

        recyclerAdapter = new FirebaseIndexRecyclerAdapter<Course, CourseHolder>(
                Course.class,
                R.layout.course_list_item,
                CourseHolder.class,
                databaseReference.child(USERS).child(user.getUid()).child(COURSES),
                databaseReference.child(COURSES)
        ) {
            @Override
            protected void populateViewHolder(CourseHolder viewHolder, Course model, int position) {
                viewHolder.bind(model);
            }
        };

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(recyclerAdapter);
        }
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
        menu.add(0, R.id.add_course, 2, "Add new course");
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onCourseSelected(String CourseID);

        void onAddCourse();
    }

    /**
     * RecclerVIew.Adapter calls this class to create a viewHolder
     */
    public static class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.course_list_item_text1)
        TextView mTitleTextView;
        @BindView(R.id.course_list_item_text2)
        TextView mSubtitleTextView;
        private Course course;

        public CourseHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Course course) {
            this.course = course;
            mTitleTextView.setText(course.getTitle());
            mSubtitleTextView.setText(course.getPrefix() + course.getNumber() + "| " + course.getDepartment());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            String courseCode = course.getPrefix() + course.getNumber();

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.COURSEID, courseCode);
            context.startActivity(intent);
        }
    }

}