package com.stfalcon.android.ui.base.binding.activities;

import android.databinding.ViewDataBinding;

import com.stfalcon.android.ui.base.BaseActivity;

/**
 * Created by Anton Bevza on 29.05.16.
 */
public abstract class RealmActivityViewModel<A extends BaseActivity, B extends ViewDataBinding> extends ActivityViewModel {

    public RealmActivityViewModel(BaseActivity activity, ViewDataBinding binding) {
        super(activity, binding);
    }

    private Realm realm;

    public RealmActivityViewModel(A activity, B binding) {
        super(activity, binding);
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return realm;
    }

    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }

}