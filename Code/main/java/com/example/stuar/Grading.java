package com.example.stuar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Grading extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listAssessments = new ArrayList<>();
    ArrayList<String> grandTotal = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);
        getSupportActionBar().setTitle("Click Assessment to Grade");

        listView = findViewById(R.id.list_grading);
        request();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toGroupGrading = new Intent(Grading.this, GroupsGrading.class);
                // send in the total of the assessment
                toGroupGrading.putExtra("taskName", listAssessments.get(position));
                toGroupGrading.putExtra("total", grandTotal.get(position));
                Toast.makeText(Grading.this,"Grading " + listAssessments.get(position), Toast.LENGTH_SHORT).show();
                startActivity(toGroupGrading);

            }
        });
    }

    public void request(){
        WebService service = new WebService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/task.php";
        service.connect(Grading.this, url, new RequestHandler() {
            @Override
            public void response(String data) {
                try {
                    getAssessments(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAssessments(String JSON) throws JSONException {
        JSONArray array = new JSONArray(JSON);
        for(int i = 0; i < array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            String assessmentName = object.getString("TASK_NAME");
            String assessmentTotal = object.getString("GRAND_TOTAL");
            listAssessments.add(assessmentName);
            grandTotal.add(assessmentTotal);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listAssessments);
        listView.setAdapter(adapter);

    }
}
