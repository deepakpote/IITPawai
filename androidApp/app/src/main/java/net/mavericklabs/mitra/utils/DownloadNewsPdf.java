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

package net.mavericklabs.mitra.utils;

import android.os.AsyncTask;

import net.mavericklabs.mitra.model.News;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by vishakha on 07/04/17.
 */

public class DownloadNewsPdf extends AsyncTask<News, Void, Void> {

    public String mitraDirectoryPath;

    public DownloadNewsPdf(String mitraDirectoryPath) {
        this.mitraDirectoryPath = mitraDirectoryPath;
    }

    @Override
    protected Void doInBackground(News... newsList) {

        for (final News news : newsList) {
            Logger.d("Downloading " + news.getNewsTitle());

            final OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url(news.getPdfFileURL())
                        .build();
                String extension = ".pdf";
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()) {
                    String downloadFileName = DownloadUtils.getFilePath(news.getNewsTitle(), extension);
                    File downloadedFile = new File(downloadFileName);
                    BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                    sink.writeAll(response.body().source());
                    sink.close();
                } else {
                    Logger.d("Download failed " + response.toString());
                }

            }catch (IllegalArgumentException ex) {
                Logger.d(" error " + ex.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
