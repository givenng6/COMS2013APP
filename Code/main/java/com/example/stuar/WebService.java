package com.example.stuar;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {

    public void connect(Activity activity, String url, RequestHandler handler){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {

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
