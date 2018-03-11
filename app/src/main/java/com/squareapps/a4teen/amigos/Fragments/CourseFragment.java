package com.squareapps.a4teen.amigos.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.Activities.MainActivity;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.databinding.ActivityCourseDetailBinding;

import java.util.Objects;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_CODE;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


public class CourseFragment extends FragmentBase implements View.OnClickListener {
    @Nullable
    private static Course myCourse;
    private final String TAG = getSimpleName(this);
    DatabaseReference databaseReference;
    ValueEventListener myEventListener;
    private String courseCode;

    @NonNull
    public static CourseFragment newInstance(Bundle bundle) {
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(myEventListener);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        courseCode = (savedInstanceState == null)
                ? getArguments().getString(COURSE_CODE)
                : savedInstanceState.getString(COURSE_CODE);

        myCourse = new Course();

        final String action = getActivity().getIntent().getAction();
        myCourse.setAction(action);

        databaseReference = getDataRef().child(COURSES).child(courseCode);

        myEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                if (course != null) {
                    myCourse.setCollege(course.getCollege());
                    myCourse.setCoPrerequisites(course.getCoPrerequisites());
                    myCourse.setTitle(course.getTitle());
                    myCourse.setCourseID(course.getCourseID());
                    myCourse.setDepartment(course.getDepartment());
                    myCourse.setCreditHours(course.getCreditHours());
                    myCourse.setDescription(course.getDescription());
                    myCourse.setNumber(course.getNumber());
                    myCourse.setPrefix(course.getPrefix());
                    myCourse.setPrerequisites(course.getPrerequisites());
                    myCourse.setCourseRequirements(course.getCourseRequirements());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(getArguments());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActivityCourseDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_course_detail, container, false);
        binding.setCourse(myCourse);
        View view = binding.getRoot();

        FloatingActionButton fab = view.findViewById(R.id.fab);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        fab.setOnClickListener(CourseFragment.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (!Objects.isNull(getActivity().getIntent().getAction())) {
            final Intent intent = new Intent(getActivity(), MainActivity.class);
            getDataRef()
                    .child(USERS)
                    .child(getUid())
                    .child(COURSES)
                    .child(courseCode)
                    .setValue(true, (databaseError, databaseReference) -> {
                        startActivity(intent);
                        getActivity().finish();
                    });


        } else {
            Intent intent = new Intent(view.getContext(), ChatMembersActivity.class);
            intent.setAction(ChatMembersFragment.ACTION_SEARCH_COURSE_MEMEBRS);
            intent.putExtra(ChatMembersFragment.EXTRA_PARAM1, courseCode);
            startActivity(intent);
        }

    }

}
