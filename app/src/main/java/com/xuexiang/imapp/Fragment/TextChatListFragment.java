package com.xuexiang.imapp.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.xuexiang.imapp.Constraints;
import com.xuexiang.imapp.R;
import com.xuexiang.imapp.activity.TextChatActivity;


import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Su Yuan
 * @Date: 2021/4/14
 * @Decription:
 */
public class TextChatListFragment extends Fragment {

//    private String[] data = {"SuYuAn : Hello！", "You : Hi!"};
    public String[] data = new String[]{};
    private List<String> saveChatMsg = new ArrayList<String>(); ;
    private View view;
    private ListView listView;
    private Handler receiveHandler;
    private Runnable runnable;
//    private int test_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //通过参数中的布局填充获取对应布局
        view =inflater.inflate(R.layout.fragment_chat_text_list, container,false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(),android.R.layout.simple_list_item_1,data);
        listView = (ListView) view.findViewById(R.id.message);
        listView.setAdapter(adapter);

        receiveData();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        receiveHandler.removeCallbacks(runnable);
    }

    // update chat list
    private void updateChatList(){
//        test_num += 1;
//        data = new String[]{""+ test_num};
        if(Constraints.new_send == true) {
            saveChatMsg.add(Constraints.msg_send);
            data = (String[]) saveChatMsg.toArray(new String[saveChatMsg.size()]);
            System.out.println(Constraints.msg_send);
            Constraints.new_send = false;
        }
        if(Constraints.new_recv==true){
            saveChatMsg.add(Constraints.msg_recv);
            data = (String[]) saveChatMsg.toArray(new String[saveChatMsg.size()]);
            System.out.println(Constraints.msg_recv);
            Constraints.new_recv = false;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(),android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    // keep receiving data
    private void receiveData(){
        final int Time = 2000;    //delay

        this.receiveHandler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                receiveHandler.postDelayed(this, Time);
                if(Constraints.new_send == true || Constraints.new_recv==true)
                     updateChatList();
            }
        };
        receiveHandler.postDelayed(runnable, Time);
    }

}
