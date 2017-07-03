package com.square.apps.amigos.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.square.apps.amigos.Activities.DetailActivity;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.course.Course;

import java.util.List;


public class SearchCourseResultsFragment extends Fragment {

    private List<Course> courses;

    //[START declare_RecyclerView]
    private RecyclerView mRecyclerView;
    //[END declare_RecyclerView]

    //[START declare_RecyclerView_Adapter]
    private CourseAdapter mAdapter;
    //[END declare_RecyclerView_Adapter]

    public SearchCourseResultsFragment(){
        // Required empty public constructor
    }

    public static SearchCourseResultsFragment newInstance(List<Course> courseList){
        SearchCourseResultsFragment fragment = new SearchCourseResultsFragment();
        fragment.setCourses(courseList);
        return fragment;
    }

    private void setCourses(List<Course> courses){
        this.courses = courses;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void setupAdapter(){
        if (isAdded()) {
            mRecyclerView.setAdapter(new CourseAdapter(courses));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.course_list_recycler_view, container, false);

        //[START initialize RecyclerView]
        mRecyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //[END initialize_RecyclerView]

        setupAdapter();
        return view;

    }


    /**
     * RecclerVIew.Adapter calls this class to create a viewHolder
     */
    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Course course;

        private TextView mTitleTextView;
        private TextView mSubtitleTextView;
        private TextView mDescriptionTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.course_list_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.course_title_tv);
            mSubtitleTextView = (TextView) itemView.findViewById(R.id.course_subtitle);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.course_description_tv);
        }

        public void bind(Course course){
            this.course = course;
            mTitleTextView.setText(course.getTitle());
            mSubtitleTextView.setText(course.getPrefix() + course.getNumber());
            mDescriptionTextView.setText("Tampa, the third-largest city in the U.S. state of Florida, is home to 127 completed high-rises,[1] 18 of which stand taller than 250 feet (76 m). ");
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getActivity(), course.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            Context context = view.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses){
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position){
            Course course = mCourses.get(position);
            holder.bind(course);
        }

        @Override
        public int getItemCount(){
            return mCourses.size();
        }
    }
}
