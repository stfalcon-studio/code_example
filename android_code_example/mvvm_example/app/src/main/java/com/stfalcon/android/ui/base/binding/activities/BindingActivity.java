package com.stfalcon.android.ui.base.binding.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.stfalcon.android.ui.base.BaseActivity;


/**
 * Created by troy379 on 21.01.16.
 */
public abstract class BindingActivity<B extends ViewDataBinding, VM extends ActivityViewModel>
        extends BaseActivity {

    private B binding;
    private VM viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind();
    }

    public void bind() {
        binding = DataBindingUtil.setContentView(this, getLayoutResources());
        this.viewModel = viewModel == null ? onCreate() : viewModel;
        binding.setVariable(getVariable(), viewModel);
    }

    public B getBinding() {
        return binding;
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onStop() {
        viewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        viewModel.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.onBackKeyPress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    public abstract VM onCreate();

    public VM getViewModel() {
        return viewModel;
    }

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract @IdRes int getVariable();

    /**
     * Override for set layout resource
     *
     * @return layout resource id
     */
    public abstract @LayoutRes int getLayoutResources();
}
