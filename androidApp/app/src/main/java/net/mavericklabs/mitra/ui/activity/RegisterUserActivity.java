package net.mavericklabs.mitra.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.NewUser;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {


    @BindView(R.id.email_edit_text)
    TextInputEditText emailAddressEditText;

    @OnClick(R.id.sign_in_button)
    void register () {
        Intent signIn = new Intent(RegisterUserActivity.this,SignInUserActivity.class);
        startActivity(signIn);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //emailAddressEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
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
            if (isValidEmailAddress()) {
                Call<BaseModel<GenericListDataModel>> requestOtp = RestClient.getApiService("").
                        requestOtp(new NewUser(StringUtils.removeAllWhitespace(emailAddressEditText.getText().toString()),
                                        NewUser.TYPE_REGISTER));
                final ProgressDialog progressDialog = new ProgressDialog(RegisterUserActivity.this,
                                                            R.style.ProgressDialog);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
                requestOtp.enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent verifyOtp = new Intent(RegisterUserActivity.this,VerifyOtpActivity.class);
                            String email = StringUtils.removeAllWhitespace(emailAddressEditText.getText().toString());
                            UserDetailUtils.saveEmailAddress(email,getApplicationContext());
                            UserDetailUtils.setVerifiedEmailAddress(getApplicationContext(),false);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            verifyOtp.putExtras(bundle);
                            startActivity(verifyOtp);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_email_address,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidEmailAddress() {
        return Patterns.EMAIL_ADDRESS.matcher(emailAddressEditText.getText()).matches();
    }
}
