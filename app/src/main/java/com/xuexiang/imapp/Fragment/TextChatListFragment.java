package com.xuexiang.imapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xuexiang.imapp.R;
import com.xuexiang.imapp.activity.TextChatActivity;


import androidx.fragment.app.Fragment;

/**
 * @author: Su Yuan
 * @Date: 2021/4/14
 * @Decription:
 */
public class TextChatListFragment extends Fragment {

//    private String[] data = {"SuYuAn : Hello！", "You : Hi!"};
    private String[] data={};
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //通过参数中的布局填充获取对应布局
        View view =inflater.inflate(R.layout.fragment_chat_text_list, container,false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(),android.R.layout.simple_list_item_1,data);
        listView = (ListView) view.findViewById(R.id.message);
        listView.setAdapter(adapter);

        return view;
    }

    public void update(String[] data){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(),android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
    }
}
