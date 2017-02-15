package com.example.nisha.skiweather;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nisha on 11/20/2016.
 */
public class UserLocalStore {
    public static final String SP_NAME ="User Details";
    SharedPreferences localdatabase;

    public UserLocalStore(Context context){
        localdatabase = context.getSharedPreferences(SP_NAME,0);

    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = localdatabase.edit();
        spEditor.putString("name",user.username);
        spEditor.putString("email",user.email);
        spEditor.putString("password",user.password);

    }
    public User getLoggedInUser(){
        String name = localdatabase.getString("name","");
        String email = localdatabase.getString("email","");
        String password = localdatabase.getString("password","");

        User storeUser = new User(name,password,email);
        return storeUser;

    }
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = localdatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();

    }

    public boolean getUserLoggedIn(){
        if(localdatabase.getBoolean("LoggedIn",false) == true)
            return true;
        else
            return false;

    }
    public void clearUserData(){

        SharedPreferences.Editor spEditor = localdatabase.edit();
        spEditor.clear();
        spEditor.commit();

    }
}
