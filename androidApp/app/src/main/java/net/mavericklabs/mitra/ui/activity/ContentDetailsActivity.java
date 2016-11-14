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

package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Requirements;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.RequirementsListAdapter;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentDetailsActivity extends AppCompatActivity {

    @BindView(R.id.similar_contents_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.content_card_view)
    CardView contentCardView;

    @BindView(R.id.content_image_view)
    ImageView contentImageView;

    @BindView(R.id.requirements_grid_view)
    RecyclerView requirementsGridView;

    BaseHorizontalCardListAdapter similarContentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        ViewGroup.LayoutParams imageLayoutParams = contentImageView.getLayoutParams();
        imageLayoutParams.height = displayMetrics.heightPixels / 3;
        contentImageView.setLayoutParams(imageLayoutParams);

        List<Requirements> requirementsList = new ArrayList<>();
        requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Wi-Fi"));
        requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Computer"));
        requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Projector"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        requirementsGridView.setLayoutManager(gridLayoutManager);
        requirementsGridView.setAdapter(new RequirementsListAdapter(getApplicationContext(), requirementsList));


        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO));
        contents.add(new Content("PDF 1", Constants.FileType.PDF));
        contents.add(new Content("PPT 1", Constants.FileType.PPT));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        similarContentsAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
        contentRecyclerView.setAdapter(similarContentsAdapter);


    }
}
