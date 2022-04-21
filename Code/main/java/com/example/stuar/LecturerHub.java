package com.example.stuar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LecturerHub extends AppCompatActivity {

    private android.app.AlertDialog.Builder networkDialogBuilder;
    private android.app.AlertDialog networkDialog;
    Button settings;
    TextView letter;
    TextView name;
    CardView cardViewDevTools;
    CardView cardView_AddTask;
    CardView cardViewAbout;
    CardView cardViewScores;
    CardView cardViewGrading;
    CardView cardViewSendMail;
    EditText taskName;
    EditText total;
    EditText mailSub;
    EditText mailBody;
    Button sendMail;
    Button create;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog.Builder builderAbout;
    private AlertDialog dialog;
    private  AlertDialog mailDialog;
    private AlertDialog aboutDialog;
    GetAssessments assessments = new GetAssessments(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_hub);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String currUsername = bundle.getString("User");
        String boldName = currUsername.toUpperCase();
        String boldChar = String.valueOf(boldName.charAt(0));
        letter = findViewById(R.id.txt_letter);
        letter.setText(boldChar);
        name = findViewById(R.id.txt_lect_user);
        name.setText(boldName);

        assessments.requestAssessments("https://lamp.ms.wits.ac.za/home/s2381410/task.php");

        cardViewDevTools = findViewById(R.id.card_dev_tools);
        cardViewSendMail = findViewById(R.id.card_send_mail);
        cardViewScores = findViewById(R.id.card_to_scores);
        cardViewAbout = findViewById(R.id.card_about);
        cardViewGrading = findViewById(R.id.card_Grading);
        cardView_AddTask = findViewById(R.id.add_card);


        // on click add new task
        cardView_AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssessment(currUsername);
            }
        });

        cardViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog();
            }
        });

        cardViewGrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(LecturerHub.this)){
                    connectionDialog();
                }else {
                    Intent toGrading = new Intent(LecturerHub.this, Grading.class);
                    startActivity(toGrading);
                }
            }
        });

        cardViewScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(LecturerHub.this)){
                    connectionDialog();
                }else {
                    Intent toScores = new Intent(LecturerHub.this, ViewGrades.class);
                    startActivity(toScores);
                }
            }
        });

        cardViewSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailingDialog();
            }
        });

        cardViewDevTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dev = new Intent(LecturerHub.this, DevTools.class);
                startActivity(dev);
            }
        });

    }

    public void addAssessment(String user){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupScreen = getLayoutInflater().inflate(R.layout.addtask, null);

        ArrayList<String> allTasks = new ArrayList<>(assessments.getAllAssessments());
        create = popupScreen.findViewById(R.id.btn_add_assess);
        total = popupScreen.findViewById(R.id.txt_assTotal);
        taskName = popupScreen.findViewById(R.id.txt_newAssName);
        dialogBuilder.setView(popupScreen);
        dialog = dialogBuilder.create();
        dialog.show();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(LecturerHub.this)){
                    connectionDialog();
                }else{
                    boolean flag = false;
                    String grandTotal = total.getText().toString();
                    String name = taskName.getText().toString();
                    for(int i = 0; i < allTasks.size(); i++){
                        String databaseTask = allTasks.get(i);
                        if(databaseTask.equals(name)){
                            flag = true;
                            break;
                        }
                    }

                    if(flag){
                        Toast.makeText(LecturerHub.this, "Try a different name", Toast.LENGTH_SHORT).show();
                    }else {
                        if(name.equals("")){
                            Toast.makeText(LecturerHub.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                        }else {
                            addAssessment(name.trim(), user, grandTotal);
                            Toast.makeText(LecturerHub.this, "Created", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                }
            }
        });
    }

    public void addAssessment(String assessmentName, String creator, String total){
        ContentValues values = new ContentValues();
        values.put("TASK_NAME", assessmentName);
        values.put("CREATOR", creator);
        values.put("GRAND_TOTAL", total);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/addtask.php";
        service.postMethod(LecturerHub.this, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
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

    public void mailingDialog(){
        builderAbout = new AlertDialog.Builder(this);
        final View popUp = getLayoutInflater().inflate(R.layout.send_mail, null);

        mailSub = popUp.findViewById(R.id.txt_mail_sub);
        mailBody = popUp.findViewById(R.id.txt_mail_body);
        sendMail = popUp.findViewById(R.id.btn_send_mail_done);

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(LecturerHub.this)){
                    connectionDialog();
                }else{
                    String body = mailBody.getText().toString();
                    String title = mailSub.getText().toString();
                    if(body.equals("") || title.equals("")){
                        Toast.makeText(LecturerHub.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }else{
                        addMail(title.trim(), body);
                        Toast.makeText(LecturerHub.this, "Sent", Toast.LENGTH_SHORT).show();
                        mailDialog.dismiss();
                    }
                }
            }
        });

        builderAbout.setView(popUp);
        mailDialog = builderAbout.create();
        mailDialog.show();
    }


    public void addMail(String title, String body){
        ContentValues values = new ContentValues();
        values.put("SUBJECT", title);
        values.put("BODY", body);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/sendmail.php";
        service.postMethod(LecturerHub.this, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
            }
        });
    }

    public boolean isConnected(LecturerHub activity){

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