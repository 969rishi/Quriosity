package com.quriosity.quriosity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/**
 * @author World_pc
 * @version 0.1
 * @since 10/27/2015
 */
public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    private ActionBar mActionBar;
    protected Menu mMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
       // window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Integer.valueOf(Build.VERSION.SDK_INT) >= 21) {
//           // window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MenuItem", item.getTitle() + "");
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Gets rid of the differences between implementation of Fragment & Activity
     *
     * @return Reference to self
     */
    protected Activity getActivity() {
        return this;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    protected void setupActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);

    }
    public void setTitleOfActionBar(String titleOfActionBar){
        mActionBar.setTitle(titleOfActionBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Base Resume", "Resume");
        invalidateOptionsMenu();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

}
