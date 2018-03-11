package com.squareapps.a4teen.amigos.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.User.EMAIL;
import static com.squareapps.a4teen.amigos.Common.Contract.User.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.User.PHONE_NUMBER;


public class AddContactsFragment extends FragmentBase implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 114;


    public static AddContactsFragment newInstance(Bundle bundle) {

        AddContactsFragment fragment = new AddContactsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                Log.d("Permission Relsuts", "called");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission Relsuts", "granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    uploadContacts();

                } else {
                    Log.d("Permission Relsuts", "denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_friends, container, false);

        TextView searchByEmail = view.findViewById(R.id.search_email_textView);
        TextView searchContacts = view.findViewById(R.id.search_contact_textView);
        TextView searchByName = view.findViewById(R.id.search_name_textView);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        searchByEmail.setOnClickListener(this);
        searchContacts.setOnClickListener(this);
        searchByName.setOnClickListener(this);

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent searchUserIntent = new Intent();
        switch (v.getId()) {
            case R.id.search_email_textView:
                searchUserIntent.setClass(getActivity(), SearchUsersActivity.class);
                searchUserIntent.putExtra(SearchUsersFragment.EXTRA_SEARCH_BY, EMAIL);
                startActivity(searchUserIntent);
                break;
            case R.id.search_name_textView:
                searchUserIntent.setClass(getActivity(), SearchUsersActivity.class);
                searchUserIntent.putExtra(SearchUsersFragment.EXTRA_SEARCH_BY, NAME);
                startActivity(searchUserIntent);

            case R.id.search_contact_textView:
                if (hasPermissions()) {
                    uploadContacts();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                break;
            default:
                break;
        }

    }

    private boolean hasPermissions() {
        // Here, thisActivity is the current activity
        return (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void uploadContacts() {
        Intent searchUserIntent = new Intent();
        searchUserIntent.setClass(getActivity(), SearchUsersActivity.class);
        searchUserIntent.putExtra(SearchUsersFragment.EXTRA_SEARCH_BY, PHONE_NUMBER);
        startActivity(searchUserIntent);
    }

}


