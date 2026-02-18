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

public class BrigadierCreateTaskActivity extends AppCompatActivity {

    private Button btnSelectPlace
            , btnSelectBrigade
            , btnSelectTaskType
            , btnSelectDate
            , btnCreate;
    private EditText etDescription;
    private TextView tvCounter;

    private String selectedBrigade = "";
    private String selectedTaskType = "";
    private String selectedPriority = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigadier_create_task);

        btnSelectPlace = findViewById(R.id.btnSelectPlace);
        btnSelectBrigade = findViewById(R.id.btnSelectBrigade);
        btnSelectTaskType = findViewById(R.id.btnSelectTaskType);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnCreate = findViewById(R.id.btnCreate);
        etDescription = findViewById(R.id.etDescription);
        tvCounter = findViewById(R.id.tvCounter);

        etDescription.addTextChangedListener(new android.text.TextWatcher() {
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

        btnSelectPlace.setOnClickListener(v ->
                Toast.makeText(this
                        , "Выбор места будет позже"
                        , Toast.LENGTH_SHORT).show());

        btnSelectBrigade.setOnClickListener(v -> showBrigadeDialog());
        btnSelectTaskType.setOnClickListener(v -> showTaskTypeDialog());

        btnSelectDate.setOnClickListener(v ->
                Toast.makeText(this
                        , "Выбор даты будет позже"
                        , Toast.LENGTH_SHORT).show());

        // ========== ИЗМЕНЕНО: кнопка создания ==========
        btnCreate.setOnClickListener(v -> {
            if (selectedBrigade.isEmpty()
                    || selectedTaskType.isEmpty()
                    || selectedPriority.isEmpty()
                    || etDescription.getText().toString().trim().isEmpty()) {
                Toast.makeText(this
                        , "Заполните все поля"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            TaskItem newTask = new TaskItem(
                    selectedTaskType + ": " + etDescription.getText().toString(),
                    "Только что",
                    selectedBrigade,
                    selectedPriority
            );

            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_task", newTask);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this
                    , "Задание создано!"
                    , Toast.LENGTH_SHORT).show();
            finish();
        });

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
            btnPriorityLow.setBackgroundTintList
                    (getColorStateList(android.R.color.holo_green_light));
        });

        btnPriorityHigh.setOnClickListener(v -> {
            selectedPriority = "Высокий";
            resetPriorityButtons(btnPriorityLow, btnPriorityHigh);
            btnPriorityHigh.setBackgroundTintList
                    (getColorStateList(android.R.color.holo_red_light));
        });

        btnConfirm.setOnClickListener(v -> {
            if (selectedTaskType.isEmpty() || selectedPriority.isEmpty()) {
                Toast.makeText(this
                        , "Выберите тип задания и приоритет"
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