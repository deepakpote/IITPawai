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

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PDFViewerActivity extends AppCompatActivity {

    @BindView(R.id.content_web_view) WebView contentWebView;

    @BindView(R.id.loading_panel_for_web_view)
    RelativeLayout loadingPanelForWebView;

    private String pdfURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        pdfURL = bundle.getString("pdf_url");


        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);
        contentWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingPanelForWebView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Logger.d("on load resource..");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.d("on page finished..");
                loadingPanelForWebView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Logger.d("error : " + error.toString());
                loadingPanelForWebView.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Logger.d("received http error : ");
            }
        });
        Logger.d("file path " + pdfURL);
        contentWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + "http://www.pdf995.com/samples/pdf.pdf");
        //contentWebView.loadUrl("http://www.google.com");

    }
}
