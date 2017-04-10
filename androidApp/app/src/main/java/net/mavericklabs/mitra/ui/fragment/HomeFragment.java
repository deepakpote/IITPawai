package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.api.SavedSelfLearningRequest;
import net.mavericklabs.mitra.model.api.SavedTeachingAidsRequest;
import net.mavericklabs.mitra.model.api.SelfLearningContentRequest;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.NewsListAdapter;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.DownloadUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class HomeFragment extends Fragment{

    @BindView(R.id.popularTeachingAidsRecyclerView)
    RecyclerView popularVideosRecyclerView;

    @BindView(R.id.popularSelfLearningRecyclerView)
    RecyclerView popularSelfLearningRecyclerView;

    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;

    @BindView(R.id.teaching_aids_solid_button)
    Button teachingAidsSolidButton;

    @OnClick(R.id.see_all_news)
    void seeAllNews() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_NEWS));
        }
    }

    @OnClick(R.id.teaching_aids_solid_button)
    void goToTeachingAids() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_TEACHING_AIDS));
        }
    }

    @BindView(R.id.self_learning_solid_button)
    Button selfLearningSolidButton;

    @OnClick(R.id.self_learning_solid_button)
    void goToSelfLearning() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_SELF_LEARNING));
        }
    }

    @BindView(R.id.trainings_solid_button)
    Button trainingsSolidButton;

    @OnClick(R.id.trainings_solid_button)
    void goToTrainingCalendar() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_TRAINING_CALENDAR));
        }
    }

    @BindView(R.id.teaching_aids_loading_panel)
    RelativeLayout teachingAidsLoadingPanel;

    @BindView(R.id.self_learning_loading_panel)
    RelativeLayout selfLearningLoadingPanel;


    private BaseHorizontalCardListAdapter teachingAidsAdapter, selfLearningAdapter;

    public HomeFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        syncBookmarksToServer();
        loadPopularTeachingAids();
        loadPopularSelfLearning();
        loadNews();
    }

    private void loadNews() {
        RestClient.getApiService("").listNews(LanguageUtils.getCurrentLanguage()).enqueue(new Callback<BaseModel<News>>() {
            @Override
            public void onResponse(Call<BaseModel<News>> call, Response<BaseModel<News>> response) {
                if(response.isSuccessful()) {
                    Logger.d(" News success ");
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    List<News> news = response.body().getData();
                    List<News> pdfNewsList = new ArrayList<News>();

                    for (News newsItem : news) {
                        News newsInDb = realm.where(News.class).equalTo("newsID", newsItem.getNewsID()).findFirst();
                        if(newsInDb != null) {
                            newsItem.setSeen(newsInDb.isSeen());
                            newsItem.setSaved(newsInDb.isSaved());
                            newsItem.setShowOnMainPage(newsInDb.isShowOnMainPage());
                        } else {
                            newsItem.setSeen(false);
                            newsItem.setSaved(false);
                            newsItem.setShowOnMainPage(true);
                            if(newsItem.getPdfFileURL() != null) {
                                pdfNewsList.add(newsItem);
                            }
                        }
                        newsItem.setDateToCompare(DateUtils.convertToDate(newsItem.getPublishDate(), "yyyy-MM-dd HH:mm:ss"));
                        realm.copyToRealmOrUpdate(newsItem);
                    }

                    DownloadUtils.downloadNewsPDF(getContext(), pdfNewsList);

                    realm.commitTransaction();

                    loadNewsFromDb();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<News>> call, Throwable t) {

            }
        });
    }

    private void loadPopularTeachingAids() {
        //TODO popular resources

        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(Constants.FileTypeVideo, "", "");
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchTeachingAids(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                teachingAidsLoadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
                        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();

                        for (Content content : contents) {
                            Content contentInDb = realm.where(Content.class).equalTo("contentID",
                                    content.getContentID()).findFirst();
                            if(contentInDb != null) {
                                content.setSaved(contentInDb.getSaved());
                            } else {
                                content.setSaved(false);
                            }

                            realm.copyToRealmOrUpdate(content);
                        }
                        realm.commitTransaction();

                    }
                } else {
                    loadTeachingAidsFromDb();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
                teachingAidsLoadingPanel.setVisibility(View.GONE);
                loadTeachingAidsFromDb();
            }
        });
    }

    private void loadTeachingAidsFromDb() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Content> contents = realm.where(Content.class).equalTo("contentTypeCodeID",
                Constants.ContentTypeTeachingAids).findAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);
    }

    private void loadPopularSelfLearning() {
        //TODO popular resources
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest("", "");
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchSelfLearning(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                selfLearningLoadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularSelfLearningRecyclerView.setLayoutManager(manager);
                        selfLearningAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();

                        for (Content content : contents) {
                            Content contentInDb = realm.where(Content.class).equalTo("contentID",
                                    content.getContentID()).findFirst();
                            if(contentInDb != null) {
                                content.setSaved(contentInDb.getSaved());
                            } else {
                                content.setSaved(false);
                            }

                            realm.copyToRealmOrUpdate(content);
                        }
                        realm.commitTransaction();
                    }
                } else {
                    loadSelfLearningFromDb();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
                selfLearningLoadingPanel.setVisibility(View.GONE);
                loadSelfLearningFromDb();
            }
        });
    }

    private void loadSelfLearningFromDb() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Content> contents = realm.where(Content.class).equalTo("contentTypeCodeID",
                Constants.ContentTypeSelfLearning).findAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularSelfLearningRecyclerView.setLayoutManager(linearLayoutManager);
        selfLearningAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);
    }

    private void loadNewsFromDb() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<News> dbNews = realm.where(News.class).equalTo("showOnMainPage",
                Boolean.TRUE).findAllSorted("dateToCompare", Sort.DESCENDING);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(layoutManager);
        List<News> newsListToShow = realm.copyFromRealm(dbNews);
        if(newsListToShow.size() > 5) {
            newsListToShow = newsListToShow.subList(0,5);
        }
        NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), newsListToShow);
        newsRecyclerView.setAdapter(newsListAdapter);
    }

    private void syncBookmarksToServer() {

        //Get all bookmarked content
        final List<String> bookmarkedContentFromServer = new ArrayList<>();

        SavedTeachingAidsRequest contentRequest = new SavedTeachingAidsRequest(
                Constants.ContentTypeTeachingAids, "", "", "");
        final String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).getSavedTeachingAids(LanguageUtils.getCurrentLanguage(),
                contentRequest).enqueue(new Callback<BaseModel<Content>>() {

            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d("on response - ta ");
                for (Content content : response.body().getData()) {
                    bookmarkedContentFromServer.add(content.getContentID());
                }

                SavedSelfLearningRequest selfLearningRequest = new SavedSelfLearningRequest(Constants.ContentTypeSelfLearning,
                        "", "");

                RestClient.getApiService(token).getSavedSelfLearning(LanguageUtils.getCurrentLanguage(),
                        selfLearningRequest).enqueue(new Callback<BaseModel<Content>>() {

                    @Override
                    public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                        Logger.d("on response - sl ");
                        for (Content content : response.body().getData()) {
                            bookmarkedContentFromServer.add(content.getContentID());
                        }
                        Logger.d(" bookmarked from server " + bookmarkedContentFromServer.toString() + " " + bookmarkedContentFromServer.size());

                        //Check content to sync
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<Content> localBookmarkedContents = realm.where(Content.class).equalTo("isSaved", Boolean.TRUE).findAll();
                        for (Content content : localBookmarkedContents) {
                            if(!bookmarkedContentFromServer.contains(content.getContentID())) {
                                bookmarkContent(content);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<Content>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {

            }
        });

    }

    private void bookmarkContent(Content content) {
        String token = UserDetailUtils.getToken(getContext());
        Logger.d(" Saving content " + content.getTitle());
        RestClient.getApiService(token).saveContent(content.getContentID(), true)
                .enqueue(new Callback<BaseModel<GenericListDataModel>>() {

                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            Logger.d("content saved..");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Logger.d("failed to save " );
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewsFromDb();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (teachingAidsAdapter != null) {
            teachingAidsAdapter.releaseLoaders();
        }
        if (selfLearningAdapter != null) {
            selfLearningAdapter.releaseLoaders();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(teachingAidsAdapter != null) {
            teachingAidsAdapter.releaseLoaders();
        }

        if(selfLearningAdapter != null) {
            selfLearningAdapter.releaseLoaders();
        }
    }
}
