package com.stfalcon.android.ui.adapters.recycler;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Bevza on 21.02.2016.
 */
public class SimpleRecyclerBindingAdapter<T> extends RecyclerView.Adapter<SimpleRecyclerBindingAdapter.BindingHolder> {

    private List<T> items = new ArrayList<>();
    private int variableId;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;
    private int itemLayout;

    public SimpleRecyclerBindingAdapter(List<T> items) {
        this.items = items;
    }

    public SimpleRecyclerBindingAdapter() {
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    @Override
    public SimpleRecyclerBindingAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerBindingAdapter.BindingHolder holder, int position) {
        final T item = items.get(position);
        holder.getBinding().getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, item);
            }
        });
        holder.getBinding().getRoot().setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(position, item);
                return true;
            }
            return false;
        });
        holder.getBinding().setVariable(variableId, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setVariable(int variableId) {
        this.variableId = variableId;
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setItemLayout(int item_layout) {
        this.itemLayout = item_layout;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        int curSize = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(curSize, this.items.size());
    }

    public void addItem(T item) {
        this.items.add(item);
        notifyItemInserted(this.items.size());
    }

    public void addItem(int position, T item) {
        this.items.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return items == null || items.size() == 0;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(int position, T item);
    }
}

