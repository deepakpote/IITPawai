package net.mavericklabs.mitra.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.api.NewUser;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInUserActivity extends BaseActivity {

    @BindView(R.id.register_button)
    Button signIn;

    @BindView(R.id.phone_number_edit_text)
    TextInputEditText phoneNumberEditText;

    @OnClick(R.id.register_button)
    void register () {
        Intent register = new Intent(SignInUserActivity.this, RegisterUserActivity.class);
        startActivity(register);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_user);
        ButterKnife.bind(this);

        phoneNumberEditText.setSelection(3);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
            if (isValidPhoneNumber()) {
                Call<BaseModel<GenericListDataModel>> requestOtp = RestClient.getApiService("").
                        requestOtp(new NewUser(StringUtils.removeAllWhitespace(phoneNumberEditText.getText().toString()),
                                                NewUser.TYPE_SIGN_IN));
                final ProgressDialog progressDialog = new ProgressDialog(SignInUserActivity.this,
                        R.style.ProgressDialog);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
                requestOtp.enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()) {
                            String phoneNumber = StringUtils.removeAllWhitespace(phoneNumberEditText.getText().toString());
                            UserDetailUtils.saveMobileNumber(phoneNumber,getApplicationContext());
                            UserDetailUtils.setVerifiedMobileNumber(getApplicationContext(),false);
                            Intent verifyOtp = new Intent(SignInUserActivity.this,VerifyOtpActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone_number", phoneNumber);
                            bundle.putBoolean("is_from_sign_in",true);
                            verifyOtp.putExtras(bundle);
                            startActivity(verifyOtp);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_please_register,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Toast.makeText(SignInUserActivity.this, getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),"Please enter 10 digit phone number",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidPhoneNumber() {
        return phoneNumberEditText.getText().length() == 13;
    }
}
