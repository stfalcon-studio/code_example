package com.stfalcon.android.ui.adapters.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Bevza on 01.03.16.
 */
public abstract class RecyclerBindingAdapter<T, H extends BindingViewHolder> extends RecyclerView.Adapter<H> {

    protected List<T> items;
    protected ArrayList<Wrapper<T>> wrappers = new ArrayList<>();
    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;
    private int itemLayout;
    protected Context context;

    public RecyclerBindingAdapter(List<T> items) {
        if (items != null) {
            init(items);
        }
    }

    protected void init(List<T> items) {
        setItems(items);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        return onCreateHolder(v);
    }

    public abstract H onCreateHolder(View view);

    @Override
    public void onBindViewHolder(H holder, int position) {
        final Wrapper<T> item = wrappers.get(position);
        holder.getBinding().getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item.item);
            }
        });
        holder.getBinding().getRoot().setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(position, item.item);
                return true;
            }
            return false;
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (wrappers != null) {
            return wrappers.size();
        } else {
            return 0;
        }
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
        createWrappers();
        notifyDataSetChanged();
    }

    public void addItems(List<T> newItems) {
        if (items == null) {
            items = new ArrayList<>();
        }
        int curSize = items.size();
        items.addAll(newItems);
        createWrappers();
        notifyItemRangeInserted(curSize, items.size());
    }

    public boolean isEmpty() {
        return items == null || items.size() == 0;
    }

    public void clear() {
        if (items != null) {
            items.clear();
            wrappers.clear();
        }
    }

    public void removeItemWithId(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (getItemId(i) == id) {
                items.remove(i);
                wrappers.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(int position, T item);
    }

    protected void createWrappers() {
        wrappers.clear();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                wrappers.add(createWrapper(items.get(i)));
            }
        }
    }

    public abstract Wrapper<T> createWrapper(T item);

    public static abstract class Wrapper<A> {

        protected A item;
        public int position;

        public A getItem() {
            return item;
        }

        public void setItem(A item) {
            this.item = item;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}
