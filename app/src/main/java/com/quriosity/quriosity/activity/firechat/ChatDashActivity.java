package com.quriosity.quriosity.activity.firechat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.adapter.firechat.SectionsPagerAdapter;
import com.quriosity.quriosity.utils.WedAmorApplication;

public class ChatDashActivity extends AppCompatActivity {
    private static final String TAG = "ChatDashActivity";
    private String loggedInUserFid;
    private SectionsPagerAdapter sectionsPagerAdapter;
    public static ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_);
        loggedInUserFid = ((WedAmorApplication) getApplicationContext()).getfirebaseUid();

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);

//        if (((WedAmorApplication) getApplicationContext()).getCheckLogin()) {
//            new FirebaseUtil(this);
//            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//            rootRef.collection(FirebaseUtil.USERS_FIRESTORE)
//                    .document(loggedInUserFid)
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    ((WedAmorApplication) getApplicationContext()).setUserProfileImg(task.getResult().get("profilePic").toString());
//                    ((WedAmorApplication) getApplicationContext()).setFirstUserName(task.getResult().get("firstname").toString());
//                }
//            });
//        }
    }
}
