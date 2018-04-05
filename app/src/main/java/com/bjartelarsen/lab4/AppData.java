package com.bjartelarsen.lab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;

class AppData {
    private static final AppData ourInstance = new AppData();


    public boolean loading = false;

    public String nickName = "";

    public String packageName = "com.bartelarsen.lab4";

    public ArrayList<Message> messages = new ArrayList<>();

    public void finishLoading() {
        this.loading = false;
    }

    public void startLoading() {
        this.loading = true;
    }



    static AppData getInstance() {
        return ourInstance;
    }

    private AppData() {
    }
}
