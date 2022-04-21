package com.example.stuar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class LogInActivity extends AppCompatActivity {


    private AlertDialog.Builder networkDialogBuilder;
    private AlertDialog networkDialog;
    Button settings;
    Button logInButton;
    EditText InputUsername;
    EditText InputPassword;


    String globalTypeUser;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<String> passwords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_log_in);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String typeUser = bundle.getString("User");// the type of user is logging in
        globalTypeUser = typeUser;

        request();
        InputUsername = findViewById(R.id.txt_userLogin);
        InputPassword = findViewById(R.id.txt_user_password_logIn);
        logInButton = findViewById(R.id.btn_logIn);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(LogInActivity.this)){
                    connectionDialog();
                    //Toast.makeText(LogInActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                }else {
                    String currUsername = InputUsername.getText().toString().trim();
                    String currPassword = InputPassword.getText().toString();
                    if(currPassword.equals("") || currUsername.equals("")){
                        Toast.makeText(LogInActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }else{
                        if(confirmUsers(currUsername, currPassword)){
                            InputPassword.setText("");

                            if(typeUser.equals("Student")){
                                Intent openPage = new Intent(LogInActivity.this, StudentHub.class);
                                openPage.putExtra("User", currUsername);
                                startActivity(openPage);
                                finish();

                            }else if(typeUser.equals("Lecturer")){
                                Intent openPage = new Intent(LogInActivity.this, LecturerHub.class);
                                openPage.putExtra("User", currUsername);
                                startActivity(openPage);
                                finish();
                            }

                        }else {
                            Toast.makeText(LogInActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                }


        });


    }

    public void request(){
        String url = globalTypeUser.equals("Student") ?
                "https://lamp.ms.wits.ac.za/home/s2381410/loginstudent.php"
                : "https://lamp.ms.wits.ac.za/home/s2381410/loginlecturer.php";
        WebService service = new WebService();
        service.connect(LogInActivity.this, url, new RequestHandler() {
            @Override
            public void response(String data) {
                try {
                    getUsers(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUsers(String JSON) throws JSONException {
            JSONArray array = new JSONArray(JSON);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String getUsername = object.getString("USERNAME");
                String getPassword = object.getString("PASSWORD");
                users.add(getUsername);
                passwords.add(getPassword);
            }
    }

    public boolean confirmUsers(String InputUsername, String InputPassword){
        boolean flag = false;
        int index = 0;
        for(int i = 0; i < users.size(); i++){
            String databaseUser = users.get(i);
            if(InputUsername.equals(databaseUser)){
                index = index + i;
                break;
            }
        }
        String databasePass = passwords.get(index);
        if(InputPassword.equals(databasePass) && InputUsername.equals(users.get(index))){
            flag = true;
        }

        return flag;
    }

    public void fogottenPass(View view) {
        String text = "Lol, I was lazy to code functionality for this";
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();

    }


    public boolean isConnected(LogInActivity activity){

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
        networkDialogBuilder = new AlertDialog.Builder(this);
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