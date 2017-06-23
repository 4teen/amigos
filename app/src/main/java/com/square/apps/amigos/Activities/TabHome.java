package com.square.apps.amigos.Activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.AllFriendsListFragment;
import com.square.apps.amigos.Fragments.CourseListFragment;
import com.square.apps.amigos.Fragments.FriendRequestFragment;
import com.square.apps.amigos.Fragments.MainScreenFragment;
import com.square.apps.amigos.Fragments.PendingRequestFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.RetrieveBuffMsgsService;
import com.square.apps.amigos.Services.acceptRejectFriendRequest;
import com.square.apps.amigos.common.common.db.DataProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TabHome extends AppCompatActivity implements AllFriendsListFragment.Callbacks, CourseListFragment.Callbacks, FriendRequestFragment.Callbacks, PendingRequestFragment.Callbacks, MainScreenFragment.Callbacks {

    // Declare the constants
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACCOUNT               = 0;
    private static final int CLASSES               = 1;
    private static final int FRIENDS               = 2;
    private static final int FRIEND_REQUESTS       = 3;
    private static final int PENDING_REQUESTS      = 4;
    private static final int FORUM                 = 5;
    private BroadcastReceiver mCourseLoaderBroadcastReceiver;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager         mViewPager;

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mCourseLoaderBroadcastReceiver, new IntentFilter(Contract.GETTING_COURSES_COMPLETE));
    }

    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCourseLoaderBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);
        if (savedInstanceState == null)

            mCourseLoaderBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(@NonNull Context context, Intent intent){
                /*retrieve messages that were sent while the user was offline**/
                    RetrieveBuffMsgsService.startActionRetrieveBuffMsgs(context);
                }
            };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupTabLayout(tabLayout);

    }

    private void setupTabLayout(@NonNull final TabLayout tabLayout){
        tabLayout.setMinimumWidth(160);
        tabLayout.setMinimumHeight(48);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);

        //noinspection ConstantConditions
        tabLayout.getTabAt(ACCOUNT).setText("Account");//.setIcon(R.drawable.icon_account_tab);//
        tabLayout.getTabAt(ACCOUNT);
        //noinspection ConstantConditions
        tabLayout.getTabAt(CLASSES).setText("Classes");//.setIcon(R.drawable.icon_classes_tab);//
        //noinspection ConstantConditions
        tabLayout.getTabAt(FRIENDS).setText("Friends");//.setIcon(R.drawable.icon_friends_tab);//
        //noinspection ConstantConditions
        tabLayout.getTabAt(FRIEND_REQUESTS).setText("Request");//.setIcon(R.drawable.icon_friend_request);//
        //noinspection ConstantConditions
        tabLayout.getTabAt(PENDING_REQUESTS).setText("Pending");//.setIcon(R.drawable.icon_pending_request);//
        //noinspection ConstantConditions
        tabLayout.getTabAt(FORUM).setText("Forum");//.setIcon(R.drawable.icon_forum_tab);//


        mViewPager.setCurrentItem(tabLayout.getSelectedTabPosition());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

            }

            @Override
            public void onPageSelected(int position){
                //setTitle(tabLayout.getTabAt(position).getText());

            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFriendSelected(String friendID){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(DataProvider.COL_ID, friendID);
        startActivity(intent);
    }

    @Override
    public void onAddFriend(String friendID){
        Intent intent = new Intent(this, SearchFriendsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCourseSelected(String CourseID){
        /*just Start an instance of coursePagerActivity with the course that was selected**/
        Intent intent = new Intent(this, CoursePagerActivity.class);
        intent.putExtra(Contract.COURSE_ID, CourseID);
        startActivity(intent);
    }

    @Override
    public void onFriendRequestSelected(String friendID){
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
            public void onClick(DialogInterface dialog, int which){
                intent.putExtra(acceptRejectFriendRequest.TAG, "acceptFriendRequest");
                startService(intent);
            }
        }).setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id){
                intent.putExtra(acceptRejectFriendRequest.TAG, "rejectFriendRequest");
                startService(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onPendingRequestSelected(String friendID){

    }

    @Override
    public void onAddCourse(){
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTakePicture(){
        // create Intent to take a picture and return control to the calling application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position){
            switch (position) {
                case ACCOUNT:
                    return new MainScreenFragment();
                case CLASSES:
                    return new CourseListFragment();
                default:
                    return new MainScreenFragment();
            }
        }

        @Override
        public int getCount(){
            return 6;
        }
    }


}
