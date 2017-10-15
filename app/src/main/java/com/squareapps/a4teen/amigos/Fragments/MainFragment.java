package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.AddFriendsActivity;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Activities.ProfileActivity;
import com.squareapps.a4teen.amigos.Activities.SearchFormActivity;
import com.squareapps.a4teen.amigos.Activities.SettingsActivity;
import com.squareapps.a4teen.amigos.Common.Contract;
import com.squareapps.a4teen.amigos.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;

public class MainFragment extends FragmentBase {// Declare the constants
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.tabHomeFAB)
    FloatingActionButton fab;
    SectionsPagerAdapter adapter;// Firebase instance variables

    public static MainFragment newInstance(Bundle b) {

        MainFragment fragment = new MainFragment();
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getUser() == null) {
            // Not signed in, launch the Sign In activity
            getActivity().startActivity(new Intent(null, LoginActivity.class));
            getActivity().finish();
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, v);

        setToolbar(toolbar, R.drawable.ic_menu_black_24dp);

        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {

                            case R.id.nav_search_class:
                                initSearchFormActivity();
                                break;
                            case R.id.nav_profile:
                                Intent profileSettings = new Intent(getActivity(), ProfileActivity.class);
                                getActivity().startActivity(profileSettings);
                                break;

                            case R.id.nav_add_friends:
                                Intent addFriends = new Intent(getActivity(), AddFriendsActivity.class);
                                getActivity().startActivity(addFriends);
                                break;

                        }

                        // Closing drawer on item click
                        drawerLayout.closeDrawers();
                        return true;


                    }
                });

        View headerView = navigationView.getHeaderView(0);
        final ImageView imageView =  headerView.findViewById(R.id.nav_header_image_view);

        if (getAvatarUrl() == null) {
            getDataRef()
                    .child(USERS)
                    .child(getUid())
                    .child(AVATAR_URL)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() instanceof String) {
                                String avatarUrl = dataSnapshot.getValue().toString();
                                setImageView(avatarUrl, imageView);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else
            setImageView(getAvatarUrl(), imageView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                //startActivity(i);
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });


        return v;
    }


    void initSearchFormActivity() {
        Intent searchForm = new Intent(getActivity(), SearchFormActivity.class);
        searchForm.setAction(SearchFormFragment.SEARCH);
        startActivity(searchForm);
    }// Add Fragments to Tabs


    void setupViewPager(final ViewPager viewPager) {
        adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new CourseListFragment(), Contract.CLASSES);
        adapter.addFragment(new GroupListFragment(), Contract.GROUPS);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_settings:
                Intent intent = new Intent(null, SettingsActivity.class);
                getActivity().startActivity(intent);
                return true;
            case R.id.sign_out_menu:
                signOutUser();
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    static class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}