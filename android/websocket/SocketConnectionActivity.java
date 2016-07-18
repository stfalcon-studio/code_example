package com.stfalcon.websocket;

import android.support.v7.app.AppCompatActivity;

import com.triplook.android.data.events.RealTimeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public abstract class SocketConnectionActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void handleRealTimeMessage(RealTimeEvent event) {
        // processing of all real-time events
    }
}
