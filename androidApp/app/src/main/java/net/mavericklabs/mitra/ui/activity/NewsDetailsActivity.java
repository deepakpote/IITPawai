package net.mavericklabs.mitra.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.model.News;
import net.mavericklabs.mitra.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.news_author)
    TextView author;

    @BindView(R.id.news_content)
    TextView content;

    @BindView(R.id.news_image)
    ImageView image;

    @BindView(R.id.news_title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            News news = (News) bundle.getSerializable("news_item");
            if(news != null) {
                author.setText(news.getAuthor());
                content.setText(news.getContent());
                title.setText(news.getNewsTitle());
                if(!StringUtils.isEmpty(news.getImageURL())) {
                    Glide.with(this).load("http://placehold.it/500x150").into(image);
                }
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(news.getNewsTitle());
                }
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
}
