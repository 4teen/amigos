package com.squareapps.a4teen.amigos.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.AddFriendsActivity;
import com.squareapps.a4teen.amigos.Activities.InvitationActivity;
import com.squareapps.a4teen.amigos.Activities.LoginActivity;
import com.squareapps.a4teen.amigos.Activities.NotificationsActivity;
import com.squareapps.a4teen.amigos.Activities.ProfileActivity;
import com.squareapps.a4teen.amigos.Activities.SearchFormActivity;
import com.squareapps.a4teen.amigos.Common.Contract;
import com.squareapps.a4teen.amigos.Common.Utils.AppPreferences;
import com.squareapps.a4teen.amigos.Common.Utils.CustomDataBindingAdapters;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.Settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.squareapps.a4teen.amigos.Common.Contract.PENDING_NUM;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;
import static com.squareapps.a4teen.amigos.Common.Contract.User.AVATAR_URL;


public class MainFragment extends FragmentBase {

    public ImageView imageView;
    public DatabaseReference dataref;
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
    ChildEventListener listener;

    public static MainFragment newInstance(Bundle b) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!isSignedIn() || getUid() == null) {
            // Not signed in, launch the Sign In activity
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            dataref.removeEventListener(listener);
            listener = null;
        }

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isAdded()) {
            PreferenceManager.setDefaultValues(getActivity(), R.xml.groups_pref_general, false);
        }

        dataref = getDataRef()
                .child(USERS)
                .child(getUid());

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, v);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        View headerView = navigationView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.nav_header_image_view);
        TextView display_name = headerView.findViewById(android.R.id.text1);
        TextView display_email = headerView.findViewById(android.R.id.text2);

        final String displayName = AppPreferences.getPrefDisplayName(getContext());
        final String email = AppPreferences.getPrefUserEmail(getContext());

        display_name.setText(displayName);
        display_email.setText(email);

        setNavigationView();

        listener = dataref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateCounter(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateCounter(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setupViewPager();
        tabLayout.setupWithViewPager(mViewPager);


        return v;
    }

    private void updateCounter(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        Object value = dataSnapshot.getValue();

        Log.d("NAvigationView", key);
        Log.d("NAvigationView", String.valueOf(value));

        switch (key) {
            case AVATAR_URL:
                CustomDataBindingAdapters.loadImage(imageView
                        , (String) value
                        , getResources()
                                .getDrawable(R.drawable.ic_broken_image_black_24dp));
                setAvatarUrl((String) value);
                break;

            case PENDING_NUM:
                String counter = String.valueOf(value);

                MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_group_invitations);
                TextView view = (TextView) menuItem.getActionView();
                view.setText(counter);
                break;

        }
    }

    private void setNavigationView() {

        // Set behavior of Navigation drawer
        // This method will trigger on item Click of navigation menu

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // Set item in checked state
                    menuItem.setChecked(true);

                    switch (menuItem.getItemId()) {

                        case R.id.nav_search_class:
                            startActivitySearchForm();
                            break;
                        case R.id.nav_profile:
                            startActivity(ProfileActivity.class);
                            break;

                        case R.id.nav_add_friends:
                            startActivity(AddFriendsActivity.class);
                            break;

                        case R.id.nav_notifications:
                            startActivity(NotificationsActivity.class);
                            break;

                        case R.id.nav_group_invitations:
                            startActivity(InvitationActivity.class);
                            break;

                    }

                    // Closing drawer on item click
                    drawerLayout.closeDrawers();
                    return true;
                }
        );

    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }


    void startActivitySearchForm() {
        Intent searchForm = new Intent(getActivity(), SearchFormActivity.class);
        searchForm.setAction(SearchFormFragment.SEARCH);
        startActivity(searchForm);
    }


    void setupViewPager() {

        adapter = new SectionsPagerAdapter(getFragmentManager());
        adapter.addFragment(new CourseListFragment(), Contract.CLASSES);
        adapter.addFragment(new GroupListFragment(), Contract.GROUPS);
        mViewPager.setAdapter(adapter);

        fab.setImageIcon(Icon.createWithResource(getContext(), R.drawable.ic_search_black_24dp));
        fab.setOnClickListener(v -> startActivitySearchForm());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (adapter.getItem(position) instanceof CourseListFragment) fab.show();
                else fab.hide();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                startActivity(SettingsActivity.class);
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

    static class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}