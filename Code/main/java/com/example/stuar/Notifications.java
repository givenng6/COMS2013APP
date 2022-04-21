package com.example.stuar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    ListView listView;
    TextView msgId;
    TextView msg;
    ArrayList<String> listBody = new ArrayList<>();
    ArrayList<String> listMails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Communication");

        listView = findViewById(R.id.list_mails);
        request();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = listMails.get(position);
                String bod = listBody.get(position);
                mailDialog(title, bod);
            }
        });
    }

    public void request(){
        WebService service = new WebService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/mail.php";
        service.connect(Notifications.this, url, new RequestHandler() {
            @Override
            public void response(String data) {
                try {
                    getMails(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getMails(String JSON) throws JSONException {
        JSONArray array = new JSONArray(JSON);
        for(int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String subject = object.getString("SUBJECT");
            String body = object.getString("BODY");
            listMails.add(subject);
            listBody.add(body);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listMails);
        listView.setAdapter(adapter);

    }

    public void mailDialog(String title, String body){
        builder = new AlertDialog.Builder(this);
        final View pop = getLayoutInflater().inflate(R.layout.view_notifications, null);

        msg = pop.findViewById(R.id.txt_msg);
        msgId = pop.findViewById(R.id.txt_msgid);

        msg.setText(body);
        msgId.setText(title);

        builder.setView(pop);
        dialog = builder.create();
        dialog.show();
    }
}