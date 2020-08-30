package com.quriosity.quriosity.fragments.firechat;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.adapter.firechat.FriendsListAdapter;
import com.quriosity.quriosity.models.FirestoreUsersModel;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ContactsFrag";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressbar;
    private LinearLayout mainLayout, noDataFound;
    private ImageView imageView;
    private RecyclerView recyclerview;
    private FriendsListAdapter chatsListAdapter;
    private String myFirebaseID;
    private String myFireFirstName;

    public ContactsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFrag newInstance(String param1, String param2) {
        ContactsFrag fragment = new ContactsFrag();
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
        myFireFirstName = ((WedAmorApplication) getActivity().getApplicationContext()).getFirstUserName();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        progressbar = view.findViewById(R.id.progress_bar);
        noDataFound = view.findViewById(R.id.no_data_found_layout);
        imageView = view.findViewById(R.id.imageView_chats);
        mainLayout = view.findViewById(R.id.wholedata_home_fragment_layout);
        recyclerview = view.findViewById(R.id.recycler_destination_home_fragment_layout);
        showProgress();

        return view;
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
        Log.d(TAG, "setUpRecyclerChatsList: myFirebaseID " + myFirebaseID);
        FirebaseFirestore fireob = FirebaseFirestore.getInstance();
        Query query = fireob.collection(FirebaseUtil.USERS_FIRESTORE);
        FirestoreRecyclerOptions<FirestoreUsersModel> options = new FirestoreRecyclerOptions.Builder<FirestoreUsersModel>()
                .setQuery(query, FirestoreUsersModel.class)
                .build();
        chatsListAdapter = new FriendsListAdapter(options, getActivity(), myFirebaseID, ContactsFrag.this, myFireFirstName);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(chatsListAdapter);
        recyclerview.setHasFixedSize(false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                hideProgress();
            }
        }, 1000);

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
}