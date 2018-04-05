package com.bjartelarsen.lab4;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ServiceWorker extends Service {


    String username;
    DatabaseReference database;

    NotificationManager notificationManager = null;
    NotificationCompat.Builder notificationBuilder = null;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "Chat app";

    public ServiceWorker() {}

    public void onCreate() {
        Log.i("SW", "Started SWORKER");
        database = FirebaseDatabase.getInstance().getReference("messages");
        addListenerOnDatabase();

        Log.i("Notifications", "Started notifications");

        getUserPreference();

        // Variables for notification
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // sets notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "MessageApp", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Chat app notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.argb(100,255, 105, 180));
            notificationChannel.setVibrationPattern(new long[]{0, 100, 20, 100, 20, 100, 20, 100});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * onStartCommand default function
     * **/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * onDestroy default function that runs when service stops
     * **/
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * onBind default function that runs when service is binded
     * **/
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    public void addListenerOnDatabase() {
        // Read from the database
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Message msg = dataSnapshot.getValue(Message.class);

                AppData appData = AppData.getInstance();
                if (validate(msg.message) && validate(msg.name) && !isForeground(appData.packageName)) {
                    if (!username.equals(msg.name)) {
                        notifyUser(msg.message, msg.name);
                    }
                }

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

    public boolean validate(String string) {
        boolean valid;
        switch (string) {
            case "": valid = false;
                break;
            default: valid = true;
                break;
        }
        return valid;
    }

    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }

    public void notifyUser(String msg, String author) {
        // Create and send a notification to user
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        notificationBuilder.setContentTitle("MessageApp");
        notificationBuilder.setContentText("New message from " + author + " - " + msg);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void getUserPreference() {
        // Get shared prefs
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);

        // Get values from shared prefs
        username = sharedPref.getString("nick", "");
    }
}
