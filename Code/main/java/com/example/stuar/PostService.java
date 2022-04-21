package com.example.stuar;

import android.app.Activity;
import android.content.ContentValues;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostService {

    public void postMethod(Activity activity, String url, ContentValues toPost, RequestHandler handler){
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder post = new FormBody.Builder();
        for(String key:toPost.keySet()){
            post.add(key, toPost.getAsString(key));
        }

        Request request = new Request.Builder()
                .url(url)
                .post(post.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String webData = response.body().string();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.response(webData);
                    }
                });

            }
        });
    }



}
