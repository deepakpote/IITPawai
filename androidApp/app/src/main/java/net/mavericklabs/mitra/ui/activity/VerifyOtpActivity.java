package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyOtpActivity extends AppCompatActivity {

    @BindView(R.id.entered_phone_number_edit_text)
    EditText enteredPhoneNumberEditText;

    @BindView(R.id.resend_otp_button)
    Button resendOtpButton;

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
            Intent verifyOtp = new Intent(VerifyOtpActivity.this,AcceptUserInfoActivity.class);
            startActivity(verifyOtp);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
