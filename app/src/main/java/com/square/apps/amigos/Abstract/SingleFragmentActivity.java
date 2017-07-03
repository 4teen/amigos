package com.square.apps.amigos.Abstract;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.square.apps.amigos.R;

/**
 * Created by YOEL on 9/12/2015.
 * abstract class used to create fragments and add it to the list of fragments
 * handled by the fragment manager
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    /**
     * Called when the Activity is first created
     **/
    @NonNull
    protected abstract Fragment createFragment();

    /**
     * @return the ID of the layout that the activity will inflate
     */
    protected int getLayoutResID(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        /*gets instance of fragment manager**/
       FragmentManager supportFragmentManager = getFragmentManager();

        /**Fragment transaction******
         ask Fragment manager for the Course fragment with the container view ID of R.id.fragmentContainer
         if this fragment is already in the list, the FM will return it.
         */
        Fragment fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer);

        /*if fragment is not in the list, create a new Course Fragment**/
        if (fragment == null) {
            fragment = createFragment();
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}

