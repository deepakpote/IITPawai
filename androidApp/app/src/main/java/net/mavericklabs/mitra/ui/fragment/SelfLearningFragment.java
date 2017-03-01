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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.SelfLearningContentRequest;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.database.DbTopic;
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

public class SelfLearningFragment extends BaseContentFragment {

    @BindView(R.id.subject_spinner)
    Spinner topicSpinner;

    @BindView(R.id.grade_spinner)
    Spinner languageSpinner;

    private List<CommonCode> filterTopicList, filterLanguageList;


    public SelfLearningFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_self_learning,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        filterTopicList = new ArrayList<>();
        filterLanguageList = new ArrayList<>();
        setupFilterView(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.TOPICS)) {
                    filterTopicList.remove(commonCode);
                } else {
                    filterLanguageList.remove(commonCode);
                }
                removeFromFilterList(position);
                searchSelfLearning( 0);
            }
        });

        final List<CommonCode> topics = new ArrayList<>(CommonCodeUtils.getTopics());
        final List<CommonCode> languages = new ArrayList<>(CommonCodeUtils.getContentLanguages());

        //Header - not a valid value
        topics.add(0, new CommonCode(0, 0,getString(R.string.topic_only), getString(R.string.topic_only), 0));
        languages.add(0,new CommonCode(0,0,getString(R.string.language),getString(R.string.language),0));


        //From profile, set initial filters
        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);

            RealmList<DbTopic> dbTopics = user.getTopics();
            for(DbTopic topic : dbTopics) {
                addTopic(CommonCodeUtils.getObjectFromCode(topic.getTopicCommonCode()));
            }
        }


        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(),
                R.layout.custom_spinner_item_header,
                topics);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        topicSpinner.setAdapter(adapter);
        topicSpinner.setSelection(0 ,false);


        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(topics.get(i).getCodeID() != 0) {
                    addTopic(topics.get(i));
                    searchSelfLearning(0);
                }
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
                if(languages.get(i).getCodeID() != 0) {
                    addLanguage(languages.get(i));
                    searchSelfLearning(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchSelfLearning(0);

    }

    private void addTopic(CommonCode topic) {
        filterTopicList.add(topic);
        addItemToFilterList(topic);
        topicSpinner.setSelection(0 ,false);
    }

    private void addLanguage(CommonCode language) {
        filterLanguageList.add(language);
        addItemToFilterList(language);
        languageSpinner.setSelection(0 ,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }


    private void searchSelfLearning(final int pageNumber) {
        Logger.d(" searching ");

        String topicList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterTopicList);
        String languageList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterLanguageList);

        if(pageNumber == 0) {
            contentRecyclerView.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
        }
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(languageList, topicList);
        contentRequest.setPageNumber(pageNumber);
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchSelfLearning(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {

                    loadContent(response, pageNumber, new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            //On Scroll, show loading panel at bottom
                            adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                            adapter.showLoading();

                            if(isNextPageToBeLoaded(newState, recyclerView)) {
                                searchSelfLearning(1);
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                        }
                    });
                    return;
                }

                if(pageNumber == 0) {
                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessage(response)).getCodeNameForCurrentLocale();
                    Logger.d(" error " + error);
                    contentRecyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(error);
                } else {
                    adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                    adapter.stopLoading();
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
                if(pageNumber > 0) {
                    adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                    adapter.stopLoading();
                }
            }
        });
    }

}
