package com.square.apps.amigos.Activities;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.square.apps.amigos.Fragments.CourseFragment;
import com.square.apps.amigos.Fragments.FriendListFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.course.Course;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity implements CourseFragment.Callbacks, FriendListFragment.Callbacks {

    public static final String EXTRA_POSITION = "position";

    private ArrayList<Course> courses;
    private ViewPager         viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*assigns an id the the viewPager and fills the layout**/
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        /*gets the list of courses**/
        courses = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            courses.add(new Course());
        }

        FragmentManager fragmentManager = getFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position){
                Course course = courses.get(position);
                return CourseFragment.newInstance(course.getCourseID());
            }

            @Override
            public int getCount(){
                return courses.size();
            }
        });


        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
            }

            /**sets the title of the page with the course title**/
            @Override
            public void onPageSelected(int position){
                Course course = courses.get(position);
                if (course.getTitle() != null) setTitle(course.getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state){
            }
        });
    }

    @Override
    public void onFindFriendsButtonPushed(@NonNull Course course){
        Intent intent = new Intent(this, FriendListActivity.class);
        intent.putExtra(EXTRA_POSITION, course.getCourseID());
        startActivity(intent);
    }

    @Override
    public void onFriendSelected(String friendID){
    }

    @Override
    public void onRemoveCourse(){
    }
}
