package com.quriosity.quriosity.adapter.firechat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.DMChatActivity;
import com.quriosity.quriosity.models.firechat.ConversationModel;
import com.quriosity.quriosity.utils.MyUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_ACTOR2;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MEACTOR1;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MSG_TYPE_IMG;


public class ChatsListAdapter extends FirestoreRecyclerAdapter<ConversationModel, ChatsListAdapter.CommentsHolder> {
    private static final String TAG = "ChatsListAdapter";
    private Context context;
    private FirestoreRecyclerOptions<ConversationModel> modelObject;
    private String myFirebaseID;
    private String otherActorID = "";

    public ChatsListAdapter(@NonNull FirestoreRecyclerOptions<ConversationModel> options, Context context, String myFirebaseID) {
        super(options);
        this.context = context;
        this.modelObject = options;
        this.myFirebaseID = myFirebaseID;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentsHolder holder, final int position, @NonNull final ConversationModel model) {
        holder.msg_time.setText(MyUtil.dayAgoForChat(model.getLatestMessage().getSenton()));
        List<String> ca = model.getConversationActors();
        if (ca.get(0).equalsIgnoreCase(myFirebaseID)) {
            otherActorID = ca.get(1);
        } else {
            otherActorID = ca.get(0);
        }
        Log.d(TAG, "onBindViewHolder: OTHER Actor2 ID " + otherActorID);


        holder.userName.setText(MyUtil.firstLetterCap(model.getUsersNameMap().get(otherActorID)));
        String userIconPath = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + otherActorID + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Log.d(TAG, "onComplete: users Icon: " + userIconPath);
        Glide.with(context).load(userIconPath)
                .error(Glide.with(context)
                        .load(R.drawable.ic_user_nav3))
                .into(holder.user_photo);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myFirebaseID.equalsIgnoreCase(model.getLatestMessage().getSentby())) {
                    context.startActivity(new Intent(context, DMChatActivity.class)
                            .putExtra(EXTRAS_CHATID, modelObject.getSnapshots().getSnapshot(position).getId())
                            .putExtra(EXTRAS_MEACTOR1, myFirebaseID)
                            .putExtra(EXTRAS_ACTOR2, model.getLatestMessage().getSentto())
                            .putExtra(Intent.EXTRA_REFERRER_NAME, TAG)

                    );
                } else {
                    context.startActivity(new Intent(context, DMChatActivity.class)
                            .putExtra(EXTRAS_CHATID, modelObject.getSnapshots().getSnapshot(position).getId())
                            .putExtra(EXTRAS_MEACTOR1, myFirebaseID)
                            .putExtra(EXTRAS_ACTOR2, model.getLatestMessage().getSentby())
                            .putExtra(Intent.EXTRA_REFERRER_NAME, TAG)
                    );
                }
            }
        });

        if (model.getTypingarray().contains(otherActorID)) {
            holder.msgTextview.setText("Typing...");
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.ITALIC);
            holder.msgTextview.setTextColor(context.getColor(R.color.colorPrimary));
            holder.msg_time.setVisibility(View.GONE);
            holder.tickTextview.setVisibility(View.GONE);
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return;
        } else {
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.NORMAL);
            holder.msgTextview.setTextColor(context.getColor(R.color.grey_60));
            holder.msg_time.setVisibility(View.VISIBLE);
        }

        if (!model.getLatestMessage().getDeletedarray().isEmpty()) {
            holder.msgTextview.setText(R.string.deleted_msg_txt);
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.ITALIC);
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return;
        } else {
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.NORMAL);
        }


        if (model.getLatestMessage().getMsgtype().equalsIgnoreCase(EXTRAS_MSG_TYPE_IMG)) {
            holder.msgTextview.setText(" Photo");
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_photo, 0, 0, 0);
            return;
        } else {
            holder.msgTextview.setText(model.getLatestMessage().getMessage());
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (model.getLatestMessage().getSentby().equalsIgnoreCase(myFirebaseID)) {
            holder.msgTextview.setText(model.getLatestMessage().getMessage());
            List<String> deletedarray = model.getLatestMessage().getDeletedarray();
            if (deletedarray.contains(model.getLatestMessage().getSentto())) {
                holder.tickTextview.setVisibility(View.GONE);
//                holder.tickTextview.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                holder.msgTextview.setText(R.string.deleted_msg_txt);
                holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.ITALIC);
                holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                return;
            }

            List<String> readarray = model.getLatestMessage().getReadarray();
            if (readarray.contains(model.getLatestMessage().getSentto())) {
                holder.tickTextview.setVisibility(View.VISIBLE);
                holder.tickTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_doublecheck_small, 0, 0, 0);
                holder.tickTextview.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                return;
            }

            List<String> receivedArray = model.getLatestMessage().getReceivedarray();
            if (receivedArray.contains(model.getLatestMessage().getSentto())) {
                holder.tickTextview.setVisibility(View.VISIBLE);
                holder.tickTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_doublecheck_small, 0, 0, 0);
//                holder.tickTextview.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                return;
            }

            holder.tickTextview.setVisibility(View.VISIBLE);

        }

        if (!model.getLatestMessage().getSentby().equalsIgnoreCase(myFirebaseID)) {
            List<String> readarray = model.getLatestMessage().getReadarray();
            if (!readarray.contains(myFirebaseID)) {
                holder.msgTextview.setTypeface(Typeface.DEFAULT_BOLD);
                holder.msg_time.setTextColor(context.getColorStateList(R.color.colorPrimary));
                holder.msgTextview.setTextColor(context.getColorStateList(R.color.black));
            }
        }
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_chats_adapter,
                parent, false);
        return new CommentsHolder(v);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        TextView userName, msgTextview, msg_time, tickTextview;
        CircleImageView user_photo;
        LinearLayout linearLayout;

        CommentsHolder(@NonNull View itemView) {
            super(itemView);
            msgTextview = itemView.findViewById(R.id.message_textview);
            userName = itemView.findViewById(R.id.all_user_name);
            user_photo = itemView.findViewById(R.id.all_user_profile_img);
            msg_time = itemView.findViewById(R.id.msg_time);
            linearLayout = itemView.findViewById(R.id.linearlayout_chat_adptr);
            tickTextview = itemView.findViewById(R.id.tick_sts_txtview);
        }

    }
}
