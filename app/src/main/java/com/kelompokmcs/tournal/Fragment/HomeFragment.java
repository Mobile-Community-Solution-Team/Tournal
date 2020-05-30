package com.kelompokmcs.tournal.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideOption;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.kelompokmcs.tournal.Activity.CreateGroupActivity;
import com.kelompokmcs.tournal.Activity.JoinGroupActivity;
import com.kelompokmcs.tournal.R;

public class HomeFragment extends Fragment {

    private CardView cvJoinGroup, cvCreateGroup;
    private TextView tvUserName;
    private ImageView ivUserPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();

        cvJoinGroup = rootView.findViewById(R.id.cv_join_group);
        cvCreateGroup = rootView.findViewById(R.id.cv_create_group);
        tvUserName = rootView.findViewById(R.id.tv_user_name);
        ivUserPhoto = rootView.findViewById(R.id.iv_user_photo);

        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(getContext());

        tvUserName.setText(user.getDisplayName());
        Glide.with(getContext()).load(user.getPhotoUrl()).centerCrop().into(ivUserPhoto);

        cvJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), JoinGroupActivity.class);
                startActivity(i);
            }
        });

        cvCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(i);
            }
        });
    }
}
