package com.squareapps.a4teen.amigos.Activities;


import android.app.FragmentManager;
import android.support.annotation.NonNull;


import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.Fragments.SearchCourseResultsFragment;
import com.squareapps.a4teen.amigos.Fragments.SearchFormFragment;
import com.squareapps.a4teen.amigos.R;

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
