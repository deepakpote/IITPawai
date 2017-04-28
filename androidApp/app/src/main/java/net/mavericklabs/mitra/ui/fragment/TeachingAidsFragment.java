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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.database.DbGrade;
import net.mavericklabs.mitra.model.database.DbSubject;
import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class TeachingAidsFragment extends Fragment implements TeachingAidsContentFragment.FilterChangeListener{

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
    private List<CommonCode> filterGradeList, filterSubjectList;
    private HashSet<Integer> filters;
    private boolean isChanged;


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

        filterGradeList = new ArrayList<>();
        filterSubjectList = new ArrayList<>();
        filters = new HashSet<>();

        //From profile, set initial filters
        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();

        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);
            RealmList<DbSubject> dbSubjects = user.getSubjects();
            for(DbSubject subject : dbSubjects) {
                addSubjectToFilter(CommonCodeUtils.getObjectFromCode(subject.getSubjectCommonCode()));
            }

            RealmList<DbGrade> dbGrades = user.getGrades();
            for(DbGrade grade : dbGrades) {
                addGradeToFilter(CommonCodeUtils.getObjectFromCode(grade.getGradeCommonCode()));
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.d("My resources on resume ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    @Override
    public boolean addGradeToFilter(CommonCode commonCode) {
        Logger.d(" filters " + filters);

        if(!filters.contains(commonCode.getCodeID())) {
            filterGradeList.add(commonCode);
            isChanged = true;
            filters.add(commonCode.getCodeID());
            return true;
        }
        return false;
    }

    @Override
    public boolean addSubjectToFilter(CommonCode commonCode) {
        if(!filters.contains(commonCode.getCodeID())) {
            filterSubjectList.add(commonCode);
            isChanged = true;
            filters.add(commonCode.getCodeID());
            return true;
        }

        return false;
    }

    @Override
    public void removeGradeFromFilter(CommonCode commonCode) {
        filterGradeList.remove(commonCode);
        filters.remove(commonCode.getCodeID());
        isChanged = true;
    }

    @Override
    public void removeSubjectFromFilter(CommonCode commonCode) {
        filterSubjectList.remove(commonCode);
        filters.remove(commonCode.getCodeID());
        isChanged = true;
    }

    @Override
    public void setChanged(Boolean changed) {
        isChanged = changed;
    }

    @Override
    public Boolean isChanged() {
        return isChanged;
    }

    @Override
    public List<CommonCode> getSubjects() {
        return filterSubjectList;
    }

    @Override
    public List<CommonCode> getGrades() {
        return filterGradeList;
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TeachingAidsContentFragment (defined as a static inner class below).
            return TeachingAidsContentFragment.newInstance(getFileTypeForPosition(position));
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return CommonCodeUtils.getFileTypeCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CommonCodeUtils.getObjectFromCode(getFileTypeForPosition(position)).getCodeNameForCurrentLocale();
        }

        private int getFileTypeForPosition(int position) {
            if(position == 0) {
                return Constants.FileTypeVideo;
            }
            if(position == 1) {
                return Constants.FileTypeEkStep;
            }

            return CommonCodeUtils.getFileTypeAtPosition(position - 1).getCodeID();
        }

    }


}
