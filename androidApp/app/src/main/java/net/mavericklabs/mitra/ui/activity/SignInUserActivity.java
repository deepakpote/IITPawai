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
import butterknife.OnClick;

public class SignInUserActivity extends AppCompatActivity {

    @BindView(R.id.register_button)
    Button signIn;

    @BindView(R.id.phone_number_edit_text)
    EditText phoneNumberEditText;

    @OnClick(R.id.register_button)
    void register () {
        Intent register = new Intent(SignInUserActivity.this,RegisterUserActivity.class);
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
            Intent verifyOtp = new Intent(SignInUserActivity.this,VerifyOtpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phone_number",phoneNumberEditText.getText().toString());
            verifyOtp.putExtras(bundle);
            startActivity(verifyOtp);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
