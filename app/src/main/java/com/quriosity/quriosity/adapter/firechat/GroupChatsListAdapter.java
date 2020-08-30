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
import com.quriosity.quriosity.activity.firechat.GroupChatActivity;
import com.quriosity.quriosity.models.firechat.ConversationModel;
import com.quriosity.quriosity.utils.MyUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_CHATID;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MSG_TYPE_IMG;


public class GroupChatsListAdapter extends FirestoreRecyclerAdapter<ConversationModel, GroupChatsListAdapter.CommentsHolder> {
    private static final String TAG = "GroupChatsLstAdptr";
    private Context context;
    private FirestoreRecyclerOptions<ConversationModel> modelObject;
    private String myFirebaseID;
    private String otherActorID;


    public GroupChatsListAdapter(@NonNull FirestoreRecyclerOptions<ConversationModel> options, Context context, String myFirebaseID) {
        super(options);
        this.context = context;
        this.modelObject = options;
        this.myFirebaseID = myFirebaseID;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CommentsHolder holder, final int position, @NonNull final ConversationModel model) {
        holder.msg_time.setText(MyUtil.dayAgoForChat(model.getLatestMessage().getSenton()));
        otherActorID = model.getLatestMessage().getSentby();
        Log.d(TAG, "onBindViewHolder: OTHER Actor2 ID " + otherActorID);


        holder.userName.setText(model.getConversationTitle());
        Glide.with(context).load(model.getConversationIconUrl())
                .error(Glide.with(context)
                        .load(R.drawable.ic_team_group))
                .into(holder.user_photo);

        holder.linearLayout.setOnClickListener(v ->
                context.startActivity(new Intent(context, GroupChatActivity.class)
                        .putExtra(EXTRAS_CHATID, modelObject.getSnapshots().getSnapshot(position).getId())
                        .putExtra(Intent.EXTRA_REFERRER_NAME, TAG)
                ));

        if (!model.getTypingarray().isEmpty()) {
            holder.msgTextview.setText(model.getUsersNameMap().get(model.getTypingarray().get(0)) + " is Typing...");
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
            holder.tickTextview.setVisibility(View.GONE);
            holder.msgTextview.setText(R.string.deleted_msg_txt);
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.ITALIC);
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return;
        } else {
            holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.NORMAL);
        }


        if (model.getLatestMessage().getMsgtype().equalsIgnoreCase(EXTRAS_MSG_TYPE_IMG)) {
            if (otherActorID.equalsIgnoreCase(myFirebaseID)) {
                holder.msgTextview.setText(" Photo sent by you");
            } else
                holder.msgTextview.setText(" Photo sent by " + model.getUsersNameMap().get(otherActorID));
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_photo, 0, 0, 0);
        } else {
            if (otherActorID.equalsIgnoreCase(myFirebaseID)) {
                holder.msgTextview.setText("You" + ": " + model.getLatestMessage().getMessage());
            } else
                holder.msgTextview.setText(model.getUsersNameMap().get(otherActorID) + ": " + model.getLatestMessage().getMessage());
            holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (model.getLatestMessage().getSentby().equalsIgnoreCase(myFirebaseID)) {
            holder.msgTextview.setText(model.getLatestMessage().getMessage());

            List<String> deletedarray = model.getLatestMessage().getDeletedarray();
            if (deletedarray.contains(model.getLatestMessage().getSentto())) {
                holder.tickTextview.setVisibility(View.GONE);
                holder.msgTextview.setText(R.string.deleted_msg_txt);
                holder.msgTextview.setTypeface(holder.msgTextview.getTypeface(), Typeface.ITALIC);
                holder.msgTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                return;
            }

            List<String> readarray = model.getLatestMessage().getReadarray();
            if (readarray.size() >= model.getConversationActors().size()) {
                holder.tickTextview.setVisibility(View.VISIBLE);
                holder.tickTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_doublecheck_small, 0, 0, 0);
                holder.tickTextview.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                return;
            }

            List<String> receivedArray = model.getLatestMessage().getReceivedarray();
            if (receivedArray.size() >= model.getConversationActors().size()) {
                holder.tickTextview.setVisibility(View.VISIBLE);
                holder.tickTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_doublecheck_small, 0, 0, 0);
                holder.tickTextview.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.grey_60));
                return;
            }

            holder.tickTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_singlecheck_small, 0, 0, 0);
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
