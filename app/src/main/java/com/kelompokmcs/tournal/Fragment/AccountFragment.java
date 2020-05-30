package com.kelompokmcs.tournal.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.R;
import com.kelompokmcs.tournal.Activity.SignInActivity;

public class AccountFragment extends Fragment {

    private TextView tvName, tvGivenName,tvFamilyName,tvEmail, tvId;
    private ImageView ivPersonPhoto;
    private Button btnSignOut;
    private DBTransaction dbTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();

        tvName = rootView.findViewById(R.id.tv_name);
        tvGivenName = rootView.findViewById(R.id.tv_given_name);
        tvFamilyName = rootView.findViewById(R.id.tv_family_name);
        tvEmail = rootView.findViewById(R.id.tv_email);
        tvId = rootView.findViewById(R.id.tv_id);
        ivPersonPhoto = rootView.findViewById(R.id.iv_person_photo);
        btnSignOut = rootView.findViewById(R.id.btn_sign_out);
        dbTransaction = new DBTransaction(getContext());

        GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(rootView.getContext());

        String personName = signedInAccount.getDisplayName();
        String personGivenName = signedInAccount.getGivenName();
        String personFamilyName = signedInAccount.getFamilyName();
        String personEmail = signedInAccount.getEmail();
        String personId = signedInAccount.getId();
        Uri personPhoto = signedInAccount.getPhotoUrl();

        tvName.setText(personName);
        tvGivenName.setText(personGivenName);
        tvFamilyName.setText(personFamilyName);
        tvEmail.setText(personEmail);
        tvId.setText(personId);
        Glide.with(this).load(personPhoto).centerCrop().into(ivPersonPhoto);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //delete all data in all table
                        dbTransaction.deleteAllTable();
                        //then move to sign in page
                        Intent i = new Intent(getActivity(),SignInActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
    }

}
