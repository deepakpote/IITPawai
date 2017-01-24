package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.news_details)
    TextView newsDetails;

    @BindView(R.id.news_content)
    TextView content;

    @BindView(R.id.image_view_pager)
    ViewPager imageViewPager;

    @BindView(R.id.news_title)
    TextView title;

    ImagePagerAdapter pagerAdapter;
    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);


        imageList = new ArrayList<>();
        imageList.add("http://placehold.it/500x400/ffff00/000000");
        imageList.add("http://placehold.it/500x100/ff00ff/000000");
        imageList.add("http://placehold.it/500x500/00ffff/000000");
        imageList.add("http://placehold.it/500x150/ff0000/000000");
        imageList.add("http://placehold.it/500x150/00ff00/000000");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new ImagePagerAdapter(getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        imageViewPager.setAdapter(pagerAdapter);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String newsID = bundle.getString("news_item");
            Realm realm = Realm.getDefaultInstance();
            News news = realm.where(News.class).equalTo("newsID", newsID).findFirst();

            if(news != null) {
                Date date = DateUtils.convertToDate(news.getCreatedOn(), "yyyy-MM-dd'T'HH:mm:ss'Z'");
                String dateString = DateUtils.convertToString(date, "dd MMM, yyyy");
                newsDetails.setText(dateString);
                content.setText(news.getContent());
                title.setText(news.getNewsTitle());
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(news.getNewsTitle());
                }

                realm.beginTransaction();
                news.setSeen(true);
                realm.commitTransaction();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ImagePagerAdapter extends android.support.v4.view.PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        ImagePagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);
            Glide.with(mContext).load(imageList.get(position)).into(imageView);

            ImageView leftArrow = (ImageView) itemView.findViewById(R.id.left_arrow);
            ImageView rightArrow = (ImageView) itemView.findViewById(R.id.right_arrow);

            if(position == 0) {
                leftArrow.setVisibility(View.GONE);
            }

            if(position == getCount() - 1) {
                rightArrow.setVisibility(View.GONE);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
