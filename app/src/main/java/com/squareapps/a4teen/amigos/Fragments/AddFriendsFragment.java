package com.squareapps.a4teen.amigos.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.Common.ContactsFirebaseLoader;
import com.squareapps.a4teen.amigos.R;

import static com.squareapps.a4teen.amigos.Common.Contract.CONTACTS;
import static com.squareapps.a4teen.amigos.Common.Contract.COUNT;
import static com.squareapps.a4teen.amigos.Common.Contract.EMAIL;
import static com.squareapps.a4teen.amigos.Common.Contract.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.PHONE_NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.USERS;


public class AddFriendsFragment extends FragmentBase implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 114;

    private TextView searchByEmail;
    private TextView searchContacts;
    private TextView searchByName;

    private ProgressDialog progressDialog;

    public static AddFriendsFragment newInstance(Bundle bundle) {

        AddFriendsFragment fragment = new AddFriendsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                Log.d("Permission Relsuts", "called");
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission Relsuts", "granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    beginUpload();

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

        searchByEmail = view.findViewById(R.id.search_email_textView);
        searchContacts = view.findViewById(R.id.search_contact_textView);
        searchByName = view.findViewById(R.id.search_name_textView);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        searchByEmail.setOnClickListener(this);
        searchContacts.setOnClickListener(this);
        searchByName.setOnClickListener(this);


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_friends, menu);
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

                    Uri uri = ContactsContract.Contacts.CONTENT_URI;
                    final Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    assert cursor != null;
                    final int count = cursor.getCount();
                    cursor.close();

                    DatabaseReference reference = getDataRef()
                            .child(USERS)
                            .child(getUid())
                            .child(CONTACTS)
                            .child(COUNT);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Object object = dataSnapshot.getValue();
                            if (object == null || (long) object != count) {
                                beginUpload();
                            } else {
                                Intent searchUserIntent = new Intent();
                                searchUserIntent.setClass(getActivity(), SearchUsersActivity.class);
                                searchUserIntent.putExtra(SearchUsersFragment.EXTRA_SEARCH_BY, PHONE_NUMBER);
                                startActivity(searchUserIntent);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


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

    private void beginUpload() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("searching contacts...");
        progressDialog.setCancelable(false);
        AsynLoadContacts asynLoadContacts = new AsynLoadContacts();
        asynLoadContacts.execute();
    }

    private void uploadContacts() {
        ContactsFirebaseLoader contacstLoader = new ContactsFirebaseLoader(getActivity().getContentResolver());
        contacstLoader.begin();
    }


    private class AsynLoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Intent searchUserIntent = new Intent();
            searchUserIntent.setClass(getActivity(), SearchUsersActivity.class);
            searchUserIntent.putExtra(SearchUsersFragment.EXTRA_SEARCH_BY, PHONE_NUMBER);
            startActivity(searchUserIntent);
        }

        @Override
        protected Void doInBackground(Void... params) {
            uploadContacts();
            return null;
        }
    }
}


