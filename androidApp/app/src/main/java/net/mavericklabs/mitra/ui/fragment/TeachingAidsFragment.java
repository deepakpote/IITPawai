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

package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 14/11/16.
 */

public class TeachingAidsFragment extends Fragment{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentStatePagerAdapter}.
     */
    private PagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;


    public TeachingAidsFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teaching_aids,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Logger.d("My resources on view created ");

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_my_resources);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.d("My resources on resume ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(teachingAidsAdapter != null) {
//            teachingAidsAdapter.releaseLoaders();
//        }
//
//        if(selfLearningAdapter != null) {
//            selfLearningAdapter.releaseLoaders();
//        }
    }

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
            View rootView = inflater.inflate(R.layout.fragment_teaching_aids_list, container, false);
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


    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TeachingAidsContentFragment (defined as a static inner class below).
            return TeachingAidsFragment.TeachingAidsContentFragment.newInstance(position + 1);
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
