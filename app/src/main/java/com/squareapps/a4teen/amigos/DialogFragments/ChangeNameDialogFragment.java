package com.squareapps.a4teen.amigos.DialogFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;

/**
 * Created by y-pol on 7/8/2017.
 */

public class ChangeNameDialogFragment extends BaseDialogFragment {

    @Override
    protected Dialog createDialog() {

        dialogViewTex2.setText("RENAME GROUP");
        dialogViewTex1.setHint("New Group Name");

        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = dialogViewTex1.getText().toString().trim();

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
}
