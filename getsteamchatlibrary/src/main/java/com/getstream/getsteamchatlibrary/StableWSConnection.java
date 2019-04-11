package com.getstream.getsteamchatlibrary;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class StableWSConnection extends WebSocketListener {



    String wsURL, clientID,userID;
    int consecutiveFailures, totalFailures, wsID;

    boolean isConnecting = false, isHealthy;

    Date lastEvent;

    int healthCheckInterval, monitorInterval;

    private OkHttpClient client;

    Request request;
    EchoWebSocketListener listener;
    WebSocket ws;

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
//            webSocket.send("Hello, it's SSaurel !");
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");

            lastEvent = new Date();

            isConnecting = false;
            consecutiveFailures = 0;
            _startMonitor();
            _startHealthCheck();

        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {

            lastEvent = new Date();

            isConnecting = false;
            consecutiveFailures = 0;
            _startMonitor();
            _startHealthCheck();
//            this.messageCallback()
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            lastEvent = new Date();


            isConnecting = false;
            consecutiveFailures = 0;
            _startMonitor();
            _startHealthCheck();
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
//            webSocket.close(NORMAL_CLOSURE_STATUS, null);

            if(code != 1000){
                consecutiveFailures += 1;
                totalFailures += 1;
                _setHealth(false);

                _reconnect(0);
            }
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            consecutiveFailures += 1;
            totalFailures += 1;
            _setHealth(false);
            _reconnect(0);

        }
    }

    StableWSConnection(String wsURL, String clientID, String userID/*, messageCallBack, recoverCallback, eventCallback*/){

        this.wsURL = wsURL;
        this.clientID = clientID;
        this.userID = userID;

        /** consecutive failures influence the duration of the timeout */
        this.consecutiveFailures = 0;
        /** keep track of the total number of failures */
        this.totalFailures = 0;

        /** We only make 1 attempt to reconnect at the same time.. */
        this.isConnecting = false;
        /** Boolean that indicates if we have a working connection to the server */
        this.isHealthy = false;

        /** Incremented when a new WS connection is made */
        this.wsID = 1;

        /** Store the last event time for health checks */
        this.lastEvent = null;

        /** Send a health check message every 30 seconds */
        this.healthCheckInterval = 30 * 1000;
        /** Every second we verify that we didn't miss any health checks */
        this.monitorInterval = 1 * 1000;

//        this._listenForConnectionChanges();
    }

//   void _listenForConnectionChanges(){
//        if (typeof window !== 'undefined') {
//            window.addEventListener('offline', this.onlineStatusChanged);
//            window.addEventListener('online', this.onlineStatusChanged);
//        }
//    }

    void connect() {


        if(this.isConnecting){
            return;
        }

        this.isConnecting = true;

        this._connect();


//        this.isConnecting = false;
//        this.consecutiveFailures = 0;
//        this._startMonitor();
//        this.startHealthCheck();
    }

    void _connect() {

        client = new OkHttpClient();

        request = new Request.Builder().url(this.wsURL).build();
        listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    void _startMonitor(){

        long interval = new Date().getTime() - lastEvent.getTime();
        if(interval > this.healthCheckInterval + 10 * 10000){
            this._setHealth(false);
            this._reconnect(0);
        }
    }

    void _reconnect(long interval){
        if(this.isConnecting || this.isHealthy){
            return;
        }
        this.isConnecting = true;

        if(interval == 0){
            interval = this._retryInterval();
        }

//        this._destroyCurrentWSConnection();

//        sleep(interval);

        this._connect();

        this.isConnecting = false;
        this.consecutiveFailures = 0;

    }

//    _destroyCurrentWSConnection(){
//        this.wsID += 1;
//        if(this.ws && this.ws.){
//
//        }
//    }

    long _retryInterval(){

        int max = this.consecutiveFailures * 5000;
        if(max > 25000){
            max = 25000;
        }
        int min = (this.consecutiveFailures - 1) * 5000;
        long interval = Math.round(Math.random() * (max - min) + min);
        if(interval < 1000){
            interval = 1000;
        }

        return interval;

    }
    void _startHealthCheck(){
//        ws.send("type");
    }

//    void _setupConnectionPromise(){
//        this.isResolved = false;
//        this.connectionOpen = new Promise
//    }

    void _setHealth(boolean healthy){
        if(healthy && !this.isHealthy){
            this.isHealthy = true;
        }

        if(!healthy && this.isHealthy){
            this.isHealthy = false;
        }
    }
}