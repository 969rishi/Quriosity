package com.quriosity.quriosity.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.DMChatActivity;
import com.quriosity.quriosity.activity.firechat.GroupChatActivity;
import com.quriosity.quriosity.utils.MyUtil;
import com.quriosity.quriosity.utils.MyWorker;
import com.quriosity.quriosity.utils.WedAmorApplication;

import java.util.Map;

import static android.content.Intent.EXTRA_REFERRER_NAME;
import static com.quriosity.quriosity.utils.FirebaseUtil.CHATS;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ACTOR2;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MEACTOR1;
import static com.quriosity.quriosity.utils.FirebaseUtil.USERS_FIRESTORE;
import static com.quriosity.quriosity.utils.FirebaseUtil.MESSAGES;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_FCM_SERVICES;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String myFirebaseID;
    public FirebaseFirestore mDatabase;
    private Map<String, String> hashMap;


    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
        Log.d(TAG, "onSendError: ");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: ");
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        Log.d(TAG, "onMessageSent: ");
    }

    public MyFirebaseMessagingService() {
        super();
        Log.d(TAG, "MyFirebaseMessagingService: constructor");
        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    protected Intent getStartCommandIntent(Intent intent) {
        Log.d(TAG, "getStartCommandIntent: ");
        return super.getStartCommandIntent(intent);
    }

    @Override
    public boolean handleIntentOnMainThread(Intent intent) {
        Log.d(TAG, "handleIntentOnMainThread: ");
        return super.handleIntentOnMainThread(intent);
    }

    @Override
    public void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: ");
        super.handleIntent(intent);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        myFirebaseID = ((WedAmorApplication) getApplicationContext()).getfirebaseUid();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            hashMap = remoteMessage.getData();
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

            //do entry in received array
            handleNow();
            Log.d(TAG, "onMessageReceived: ConversationType " + hashMap.get(MyUtil.NOTI_action));
            if (hashMap.get(MyUtil.NOTI_action).equalsIgnoreCase("OnetoOneActivity")) {
                if (((WedAmorApplication) getApplicationContext()).getIsAppInForeground()) {
                    String currntActiveChat = ((WedAmorApplication) getApplicationContext()).getCurrentActiveChatID();
//                Log.d(TAG, "onMessageReceived: IsAppInForeground Yes \n currntAChID" + currntActiveChat + "serverChID " + fcm_model.getChatID());
                    Log.d(TAG, "onMessageReceived: IsAppInForeground Yes");
                    if (!currntActiveChat.equalsIgnoreCase("0") && !currntActiveChat.equalsIgnoreCase(hashMap.get(MyUtil.NOTI_chatID))) {
                        if (hashMap.get(MyUtil.NOTI_msgtype).equalsIgnoreCase("photo")) {
                            readyImageNotification();
                        } else
                            readyTextNotification();

                    }
                } else {
                    Log.d(TAG, "onMessageReceived: IsAppInForeground No ");
                    if (hashMap.get(MyUtil.NOTI_msgtype).equalsIgnoreCase("photo")) {
                        readyImageNotification();
                    } else
                        readyTextNotification();
                }
            } else {
                if (hashMap.get(MyUtil.NOTI_actorOtherId).equalsIgnoreCase(myFirebaseID)) {
                    Log.d(TAG, "onMessageReceived: my own msg notification received from group");
                    return;
                }
                if (((WedAmorApplication) getApplicationContext()).getIsAppInForeground()) {
                    String currntActiveChat = ((WedAmorApplication) getApplicationContext()).getCurrentActiveChatID();
//                Log.d(TAG, "onMessageReceived: IsAppInForeground Yes \n currntAChID" + currntActiveChat + "serverChID " + fcm_model.getChatID());
                    Log.d(TAG, "onMessageReceived: IsAppInForeground Yes chatID= " + currntActiveChat);
                    if (!currntActiveChat.equalsIgnoreCase("0") && !currntActiveChat.equalsIgnoreCase(hashMap.get(MyUtil.NOTI_chatID))) {
                        if (hashMap.get(MyUtil.NOTI_msgtype).equalsIgnoreCase("photo")) {
                            groupReadyImageNotification();
                        } else
                            groupReadyTextNotification();

                    }
                } else {
                    Log.d(TAG, "onMessageReceived: IsAppInForeground No ");
                    if (hashMap.get(MyUtil.NOTI_msgtype).equalsIgnoreCase("photo")) {
                        groupReadyImageNotification();
                    } else
                        groupReadyTextNotification();
                }
            }
        }


    }

    private void groupReadyTextNotification() {
        String userIcon = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + hashMap.get(MyUtil.NOTI_actorOtherId) + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Glide.with(this)
                .asBitmap()
                .load(userIcon)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        sendGroupNotification(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body), hashMap.get(MyUtil.NOTI_chatID), resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        sendGroupNotificationWithoutImg(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body), hashMap.get(MyUtil.NOTI_chatID));
                    }
                });
    }

    private void groupReadyImageNotification() {
        String userIcon = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + hashMap.get(MyUtil.NOTI_actorOtherId) + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Glide.with(this)
                .asBitmap()
                .load(userIcon)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        sendGroupNotification(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body) + " sent you an image 📷", hashMap.get(MyUtil.NOTI_chatID), resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        sendGroupNotificationWithoutImg(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body) + " sent you an image 📷", hashMap.get(MyUtil.NOTI_chatID));
                    }
                });
    }
    // [END receive_message]


    private void readyTextNotification() {
        String userIcon = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + hashMap.get(MyUtil.NOTI_actorOtherId) + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Glide.with(this)
                .asBitmap()
                .load(userIcon)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        sendNotification(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body), hashMap.get(MyUtil.NOTI_chatID),
                                hashMap.get(MyUtil.NOTI_actorMeId), hashMap.get(MyUtil.NOTI_actorOtherId), userIcon, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        sendNotificationWithOutImg(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_body), hashMap.get(MyUtil.NOTI_chatID),
                                hashMap.get(MyUtil.NOTI_actorMeId), hashMap.get(MyUtil.NOTI_actorOtherId), userIcon);
                    }
                });
    }

    private void readyImageNotification() {
        String userIcon = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + hashMap.get(MyUtil.NOTI_actorOtherId) + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Glide.with(this)
                .asBitmap()
                .load(userIcon)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        sendNotification(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_NAME) + " sent you an image 📷", hashMap.get(MyUtil.NOTI_chatID),
                                hashMap.get(MyUtil.NOTI_actorMeId), hashMap.get(MyUtil.NOTI_actorOtherId), userIcon, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        sendNotificationWithOutImg(hashMap.get(MyUtil.NOTI_NAME), hashMap.get(MyUtil.NOTI_NAME) + " sent you an image 📷", hashMap.get(MyUtil.NOTI_chatID),
                                hashMap.get(MyUtil.NOTI_actorMeId), hashMap.get(MyUtil.NOTI_actorOtherId), userIcon);
                    }
                });
    }


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (((WedAmorApplication) getApplicationContext()).getCheckLogin())
            sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
        fireMsgReceivedEntry(hashMap.get(MyUtil.NOTI_chatID), hashMap.get(MyUtil.NOTI_msgID), myFirebaseID);
        latestMsgReceivedEntry(hashMap.get(MyUtil.NOTI_chatID), myFirebaseID);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        fireUpdateLoginData(((WedAmorApplication) getApplicationContext()).getfirebaseUid(),
                "registrationtoken", token);
//        Log.d(TAG, "sendRegistrationToServer: " + token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     *                    //@param resource
     */
    private void sendNotification(String title, String messageBody, String chatid, String actorMe, String actorOther, String actorOtherProfileUrl, Bitmap actor2bitmap) {
        Intent intent = new Intent(this, DMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRAS_CHATID, chatid);
        intent.putExtra(EXTRAS_MEACTOR1, actorMe);
        intent.putExtra(EXTRAS_ACTOR2, actorOther);
        intent.putExtra(EXTRA_REFERRER_NAME, EXTRAS_FCM_SERVICES);
//        intent.putExtra(Intent.EXTRA_ORIGINATING_URI, actorOtherProfileUrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setLargeIcon(actor2bitmap)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationWithOutImg(String title, String messageBody, String chatid, String actorMe, String actorOther, String actorOtherProfileUrl) {
        Intent intent = new Intent(this, DMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRAS_CHATID, chatid);
        intent.putExtra(EXTRAS_MEACTOR1, actorMe);
        intent.putExtra(EXTRAS_ACTOR2, actorOther);
        intent.putExtra(EXTRA_REFERRER_NAME, EXTRAS_FCM_SERVICES);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendGroupNotification(String title, String messageBody, String chatid, Bitmap actor2bitmap) {
        Intent intent = new Intent(this, GroupChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRAS_CHATID, chatid);
        intent.putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_FCM_SERVICES);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setLargeIcon(actor2bitmap)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendGroupNotificationWithoutImg(String title, String messageBody, String chatid) {
        Intent intent = new Intent(this, GroupChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRAS_CHATID, chatid);
        intent.putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_FCM_SERVICES);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    public void fireMsgReceivedEntry(final String chatId,
                                     final String msgId,
                                     final String objectvalue) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId).collection(MESSAGES).document(msgId);
        newCityRef.update(
                "receivedarray", FieldValue.arrayUnion(objectvalue)
        );

        Log.d(TAG, "fireUpdateUserStatus: user status is " + objectvalue);
    }

    //below code for update read entry chat collection
    public void latestMsgReceivedEntry(final String chatId,
                                       final String item) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId);
        newCityRef.update("latestMessage.receivedarray", FieldValue.arrayUnion(item));
    }

    //below code for update user Online Status
    public void fireUpdateLoginData(final String id,
                                    final String fieldKey,
                                    final Object objectvalue) {
        final DocumentReference newCityRef = mDatabase.collection(USERS_FIRESTORE)
                .document(id);
        newCityRef.update(
                fieldKey, objectvalue
        );
    }
}