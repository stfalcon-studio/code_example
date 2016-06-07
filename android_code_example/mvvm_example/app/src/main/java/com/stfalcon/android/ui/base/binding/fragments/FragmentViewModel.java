package com.stfalcon.android.ui.base.binding.fragments;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;

/**
 * Created by troy379 on 16.01.16.
 */
public abstract class FragmentViewModel<F extends BindingFragment, B extends ViewDataBinding>
        extends BaseObservable {

    protected abstract void initialize(B binding);

    private F fragment;
    private B binding;
    private Activity activity;

    public FragmentViewModel(F fragment, B binding) {
        this.fragment = fragment;
        this.binding = binding;
        this.activity = this.fragment.getActivity();
        initialize(binding);
    }

    public F getFragment() {
        return fragment;
    }

    public Fragment getParentFragment() {
        return fragment.getParentFragment();
    }

    public B getBinding() {
        return binding;
    }

    public Activity getActivity() {
        return activity;
    }

    public void updateBinding(B binding) {
        this.binding = binding;
        initialize(binding);
    }

    /**
     * Fragment lifecycle
     */
    public void onViewCreated(){

    }
    public void onDestroy() {

    }
    public void onPause() {

    }
    public void onResume() {

    }
    public void onDestroyView(){
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


    /**
     * -----------------------
     */
}
