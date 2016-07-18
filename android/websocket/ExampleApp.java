package com.stfalcon.websocket;

import android.app.Application;
import android.util.Log;

public class ExampleApp extends Application {

    private ExampleSocketConnection exampleSocketConnection;

    @Override
    public void onCreate() {
        super.onCreate();

        exampleSocketConnection = new ExampleSocketConnection(this);
        BackgroundManager.get(this).registerListener(appActivityListener);
    }


    public void closeSocketConnection() {
        exampleSocketConnection.closeConnection();
    }

    public void openSocketConnection() {
        exampleSocketConnection.openConnection();
    }

    public boolean isSocketConnected() {
        return exampleSocketConnection.isConnected();
    }

    public void reconnect() {
        exampleSocketConnection.openConnection();
    }


    private BackgroundManager.Listener appActivityListener = new BackgroundManager.Listener() {
        public void onBecameForeground() {
            openSocketConnection();
            Log.i("Websocket", "Became Foreground");
        }

        public void onBecameBackground() {
            closeSocketConnection();
            Log.i("Websocket", "Became Background");
        }
    };
}
