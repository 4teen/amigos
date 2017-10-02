package com.squareapps.a4teen.amigos.DialogFragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.squareapps.a4teen.amigos.Abstract.BaseDialogFragment;
import com.squareapps.a4teen.amigos.R;

/**
 * Created by y-pol on 7/3/2017.
 */

public class AddGroupDialogFragment extends BaseDialogFragment {

    @Override
    protected Dialog createDialog() {
        dialogViewTex1 = (EditText) dialogView.findViewById(R.id.dialog_fragment_et);
        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
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

