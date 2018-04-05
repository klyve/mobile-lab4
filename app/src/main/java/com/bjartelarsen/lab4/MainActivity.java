package com.bjartelarsen.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();
        setContentView(R.layout.activity_splash_screen);

        final AppData appData = AppData.getInstance();

        appData.startLoading();

        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadFromCache();
                            appData.finishLoading();
                            setActivity();

                        }
                    });
                }
            }, 500);





        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Message msg1 = new Message("Hello world", "Bjarte");
        //Message msg2 = new Message("Whats up?", "someone Else");

        //DatabaseReference myRef = database.getReference();
        //DatabaseReference newRef1 = myRef.child("messages").push();
        //DatabaseReference newRef2 = myRef.child("messages").push();

        //newRef1.setValue(msg1);
        //newRef2.setValue(msg2);

    }


    private void hideActionBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();
    }

    public void setActivity() {
        AppData appData = AppData.getInstance();
        Intent intent;
        if(appData.nickName == "") {
            intent = new Intent(getApplicationContext(), SelectNickName.class);
        }else {
            intent = new Intent(getApplicationContext(), ChatRoom.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    public void loadFromCache() {
        Log.i("Shared pref", "Loading shared pref");
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        AppData appData = AppData.getInstance();
        appData.nickName = sharedPref.getString("nick", "");
    }
}
