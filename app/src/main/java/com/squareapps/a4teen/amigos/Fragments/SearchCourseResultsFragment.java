package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Common.CourseFetchr;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static java.io.File.separator;


public class SearchCourseResultsFragment extends Fragment {

    public static final String COURSES = "courses";
    public static final String USERS = "users";
    @BindView(R.id.course_recycler_view)
    RecyclerView mRecyclerView;
    private List<Course> courses;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String courseCode;
    //[END declare_RecyclerView]
    private CourseAdapter courseAdapter;

    public static SearchCourseResultsFragment newInstance(List<Course> courseList) {
        SearchCourseResultsFragment fragment = new SearchCourseResultsFragment();
        fragment.setCourses(courseList);
        return fragment;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        courseAdapter = new CourseAdapter(courses);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.course_list_recycler_view, container, false);
        ButterKnife.bind(this, view);
        setupAdapter();
        return view;

    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(courseAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * RecclerVIew.Adapter calls this class to create a viewHolder
     */
    public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public static final String STUDENTS = "students";
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
            courseCode = course.getPrefix() + course.getNumber();
            mTitleTextView.setText(course.getTitle());
            mSubtitleTextView.setText(courseCode + "| " + course.getDepartment());
        }

        @Override
        public void onClick(View view) {
            databaseReference.updateChildren(course.courseSearchToMap());
            String[] strings = {course.getPrefix(), course.getNumber(), "", "", SearchFormFragment.DETAIL};
            new FetchItemsTask().execute(strings);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.course_list_item, parent, false);
            return new CourseHolder(v);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = mCourses.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<String, Void, List<Course>> {
        private String prefix, number;

        @Override
        protected List<Course> doInBackground(String... params) {
            prefix = params[0];
            number = params[1];
            return new CourseFetchr().fetchCourses(params[0], params[1], params[2], params[3], params[4]);
        }

        @Override
        protected void onPostExecute(List<Course> courses) {
            for (Course c : courses) {
                String courseCode = prefix + number;
                HashMap<String, Object> map = c.courseDetailtoMap(courseCode);
                map.put(USERS + separator + user.getUid() + separator + COURSES + separator + courseCode, true);
                map.put(STUDENTS + separator + courseCode + separator + user.getUid(), true);
                databaseReference.updateChildren(map);
                courseAdapter.notifyDataSetChanged();
            }
        }
    }

}
