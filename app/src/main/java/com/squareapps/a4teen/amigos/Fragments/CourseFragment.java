package com.squareapps.a4teen.amigos.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Course;
import com.squareapps.a4teen.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;


public class CourseFragment extends Fragment {
    private static final String EXTRA_COURSE_ID = "EXTRA_COURSEID";


    @BindView(R.id.detail_course_toolbar)
    Toolbar toolbar;

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

    @BindView(R.id.course_detail_Fab)
    FloatingActionButton fab;

    @BindView(R.id.detail_course_collapsingToolBarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;


    DatabaseReference databaseReference;
    ValueEventListener myEventListener;

    @Nullable
    private Course myCourse;
    private String courseId;

    @NonNull
    public static CourseFragment newInstance(String courseID) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_COURSE_ID, courseID);
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
        courseId = getArguments().getString(EXTRA_COURSE_ID);
        String courseID = getArguments().getString(EXTRA_COURSE_ID);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(COURSES).child(courseID);
        myEventListener = databaseReference.addValueEventListener(createEventListener());

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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
        assert myCourse != null;

        collapsingToolbarLayout.setTitle(myCourse.getTitle());
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
