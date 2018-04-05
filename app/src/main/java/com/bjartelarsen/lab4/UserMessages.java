package com.bjartelarsen.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserMessages extends AppCompatActivity {



    private AppData appData = AppData.getInstance();

    public ArrayList<Message> messages = appData.messages;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            Log.i("USER", username);
            toolbar.setTitle(username);
        }


        ListView listView = findViewById(R.id.listView);

        ArrayList<Message> newMessages = new ArrayList<>();

        for(int i = 0; i < messages.size(); ++i) {
            Message msg = messages.get(i);

            if(username.equals(msg.name)) {
                newMessages.add(msg);
            }
        }

        listView.setAdapter(new MessageAdapter(this, newMessages));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatRoom.class);
                startActivity(i);
            }
        });
    }

}
