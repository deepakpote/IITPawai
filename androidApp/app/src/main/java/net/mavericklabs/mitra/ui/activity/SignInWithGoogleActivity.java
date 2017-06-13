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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.LoginUser;
import net.mavericklabs.mitra.model.api.RegisterWithGoogle;
import net.mavericklabs.mitra.model.api.RegisterWithGoogleUserResponse;
import net.mavericklabs.mitra.model.database.DbGrade;
import net.mavericklabs.mitra.model.database.DbSubject;
import net.mavericklabs.mitra.model.database.DbTopic;
import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.net.ConnectException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInWithGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.sign_in_button)
    SignInButton signInButton;

    private int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private boolean setEmail = false;

    @OnClick(R.id.sign_in_button) void signIn() {
        signInButton.setEnabled(false);

        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        signInButton.setEnabled(true);
                    }
                });
            }
        }, 5000);

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
        if(bundle != null && bundle.containsKey("set_email")) {
            setEmail = bundle.getBoolean("set_email");
        }

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
            progressDialog = new ProgressDialog(SignInWithGoogleActivity.this,
                    R.style.ProgressDialog);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();

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
            UserDetailUtils.saveEmail(acct.getEmail(), getApplicationContext());
            firebaseAuthWithGoogle(acct);
        } else {
            // Signed out, show unauthenticated UI.
            proceed(null, null);
        }
    }

    private void proceed(FirebaseUser currentUser, final String idToken) {
        //Call to server here.
        if (currentUser != null) {

            if (setEmail) {
                //User is already signed in. Accept and set email.
                String authToken = UserDetailUtils.getToken(getApplicationContext());
                RestClient.getApiService(authToken).setEmail(new RegisterWithGoogle(idToken,"", "false")).enqueue(
                        new Callback<BaseModel<RegisterWithGoogleUserResponse>>() {
                            @Override
                            public void onResponse(Call<BaseModel<RegisterWithGoogleUserResponse>> call, Response<BaseModel<RegisterWithGoogleUserResponse>> response) {
                                progressDialog.dismiss();
                                if (response.isSuccessful()) {
                                    Intent home = new Intent(SignInWithGoogleActivity.this, HomeActivity.class);
                                    startActivity(home);
                                    finishAffinity();
                                } else {
                                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessageForSetEmail(response)).getCodeNameForCurrentLocale();
                                    if(!StringUtils.isEmpty(error)) {
                                        Toast.makeText(SignInWithGoogleActivity.this, error, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignInWithGoogleActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<BaseModel<RegisterWithGoogleUserResponse>> call, Throwable t) {
                                progressDialog.dismiss();
                                if(t instanceof ConnectException) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            } else {

                String fcmToken = FirebaseInstanceId.getInstance().getToken();
                RestClient.getApiService("").registerUserWithGoogle(new RegisterWithGoogle(idToken, fcmToken, "true")).enqueue(
                        new Callback<BaseModel<RegisterWithGoogleUserResponse>>() {
                            @Override
                            public void onResponse(Call<BaseModel<RegisterWithGoogleUserResponse>> call,
                                                   Response<BaseModel<RegisterWithGoogleUserResponse>> response) {
                                if (response.isSuccessful()) {
                                    //Till we get permanent token, save google token
                                    UserDetailUtils.saveGoogleToken(idToken, getApplicationContext());

                                    if (response.body().getResponseMessage().equals(100110)) {
                                        progressDialog.dismiss();

                                        //new email for Mitra. Ask if existing user with phone no -

                                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View dialogLayout = layoutInflater.inflate(R.layout.sign_in_chooser_dialog, null);

                                        Button phone = (Button) dialogLayout.findViewById(R.id.phone_number_button);
                                        Button notRegistered = (Button) dialogLayout.findViewById(R.id.not_registered);

                                        phone.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent signIn = new Intent(SignInWithGoogleActivity.this, SignInUserActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putBoolean("is_from_sign_in", true);
                                                signIn.putExtras(bundle);
                                                startActivity(signIn);

                                            }
                                        });

                                        notRegistered.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent almostDone = new Intent(SignInWithGoogleActivity.this, AlmostDoneActivity.class);
                                                startActivity(almostDone);

                                            }
                                        });


                                        AlertDialog alertDialog = new AlertDialog.Builder(SignInWithGoogleActivity.this)
                                                .setView(dialogLayout)
                                                .create();

                                        alertDialog.show();


                                    } else {
                                        //existing user. Get user details
                                        String token = response.body().getData().get(0).getToken();
                                        String userId = response.body().getData().get(0).getUserID();

                                        UserDetailUtils.saveToken(token, getApplicationContext());
                                        UserDetailUtils.saveUserId(userId, getApplicationContext());

                                        getUserDetails();

                                    }

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<BaseModel<RegisterWithGoogleUserResponse>> call, Throwable t) {
                                progressDialog.dismiss();
                                if(t instanceof ConnectException) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                }

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

    private void getUserDetails() {
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).getUserDetails()
                .enqueue(new Callback<BaseModel<LoginUser>>() {
                    @Override
                    public void onResponse(Call<BaseModel<LoginUser>> call, Response<BaseModel<LoginUser>> response) {
                        if(response.isSuccessful()) {
                            LoginUser user = response.body().getData().get(0);
                            DbUser dbUser = new DbUser(user.getUserName(),user.getUserType(),user.getDistrict());
                            dbUser.setPreferredLanguage(user.getPreferredLanguage());
                            dbUser.setUdise(user.getUdiseCode());

                            CommonCode language = CommonCodeUtils.getObjectFromCode(user.getPreferredLanguage());
                            Logger.d(" language " + language.getCodeID() + language.getCodeNameEnglish());
                            int lang = Constants.AppLanguageEnglish;
                            if(language.getCodeNameEnglish().equals("Marathi")) {
                                lang = Constants.AppLanguageMarathi;
                            }
                            LanguageUtils.setLocale(lang, getApplicationContext());

                            if(!StringUtils.isEmpty(user.getSubjectCodeIDs())) {
                                List<Integer> subjectCodes = StringUtils.splitCommas(user.getSubjectCodeIDs());
                                RealmList<DbSubject> dbSubjects = new RealmList<>();
                                for(Integer code : subjectCodes) {
                                    dbSubjects.add(new DbSubject(code));
                                }
                                dbUser.setSubjects(dbSubjects);
                            }

                            if(!StringUtils.isEmpty(user.getGradeCodeIDs())) {
                                List<Integer> gradeCodes = StringUtils.splitCommas(user.getGradeCodeIDs());
                                RealmList<DbGrade> dbGrade = new RealmList<>();
                                for(Integer code : gradeCodes) {
                                    dbGrade.add(new DbGrade(code));
                                }
                                dbUser.setGrades(dbGrade);
                            }

                            if(!StringUtils.isEmpty(user.getTopicCodeIDs())) {
                                List<Integer> topicCodes = StringUtils.splitCommas(user.getTopicCodeIDs());
                                RealmList<DbTopic> dbTopic = new RealmList<>();
                                for(Integer code : topicCodes) {
                                    dbTopic.add(new DbTopic(code));
                                }
                                dbUser.setTopics(dbTopic);
                            }

                            if(!StringUtils.isEmpty(user.getPhotoUrl())) {
                                dbUser.setProfilePhotoPath(user.getPhotoUrl());
                            }

                            dbUser.setDepartmentID(user.getDepartment());


                            UserDetailUtils.setEnteredInformation(getApplicationContext(),Boolean.TRUE);

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            realm.copyToRealmOrUpdate(dbUser);
                            realm.commitTransaction();

                            progressDialog.dismiss();

                            // Obtain the FirebaseAnalytics instance.
                            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                            firebaseAnalytics.setUserId(user.getUserID());
                            firebaseAnalytics.setUserProperty("district",
                                    CommonCodeUtils.getObjectFromCode(user.getDistrict()).getCodeNameEnglish());

                            Intent home = new Intent(SignInWithGoogleActivity.this,HomeActivity.class);
                            startActivity(home);
                            finishAffinity();
                        } else {
                            Toast.makeText(SignInWithGoogleActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<LoginUser>> call, Throwable t) {
                        if(t instanceof ConnectException) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
