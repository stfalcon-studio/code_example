package com.stfalcon.websocket;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;


public class ClientWebSocket {

    private static final String TAG = "Websocket";
    private MessageListener listener;
    private String host;
    private WebSocket ws;


    public ClientWebSocket(MessageListener listener, String host) {
        this.listener = listener;
        this.host = host;
    }

    public void connect() {
        new Thread(() -> {

            if (ws != null) {
                reconnect();
            } else {
                try {
                    WebSocketFactory factory = new WebSocketFactory();
                    SSLContext context = NaiveSSLContext.getInstance("TLS");
                    factory.setSSLContext(context);
                    ws = factory.createSocket(host);
                    ws.addListener(new SocketListener());
                    ws.connect();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void reconnect() {
        try {
            ws = ws.recreate().connect();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebSocket getConnection() {
        return ws;
    }

    public void close() {
        ws.disconnect();
    }


    public class SocketListener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            Log.i(TAG, "onConnected");
        }

        public void onTextMessage(WebSocket websocket, String message) {
            listener.onSocketMessage(message);
            Log.i(TAG, "Message --> " + message);
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());

            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket websocket,
                                   WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                                   boolean closedByServer) {
            Log.i(TAG, "onDisconnected");
            if (closedByServer) {
                reconnect();
            }
        }

        @Override
        public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
            Log.i(TAG, "Error -->" + cause.getMessage());
            reconnect();
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onPongFrame(websocket, frame);
            websocket.sendPing("Are you there?");
        }
    }


    public interface MessageListener {
        void onSocketMessage(String message);
    }
}
