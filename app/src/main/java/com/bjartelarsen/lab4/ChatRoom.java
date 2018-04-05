package com.bjartelarsen.lab4;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {


    private DatabaseReference database;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    private AppData appData = AppData.getInstance();

    public ArrayList<Message> messages = appData.messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        database = db.getReference("messages");

        if (!isServiceWorkerRunning()) {
            Log.i("Service worker", "Running");
            Intent serviceIntent = new Intent(this, ServiceWorker.class);
            this.startService(serviceIntent);
        }else {
            Log.i("Service worker", "Not running");
        }
        addListenerOnDataBase();
        updateListView();

        addEventListenerToListView();

        addEventListenerToSendMessage();

    }

    protected void addEventListenerToSendMessage() {
        Button btn = findViewById(R.id.sendbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("BTN", "SENDING DATA");

                DatabaseReference myRef = db.getReference();
                DatabaseReference msgRef = myRef.child("messages").push();
                EditText textMessageView = findViewById(R.id.inputMessage);
                String message = textMessageView.getText().toString();
                msgRef.setValue(new Message(message, appData.nickName));

                textMessageView.setText("");
            }
        });
    }


    protected void addEventListenerToListView() {
        ListView listview = findViewById(R.id.listView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), UserMessages.class);
                intent.putExtra("username", messages.get(position).name);
                //Log.i("User:", messages.get(position).name);
                startActivity(intent);
            }
        });
    }


    protected void updateListView() {
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new MessageAdapter(this, messages));
    }



    private void addListenerOnDataBase() {
        // Read from the database

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Message msg = dataSnapshot.getValue(Message.class);
                messages.add(msg);
                updateListView();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException());
            }
        });
    }


    private boolean isServiceWorkerRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceWorker.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
