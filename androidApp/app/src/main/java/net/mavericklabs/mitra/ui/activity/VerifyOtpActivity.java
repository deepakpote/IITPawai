package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyOtpActivity extends AppCompatActivity {

    @BindView(R.id.entered_phone_number_edit_text)
    EditText enteredPhoneNumberEditText;

    @BindView(R.id.resend_otp_button)
    Button resendOtpButton;

    @BindView(R.id.otp_edit_text)
    EditText otpEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        String phoneNumber = "";

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            phoneNumber = bundle.getString("phone_number");
            enteredPhoneNumberEditText.setText(phoneNumber);
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
                Intent verifyOtp = new Intent(VerifyOtpActivity.this,AcceptUserInfoActivity.class);
                startActivity(verifyOtp);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_please_enter_4_digit_otp,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isValidOtp() {
        return otpEditText.getText().length() == 4;
    }
}
