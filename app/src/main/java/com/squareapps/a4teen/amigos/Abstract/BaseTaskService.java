package com.squareapps.a4teen.amigos.Abstract;

import android.app.IntentService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public abstract class BaseTaskService extends IntentService {

    static final int PROGRESS_NOTIFICATION_ID = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseTaskService(String name) {
        super(name);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    public DatabaseReference getDataRef() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
