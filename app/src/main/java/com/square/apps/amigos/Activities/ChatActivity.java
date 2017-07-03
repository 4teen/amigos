package com.square.apps.amigos.Activities;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.square.apps.amigos.Abstract.SingleFragmentActivity;
import com.square.apps.amigos.Fragments.ChatFragment;


public class ChatActivity extends SingleFragmentActivity {


    @NonNull
    @Override
    protected Fragment createFragment(){
        return new ChatFragment();
    }




}
