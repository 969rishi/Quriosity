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
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.DMChatActivity;
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


public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.ItemRowHolder> {
    private static final String TAG = "ChatsListAdapter";
    private DMChatActivity context;
    List<MessageModel> messageModel;
    String conversationID, myfID, actor2ID;

    public SingleChatAdapter(DMChatActivity dmChatActivityTest, List<MessageModel> messageModel, String conversationID, String myID, String actor2ID) {
        this.context = dmChatActivityTest;
        this.messageModel = messageModel;
        this.conversationID = conversationID;
        this.actor2ID = actor2ID;
        this.myfID = myID;
    }


    @NonNull
    @Override
    public SingleChatAdapter.ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_message_layout, parent, false);

        return new SingleChatAdapter.ItemRowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SingleChatAdapter.ItemRowHolder holder, int position) {
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
                if (readarray.contains(actor2ID)) {
                    holder.doubleTick_mime.setVisibility(View.VISIBLE);
                    holder.doubleTick_mime.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                    return;
                }

                List<String> receivedArray = messageModel.get(position).getReceivedarray();
                if (receivedArray != null) {
                    if (receivedArray.contains(actor2ID)) {
                        holder.doubleTick_mime.setVisibility(View.VISIBLE);
                        return;
                    }
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

                return;
            }
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
            if (readarray.contains(actor2ID)) {
                holder.doubleTick.setVisibility(View.VISIBLE);
                holder.doubleTick.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                holder.actorMe.setText(messageModel.get(position).getMessage());
                return;
            }

            List<String> receivedArray = messageModel.get(position).getReceivedarray();
            if (receivedArray != null) {
                if (receivedArray.contains(actor2ID)) {
                    holder.doubleTick.setVisibility(View.VISIBLE);
                    holder.actorMe.setText(messageModel.get(position).getMessage());
                    return;
                }
            }


//            if (Long.parseLong(messageModel.get(position).getSenton()) > Long.parseLong(context.actor2OnlineTimeStmp)) {
//                if (context.actor2UserOnlineSts.equalsIgnoreCase(FirebaseUtil.EXTRAS_ONLINE_STS1)) {
//                    holder.doubleTick.setVisibility(View.VISIBLE);
//                    holder.actorMe.setText(messageModel.get(position).getMessage());
//                } else {
//                    holder.singleTick.setVisibility(View.VISIBLE);
//                    holder.actorMe.setText(messageModel.get(position).getMessage());
//                }
//
//            } else {
//                holder.doubleTick.setVisibility(View.VISIBLE);
//                holder.actorMe.setText(messageModel.get(position).getMessage());
//            }
            holder.singleTick.setVisibility(View.VISIBLE);
            holder.actorMe.setText(messageModel.get(position).getMessage());

        } else {
            if (!messageModel.get(position).getMsgtype().equalsIgnoreCase(EXTRAS_MSG_TYPE_TXT)) {
                List<String> deletedarray = messageModel.get(position).getDeletedarray();
                if (deletedarray.contains(actor2ID)) {
                    holder.actorOtherLayout.setVisibility(View.VISIBLE);
                    holder.time_textviewActor2.setVisibility(View.GONE);
                    holder.actorOther.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    holder.actorOther.setPadding(MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context), MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context));
                    return;
                }
                holder.actorOtherLayout.setVisibility(View.GONE);
                holder.actorOtherMimeLayout.setVisibility(View.VISIBLE);
                holder.time_textview_mime_Other.setText(MyUtil.dayAgoForChatting(messageModel.get(position).getSenton()));

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
            if (deletedarray.contains(actor2ID)) {
                holder.time_textviewActor2.setVisibility(View.GONE);
                holder.actorOther.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                holder.actorOther.setPadding(MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context), MyUtil.dpToPx(10, context), MyUtil.dpToPx(8, context));
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

        public ItemRowHolder(View itemView) {
            super(itemView);

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
