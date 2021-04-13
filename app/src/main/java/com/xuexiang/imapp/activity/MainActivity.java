package com.xuexiang.imapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.xuexiang.imapp.Constrains;
import com.xuexiang.imapp.R;
import com.xuexiang.imapp.socket.TcpConnect;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.user_name)
    MaterialEditText user_name;
    @BindView(R.id.user_password)
    MaterialEditText user_password;

    private boolean receive_state = false;
    private String receive_data = "";
    private String session_id = "";

    public static final String TCP_IP_ADDRESS = "159.75.220.96";
    public static final Integer TCP_PORT = 8888;

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
//                Log.i("connected:", "successful");
            }
        });
        TcpConnect.sharedCenter().setReceivedCallback(new TcpConnect.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receivedMessage) throws UnsupportedEncodingException, JSONException {
                receive_state = true;
                Log.i("received_msg", receivedMessage);
                receive_data = receivedMessage;
                JSONObject rec_data = new JSONObject(receive_data);
                session_id = rec_data.get("session_id").toString();
                JSONArray receiver = new JSONArray(rec_data.get("receivers").toString());
                Constrains.contact_list = receiver;

                jump_page();
                Log.i("session", receiver.get(0).toString());
            }
        });
    }

    @Override
    public void onClick(View v){
        String uname = user_name.getEditValue();
        String upass = user_password.getEditValue();
        uname = "ChenLu";
        upass = "cl961007";

        TcpConnect.sharedCenter().connect(TCP_IP_ADDRESS, TCP_PORT);
        receive_state = false;

        if(uname.length() == 0){
            Toast.makeText(MainActivity.this,"Please input user name!", Toast.LENGTH_SHORT).show();
        }else if(upass.length() == 0){
            Toast.makeText(MainActivity.this,"Please input password!", Toast.LENGTH_SHORT).show();
        }

        // construct json
        String data = null;
        try {
            data = createJson(uname, upass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TcpConnect.sharedCenter().send(data.getBytes());

        if(receive_state)
            TcpConnect.sharedCenter().disconnect();
    }

    private String createJson(String uname, String upass) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name",uname);
        jsonObject.put("user_password",upass);
        jsonObject.put("head", "login");
        String data = jsonObject.toString();
        Log.i("json:", data);
        return data;
    }

    private void jump_page(){
        // jump to contact page
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
    }
}