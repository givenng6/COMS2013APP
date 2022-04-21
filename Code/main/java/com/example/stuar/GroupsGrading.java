package com.example.stuar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupsGrading extends AppCompatActivity {


    private AlertDialog.Builder builderGrading;
    private AlertDialog gradingDialog;
    ArrayList<String> groups = new ArrayList<>();
    EditText score;
    TextView theScore;
    Button submitGrading;
    String globalAssignName;
    ListView listView;
    String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_grading);
        getSupportActionBar().setTitle("Click a Group to Grade");
        Intent intent = getIntent();
        // receive the total for the assessment
        Bundle bundle = intent.getExtras();
        String assignmentName = bundle.getString("taskName");
        String total = bundle.getString("total");
        globalAssignName = assignmentName;

        listView = findViewById(R.id.list_grading_group);
        post();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupName = groups.get(position);
                gradeDialog(total);

            }
        });
    }

    public void post(){
        ContentValues values = new ContentValues();
        values.put("ASSESSMENT_NAME", globalAssignName);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/gettask.php";
        service.postMethod(GroupsGrading.this, url, values,new RequestHandler() {
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
            groups.add(groupName);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);
    }

    public void gradeDialog(String  total){
        builderGrading = new AlertDialog.Builder(this);
        final View gradingPopUp = getLayoutInflater().inflate(R.layout.mark_group, null);

        theScore = gradingPopUp.findViewById(R.id.txt_total_score);
        score = gradingPopUp.findViewById(R.id.edit_score);
        submitGrading = gradingPopUp.findViewById(R.id.btn_submit_grading);
        theScore.setText(total);

        submitGrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currScore = score.getText().toString();
                if(currScore.equals("")){
                    Toast.makeText(GroupsGrading.this, "Score can't be empty", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(currScore) > Integer.parseInt(total)){
                    Toast.makeText(GroupsGrading.this, "Score cant be greater than the grand total", Toast.LENGTH_SHORT).show();
                }else {
                    addGrading(groupName, globalAssignName, currScore, total);
                    Toast.makeText(GroupsGrading.this, "Graded Successfully", Toast.LENGTH_SHORT).show();
                    gradingDialog.dismiss();
                }
            }
        });

        builderGrading.setView(gradingPopUp);
        gradingDialog = builderGrading.create();
        gradingDialog.show();
    }

    public void addGrading(String groupName, String taskName, String score, String total){
        ContentValues values = new ContentValues();
        values.put("GROUP_NAME", groupName);
        values.put("ASSESSMENT", taskName);
        values.put("SCORE", score);
        values.put("TOTAL", total);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/grading.php";
        service.postMethod(GroupsGrading.this, url, values,new RequestHandler() {
            @Override
            public void response(String data) {

            }
        });
    }

}