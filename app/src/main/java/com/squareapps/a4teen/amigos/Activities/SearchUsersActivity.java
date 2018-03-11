package com.squareapps.a4teen.amigos.Activities;


import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.SearchUsersFragment;
import com.squareapps.a4teen.amigos.Fragments.SearchUsersFragment2;

import static com.squareapps.a4teen.amigos.Common.Contract.User.PHONE_NUMBER;


public class SearchUsersActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment createFragment() {
        String searchBy = getIntent().getStringExtra(SearchUsersFragment.EXTRA_SEARCH_BY);
        if (searchBy.equals(PHONE_NUMBER))
            return SearchUsersFragment2.newInstance(getIntent().getExtras());
        else
            return SearchUsersFragment.newInstance(getIntent().getExtras());
    }


}
