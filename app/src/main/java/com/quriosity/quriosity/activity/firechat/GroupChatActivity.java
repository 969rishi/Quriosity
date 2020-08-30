package com.quriosity.quriosity.activity.firechat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gsconrad.richcontentedittext.RichContentEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.adapter.firechat.GroupPaginateChatAdapter;
import com.quriosity.quriosity.models.firechat.MessageModel;
import com.quriosity.quriosity.utils.CustomTypingEditText;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.quriosity.quriosity.utils.FirebaseUtil.CONVERSATION_ACTORS;
import static com.quriosity.quriosity.utils.FirebaseUtil.CONVERSATION_TITLE;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_STS1;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ONLINE_STS2;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_TYPING_ARR;
import static com.quriosity.quriosity.utils.FirebaseUtil.LIMIT;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_CREATE_GROUP;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_FCM_SERVICES;


public class GroupChatActivity extends AppCompatActivity {
    private static final String TAG = "GroupChatActivity";
    public static final int REQUEST_IMAGE = 100;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private Toolbar chatToolbar;
    private TextView chatUserName;
    private TextView chatUserActiveStatus;
    private CircleImageView chatUserImageView;
    private ImageView send_message, send_image;
    private CustomTypingEditText input_user_message;
    private RecyclerView messageList_ReCyVw;
    private String conversationID = "";
    private String myID = "";
    FirebaseFirestore mdatabase;
    private boolean isScrolling;
    private int currentItems, totalItems, scrollOutItems;
    private LinearLayoutManager layoutManager;
    private DocumentSnapshot lastVisible;
    private List<MessageModel> messageModel;
    private GroupPaginateChatAdapter testAdapter;

    public List<String> conversationActors = new ArrayList<>();
    public Map<String, String> conUsersNameMap = new HashMap<>();
    private String myFirstName;
    private String isItNewGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_chat);
        new FirebaseUtil(this);
        Intent intent = getIntent();
        conversationID = intent.getExtras().getString(EXTRAS_CHATID);

        if (intent.getExtras().getString(Intent.EXTRA_REFERRER_NAME) != null) {
            isItNewGroup = intent.getExtras().getString(Intent.EXTRA_REFERRER_NAME);
        }

        myID = ((WedAmorApplication) getApplicationContext()).getfirebaseUid();
        myFirstName = ((WedAmorApplication) getApplicationContext()).getFirstUserName();
        mdatabase = FirebaseFirestore.getInstance();

        Log.d(TAG, "onCreate: conversationID " + conversationID);
        Log.d(TAG, "onCreate: myID " + myID);
        ((WedAmorApplication) getApplicationContext()).setCurrentActiveChatID(conversationID);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chats);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupChatActivity.this, "This feature will be added soon", Toast.LENGTH_SHORT).show();
            }
        });

        chatUserName = findViewById(R.id.action_bar_title_1);
        chatUserActiveStatus = findViewById(R.id.action_bar_title_2);
        chatUserImageView = findViewById(R.id.conversation_contact_photo);
        send_message = findViewById(R.id.c_send_message_BTN);
        send_image = findViewById(R.id.c_send_image_BTN);
        input_user_message = findViewById(R.id.c_input_message);
        messageList_ReCyVw = findViewById(R.id.message_list);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_user_message.getText().toString().isEmpty() && input_user_message.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(GroupChatActivity.this, "Please enter message to send", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage();
            }
        });

        send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerActivity.clearCache(GroupChatActivity.this);
                checkStoragePermissionStatus();
            }
        });
        FirebaseUtil.updateFirstNameConversation(conversationID, myID, myFirstName.toLowerCase());
        setupLisnterForNameAndIcon();
        setupRichContentEditText();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().subscribeToTopic(conversationID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isItNewGroup.equalsIgnoreCase(EXTRAS_FCM_SERVICES)) {
            //Todo take to dashboard
        }
        super.onBackPressed();
        ((WedAmorApplication) getApplicationContext()).setCurrentActiveChatID("0");
    }

    private void setupLisnterForNameAndIcon() {
        mdatabase.collection(FirebaseUtil.CHATS)
                .document(conversationID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                conversationActors = (List<String>) task.getResult().get(CONVERSATION_ACTORS);
                conversationActors.remove(myID);
                chatUserName.setText(task.getResult().get(CONVERSATION_TITLE).toString());

                conUsersNameMap = (Map<String, String>) task.getResult().get("usersNameMap");

                int itr = 0;
                String headStatus;
                StringBuilder userList = new StringBuilder();
                for (String nameslist : conUsersNameMap.values()) {
                    if (!nameslist.equalsIgnoreCase(myFirstName))
                        userList.insert(0, MyUtil.firstLetterCap(nameslist) + ", ");
                    itr++;
                    if (itr == 5)
                        break;
                }
                if (conUsersNameMap.values().size() <= 5) {
                    headStatus = userList.append("You").toString();
                } else {
                    headStatus = userList.append("You and more...").toString();
                }


                mdatabase.collection(FirebaseUtil.CHATS)
                        .document(conversationID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        List<String> getReadArray = (List<String>) documentSnapshot.get(EXTRAS_TYPING_ARR);
                        if (getReadArray != null) {
                            if (getReadArray.isEmpty()) {
                                chatUserActiveStatus.setText(headStatus);
                            } else {
                                chatUserActiveStatus.setText(MyUtil.firstLetterCap(conUsersNameMap.get(getReadArray.get(0))) + " is typing...");
                            }
                        }
                    }
                });


                setUpRecyclerChatsList();
                if (!isItNewGroup.equalsIgnoreCase(EXTRAS_CREATE_GROUP)) {
                    setupRecyclerScroll();
                }

                Glide.with(GroupChatActivity.this).load(task.getResult().get("conversationIconUrl").toString())
                        .error(Glide.with(GroupChatActivity.this)
                                .load(R.drawable.ic_team_group_white))
                        .into(chatUserImageView);

                findViewById(R.id.conversation_contact_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new StfalconImageViewer.Builder<String>(GroupChatActivity.this, new ArrayList<>(Arrays.asList(task.getResult().get("conversationIconUrl").toString())), new ImageLoader<String>() {
                            @Override
                            public void loadImage(ImageView imageView, String image) {
//                                Glide.with(GroupChatActivity.this).load(image).into(imageView);
                                Glide.with(GroupChatActivity.this).load(image)
                                        .error(Glide.with(GroupChatActivity.this)
                                                .load(R.drawable.ic_team_group_white))
                                        .into(imageView);
                            }
                        }).show();
                    }
                });


            }
        });
    }

    private void setupRichContentEditText() {
        // The following line sets the listener that is called when rich content is received
        input_user_message.setOnRichContentListener(new RichContentEditText.OnRichContentListener() {
            // Called when a keyboard sends rich content
            @Override
            public void onRichContent(Uri contentUri, ClipDescription description) {
                if (description.getMimeTypeCount() > 0) {
                    //upload using uri and entry in db
                    sendMEMESMessage(contentUri);
                }
            }
        });

        input_user_message.setOnTypingModified(new CustomTypingEditText.OnTypingModified() {
            @Override
            public void onIsTypingModified(EditText view, boolean isTyping) {

                if (isTyping) {
                    Log.i(TAG, "onIsTypingModified: User typing started");
                    new AsyncCallerTyping().execute(EXTRAS_ONLINE_STS2);
                } else {
                    Log.i(TAG, "onIsTypingModified: User typing... stopped");
                    new AsyncCallerTyping().execute(EXTRAS_ONLINE_STS1);
                }
            }

        });
    }

    private class AsyncCallerTyping extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument
            // and u can access the parent class' variable url over here
            if (params[0].equalsIgnoreCase(EXTRAS_ONLINE_STS2)) {
//                fireUpdateUserData(myID,
//                        EXTRAS_USER_ONLINE_STATUS,
//                        EXTRAS_ONLINE_STS2);

                FirebaseUtil.fireAddConversation(conversationID, myID);
            } else {
//                fireUpdateUserData(myID,
//                        EXTRAS_USER_ONLINE_STATUS,
//                        EXTRAS_ONLINE_STS1);
                FirebaseUtil.fireRemoveConversation(conversationID, myID);
            }

            return null;
        }
    }

    private void scrollToBottom() {
        messageList_ReCyVw.smoothScrollToPosition(testAdapter.getItemCount());
    }


    private void setUpRecyclerChatsList() {
        Query query = mdatabase.collection(FirebaseUtil.CHATS)
                .document(conversationID).collection("messages")
                .orderBy("senton", Query.Direction.DESCENDING)
                .limit(LIMIT);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!isItNewGroup.equalsIgnoreCase(EXTRAS_CREATE_GROUP)) {
                    lastVisible = value.getDocuments().get(value.size() - 1);
                }
                messageModel = value.toObjects(MessageModel.class);

                testAdapter = new GroupPaginateChatAdapter(GroupChatActivity.this, messageModel, conversationID, myID);
                layoutManager = new LinearLayoutManager(GroupChatActivity.this, RecyclerView.VERTICAL, true);
                messageList_ReCyVw.setLayoutManager(layoutManager);
                messageList_ReCyVw.setAdapter(testAdapter);

                if (isItNewGroup.equalsIgnoreCase(EXTRAS_CREATE_GROUP)) {
                    if (testAdapter.getItemCount() >= 1) {
                        lastVisible = value.getDocuments().get(value.size() - 1);
                        isItNewGroup = "";
                        setupRecyclerScroll();
                    }
                }
            }
        });
    }


    private void setupRecyclerScroll() {
        messageList_ReCyVw.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //paginated
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    Log.d(TAG, "onScrolled: bhai ye last video hoga ");
                    loadMoreOldMsgs();
                }

            }
        });
    }

    private void loadMoreOldMsgs() {
        Query query = mdatabase.collection(FirebaseUtil.CHATS)
                .document(conversationID).collection("messages")
                .orderBy("senton", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(30);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    List<MessageModel> models = queryDocumentSnapshots.toObjects(MessageModel.class);
                    messageModel.addAll(models);
                    testAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupChatActivity.this, "All Messages Loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProfile(Uri uri) {
        Log.d(TAG, "Image cache path: " + uri);
        sendMEMESMessage(uri);
    }

    private void sendMessage() {
        List<String> readarr = new ArrayList<>();
        List<String> deletearr = new ArrayList<>();
        List<String> receivedArr = new ArrayList<>();

        firestoreDMTextEntry(conversationID, input_user_message.getText().toString().trim(), "text",
                myID, MyUtil.getTimeStampInMillis(), "", "", conversationActors, readarr, deletearr, receivedArr);
        input_user_message.setText("");
        input_user_message.requestFocus();
    }

    private void sendMEMESMessage(Uri contentUri) {
        List<String> readarr = new ArrayList<>();
        List<String> deletearr = new ArrayList<>();
        List<String> receivedArr = new ArrayList<>();

        firestoreDMMIMEEntry(conversationID, "", "image",
                myID, MyUtil.getTimeStampInMillis(), "",
                "https://i.pinimg.com/originals/a4/f2/cb/a4f2cb80ff2ae2772e80bf30e9d78d4c.gif",
                conversationActors, readarr, deletearr, contentUri, receivedArr);
        input_user_message.setText("");
        input_user_message.requestFocus();
    }

    public void firestoreDMMIMEEntry(
            final String conversationID,
            final String message,
            final String msgtype,
            final String sentby,
            final String senton,
            final String sentto,
            final String url,
            final List<String> sentarray,
            final List<String> readarray,
            final List<String> deletedarray,
            Uri contentUri,
            final List<String> receivedarray) {
        WriteBatch batchOperation = mdatabase.batch();
        final DocumentReference newCityRef = mdatabase.collection("chats")
                .document(conversationID)
                .collection("messages")
                .document();
        batchOperation.set(newCityRef, new MessageModel(newCityRef.getId(), message, msgtype, sentby, senton, sentto, url, sentarray, readarray, deletedarray, receivedarray));

        final DocumentReference reference = mdatabase.collection("chats")
                .document(conversationID);
        batchOperation.update(reference,
                "latestMessage",
                new MessageModel(newCityRef.getId(), message, msgtype, sentby, senton, sentto, url, sentarray, readarray, deletedarray, receivedarray));
        batchOperation.commit();
        FirebaseUtil.uploadMIMEImage(contentUri, conversationID, newCityRef.getId(), (MyUtil.getTimeStampInMillis() + myID));
    }

    public void firestoreDMTextEntry(
            final String conversationID,
            final String message,
            final String msgtype,
            final String sentby,
            final String senton,
            final String sentto,
            final String url,
            final List<String> sentarray,
            final List<String> readarray,
            final List<String> deletedarray,
            final List<String> receivedarray) {
        WriteBatch batchOperation = mdatabase.batch();
        final DocumentReference newCityRef = mdatabase.collection("chats")
                .document(conversationID)
                .collection("messages")
                .document();
        batchOperation.set(newCityRef, new MessageModel(newCityRef.getId(), message, msgtype, sentby, senton, sentto, url, sentarray, readarray, deletedarray, receivedarray));

        final DocumentReference reference = mdatabase.collection("chats")
                .document(conversationID);
        batchOperation.update(reference,
                "latestMessage",
                new MessageModel(newCityRef.getId(), message, msgtype, sentby, senton, sentto, url, sentarray, readarray, deletedarray, receivedarray));
        batchOperation.commit();
    }

    //Image from phone gallery
    private void checkStoragePermissionStatus() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("TAG", "User agreed to make required Storage settings changes.");
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                // loading profile image from local cache
                loadProfile(uri);
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}