package com.xuexiang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xuexiang.imapp.R;
import bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Chat Adapter
 */
public class ChatMessageAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<ChatMessage> myDatas;

    public ChatMessageAdapter(Context context, List<ChatMessage> myDatas){
        myInflater = LayoutInflater.from(context);
        this.myDatas = myDatas;
    }
    @Override
    public int getCount() {
        //返回适配器中数据集的数据个数
        return myDatas.size();
    }

    @Override
    public Object getItem(int position) {
        //获取数据集中与索引对应的数据项
        return myDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        //获取指定行对应的ID；
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // View类型数量
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = myDatas.get(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
                convertView = myInflater.inflate(R.layout.from_msg,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.myMsg = convertView.findViewById(R.id.from_msg);
                viewHolder.myName = convertView.findViewById(R.id.from_name);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        viewHolder.myMsg.setText(chatMessage.getMsg());
        viewHolder.myName.setText(chatMessage.getName());
        //每一行Item的显示内容
        return convertView;
    }

    private final class ViewHolder{
        TextView myMsg;
        TextView myName;
    }

}
