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
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.ContentRequest;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.activity.EditProfileActivity;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if(CommonCodeUtils.getFileTypeCount() > 4) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

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


        public TeachingAidsContentFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TeachingAidsContentFragment newInstance(int tabNumber) {
            TeachingAidsContentFragment fragment = new TeachingAidsContentFragment();
            Bundle args = new Bundle();
            args.putInt("tabNumber", tabNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teaching_aids_list, container, false);
            ButterKnife.bind(this, rootView);

            int tabNumber = getArguments().getInt("tabNumber");

            List<CommonCode> subjects = new ArrayList<>(CommonCodeUtils.getSubjects());
            List<CommonCode> grades = new ArrayList<>(CommonCodeUtils.getGrades());

            //Header - not a valid value
            subjects.add(0, new CommonCode("0", "0","Subject", "Subject", 0));
            grades.add(0,new CommonCode("0","0","Grade","Grade",0));


            SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                    subjects);
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            subjectSpinner.setAdapter(adapter);

            SpinnerArrayAdapter gradeAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                    grades);
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            gradeSpinner.setAdapter(gradeAdapter);

            String fileType = CommonCodeUtils.getFileTypeAtPosition(tabNumber).getCodeID();

            ContentRequest contentRequest = new ContentRequest(UserDetailUtils.getUserId(getContext()),
                    fileType, "101100", "", "");
            RestClient.getApiService("").searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
                @Override
                public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                    Logger.d(" Succes");
                    if(response.isSuccessful()) {
                        if(response.body().getData() != null) {
                            List<Content> contents = response.body().getData();
                            Logger.d(" contents " + contents.size());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            contentRecyclerView.setLayoutManager(linearLayoutManager);
                            ContentVerticalCardListAdapter adapter = new ContentVerticalCardListAdapter(getContext(), contents);
                            contentRecyclerView.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                    Logger.d(" on fail");
                }
            });

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
//            if(adapter != null) {
//                adapter.releaseLoaders();
//            }
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
            return TeachingAidsFragment.TeachingAidsContentFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return CommonCodeUtils.getFileTypeCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CommonCodeUtils.getFileTypeAtPosition(position).getCodeNameForCurrentLocale();
        }

    }


}
