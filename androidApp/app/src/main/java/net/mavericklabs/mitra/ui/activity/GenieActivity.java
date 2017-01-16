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

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenieActivity extends AppCompatActivity {

    @BindView(R.id.content_web_view)
    WebView contentWebView;

    private Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genie);

        ButterKnife.bind(this);

        final Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            content = (Content) bundle.getSerializable("content");
        }

        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);
        contentWebView.loadUrl(content.getFileName());
    }
}
