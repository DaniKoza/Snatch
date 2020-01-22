package com.example.snatch;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {
    private SharedPreferences prefs;

    public MySharedPreferences(Context context) {
        prefs = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putArrayList(ArrayList<Record> list) {
        Type recordType = new TypeToken<ArrayList<Record>>() {
        }.getType();
        this.putString("scoreList", new Gson().toJson(list, recordType));
    }

    public ArrayList<Record> getArrayList(String list, String defaultValue) {
        String scoreSetJson = this.getString(list, defaultValue);
        if (!scoreSetJson.isEmpty()) {
            if (!scoreSetJson.equalsIgnoreCase("na")) {
                Type recordType = new TypeToken<ArrayList<Record>>() {
                }.getType();
                return new Gson().fromJson(scoreSetJson, recordType);
            }
        }
        return new ArrayList<>();
    }

    public void putFloat(String key, Float val) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, val);
        editor.apply();
    }

    public float getFloat(String key, Float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }

    public void removeKey(String key) {
        prefs.edit().remove(key);
    }
}
