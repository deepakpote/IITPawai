package net.mavericklabs.mitra.ui.fragment;

import android.content.Intent;
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
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.activity.TeachingAidsActivity;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.NewsListAdapter;
import net.mavericklabs.mitra.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 14/11/16.
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
        Intent intent = new Intent(getContext(), TeachingAidsActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.self_learning_solid_button)
    Button selfLearningSolidButton;

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

        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO));
        contents.add(new Content("PDF 1", Constants.FileType.PDF));
        contents.add(new Content("PPT 1", Constants.FileType.PPT));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        popularSelfLearningRecyclerView.setLayoutManager(manager);
        selfLearningAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);

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

    @Override
    public void onDetach() {
        super.onDetach();
        if(teachingAidsAdapter != null) {
            teachingAidsAdapter.releaseLoaders();
        }

        if(selfLearningAdapter != null) {
            selfLearningAdapter.releaseLoaders();
        }
    }
}
