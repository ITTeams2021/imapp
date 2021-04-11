package com.xuexiang.imapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.xuexiang.imapp.R;
import com.xuexiang.imapp.socket.TcpConnect;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.user_name)
    MaterialEditText user_name;
    @BindView(R.id.user_password)
    MaterialEditText user_password;

    public static final String TCP_IP_ADDRESS = "172.217.160.78";
    public static final Integer TCP_PORT = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        findViewById(R.id.btn_login).setOnClickListener(this);

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
    }

    public void onClick(View v){
        String uname = user_name.getEditValue();
        String upass = user_password.getEditValue();

        if(uname.length() == 0){
            Toast.makeText(MainActivity.this,"Please input user name!", Toast.LENGTH_SHORT).show();
        }else if(upass.length() == 0){
            Toast.makeText(MainActivity.this,"Please input password!", Toast.LENGTH_SHORT).show();
        }

        TcpConnect.sharedCenter().connect(TCP_IP_ADDRESS, TCP_PORT);
        // construct json
        String data = null;
        try {
            data = createJson(uname, upass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TcpConnect.sharedCenter().send(data.getBytes());
//        TcpConnect.sharedCenter().receive();
//        TcpConnect.sharedCenter().disconnect();
    }

    private String createJson(String uname, String upass) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name",uname);
        jsonObject.put("user_password",upass);
        String data = jsonObject.toString();
        return data;
    }
}