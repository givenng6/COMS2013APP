package com.example.stuar;

import android.app.Activity;
import android.content.ContentValues;

public class Delete {

    public void toDelete(Activity activity,String table, String col, String input){
        ContentValues values = new ContentValues();
        values.put("TABLE", table);
        values.put("COL", col);
        values.put("INPUT", input);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/del.php";
        service.postMethod(activity, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
            }
        });
    }

    public void toUpdate(Activity activity, String name, String group){
        ContentValues values = new ContentValues();
        values.put("STUDENT2", name);
        values.put("GROUP", group);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/join.php";
        service.postMethod(activity, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
            }
        });
    }

}
