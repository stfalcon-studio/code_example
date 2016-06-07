package com.stfalcon.android.ui.base.binding.activities;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;

import com.stfalcon.android.ui.base.BaseActivity;

/**
 * Created by alex on 19.01.16.
 */
public abstract class ActivityViewModel<A extends BaseActivity, B extends ViewDataBinding>
        extends BaseObservable {

    protected A activity;
    protected B binding;

    public ActivityViewModel(A activity, B binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public A getActivity() {
        return activity;
    }

    public B getBinding() {
        return binding;
    }

    public void finish() {
        activity.finish();
    }

    /**
     * Activity lifecycle
     */
    public void onBackKeyPress() {

    }

    public void onStart() {

    }

    public void onStop() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onDestroy() {

    }

    public void onPause() {

    }

    public void onResume() {

    }
    /**
     * -----------------------
     */
}