package com.square.apps.amigos.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.square.apps.amigos.R;

/**
 * Created by y-pol on 7/3/2017.
 */

public class AddGroupDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.dialog_fragment, null))
                .setPositiveButton(R.string.create_group, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

    }
}

