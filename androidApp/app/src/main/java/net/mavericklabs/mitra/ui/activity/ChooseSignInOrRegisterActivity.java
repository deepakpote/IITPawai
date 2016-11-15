package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseSignInOrRegisterActivity extends AppCompatActivity {

    @BindView(R.id.activity_choose_sign_in_or_register)
    RelativeLayout relativeLayout;

    @BindView(R.id.select_regsiter_button)
    Button registerButton;

    @BindView(R.id.select_sign_in_button)
    Button signInButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.select_sign_in_button)
    void signIn() {
        Intent signIn = new Intent(ChooseSignInOrRegisterActivity.this,SignInUserActivity.class);
        startActivity(signIn);
    }

    @OnClick(R.id.select_regsiter_button)
    void register() {
        Intent register = new Intent(ChooseSignInOrRegisterActivity.this,RegisterUserActivity.class);
        startActivity(register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_in_or_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        String defaultLanguage = getDefaultLanguage();
        Snackbar.make(relativeLayout, "ENGLISH is your default language. To change visit settings > language",
                Snackbar.LENGTH_LONG).show();
    }

    private String getDefaultLanguage() {
        return "MARATHI";
    }
}
