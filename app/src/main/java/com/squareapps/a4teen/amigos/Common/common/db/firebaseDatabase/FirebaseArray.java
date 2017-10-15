package com.squareapps.a4teen.amigos.Common.common.db.firebaseDatabase;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y-pol on 10/13/2017.
 */

public class FirebaseArray implements ChildEventListener {
    public interface ChangeEventListener {
        enum EventType {ADDED, CHANGED, REMOVED, MOVED}

        void onChanged(EventType type, int index, int oldIndex);

        void onCancelled(DatabaseError error);
    }

    protected ChangeEventListener mListener;
    protected boolean mIsListening;
    private Query mQuery;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public FirebaseArray(Query ref) {
        mQuery = ref;
    }

    public void setChangeEventListener(@NonNull ChangeEventListener listener) {
        if (mIsListening && listener == null) {
            throw new IllegalStateException("Listener cannot be null.");
        }
        mListener = listener;
    }

    public void startListening() {
        if (mListener == null) throw new IllegalStateException("Listener cannot be null.");
        mQuery.addChildEventListener(this);
        mIsListening = true;
    }

    public void stopListening() {
        mQuery.removeEventListener(this);
        mSnapshots.clear();
        mIsListening = false;
    }

    public int size() {
        return mSnapshots.size();
    }

    public DataSnapshot get(int index) {
        return mSnapshots.get(index);
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        int index = 0;
        if (previousChildKey != null) {
            index = getIndexForKey(previousChildKey) + 1;
        }
        mSnapshots.add(index, snapshot);
        notifyChangeListener(ChangeEventListener.EventType.ADDED, index);
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        int index = getIndexForKey(snapshot.getKey());
        mSnapshots.set(index, snapshot);
        notifyChangeListener(ChangeEventListener.EventType.CHANGED, index);
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        int index = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(index);
        notifyChangeListener(ChangeEventListener.EventType.REMOVED, index);
    }



    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        int oldIndex = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(oldIndex);
        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
        mSnapshots.add(newIndex, snapshot);
        mListener.onChanged(ChangeEventListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override
    public void onCancelled(DatabaseError error) {
        mListener.onCancelled(error);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    protected void notifyChangeListener(ChangeEventListener.EventType type, int index) {
        mListener.onChanged(type, index, -1);
    }
}