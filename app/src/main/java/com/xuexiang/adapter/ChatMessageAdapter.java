package com.xuexiang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuexiang.imapp.R;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater myInflater;//用于挤压显示消息
    private List<ChatMessage> myDatas;

    @Override
    public int getCount() {
        return myDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return myDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private final class ViewHolder{
        TextView myName;
        TextView myMsg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = myDatas.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = myInflater.inflate(R.layout.from_msg,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.myName = convertView.findViewById(R.id.from_name);
            viewHolder.myMsg = convertView.findViewById(R.id.from_msg);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.myMsg.setText(chatMessage.getMsg());
        viewHolder.myName.setText(chatMessage.getName());

        return convertView;
    }
}
