package com.squareapps.a4teen.amigos.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Common.Contract;
import com.squareapps.a4teen.amigos.Fragments.CourseListFragment;
import com.squareapps.a4teen.amigos.Fragments.SearchFormFragment;

public class MainActivity extends SingleFragmentActivity implements CourseListFragment.Callbacks {


    @NonNull
    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance(null);
    }

    @Override
    public void onCourseSelected(String CourseID) {
        initDetailActivity(CourseID);
    }

    @Override
    public void onAddCourse() {
        initSearchFormActivity();
    }

    void initDetailActivity(String CourseID) {
        Intent intent = new Intent(null, DetailActivity.class);
        intent.putExtra(Contract.COURSE_ID, CourseID);
        startActivity(intent);
    }

     void initSearchFormActivity() {
        Intent searchForm = new Intent(this, SearchFormActivity.class);
        searchForm.setAction(SearchFormFragment.SEARCH);
        startActivity(searchForm);
    }// Add Fragments to Tabs

}
