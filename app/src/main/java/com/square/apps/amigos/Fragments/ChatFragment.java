package com.square.apps.amigos.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.square.apps.amigos.Activities.ChatMediaActivity;
import com.square.apps.amigos.Activities.ChatMembersActivity;
import com.square.apps.amigos.Activities.LoginActivity;
import com.square.apps.amigos.Activities.SearchUsersActivity;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.FirebaseUtils;
import com.square.apps.amigos.common.common.Message;
import com.square.apps.amigos.common.common.Photo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.square.apps.amigos.Contract.GROUPS;
import static com.square.apps.amigos.Contract.MEDIA;
import static com.square.apps.amigos.Contract.MESSAGE;
import static com.square.apps.amigos.Contract.MESSAGES;
import static com.square.apps.amigos.Contract.NAME;
import static com.square.apps.amigos.Contract.TIMESTAMP;
import static com.square.apps.amigos.Contract.USERS;


public class ChatFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    private static final String TAG = "ChatFragment";
    private static final int REQUEST_CHANGE_NAME = 0415;
    private static final int REQUEST_IMAGE = 0416;
    private final FirebaseUtils firebaseUtils = new FirebaseUtils() {
    };
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

    ImageView navHeaderImageView;

    private String mUsername;
    private String mUid;
    private String mPhotoUrl;
    private String groupID;
    private LinearLayoutManager mLinearLayoutManager;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;

    @NonNull
    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mUsername = mFirebaseUser.getDisplayName();
        mUid = mFirebaseUser.getUid();

        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();

        }
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_chat, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String groupName = bundle.getSerializable(Contract.GROUP_NAME).toString();
        groupID = bundle.getSerializable(Contract.GROUP_ID).toString();

        toolbar.setTitle(groupName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        View navheader = navigationView.getHeaderView(0);//navHeader index = 0
        navHeaderImageView = (ImageView) navheader.findViewById(R.id.nav_header_image_view);
        navHeaderImageView.setImageDrawable(getResources().getDrawable(R.drawable.tampa_skyline));


        // Adding menu icon to Toolbar
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, getActivity().getTheme());
            indicator
                    .setTint(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()));

            supportActionBar.setHomeAsUpIndicator(indicator);

            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        setupNavigationView();


        if (groupID == null) throw new AssertionError();

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        //[START initialize_recycler_view]
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        //[END initialize_recycler_view]

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
        //[END initialize_mMesageEditText_TextWatcher]

        //[START initiliaze_mSendButton]
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageId = mFirebaseDatabaseReference
                        .child(MESSAGES)
                        .child(groupID)
                        .push().getKey();

                final Message friendlyMessage = new Message(mUid,
                        messageId,
                        mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl,
                        null /* no image */);

                mFirebaseDatabaseReference
                        .child(MESSAGES)
                        .child(groupID)
                        .child(messageId)
                        .setValue(friendlyMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    String key = databaseReference.getKey();

                                    DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
                                    Date date = new Date();

                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put(USERS + "/" + mUid + "/" + MESSAGES + "/" + key + "/" + groupID, true);
                                    map.put(GROUPS + "/" + groupID + "/" + MESSAGE, mUsername + ": " + friendlyMessage.getText());
                                    map.put(GROUPS + "/" + groupID + "/" + TIMESTAMP, dateFormat.format(date));
                                    updateDataSet(map);
                                } else {
                                    Log.w(TAG, "Unable to write " + MESSAGE + " to database.", databaseError.toException());
                                }


                            }
                        });

                mMessageEditText.setText("");
            }
        });
        //[END initiliaze_mSendButton]

        //[START initiliaze_AddMessageImageView]
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Select image for image message on click.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        //[END initiliaze_AddMessageImageView]

        //[START initialize_firebase_Adapter]
        // New child entries
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>
                (Message.class,
                        R.layout.activity_chat_list_item, //layout
                        MessageViewHolder.class,//viewholder
                        mFirebaseDatabaseReference.child(MESSAGES + "/" + groupID))

        {

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, Message model, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);


                if (model.getText() != null) {
                    viewHolder.messageTextView.setText(model.getText());

                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else {

                    firebaseUtils.setImageView(model.getImageUrl(), viewHolder.messageImageView);

                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);

                }

                //populate name of sender
                viewHolder.messengerTextView.setText(model.getName());

                firebaseUtils.setImageView(model.getPhotoUrl(), viewHolder.messengerImageView);

            }
            //[END_PopulateViewHolder]
        };
        //[END_firebaseRecyclerAdapter]


        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        registerForContextMenu(mMessageRecyclerView);
        return view;

    }

    private void setupNavigationView() {
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        switch (requestCode) {
            case REQUEST_IMAGE:
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());


                    String messageId = mFirebaseDatabaseReference
                            .child(MESSAGES)
                            .child(groupID)
                            .push().getKey();

                    Message tempMessage = new Message(
                            mUid,
                            messageId,
                            null,
                            mUsername,
                            mPhotoUrl,
                            LOADING_IMAGE_URL);


                    mFirebaseDatabaseReference
                            .child(MESSAGES)
                            .child(groupID)
                            .child(messageId)
                            .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();

                                        StorageReference storageReference = FirebaseStorage
                                                .getInstance()
                                                .getReference(mFirebaseUser.getUid())
                                                .child(key)
                                                .child(uri.getLastPathSegment());

                                        putImageInStorage(storageReference, uri, key);

                                    } else {
                                        Log.w(TAG, "Unable to write " + MESSAGE + " to database.", databaseError.toException());
                                    }
                                }
                            });

                }
                break;
            case REQUEST_CHANGE_NAME:
                String groupName = data
                        .getSerializableExtra(ChangeNameDialogFragment.EXTRA_CHANGE_NAME)
                        .toString();

                if (groupName != null) {
                    toolbar.setTitle(groupName);

                    HashMap<String, Object> groupsMap = new HashMap<>();
                    groupsMap.put(GROUPS + "/" + groupID + "/" + NAME + "/", groupName);

                    updateDataSet(groupsMap);
                }

                break;

            default:
        }
    }

    private void updateDataSet(HashMap<String, Object> groupsMap) {
        mFirebaseDatabaseReference.updateChildren(groupsMap);
        mFirebaseAdapter.notifyDataSetChanged();
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference
                .putFile(uri)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String imageUrl = task.getResult().getMetadata()
                                    .getDownloadUrl()
                                    .toString();

                            Photo photoItem = new Photo(imageUrl, mUid, groupID);
                            mFirebaseDatabaseReference.child(MEDIA).child(groupID).child(key).setValue(photoItem);

                            HashMap<String, Object> map = new HashMap<>();
                            map.put(MESSAGES + "/" + groupID + "/" + key + "/imageUrl", imageUrl);
                            map.put(USERS + "/" + mUid + "/" + MESSAGES + "/" + key + "/" + groupID, true);
                            updateDataSet(map);

                        } else {
                            Log.w(TAG, "Image upload task was not successful.", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                Intent i = new Intent(getActivity(), SearchUsersActivity.class);
                i.putExtra("groupID", groupID);
                startActivity(i);
                getActivity().finish();
                return true;

            case R.id.change_name:
                FragmentManager fragmentManager = getFragmentManager();
                ChangeNameDialogFragment changeNameDialogFragment = new ChangeNameDialogFragment();
                changeNameDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
                changeNameDialogFragment.setTargetFragment(this, REQUEST_CHANGE_NAME);
                changeNameDialogFragment.show(fragmentManager, "CHANGE NAME DIALOG FRAGMENT");
                return true;

            case R.id.change_avatar:
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
                Message message = mFirebaseAdapter.getItem(item.getOrder());
                Log.d(TAG, message.getId());

                HashMap<String, Object> map = new HashMap<>();
                map.put(MESSAGES + "/" + groupID + "/" + message.getId(), null);
                map.put(USERS + "/" + mUid + "/" + MESSAGES + "/" + message.getId(), null);
                updateDataSet(map);

                return true;
            case R.id.chat_fragment_copyText_item_menu:
                //deleteNote(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @StringDef({TAG,
            MESSAGES,
            LOADING_IMAGE_URL})

    @Retention(RetentionPolicy.SOURCE)
    public @interface ChatFragmentConstants {
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @BindView(R.id.messageTextView)
        TextView messageTextView;

        @BindView(R.id.messageImageView)
        ImageView messageImageView;

        @BindView(R.id.messengerTextView)
        TextView messengerTextView;

        @BindView(R.id.messengerImageView)
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            itemView.setOnCreateContextMenuListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Message Options");
            menu.add(0, R.id.chat_fragment_delete_item_menu, getAdapterPosition(), "Delete");
            menu.add(0, R.id.chat_fragment_copyText_item_menu, getAdapterPosition(), "Copy text");


        }
    }
}
