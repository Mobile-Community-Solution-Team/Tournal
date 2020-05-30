package com.kelompokmcs.tournal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.API.VolleySingleton;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements RequestResult {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private APIRequest apiRequest;
    int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = findViewById(R.id.sign_in_button);
        apiRequest = new APIRequest(this);
        //standard size from google
        signInButton.setSize(SignInButton.SIZE_WIDE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent i = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            JsonObjectRequest request = apiRequest.addNewUser(account.getId(),account.getDisplayName(),account.getEmail(), String.valueOf(account.getPhotoUrl()));
            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (ApiException e) {
            Toast.makeText(this, "Failed to sign in. Please try again", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("addNewUser")){
            Intent i = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("addNewUser")){
            Toast.makeText(this, "Failed to sign in. Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
