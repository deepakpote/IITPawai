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

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.NewsListAdapter;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @OnClick(R.id.teaching_aids_solid_button)
    void goToTeachingAids() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu().getItem(1));
        }
    }

    @BindView(R.id.self_learning_solid_button)
    Button selfLearningSolidButton;

    @OnClick(R.id.self_learning_solid_button)
    void goToSelfLearning() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu().getItem(2));
        }
    }

    @BindView(R.id.trainings_solid_button)
    Button trainingsSolidButton;

    private BaseHorizontalCardListAdapter teachingAidsAdapter, selfLearningAdapter;

    public HomeFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        loadPopularTeachingAids();
        loadPopularSelfLearning();

        List<News> news = new ArrayList<>();
        news.add(new News("Title 1", "Details 1"));
        news.add(new News("Title 2", "Details 2"));
        news.add(new News("Title 3", "Details 3"));
        news.add(new News("Title 4", "Details 4"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(layoutManager);
        NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), news);
        newsRecyclerView.setAdapter(newsListAdapter);

    }

    private void loadPopularTeachingAids() {
        //TODO popular resources

        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(UserDetailUtils.getUserId(getContext()),
                "108100", "101100", "", "");
        RestClient.getApiService("").searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
                        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

    private void loadPopularSelfLearning() {
        //TODO popular resources
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getContext()),
                "101100", "");
        RestClient.getApiService("").searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularSelfLearningRecyclerView.setLayoutManager(manager);
                        selfLearningAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
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
