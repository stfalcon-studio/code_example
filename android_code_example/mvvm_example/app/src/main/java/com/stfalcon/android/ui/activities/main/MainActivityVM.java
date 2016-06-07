package com.stfalcon.android.ui.activities.main;

import android.content.Intent;
import android.databinding.ObservableInt;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.stfalcon.android.R;
import com.stfalcon.android.binding.fields.ObservableBoolean;
import com.stfalcon.android.data.events.OpenFragmentEvent;
import com.stfalcon.android.data.preferences.Preferences;
import com.stfalcon.android.databinding.ActivityMainBinding;
import com.stfalcon.android.ui.base.binding.activities.ActivityViewModel;
import com.stfalcon.android.ui.custom.BottomTabLayout;
import com.stfalcon.android.ui.fragments.auth.AuthFragment;
import com.stfalcon.android.ui.fragments.guide.GuideFragment;
import com.stfalcon.android.ui.fragments.profile.ProfileFragment;
import com.stfalcon.android.ui.fragments.travellers.TravellersFragment;
import com.stfalcon.android.ui.fragments.travels.TravelsFragment;
import com.stfalcon.android.utils.AppUtilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Anton Bevza on 4/29/16.
 */
public class MainActivityVM extends ActivityViewModel<MainActivity, ActivityMainBinding> {

    public int menuRes = R.menu.menu_bottom_layout;
    public ObservableBoolean isKeyboardShown = new ObservableBoolean(false);
    public BottomTabLayout.OnItemSelectedListener tabListener;
    public ObservableInt startSelectedTab = new ObservableInt(0);
    private int container;


    public MainActivityVM(MainActivity activity, ActivityMainBinding binding, int tabId) {
        super(activity, binding);
        startSelectedTab.set(tabId);
        container = R.id.container;
        tabListener = this::switchFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void switchFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.menu_profile:
                if (Preferences.getManager().getAuthToken().length() > 0) {
                    fragment = ProfileFragment.newInstance();
                } else {
                    fragment = AuthFragment.newInstance();
                }
                break;
            case R.id.menu_companions:
                fragment = TravellersFragment.newInstance();
                break;
            case R.id.menu_guides:
                fragment = GuideFragment.newInstance();
                break;
            case R.id.menu_messages:
            case R.id.menu_trips:
                fragment = TravelsFragment.newInstance();
                break;
        }
        if (fragment != null) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(container, fragment);
            transaction.commit();
        }
    }

    public void onKeyboardShown(boolean visible) {
        isKeyboardShown.set(visible);
    }

    @Subscribe
    public void onChangeFragment(OpenFragmentEvent changeFragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, changeFragment.getFragment());
        if (changeFragment.isAddToBackStack()) {
            transaction.addToBackStack(null);
        } else {
            activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        transaction.commit();
        try {
            AppUtilities.hideSoftKeyboard(activity, activity.getCurrentFocus());
        } catch (Exception ignore) {
        }
    }
}
