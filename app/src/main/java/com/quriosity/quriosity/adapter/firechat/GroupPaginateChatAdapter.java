package com.quriosity.quriosity.adapter.firechat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.GroupChatActivity;
import com.quriosity.quriosity.models.firechat.MessageModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.quriosity.quriosity.utils.FirebaseUtil.EXTRAS_MSG_TYPE_TXT;


public class GroupPaginateChatAdapter extends RecyclerView.Adapter<GroupPaginateChatAdapter.ItemRowHolder> {
    private static final String TAG = "GroupPaginateChatAdapter";
    private GroupChatActivity context;
    List<MessageModel> messageModel;
    String conversationID, myfID;
    FirebaseFirestore mdatabase;

    public GroupPaginateChatAdapter(GroupChatActivity dmChatActivityTest, List<MessageModel> messageModel, String conversationID, String myID) {
        this.context = dmChatActivityTest;
        this.messageModel = messageModel;
        this.conversationID = conversationID;
        this.myfID = myID;
        mdatabase = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public GroupPaginateChatAdapter.ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_message_layout, parent, false);

        return new GroupPaginateChatAdapter.ItemRowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupPaginateChatAdapter.ItemRowHolder holder, int position) {
        if (messageModel.get(position).getSentby().equalsIgnoreCase(myfID)) {
            if (!messageModel.get(position).getMsgtype().equalsIgnoreCase(EXTRAS_MSG_TYPE_TXT)) {
                List<String> deletedarray = messageModel.get(position).getDeletedarray();
                if (deletedarray.contains(myfID)) {
                    holder.actorMeLayout.setVisibility(View.VISIBLE);
                    holder.time_textview.setVisibility(View.GONE);
                    holder.actorMe.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    return;
                }
                holder.actorMeLayout.setVisibility(View.GONE);
                holder.actorMeMimeLayout.setVisibility(View.VISIBLE);
                holder.time_textview_mime.setText(MyUtil.dayAgoForChatting(messageModel.get(position).getSenton()));
                Glide.with(context)
                        .load(messageModel.get(position).getUrl().trim())
                        .placeholder(holder.circularProgressDrawable)
                        .error(Glide.with(context)
                                .load(R.drawable.ic_error_chat))
                        .into(holder.actorMeMime);
                holder.actorMeMime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StfalconImageViewer.Builder<String>(context, new ArrayList<>(Arrays.asList(messageModel.get(position).getUrl().trim())),
                                new ImageLoader<String>() {
                            @Override
                            public void loadImage(ImageView imageView, String image) {
                                Glide.with(context).load(image).into(imageView);
                            }
                        })
                                .withTransitionFrom(holder.actorMeMime)
                                .show();
                    }
                });

                List<String> readarray = messageModel.get(position).getReadarray();
                if (readarray.size() == context.conversationActors.size()) {
                    holder.doubleTick_mime.setVisibility(View.VISIBLE);
                    holder.doubleTick_mime.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                    return;
                }

                List<String> receivedArray = messageModel.get(position).getReceivedarray();
                if (receivedArray.size() == context.conversationActors.size()) {
                    holder.doubleTick_mime.setVisibility(View.VISIBLE);
                    return;
                }
                holder.singleTick_mime.setVisibility(View.VISIBLE);

//                if (Long.parseLong(messageModel.get(position).getSenton()) > Long.parseLong(context.actor2OnlineTimeStmp)) {
//                    if (context.actor2UserOnlineSts.equalsIgnoreCase(FirebaseUtil.EXTRAS_ONLINE_STS1)) {
//                        holder.doubleTick_mime.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.singleTick_mime.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//                    holder.doubleTick.setVisibility(View.VISIBLE);
//                }

            } else {
                holder.actorMeMimeLayout.setVisibility(View.GONE);
                holder.actorMeLayout.setVisibility(View.VISIBLE);
                holder.time_textview.setText(MyUtil.dayAgoForChatting(messageModel.get(position).getSenton()));
                List<String> deletedarray = messageModel.get(position).getDeletedarray();
                if (!deletedarray.isEmpty()) {
                    holder.time_textview.setVisibility(View.GONE);
                    holder.actorMe.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    return;
                }

                List<String> readarray = messageModel.get(position).getReadarray();
                if (readarray.size() == context.conversationActors.size()) {
                    holder.doubleTick.setVisibility(View.VISIBLE);
                    holder.doubleTick.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                    holder.actorMe.setText(messageModel.get(position).getMessage());
                    return;
                }

                List<String> receivedArray = messageModel.get(position).getReceivedarray();
                if (receivedArray.size() == context.conversationActors.size()) {
                    holder.doubleTick.setVisibility(View.VISIBLE);
                    holder.actorMe.setText(messageModel.get(position).getMessage());
                    return;
                }

                holder.singleTick.setVisibility(View.VISIBLE);
                holder.actorMe.setText(messageModel.get(position).getMessage());
            }

        } else {
            if (!messageModel.get(position).getMsgtype().equalsIgnoreCase(EXTRAS_MSG_TYPE_TXT)) {
                List<String> deletedarray = messageModel.get(position).getDeletedarray();
                if (deletedarray.contains(messageModel.get(position).getSentby())) {
                    holder.actorOtherLayout.setVisibility(View.VISIBLE);
                    holder.time_textviewActor2.setVisibility(View.GONE);
                    holder.actorOther.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    holder.actorOther.setPadding(MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context), MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context));
                    holder.actorOther1002.setText(MyUtil.firstLetterCap(context.conUsersNameMap.get(messageModel.get(position).getSentby())));

                    return;
                }
                holder.actorOtherLayout.setVisibility(View.GONE);
                holder.actorOtherMimeLayout.setVisibility(View.VISIBLE);
                holder.time_textview_mime_Other.setText(MyUtil.dayAgoForChatting(messageModel.get(position).getSenton()));
                holder.actorOtherMime10022.setText(MyUtil.firstLetterCap(context.conUsersNameMap.get(messageModel.get(position).getSentby())));
                List<String> getReadArray = messageModel.get(position).getReadarray();
                if (!getReadArray.contains(myfID)) {
                    FirebaseUtil.fireMsgReadEntry(conversationID, messageModel.get(position).getMid(), myfID);
                    FirebaseUtil.latestMsgReadEntry(conversationID, myfID);
                }

                List<String> getReceivedArray = messageModel.get(position).getReceivedarray();
                if (getReceivedArray != null) {
                    if (!getReceivedArray.contains(myfID)) {
                        FirebaseUtil.fireMsgReceivedEntry(conversationID, messageModel.get(position).getMid(), myfID);
                        FirebaseUtil.latestMsgReceivedEntry(conversationID, myfID);
                    }
                }


                Glide.with(context)
                        .load(messageModel.get(position).getUrl().trim())
                        .placeholder(holder.circularProgressDrawable)
                        .error(Glide.with(context)
                                .load(R.drawable.ic_error_chat))
                        .into(holder.actorOtherMime);
                holder.actorOtherMime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new StfalconImageViewer.Builder<String>(context, new ArrayList<>(Arrays.asList(messageModel.get(position).getUrl().trim())), new ImageLoader<String>() {
                            @Override
                            public void loadImage(ImageView imageView, String image) {
                                Glide.with(context).load(image).into(imageView);
                            }
                        }).withTransitionFrom(holder.actorOtherMime)
                                .show();
                    }
                });

                return;
            }

            holder.actorOtherLayout.setVisibility(View.VISIBLE);
            List<String> deletedarray = messageModel.get(position).getDeletedarray();
            if (deletedarray.contains(messageModel.get(position).getSentby())) {
                holder.time_textviewActor2.setVisibility(View.GONE);
                holder.actorOther.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                holder.actorOther.setPadding(MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context), MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context));
                holder.actorOther1002.setText(MyUtil.firstLetterCap(context.conUsersNameMap.get(messageModel.get(position).getSentby())));
                return;
            }

            List<String> getReadArray = messageModel.get(position).getReadarray();
            if (!getReadArray.contains(myfID)) {
                FirebaseUtil.fireMsgReadEntry(conversationID, messageModel.get(position).getMid(), myfID);
                FirebaseUtil.latestMsgReadEntry(conversationID, myfID);
            }

            List<String> getReceivedArray = messageModel.get(position).getReceivedarray();
            if (getReceivedArray != null) {
                if (!getReceivedArray.contains(myfID)) {
                    FirebaseUtil.fireMsgReceivedEntry(conversationID, messageModel.get(position).getMid(), myfID);
                    FirebaseUtil.latestMsgReceivedEntry(conversationID, myfID);
                }
            }

            holder.actorOther1002.setText(MyUtil.firstLetterCap(context.conUsersNameMap.get(messageModel.get(position).getSentby())));
            holder.actorOther.setText(messageModel.get(position).getMessage());
            holder.time_textviewActor2.setText(MyUtil.dayAgoForChatting(messageModel.get(position).getSenton()));
        }


        holder.actorMeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", messageModel.get(position).getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text Copied.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.actorOtherLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", messageModel.get(position).getMessage());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text Copied.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


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
        return messageModel.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView actorOther, actorMe, time_textview, time_textview_mime, time_textview_mime_Other, time_textviewActor2;
        View actorMeLayout, actorMeMimeLayout, actorOtherMimeLayout;
        RelativeLayout actorOtherLayout;
        ImageView singleTick, singleTick_mime, doubleTick_mime, doubleTick, actorMeMime, actorOtherMime;
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        TextView actorOther1002, actorOtherMime10022;

        public ItemRowHolder(View itemView) {
            super(itemView);

            actorOther1002 = itemView.findViewById(R.id.other_user_name1002);
            actorOtherMime10022 = itemView.findViewById(R.id.other_user_name10022);
            actorOther = itemView.findViewById(R.id.actor_other_textview1002);
            actorMe = itemView.findViewById(R.id.actor_me_textview1001);
            actorMeMime = itemView.findViewById(R.id.actor_me_imageview1001);
            actorOtherMime = itemView.findViewById(R.id.actor_other_imageview10022);
            actorMeLayout = itemView.findViewById(R.id.single_line_msgbox_layout);
            actorOtherMimeLayout = itemView.findViewById(R.id.actor_other_mimebox_layout);
            actorMeMimeLayout = itemView.findViewById(R.id.single_line_mimebox_layout);
            actorOtherLayout = itemView.findViewById(R.id.actor_other_msgbox_layout);
            time_textview_mime = itemView.findViewById(R.id.time_textview10011);
            time_textview_mime_Other = itemView.findViewById(R.id.time_textview10022);
            time_textview = itemView.findViewById(R.id.time_textview1001);
            time_textviewActor2 = itemView.findViewById(R.id.time_textview1002);
            singleTick_mime = itemView.findViewById(R.id.single_tick_imgview10011);
            singleTick = itemView.findViewById(R.id.single_tick_imgview);
            doubleTick_mime = itemView.findViewById(R.id.double_tick_imgview10011);
            doubleTick = itemView.findViewById(R.id.double_tick_imgview);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            int highlightColor = context.getColor(R.color.colorPrimaryDark);
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
            circularProgressDrawable.setColorFilter(colorFilter);
            circularProgressDrawable.start();
        }
    }
}
