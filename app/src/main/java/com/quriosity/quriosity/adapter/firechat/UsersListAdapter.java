package com.quriosity.quriosity.adapter.firechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.CreateGroupActivity;
import com.quriosity.quriosity.models.FirestoreUsersModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersListAdapter extends FirestoreRecyclerAdapter<FirestoreUsersModel, UsersListAdapter.CommentsHolder> {
    private static final String TAG = "FriendListAdapter";
    private Context context;
    private FirestoreRecyclerOptions<FirestoreUsersModel> modelObject;
    private String myFirebaseID;
    FirebaseFirestore rootRef;
    private CreateGroupActivity activity;

    public UsersListAdapter(@NonNull FirestoreRecyclerOptions<FirestoreUsersModel> options, Context context, String myFirebaseID, CreateGroupActivity activity) {
        super(options);
        this.context = context;
        this.modelObject = options;
        this.myFirebaseID = myFirebaseID;
        this.activity = activity;
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

            holder.linearLayout.setOnClickListener(v -> {
                if (activity.selectedPerson.contains(model)) {
                    activity.selectedPerson.remove(model);
                    activity.refreshMenuItems();
                    holder.checkBox.setChecked(false);
                } else {
                    activity.selectedPerson.add(model);
                    holder.checkBox.setChecked(true);
                    activity.refreshMenuItems();
                }

            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity.selectedPerson.contains(model)) {
                        activity.selectedPerson.remove(model);
                        activity.refreshMenuItems();
                        holder.checkBox.setChecked(false);
                    } else {
                        activity.selectedPerson.add(model);
                        holder.checkBox.setChecked(true);
                        activity.refreshMenuItems();
                    }
                }
            });

        });
        if (model.getFirebaseuid() != null) {
            holder.linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_adapter,
                parent, false);
        return new CommentsHolder(v);
    }


    //below code to create unique item in recyclerView
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CheckBox checkBox;
        CircleImageView user_photo;
        LinearLayout linearLayout;

        CommentsHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_create_group);
            userName = itemView.findViewById(R.id.all_user_name);
            user_photo = itemView.findViewById(R.id.all_user_profile_img);
            linearLayout = itemView.findViewById(R.id.linearlayout_friend_adptr);
        }

    }
}
