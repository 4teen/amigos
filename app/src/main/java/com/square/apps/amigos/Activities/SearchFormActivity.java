package com.square.apps.amigos.Activities;


import android.app.FragmentManager;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.SearchCourseResultsFragment;
import com.square.apps.amigos.Fragments.SearchFormFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.Course;

import java.util.List;

public class SearchFormActivity extends SingleFragmentActivity implements SearchFormFragment.Callbacks {


    @NonNull
    @Override
    protected SearchFormFragment createFragment() {
        return (getIntent().getAction().equals(SearchFormFragment.DETAIL))
                ? SearchFormFragment.newInstance(SearchFormFragment.DETAIL)
                : SearchFormFragment.newInstance(SearchFormFragment.SEARCH);
    }


    @Override
    public void onSubmitQuery(List<Course> courses) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainer, SearchCourseResultsFragment.newInstance(courses)).commit();
    }

}
