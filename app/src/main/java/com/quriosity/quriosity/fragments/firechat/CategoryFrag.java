package com.quriosity.quriosity.fragments.firechat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.adapter.firechat.CategoryAdapter;
import com.quriosity.quriosity.models.firechat.FirestoreCategoryModel;
import com.quriosity.quriosity.utils.WedAmorApplication;


public class CategoryFrag extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CategoryFrag";

    private String mParam1;
    private String mParam2;
    private ProgressBar progressbar;
    private LinearLayout mainLayout, noDataFound;
/*    private ImageView imageView;*/
    private RecyclerView recyclerview;
    private CategoryAdapter categoryListAdapter;
    private String myFirebaseID;

    public CategoryFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFrag.
     */

    public static CategoryFrag newInstance(String param1, String param2) {
        CategoryFrag fragment = new CategoryFrag();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        progressbar = view.findViewById(R.id.progress_bar_category);
        noDataFound = view.findViewById(R.id.no_data_found_layout_category);
  //      imageView = view.findViewById(R.id.imageView_chats_category);
        mainLayout = view.findViewById(R.id.wholedata_category_fragment_layout);
        recyclerview = view.findViewById(R.id.recycler_destination_home_fragment_layout_category);

        showProgress();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (categoryListAdapter != null) {
            categoryListAdapter.stopListening();
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
        if (categoryListAdapter != null) {
            categoryListAdapter.startListening();
        }
    }

    private void setUpRecyclerChatsList() {

        Log.d(TAG, "setUpRecyclerChatsList: ");
        FirebaseFirestore fireob = FirebaseFirestore.getInstance();
        Query query = fireob.collection("category");
        FirestoreRecyclerOptions<FirestoreCategoryModel> options = new FirestoreRecyclerOptions.Builder<FirestoreCategoryModel>()
                .setQuery(query, FirestoreCategoryModel.class)
                .build();
        categoryListAdapter = new CategoryAdapter(options, getActivity(), myFirebaseID);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(categoryListAdapter);
        recyclerview.setHasFixedSize(false);
        hideProgress();

/*
        FirebaseFirestore fireob = FirebaseFirestore.getInstance();
        Query query = fireob.collection(FirebaseUtil.CHATS)
                .whereArrayContains("conversationActors", myFirebaseID)
                .whereEqualTo("type", CONVERSATION_TYPE_DM)
                .orderBy("latestMessage.senton", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CategoryModel> options = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();
*/

    }


    private void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.GONE);
    }

    private void showNoData() {
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressbar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        noDataFound.setVisibility(View.GONE);
    }
}