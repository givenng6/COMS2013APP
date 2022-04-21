package com.example.stuar;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetAssessments {
    Activity activity;
    public ArrayList<String> allAssessments = new ArrayList<>();

    public GetAssessments(Activity activity){
        this.activity = activity;
    }

    public void setAllAssessments(ArrayList<String> allAssessments){
        this.allAssessments.addAll(allAssessments);
    }

    public ArrayList<String> getAllAssessments(){
        return this.allAssessments;
    }


    public void requestAssessments(String url){
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
            String userName = object.getString("TASK_NAME");
            makeList.add(userName);
        }
        setAllAssessments(makeList);
    }

}
