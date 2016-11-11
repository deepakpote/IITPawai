package net.mavericklabs.mitra.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.popularTeachingAidsRecyclerView)
    RecyclerView popularVideosRecyclerView;

    @BindView(R.id.popularSelfLearningRecyclerView)
    RecyclerView popularSelfLearningRecyclerView;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.faded_background_view)
    View fadedBackgroundView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean isFabExpanded = false;

    BaseHorizontalCardListAdapter teachingAidsAdapter, selfLearningAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFabExpanded) {
                    fadeInView(bottomNavigationView, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fab.setImageResource(R.drawable.ic_explore_white_24dp);
                        }
                    });
                    fadeOutView(fadedBackgroundView);

                    isFabExpanded = false;
                } else {
                    fadeInView(fadedBackgroundView, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fab.setImageResource(R.drawable.ic_close_white_24dp);
                        }
                    });
                    fadeOutView(bottomNavigationView);
                    isFabExpanded = true;
                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<Content> contents = new ArrayList<>();
        contents.add(new Content("Video 1", Constants.FileType.VIDEO));
        contents.add(new Content("PDF 1", Constants.FileType.PDF));
        contents.add(new Content("PPT 1", Constants.FileType.PPT));
        contents.add(new Content("Video 2", Constants.FileType.VIDEO));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        popularSelfLearningRecyclerView.setLayoutManager(layoutManager);
        selfLearningAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);


        bottomNavigationView.setLayoutAnimation(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                return false;
            }
        });

    }

    private void fadeInView(View viewToShow, AnimatorListenerAdapter listenerAdapter) {
        // Set the viewToShow to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.

        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the viewToShow to 100% opacity
        viewToShow.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(listenerAdapter);

    }

    private void fadeOutView(final View viewToHide) {

        // Animate the viewToHide to 0% opacity.
        // After the animation ends, set its visibility to GONE

        viewToHide.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewToHide.setVisibility(View.GONE);
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        teachingAidsAdapter.releaseLoaders();
        selfLearningAdapter.releaseLoaders();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
