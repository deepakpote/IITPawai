package net.mavericklabs.mitra.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.DownloadUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;

import java.io.File;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

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

    @BindView(R.id.download_icon)
    ImageView downloadNewsPDF;

    List<String> imageList;
    private News news;
    private Boolean forSharing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            final Bundle bundle = getIntent().getExtras();
            String newsID = bundle.getString("news_item");
            final Realm realm = Realm.getDefaultInstance();
            news = realm.where(News.class).equalTo("newsID", newsID).findFirst();

            if(news != null) {
                Date date = DateUtils.convertToDate(news.getPublishDate(), "yyyy-MM-dd HH:mm:ss");
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
                    shareNews.setVisibility(View.GONE);
                    downloadNewsPDF.setVisibility(View.GONE);
                } else {
                    showPDF.setVisibility(View.VISIBLE);
                    shareNews.setVisibility(View.VISIBLE);
                    downloadNewsPDF.setVisibility(View.VISIBLE);

                    downloadNewsPDF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            forSharing = false;
                            DownloadUtils.downloadNewsPDF(NewsDetailsActivity.this, news, false);
                        }
                    });

                    shareNews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_share_news), Toast.LENGTH_SHORT).show();
                            forSharing = true;
                            DownloadUtils.downloadNewsPDF(NewsDetailsActivity.this, news, true);
                        }
                    });
                    showPDF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(HttpUtils.isNetworkAvailable(getApplicationContext())) {
                                //If online, load in webview
                                Intent pdfActivity = new Intent(NewsDetailsActivity.this, PDFViewerActivity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("pdf_url", news.getPdfFileURL());
                                pdfActivity.putExtras(bundle1);
                                startActivity(pdfActivity);
                            } else {
                                //If offline, launch intent
                                Logger.d(" news " + DownloadUtils.getFilePath(news.getNewsTitle(), ".pdf"));
                                try {
                                    File file = new File(DownloadUtils.getFilePath(news.getNewsTitle(), ".pdf"));
                                    if(file.exists()) {
                                        Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                                                "net.mavericklabs.mitra.provider", file);

                                        Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                                        intentUrl.setDataAndType(uri, "application/pdf");
                                        intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intentUrl.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(intentUrl);
                                    } else {
                                        Toast.makeText(NewsDetailsActivity.this, getString(R.string.toast_not_available_offline), Toast.LENGTH_LONG).show();
                                    }

                                }
                                catch (ActivityNotFoundException e) {
                                    Toast.makeText(NewsDetailsActivity.this, getString(R.string.toast_cannot_open_file), Toast.LENGTH_LONG).show();
                                }
                            }


                        }
                    });
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        DownloadUtils.onRequestPermissionResult(requestCode, grantResults, NewsDetailsActivity.this,
                news, forSharing);
        forSharing = false;
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
            Glide.with(mContext).load(imageList.get(position)).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    loadingPanel.setVisibility(View.GONE);
                    if(e instanceof ConnectException) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
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
