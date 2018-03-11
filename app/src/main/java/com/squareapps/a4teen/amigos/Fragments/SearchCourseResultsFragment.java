package com.squareapps.a4teen.amigos.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.CourseDetailActivity;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.Services.DowloadService;
import com.squareapps.a4teen.amigos.ViewHolders.CourseListHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_CODE;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.CREDITS;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.PREFIX;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.TITLE;
import static com.squareapps.a4teen.amigos.Common.Contract.OUTPUT;


public class SearchCourseResultsFragment extends FragmentBase implements View.OnClickListener {
    private final String TAG = getSimpleName(this);
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private List<Course> courses;
    private CourseAdapter courseAdapter;
    private BroadcastReceiver mBroadcastReceiver;

    public static SearchCourseResultsFragment newInstance(List<Course> courseList) {
        SearchCourseResultsFragment fragment = new SearchCourseResultsFragment();
        fragment.setCourses(courseList);
        return fragment;
    }

    private void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mBroadcastReceiver, DowloadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        courseAdapter = new CourseAdapter(courses);

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //    hideProgressDialog();

                switch (intent.getAction()) {
                    case DowloadService.DOWNLOAD_COMPLETED:
                    case DowloadService.DOWNLOAD_ERROR:
                        onDownloadResultIntent(intent);
                        break;
                }
            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recycler_view, container, false);
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
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        Course course = courseAdapter.getItem(position);

        getDataRef().updateChildren(course.courseSearchToMap());

        Bundle bundle = new Bundle();
        bundle.putString(COURSE_CODE, course.getCode());
        bundle.putString(PREFIX, course.getPrefix());
        bundle.putString(NUMBER, course.getNumber());
        bundle.putString(CREDITS, "");
        bundle.putString(TITLE, "");
        bundle.putString(OUTPUT, SearchFormFragment.DETAIL);

        DowloadService.startActionDownload(getContext(), bundle);

    }

    private void onDownloadResultIntent(Intent intent) {
        Intent courseDetailIntent = new Intent(getContext(), CourseDetailActivity.class);
        courseDetailIntent.setAction("Search");
        courseDetailIntent.putExtras(intent.getExtras());
        startActivity(courseDetailIntent);
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseListHolder> {

        private List<Course> mCourses;

        CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = newItemView(parent, R.layout.course_list_item);
            itemView.setOnClickListener(SearchCourseResultsFragment.this);
            return new CourseListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CourseListHolder holder, int position) {
            // holder.itemView.setActivated(isSelected(position));
            Course course = mCourses.get(position);
            holder.getBinding().setCourse(course);
            holder.getBinding().executePendingBindings();
            // holder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

        public Course getItem(int position) {
            return mCourses.get(position);
        }


    }

}


