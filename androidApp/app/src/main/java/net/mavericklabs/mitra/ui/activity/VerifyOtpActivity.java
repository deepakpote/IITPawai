package net.mavericklabs.mitra.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.api.model.Token;
import net.mavericklabs.mitra.api.model.VerifyUserOtp;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    @BindView(R.id.entered_phone_number_edit_text)
    EditText enteredEmailEditText;

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
                    requestOtp(new NewUser(StringUtils.removeAllWhitespace(email),
                            NewUser.TYPE_SIGN_IN));
        } else {
            requestOtp = RestClient.getApiService("").
                    requestOtp(new NewUser(StringUtils.removeAllWhitespace(email),
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

    private String email = "";
    private boolean isFromSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            email = bundle.getString("email");
            isFromSignIn = bundle.getBoolean("is_from_sign_in");
            MitraSharedPreferences.saveToPreferences(getApplicationContext(),"sign_in",Boolean.valueOf(isFromSignIn));
            enteredEmailEditText.setKeyListener(null);
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
                String authenticationType;
                if(isFromSignIn) {
                    authenticationType = NewUser.TYPE_SIGN_IN;
                } else {
                    authenticationType = NewUser.TYPE_REGISTER;
                }
                VerifyUserOtp verifyUserOtp = new VerifyUserOtp(email,otpEditText.getText().toString(), authenticationType);

                final ProgressDialog progressDialog = new ProgressDialog(VerifyOtpActivity.this,
                        R.style.ProgressDialog);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();

                RestClient.getApiService("").verifyOtp(verifyUserOtp).enqueue(new Callback<BaseModel<Token>>() {
                    @Override
                    public void onResponse(Call<BaseModel<Token>> call, Response<BaseModel<Token>> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()) {
                            if(isFromSignIn) {
                                String token = response.body().getData().get(0).getToken();
                                UserDetailUtils.saveToken(token,getApplicationContext());
                                UserDetailUtils.setVerifiedEmailAddress(getApplicationContext(),true);
                                Intent home = new Intent(VerifyOtpActivity.this,HomeActivity.class);
                                startActivity(home);
                                finishAffinity();
                            } else {
                                UserDetailUtils.setVerifiedEmailAddress(getApplicationContext(),true);
                                Intent almostDone = new Intent(VerifyOtpActivity.this,AlmostDoneActivity.class);
                                MitraSharedPreferences.saveToPreferences(getApplicationContext(), "OTP", otpEditText.getText().toString());
                                startActivity(almostDone);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_please_enter_6_digit_otp,Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        //do nothing..
    }

    private boolean isValidOtp() {
        return otpEditText.getText().length() == 6;
    }
}
