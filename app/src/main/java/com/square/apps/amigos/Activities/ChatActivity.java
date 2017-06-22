package com.square.apps.amigos.Activities;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.square.apps.amigos.AppControl;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Fragments.ChatFragment;
import com.square.apps.amigos.R;
import com.square.apps.amigos.Services.sendMessageIntentService;
import com.square.apps.amigos.common.common.Camera.PictureUtils;
import com.square.apps.amigos.common.common.Friend.Friend;
import com.square.apps.amigos.common.common.Friend.FriendLab;
import com.square.apps.amigos.common.common.db.DataProvider;

@SuppressWarnings("deprecation")
public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatFragment.callBacks {


    private EditText msgEdit;
    @Nullable
    private String friendEmail;
    private String msg;
    @Nullable
    private BroadcastReceiver ChatBroadcastReceiver;

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ChatBroadcastReceiver,
                new IntentFilter(Contract.SEND_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(ChatBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*setting up the drawer**/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        String friendID = getIntent().getStringExtra(DataProvider.COL_ID);

        /*getting the information from the database**/
        Friend friend = FriendLab.get(this).getFriend(friendID, Contract.PRIMARY_KEY);
        friendEmail = friend != null ? friend.getEmail() : null;
        String friendName = friend.getName();

        setTitle(friendName);

        msgEdit = (EditText) findViewById(R.id.msg_edit);
        Button sendBtn = (Button) findViewById(R.id.send_btn);

        ChatBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, @NonNull Intent intent) {
                boolean error = intent.getBooleanExtra("error", false);
                if (error) {
                    String error_msg = intent.getStringExtra("error_msg");
                    Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                } else {
                    msgEdit.setText(null);

                    /*autoHides keyboard**/
                    View view = getCurrentFocus(); // Check if no view has focus:
                    if (view != null) { //auto-hide the keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    String myEmail = AppControl.getInstance(context).getUserEmail();

                    /*storing message in database**/
                    ContentValues values = new ContentValues();
                    values.put(DataProvider.COL_MSG, msg);
                    values.put(DataProvider.COL_TO, friendEmail);
                    values.put(DataProvider.COL_FROM, myEmail);
                    getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
                }
            }
        };

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = msgEdit.getText().toString();
                send();

            }
        });

        Fragment chatFragment = ChatFragment.newInstance(friendID);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.msg_list, chatFragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String[] PROJECTION = new String[]{
                Contract.PROFILE_IMAGE,
                Contract.NAME,
                Contract.EMAIL,
        };

        String friendID = getIntent().getStringExtra(Contract.PRIMARY_KEY);
        Cursor cursor = getApplicationContext().getContentResolver()
                .query(Uri.withAppendedPath(DataProvider.CONTENT_URI_ALL_FRIENDS, friendID)
                        , PROJECTION, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        String stringImage = cursor.getString(cursor.getColumnIndex(Contract.PROFILE_IMAGE));
        cursor.close();
        Bitmap friendImage = PictureUtils.decodeBase64(stringImage);
        ImageView avatar = (ImageView) findViewById(R.id.Friend_imageView);

        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        avatar.setImageBitmap(friendImage);

        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Nullable
    @Override
    public String getFriendEmail() {
        return friendEmail;
    }

    private void send() {
        Intent intent = new Intent(this, sendMessageIntentService.class);
        intent.putExtra(DataProvider.COL_MSG, msg);
        intent.putExtra(DataProvider.COL_EMAIL, friendEmail);
        startService(intent);
    }

}
