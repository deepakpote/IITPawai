/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.utils.AnimationUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelfLearningActivity extends AppCompatActivity {

    @BindView(R.id.faded_background_view)
    View fadedBackgroundView;

    @BindView(R.id.button_layout)
    LinearLayout buttonLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.teaching_aids_button)
    Button teachingAidsButton;

    @BindView(R.id.self_learning_button)
    Button selfLearningButton;

    @BindView(R.id.trainings_button)
    Button trainingsButton;

    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    private ContentVerticalCardListAdapter adapter;

    private boolean isFabExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_learning);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Self Learning");
        }

        setupFAB();

        //Temp
        List<String> topics = Arrays.asList("Topic", "English", "Marathi", "Maths");
        List<String> languages = Arrays.asList("Language", "1", "2", "3");

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, topics);
        subjectSpinner.setAdapter(subjectAdapter);
        subjectSpinner.setPrompt(topics.get(0));


        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PDF 1", Constants.FileType.PDF, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PPT 1", Constants.FileType.PPT, Constants.Type.SELF_LEARNING));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContentVerticalCardListAdapter(getApplicationContext(), contents);
        contentRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, languages);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setPrompt(languages.get(0));

    }

    private void setupFAB() {

//            teachingAidsButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Go to teaching aids
//                    Intent intent = new Intent(HomeActivity.this, TeachingAidsActivity.class);
//                    startActivity(intent);
//                }
//            });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            selfLearningButton.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
        } else {
            selfLearningButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        //Here the bottom nav bar is missing, so adjust margins accordingly

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
        layoutParams.setMargins(0,0,0, DisplayUtils.dpToPx(16, getApplicationContext()));
        fab.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) buttonLayout.getLayoutParams();
        buttonLayoutParams.setMargins(0,0,0, DisplayUtils.dpToPx(72, getApplicationContext()));
        buttonLayout.setLayoutParams(buttonLayoutParams);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isFabExpanded) {
                        AnimationUtils.fadeOutView(fadedBackgroundView);
                        fab.setImageResource(R.drawable.ic_explore_white_24dp);
                        isFabExpanded = false;
                    } else {
                        AnimationUtils.fadeInView(fadedBackgroundView, new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                fab.setImageResource(R.drawable.ic_close_white_24dp);
                            }
                        });
                        isFabExpanded = true;
                    }


                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teaching_aids, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.releaseLoaders();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
