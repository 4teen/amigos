package com.square.apps.amigos.Activities;


import android.app.FragmentManager;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.SearchCourseFragment;
import com.square.apps.amigos.Fragments.SearchCourseResultsFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.course.Course;

import java.util.List;

public class searchCourseActivity extends SingleFragmentActivity implements SearchCourseFragment.Callbacks {


    @NonNull
    @Override
    protected SearchCourseFragment createFragment(){
        return new SearchCourseFragment();
    }


    @Override
    public void onSubmitQuery(List<Course> courses){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainer, new SearchCourseResultsFragment().newInstance(courses)).commit();
    }
}
