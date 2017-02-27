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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.database.DbGrade;
import net.mavericklabs.mitra.model.database.DbSubject;
import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void addGradeToFilter(CommonCode commonCode) {
        filterGradeList.add(commonCode);
        isChanged = true;
    }

    @Override
    public void addSubjectToFilter(CommonCode commonCode) {
        filterSubjectList.add(commonCode);
        isChanged = true;
    }

    @Override
    public void removeGradeFromFilter(CommonCode commonCode) {
        filterGradeList.remove(commonCode);
        isChanged = true;
    }

    @Override
    public void removeSubjectFromFilter(CommonCode commonCode) {
        filterSubjectList.remove(commonCode);
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
            return TeachingAidsContentFragment.newInstance(position);
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
