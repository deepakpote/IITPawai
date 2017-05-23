/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.NewUser;
import net.mavericklabs.mitra.model.api.RegisterWithGoogle;
import net.mavericklabs.mitra.model.api.RegisterWithGoogleUserResponse;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInWithGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.sign_in_button)
    SignInButton signInButton;

    private int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private boolean isFromSignIn;

    @OnClick(R.id.sign_in_button) void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_google);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        isFromSignIn = bundle.getBoolean("is_from_sign_in");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(SignInWithGoogleActivity.this, "Authentication failed." + connectionResult.getErrorMessage(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Logger.d("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Logger.d(" email " + acct.getEmail());
            Logger.d(" token " + acct.getIdToken());
            firebaseAuthWithGoogle(acct);
        } else {
            // Signed out, show unauthenticated UI.
            proceed(null, null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Logger.d("current user " + currentUser);
        if(currentUser != null) {
            proceed(currentUser, null);
        }

    }

    private void proceed(FirebaseUser currentUser, String idToken) {
        //Call to server here.
        //If registering, take to profile. Else sign in and proceed.
        if (currentUser != null) {
            if (isFromSignIn) {
                int authenticationType = NewUser.TYPE_SIGN_IN;
                Intent home = new Intent(SignInWithGoogleActivity.this, HomeActivity.class);
                startActivity(home);
                finishAffinity();

            } else {

                RestClient.getApiService("").registerUserWithGoogle(new RegisterWithGoogle(idToken)).enqueue(
                        new Callback<BaseModel<RegisterWithGoogleUserResponse>>() {
                            @Override
                            public void onResponse(Call<BaseModel<RegisterWithGoogleUserResponse>> call,
                                                   Response<BaseModel<RegisterWithGoogleUserResponse>> response) {
                                int authenticationType = NewUser.TYPE_REGISTER;
                                Intent almostDone = new Intent(SignInWithGoogleActivity.this, AlmostDoneActivity.class);
                                startActivity(almostDone);
                                finishAffinity();

                            }

                            @Override
                            public void onFailure(Call<BaseModel<RegisterWithGoogleUserResponse>> call, Throwable t) {

                            }
                        });


            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Logger.d( "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Logger.d( "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            proceed(user, acct.getIdToken());
                        } else {
                            // If sign in fails, display a message to the user.
                            Logger.d( "signInWithCredential:failure" + task.getException());
                            Toast.makeText(SignInWithGoogleActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            proceed(null, null);
                        }
                    }
                });
    }

}
