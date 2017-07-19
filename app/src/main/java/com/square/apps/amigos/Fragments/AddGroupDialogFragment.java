package com.square.apps.amigos.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.square.apps.amigos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by y-pol on 7/3/2017.
 */

public class AddGroupDialogFragment extends DialogFragment {

    public static final String EXTRA_ADD_GROUP = "EXTRA_ADD_GROUP";

    @BindView(R.id.dialog_fragment_et)
    EditText dialogET;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_fragment, null);
        ButterKnife.bind(this, v);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = dialogET.getText().toString().trim();

                        if (!TextUtils.isEmpty(groupName))
                            sendResult(Activity.RESULT_OK, groupName);
                        else
                            dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Negative clicked", "Negative clicked");
                    }
                })
                .create();
    }


    private void sendResult(int resultCode, String groupName) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADD_GROUP, groupName);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}

