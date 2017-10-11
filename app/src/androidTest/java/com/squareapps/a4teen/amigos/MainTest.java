package com.squareapps.a4teen.amigos;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;

import com.squareapps.a4teen.amigos.Activities.MainActivity;


public class MainTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainTest() {
        super("com.squareapps.a4teen.amigos", MainActivity.class);
    }

    public void test(){
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.tabHomeFAB);
        assertEquals(-1, fab.getSize());
    }
}
