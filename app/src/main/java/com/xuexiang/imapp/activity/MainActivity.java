package com.xuexiang.imapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.xuexiang.imapp.Constraints;
import com.xuexiang.imapp.R;
import com.xuexiang.imapp.socket.TcpConnect;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import bean.ChatMessage;
import butterknife.BindView;

import static com.xuexiang.imapp.Constraints.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.user_name)
    MaterialEditText user_name;
    @BindView(R.id.user_password)
    MaterialEditText user_password;

    private boolean receive_state = false;
    private String receive_data = "";
    private String session_id = "";
    private String friend_name="";
    private String msg_content="";


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
                // determine message type
                if(rec_data.has("receivers")){
                    session_id = rec_data.get("session_id").toString();
                    Constraints.session_id = session_id;
                    JSONArray receiver = new JSONArray(rec_data.get("receivers").toString());
                    Constraints.contact_list = receiver;

                    jump_page();
                    Log.i("session", receiver.get(0).toString());
                }
                else{
                    friend_name = rec_data.get("from").toString();
                    msg_content = rec_data.get("message").toString();
                    if(Constraints.current_chat_user.equals(friend_name)){
                        Constraints.msg_content = msg_content;

                        ChatMessage fromMsg = new ChatMessage();
                        fromMsg.setName(current_user_name);
                        fromMsg.setMsg(msg_content);

                        myDatas.add(fromMsg);
                        myAdapter.notifyDataSetChanged();
                        myMsgs.setSelection(myMsgs.getCount() - 1);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        String uname = user_name.getEditValue();
        String upass = user_password.getEditValue();
        current_user_name = uname;

//        uname = "ChenLu";
//        upass = "cl961007";

        TcpConnect.sharedCenter().connect(Constraints.TCP_IP_ADDRESS, Constraints.TCP_PORT);
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