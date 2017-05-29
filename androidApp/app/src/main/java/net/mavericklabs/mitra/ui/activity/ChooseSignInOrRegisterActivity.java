package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

public class ChooseSignInOrRegisterActivity extends BaseActivity {

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



    }

    @OnClick(R.id.select_regsiter_button)
    void register() {
        Intent register = new Intent(ChooseSignInOrRegisterActivity.this,SignInWithGoogleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_from_sign_in", false);
        register.putExtras(bundle);
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
        Snackbar snackbar = Snackbar.make(relativeLayout, getString(R.string.default_language),
                Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
