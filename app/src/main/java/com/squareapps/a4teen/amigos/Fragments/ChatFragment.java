package com.squareapps.a4teen.amigos.Fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.firebase.ui.auth.ui.ImeHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;
import com.squareapps.a4teen.amigos.Abstract.FragmentBase;
import com.squareapps.a4teen.amigos.Activities.ChatActivity;
import com.squareapps.a4teen.amigos.Activities.ChatMediaActivity;
import com.squareapps.a4teen.amigos.Activities.ChatMembersActivity;
import com.squareapps.a4teen.amigos.Activities.SearchUsersActivity;
import com.squareapps.a4teen.amigos.Common.Objects.Message;
import com.squareapps.a4teen.amigos.Common.Objects.Photo;
import com.squareapps.a4teen.amigos.DialogFragments.ChangeNameDialogFragment;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.UploadService;
import com.squareapps.a4teen.amigos.ViewHolders.ChatHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.squareapps.a4teen.amigos.Common.Contract.AVATAR_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUPS;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.GROUP_NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.IMAGE_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.MEDIA;
import static com.squareapps.a4teen.amigos.Common.Contract.MESSAGE;
import static com.squareapps.a4teen.amigos.Common.Contract.MESSAGES;
import static com.squareapps.a4teen.amigos.Common.Contract.NAME;
import static com.squareapps.a4teen.amigos.Common.Contract.PATH;
import static com.squareapps.a4teen.amigos.Common.Contract.PHOTO_URL;
import static com.squareapps.a4teen.amigos.Common.Contract.TIMESTAMP;
import static com.squareapps.a4teen.amigos.Fragments.SearchCourseResultsFragment.USERS;
import static com.squareapps.a4teen.amigos.UploadService.EXTRA_FILE_URI;
import static java.io.File.separator;


public class ChatFragment extends FragmentBase implements View.OnClickListener {


    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static final String TAG = "ChatFragment";
    private static final int REQUEST_CHANGE_NAME = 269;
    private static final int REQUEST_IMAGE = 270;
    private static final int REQUEST_CHANGE_AVATAR = 271;

    private ImageView navHeaderImageView;

    private BroadcastReceiver mBroadcastReceiver;
    private ValueEventListener avatarEventListener;

    private String mUsername, mPhotoUrl, groupID, groupName;

    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, ChatHolder> adapter;


    @BindView(R.id.chat_fragment_drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.chat_fragment_nav_view)
    NavigationView navigationView;
    @BindView(R.id.sendButton)
    Button mSendButton;
    @BindView(R.id.messageRecyclerView)
    RecyclerView mMessageRecyclerView;
    @BindView(R.id.activity_chat_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.messageEditText)
    EditText mMessageEditText;
    @BindView(R.id.addMessageImageView)
    ImageView mAddMessageImageView;
    @BindView(R.id.chat_toolbar)
    Toolbar toolbar;


    @NonNull
    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mBroadcastReceiver, UploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister download receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseDatabaseReference.removeEventListener(avatarEventListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(getArguments());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        if (bundle == null && savedInstanceState != null) {
            groupName = savedInstanceState.getString(GROUP_NAME);
            groupID = savedInstanceState.getString(GROUP_ID);
        } else if (bundle != null) {
            groupName = bundle.getString(GROUP_NAME);
            groupID = bundle.getString(GROUP_ID);
        }


        mFirebaseDatabaseReference = getDataRef();

        mUsername = getUser().getDisplayName();

        if (getAvatarUrl() != null) {
            mPhotoUrl = getAvatarUrl();

        }

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //    hideProgressDialog();

                switch (intent.getAction()) {
                    case UploadService.UPLOAD_COMPLETED:
                    case UploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_chat, container, false);
        ButterKnife.bind(this, view);

        toolbar.setTitle(groupName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setToolbar(toolbar, R.drawable.ic_arrow_back_black_24dp);

        View navheader = navigationView.getHeaderView(0);//navHeader index = 0
        navHeaderImageView = navheader.findViewById(R.id.nav_header_image_view);
        navHeaderImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.sym_def_app_icon));

        setupNavigationView();

        if (groupID == null) throw new AssertionError();

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        avatarEventListener = mFirebaseDatabaseReference.child(GROUPS)
                .child(groupID)
                .child(AVATAR_URL)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                            setImageView(dataSnapshot.getValue().toString(), navHeaderImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //[START initialize_mMesageEditText_TextWatcher]
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImeHelper.setImeOnDoneListener(mMessageEditText, new ImeHelper.DonePressedListener() {
            @Override
            public void onDonePressed() {
                mSendButton.performClick();
            }
        });

        setAdapter();

        mSendButton.setOnClickListener(this);
        mAddMessageImageView.setOnClickListener(this);
        return view;

    }

    private void setAdapter() {
        Query chatQuery = getDataRef().child(MESSAGES).child(groupID);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(chatQuery, Message.class)
                .setLifecycleOwner(((ChatActivity) getActivity()))
                .build();


        adapter = new FirebaseRecyclerAdapter<Message, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(ChatHolder holder, int position, Message model) {
                holder.bind(model);
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatHolder(newItemView(parent, R.layout.activity_chat_list_item));
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mMessageRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(adapter);
    }

    private void setupNavigationView() {
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        switch (menuItem.getItemId()) {

                            case R.id.chat_fragment_nav_members:
                                Intent searchForm = new Intent(getActivity(), ChatMembersActivity.class);
                                searchForm.setAction(ChatMembersFragment.ACTION_SEARCH_CHAT_MEMEBRS);
                                searchForm.putExtra(ChatMembersFragment.EXTRA_PARAM1, groupID);
                                startActivity(searchForm);
                                break;
                            case R.id.chat_fragment_nav_media:
                                Intent chatMedia = new Intent(getActivity(), ChatMediaActivity.class);
                                chatMedia.putExtra(ChatMediaFragment.PARAM1, groupID);
                                startActivity(chatMedia);
                                break;

                        }

                        // Closing drawer on item click
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private String sendMessage(Message message) {

        String messageId = mFirebaseDatabaseReference
                .child(MESSAGES)
                .child(groupID)
                .push().getKey();

        message.setId(messageId);

        getDataRef().child(MESSAGES)
                .child(groupID)
                .child(messageId)
                .setValue(message);

        HashMap<String, Object> map = new HashMap<>();

        map.put(USERS + separator +
                getUid() + separator +
                MESSAGES + separator +
                messageId + separator +
                groupID, true);

        map.put(GROUPS + separator +
                groupID + separator +
                MESSAGE, mUsername + ": " + message.getText());

        map.put(GROUPS + separator +
                groupID + separator +
                TIMESTAMP, ServerValue.TIMESTAMP);

        getDataRef().updateChildren(map);

        return messageId;

    }

    private void sendPhotoMessage(final Uri data) {

        Message tempMessage = new Message(
                getUid(),
                null,
                null,
                mUsername,
                mPhotoUrl,
                LOADING_IMAGE_URL);

        String photoID = sendMessage(tempMessage);

        Photo photo = new Photo(mPhotoUrl, getUid(), groupID, photoID);
        photo.setTimeStamp(ServerValue.TIMESTAMP);

        HashMap<String, Object> map = new HashMap<>();

        map.put(MEDIA + separator +
                groupID + separator +
                photoID, photo);

        map.put(USERS + separator +
                getUid() + separator +
                MEDIA + separator +
                photoID, true);

        map.put(GROUPS + separator +
                groupID + separator +
                MESSAGE, getUser().getDisplayName() + "| image");

        getDataRef().updateChildren(map);

        String update1 = MESSAGES + separator +
                groupID + separator +
                photoID + separator +
                IMAGE_URL;

        String update2 = MEDIA + separator +
                groupID + separator +
                photoID + separator +
                PHOTO_URL;

        uploadImage(data, photoID, update1, update2);
    }

    private void uploadImage(Uri data, String photoID, String... strings) {

        Bundle bundle = new Bundle();
        bundle.putStringArray("updates", strings);
        bundle.putString(PATH, MEDIA + separator + groupID + photoID);
        bundle.putParcelable(EXTRA_FILE_URI, data);
        UploadService.startActionUpload(getContext(), bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        switch (requestCode) {
            case REQUEST_IMAGE:
                if (data != null) {
                    final Uri uri = data.getData();
                    sendPhotoMessage(uri);

                }
                break;
            case REQUEST_CHANGE_NAME:
                String groupName = data
                        .getSerializableExtra(BaseDialogFragment.EXTRA1)
                        .toString();

                if (groupName != null) {
                    toolbar.setTitle(groupName);

                    HashMap<String, Object> groupsMap = new HashMap<>();
                    groupsMap.put(GROUPS + separator + groupID + separator + NAME + separator, groupName);

                    getDataRef().updateChildren(groupsMap);
                }

                break;
            case REQUEST_CHANGE_AVATAR:
                final Uri avatarFile = data.getData();

                String photoID = getDataRef().child(MEDIA)
                        .child(groupID)
                        .push().getKey();

                Photo photo = new Photo(null, getUid(), groupID, photoID);


                photo.setTimeStamp(ServerValue.TIMESTAMP);

                getDataRef().child(MEDIA)
                        .child(groupID)
                        .child(photoID)
                        .setValue(photo);

                String update1 = MEDIA + separator +
                        groupID + separator +
                        photoID + separator +
                        PHOTO_URL;

                String update2 = GROUPS + separator +
                        groupID + separator +
                        AVATAR_URL;

                uploadImage(avatarFile, photoID, update1, update2);
                break;

            default:
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_members:
                Bundle bundle = new Bundle();
                bundle.putString(GROUP_ID, groupID);

                Intent i = new Intent(getActivity(), SearchUsersActivity.class);
                i.putExtras(bundle);

                startActivity(i);
                return true;

            case R.id.change_name:
                FragmentManager fragmentManager = getFragmentManager();
                ChangeNameDialogFragment changeNameDialogFragment = new ChangeNameDialogFragment();
                changeNameDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                changeNameDialogFragment.setTargetFragment(this, REQUEST_CHANGE_NAME);
                changeNameDialogFragment.show(fragmentManager, "CHANGE NAME DIALOG FRAGMENT");
                return true;


            case R.id.change_avatar:
                dispatchPictureIntent(REQUEST_CHANGE_AVATAR);
                break;

            case R.id.menu:
                drawerLayout.openDrawer(GravityCompat.END);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_fragment_delete_item_menu:
                Message message = adapter.getItem(item.getOrder());
                Log.d(TAG, message.getId());

                HashMap<String, Object> map = new HashMap<>();
                map.put(MESSAGES + separator + groupID + separator + message.getId(), null);
                map.put(USERS + separator + getUid() + separator + MESSAGES + separator + message.getId(), null);
                getDataRef().updateChildren(map);

                return true;
            case R.id.chat_fragment_copyText_item_menu:
                //deleteNote(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                final Message friendlyMessage = new Message(getUid(),
                        null,
                        mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl,
                        null /* no image */);

                sendMessage(friendlyMessage);

                mMessageEditText.setText("");
                break;
            case R.id.addMessageImageView:
                dispatchPictureIntent(REQUEST_IMAGE);
                break;


        }

    }

    private void onUploadResultIntent(Intent intent) {

        // Got a new intent from MyUploadService with a success or failure
        String mDownloadUrl = intent.getStringExtra(UploadService.EXTRA_DOWNLOAD_URL);
        Log.d("downloadUrl", mDownloadUrl);

        String[] updates = intent.getStringArrayExtra("updates");

        HashMap<String, Object> map = new HashMap<>();
        for (String update : updates) {
            map.put(update, mDownloadUrl);
            Log.d("updates", update);
        }
        getDataRef().updateChildren(map);

    }

    private void dispatchPictureIntent(final int requestCode) {
        // Select image for image message on click.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }


}
