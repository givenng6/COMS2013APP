package com.example.stuar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DevTools extends AppCompatActivity {

    TextView clicked;
    EditText toSend;
    CardView delStu;
    CardView delLect;
    CardView delTask;
    CardView delGroup;
    CardView delGradedGroup;
    Button query;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    Delete delete = new Delete();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_tools);
        getSupportActionBar().setTitle("Developer Tools");

        delStu = findViewById(R.id.card_del_stu);
        delLect = findViewById(R.id.card_del_lect);
        delTask = findViewById(R.id.card_del_assg);
        delGroup = findViewById(R.id.card_del_group);
        delGradedGroup = findViewById(R.id.card_del_graded_grp);


        delStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "STUDENTS";
                String col = "USERNAME";
                delDialog(table, col);
            }
        });

        delLect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "LECTURER";
                String col = "USERNAME";
                delDialog(table, col);
            }
        });

        delTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "ASSESSMENTS";
                String col = "TASK_NAME";
                delDialog(table, col);
            }
        });

        delGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "GROUPS";
                String col = "GROUP_NAME";
                delDialog(table, col);
            }
        });

        delGradedGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String table = "GRADES";
                String col = "GROUP_NAME";
                delDialog(table, col);
            }
        });

    }

    public void delDialog(String table, String col){
        builder = new AlertDialog.Builder(this);
        final View popUp = getLayoutInflater().inflate(R.layout.del, null);

        clicked =  popUp.findViewById(R.id.txt_clicked_name);
        clicked.setText(table);
        toSend = popUp.findViewById(R.id.txt_to_send_name);
        query = popUp.findViewById(R.id.btn_query);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = toSend.getText().toString().trim();
                if(input.equals("")){
                    Toast.makeText(DevTools.this, "Can't compute empty field", Toast.LENGTH_SHORT).show();
                }else {
                    delete.toDelete(DevTools.this, table, col, input);
                    Toast.makeText(DevTools.this, "Query Ok", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        builder.setView(popUp);
        dialog = builder.create();
        dialog.show();
    }
}