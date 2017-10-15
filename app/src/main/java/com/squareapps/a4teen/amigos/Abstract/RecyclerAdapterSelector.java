package com.squareapps.a4teen.amigos.Abstract;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by y-pol on 10/9/2017.
 */

public abstract class RecyclerAdapterSelector<T, VH extends HolderBase> extends RecyclerView.Adapter<VH> {

    private SparseBooleanArray selectedItems;

    public RecyclerAdapterSelector() {
        this.selectedItems = new SparseBooleanArray();
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
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public boolean isSelected(int pos) {
        return selectedItems.get(pos);
    }
}
