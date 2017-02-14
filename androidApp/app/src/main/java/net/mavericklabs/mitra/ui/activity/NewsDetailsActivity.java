package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewsDetailsActivity extends BaseActivity {

    @BindView(R.id.news_details)
    TextView newsDetails;

    @BindView(R.id.news_content)
    TextView content;

    @BindView(R.id.image_view_pager)
    ViewPager imageViewPager;

    @BindView(R.id.news_title)
    TextView title;

    @BindView(R.id.show_pdf)
    ImageView showPDF;

    @BindView(R.id.save_news)
    ImageView saveNews;

    @BindView(R.id.share_news)
    ImageView shareNews;

    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            final Bundle bundle = getIntent().getExtras();
            String newsID = bundle.getString("news_item");
            final Realm realm = Realm.getDefaultInstance();
            final News news = realm.where(News.class).equalTo("newsID", newsID).findFirst();

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
                if(!StringUtils.isEmpty(news.getImageURL())) {
                    imageList = StringUtils.splitCommasToStrings(news.getImageURL());

                    if(imageList != null && !imageList.isEmpty()) {
                        Logger.d(" image list is not empty " + imageList.size() + " url " + imageList.get(0));
                        imageViewPager.setVisibility(View.VISIBLE);
                        // Create the adapter that will return a fragment for each of images
                        ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(getApplicationContext());

                        // Set up the ViewPager with the sections adapter.
                        imageViewPager.setAdapter(pagerAdapter);
                    } else {
                        imageViewPager.setVisibility(View.GONE);
                    }
                }


                realm.beginTransaction();
                news.setSeen(true);
                realm.commitTransaction();
                setBookmarkIcon(news);

                saveNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.beginTransaction();
                        //Toggle
                        news.setSaved(!news.isSaved());
                        realm.commitTransaction();
                        setBookmarkIcon(news);
                    }
                });

                if(StringUtils.isEmpty(news.getPdfFileURL())) {
                    showPDF.setVisibility(View.GONE);
                } else {
                    showPDF.setVisibility(View.VISIBLE);
                    showPDF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Logger.d(" clicked " + news.getPdfFileURL());
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getPdfFileURL()));
//                            startActivity(browserIntent);
                            Intent pdfActivity = new Intent(NewsDetailsActivity.this, PDFViewerActivity.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("pdf_url", news.getPdfFileURL());
                            pdfActivity.putExtras(bundle1);
                            startActivity(pdfActivity);

                        }
                    });
                }


                shareNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //What is to be done?
                    }
                });
            }
        }
    }

    private void setBookmarkIcon(News news) {
        //If saved news now,
        if(news.isSaved()) {
            saveNews.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_bookmark_accent_24dp));
        } else {
            saveNews.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_bookmark_grey_24dp));
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
            // Show total pages.
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            final RelativeLayout loadingPanel = (RelativeLayout) itemView.findViewById(R.id.loading_panel_layout);
            loadingPanel.setVisibility(View.VISIBLE);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);
            Glide.with(mContext).load(imageList.get(position)).diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    loadingPanel.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    loadingPanel.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageView);

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
