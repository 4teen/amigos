package com.squareapps.a4teen.amigos.Activities;

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
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareapps.a4teen.amigos.Fragments.CourseListFragment;
import com.squareapps.a4teen.amigos.Fragments.GroupListFragment;
import com.squareapps.a4teen.amigos.Fragments.LoginFragment;
import com.squareapps.a4teen.amigos.Fragments.SearchFormFragment;
import com.squareapps.a4teen.amigos.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.CLASSES;
import static com.squareapps.a4teen.amigos.Common.Contract.COURSE_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;

public class MainActivity extends AppCompatActivity implements
        CourseListFragment.Callbacks {


    // Declare the constants
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

    SectionsPagerAdapter adapter;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {

                            case R.id.nav_bookmark:
                                initSearchFormActivity();
                                break;
                            case R.id.nav_profile:
                                Intent profileSettings = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(profileSettings);
                                break;

                        }

                        // Closing drawer on item click
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                //startActivity(i);
                Snackbar.make(v, "Hello Snackbar!",
                        Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void initSearchFormActivity() {
        Intent searchForm = new Intent(getApplicationContext(), SearchFormActivity.class);
        searchForm.setAction(SearchFormFragment.SEARCH);
        startActivity(searchForm);
    }

    // Add Fragments to Tabs
    private void setupViewPager(final ViewPager viewPager) {
        adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new CourseListFragment(), CLASSES);
        adapter.addFragment(new GroupListFragment(), GROUPS);
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.sign_out_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(LoginFragment.createIntent(MainActivity.this));
                                    finish();
                                }
                            }
                        });
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCourseSelected(String CourseID) {
        initDetailActivity(CourseID);
    }

    @Override
    public void onAddCourse() {
        initSearchFormActivity();
    }


    private void initDetailActivity(String CourseID) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(COURSE_ID, CourseID);
        startActivity(intent);
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
