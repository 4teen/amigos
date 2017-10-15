package com.squareapps.a4teen.amigos.Abstract;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;


public abstract class FireBaseIndexRecyclerAdapterSelector<T, VH extends HolderBase>
        extends FirebaseRecyclerAdapter<T, VH> {

    private SparseBooleanArray selectedItems;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FireBaseIndexRecyclerAdapterSelector(FirebaseRecyclerOptions<T> options) {
        super(options);
        selectedItems = new SparseBooleanArray();
    }


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
