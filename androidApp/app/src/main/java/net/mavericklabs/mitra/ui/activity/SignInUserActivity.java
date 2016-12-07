package net.mavericklabs.mitra.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

public class SignInUserActivity extends AppCompatActivity {

    @BindView(R.id.register_button)
    Button signIn;

    @BindView(R.id.email_edit_text)
    TextInputEditText emailEditText;

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
                        requestOtp(new NewUser(StringUtils.removeAllWhitespace(emailEditText.getText().toString()),
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
                            String email = StringUtils.removeAllWhitespace(emailEditText.getText().toString());
                            UserDetailUtils.saveEmailAddress(email,getApplicationContext());
                            UserDetailUtils.setVerifiedEmailAddress(getApplicationContext(),false);
                            Intent verifyOtp = new Intent(SignInUserActivity.this,VerifyOtpActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putBoolean("is_from_sign_in",true);
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

    private boolean isValidPhoneNumber() {
        return Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches();
    }
}
