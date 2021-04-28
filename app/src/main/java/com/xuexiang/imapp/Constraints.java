package com.xuexiang.imapp;

import android.widget.ListView;

import com.xuexiang.adapter.ChatMessageAdapter;

import org.json.JSONArray;

import java.util.List;

import bean.ChatMessage;

/**
 * @author: Su Yuan
 * @Date: 2021/4/14
 * @Decription:
 */
public class Constraints {
    public static String session_id;
    public static JSONArray contact_list;
    public static String current_user_name;
    public static String current_chat_user;
    public static String msg_content;

    public static final String TCP_IP_ADDRESS = "159.75.220.96";
    public static final Integer TCP_PORT = 8888;

    public static final String SOCKET_HOST = "159.75.220.96";
    public static final Integer SOCKET_UDP_PORT=7777;

    public static ListView myMsgs; //listView
    public static ChatMessageAdapter myAdapter;
    public static List<ChatMessage> myDatas;
}
