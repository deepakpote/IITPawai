package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.annotation.AnimatorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class AlmostDoneActivity extends AppCompatActivity {

    @BindView(R.id.activity_almost_done)
    RelativeLayout layout;

    @BindView(R.id.background_image)
    ImageView background;

    @OnTouch(R.id.background_image)
    boolean interruptThread() {
        if(timerThread != null) {
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
        timerThread = new Thread()  {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent selectLanguage = new Intent(AlmostDoneActivity.this,ProfileActivity.class);
                    startActivity(selectLanguage);
                    finishAffinity();
                }
            }
        };
        timerThread.start();
    }
}
