package com.squareapps.a4teen.amigos.Activities;


import android.app.Fragment;
import android.support.annotation.NonNull;

import com.squareapps.a4teen.amigos.Abstract.SingleFragmentActivity;
import com.squareapps.a4teen.amigos.Fragments.SearchContactsFragment;
import com.squareapps.a4teen.amigos.Fragments.SearchUsersFragment;

import static com.squareapps.a4teen.amigos.Common.Contract.PHONE_NUMBER;


public class SearchUsersActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment createFragment() {
        String searchBy = getIntent().getStringExtra(SearchUsersFragment.EXTRA_SEARCH_BY);
        if (searchBy.equals(PHONE_NUMBER))
            return SearchContactsFragment.newInstance(getIntent().getExtras());
        else
            return SearchUsersFragment.newInstance(getIntent().getExtras());
    }


}
