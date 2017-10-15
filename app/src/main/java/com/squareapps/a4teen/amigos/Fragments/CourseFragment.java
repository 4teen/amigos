package com.squareapps.a4teen.amigos.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;


public class CourseFragment extends FragmentBase {
    private static final String TAG = "Course Fragment";
    private static final String EXTRA_COURSE_ID = "EXTRA_COURSEID";

    private String courseId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView0)
    TextView title;

    @BindView(R.id.detail_description)
    TextView description;

    @BindView(R.id.detail_credits)
    TextView credits;

    @BindView(R.id.detail_prerequisites)
    TextView prerequisites;

    @BindView(R.id.detail_corequisites)
    TextView corequisites;

    @BindView(R.id.detail_co_prerequsites)
    TextView coPrerequisites;

    @BindView(R.id.detail_requirements)
    TextView courseRequirements;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    DatabaseReference databaseReference;
    ValueEventListener myEventListener;

    @Nullable
    private static Course myCourse;

    @NonNull
    public static CourseFragment newInstance(String courseID) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_COURSE_ID, courseID);
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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
        courseId = getArguments().getString(EXTRA_COURSE_ID);
        if (savedInstanceState != null && courseId == null)
            courseId = savedInstanceState.getString(EXTRA_COURSE_ID);
        else if (savedInstanceState == null) {
            Log.d(TAG, "arguments did not save into SavedInstance");
        }

        assert courseId != null;
        databaseReference = getDataRef().child(COURSES).child(courseId);
        myEventListener = databaseReference.addValueEventListener(createEventListener());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(getArguments());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_detail, container, false);
        ButterKnife.bind(this, view);

        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatMembersActivity.class);
                intent.setAction(ChatMembersFragment.ACTION_SEARCH_COURSE_MEMEBRS);
                intent.putExtra(ChatMembersFragment.EXTRA_PARAM1, courseId);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void updateUI() {
        if (myCourse == null)
            return;
        toolbar.setTitle("Deparment of " + myCourse.getDepartment());
        title.setText(myCourse.getTitle());
        description.setText(myCourse.getDescription());
        credits.setText(myCourse.getCredits());
        prerequisites.setText(myCourse.getPrerequisites());
        corequisites.setText(myCourse.getCorequisites());
        coPrerequisites.setText(myCourse.getCoPrerequisites());
        courseRequirements.setText(myCourse.getCourseRequirements());
    }

    private ValueEventListener createEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                if (course != null) {
                    myCourse = course;
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


    }
}
