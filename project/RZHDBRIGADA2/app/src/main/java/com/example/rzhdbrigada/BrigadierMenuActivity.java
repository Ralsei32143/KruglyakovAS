package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BrigadierMenuActivity extends AppCompatActivity {

    private Button btnCreateTask
            , btnListTasks
            , btnReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigadier_menu);

        btnCreateTask = findViewById(R.id.btnCreateTask);
        btnListTasks = findViewById(R.id.btnListTasks);
        btnReports = findViewById(R.id.btnReports);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrigadierMenuActivity.this
                        , BrigadierCreateTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnListTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrigadierMenuActivity.this
                        , BrigadierListTasksActivity.class);
                startActivity(intent);
            }
        });

        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrigadierMenuActivity.this
                        , BrigadierReportActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("new_task")) {
                TaskItem newTask = (TaskItem) data.getSerializableExtra("new_task");
                Intent listIntent = new Intent(this
                        , BrigadierListTasksActivity.class);
                listIntent.putExtra("new_task", newTask);
                startActivity(listIntent);
            }
        }
    }
}