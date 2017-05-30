package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class AlmostDoneActivity extends BaseActivity {

    @BindView(R.id.activity_almost_done)
    RelativeLayout layout;

    @BindView(R.id.background_image)
    ImageView background;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnTouch(R.id.background_image)
    boolean interruptThread() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
        return true;
    }
    
    private Thread timerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almost_done);
        ButterKnife.bind(this);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
            }
        }
        timerThread = new Thread()  {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    Intent profileActivity = new Intent(AlmostDoneActivity.this,EditProfileActivity.class);
                    startActivity(profileActivity);
                    finishAffinity();


                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        interruptThread();
    }
}
