package com.squareapps.a4teen.amigos.Abstract;

import android.support.annotation.LayoutRes;
import android.util.SparseBooleanArray;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public abstract class FireBaseIndexRecyclerAdapterSelector<T, VH extends HolderBase<T>> extends FirebaseIndexRecyclerAdapter<T, VH> {

    private SparseBooleanArray selectedItems;

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
    protected FireBaseIndexRecyclerAdapterSelector(Class<T> modelClass, @LayoutRes int modelLayout, Class<VH> viewHolderClass, Query keyRef, Query dataRef) {
        super(modelClass, modelLayout, viewHolderClass, keyRef, dataRef);
        selectedItems = new SparseBooleanArray();
    }


    @Override
    protected abstract void populateViewHolder(VH viewHolder, T model, int position);


    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public boolean isSelected(int pos) {
        return selectedItems.get(pos);
    }


}
