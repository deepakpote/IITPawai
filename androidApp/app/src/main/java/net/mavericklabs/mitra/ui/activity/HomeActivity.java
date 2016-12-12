package net.mavericklabs.mitra.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.ui.fragment.EventCalendarFragment;
import net.mavericklabs.mitra.ui.fragment.HomeFragment;
import net.mavericklabs.mitra.ui.fragment.MyResourcesFragment;
import net.mavericklabs.mitra.ui.fragment.NotificationFragment;
import net.mavericklabs.mitra.ui.fragment.ProfileFragment;
import net.mavericklabs.mitra.ui.fragment.SelfLearningFragment;
import net.mavericklabs.mitra.ui.fragment.SettingsFragment;
import net.mavericklabs.mitra.ui.fragment.TeachingAidsFragment;
import net.mavericklabs.mitra.utils.AnimationUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.faded_background_view)
    View fadedBackgroundView;

    @OnClick(R.id.faded_background_view)
    void dismissView() {
        collapseFab();
    }

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.teaching_aids_button)
    Button teachingAidsButton;

    @BindView(R.id.self_learning_button)
    Button selfLearningButton;

    @BindView(R.id.trainings_button)
    Button trainingsButton;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;


    private TabLayout tabLayout;
    private boolean isFabExpanded = false;

    public int DRAWER_ITEM_HOME = 0;
    public int DRAWER_ITEM_TEACHING_AIDS = 1;
    public int DRAWER_ITEM_SELF_LEARNING = 2;
    public int DRAWER_ITEM_TRAINING_CALENDAR = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs_my_resources);

        RealmResults<DbUser> user = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFAB();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        selectDrawerItem(navigationView.getMenu().getItem(DRAWER_ITEM_HOME));
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        ImageView profilePhoto = (ImageView) headerView.findViewById(R.id.nav_header_image);
        if(user.size() ==1) {
            userNameTextView.setText(user.get(0).getName());
            if(!StringUtils.isEmpty(user.get(0).getProfilePhotoPath())) {
                Glide.with(this).load(user.get(0).getProfilePhotoPath())
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(profilePhoto);
            } else {
                Glide.with(this).load(R.drawable.placeholder_user).
                        bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                        into(profilePhoto);
            }
        }
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction;
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                tabLayout.setVisibility(View.GONE);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                setTitle("My Profile");
                // Close the navigation drawer
                drawerLayout.closeDrawers();
            }
        });
    }

    public void selectDrawerItem(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;

        Fragment fragment = null;
        Class fragmentClass;

        switch (item.getItemId()) {

            case R.id.nav_home :
                AnimationUtils.fadeOutView(tabLayout);
                tabLayout.setVisibility(View.GONE);
                fragmentClass = HomeFragment.class;
                break;

            case R.id.nav_teaching_aids :
                AnimationUtils.fadeInView(tabLayout, null);
                //tabLayout.setVisibility(View.VISIBLE);
                fragmentClass = TeachingAidsFragment.class;
                break;

            case R.id.nav_self_learning_videos :
                tabLayout.setVisibility(View.GONE);
                fragmentClass = SelfLearningFragment.class;
                break;

            case R.id.nav_my_resources :
                tabLayout.setVisibility(View.VISIBLE);
                fragmentClass = MyResourcesFragment.class;
                break;

            case R.id.nav_training_calendar:
                tabLayout.setVisibility(View.GONE);
                fragmentClass = EventCalendarFragment.class;
                break;

            //commenting for now. to be implemented in next release.
//            case R.id.nav_notification:
//                tabLayout.setVisibility(View.GONE);
//                fragmentClass = NotificationFragment.class;
//                break;
            case R.id.nav_settings:
                tabLayout.setVisibility(View.GONE);
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //AnimationUtils.fadeInView(bottomNavigationView, null);
        fadedBackgroundView.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.ic_explore_white_24dp);
        isFabExpanded = false;
    }

    private void setupFAB() {
        teachingAidsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to teaching aids
                selectDrawerItem(navigationView.getMenu().getItem(DRAWER_ITEM_TEACHING_AIDS));
                collapseFab();
            }
        });

        selfLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to self learning
                selectDrawerItem(navigationView.getMenu().getItem(DRAWER_ITEM_SELF_LEARNING));
                collapseFab();
            }
        });

        trainingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to self learning
                selectDrawerItem(navigationView.getMenu().getItem(DRAWER_ITEM_TRAINING_CALENDAR));
                collapseFab();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFabExpanded) {
                    collapseFab();
                } else {
                    expandFab();
                }

            }
        });
    }

    private void expandFab() {
        AnimationUtils.fadeInView(fadedBackgroundView, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.ic_close_white_24dp);
            }
        });
        //AnimationUtils.fadeOutView(bottomNavigationView);
        isFabExpanded = true;
    }

    private void collapseFab() {
        fab.setImageResource(R.drawable.ic_explore_white_24dp);
        AnimationUtils.fadeOutView(fadedBackgroundView);

        isFabExpanded = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            MenuItem homeItem = navigationView.getMenu().getItem(DRAWER_ITEM_HOME);

            if (!homeItem.isChecked()) {
                // select home item
                Logger.d("in if.. selecting home item..");
                selectDrawerItem(homeItem);
                return;
            }
        }
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
