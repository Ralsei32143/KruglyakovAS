package com.example.rzhdbrigada;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BrigadierListTasksActivity extends AppCompatActivity {

    private Button btnToday
            , btnWeek
            , btnMonth;
    private Button btnEdit
            , btnDelete;
    private LinearLayout tasksContainer;
    private TextView tvTotalCount
            , tvPendingCount
            , tvApprovedCount;

    private List<TaskItem> taskList = new ArrayList<>();
    private List<TaskItem> selectedTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigadier_list_tasks);

        btnToday = findViewById(R.id.btnToday);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        tasksContainer = findViewById(R.id.tasksContainer);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvApprovedCount = findViewById(R.id.tvApprovedCount);

        initTestData();
        displayTasks();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("new_task")) {
            TaskItem newTask = (TaskItem) intent.getSerializableExtra("new_task");
            if (newTask != null) {
                taskList.add(0, newTask);
                displayTasks();
                Toast.makeText(this, "Новое задание добавлено"
                        , Toast.LENGTH_SHORT).show();
            }
        }

        btnToday.setOnClickListener(v -> {
            resetPeriodButtons();
            btnToday.setTextColor(getColor(R.color.red));
            filterTasks("today");
        });

        btnWeek.setOnClickListener(v -> {
            resetPeriodButtons();
            btnWeek.setTextColor(getColor(R.color.red));
            filterTasks("week");
        });

        btnMonth.setOnClickListener(v -> {
            resetPeriodButtons();
            btnMonth.setTextColor(getColor(R.color.red));
            filterTasks("month");
        });

        btnEdit.setOnClickListener(v -> {
            selectedTasks.clear();
            int selectedIndex = -1;

            for (int i = 0; i < tasksContainer.getChildCount(); i++) {
                View taskView = tasksContainer.getChildAt(i);
                CheckBox checkBox = taskView.findViewById(R.id.cbTaskSelect);
                if (checkBox != null && checkBox.isChecked()) {
                    selectedTasks.add(taskList.get(i));
                    selectedIndex = i;
                }
            }

            if (selectedTasks.size() != 1) {
                Toast.makeText(this
                        , "Выберите одно задание для изменения"
                        , Toast.LENGTH_SHORT).show();
            } else {
                Intent editIntent = new Intent(BrigadierListTasksActivity.this
                        , BrigadierEditTaskActivity.class);
                editIntent.putExtra("edit_task", taskList.get(selectedIndex));
                startActivityForResult(editIntent, 2);
            }
        });

        btnDelete.setOnClickListener(v -> {
            selectedTasks.clear();
            for (int i = 0; i < tasksContainer.getChildCount(); i++) {
                View taskView = tasksContainer.getChildAt(i);
                CheckBox checkBox = taskView.findViewById(R.id.cbTaskSelect);
                if (checkBox != null && checkBox.isChecked()) {
                    selectedTasks.add(taskList.get(i));
                }
            }

            if (selectedTasks.isEmpty()) {
                Toast.makeText(this, "Выберите задания для удаления"
                        , Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Подтверждение")
                        .setMessage("Удалить выбранные задания?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            taskList.removeAll(selectedTasks);
                            displayTasks();
                            updateCounters();
                            Toast.makeText(this, "Задания удалены"
                                    , Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode
            , int resultCode
            , Intent data) {
        super.onActivityResult(requestCode
                , resultCode
                , data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("edited_task")) {
                TaskItem editedTask = (TaskItem) data.getSerializableExtra("edited_task");
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).title.equals(editedTask.title)) {
                        taskList.set(i, editedTask);
                        break;
                    }
                }
                displayTasks();
                Toast.makeText(this
                        , "Задание обновлено"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initTestData() {
        taskList.add(new TaskItem("Ремонт пути, участок 3"
                , "13.02.2026 14:30"
                , "Восточная"
                , "Высокий"));
        taskList.add(new TaskItem("Проверка состава, путь 5"
                , "13.02.2026 10:15"
                , "Западная"
                , "Низкий"));
        taskList.add(new TaskItem("Уборка территории, адм. корпус"
                , "12.02.2026 09:45"
                , "Северная"
                , "Низкий"));
        taskList.add(new TaskItem("Замена оборудования, цех №2"
                , "12.02.2026 16:20"
                , "Южная"
                , "Высокий"));

        for (TaskItem task : taskList) {
            task.status = "pending";
        }
    }

    private void displayTasks() {
        tasksContainer.removeAllViews();

        for (TaskItem task : taskList) {
            View taskView = getLayoutInflater().inflate(R.layout.item_task, null);

            TextView tvTitle = taskView.findViewById(R.id.tvTaskTitle);
            TextView tvDateTime = taskView.findViewById(R.id.tvDateTime);
            CheckBox cbSelect = taskView.findViewById(R.id.cbTaskSelect);

            tvTitle.setText(task.title);
            tvDateTime.setText(task.dateTime + " (" + task.priority + ")");

            tasksContainer.addView(taskView);
        }

        updateCounters();
    }

    private void updateCounters() {
        tvTotalCount.setText(String.valueOf(taskList.size()));
        tvPendingCount.setText(String.valueOf(taskList.size()));
        tvApprovedCount.setText("0");
    }

    private void filterTasks(String period) {
        displayTasks();
    }

    private void resetPeriodButtons() {
        btnToday.setTextColor(getColor(R.color.gray));
        btnWeek.setTextColor(getColor(R.color.gray));
        btnMonth.setTextColor(getColor(R.color.gray));
    }
}