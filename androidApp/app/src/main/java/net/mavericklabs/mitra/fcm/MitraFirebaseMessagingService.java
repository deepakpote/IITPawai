package net.mavericklabs.mitra.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.Api;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.database.Language;
import net.mavericklabs.mitra.model.database.Migration;
import net.mavericklabs.mitra.ui.activity.ContentDetailsActivity;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.activity.NewsDetailsActivity;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.utils.MitraContextWrapper;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 12/7/16.
 */

public class MitraFirebaseMessagingService extends FirebaseMessagingService{

    //Add more notification types here
    public final String NOTIFICATION_TYPE_DEFAULT = "0";

    @Override
    protected void attachBaseContext(Context newBase) {

        //Set realm schema version
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(4) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run
                .build();

        Realm.setDefaultConfiguration(config);


        Context context = MitraContextWrapper.wrap(newBase, LanguageUtils.getCurrentLocale());
        super.attachBaseContext(context);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.d("message received : " + remoteMessage.getData());
        Logger.d("message received : " + remoteMessage.getData().get("body"));

        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        String type = remoteMessage.getData().get("type");
        String objectId = remoteMessage.getData().get("ObjectID");
        Logger.d("object id is " + objectId);
        String notificationTypeCodeId = remoteMessage.getData().get("notificationTypeCodeID");
        if(StringUtils.isEmpty(type)) {
            type = NOTIFICATION_TYPE_DEFAULT;
        }

        DbNotification notification = new DbNotification(title,
                                                body,
                                                type,
                                                notificationTypeCodeId,
                                                objectId,
                                                System.currentTimeMillis());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insert(notification);
        realm.commitTransaction();

        if(objectId != null) {
            switch (notificationTypeCodeId) {
                case Constants.NotificationTypeNews:
                    fetchNews(objectId, title, body);
                    break;
                case Constants.NotificationTypeTeachingAids: // deliberate fall-through
                case Constants.NotificationTypeSelfLearning:
                    fetchContent(objectId, title, body);
                    break;
                case Constants.NotificationTypeTraining:
                    /*
                     * showing default notification for now. can handle training notifications in
                     * later phase
                     */
                    showDefaultNotification(title, body);
                    break;
                case Constants.NotificationTypeOther:
                    /*
                     * showing default notification for now. can handle training notifications in
                     * later phase
                     */
                    showDefaultNotification(title, body);
                    break;
                default:
                    showDefaultNotification(title, body);
                    break;
            }
        } else {
            showDefaultNotification(title,body);
        }

    }

    private void fetchContent(final String contentId, final String title, final String body) {
        String token = UserDetailUtils.getToken(getApplicationContext());
        int languageCodeId = LanguageUtils.getCurrentLanguage();
        Api api = RestClient.getApiService(token);
        api.getContent(contentId,languageCodeId).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d("RESPONSE RECEIVED!");
                if(response.isSuccessful()) {
                    Content content = response.body().getData().get(0);
                    Logger.d("content is " + content);
                    if(content != null) {
                        Intent intent = new Intent(getApplicationContext(),ContentDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("content",content);
                        intent.putExtras(bundle);
                        Logger.d("set intent and move forward..");
                        intent.setAction(Long.toString(System.currentTimeMillis()));
                        proceedToShowNotification(intent,title,body);
                    } else {
                        showDefaultNotification(title,body);
                        Logger.d("call failed.. B");
                    }
                } else {
                    Logger.d("call failed..");
                    showDefaultNotification(title,body);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d("call failed.. on failure.." + t.getLocalizedMessage());
            }
        });
    }

    private void fetchNews(final String newsId, final String title, final String body) {
        Api api = RestClient.getApiService("");
        api.listNews(LanguageUtils.getCurrentLanguage()).enqueue(new Callback<BaseModel<News>>() {
            @Override
            public void onResponse(Call<BaseModel<News>> call, Response<BaseModel<News>> response) {
                if(response.isSuccessful()) {
                    List<News> newsList = response.body().getData();
                    Logger.d("news id " + newsList.get(0).getNewsID());
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(newsList);
                    realm.commitTransaction();

                    RealmResults<News> results = realm.where(News.class).equalTo("newsID",newsId).findAll();
                    if(results.size() > 0) {
                        String newsId = results.first().getNewsID();

                        Intent intent = new Intent(getApplicationContext(),NewsDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("news_item",newsId);
                        intent.putExtras(bundle);
                        intent.setAction(Long.toString(System.currentTimeMillis()));
                        proceedToShowNotification(intent,title,body);
                    } else {
                        showDefaultNotification(title,body);
                    }
                } else {
                    showDefaultNotification(title,body);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<News>> call, Throwable t) {
                showDefaultNotification(title,body);
            }
        });
    }

    private void showDefaultNotification(String title, String body) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("show_notification",true);
        intent.putExtras(bundle);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        proceedToShowNotification(intent,title,body);
    }

    private void proceedToShowNotification(Intent intent, String title, String body) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.mipmap.ic_launcher);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_icon_black_white_small)
                        .setLargeIcon(notificationLargeIconBitmap)
                        .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0))
                        .setContentText(body);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
