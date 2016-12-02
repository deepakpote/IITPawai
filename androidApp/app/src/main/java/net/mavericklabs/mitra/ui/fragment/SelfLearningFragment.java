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
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelfLearningFragment extends Fragment {

    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    private ContentVerticalCardListAdapter adapter;


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

        //Temp
        List<String> topics = Arrays.asList("Topic", "English", "Marathi", "Maths");
        List<String> languages = Arrays.asList("Language", "1", "2", "3");

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, topics);
        subjectSpinner.setAdapter(subjectAdapter);
        subjectSpinner.setPrompt(topics.get(0));


        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PDF 1", Constants.FileType.PDF, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PPT 1", Constants.FileType.PPT, Constants.Type.SELF_LEARNING));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContentVerticalCardListAdapter(getContext(), contents);
        contentRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, languages);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setPrompt(languages.get(0));

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.releaseLoaders();
    }

}
