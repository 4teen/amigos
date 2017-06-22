package com.square.apps.amigos.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.CourseFragment;
import com.square.apps.amigos.Fragments.FriendListFragment;
import com.square.apps.amigos.Fragments.NoticeDialogFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.RemoveCourseService;
import com.square.apps.amigos.common.common.course.Course;
import com.square.apps.amigos.common.common.course.CourseLab;

import java.util.ArrayList;


public class CoursePagerActivity extends AppCompatActivity
        implements CourseFragment.Callbacks, FriendListFragment.Callbacks, NoticeDialogFragment.NoticeDialogListener {

    private ArrayList<Course> courses;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*assigns an id the the viewPager and fills the layout**/
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        /*gets the list of courses**/
        courses = CourseLab.get(this).getCourses();
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Course course = courses.get(position);
                return CourseFragment.newInstance(course.getCourseID());
            }

            @Override
            public int getCount() {
                return courses.size();
            }
        });


        /*sets the current item of the view pager to the one that was selected**/
        String courseID = getIntent().getStringExtra(Contract.COURSE_ID);
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseID().equals(courseID)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /**sets the title of the page with the course title**/
            @Override
            public void onPageSelected(int position) {
                Course course = courses.get(position);
                if (course.getTitle() != null)
                    setTitle(course.getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onFindFriendsButtonPushed(@NonNull Course course) {
        Intent intent = new Intent(this, FriendListActivity.class);
        intent.putExtra(Contract.COURSE_ID, course.getCourseID());
        startActivity(intent);
    }

    @Override
    public void onFriendSelected(String friendID) {
    }

    @Override
    public void onRemoveCourse() {
        Course course = courses.get(viewPager.getCurrentItem());
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", "Are you sure you want to remove " + course + " from your classes?");
        bundle.putSerializable("posButton", "YES");
        bundle.putSerializable("negButton", "NO");
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "deleted course");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String CourseID = courses.get(viewPager.getCurrentItem()).getCourseID();
        Intent removeCourseIntent = new Intent(this, RemoveCourseService.class);
        removeCourseIntent.putExtra(Contract.COURSE_ID, CourseID);
        startService(removeCourseIntent);

        Intent intent = new Intent(getApplicationContext(), TabHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
