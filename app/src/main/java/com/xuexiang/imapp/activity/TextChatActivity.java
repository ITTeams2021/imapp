package com.xuexiang.imapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.imapp.Constraints;
import com.xuexiang.imapp.R;
import com.xuexiang.imapp.socket.TcpConnect;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import org.json.JSONException;

@Page(name = "text_chat", anim = CoreAnim.none)
public class TextChatActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] data = {"SuYuAn : Hello！", "You : Hi!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_chat);
        TextView chat_title = findViewById(R.id.text_chat_title);
        chat_title.setText(Constraints.current_chat_user);

    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public void onClick(View v){

    }

}