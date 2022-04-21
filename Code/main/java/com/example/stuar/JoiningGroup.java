package com.example.stuar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JoiningGroup extends AppCompatActivity {
    Button join;
    TextView currGroup;
    Delete update = new Delete();
    GroupData groupData = new GroupData(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_group);
        getSupportActionBar().setTitle("Join Group");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String groupName = bundle.getString("Group");
        String currUser = bundle.getString("User");
        groupData.requestGroups(groupName);

        join = findViewById(R.id.btn_join_curr_group);
        currGroup = findViewById(R.id.txt_curr_group);
        currGroup.setText(groupName);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String student1 = groupData.getStudent1();
                String student2 = groupData.getStudent2();
                if(student2.equals("null") && !student1.equals(currUser)){
                    update.toUpdate(JoiningGroup.this, currUser, groupName);
                    Toast.makeText(JoiningGroup.this, "Joined successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(JoiningGroup.this, "Group full or already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}