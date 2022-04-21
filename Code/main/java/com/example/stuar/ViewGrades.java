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

public class ViewGrades extends AppCompatActivity {

    TextView title;
    TextView upper;
    TextView lower;
    ListView listView;
    ArrayList<String> gradedGroups = new ArrayList<>();
    ArrayList<String> taskName = new ArrayList<>();
    ArrayList<String> score = new ArrayList<>();
    ArrayList<String> total = new ArrayList<>();
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grades);
        getSupportActionBar().setTitle("Graded Groups");

        listView = findViewById(R.id.list_all_groups_graded);
        request();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sub = taskName.get(position);
                String num = score.get(position);
                String dem = total.get(position);
               reportDialog(sub, num, dem);
            }
        });
    }

    public void request(){
        WebService service = new WebService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/getgraded.php";
        service.connect(ViewGrades.this, url, new RequestHandler() {
            @Override
            public void response(String data) {
                try {
                    getGroups(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGroups(String JSON) throws JSONException {
        JSONArray array = new JSONArray(JSON);
        for(int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String groupName = object.getString("GROUP_NAME");
            String task = object.getString("ASSESSMENT");
            String scoree = object.getString("SCORE");
            String totall = object.getString("TOTAL");
            gradedGroups.add(groupName);
            taskName.add(task);
            score.add(scoree);
            total.add(totall);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , gradedGroups);
        listView.setAdapter(adapter);

    }

    public void reportDialog(String sub, String num, String dem){
        builder = new AlertDialog.Builder(this);
        final View popUp = getLayoutInflater().inflate(R.layout.report, null);

        title = popUp.findViewById(R.id.txt_subject);
        upper = popUp.findViewById(R.id.txt_numerator);
        lower = popUp.findViewById(R.id.txt_demon);
        title.setText(sub);
        upper.setText(num);
        lower.setText(dem);

        builder.setView(popUp);
        dialog = builder.create();
        dialog.show();
    }
}