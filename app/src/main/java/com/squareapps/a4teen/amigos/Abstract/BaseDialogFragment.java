package com.squareapps.a4teen.amigos.Abstract;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.squareapps.a4teen.amigos.R;


public abstract class BaseDialogFragment extends DialogFragment {

    public static final String EXTRA1 = "extra1";

    protected View dialogView;

    protected abstract Dialog createDialog();

    protected int getLayoutRes() {
        return R.layout.dialog_fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogView = LayoutInflater.from(getActivity())
                .inflate(getLayoutRes(), null);

        return createDialog();
    }

    protected void sendResult(int resultCode, String groupName) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA1, groupName);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
