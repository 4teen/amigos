package com.square.apps.amigos.Activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.AllFriendsListFragment;
import com.square.apps.amigos.Fragments.CourseListFragment;
import com.square.apps.amigos.Fragments.FriendRequestFragment;
import com.square.apps.amigos.Fragments.GroupListFragment;
import com.square.apps.amigos.Fragments.LoginFragment;
import com.square.apps.amigos.Fragments.MainScreenFragment;
import com.square.apps.amigos.Fragments.PendingRequestFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.acceptRejectFriendRequest;
import com.square.apps.amigos.common.common.db.DataProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabHome extends AppCompatActivity implements AllFriendsListFragment.Callbacks, CourseListFragment.Callbacks, FriendRequestFragment.Callbacks, PendingRequestFragment.Callbacks, MainScreenFragment.Callbacks, GoogleApiClient.OnConnectionFailedListener {


    // Declare the constants
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACCOUNT = 0;
    private static final int CLASSES = 1;
    private static final int FRIENDS = 2;
    private static final int FRIEND_REQUESTS = 3;
    private static final int PENDING_REQUESTS = 4;
    private static final int FORUM = 5;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tabHome_coordinating_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    SectionsPagerAdapter adapter;
    private View mRootView;
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
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
        mUsername = mFirebaseUser.getDisplayName();

        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }

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

                        if(menuItem.getItemId()==R.id.nav_bookmark){
                            Intent i = new Intent(getApplicationContext(), searchCourseActivity.class);
                            startActivity(i);
                        }

                        // Closing drawer on item click
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tabHomeFAB);
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

    // Add Fragments to Tabs
    private void setupViewPager(final ViewPager viewPager) {
        adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new MainScreenFragment(), "Account");
        adapter.addFragment(new CourseListFragment(), "Classes");
        adapter.addFragment(new GroupListFragment(), "Groups");
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
        if (i == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (i == R.id.sign_out_menu) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(LoginFragment.createIntent(TabHome.this));
                                finish();
                            } else {
                                showSnackbar(R.string.sign_out_failed);
                            }
                        }
                    });
            return true;
        } else if (i == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFriendSelected(String friendID) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(DataProvider.COL_ID, friendID);
        startActivity(intent);
    }

    @Override
    public void onAddFriend(String friendID) {
        Intent intent = new Intent(this, SearchFriendsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCourseSelected(String CourseID) {
        /*just Start an instance of coursePagerActivity with the course that was selected**/
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Contract.COURSE_ID, CourseID);
        startActivity(intent);
    }

    @Override
    public void onFriendRequestSelected(String friendID) {
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(DataProvider.CONTENT_URI_FRIENDREQUESTS, friendID), null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.NAME));
        cursor.close();

        final Intent intent = new Intent(this, acceptRejectFriendRequest.class);
        intent.putExtra(Contract.STUDENT_ID, friendID);
        /*
          displays an alert dialog double checking that the user wants to add the course
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(name).setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra(acceptRejectFriendRequest.TAG, "acceptFriendRequest");
                startService(intent);
            }
        }).setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                intent.putExtra(acceptRejectFriendRequest.TAG, "rejectFriendRequest");
                startService(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onPendingRequestSelected(String friendID) {

    }

    @Override
    public void onAddCourse() {
        Intent intent = new Intent(this, searchCourseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTakePicture() {
        // create Intent to take a picture and return control to the calling application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Camera and Video variables
     */

    // Define the list of accepted constants and declare the Tabs annotation
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ACCOUNT, CLASSES, FRIENDS, FRIEND_REQUESTS, PENDING_REQUESTS, FORUM})
    public @interface Tabs {
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
