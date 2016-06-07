package com.stfalcon.android.ui.adapters.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stfalcon.android.R;
import com.stfalcon.android.ui.fragments.auth.SignInFragment;
import com.stfalcon.android.ui.fragments.auth.SignUpFragment;
import com.stfalcon.android.ui.fragments.profile.friends.FollowersFragment;
import com.stfalcon.android.ui.fragments.profile.friends.MyFriendsFragment;

/**
 * Created by Anton Bevza on 18.02.16.
 */
public class FriendsPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;

    public FriendsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyFriendsFragment.newInstance();
            case 1:
                return FollowersFragment.newInstance();
            default:
                throw new NullPointerException();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.friends_tab_my).toUpperCase();
            case 1:
                return context.getString(R.string.friends_tab_followers).toUpperCase();
            default:
                throw new NullPointerException();
        }
    }
}
