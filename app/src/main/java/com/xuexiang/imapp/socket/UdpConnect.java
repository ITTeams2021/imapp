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

import com.xuexiang.imapp.Constraints;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.xuexiang.imapp.Constraints.SOCKET_HOST;
import static com.xuexiang.imapp.Constraints.SOCKET_UDP_PORT;

/**
 * @author: Su Yuan
 * @Date: 2021/4/11
 * @Decription:
 */
public class UdpConnect {
    private static UdpConnect UdpConnect;
    private static final String TAG = "UdpConnect";
    //    single size of one thread pool
    private static final int POOL_SIZE = 5;
    private static final int BUFFER_LENGTH = 1024;
    private byte[] receiveByte = new byte[BUFFER_LENGTH];

    private boolean isThreadRunning = false;

    private DatagramSocket client;
    private DatagramPacket receivePacket;

    private ExecutorService mThreadPool;
    private Thread clientThread;

    private OnUDPReceiveCallbackBlock udpReceiveCallback;



    private UdpConnect() {
        super();
        int cpuNumbers = Runtime.getRuntime().availableProcessors();
//        init the thread pool
        mThreadPool = Executors.newFixedThreadPool(cpuNumbers * POOL_SIZE);
    }
    //    offer a global static function
    public static UdpConnect getUdpConnect() {
        if (UdpConnect == null) {
            synchronized (UdpConnect.class) {
                if (UdpConnect == null) {
                    UdpConnect = new UdpConnect();
                }
            }
        }
        return UdpConnect;
    }

    public void startUDPSocket() {
        if (client != null) return;
        try {
//          listen the port
            client = new DatagramSocket(Constraints.SOCKET_UDP_PORT);

            if (receivePacket == null) {
                receivePacket = new DatagramPacket(receiveByte, BUFFER_LENGTH);
            }
            startSocketThread();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    /**
     *  start sending thread
     **/
    private void startSocketThread() {
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "clientThread is running...");
                receiveMessage();
            }
        });
        isThreadRunning = true;
        clientThread.start();
    }
    /**
     * deal with the receive data
     **/
    private void receiveMessage() {
        while (isThreadRunning) {
            if (client != null) {
                try {
                    client.receive(receivePacket);
                } catch (IOException e) {
                    Log.e(TAG, "UDP receive fail, stop the thread.");
                    stopUDPSocket();
                    e.printStackTrace();
                    return;
                }
            }

            if (receivePacket == null || receivePacket.getLength() == 0) {
                Log.e(TAG, "Can not receive UDP data or data is null.");
                continue;
            }
            String strReceive = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Log.d(TAG, strReceive + " from " + receivePacket.getAddress().getHostAddress() + ":" + receivePacket.getPort());
//            parse the json data
            if (udpReceiveCallback != null) {
                udpReceiveCallback.OnParserComplete(receivePacket);
            }
//            reset the length after every time receiving
            if (receivePacket != null) {
                receivePacket.setLength(BUFFER_LENGTH);
            }
        }
    }
    /**
     * stop UDP
     **/
    public void stopUDPSocket() {
        isThreadRunning = false;
        receivePacket = null;
        if (clientThread != null) {
            clientThread.interrupt();
        }
        if (client != null) {
            client.close();
            client = null;
        }
        removeCallback();
    }
    /**
     * send data
     **/
    public void sendMessage(final String message) {
        if (client == null) {
            startUDPSocket();
        }
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress targetAddress = InetAddress.getByName(SOCKET_HOST);
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), targetAddress, SOCKET_UDP_PORT);
                    client.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public interface OnUDPReceiveCallbackBlock {
        void OnParserComplete(DatagramPacket data);
    }
    public void setUdpReceiveCallback(OnUDPReceiveCallbackBlock callback) {
        this.udpReceiveCallback = callback;
    }
    public void removeCallback(){
        udpReceiveCallback = null;
    }
}
