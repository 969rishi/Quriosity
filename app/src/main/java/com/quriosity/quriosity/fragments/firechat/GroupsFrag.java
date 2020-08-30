package com.quriosity.quriosity.fragments.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.CreateGroupActivity;
import com.quriosity.quriosity.adapter.firechat.GroupChatsListAdapter;
import com.quriosity.quriosity.models.firechat.ConversationModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import static com.quriosity.quriosity.utils.FirebaseUtil.CONVERSATION_TYPE_GROUP;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "GroupsFrag";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerview;
    private FloatingActionButton floating_btn;
    private LinearLayout mainLayout, noDataFound;
    private ProgressBar progressbar;

    private GroupChatsListAdapter chatsListAdapter;
    private String myFirebaseID;

    public GroupsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFrag newInstance(String param1, String param2) {
        GroupsFrag fragment = new GroupsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myFirebaseID = ((WedAmorApplication) getActivity().getApplicationContext()).getfirebaseUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        progressbar = view.findViewById(R.id.progress_bar);
        noDataFound = view.findViewById(R.id.no_data_found_layout);
        mainLayout = view.findViewById(R.id.wholedata_group_fragment_layout);
        recyclerview = view.findViewById(R.id.recycler_destination_group_fragment_layout);
        floating_btn = view.findViewById(R.id.floating_btn_group);
        setOnClickMethods();
        return view;

    }

    private void setOnClickMethods() {
        floating_btn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreateGroupActivity.class));
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (chatsListAdapter != null) {
            chatsListAdapter.stopListening();
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
        Query query = fireob.collection(FirebaseUtil.CHATS)
                .whereArrayContains("conversationActors", myFirebaseID)
                .whereEqualTo("type", CONVERSATION_TYPE_GROUP)
                .orderBy("latestMessage.senton", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ConversationModel> options = new FirestoreRecyclerOptions.Builder<ConversationModel>()
                .setQuery(query, ConversationModel.class)
                .build();
        chatsListAdapter = new GroupChatsListAdapter(options, getActivity(), myFirebaseID);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(chatsListAdapter);
        recyclerview.setHasFixedSize(false);
        hideProgress();
    }

    private void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        floating_btn.setVisibility(View.GONE);
        noDataFound.setVisibility(View.GONE);
    }

    private void showNoData() {
        floating_btn.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        noDataFound.setVisibility(View.GONE);
        floating_btn.setVisibility(View.VISIBLE);
    }
}