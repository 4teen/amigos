package com.square.apps.amigos.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.course.Course;


/**
 * CourseFragment is a controller that interacts with model and view objects.
 * its job is to present the details of a specific course and update those details
 * as the use changes them
 */
@SuppressWarnings("deprecation")
public class CourseFragment extends Fragment {
    private static final String EXTRA_COURSE_ID = "null";
    @Nullable
    private Course    course;
    @Nullable
    private Callbacks callbacks;

    /**
     * end of interface
     **/

    @NonNull
    public static CourseFragment newInstance(String courseID){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_COURSE_ID, courseID);
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String courseID = (String) getArguments().getSerializable(EXTRA_COURSE_ID);
        course = new Course();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_delete_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_settings:
                assert callbacks != null;
                callbacks.onRemoveCourse();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.course_detail_fragment, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_course_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.detail_course_collapsingToolBarLayout);


        TextView college = (TextView) view.findViewById(R.id.collegeSum);
        TextView department = (TextView) view.findViewById(R.id.departmentSum);
        TextView subject = (TextView) view.findViewById(R.id.subjectSum);
        TextView title = (TextView) view.findViewById(R.id.titleSum);
        TextView instructor = (TextView) view.findViewById(R.id.InstructorSum);
        TextView section = (TextView) view.findViewById(R.id.sectionSum);
        TextView time = (TextView) view.findViewById(R.id.timeSum);
        TextView building = (TextView) view.findViewById(R.id.buildingSum);
        TextView room = (TextView) view.findViewById(R.id.roomSum);
        TextView campus = (TextView) view.findViewById(R.id.campusSum);

        Button findFriends = (Button) view.findViewById(R.id.findFriendsButtonSum);

        assert course != null;
        college.setText("College | " + course.getCollege());
        department.setText("Department | " + course.getDepartment());
        subject.setText(course.getSubjectCRS() + " | ");
        title.setText(course.getTitle());
        instructor.setText("Instructor | " + course.getInstructor());
        section.setText("Section | 0" + course.getSection());
        time.setText("Time | " + course.getClassTime());
        building.setText(course.getBuilding());
        room.setText(course.getRoom());
        campus.setText(course.getCampus());

        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                assert callbacks != null;
                callbacks.onFindFriendsButtonPushed(course);
            }
        });
        return view;
    }

    /**
     * required interface for hosting activities
     */
    public interface Callbacks {
        void onRemoveCourse();

        void onFindFriendsButtonPushed(Course course);
    }

}
