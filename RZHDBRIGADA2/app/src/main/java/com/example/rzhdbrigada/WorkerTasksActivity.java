package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class WorkerTasksActivity extends AppCompatActivity {

    private LinearLayout tasksContainer;
    private TextView tvActive
            , tvCompleted;
    private List<TaskItem> allTasks = new ArrayList<>();
    private List<TaskItem> filteredTasks = new ArrayList<>();
    private String currentFilter = "active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_tasks);

        tasksContainer = findViewById(R.id.tasksContainer);
        tvActive = findViewById(R.id.tvActive);
        tvCompleted = findViewById(R.id.tvCompleted);

        initTestData();

        filterTasks("active");

        tvActive.setOnClickListener(v -> {
            updateFilterButtons("active");
            filterTasks("active");
        });

        tvCompleted.setOnClickListener(v -> {
            updateFilterButtons("completed");
            filterTasks("completed");
        });
    }

    private void initTestData() {
        allTasks.add(new TaskItem("Ремонт пути, участок 3"
                , "13.02.2026 14:30"
                , "Восточная"
                , "Высокий"
                , "active"));
        allTasks.add(new TaskItem("Проверка состава, путь 5"
                , "13.02.2026 10:15"
                , "Западная"
                , "Низкий"
                , "active"));
        allTasks.add(new TaskItem("Уборка территории, адм. корпус"
                , "12.02.2026 09:45"
                , "Северная"
                , "Низкий"
                , "completed"));
        allTasks.add(new TaskItem("Замена оборудования, цех №2"
                , "12.02.2026 16:20"
                , "Южная"
                , "Высокий"
                , "completed"));
        allTasks.add(new TaskItem("Осмотр путей, перегон 7"
                , "11.02.2026 11:00"
                , "Восточная"
                , "Средний"
                , "active"));
        allTasks.add(new TaskItem("Монтаж оборудования"
                , "10.02.2026 13:15"
                , "Западная"
                , "Высокий"
                , "completed"));
    }

    private void filterTasks(String filter) {
        filteredTasks.clear();

        for (TaskItem task : allTasks) {
            if (filter.equals("active") && "active".equals(task.status)) {
                filteredTasks.add(task);
            } else if (filter.equals("completed") && "completed".equals(task.status)) {
                filteredTasks.add(task);
            }
        }

        displayTasks();

        String message = filter.equals("active") ?
                "Активных заданий: "
                        + filteredTasks.size() :
                "Выполненных заданий: "
                        + filteredTasks.size();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void displayTasks() {
        tasksContainer.removeAllViews();

        if (filteredTasks.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Нет заданий");
            emptyView.setTextSize(16);
            emptyView.setTextColor(getColor(R.color.gray));
            emptyView.setPadding(0, 50, 0, 50);
            emptyView.setGravity(android.view.Gravity.CENTER);
            tasksContainer.addView(emptyView);
            return;
        }

        for (TaskItem task : filteredTasks) {
            View taskView = getLayoutInflater().inflate(R.layout.item_task_worker, null);

            TextView tvTitle = taskView.findViewById(R.id.tvTaskTitle);
            TextView tvDetails = taskView.findViewById(R.id.tvTaskDetails);
            Button btnAction = taskView.findViewById(R.id.btnStartTask);

            tvTitle.setText(task.title);
            tvDetails.setText(task.dateTime
                    + " | "
                    + task.brigade
                    + " | Приоритет: "
                    + task.priority);

            if ("active".equals(task.status)) {
                btnAction.setText("Начать работу");
                btnAction.setOnClickListener(v -> {
                    Intent intent = new Intent(WorkerTasksActivity.this
                            , WorkerCreateDocumentActivity.class);
                    intent.putExtra("task", task);
                    startActivity(intent);
                });
            } else {
                btnAction.setText("Просмотреть отчет");
                btnAction.setOnClickListener(v -> {
                    Toast.makeText(this, "Просмотр отчета: "
                            + task.title, Toast.LENGTH_SHORT).show();
                });
            }

            tasksContainer.addView(taskView);
        }
    }

    private void updateFilterButtons(String selected) {
        tvActive.setTextColor(getColor(R.color.gray));
        tvActive.setBackgroundColor(getColor(android.R.color.white));
        tvCompleted.setTextColor(getColor(R.color.gray));
        tvCompleted.setBackgroundColor(getColor(android.R.color.white));

        if ("active".equals(selected)) {
            tvActive.setTextColor(getColor(R.color.red));
        } else {
            tvCompleted.setTextColor(getColor(R.color.red));
        }
    }
}