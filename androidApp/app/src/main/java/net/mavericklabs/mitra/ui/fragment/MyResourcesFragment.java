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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class MyResourcesFragment extends Fragment{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public TextView subtitle0, subtitle1;
    private TextView title0, title1;


    public MyResourcesFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("My resources on createview");
        return inflater.inflate(R.layout.fragment_my_resources,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Logger.d("My resources on view created ");

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_my_resources);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        setupCustomTabs();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {

                    //Subtitle is visible only for selected items
                    subtitle0.setVisibility(View.VISIBLE);
                    subtitle1.setVisibility(View.GONE);

                    //Toggle title text color on selection
                    title0.setTextColor(getResources().getColor(R.color.colorAccent));
                    title1.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    subtitle0.setVisibility(View.GONE);
                    subtitle1.setVisibility(View.VISIBLE);

                    title0.setTextColor(getResources().getColor(R.color.colorWhite));
                    title1.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupCustomTabs() {
        TabLayout.Tab tab0 = tabLayout.getTabAt(0);
        if(tab0 != null) {
            tab0.setCustomView(R.layout.custom_tab);

            if(tab0.getCustomView() != null) {
                title0 = (TextView) tab0.getCustomView().findViewById(R.id.title);
                title0.setText(getResources().getString(R.string.teaching_aids));

                subtitle0 = (TextView) tab0.getCustomView().findViewById(R.id.subtitle);

                //Tab0 is initially selected
                title0.setTextColor(getResources().getColor(R.color.colorAccent));
                subtitle0.setVisibility(View.VISIBLE);
            }
        }

        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        if(tab1 != null) {
            tab1.setCustomView(R.layout.custom_tab);

            if(tab1.getCustomView() != null) {
                title1 = (TextView) tab1.getCustomView().findViewById(R.id.title);
                title1.setText(getResources().getString(R.string.self_learning));

                subtitle1 = (TextView) tab1.getCustomView().findViewById(R.id.subtitle);

                //Tab1 is initially not selected
                subtitle1.setVisibility(View.GONE);
                title1.setTextColor(getResources().getColor(R.color.colorWhite));
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("My resources on resume ");
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        if(teachingAidsAdapter != null) {
////            teachingAidsAdapter.releaseLoaders();
////        }
////
////        if(selfLearningAdapter != null) {
////            selfLearningAdapter.releaseLoaders();
////        }
//    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return MyResourcesTeachingAidsFragment.newInstance();
            }
            return MyResourcesSelfLearningFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
