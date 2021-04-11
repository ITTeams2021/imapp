package com.xuexiang.imapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.xuexiang.imapp.activity.MainActivity;
import com.xuexiang.imapp.socket.TcpConnect;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.xuexiang.templateproject", appContext.getPackageName());
    }

    @Test
    public void sendTcp(){
        TcpConnect.sharedCenter().setDisconnectedCallback(new TcpConnect.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {

            }
        });
        TcpConnect.sharedCenter().setConnectedCallback(new TcpConnect.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
            }
        });
        TcpConnect.sharedCenter().setReceivedCallback(new TcpConnect.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
            }
        });
        TcpConnect.sharedCenter().connect(MainActivity.TCP_IP_ADDRESS, 80);
        String msg = "hello word is siadijfaofi";
        TcpConnect.sharedCenter().send(msg.getBytes());
    }
}
