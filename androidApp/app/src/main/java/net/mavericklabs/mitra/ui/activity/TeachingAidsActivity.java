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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeachingAidsActivity extends AppCompatActivity {

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

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private boolean isFabExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_aids);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Teaching Aids");
        }

        setupFAB();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
            teachingAidsButton.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
        } else {
            teachingAidsButton.setTextColor(getResources().getColor(R.color.colorAccent));
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TeachingAidsContentFragment extends Fragment {

        @BindView(R.id.subject_spinner)
        Spinner subjectSpinner;

        @BindView(R.id.grade_spinner)
        Spinner gradeSpinner;

        @BindView(R.id.content_recycler_view)
        RecyclerView contentRecyclerView;

        private ContentVerticalCardListAdapter adapter;

        public TeachingAidsContentFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TeachingAidsContentFragment newInstance(int sectionNumber) {
            TeachingAidsContentFragment fragment = new TeachingAidsContentFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teaching_aids, container, false);
            ButterKnife.bind(this, rootView);

            //Temp
            List<String> subjects = Arrays.asList("Subject", "English", "Marathi", "Maths");
            List<String> grades = Arrays.asList("Grade", "1", "2", "3");

            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, subjects);
            subjectSpinner.setAdapter(subjectAdapter);
            subjectSpinner.setPrompt(subjects.get(0));


            List<Content> contents = new ArrayList<>();
            contents.add(new Content("Video 1", Constants.FileType.VIDEO, Constants.Type.TEACHING_AIDS));
            contents.add(new Content("PDF 1", Constants.FileType.PDF, Constants.Type.TEACHING_AIDS));
            contents.add(new Content("PPT 1", Constants.FileType.PPT, Constants.Type.TEACHING_AIDS));
            contents.add(new Content("Video 2", Constants.FileType.VIDEO, Constants.Type.TEACHING_AIDS));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            contentRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new ContentVerticalCardListAdapter(getContext(), contents);
            contentRecyclerView.setAdapter(adapter);

            ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, grades);
            gradeSpinner.setAdapter(gradeAdapter);
            gradeSpinner.setPrompt(grades.get(0));

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            adapter.releaseLoaders();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TeachingAidsContentFragment (defined as a static inner class below).
            return TeachingAidsContentFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Video";
                case 1:
                    return "Audio";
                case 2:
                    return "PPT";
                case 3:
                    return "Worksheet";
                case 4:
                    return "Genie";
            }
            return null;
        }
    }
}
