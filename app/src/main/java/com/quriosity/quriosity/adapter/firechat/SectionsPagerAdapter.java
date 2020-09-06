package com.quriosity.quriosity.adapter.firechat;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.quriosity.quriosity.R;
import com.quriosity.quriosity.fragments.firechat.CategoryFrag;
import com.quriosity.quriosity.fragments.firechat.ChatsFrag;
import com.quriosity.quriosity.fragments.firechat.ContactsFrag;
import com.quriosity.quriosity.fragments.firechat.GroupsFrag;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3,R.string.add};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ChatsFrag();
                break;
            case 1:
                fragment = new GroupsFrag();
                break;
            case 2:
                fragment = new ContactsFrag();
                break;
            /*case 3:
                fragment = new CategoryFrag();
                break;*/
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}