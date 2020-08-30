package com.quriosity.quriosity.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quriosity.quriosity.models.FirestoreUsersModel;

public class FirebaseUtil {
    public static final String USERS_FIRESTORE = "users";
    public static final String PRIMARYEMAIL_FIRESTORE = "primaryemail";
    public static final String MOBILE_FIRESTORE = "usermobile";
    public static final String USERNAME_FIRESTORE = "username";
    public static final String MOBILE_10_FIRESTORE = "user10digitmobile";
    public static final String SIGNUP_METHOD_FIRESTORE = "signupmethod";
    public static final String FIRSTNAME_FIRESTORE = "firstname";
    public static final String USERTYPE_FIRESTORE = "usertyperoll";
    public static final String FIRE_UID_FIRESTORE = "firebaseuid";
    public static final String PASSWORD_FIRESTORE = "userpassword";
    public static final String USERS_REGISTRATION_TOKEN_FIELD = "registrationToken";
    public static final String USERS_LAST_LOGIN = "lastlogin";

    public static final String SIGNUP_METHOD_PHONE = "phone";
    public static final String USER_TYPE_CUSTOMER = "customer";

    public static final int LIMIT = 15;
    public static final String CHATS = "chats";
    public static final String CONVERSATION_ACTORS = "conversationActors";
    public static final String CONVERSATION_TITLE = "conversationTitle";
    public static final String EXTRAS_TYPING_ARR = "typingarray";
    public static final String FRIENDS = "friends";
    public static final String FRIENDS_LIST = "firendslist";
    public static final String MESSAGES = "messages";
    public static final String EXTRAS_CHATID = "chatsID";
    public static final String EXTRAS_MEACTOR1 = "SELF_ACTOR1";
    public static final String EXTRAS_ACTOR2 = "ACTOR_ID_2";
    public static final String EXTRAS_ACTIVE_CONVERSATION_ID = "activeConversationId";
    public static final String EXTRAS_USER_ONLINE_STATUS = "onlineStatus";
    public static final String EXTRAS_ONLINE_STS0 = "offline";
    public static final String EXTRAS_ONLINE_STS1 = "online";
    public static final String EXTRAS_ONLINE_STS2 = "typing";
    public static final String EXTRAS_ONLINE_STS3 = "audio_recording";
    public static final String EXTRAS_ONLINE_STS4 = "video_recording";
    public static final String EXTRAS_ONLINE_STS5 = "video_recording";
    public static final String EXTRAS_ONLINE_TIMESTAMP = "lastOnlineTimestamp";
    public static final String EXTRAS_LAST_LOGIN = "lastLoginAt";
    public static final String EXTRAS_MSG_TYPE_TXT = "text";
    public static final String EXTRAS_MSG_TYPE_IMG = "image";
    public static final String EXTRAS_MSG_TYPE_VIDEO = "video";
    public static final String CONVERSATION_TYPE_DM = "DM";
    public static final String CONVERSATION_TYPE_GROUP = "GROUP";
    public static final String FIRSTNAME_FIRE = "firstname";
    public static final int ONLINE_STS_JOBID = 1001;


    public static String myfuid, userfirstname;
    public static FirebaseFirestore mDatabase;
    private static String timestampMillis = MyUtil.getTimeStampInMillis();
    private static String TAG = "FirebaseUtil";
    //private static WriteBatch batchOperation;
    private static Context context;

    public FirebaseUtil(Context splashContext) {
        context = splashContext;
        mDatabase = FirebaseFirestore.getInstance();
        reInstatiateValues();
    }

    public static void reInstatiateValues() {
        myfuid = ((WedAmorApplication) context.getApplicationContext()).getfirebaseUid();
        userfirstname = ((WedAmorApplication) context.getApplicationContext()).getFirstUserName().trim();
        Log.d(TAG, "reInstatiateValues: myfuid " + myfuid);
        Log.d(TAG, "reInstatiateValues: User First name " + userfirstname);
    }


    public static void uploadMIMEImage(final Uri imgUri, final String conversationID, final String msgID, String imgName) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = mStorageRef
                .child("chats")
                .child(conversationID)
                .child("images")
                .child(imgName);
        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(imgUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        Log.d(TAG, "uploadFromUri: upload success");
                        // Request the public download URL
                        return photoRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri: getDownloadUri success " + downloadUri);
                        fireUpdateImageEntry(conversationID, msgID, downloadUri.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                    }
                });
    }


    //below code for add typing status
    public static void fireAddConversation(final String chatID,
                                           final String item) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatID);
        newCityRef.update(EXTRAS_TYPING_ARR, FieldValue.arrayUnion(item)
        );

        Log.d(TAG, "fireUpdateConversation: userid added to typing " + item);
    }

    //below code for remove typing Status
    public static void fireRemoveConversation(final String chatID,
                                              final String item) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatID);
        newCityRef.update(EXTRAS_TYPING_ARR, FieldValue.arrayRemove(item)
        );

        Log.d(TAG, "fireRemoveConversation: userid removed to typing " + item);
    }

    //below code for update user Online Status
    public static void fireUpdateUserData(final String id,
                                          final String fieldKey,
                                          final Object objectvalue) {
        final DocumentReference newCityRef = mDatabase.collection(USERS_FIRESTORE)
                .document(id);
        newCityRef.update(
                fieldKey, objectvalue
        );

        Log.d(TAG, "fireUpdateUserStatus: user status is " + objectvalue);
    }

    //below code for Read entry message collection
    public static void fireUpdateImageEntry(final String chatId,
                                            final String msgId,
                                            final String imgUrl) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId).collection(MESSAGES).document(msgId);
        newCityRef.update(
                "url", imgUrl
        );
    }

    //below code for Read entry message collection

    public static void fireMsgReadEntry(final String chatId,
                                        final String msgId,
                                        final String objectvalue) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId).collection(MESSAGES).document(msgId);
        newCityRef.update(
                "readarray", FieldValue.arrayUnion(objectvalue)
        );

        Log.d(TAG, "fireUpdateUserStatus: user status is " + objectvalue);
    }

    //below code for update read entry chat collection
    public static void latestMsgReadEntry(final String chatId,
                                          final String item) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId);
        newCityRef.update("latestMessage.readarray", FieldValue.arrayUnion(item));
    }


    //below code for Read entry message collection

    public static void fireMsgReceivedEntry(final String chatId,
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
    public static void latestMsgReceivedEntry(final String chatId,
                                              final String item) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId);
        newCityRef.update("latestMessage.receivedarray", FieldValue.arrayUnion(item));
    }

    //below code for update user name in map of chat collection
    public static void updateFirstNameConversation(final String chatId,
                                                   final String key,
                                                   final String value) {
        final DocumentReference newCityRef = mDatabase.collection(CHATS)
                .document(chatId);
        newCityRef.update("usersNameMap." + key, value);
    }


    //below code for update user Online Status
    public static void fireUpdateUserStatus(
            FirebaseFirestore mmDatabase, final String id,
            final String fieldKey,
            final Object objectvalue) {
        if (mmDatabase != null) {
            final DocumentReference newCityRef = mmDatabase.collection(USERS_FIRESTORE)
                    .document(id);
            newCityRef.update(
                    fieldKey, objectvalue
            );
        } else {
            final DocumentReference newCityRef = mDatabase.collection(USERS_FIRESTORE)
                    .document(id);
            newCityRef.update(
                    fieldKey, objectvalue
            );
        }

        Log.d(TAG, "fireUpdateUserStatus: user status is " + objectvalue);
    }

    //below code for new User entry in Users in firebase firestore db
    public static void firestoreNewUserEntryUserTable(
            String uid, final String useremail,
            final String usermobile,
            final String userPassword,
            final String user10digitmobile,
            String userFullName,
            String registrationToken
    ) {
        reInstatiateValues();
        timestampMillis = MyUtil.getTimeStampInMillis();
        String[] frstlastname = userFullName.split(" ", 2);
        String userlastnm = "";
        if (frstlastname.length > 1) {
            userlastnm = frstlastname[1];
        }
        final DocumentReference newCityRef = mDatabase.collection(USERS_FIRESTORE)
                .document(uid);
        newCityRef.set(new FirestoreUsersModel(userlastnm, userfirstname, "", "", userFullName, false,
                user10digitmobile, uid, FirebaseUtil.SIGNUP_METHOD_PHONE, timestampMillis, timestampMillis,
                usermobile, useremail, userPassword, user10digitmobile, userfirstname, registrationToken, timestampMillis,
                FirebaseUtil.EXTRAS_ONLINE_STS1, "", ""));
    }


    public static void fbUpdateLoginTable(
            final String myfuid,
            final String fieldname,
            final String fieldValue) {

        DocumentReference usrLikeDoc = mDatabase.collection(USERS_FIRESTORE)
                .document(myfuid);
        usrLikeDoc.update(fieldname, fieldValue);
    }

    private static void uploadCoverImage(final Uri imgUri, final String collectionName, final String documentName, final String imgName) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = mStorageRef
                .child("cdn")
                .child("is")
                .child(collectionName)
                .child(documentName)
                .child(imgName);
        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(imgUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        Log.d(TAG, "uploadFromUri: upload success");
                        // Request the public download URL
                        return photoRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {
                        // Upload succeeded
//                        Log.d(TAG, "uploadFromUri: getDownloadUri success " + downloadUri);
//                        Glide.with(AddDestActivity.this).load(downloadUri).into(coverImgView);
//                        hideCoverProgress();
//                        coverImgUri = downloadUri.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                    }
                });
    }

    private static void uploadIconImage(final Uri imgUri, final String collectionName, final String documentName, final String imgName) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = mStorageRef
                .child("cdn")
                .child("is")
                .child(collectionName)
                .child(documentName)
                .child(imgName);
        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(imgUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        Log.d(TAG, "uploadFromUri: upload success");
                        // Request the public download URL
                        return photoRef.getDownloadUrl();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri downloadUri) {
                        // Upload succeeded
//                        Log.d(TAG, "uploadFromUri: getDownloadUri success " + downloadUri);
//                        iconImgUri = downloadUri.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                    }
                });
    }

    //below code to update cover image and icon image
    public static void firestoreUpdateCoverIconImage(final String collectionName, final String id,
                                                     final String iconImgUri,
                                                     final String coverImgUri) {
        if (iconImgUri != null) {
            if (!iconImgUri.equalsIgnoreCase("")) {
                uploadIconImage(Uri.parse(iconImgUri), collectionName, String.valueOf(id), "iconimg");
            }
        }
        if (coverImgUri != null) {
            if (!coverImgUri.equalsIgnoreCase("")) {
                uploadCoverImage(Uri.parse(coverImgUri), collectionName, String.valueOf(id), "coverimg");
            }
        }
    }
}
