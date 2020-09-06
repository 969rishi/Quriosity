package com.quriosity.quriosity.adapter.firechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.models.firechat.FirestoreCategoryModel;


public class CategoryAdapter extends FirestoreRecyclerAdapter<FirestoreCategoryModel, CategoryAdapter.CategoryHolder> {
    private static final String TAG = "ChatsListAdapter";
    private Context context;
    private FirestoreRecyclerOptions<FirestoreCategoryModel> modelObject;
    private String myFirebaseID;
    private String otherActorID = "";

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<FirestoreCategoryModel> options, Context context, String myFirebaseID) {
        super(options);
        this.context = context;
        this.modelObject = options;
        this.myFirebaseID = myFirebaseID;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CategoryHolder holder, final int position,
                                    @NonNull final FirestoreCategoryModel model) {

        /*String userIconPath = MyUtil.FIREBASE_STORAGE_BASE_URL + "users%2F" + otherActorID + "%2F" + "iconimg" + MyUtil.FIREBASE_STORAGE_END_URL;
        Log.d(TAG, "onComplete: users Icon: " + userIconPath);
        Glide.with(context).load(userIconPath)
                .error(Glide.with(context)
                        .load(R.drawable.ic_user_nav3))
                .into(holder.user_photo);
*/
  /*      holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
*/
     /*   if (model.getCategoryTitle().contains(otherActorID)) {
            holder.category_title.setText();
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
        }*/

        holder.category_title.setText(model.getTitle());
        holder.category_desc.setText(model.getDescription());
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_category_adapter,
                parent, false);
        return new CategoryHolder(v);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView category_title, category_desc;
/*
        CircleImageView user_photo;
*/

        CategoryHolder(@NonNull View itemView) {
            super(itemView);
            category_title = itemView.findViewById(R.id.all_category_title);
            category_desc = itemView.findViewById(R.id.all_category_description);
        }


    }
}
