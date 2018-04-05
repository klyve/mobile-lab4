package com.bjartelarsen.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectNickName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_select_nick_name);


        Button button = findViewById(R.id.getStartedButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveSharedPreferences();
                Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }


    private void saveSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("FileName",0);
        TextView nickName = findViewById(R.id.nickName);
        String name = nickName.getText().toString();
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("nick", name);
        prefEditor.apply();

        AppData appData = AppData.getInstance();
        appData.nickName = name;
    }
}
