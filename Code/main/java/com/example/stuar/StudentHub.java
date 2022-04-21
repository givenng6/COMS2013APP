package com.example.stuar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentHub extends AppCompatActivity {


    private android.app.AlertDialog.Builder networkDialogBuilder;
    private android.app.AlertDialog networkDialog;
    Button settings;
    CardView assessment;
    CardView notifications;
    CardView about;
    CardView exit;
    private AlertDialog.Builder builderAbout;
    private AlertDialog aboutDialog;
    public TextView loggedInUser;
    String currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_hub);
        getSupportActionBar().setTitle("STUDENT HUB");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String loggedInAs = bundle.getString("User");// the type of user is logging in
        loggedInUser = findViewById(R.id.txt_username_loggedIn);
        loggedInUser.setText(loggedInAs);
        currUser = loggedInUser.getText().toString();

        assessment = findViewById(R.id.card_assignment);
        notifications = findViewById(R.id.card_stu_notifica);
        about = findViewById(R.id.card_about_stu);
        exit = findViewById(R.id.card_app_close);

        assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(StudentHub.this)){
                    connectionDialog();
                }else {
                    Intent openPage = new Intent(StudentHub.this, ListStudentAssign.class);
                    openPage.putExtra("User", currUser);
                    startActivity(openPage);
                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog();
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(StudentHub.this)){
                    connectionDialog();
                }else {
                    Intent openPage = new Intent(StudentHub.this, Notifications.class);
                    startActivity(openPage);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openPage = new Intent(StudentHub.this, SplashScreen.class);
                startActivity(openPage);
                finish();
            }
        });


    }

    public void infoDialog(){
        builderAbout = new AlertDialog.Builder(this);
        final View aboutPopUp = getLayoutInflater().inflate(R.layout.about, null);

        builderAbout.setView(aboutPopUp);
        aboutDialog = builderAbout.create();
        aboutDialog.show();
    }

    public boolean isConnected(StudentHub activity){

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi  = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile  = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    public void connectionDialog(){
        networkDialogBuilder = new android.app.AlertDialog.Builder(this);
        final View popUp = getLayoutInflater().inflate(R.layout.no_connection, null);


        settings = popUp.findViewById(R.id.btn_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                networkDialog.dismiss();
            }
        });
        networkDialogBuilder.setView(popUp);
        networkDialog = networkDialogBuilder.create();
        networkDialog.show();
    }

}