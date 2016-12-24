package net.mavericklabs.mitra.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.LoginUser;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.Token;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;
import net.mavericklabs.mitra.database.model.DbGrade;
import net.mavericklabs.mitra.database.model.DbSubject;
import net.mavericklabs.mitra.database.model.DbTopic;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;

public class VerifyOtpActivity extends AppCompatActivity {

    @BindView(R.id.entered_phone_number_edit_text)
    EditText enteredPhoneNumberEditText;

    @BindView(R.id.resend_otp_button)
    Button resendOtpButton;

    @BindView(R.id.otp_edit_text)
    EditText otpEditText;

    @OnTouch(R.id.entered_phone_number_edit_text)
    boolean editPhoneNumber(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {

            AlertDialog dialog = new AlertDialog.Builder(VerifyOtpActivity.this)
                                        .setMessage(R.string.edit_your_phone_number)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                VerifyOtpActivity.super.onBackPressed();
                                            }
                                        })
                                        .setNegativeButton("No",null)
                                        .create();
            dialog.show();
            return true;
        }
        return false;
    }

    @OnClick(R.id.resend_otp_button)
    void resendOtp() {
        Call<BaseModel<GenericListDataModel>> requestOtp;
        if(isFromSignIn) {
            requestOtp = RestClient.getApiService("").
                    requestOtp(new NewUser(StringUtils.removeAllWhitespace(phoneNumber),
                            NewUser.TYPE_SIGN_IN));
        } else {
            requestOtp = RestClient.getApiService("").
                    requestOtp(new NewUser(StringUtils.removeAllWhitespace(phoneNumber),
                            NewUser.TYPE_REGISTER));
        }
        requestOtp.enqueue(new Callback<BaseModel<GenericListDataModel>>() {
            @Override
            public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.otp_sent,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                //TODO show error
            }
        });
    }

    private String phoneNumber = "";
    private boolean isFromSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            phoneNumber = bundle.getString("phone_number");
            isFromSignIn = bundle.getBoolean("is_from_sign_in");
            MitraSharedPreferences.saveToPreferences(getApplicationContext(),"sign_in",Boolean.valueOf(isFromSignIn));
            Logger.d("sign in.." + isFromSignIn);
            String formattedNumber;
            if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber,"in");
            } else {
                formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber);
            }
            enteredPhoneNumberEditText.setText(formattedNumber);
            enteredPhoneNumberEditText.setKeyListener(null);
        }

        otpEditText.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_next,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.action_next) {
            if (isValidOtp()) {
                int authenticationType;
                if(isFromSignIn) {
                    authenticationType = NewUser.TYPE_SIGN_IN;
                } else {
                    authenticationType = NewUser.TYPE_REGISTER;
                }
                String token = FirebaseInstanceId.getInstance().getToken();
                VerifyUserOtp verifyUserOtp = new VerifyUserOtp(phoneNumber,otpEditText.getText().toString(),
                                                    authenticationType,token);

                final ProgressDialog progressDialog = new ProgressDialog(VerifyOtpActivity.this,
                        R.style.ProgressDialog);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();

                RestClient.getApiService("").verifyOtp(verifyUserOtp).enqueue(new Callback<BaseModel<Token>>() {
                    @Override
                    public void onResponse(Call<BaseModel<Token>> call, Response<BaseModel<Token>> response) {
                        if(response.isSuccessful()) {
                            if(isFromSignIn) {
                                String token = response.body().getData().get(0).getToken();
                                String userId = response.body().getData().get(0).getUserId();
                                UserDetailUtils.saveToken(token,getApplicationContext());
                                UserDetailUtils.saveUserId(userId,getApplicationContext());
                                Logger.d("User id saved.." + userId);
                                UserDetailUtils.setVerifiedMobileNumber(getApplicationContext(),true);
                                fetchUserDetails(progressDialog);
                            } else {
                                progressDialog.dismiss();
                                UserDetailUtils.setVerifiedMobileNumber(getApplicationContext(),true);
                                Intent almostDone = new Intent(VerifyOtpActivity.this,AlmostDoneActivity.class);
                                MitraSharedPreferences.saveToPreferences(getApplicationContext(), "OTP", otpEditText.getText().toString());
                                startActivity(almostDone);
                            }
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.error_invalid_otp,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<Token>> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_please_enter_6_digit_otp,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserDetails(final ProgressDialog progressDialog) {
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

                            if(!StringUtils.isEmpty(user.getSubjectCodeIDs())) {
                                List<Integer> subjectCodes = StringUtils.splitCommas(user.getSubjectCodeIDs());
                                RealmList<DbSubject> dbSubjects = new RealmList<DbSubject>();
                                for(Integer code : subjectCodes) {
                                    dbSubjects.add(new DbSubject(code));
                                }
                                dbUser.setSubjects(dbSubjects);
                            }

                            if(!StringUtils.isEmpty(user.getGradeCodeIDs())) {
                                List<Integer> gradeCodes = StringUtils.splitCommas(user.getGradeCodeIDs());
                                RealmList<DbGrade> dbGrade = new RealmList<DbGrade>();
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

                            UserDetailUtils.setEnteredInformation(getApplicationContext(),Boolean.TRUE);

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            realm.copyToRealm(dbUser);
                            realm.commitTransaction();

                            progressDialog.dismiss();
                            Intent home = new Intent(VerifyOtpActivity.this,HomeActivity.class);
                            startActivity(home);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<BaseModel<LoginUser>> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //do nothing..
    }

    private boolean isValidOtp() {
        return otpEditText.getText().length() == 6;
    }
}
