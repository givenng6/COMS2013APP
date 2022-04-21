package com.example.stuar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectedStudentAssign extends AppCompatActivity implements GroupDialog.DialogListener {

    ListView listView;
    ArrayList<String> groups = new ArrayList<>();
    Button createGroup;
    Button joinGroup;
    String globalAssignName;
    String student1;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_student_assign);
        getSupportActionBar().setTitle("Assessment Groups");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String assignmentName = bundle.getString("taskName");
        String currUser = bundle.getString("User");
        globalAssignName = assignmentName;
        student1 = currUser;
        post();


        listView = findViewById(R.id.list_existing_groups);

        // The create new group button
        createGroup = findViewById(R.id.btn_create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupDialog();
            }
        });


        // what to do if you click a group
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupNameClicked = groups.get(position);
                Intent join = new Intent(SelectedStudentAssign.this, JoiningGroup.class);
                join.putExtra("User", currUser);
                join.putExtra("Group", groupNameClicked);
                startActivity(join);

            }
        });
    }

    public void post(){
        ContentValues values = new ContentValues();
        values.put("ASSESSMENT_NAME", globalAssignName);
        PostService service = new PostService();
        String url = "https://lamp.ms.wits.ac.za/home/s2381410/gettask.php";
        service.postMethod(SelectedStudentAssign.this, url, values,new RequestHandler() {
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


    // creating a new group === DONE!
    public void createGroupDialog(){
        GroupDialog groupDialog = new GroupDialog();
        groupDialog.show(getSupportFragmentManager(), "Group Dialog");
    }
    @Override
    public void input(String groupName) {
        // Adding new group to the database
        boolean flag = false;
        for(int i = 0; i < groups.size(); i++){
            if(groups.get(i).equals(groupName)){
                flag = true;
            }
        }

        if(flag){
            Toast.makeText(this, groupName + " already exits", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put("ASSESSMENT_NAME", globalAssignName);
            values.put("STUDENT1", student1);
            values.put("GROUP_NAME", groupName);

            PostService service = new PostService();
            String url = "https://lamp.ms.wits.ac.za/home/s2381410/newgroup.php";
            service.postMethod(SelectedStudentAssign.this, url, values,new RequestHandler() {
                @Override
                public void response(String data) {
                    try {
                        getGroups(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Toast.makeText(this, "Group created refresh page", Toast.LENGTH_SHORT).show();
        }
    }



}