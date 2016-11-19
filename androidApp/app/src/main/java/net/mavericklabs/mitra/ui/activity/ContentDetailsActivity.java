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

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Requirements;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.RequirementsListAdapter;
import net.mavericklabs.mitra.utils.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.details)
    TextView details;

    @BindView(R.id.description)
    TextView description;


    @BindView(R.id.requirements_layout)
    LinearLayout requirementsLayout;

    BaseHorizontalCardListAdapter similarContentsAdapter;
    private Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        ViewGroup.LayoutParams imageLayoutParams = contentImageView.getLayoutParams();
        imageLayoutParams.height = displayMetrics.heightPixels / 3;
        contentImageView.setLayoutParams(imageLayoutParams);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            content = (Content) bundle.getSerializable("content");
        }

        if(content != null) {
            if(content.getType() == Constants.Type.TEACHING_AIDS) {

                requirementsLayout.setVisibility(View.VISIBLE);
                List<Requirements> requirementsList = new ArrayList<>();
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Wi-Fi"));
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Computer"));
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Projector"));

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                requirementsGridView.setLayoutManager(gridLayoutManager);
                requirementsGridView.setAdapter(new RequirementsListAdapter(getApplicationContext(), requirementsList));

                details.setText(" Subject | Grade");

            } else {
                requirementsLayout.setVisibility(View.GONE);
                details.setText(" Topic | Language");
            }

            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(content.getTitle());
            }
        }



        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PDF 1", Constants.FileType.PDF, Constants.Type.SELF_LEARNING));
        contents.add(new Content("PPT 1", Constants.FileType.PPT, Constants.Type.SELF_LEARNING));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO, Constants.Type.SELF_LEARNING));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        contentRecyclerView.setLayoutManager(linearLayoutManager);
        similarContentsAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
        contentRecyclerView.setAdapter(similarContentsAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}
