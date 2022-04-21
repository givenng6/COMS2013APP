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

public class ListStudentAssign extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listAssessments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student_assign);
        getSupportActionBar().setTitle("Assessments");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String currUser = bundle.getString("User");
        listView = findViewById(R.id.my_list);
        request();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openPage = new Intent(ListStudentAssign.this, SelectedStudentAssign.class);
                openPage.putExtra("taskName", listAssessments.get(position));
                openPage.putExtra("User", currUser);
                startActivity(openPage);
            }
        });

    }

    public void request(){
        WebService service = new WebService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/task.php";
        service.connect(ListStudentAssign.this, url, new RequestHandler() {
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
            listAssessments.add(assessmentName);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listAssessments);
        listView.setAdapter(adapter);

    }
}