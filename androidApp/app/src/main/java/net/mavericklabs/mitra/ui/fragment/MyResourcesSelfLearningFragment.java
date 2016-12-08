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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class MyResourcesSelfLearningFragment extends Fragment {
    @BindView(R.id.topic_spinner)
    Spinner topicSpinner;

    @BindView(R.id.language_spinner)
    Spinner languageSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    private ContentVerticalCardListAdapter adapter;

    public MyResourcesSelfLearningFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MyResourcesSelfLearningFragment newInstance() {
        MyResourcesSelfLearningFragment fragment = new MyResourcesSelfLearningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_resources_self_learning, container, false);
        ButterKnife.bind(this, rootView);

        final List<CommonCode> topics = new ArrayList<>(CommonCodeUtils.getTopics());
        final List<CommonCode> languages = new ArrayList<>(CommonCodeUtils.getLanguages());

        //Header - not a valid value
        topics.add(0, new CommonCode("", "","Topic", "Topic", 0));
        languages.add(0,new CommonCode("","","Language","Language",0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(),
                R.layout.custom_spinner_item_header,
                topics);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        topicSpinner.setAdapter(adapter);
        topicSpinner.setSelection(0 ,false);


        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode language = (CommonCode) languageSpinner.getSelectedItem();
                loadMySelfLearning(language.getCodeID(), topics.get(i).getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerArrayAdapter languageAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                languages);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(0 ,false);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode topic = (CommonCode) topicSpinner.getSelectedItem();
                loadMySelfLearning(languages.get(i).getCodeID() , topic.getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String language = "";

        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);
            language = user.getPreferredLanguage();
        }

        loadMySelfLearning(language, "");

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.releaseLoaders();
    }

    private void loadMySelfLearning(String language, String topic) {
        List<Content> contents = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContentVerticalCardListAdapter(getContext(), contents);
        contentRecyclerView.setAdapter(adapter);

    }
}
