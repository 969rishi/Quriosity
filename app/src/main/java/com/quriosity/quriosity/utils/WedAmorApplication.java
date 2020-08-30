package com.quriosity.quriosity.utils;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_STS0;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_STS1;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_TIMESTAMP;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_USER_ONLINE_STATUS;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERS_FIRESTORE;
import static com.quriosity.quriosity.utils.MyUtil.CHANNEL_ID;


public class WedAmorApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "WedAmorApplication";
    private WedAmorSharedPreference wedAmorSharedPreference;
    private FirebaseFirestore mDatabase;
    private static Context context;

    private static final AtomicBoolean applicationBackgrounded = new AtomicBoolean(true);
    private static final long INTERVAL_BACKGROUND_STATE_CHANGE = 100;
    private static WeakReference<Activity> currentActivityReference;

    @Override
    public void onCreate() {
        super.onCreate();
        wedAmorSharedPreference = new WedAmorSharedPreference(this);
        context = getApplicationContext();
        createNoficationsChannels();
        mDatabase = FirebaseFirestore.getInstance();
        this.registerActivityLifecycleCallbacks(this);
    }


    private void determineForegroundStatus() {
        if (applicationBackgrounded.get()) {
            onEnterForeground();
            applicationBackgrounded.set(false);
        }
    }

    private void determineBackgroundStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!applicationBackgrounded.get() && currentActivityReference == null) {
                    applicationBackgrounded.set(true);
                    onEnterBackground();
                }
            }
        }, INTERVAL_BACKGROUND_STATE_CHANGE);
    }

    public void onEnterForeground() {
        //This is where you'll handle logic you want to execute when your application enters the foreground
        setIsAppInForeground(true);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (getCheckLogin()) {
            new AsyncCaller().execute();
        }
    }

    public void onEnterBackground() {
        //This is where you'll handle logic you want to execute when your application enters the background
        setIsAppInForeground(false);
        if (getCheckLogin()) {
            new AsyncCallerOffline().execute();
        }
    }

    public void createNoficationsChannels() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId = context.getString(R.string.default_notification_channel_id);
//            String channelName = context.getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager =
//                    context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_HIGH));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId = context.getString(R.string.articles_notification_channel_id);
//            NotificationManager notificationManager =
//                    context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    context.getString(R.string.articles_notification_channel_name), NotificationManager.IMPORTANCE_HIGH));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId = context.getString(R.string.vendor_notification_channel_id);
//            NotificationManager notificationManager =
//                    context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    context.getString(R.string.vendor_notification_channel_name), NotificationManager.IMPORTANCE_HIGH));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId = context.getString(R.string.product_notification_channel_id);
//            NotificationManager notificationManager =
//                    context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    context.getString(R.string.product_notification_channel_name), NotificationManager.IMPORTANCE_HIGH));
//        }
    }

    public boolean getCheckLogin() {
        return wedAmorSharedPreference.getBoolean(WedAmorSharedPreference.SharePrefKey.USER_LOGIN_STATUS, false);
    }

    public void setCheckLogin(boolean status) {
        wedAmorSharedPreference.saveBoolean(WedAmorSharedPreference.SharePrefKey.USER_LOGIN_STATUS, status);
    }

    public boolean getCheckFirstInstall() {
        return wedAmorSharedPreference.getBoolean(WedAmorSharedPreference.SharePrefKey.FIRST_INSTALL, false);
    }

    public void setCheckFirstInstall(boolean status) {
        wedAmorSharedPreference.saveBoolean(WedAmorSharedPreference.SharePrefKey.FIRST_INSTALL, status);
    }

    //user phone number with country code
    public String getPhoneNoWithCountryCode() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.PHONE_NO_CC, "");
    }

    public void setPhoneNoWithCountryCode(String id) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.PHONE_NO_CC, id);
    }
    //user phone number only 10 digit
    public String getPhoneOnly10Digit() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.PHONE_NO, "");
    }

    public void setPhoneOnly10Digit(String id) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.PHONE_NO, id);
    }

    public String getFirebaseRegistrationToken() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.FIREBASE_REGISTRATION_TOKEN, "");
    }

    public void setFirebaseRegistrationToken(String jwttoken) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.FIREBASE_REGISTRATION_TOKEN, jwttoken);
    }


    public String getFirstUserName() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.USER_FIRST_NAME, "");
    }

    public void setFirstUserName(String userName) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.USER_FIRST_NAME, userName);
    }

    public String getLastUserName() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.USER_LAST_NAME, "");
    }

    public void setLastUserName(String userName) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.USER_LAST_NAME, userName);
    }

    public String getPrimaryEmailId() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.PRIMARY_EMAIL_ID, "");
    }

    public void setPrimaryEmailId(String primaryEmailId) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.PRIMARY_EMAIL_ID, primaryEmailId);
    }

    public String getUserImage() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.USER_IMAGE, "");
    }

    public void setUserImage(String userImage) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.USER_IMAGE, userImage);
    }


    public String getfirebaseUid() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.FIREBASE_UID, "");
    }

    public void setfirebaseUid(String fireuid) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.FIREBASE_UID, fireuid);
    }

    public String getCurrentActiveChatID() {
        return wedAmorSharedPreference.getString(WedAmorSharedPreference.SharePrefKey.CURRENT_ACTIVE_CHATID, "0");
    }

    public void setCurrentActiveChatID(String fireuid) {
        wedAmorSharedPreference.saveString(WedAmorSharedPreference.SharePrefKey.CURRENT_ACTIVE_CHATID, fireuid);
    }

    //below code to get/set if user set their current selected address
    public Boolean getIsAppInForeground() {
        return wedAmorSharedPreference.getBoolean(WedAmorSharedPreference.SharePrefKey.isAPPFORGROUND, false);
    }

    public void setIsAppInForeground(Boolean isAppInForeground) {
        wedAmorSharedPreference.saveBoolean(WedAmorSharedPreference.SharePrefKey.isAPPFORGROUND, isAppInForeground);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        WedAmorApplication.currentActivityReference = new WeakReference<>(activity);
        determineForegroundStatus();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        WedAmorApplication.currentActivityReference = null;
        determineBackgroundStatus();
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument
            // and u can access the parent class' variable url over here
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "doInBackground: this is AsyncCaller running in Background ");
            fireUpdateUserData(getfirebaseUid(),
                    EXTRAS_USER_ONLINE_STATUS,
                    EXTRAS_ONLINE_STS1);

            //update user online timestamp
            fireUpdateUserData(
                    getfirebaseUid(),
                    EXTRAS_ONLINE_TIMESTAMP, MyUtil.getTimeStampInMillis());

            return null;
        }

    }

    private class AsyncCallerOffline extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument
            // and u can access the parent class' variable url over here
            Log.d(TAG, "doInBackground: this is AsyncCallerOffline running in Background ");
            fireUpdateUserData(getfirebaseUid(),
                    EXTRAS_USER_ONLINE_STATUS,
                    EXTRAS_ONLINE_STS0);
            //update user online timestamp
            fireUpdateUserData(
                    getfirebaseUid(),
                    EXTRAS_ONLINE_TIMESTAMP, MyUtil.getTimeStampInMillis());

            return null;
        }
    }


    public void fireUpdateUserData(final String id,
                                   final String fieldKey,
                                   final Object objectvalue) {
        final DocumentReference newCityRef = mDatabase.collection(USERS_FIRESTORE)
                .document(id);
        newCityRef.update(
                fieldKey, objectvalue
        );

        Log.d(TAG, "fireUpdateUserStatus: user status is " + objectvalue);
    }

}
