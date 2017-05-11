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

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.ContentDataRequest;
import net.mavericklabs.mitra.model.api.ContentDataResponse;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishakha on 07/04/17.
 */

public class DownloadUtils {

    public static final int EXTERNAL_STORE_WRITE_REQUEST_CODE = 1;

    private static void downloadVideo(final Activity activity, final String contentID) {

        final Context context = activity.getApplicationContext();
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(context.getString(R.string.youtube_save_offline_dialog))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final ProgressDialog progressDialog = new ProgressDialog(activity);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(context.getString(R.string.loading));

                        String token = UserDetailUtils.getToken(context);
                        Call<BaseModel<ContentDataResponse>> saveRequest = RestClient.getApiService(token)
                                .download(new ContentDataRequest(contentID));

                        saveRequest.enqueue(new Callback<BaseModel<ContentDataResponse>>() {

                            @Override
                            public void onResponse(Call<BaseModel<ContentDataResponse>> call, Response<BaseModel<ContentDataResponse>> response) {
                                if(progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if (response.isSuccessful()) {
                                    List<ContentDataResponse> responseList = response.body().getData();
                                    Logger.d(" file " + responseList.get(0).getFileName());

                                    String url = responseList.get(0).getFileName();

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                    Realm realm = Realm.getDefaultInstance();
                                    Content content = realm.where(Content.class).equalTo("contentID",contentID).findFirst();
                                    if(content != null) {
                                        realm.beginTransaction();
                                        content.setDownloaded(true);
                                        realm.copyToRealmOrUpdate(content);
                                        realm.commitTransaction();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseModel<ContentDataResponse>> call, Throwable t) {
                                if(progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if(t instanceof ConnectException) {
                                    Toast.makeText(context, context.getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(context.getString(R.string.not_now), null)
                .create();

        alertDialog.show();
    }

    public static void downloadResource(Content content, Activity activity) {
        Logger.d(" download resource");
        if(content.getFileType().equals(Constants.FileTypeVideo)) {
            downloadVideo(activity, content.getContentID());
        } else {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                Logger.d(" not granted");
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORE_WRITE_REQUEST_CODE);

                // onRequestPermissionsResult method gets the result of the request.
            } else {
                Logger.d(" download pdf");
                downloadContent(activity, content);
            }
        }
    }

    private static void downloadContent(final Activity activity, final Content content) {

        final Context context = activity.getApplicationContext();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.loading));

        String token = UserDetailUtils.getToken(context);
        Call<BaseModel<ContentDataResponse>> saveRequest = RestClient.getApiService(token)
                .download(new ContentDataRequest(content.getContentID()));

        saveRequest.enqueue(new Callback<BaseModel<ContentDataResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<ContentDataResponse>> call, Response<BaseModel<ContentDataResponse>> response) {
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response.isSuccessful()) {
                    List<ContentDataResponse> responseList = response.body().getData();
                    Logger.d(" file " + responseList.get(0).getFileName());

                    String url = responseList.get(0).getFileName();
                    String extension;
                    if(content.getFileType() == Constants.FileTypeAudio) {
                        extension = ".mp3";
                    } else {
                        extension = ".pdf";
                    }

                    downloadPDF(url, content.getTitle(), extension, activity, false);

                }
            }

            @Override
            public void onFailure(Call<BaseModel<ContentDataResponse>> call, Throwable t) {
                Logger.d(" on failure ");
                if(progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(t instanceof ConnectException) {
                    Toast.makeText(activity, context.getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private static void downloadPDF(String url, final String title, final String extension, Activity activity,
                                    final boolean forSharing) {
        // get download service and enqueue file
        final String mitraDirectoryPath = createDirectoryStructure();
        final Context context = activity.getApplicationContext();

        final OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    if(response.isSuccessful()) {
                        String downloadFileName = mitraDirectoryPath +
                                File.separator + title + extension;
                        final File downloadedFile = new File(downloadFileName);
                        BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                        sink.writeAll(response.body().source());
                        sink.close();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getString(R.string.download_complete),
                                        Toast.LENGTH_SHORT).show();
                                if(forSharing) {
                                    String shareBody = context.getString(R.string.message_news_share) + title;
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("application/pdf");
                                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(downloadedFile));
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MITRA " + title);
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_title)));
                                }
                            }
                        });
                    }
                }

                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }
            });

            Toast.makeText(context, context.getString(R.string.download_file_location,
                    mitraDirectoryPath + File.separator +title),
                    Toast.LENGTH_LONG).show();
        }catch (IllegalArgumentException ex) {
            Logger.d(" error " + ex.getMessage());
            Toast.makeText(activity, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    public static void onRequestPermissionResult(int requestCode, @NonNull int[] grantResults, Activity activity,
                                                 Content content) {
        if(requestCode == DownloadUtils.EXTERNAL_STORE_WRITE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadContent(activity, content);
            } else {
                Toast.makeText(activity, "Permission denied. Unable to download.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void onRequestPermissionResult(int requestCode, @NonNull int[] grantResults, Activity activity,
                                                 News news, boolean forSharing) {
        if(requestCode == DownloadUtils.EXTERNAL_STORE_WRITE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPDF(news.getPdfFileURL(), news.getNewsTitle(), ".pdf", activity, forSharing);
            } else {
                Toast.makeText(activity, activity.getString(R.string.error_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String createDirectoryStructure(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append(File.separator);
        stringBuilder.append("MITRA");
        String mitraDirectoryPath = stringBuilder.toString();
        File mitraDirectory = new File(mitraDirectoryPath);
        Logger.d("Directory Path " + mitraDirectoryPath);
        mitraDirectory.mkdirs();

        return mitraDirectoryPath;
    }

    public static void downloadNewsPDF(Activity activity, News news, boolean forSharing) {

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission.
            Logger.d(" not granted");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORE_WRITE_REQUEST_CODE);

            // onRequestPermissionsResult method gets the result of the request.
        } else {
            Logger.d(" download pdf");
            downloadPDF(news.getPdfFileURL(), news.getNewsTitle(), ".pdf", activity, forSharing);
        }
    }

    public static String getFilePath(String title, String extension) {
        final String mitraDirectoryPath = createDirectoryStructure();
        return mitraDirectoryPath +
                File.separator + title + extension;
    }
}
