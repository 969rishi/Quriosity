package com.quriosity.quriosity.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.quriosity.quriosity.R;
import com.quriosity.quriosity.activity.firechat.ChatDashActivity;
import com.quriosity.quriosity.fragments.explore.ExploreFragment;
import com.quriosity.quriosity.fragments.favourite.FavouritesFragment;
import com.quriosity.quriosity.fragments.firechat.CategoryFrag;
import com.quriosity.quriosity.fragments.home.HomeFragment;
import com.quriosity.quriosity.fragments.inbox.InboxFragment;
import com.quriosity.quriosity.fragments.recommendation.RecommendationFragment;
import com.quriosity.quriosity.login.LoginActivity;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.MyUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

import java.util.HashMap;

public class DashboardMainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        RecommendationFragment.OnFragmentInteractionListener,
        FavouritesFragment.OnFragmentInteractionListener,
        InboxFragment.OnFragmentInteractionListener,
        ExploreFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mtoolbar;
    private ActionBar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private TextView userFullName, textviewLocation, myBookingsTextView, myTripsTextView;
    private EditText searchEditText;
    private ImageView navHeaderUsrIcon, logoutAppView, commentAppView, notificationAppView;
    private String TEMPSTORE_BACK_TIMESTAMP = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home: {
//                    if (!MyUtil.isUserLoggedIn(DashboardMainActivity.this)) {
//                        MyUtil.showLoginDialog(DashboardMainActivity.this);
//                    } else {
//                        fragment = new Booking();
//                        setActionBarText(getString(R.string.inbox));
//                    }
                    fragment = new HomeFragment();
                }
                break;
                case R.id.navigation_inbox: {
                    if (!MyUtil.isUserLoggedIn(DashboardMainActivity.this)) {
                        MyUtil.showLoginDialog(DashboardMainActivity.this);
                    } else
                        fragment = new InboxFragment();
                }
                break;
                case R.id.navigation_explore: {
                    fragment = new ExploreFragment();
                    //setActionBarText(getString(R.string.explore).toLowerCase());
                }
                break;
         /*       case R.id.navigation_favourites: {
//                    if (!MyUtil.isUserLoggedIn(DashboardMainActivity.this)) {
//                        MyUtil.showLoginDialog(DashboardMainActivity.this);
//                    } else {
//                        fragment = new UserProfileFragment();
//                        setActionBarText(getString(R.string.favourites));
//                    }
                    fragment = new FavouritesFragment();
                }
                break;
          */

         case R.id.navigation_recommendations: {
//                    if (!MyUtil.isUserLoggedIn(DashboardMainActivity.this)) {
//                        MyUtil.showLoginDialog(DashboardMainActivity.this);
//                    } else {
//                        fragment = new RecommendationFragment();
//                         setActionBarText(getString(R.string.recommendations));
//                    }
                    fragment = new RecommendationFragment();
                }
                break;
                case R.id.navigation_category: {
                    fragment = new CategoryFrag();
                }
                break;

            }
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return true;
            }
            return false;
        }
    };
    private boolean isDrawerOpen;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity_main); //Main Layout
        mtoolbar = findViewById(R.id.dashboard_toolbar);
        textviewLocation = findViewById(R.id.textviewLocationDashboard);
        searchEditText = findViewById(R.id.searchEditTextDashboar);
        setSupportActionBar(mtoolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                isDrawerOpen = false;
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }

        };
        drawer.bringToFront();
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //for sidebar navigaiton
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //if nav header need to swap
        View navHeaderview = navigationView.inflateHeaderView(R.layout.nav_header_user_layout);
        loadNavHeader(navHeaderview);

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_user_drawer);


        //for bottom navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //deault bottom navigation go to explore
        bottomNavigationView.setSelectedItemId(R.id.navigation_explore);

        textviewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardMainActivity.this, "Search Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNavHeader(View navHeaderview) {
        navHeaderUsrIcon = navHeaderview.findViewById(R.id.circularimgview_nav_header_main);
        userFullName = navHeaderview.findViewById(R.id.username_nav_header_main);
        logoutAppView = navHeaderview.findViewById(R.id.logout_app_nav_header_main);
        commentAppView = navHeaderview.findViewById(R.id.comment_nav_header_main);
        notificationAppView = navHeaderview.findViewById(R.id.notification_bell_nav_header_main);

        userFullName.setText(((WedAmorApplication) getApplicationContext()).getFirstUserName());

        Glide.with(this).load("https://www.nicesnippets.com/demo/following1.jpg").into(navHeaderUsrIcon);

        navHeaderUsrIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtil.isUserLoggedIn(DashboardMainActivity.this)) {
                    MyUtil.showLoginDialog(DashboardMainActivity.this);
                } else {
                    Toast.makeText(DashboardMainActivity.this, "Profile Activity Needs to Create", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(DashboardMainActivity.this, ProfileActivity.class));
                }
                closeDrawer();
            }
        });


        notificationAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
//                startActivity(new Intent(DashboardMainActivity.this, Notification.class));
                Toast.makeText(DashboardMainActivity.this, "Notification need to Create", Toast.LENGTH_SHORT).show();
            }
        });

        commentAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                startActivity(new Intent(DashboardMainActivity.this, ChatDashActivity.class));
            }
        });

        logoutAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                if (((WedAmorApplication) getApplicationContext()).getfirebaseUid().equalsIgnoreCase("")) {
                    startActivity(new Intent(DashboardMainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    FirebaseUtil.fbUpdateLoginTable(((WedAmorApplication) getApplicationContext()).getfirebaseUid(),
                            FirebaseUtil.USERS_REGISTRATION_TOKEN_FIELD, "");

                    logoutMethod();
                    Intent i = new Intent(DashboardMainActivity.this, LoginActivity.class);
                    i.putExtra("operation", "signout");
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(i);
                    finish();
                }
            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void signOutMethod(View view) {
        FirebaseUtil.fbUpdateLoginTable(((WedAmorApplication) getApplicationContext()).getfirebaseUid(),
                FirebaseUtil.USERS_REGISTRATION_TOKEN_FIELD, "");

        logoutMethod();
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("operation", "signout");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FirebaseAuth.getInstance().signOut();

        startActivity(i);
        finish();
    }

    private void logoutMethod() {
        ((WedAmorApplication) getApplicationContext()).setfirebaseUid("");
        ((WedAmorApplication) getApplicationContext()).setFirstUserName("");
        ((WedAmorApplication) getApplicationContext()).setCheckLogin(false);
        ((WedAmorApplication) getApplicationContext()).setPrimaryEmailId("");
        ((WedAmorApplication) getApplicationContext()).setPhoneOnly10Digit("");
        ((WedAmorApplication) getApplicationContext()).setPhoneNoWithCountryCode("");
        ((WedAmorApplication) getApplicationContext()).setFirebaseRegistrationToken("");


        //set for no user login
        ((WedAmorApplication) getApplicationContext()).setFirstUserName("User");

    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen) {
            closeDrawer();
        } else {
            if (TEMPSTORE_BACK_TIMESTAMP.isEmpty()) {
                TEMPSTORE_BACK_TIMESTAMP = MyUtil.getTimeStampInMillis();
                Toast.makeText(this, "Press again to close", Toast.LENGTH_SHORT).show();
            } else {
                if (MyUtil.fiveSecWaiting(TEMPSTORE_BACK_TIMESTAMP))
                    minimizeApp();
                else {
                    TEMPSTORE_BACK_TIMESTAMP = MyUtil.getTimeStampInMillis();
                    Toast.makeText(this, "Press again to close", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.END);
    }

//    public void notify(View view) {
//        Intent i = new Intent(this, Notification.class);
//        i.putExtra("uuid", getIntent().getIntExtra("uuid", 10000));
//        startActivity(i);
//    }
//
//    public void infoid(View view) {
//        Intent i = new Intent(this, Chatadd.class);
//        i.putExtra("uuid", "" + getIntent().getIntExtra("uuid", 1000000));
//        startActivity(i);
//    }

    private void setActionBarText(String text) {
        toolbar = getSupportActionBar();
        toolbar.setDisplayShowCustomEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);
        toolbar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.blogs_explore) {
            Toast.makeText(this, "Blogs", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.paid_travel) {
            Toast.makeText(this, "paid travel programme", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.travel_guides) {
            Toast.makeText(this, "travel guide", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.upload_photoviedo) {
            Toast.makeText(this, "publish blogs", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.create_trips) {
            Toast.makeText(this, "create trips", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.ask_community) {
            Toast.makeText(this, "ask community", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.help_me) {
            Toast.makeText(this, "contact@tnt.com", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.settingmenuitem) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.refernearn) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Tired of the same boring trips? If your current friends are not enough then this social trip companion finder will help you find your perfect travel buddy!" +
                                "\n Click here: " + "like of playstore");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            } catch (Exception e) {
                Log.e("ErrorInSharing", e + "");
            }
            closeDrawer();
        } else if (id == R.id.rateusonplaystore) {

            Uri uri = Uri.parse("market://details?id=com.tnt.android&hl=en_IN");
//                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.tnt.android&hl=en_IN" + this.getPackageName())));
            }

            closeDrawer();
        } else if (id == R.id.mytrip) {
            Toast.makeText(this, "my trips", Toast.LENGTH_SHORT).show();
            closeDrawer();
        } else if (id == R.id.mybookings) {
            Toast.makeText(this, "my bookings", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }
        return true;
    }

}