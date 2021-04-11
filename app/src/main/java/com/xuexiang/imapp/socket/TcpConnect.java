/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.imapp.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author: Su Yuan
 * @Date: 2021/04/11
 * @Decription:
 */
public class TcpConnect {
    private static TcpConnect instance;
    private static final String TAG = "TcpConnect";
    //    Socket
    private Socket socket;
    //    IP address
    private String ipAddress;
    //    port
    private int port;
    private Thread thread;
    //    Socket outputStream
    private OutputStream outputStream;
    //    Socket inputStream
    private InputStream inputStream;
    //    connect callback
    private OnServerConnectedCallbackBlock connectedCallback;
    //    connect fail
    private OnServerDisconnectedCallbackBlock disconnectedCallback;
    //   receive callback
    private OnReceiveCallbackBlock receivedCallback;

    private TcpConnect() {
        super();
    }
    //    offer a global static function
    public static TcpConnect sharedCenter() {
        if (instance == null) {
            synchronized (TcpConnect.class) {
                if (instance == null) {
                    instance = new TcpConnect();
                }
            }
        }
        return instance;
    }
    /**
     *  connect by ip address and port
     *
     * @param ipAddress
     * @param port
     */
    public void connect(final String ipAddress, final int port) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ipAddress, port);
//                    socket.setSoTimeout ( 2 * 1000 );//设置超时时间
                    if (isConnected()) {
                        TcpConnect.sharedCenter().ipAddress = ipAddress;
                        TcpConnect.sharedCenter().port = port;
                        if (connectedCallback != null) {
                            connectedCallback.callback();
                        }
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        receive();
                        Log.i(TAG,"Connect successful");
                    }else {
                        Log.i(TAG,"Connect fail");
                        if (disconnectedCallback != null) {
                            disconnectedCallback.callback(new IOException("Connect fail"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Connect error");
                    if (disconnectedCallback != null) {
                        disconnectedCallback.callback(e);
                    }
                }
            }
        });
        thread.start();
    }
    /**
     * judge if it is connected.
     */
    public boolean isConnected() {
        return socket.isConnected();
    }
    /**
     * connect
     */
    public void connect() {
        connect(ipAddress,port);
    }
    /**
     * disconnect
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
                if (socket.isClosed()) {
                    if (disconnectedCallback != null) {
                        disconnectedCallback.callback(new IOException("disconnect"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * receive the data
     */
    public void receive() {
        while (isConnected()) {
            try {
                /**convert data*/
                byte[] bt = new byte[1024];
//                get the len of byte
                int length = inputStream.read(bt);
//               get the right size of byte
                byte[] bs = new byte[length];
                System.arraycopy(bt, 0, bs, 0, length);

                String str = new String(bs, "UTF-8");
                if (str != null) {
                    if (receivedCallback != null) {
                        receivedCallback.callback(str);
                    }
                }
                Log.i(TAG,"Receive successfully");
            } catch (IOException e) {
                Log.i(TAG,"Receive fail");
            }
        }
    }
    /**
     * send the data
     *
     * @param data
     */
    public void send(final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    try {
                        outputStream.write(data);
                        outputStream.flush();
                        Log.i(TAG,"send successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.i(TAG,"send fail");
                    }
                } else {
                    connect();
                }
            }
        }).start();

    }

    public interface OnServerConnectedCallbackBlock {
        void callback();
    }
    public interface OnServerDisconnectedCallbackBlock {
        void callback(IOException e);
    }
    public interface OnReceiveCallbackBlock {
        void callback(String receicedMessage);
    }

    public void setConnectedCallback(OnServerConnectedCallbackBlock connectedCallback) {
        this.connectedCallback = connectedCallback;
    }

    public void setDisconnectedCallback(OnServerDisconnectedCallbackBlock disconnectedCallback) {
        this.disconnectedCallback = disconnectedCallback;
    }

    public void setReceivedCallback(OnReceiveCallbackBlock receivedCallback) {
        this.receivedCallback = receivedCallback;
    }
    /**
     * remove the callback
     */
    private void removeCallback() {
        connectedCallback = null;
        disconnectedCallback = null;
        receivedCallback = null;
    }
}
