package com.example.stuar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Collections.addAll;

public class MainActivity extends AppCompatActivity {
    private android.app.AlertDialog.Builder networkDialogBuilder;
    private android.app.AlertDialog networkDialog;
    Button settings;

    EditText password;
    EditText confirm;
    EditText username;
    Button createAccount;
    Button agreeSignIn;
    RadioGroup group;
    RadioButton createAccAs;
    private ArrayList<String> myUsers;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    VerifyUser verifyUser = new VerifyUser(this);
    VerifyUser verfyLecturer =  new VerifyUser(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);


        verfyLecturer.requestUsers("https://lamp.ms.wits.ac.za/home/s2381410/alllecturers.php");
        verifyUser.requestUsers("https://lamp.ms.wits.ac.za/home/s2381410/allstudents.php");
        password = findViewById(R.id.txt_password_signin);
        confirm = findViewById(R.id.txt_conf_password_signin);
        username = findViewById(R.id.txt_username_signup);

        createAccount = findViewById(R.id.btn_create_acc);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    connectionDialog();
                }else {
                    String checkUsername = username.getText().toString();
                    String checkPassword = password.getText().toString();
                    String checkConfirm = confirm.getText().toString();

                    if(checkConfirm.equals("") || checkPassword.equals("") || checkUsername.equals("")){
                        Toast.makeText(MainActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }else if(!checkConfirm.equals(checkPassword)){
                        Toast.makeText(MainActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }else{
                        signInDialog(checkUsername.trim(), checkPassword);
                        password.setText("");
                        username.setText("");
                        confirm.setText("");
                    }
                }
            }
        });
    }

    // sending the type of user with account to log in
    public void lectSignIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.putExtra("User", "Lecturer");
        startActivity(intent);
    }

    public void studentLogIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.putExtra("User", "Student");
        startActivity(intent);
    }

    // student add method
    public void post(String addUsername, String addPassword){
        ContentValues values = new ContentValues();
        values.put("USERNAME", addUsername);
        values.put("PASSWORD", addPassword);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/addstudent.php";
        service.postMethod(MainActivity.this, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
            }
        });
    }

    public void postNewLecture(String addUsername, String addPassword){
        ContentValues values = new ContentValues();
        values.put("USERNAME", addUsername);
        values.put("PASSWORD", addPassword);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/addlecturer.php";
        service.postMethod(MainActivity.this, url, values,new RequestHandler() {
            @Override
            public void response(String data) {
            }
        });
    }

    public void signInDialog(String thePerson, String password){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupScreen = getLayoutInflater().inflate(R.layout.signin, null);

        group = popupScreen.findViewById(R.id.radioGroup);
        agreeSignIn = popupScreen.findViewById(R.id.btn_accept);
        dialogBuilder.setView(popupScreen);
        dialog = dialogBuilder.create();
        dialog.show();

        agreeSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPresent = false;
                boolean isFound = false;
                int radioId = group.getCheckedRadioButtonId();
                createAccAs = popupScreen.findViewById(radioId);
                String option = createAccAs.getText().toString();
                ArrayList<String> list = new ArrayList<>(verifyUser.getAllUsers());
                ArrayList<String> listLecturers = new ArrayList<>(verfyLecturer.getAllUsers());

                for(int i = 0; i < list.size(); i++){
                    String studentOnDatabase = list.get(i);
                    if(thePerson.equals(studentOnDatabase)){
                        isPresent = true;
                        break;
                    }
                }

                for(int i = 0; i < listLecturers.size(); i++){
                    String lectureDatabase = listLecturers.get(i);
                    if(thePerson.equals(lectureDatabase)){
                        isFound = true;
                        break;
                    }
                }

                // what to do when creating an account!
                if(option.equals("Student")){
                    if(isPresent){
                        Toast.makeText(MainActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        post(thePerson, password);
                        Toast.makeText(MainActivity.this, thePerson + " account created, now log in", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }else if(option.equals("Lecturer")){
                    if(isFound){
                        Toast.makeText(MainActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        postNewLecture(thePerson, password);
                        Toast.makeText(MainActivity.this, thePerson + " account created, now log in", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

        });
    }

    public boolean isConnected(MainActivity activity){

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