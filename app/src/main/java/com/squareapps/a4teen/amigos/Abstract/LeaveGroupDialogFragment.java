package com.squareapps.a4teen.amigos.Abstract;

/**
 * Created by y-pol on 2/28/2018.s
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareapps.a4teen.amigos.Fragments.InvitationListFragment;
import com.squareapps.a4teen.amigos.R;

import static com.firebase.ui.auth.ResultCodes.OK;

public class LeaveGroupDialogFragment extends DialogFragment implements View.OnClickListener {

    private DialogFragmentCallbacks callbacks;

    public static LeaveGroupDialogFragment newInstance(InvitationListFragment fragment, Bundle bundle) {
        LeaveGroupDialogFragment newFragment = new LeaveGroupDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.setTargetFragment(fragment, OK);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            callbacks = (DialogFragmentCallbacks) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getContext().toString()
                    + " must implement NoticeDialogListener");
        }

    }

    private int getLayoutRes() {
        return R.layout.leave_group_dialog_fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(getLayoutRes(), null);

        // Watch for button clicks.
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


        return view;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                callbacks.onDialogNegativeClick(this);
                break;
            case R.id.button2:
                callbacks.onDialogPositiveClick(this);
                break;
        }
    }

    public interface DialogFragmentCallbacks {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

}
