package com.example.onlineattendance;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.onlineattendance.Pojo.User;
import com.google.gson.Gson;

public class UserPreference {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean saveUser(String name, String id, String department, String semester, String section) {
        //editor.putString("email",email);
        //editor.putString("pass",pass);
        User user = new User(name, id, department, semester,section);
        Gson gson = new Gson();
        String user_string = gson.toJson(user);
        editor.putString("user_object", user_string);
        boolean set = editor.commit();
        return set;
    }

    public User getUserObject() {
        Gson gson = new Gson();
        String user_string = sharedPreferences.getString("user_object", null);
        User user = gson.fromJson(user_string, User.class);
        return user;
    }

    public boolean setUserSituation(String situation) {
        editor.putString("situation", situation);
        boolean set = editor.commit();
        return set;
    }

    public boolean getUserSituation() {

        String situation = sharedPreferences.getString("situation", "logout");

        if (situation.equals("login")) {
            return true;
        } else {
            return false;
        }
    }

}
