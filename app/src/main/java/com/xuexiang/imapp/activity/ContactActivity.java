package com.xuexiang.imapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.imapp.Constrains;
import com.xuexiang.imapp.R;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import org.json.JSONException;

@Page(name = "contacts", anim = CoreAnim.none)
public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        data = new String[Constrains.contact_list.length()];
        for(int i = 0; i < Constrains.contact_list.length(); i++){
            try {
                data[i] = Constrains.contact_list.get(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ContactActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView) findViewById(R.id.lv_demo1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                (parent, view, position, id) -> Log.i("item", "parent" + parent.toString() + "position" + position + "id" + id )
        );
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public void onClick(View v){

    }

}