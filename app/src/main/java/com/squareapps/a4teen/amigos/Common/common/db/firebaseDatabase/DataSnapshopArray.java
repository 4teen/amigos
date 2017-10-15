package com.squareapps.a4teen.amigos.Common.common.db.firebaseDatabase;

import com.firebase.ui.database.ChangeEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DataSnapshopArray implements ValueEventListener, ChildEventListener {

    private Query reference;
    private ArrayList<DataSnapshot> mSnapshots = new ArrayList<>();
    private ChangeEventListener mListener;

    public DataSnapshopArray(Query reference) {
        this.reference = reference;
        this.reference.addChildEventListener(this);
        this.reference.addValueEventListener(this);
    }

    private int size() {
        return mSnapshots.size();
    }

    private boolean isEmpty() {
        return mSnapshots.isEmpty();
    }

    private DataSnapshot get(int index) {
        return mSnapshots.get(index);
    }

    public void setOnChangedListener(ChangeEventListener listener) {
        mListener = listener;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        int index = 0;
        if (s != null) {
            index = getIndexForKey(s) + 1;
        }
        mSnapshots.add(index, dataSnapshot);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        int index = getIndexForKey(dataSnapshot.getKey());
        mSnapshots.set(index, dataSnapshot);

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
}
