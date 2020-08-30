package com.quriosity.quriosity.activity.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.adapter.firechat.UsersListAdapter;
import com.quriosity.quriosity.models.FirestoreUsersModel;
import com.quriosity.quriosity.models.firechat.ConversationModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quriosity.quriosity.utils.FirebaseUtil.CHATS;
import static com.quriosity.quriosity.utils.FirebaseUtil.CONVERSATION_TYPE_GROUP;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_CREATE_GROUP;

public class CreateGroupActivity extends AppCompatActivity {
    private static final String TAG = "CreateGroupActivity";
    private ProgressBar progressbar;
    private LinearLayout mainLayout, noDataFound;
    private RecyclerView recyclerview;
    private UsersListAdapter chatsListAdapter;
    private String myFirebaseID;
    private Toolbar toolbar;
    FirebaseFirestore rootRef;
    public ArrayList<FirestoreUsersModel> selectedPerson = new ArrayList<>();
    private String myFirstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        myFirebaseID = ((WedAmorApplication) getApplicationContext()).getfirebaseUid();
        myFirstName = ((WedAmorApplication) getApplicationContext()).getFirstUserName();
        progressbar = findViewById(R.id.progress_bar);
        noDataFound = findViewById(R.id.no_data_found_layout);
        mainLayout = findViewById(R.id.wholedata_home_fragment_layout);
        recyclerview = findViewById(R.id.recycler_destination_home_fragment_layout);
        toolbar = findViewById(R.id.toolbar_create_group);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showProgress();
        rootRef = FirebaseFirestore.getInstance();
        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
    }


    public void refreshMenuItems() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_group, menu);
        if (selectedPerson.size() > 0) {
            menu.getItem(0).setVisible(true);
            toolbar.setTitle(selectedPerson.size() + " People");
        } else {
            menu.getItem(0).setVisible(false);
            toolbar.setTitle("Contacts");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group_action: {
                // do your sign-out stuff
                showDialogAddReview();
//                Toast.makeText(this, "" + ((WedAmorApplication) getApplicationContext()).getFirstUserName()
//                        + "\n" + ((WedAmorApplication) getApplicationContext()).getUserProfileImg(), Toast.LENGTH_SHORT).show();
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return true;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (chatsListAdapter != null) {
            chatsListAdapter.stopListening();
            selectedPerson.clear();
            refreshMenuItems();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress();
        setUpRecyclerChatsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatsListAdapter != null) {
            chatsListAdapter.startListening();
        }
    }

    private void setUpRecyclerChatsList() {
        FirebaseFirestore fireob = FirebaseFirestore.getInstance();
        Query query = fireob.collection(FirebaseUtil.USERS_FIRESTORE);
        FirestoreRecyclerOptions<FirestoreUsersModel> options = new FirestoreRecyclerOptions.Builder<FirestoreUsersModel>()
                .setQuery(query, FirestoreUsersModel.class)
                .build();
        chatsListAdapter = new UsersListAdapter(options, CreateGroupActivity.this, myFirebaseID, CreateGroupActivity.this);
        recyclerview.setLayoutManager(new LinearLayoutManager(CreateGroupActivity.this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(chatsListAdapter);
        recyclerview.setHasFixedSize(false);
        hideProgress();
    }

    private void showDialogAddReview() {
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(CreateGroupActivity.this);
        View mView = View.inflate(CreateGroupActivity.this, R.layout.add_review_vendor_detail_layout, null);
        mBuilder.setCancelable(true);
        final Button submitBtn, cancelBtn;
        final EditText editText;

        editText = mView.findViewById(R.id.edittext_group_name);
        submitBtn = mView.findViewById(R.id.ok_button);
        cancelBtn = mView.findViewById(R.id.cancel_button);

        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        cancelBtn.setOnClickListener(view -> dialog.cancel());
        submitBtn.setOnClickListener(view -> {
            if (editText.getText().toString().isEmpty()) {
                editText.setError("Field cannot be empty");
                editText.requestFocus();
            } else {
                dialog.cancel();
                showProgress();
                createNewCoversation(selectedPerson, editText.getText().toString().trim());
            }

        });
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.GONE);
    }

    private void showNoData() {
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        noDataFound.setVisibility(View.GONE);
    }


    private void createNewCoversation(ArrayList<FirestoreUsersModel> model, String groupName) {
        List<String> typingarr = new ArrayList<>();
        List<String> ca = new ArrayList<>();
        Map<String, String> userNameMap = new HashMap<>();

//        for (int i = 0; i < model.size(); i++) {
//            userNameMap.put(i + "", model.get(i).getFirstname());
//            ca.add(model.get(i).getFirebaseuid());
//        }

        for (FirestoreUsersModel firestModel : model) {
            ca.add(firestModel.getFirebaseuid());
            userNameMap.put(firestModel.getFirebaseuid(), firestModel.getFirstname());

        }
        userNameMap.put(myFirebaseID, myFirstName);
        ca.add(myFirebaseID);

        fireDMConversationtEntry(CONVERSATION_TYPE_GROUP, myFirebaseID, MyUtil.getTimeStampInMillis(), ca, typingarr, groupName, userNameMap);
    }

    private void fireDMConversationtEntry(
            final String msgtype,
            final String createdby,
            final String createdOn,
            final List<String> converArray,
            final List<String> typingarr,
            String groupName,
            final Map<String, String> userNameMap) {
        final DocumentReference newCityRef = rootRef.collection(CHATS).document();
        newCityRef.set(new ConversationModel(newCityRef.getId(), groupName, converArray, createdby, createdOn, msgtype, typingarr, userNameMap, "")).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createTopic(newCityRef.getId());
                    }
                });
    }

    private void createTopic(String id) {
        FirebaseMessaging.getInstance().subscribeToTopic(id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        startActivity(new Intent(CreateGroupActivity.this, GroupChatActivity.class)
                                .putExtra(EXTRAS_CHATID, id)
                                .putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_CREATE_GROUP)
                        );
                        finish();
//                        hideProgress();
                    }
                });
    }
}