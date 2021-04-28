package com.xuexiang.imapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.imapp.Constraints;
import com.xuexiang.imapp.R;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import org.json.JSONException;

@Page(name = "contacts", anim = CoreAnim.none)
public class ContactActivity extends AppCompatActivity{

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        data = new String[Constraints.contact_list.length()];
        for(int i = 0; i < Constraints.contact_list.length(); i++){
            try {
                data[i] = Constraints.contact_list.get(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ContactActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView) findViewById(R.id.message);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Log.i("item", "parent" + parent.toString() + "position" + position + "id" + id);
                    try {
                        String friend_name = Constraints.contact_list.get(position).toString();
                        Constraints.current_chat_user = friend_name;
                        jump_page();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void setData(String[] data) {
        this.data = data;
    }


    private void jump_page(){
        // jump to contact page
        Intent intent = new Intent(ContactActivity.this, TextChatActivity.class);
        startActivity(intent);
    }

}