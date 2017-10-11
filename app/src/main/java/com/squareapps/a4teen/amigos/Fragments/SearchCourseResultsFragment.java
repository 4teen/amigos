package com.squareapps.a4teen.amigos.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Abstract.RecyclerAdapterSelector;
import com.squareapps.a4teen.amigos.Common.CourseFetchr;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.ViewHolders.CourseHolder2;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.STUDENTS;
import static java.io.File.separator;


public class SearchCourseResultsFragment extends FragmentBase implements CourseHolder2.Callbacks {

    public static final String COURSES = "courses";
    public static final String USERS = "users";
    @BindView(R.id.course_recycler_view)
    RecyclerView mRecyclerView;
    private List<Course> courses;
    private DatabaseReference databaseReference;
    private CourseAdapter courseAdapter;


    public static SearchCourseResultsFragment newInstance(List<Course> courseList) {
        SearchCourseResultsFragment fragment = new SearchCourseResultsFragment();
        fragment.setCourses(courseList);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        databaseReference = getDataRef();
        courseAdapter = new CourseAdapter(courses);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        courseAdapter.clearSelections();
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

    private void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public void onItemSelected(int pos, Course course, View itemView) {
        courseAdapter.toggleSelection(pos);
        boolean selected = courseAdapter.isSelected(pos);
        itemView.setActivated(selected);
        CourseHolder2 courseHolder2 = (CourseHolder2) mRecyclerView.findContainingViewHolder(itemView);
        if (!itemView.isActivated() && courseAdapter.getSelectedItemCount() <= 0) {
            courseHolder2.dismissActionBar();
        }
        if (itemView.isActivated() || courseAdapter.getSelectedItemCount() >= 1)
            courseHolder2.startActionMode();


    }

    @Override
    public void onAddItem(Course course) {
        getDataRef().updateChildren(course.courseSearchToMap());
        String[] strings = {course.getPrefix(), course.getNumber(), "", "", SearchFormFragment.DETAIL};
        new FetchItemsTask().execute(strings);
        getActivity().finish();

    }

    @Override
    public void onDestoryActionBar() {
        if (courseAdapter.getItemCount() > 0)
            for (Integer i : courseAdapter.getSelectedItems()) {
                CourseHolder2 ch2 = (CourseHolder2) mRecyclerView.findViewHolderForAdapterPosition(i);
                ch2.itemView.setActivated(false);
            }
        courseAdapter.clearSelections();

    }

    @Override
    public void updateActionModeCounter(ActionMode mode, Menu menu) {
        if (mode != null) {
            MenuItem menuItem = menu.findItem(R.id.menu_counter);
            menuItem.setActionView(R.layout.menu_counter);
            TextView textView = (TextView) menuItem.getActionView().findViewById(R.id.counter);
            textView.setText(String.valueOf(courseAdapter.getSelectedItemCount()));

        }
    }

    private class CourseAdapter extends RecyclerAdapterSelector<Course, CourseHolder2> {

        private List<Course> mCourses;

        CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.course_list_item, parent, false);
            return new CourseHolder2(v, SearchCourseResultsFragment.this);
        }

        @Override
        public void onBindViewHolder(CourseHolder2 holder, int position) {
            holder.itemView.setActivated(isSelected(position));
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
                map.put(USERS + separator + getUid() + separator + COURSES + separator + courseCode, true);
                map.put(STUDENTS + separator + courseCode + separator + getUid(), true);
                databaseReference.updateChildren(map);
                courseAdapter.notifyDataSetChanged();
            }
        }
    }

}
