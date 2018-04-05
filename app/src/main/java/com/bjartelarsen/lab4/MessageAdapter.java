package com.bjartelarsen.lab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> messages = null;
    private Context ctx;

    private AppData appData = AppData.getInstance();

    public MessageAdapter(Context context, ArrayList<Message> list) {
        this.ctx = context;
        this.messages = list;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Message msg = messages.get(index);

        if(appData.nickName == msg.name) {
            view = inflater.inflate(R.layout.list_item_my_message, viewGroup, false);
        }else {
            view = inflater.inflate(R.layout.list_item_message, viewGroup, false);
        }

        TextView username = view.findViewById(R.id.username);
        TextView textMessage = view.findViewById(R.id.message);
        username.setText(msg.name);
        textMessage.setText(msg.message);

        return view;
    }
}
