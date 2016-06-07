package com.stfalcon.android.ui.adapters.recycler.custom;

import android.util.Log;
import android.view.View;

import com.stfalcon.android.R;
import com.stfalcon.android.data.events.RemoveFriendEvent;
import com.stfalcon.android.data.models.entities.User;
import com.stfalcon.android.databinding.ItemFriendBinding;
import com.stfalcon.android.ui.adapters.recycler.BindingViewHolder;
import com.stfalcon.android.ui.adapters.recycler.RecyclerBindingAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Anton Bevza on 5/6/16.
 */
public class MyFriendsListAdapter extends RecyclerBindingAdapter<User, MyFriendsListAdapter.FriendsViewHolder> {
    private static final int VIEW = R.layout.item_friend;

    public MyFriendsListAdapter(List<User> items) {
        super(items);
        setItemLayout(VIEW);
    }

    @Override
    public long getItemId(int position) {
        if (items != null) {
            return items.get(position).getId();
        }
        return 0;
    }

    @Override
    public FriendsViewHolder onCreateHolder(View view) {
        return new FriendsViewHolder(view);
    }

    @Override
    public FriendWrapper createWrapper(User item) {
        return new FriendWrapper(item);
    }


    public class FriendsViewHolder extends BindingViewHolder<FriendWrapper, ItemFriendBinding> {


        public FriendsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(FriendWrapper friendWrapper) {
            getBinding().setWrapper(friendWrapper);
        }
    }

    public class FriendWrapper extends RecyclerBindingAdapter.Wrapper<User> {

        public FriendWrapper(User user) {
            setItem(user);
        }

        @Override
        public User getItem() {
            return item;
        }

        public void remove() {
            EventBus.getDefault().post(new RemoveFriendEvent(getItem().getId()));
        }

        public void onItemClick(){
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(getItem());
            }
        }

        public void message() {
            Log.i("TAG", "message to userId: " + getItem().getId());
        }
    }
}
