package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;

public class WorkerCreateDocumentActivity extends AppCompatActivity {

    private TextView tvTaskTitle, tvBrigadeName;
    private Button btnInWork
            , btnCompleted;
    private CheckBox cbProblem1
            , cbProblem2
            , cbProblem3;
    private EditText etComment;
    private TextView tvCounter;
    private Button btnSubmit;

    private String selectedStatus = "in_work";
    private TaskItem currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_create_document);


        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvBrigadeName = findViewById(R.id.tvBrigadeName);
        btnInWork = findViewById(R.id.btnInWork);
        btnCompleted = findViewById(R.id.btnCompleted);
        cbProblem1 = findViewById(R.id.cbProblem1);
        cbProblem2 = findViewById(R.id.cbProblem2);
        cbProblem3 = findViewById(R.id.cbProblem3);
        etComment = findViewById(R.id.etComment);
        tvCounter = findViewById(R.id.tvCounter);
        btnSubmit = findViewById(R.id.btnSubmit);


        currentTask = (TaskItem) getIntent().getSerializableExtra("task");
        if (currentTask != null) {
            tvTaskTitle.setText(currentTask.title);
            tvBrigadeName.setText("Бригада: " + currentTask.brigade);
        }


        etComment.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s
                    , int start
                    , int count
                    , int after) {}

            @Override
            public void onTextChanged(CharSequence s
                    , int start
                    , int before
                    , int count) {
                tvCounter.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnInWork.setOnClickListener(v -> {
            updateStatusButtons("in_work");
            selectedStatus = "in_work";
        });

        btnCompleted.setOnClickListener(v -> {
            updateStatusButtons("completed");
            selectedStatus = "completed";
        });
        btnSubmit.setOnClickListener(v -> {
            StringBuilder problems = new StringBuilder();
            if (cbProblem1.isChecked()) problems.append("Износ рельс/шпал, ");
            if (cbProblem2.isChecked()) problems.append("Загрязнение путей, ");
            if (cbProblem3.isChecked()) problems.append("Повреждение оборудования, ");

            String comment = etComment.getText().toString().trim();

            DocumentReport report = new DocumentReport(
                    currentTask,
                    selectedStatus,
                    problems.toString(),
                    comment
            );


            String statusText = selectedStatus.equals("in_work") ? "В работе" : "Выполнено";
            String message = "Статус: "
                    + statusText + "\n"
                    +"Проблемы: "
                    + (problems.length() > 0 ? problems.toString() : "не указаны")
                    + "\n"
                    + "Комментарий: "
                    + (comment.isEmpty() ? "нет" : comment);

            Toast.makeText(this
                    , "Отчет отправлен!\n" + message
                    , Toast.LENGTH_LONG).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("report", report);
            setResult(RESULT_OK, resultIntent);

            finish();
        });
    }

    private void updateStatusButtons(String selected) {

        btnInWork.setBackgroundTintList(getColorStateList(android.R.color.white));
        btnCompleted.setBackgroundTintList(getColorStateList(android.R.color.white));
        btnInWork.setTextColor(getColor(R.color.red));
        btnCompleted.setTextColor(getColor(R.color.red));

        if ("in_work".equals(selected)) {
            btnInWork.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        } else {
            btnCompleted.setBackgroundTintList(getColorStateList(android.R.color.holo_green_light));
        }
    }


    private class DocumentReport implements Serializable {
        TaskItem task;
        String status;
        String problems;
        String comment;

        DocumentReport(TaskItem task, String status, String problems, String comment) {
            this.task = task;
            this.status = status;
            this.problems = problems;
            this.comment = comment;
        }
    }
}