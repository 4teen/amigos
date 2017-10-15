package com.squareapps.a4teen.amigos.Common.common.db.firebaseDatabase;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.squareapps.a4teen.amigos.Common.JoinResolver;


public abstract class FireBaseIndexResolverAdapter<T, VH extends RecyclerView.ViewHolder>
        extends FirebaseRecyclerAdapter<T, VH> implements JoinResolver {

    private final static String TAG = FireBaseIndexResolverAdapter.class.getSimpleName();

    protected Query mDataQuery;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance
     *                        of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param keyRef          The Firebase location containing the list of keys to be found in {@code dataRef}.
     *                        Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     * @param dataRef         The Firebase location to watch for data changes.
     *                        Each key key found at {@code keyRef}'s location represents
     *                        a list item in the {@code RecyclerView}.
     */
    public FireBaseIndexResolverAdapter(Class<T> modelClass,
                                        @LayoutRes int modelLayout,
                                        Class<VH> viewHolderClass,
                                        Query keyRef,
                                        Query dataRef) {
        super(modelClass, modelLayout, viewHolderClass, new FirebaseIndexArray(keyRef), false);
        mDataQuery = dataRef;
        ((FirebaseIndexArray) mSnapshots).setJoinResolver(this);
        mSnapshots.startListening();
    }

    @Override
    public Query onJoin(DataSnapshot keySnapshot, String previousChildKey) {
        return mDataQuery.getRef().child(keySnapshot.getKey());
    }

    @Override
    public Query onDisjoin(DataSnapshot keySnapshot) {
        return mDataQuery.getRef().child(keySnapshot.getKey());
    }

    @Override
    public void onJoinFailed(int index, DataSnapshot snapshot) {
        Log.w(TAG, "Key not found at ref " + snapshot.getRef() + " for index " + index + ".");
    }

}
