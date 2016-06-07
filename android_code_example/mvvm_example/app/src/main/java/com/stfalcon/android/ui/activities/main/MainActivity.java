package com.stfalcon.android.ui.activities.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.stfalcon.android.BR;
import com.stfalcon.android.R;
import com.stfalcon.android.databinding.ActivityMainBinding;
import com.stfalcon.android.ui.base.binding.activities.BindingActivity;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainActivityVM> {

    private static final String TAB_ID = "tabId";

    @Override
    public MainActivityVM onCreate() {
        setKeyboardListener(visible -> getViewModel().onKeyboardShown(visible));
        int tabId = getIntent().getExtras().getInt(TAB_ID);
        return new MainActivityVM(this, getBinding(), tabId);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutResources() {
        return R.layout.activity_main;
    }

    public static void open(Activity activity, int tabId) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(TAB_ID, tabId);
        activity.startActivity(intent);
    }
}
