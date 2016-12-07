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


public class MyResourcesTeachingAidsFragment extends Fragment {
    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.type_spinner)
    Spinner typeSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    private ContentVerticalCardListAdapter adapter;

    public MyResourcesTeachingAidsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MyResourcesTeachingAidsFragment newInstance() {
        MyResourcesTeachingAidsFragment fragment = new MyResourcesTeachingAidsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_resources_teaching_aids, container, false);
        ButterKnife.bind(this, rootView);

        //Temp
        List<String> subjects = Arrays.asList("Subject", "English", "Marathi", "Maths");
        List<String> grades = Arrays.asList("Grade", "1", "2", "3");
        //List<String> types = Arrays.asList("Type", Constants.FileType.VIDEO.toString(), Constants.FileType.PDF.toString());

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, subjects);
        subjectSpinner.setAdapter(subjectAdapter);
        subjectSpinner.setPrompt(subjects.get(0));


        List<Content> contents = new ArrayList<>();
//        contents.add(new Content("Video 1", Constants.FileType.VIDEO, Constants.Type.TEACHING_AIDS));
//        contents.add(new Content("PDF 1", Constants.FileType.PDF, Constants.Type.TEACHING_AIDS));
//        contents.add(new Content("PPT 1", Constants.FileType.PPT, Constants.Type.TEACHING_AIDS));
//        contents.add(new Content("Video 2", Constants.FileType.VIDEO, Constants.Type.TEACHING_AIDS));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContentVerticalCardListAdapter(getContext(), contents);
        contentRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, grades);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setPrompt(grades.get(0));

//        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_dropdown_item, types);
//        typeSpinner.setAdapter(typeAdapter);
//        typeSpinner.setPrompt(grades.get(0));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.releaseLoaders();
    }
}
