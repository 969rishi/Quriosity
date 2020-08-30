package com.quriosity.quriosity.adapter.firechat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.DMChatActivity;
import com.quriosity.quriosity.fragments.firechat.ContactsFrag;
import com.quriosity.quriosity.models.FirestoreUsersModel;
import com.quriosity.quriosity.models.firechat.ConversationModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.quriosity.quriosity.utils.FirebaseUtil.CHATS;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ACTOR2;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MEACTOR1;
import static com.quriosity.quriosity.utils.MyUtil.EXTRAS_CONTACTS_FRAG;


public class FriendsListAdapter extends FirestoreRecyclerAdapter<FirestoreUsersModel, FriendsListAdapter.CommentsHolder> {
    private static final String TAG = "FriendListAdapter";
    private Context context;
    private FirestoreRecyclerOptions<FirestoreUsersModel> modelObject;
    private String myFirebaseID;
    private String myFireFirstName;
    FirebaseFirestore rootRef;
    private ContactsFrag contactsFrag;

    public FriendsListAdapter(@NonNull FirestoreRecyclerOptions<FirestoreUsersModel> options, Context context, String myFirebaseID, ContactsFrag contactsFrag, String myFireFirstName) {
        super(options);
        this.context = context;
        this.modelObject = options;
        this.myFirebaseID = myFirebaseID;
        this.myFireFirstName = myFireFirstName;
        this.contactsFrag = contactsFrag;
        rootRef = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentsHolder holder, final int position, @NonNull final FirestoreUsersModel model) {
        if (model.getFirebaseuid().equalsIgnoreCase(myFirebaseID)) {
            return;
        }

        rootRef.collection(FirebaseUtil.USERS_FIRESTORE)
                .document(model.getFirebaseuid())
                .get().addOnCompleteListener(task -> {
            holder.userName.setText(MyUtil.firstLetterCap(task.getResult().get("firstname").toString().trim()));
            String userIconPath = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + model.getFirebaseuid() + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
            Glide.with(context).load(userIconPath)
                    .error(Glide.with(context)
                            .load(R.drawable.ic_user_nav3))
                    .into(holder.user_photo);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkMethod1(model, task.getResult().get("firstname").toString().trim());
                    contactsFrag.showProgress();
                }
            });

        });
        if (model.getFirebaseuid() != null) {
            holder.linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void checkMethod1(FirestoreUsersModel model, String firstname) {
        rootRef.collection(CHATS)
                .whereEqualTo("type", "DM")
                .whereEqualTo("createdby", myFirebaseID)
                .whereArrayContains("conversationActors", model.getFirebaseuid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().getDocuments().size() > 0) {
                    context.startActivity(new Intent(context, DMChatActivity.class)
                            .putExtra(EXTRAS_CHATID, task.getResult().getDocuments().get(0).getId())
                            .putExtra(EXTRAS_MEACTOR1, myFirebaseID)
                            .putExtra(EXTRAS_ACTOR2, model.getFirebaseuid())
                            .putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_CONTACTS_FRAG)
                    );
                    contactsFrag.hideProgress();
                } else {
                    checkMethod2(model, firstname);
                }
            }
        });
    }

    private void checkMethod2(FirestoreUsersModel model, String firstname) {
        rootRef.collection(CHATS)
                .whereEqualTo("type", "DM")
                .whereEqualTo("createdby", model.getFirebaseuid())
                .whereArrayContains("conversationActors", myFirebaseID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().getDocuments().size() > 0) {
                    context.startActivity(new Intent(context, DMChatActivity.class)
                            .putExtra(EXTRAS_CHATID, task.getResult().getDocuments().get(0).getId())
                            .putExtra(EXTRAS_MEACTOR1, myFirebaseID)
                            .putExtra(EXTRAS_ACTOR2, model.getFirebaseuid())
                            .putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_CONTACTS_FRAG)
                    );
                    contactsFrag.hideProgress();
                } else {
                    Log.d(TAG, "onComplete: line no 119");
                    createNewCoversation(model, firstname);
                }
            }
        });

    }

    private void createNewCoversation(FirestoreUsersModel model, String firstname) {
        Map<String, String> userNameMap = new HashMap<>();
        userNameMap.put(model.getFirebaseuid(), firstname);
        userNameMap.put(myFirebaseID, myFireFirstName);

        List<String> typingarr = new ArrayList<>();
        List<String> ca = new ArrayList<>();
        ca.add(myFirebaseID);
        ca.add(model.getFirebaseuid());

        fireDMConversationtEntry("DM", myFirebaseID, MyUtil.getTimeStampInMillis(), ca, model, typingarr, userNameMap, "");
    }

    private void fireDMConversationtEntry(
            final String msgtype,
            final String createdby,
            final String createdOn,
            final List<String> converArray,
            FirestoreUsersModel model,
            final List<String> typingarr,
            final Map<String, String> userNameMap,
            final String conversationIcon) {
        final DocumentReference newCityRef = rootRef.collection(CHATS).document();
        newCityRef.set(new ConversationModel(newCityRef.getId(), "", converArray, createdby, createdOn, msgtype, typingarr, userNameMap, conversationIcon))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                context.startActivity(new Intent(context, DMChatActivity.class)
                        .putExtra(EXTRAS_CHATID, newCityRef.getId())
                        .putExtra(EXTRAS_MEACTOR1, myFirebaseID)
                        .putExtra(EXTRAS_ACTOR2, model.getFirebaseuid())
                        .putExtra(Intent.EXTRA_REFERRER_NAME, EXTRAS_CONTACTS_FRAG)
                );
                contactsFrag.hideProgress();
            }
        });
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.firends_list_adapter,
                parent, false);
        return new CommentsHolder(v);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView user_photo;
        LinearLayout linearLayout;

        CommentsHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.all_user_name);
            user_photo = itemView.findViewById(R.id.all_user_profile_img);
            linearLayout = itemView.findViewById(R.id.linearlayout_friend_adptr);
        }

    }
}
