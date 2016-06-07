package com.stfalcon.android.ui.adapters.recycler;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by troy379 on 21.01.16.
 */
public abstract class BindingViewHolder<D, B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private B binding;

    public abstract void bind(D d);

    public BindingViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public B getBinding() {
        return binding;
    }
}
