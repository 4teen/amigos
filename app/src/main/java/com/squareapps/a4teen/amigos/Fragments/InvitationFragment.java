package com.squareapps.a4teen.amigos.Fragments;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareapps.a4teen.amigos.Common.POJOS.Invitation;
import com.squareapps.a4teen.amigos.R;
import com.squareapps.a4teen.amigos.databinding.InvitationDetailBinding;

public class InvitationFragment extends Fragment {

    public static InvitationFragment newInstance(Bundle bundle) {

        InvitationFragment fragment = new InvitationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.invitation_detail, container, false);
        InvitationDetailBinding binding = DataBindingUtil.bind(rootView);

        Invitation invitation = new Invitation(getArguments());
        binding.setInvitation(invitation);
        binding.executePendingBindings();

        return rootView;

    }
}

