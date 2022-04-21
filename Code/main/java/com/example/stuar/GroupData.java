package com.example.stuar;

import android.app.Activity;
import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupData {
    public String student1;
    public String student2;
    public Activity activity;

    public GroupData(Activity activity){
        this.activity = activity;
    }

    public void setStudent1(String name){
        this.student1 = name;
    }

    public String getStudent1(){
        return this.student1;
    }

    public void setStudent2(String name){
        this.student2 = name;
    }

    public String getStudent2(){
        return this.student2;
    }


    public void requestGroups(String groupName){
        ContentValues values = new ContentValues();
        values.put("GROUP_NAME", groupName);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/checkgroup.php";
        service.postMethod(activity, url, values,new RequestHandler() {
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
        JSONArray array = new JSONArray(JSON);
        for(int i = 0; i < JSON.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String s1 = object.getString("STUDENT1");
            String s2 = object.getString("STUDENT2");
            setStudent1(s1);
            setStudent2(s2);
        }

    }
}
