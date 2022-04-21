package com.example.stuar;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerifyUser {
    Activity activity;
    public ArrayList<String> allUsers = new ArrayList<>();

    public VerifyUser(Activity activity){
        this.activity = activity;
    }

    public void setAllUsers(ArrayList<String> allUsers){
        this.allUsers.addAll(allUsers);
    }

    public ArrayList<String> getAllUsers(){
        return this.allUsers;
    }


    public void requestUsers(String url){
        WebService service = new WebService();
        service.connect(activity, url, new RequestHandler() {
            @Override
            public void response(String data) {
                try {
                    getStudents(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getStudents(String JSON) throws JSONException {
        ArrayList<String> makeList = new ArrayList<>();
        JSONArray array = new JSONArray(JSON);
        for(int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String userName = object.getString("USERNAME");
            makeList.add(userName);
        }
        setAllUsers(makeList);
    }

}

