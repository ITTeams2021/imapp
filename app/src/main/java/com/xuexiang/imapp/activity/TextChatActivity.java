package com.xuexiang.imapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.adapter.ChatMessageAdapter;
import com.xuexiang.imapp.Constraints;
import com.xuexiang.imapp.R;
import com.xuexiang.imapp.socket.TcpConnect;
import com.xuexiang.imapp.socket.UdpConnect;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import bean.ChatMessage;

import static com.xuexiang.imapp.Constraints.current_chat_user;
import static com.xuexiang.imapp.Constraints.current_user_name;

@Page(name = "text_chat", anim = CoreAnim.none)
public class TextChatActivity extends AppCompatActivity {

//    private String[] data = {"SuYuAn : Hello！", "You : Hi!"};
    private UdpConnect udpConnect=null;
    private Button sendBtn;
    private Button voiceBtn;
    private TextView chat_title;
    private EditText editText;

    private boolean receive_state = false;
    private String receive_data = "";
    private String session_id = "";
    private String send_msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_chat);
        TcpConnect.sharedCenter().connect(Constraints.TCP_IP_ADDRESS, Constraints.TCP_PORT);
        udpConnect.getUdpConnect();
        udpConnect.startUDPSocket();

        initView();
        //set the title to the username that you chat with
        chat_title.setText(Constraints.current_chat_user);
        initListener();
        receiveMsg();
    }

//    public void setData(String[] data) {
//        this.data = data;
//    }

    private void initListener() {
        // send message
        sendBtn.setOnClickListener(v -> {
            send_msg = editText.getText().toString();
            if (send_msg.length() == 0) {
                Toast.makeText(TextChatActivity.this, "The message should not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String data = createJson(current_user_name, send_msg, current_chat_user);
                udpConnect.sendMessage(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ChatMessage toMsg = new ChatMessage();
            toMsg.setName(current_user_name);
            toMsg.setMsg(send_msg);

            Constraints.myDatas.add(toMsg);
            Constraints.myAdapter.notifyDataSetChanged();
            Constraints.myMsgs.setSelection(Constraints.myMsgs.getCount() - 1);
            editText.setText("");
        });

        // voice call
        voiceBtn.setOnClickListener(v -> {
            jumpToCall();
        });
    }

    // initiate View
    private void initView() {
        sendBtn = findViewById(R.id.send_btn);
        voiceBtn = findViewById(R.id.voice_call);
        chat_title = findViewById(R.id.text_chat_title);
        editText = findViewById(R.id.editTextTextPersonName);
        Constraints.myMsgs = findViewById(R.id.list_view_msg);
    }

    // udp json format
    private String createJson(String uname, String message, String fname) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "text");
        jsonObject.put("from", uname);
        jsonObject.put("to", fname);
//        jsonObject.put("session", session_id);
        jsonObject.put("message", message);
        String data = jsonObject.toString();
        Log.i("json:", data);
        return data;
    }

    private void receiveMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TcpConnect.sharedCenter().receive();
            }
        }).start();
    }

    private void jumpToCall(){
        // jump to voice call page
        Intent intent = new Intent(TextChatActivity.this, VoiceCallActivity.class);
        startActivity(intent);
    }
}