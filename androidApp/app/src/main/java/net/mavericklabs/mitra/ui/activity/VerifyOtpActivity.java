package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
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
            String formattedNumber;
            if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber,"in");
            } else {
                formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber);
            }
            enteredPhoneNumberEditText.setText(formattedNumber);
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
                VerifyUserOtp verifyUserOtp = new VerifyUserOtp(phoneNumber,otpEditText.getText().toString(), authenticationType);
                RestClient.getApiService("").verifyOtp(verifyUserOtp).enqueue(new Callback<BaseModel<Token>>() {
                    @Override
                    public void onResponse(Call<BaseModel<Token>> call, Response<BaseModel<Token>> response) {
                        if(response.isSuccessful()) {
                            if(isFromSignIn) {
                                String token = response.body().getData().get(0).getToken();
                                UserDetailUtils.saveToken(token,getApplicationContext());
                                Intent home = new Intent(VerifyOtpActivity.this,HomeActivity.class);
                                startActivity(home);
                                finishAffinity();
                            } else {
                                UserDetailUtils.saveMobileNumber(phoneNumber,getApplicationContext());
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

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_please_enter_6_digit_otp,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidOtp() {
        return otpEditText.getText().length() == 6;
    }
}
