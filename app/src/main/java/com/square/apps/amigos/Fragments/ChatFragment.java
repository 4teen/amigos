package com.square.apps.amigos.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.square.apps.amigos.Activities.LoginActivity;
import com.square.apps.amigos.R;
import com.square.apps.amigos.common.common.Message;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    public static final  String MESSAGES_CHILD           = "messages";
    public static final  int    DEFAULT_MSG_LENGTH_LIMIT = 10;
    private static final String TAG                      = "ChatFragment";
    private static final int    REQUEST_IMAGE            = 2;
    private static final String LOADING_IMAGE_URL        = "https://www.google.com/images/spin-32.gif";
    private String                                              mUsername;
    private String                                              mPhotoUrl;
    private SharedPreferences                                   mSharedPreferences;
    private GoogleApiClient                                     mGoogleApiClient;
    private Button                                              mSendButton;
    private RecyclerView                                        mMessageRecyclerView;
    private LinearLayoutManager                                 mLinearLayoutManager;
    private ProgressBar                                         mProgressBar;
    private EditText                                            mMessageEditText;
    private ImageView                                           mAddMessageImageView;
    // Firebase instance variables
    private FirebaseAuth                                        mFirebaseAuth;
    private FirebaseUser                                        mFirebaseUser;
    // Firebase instance variables
    private DatabaseReference                                   mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    // Firebase instance variables
    private FirebaseRemoteConfig                                mFirebaseRemoteConfig;
    private FirebaseAnalytics                                   mFirebaseAnalytics;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_chat, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //[START initialize_recycler_view]
        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        //[END initialize_recycler_view]

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        //[START initialize_mMesageEditText_TextWatcher]
        mMessageEditText = (EditText) view.findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable){
            }
        });
        //[END initialize_mMesageEditText_TextWatcher]

        //[START initiliaze_mSendButton]
        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Message friendlyMessage = new Message(mMessageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl, null /* no image */);

                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                          .push().setValue(friendlyMessage);

                mMessageEditText.setText("");
            }
        });
        //[END initiliaze_mSendButton]

        //[START initiliaze_AddMessageImageView]
        mAddMessageImageView = (ImageView) view.findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

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
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>
                (Message.class,
                R.layout.activity_chat_list_item, //layout
                MessageViewHolder.class,//viewholder
                mFirebaseDatabaseReference.child(MESSAGES_CHILD))//reference to data
        {

            @Override
            protected Message parseSnapshot(DataSnapshot snapshot){
                Message friendlyMessage = super.parseSnapshot(snapshot);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(snapshot.getKey());
                }
                return friendlyMessage;
            }

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder, Message model, int position){
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                if (model.getText() != null) {
                    viewHolder.messageTextView.setText(model.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else {
                    String imageUrl = model.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task){
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with( viewHolder.messageImageView.getContext())
                                         .load(downloadUrl)
                                         .into(viewHolder.messageImageView);
                                } else {
                                    Log.w(TAG, "Getting download url was not successful.", task.getException());
                                }
                            }
                        });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                             .load(model.getImageUrl())
                             .into(viewHolder.messageImageView);
                    }

                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }

                //populate name of sender
                viewHolder.messengerTextView.setText(model.getName());

                //[START setting up profile image of message]
                if (model.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_face_black));
                } else {
                    Glide.with(getActivity())
                         .load(model.getPhotoUrl())
                         .into(viewHolder.messengerImageView);
                }
                //[END setting up profile image of message]
            }
            //[END_PopulateViewHolder]
        };
        //[END_firebaseRecyclerAdapter]


        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount){
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
        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    Message tempMessage = new Message(null, mUsername, mPhotoUrl, LOADING_IMAGE_URL);

                    mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                              .push().setValue(tempMessage, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference){
                            if (databaseError == null) {
                                String key = databaseReference.getKey();
                                StorageReference storageReference = FirebaseStorage
                                        .getInstance()
                                        .getReference(mFirebaseUser.getUid()).child(key).child(uri.getLastPathSegment());

                                putImageInStorage(storageReference, uri, key);
                            } else {
                                Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                            }
                        }
                    });
                }
            }
        }

    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key){

        storageReference
                .putFile(uri).addOnCompleteListener(getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task){
                if (task.isSuccessful()) {
                    Message friendlyMessage = new Message(null, mUsername, mPhotoUrl, task.getResult().getMetadata().getDownloadUrl().toString());
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key).setValue(friendlyMessage);
                } else {
                    Log.w(TAG, "Image upload task was not successful.", task.getException());
                }
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView        messageTextView;
        ImageView       messageImageView;
        TextView        messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v){
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }
}
