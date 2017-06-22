package com.square.apps.amigos.Abstract;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.square.apps.amigos.R;

/**
 * Created by YOEL on 9/12/2015.
 * abstract class used to create fragments and add it to the list of fragments
 * handled by the fragment manager
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
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
       FragmentManager supportFragmentManager = getSupportFragmentManager();

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

}

