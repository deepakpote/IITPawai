package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    private boolean isAdditionalViewExpanded;

    @BindView(R.id.more_or_less_button)
    Button moreOrLessButtun;
    @BindView(R.id.more_layout)
    LinearLayout moreLayout;
    @BindView(R.id.subject_recycler_view)
    RecyclerView subjectRecyclerView;


    @OnClick(R.id.more_or_less_button)
    void expandOrCollapse() {
        if(isAdditionalViewExpanded) {
            moreOrLessButtun.setText(R.string.more);
            moreLayout.setVisibility(View.GONE);
            isAdditionalViewExpanded = false;
        } else {
            moreOrLessButtun.setText(R.string.less);
            moreLayout.setVisibility(View.VISIBLE);
            isAdditionalViewExpanded = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_user_info);
        ButterKnife.bind(this);

        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_next,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {
            Intent verifyOtp = new Intent(ProfileActivity.this,HomeActivity.class);
            startActivity(verifyOtp);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
