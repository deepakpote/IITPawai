package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.ui.adapter.ProfileActivityGradesAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    private boolean isAdditionalViewExpanded;

    @BindView(R.id.more_or_less_button) Button moreOrLessButtun;
    @BindView(R.id.more_layout) LinearLayout moreLayout;
    @BindView(R.id.subject_recycler_view) RecyclerView subjectRecyclerView;
    @BindView(R.id.grade_recycler_view) RecyclerView gradeRecyclerView;
    @BindView(R.id.more_image) ImageView moreImage;
    @BindView(R.id.less_image) ImageView lessImage;
    @BindView(R.id.grade_placeholder_text) TextView gradePlaceholderTextView;
    @BindView(R.id.subject_placeholder_text) TextView subjectPlaceholderTextView;

    @OnClick(R.id.add_grades)
    void showAddGradesMenu() {
        Logger.d("adding grades..");
    }

    @OnClick(R.id.add_subjects)
    void showAddSubjectsMenu() {
        Logger.d("adding subjects..");
    }

    @OnClick(R.id.more_or_less_button)
    void expandOrCollapse() {
        if(isAdditionalViewExpanded) {
            moreOrLessButtun.setText(R.string.more);
            moreLayout.setVisibility(View.GONE);
            isAdditionalViewExpanded = false;
            moreImage.setVisibility(View.VISIBLE);
            lessImage.setVisibility(View.GONE);
        } else {
            moreOrLessButtun.setText(R.string.less);
            moreLayout.setVisibility(View.VISIBLE);
            isAdditionalViewExpanded = true;
            moreImage.setVisibility(View.GONE);
            lessImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        subjectRecyclerView.setVisibility(View.GONE);
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        subjectRecyclerView.setAdapter(new ProfileActivitySubjectsAdapter());

        gradeRecyclerView.setVisibility(View.GONE);
        gradeRecyclerView.setHasFixedSize(true);
        gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        gradeRecyclerView.setAdapter(new ProfileActivityGradesAdapter());
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
