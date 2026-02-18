package com.example.rzhdbrigada;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BrigadierEditTaskActivity extends AppCompatActivity {

    private Button btnSelectBrigade
            , btnSelectTaskType
            , btnEdit
            , btnCancel;
    private EditText etAdditionalInfo;
    private TextView tvCounter;

    private String selectedBrigade = "";
    private String selectedTaskType = "";
    private String selectedPriority = "";
    private TaskItem currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigadier_edit_task);

        btnSelectBrigade = findViewById(R.id.btnSelectBrigade);
        btnSelectTaskType = findViewById(R.id.btnSelectTaskType);
        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        tvCounter = findViewById(R.id.tvCounter);

        // Получаем данные выбранного задания
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("edit_task")) {
            currentTask = (TaskItem) intent.getSerializableExtra("edit_task");
            loadTaskData();
        } else {
            Toast.makeText(this
                    , "Ошибка: задание не найдено"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        // Счетчик символов
        etAdditionalInfo.addTextChangedListener(new android.text.TextWatcher() {
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

        btnSelectBrigade.setOnClickListener(v -> showBrigadeDialog());
        btnSelectTaskType.setOnClickListener(v -> showTaskTypeDialog());

        btnEdit.setOnClickListener(v -> {
            if (selectedBrigade.isEmpty()) {
                Toast.makeText(this
                        , "Выберите бригаду"
                        , Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTaskType.isEmpty() || selectedPriority.isEmpty()) {
                Toast.makeText(this
                        , "Выберите тип задания и приоритет"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            currentTask.title = selectedTaskType + ": " + etAdditionalInfo.getText().toString();
            currentTask.brigade = selectedBrigade;
            currentTask.priority = selectedPriority;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("edited_task"
                    , currentTask);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this
                    , "Задание изменено!"
                    , Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadTaskData() {
        if (currentTask != null) {
            String[] parts = currentTask.title.split(":");
            if (parts.length > 0) {
                selectedTaskType = parts[0].trim();
                btnSelectTaskType.setText(selectedTaskType + " (" + currentTask.priority + ")");
            }

            selectedBrigade = currentTask.brigade;
            btnSelectBrigade.setText(selectedBrigade);
            selectedPriority = currentTask.priority;

            if (parts.length > 1) {
                etAdditionalInfo.setText(parts[1].trim());
                tvCounter.setText(parts[1].trim().length() + "/1000");
            }
        }
    }

    private void showBrigadeDialog() {
        String[] brigades = {
                "Восточная бригада (5 чел)",
                "Западная бригада (4 чел)",
                "Северная бригада (6 чел)",
                "Южная бригада (3 чел)"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите бригаду")
                .setItems(brigades, (dialog, which) -> {
                    selectedBrigade = brigades[which];
                    btnSelectBrigade.setText(selectedBrigade);
                })
                .setNegativeButton("Отмена"
                        , (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showTaskTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Тип задания и приоритет");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_task_type, null);
        builder.setView(dialogView);

        Button btnTaskType1 = dialogView.findViewById(R.id.btnTaskType1);
        Button btnTaskType2 = dialogView.findViewById(R.id.btnTaskType2);
        Button btnTaskType3 = dialogView.findViewById(R.id.btnTaskType3);
        Button btnTaskType4 = dialogView.findViewById(R.id.btnTaskType4);
        Button btnPriorityLow = dialogView.findViewById(R.id.btnPriorityLow);
        Button btnPriorityHigh = dialogView.findViewById(R.id.btnPriorityHigh);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        AlertDialog dialog = builder.create();

        resetTypeButtons(btnTaskType1, btnTaskType2, btnTaskType3, btnTaskType4);
        resetPriorityButtons(btnPriorityLow, btnPriorityHigh);

        btnTaskType1.setOnClickListener(v -> {
            selectedTaskType = "Обход и осмотр";
            resetTypeButtons(btnTaskType1, btnTaskType2, btnTaskType3, btnTaskType4);
            btnTaskType1.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        });

        btnTaskType2.setOnClickListener(v -> {
            selectedTaskType = "Ремонт";
            resetTypeButtons(btnTaskType1, btnTaskType2, btnTaskType3, btnTaskType4);
            btnTaskType2.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        });

        btnTaskType3.setOnClickListener(v -> {
            selectedTaskType = "Монтаж оборудования";
            resetTypeButtons(btnTaskType1, btnTaskType2, btnTaskType3, btnTaskType4);
            btnTaskType3.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        });

        btnTaskType4.setOnClickListener(v -> {
            selectedTaskType = "Уборка";
            resetTypeButtons(btnTaskType1, btnTaskType2, btnTaskType3, btnTaskType4);
            btnTaskType4.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_light));
        });

        btnPriorityLow.setOnClickListener(v -> {
            selectedPriority = "Низкий";
            resetPriorityButtons(btnPriorityLow, btnPriorityHigh);
            btnPriorityLow.setBackgroundTintList(getColorStateList
                    (android.R.color.holo_green_light));
        });

        btnPriorityHigh.setOnClickListener(v -> {
            selectedPriority = "Высокий";
            resetPriorityButtons(btnPriorityLow, btnPriorityHigh);
            btnPriorityHigh.setBackgroundTintList(getColorStateList
                    (android.R.color.holo_red_light));
        });

        btnConfirm.setOnClickListener(v -> {
            if (selectedTaskType.isEmpty() || selectedPriority.isEmpty()) {
                Toast.makeText(this
                        , "Выберите тип и приоритет"
                        , Toast.LENGTH_SHORT).show();
            } else {
                btnSelectTaskType.setText(selectedTaskType + " (" + selectedPriority + ")");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void resetTypeButtons(Button... buttons) {
        for (Button btn : buttons) {
            btn.setBackgroundTintList(getColorStateList(android.R.color.white));
        }
    }

    private void resetPriorityButtons(Button... buttons) {
        for (Button btn : buttons) {
            btn.setBackgroundTintList(getColorStateList(android.R.color.white));
        }
    }
}