package com.example.rzhdbrigada;

import android.app.AlertDialog;
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

public class BrigadierReportActivity extends AppCompatActivity {

    private Button btnToday
            , btnWeek
            , btnMonth;
    private Button btnExportPdf;
    private LinearLayout reportsContainer;
    private TextView tvTotalCount
            , tvPendingCount
            , tvApprovedCount;

    private List<ReportItem> reportList = new ArrayList<>();
    private List<ReportItem> selectedReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigadier_report);

        btnToday = findViewById(R.id.btnToday);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);
        btnExportPdf = findViewById(R.id.btnExportPdf);
        reportsContainer = findViewById(R.id.reportsContainer);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvApprovedCount = findViewById(R.id.tvApprovedCount);

        initTestData();
        displayReports();

        btnToday.setOnClickListener(v -> {
            resetPeriodButtons();
            btnToday.setTextColor(getColor(R.color.red));
            btnToday.setBackgroundTintList(getColorStateList(android.R.color.white));
            filterReports("today");
        });

        btnWeek.setOnClickListener(v -> {
            resetPeriodButtons();
            btnWeek.setTextColor(getColor(R.color.red));
            btnWeek.setBackgroundTintList(getColorStateList(android.R.color.white));
            filterReports("week");
        });

        btnMonth.setOnClickListener(v -> {
            resetPeriodButtons();
            btnMonth.setTextColor(getColor(R.color.red));
            btnMonth.setBackgroundTintList(getColorStateList(android.R.color.white));
            filterReports("month");
        });


        btnExportPdf.setOnClickListener(v -> {
            selectedReports.clear();

            for (int i = 0; i < reportsContainer.getChildCount(); i++) {
                View reportView = reportsContainer.getChildAt(i);
                CheckBox checkBox = reportView.findViewById(R.id.cbReportSelect);
                if (checkBox != null && checkBox.isChecked()) {
                    selectedReports.add(reportList.get(i));
                }
            }

            if (selectedReports.isEmpty()) {
                Toast.makeText(this
                        , "Выберите отчеты для экспорта"
                        , Toast.LENGTH_SHORT).show();
            } else {
                // Диалог подтверждения экспорта
                new AlertDialog.Builder(this)
                        .setTitle("Экспорт в PDF")
                        .setMessage("Экспортировать выбранные отчеты ("
                                + selectedReports.size()
                                + " шт.)?")
                        .setPositiveButton("Экспортировать"
                                , (dialog, which) -> {

                            StringBuilder reportNames = new StringBuilder();
                            for (ReportItem report : selectedReports) {
                                reportNames.append(report.title).append(", ");
                            }
                            Toast.makeText(this
                                    , "Экспортировано: " + reportNames.toString()
                                    , Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
            }
        });
    }

    // Тестовые данные
    private void initTestData() {
        reportList.add(new ReportItem("Ремонт пути, участок 3"
                , "13.02.2026 14:30"
                , "pending"));
        reportList.add(new ReportItem("Проверка состава, путь 5"
                , "13.02.2026 10:15"
                , "pending"));
        reportList.add(new ReportItem("Уборка территории, адм. корпус"
                , "12.02.2026 09:45"
                , "approved"));
        reportList.add(new ReportItem("Замена оборудования, цех №2"
                , "12.02.2026 16:20"
                , "approved"));
        reportList.add(new ReportItem("Осмотр путей, перегон 7"
                , "11.02.2026 11:00"
                , "pending"));
        reportList.add(new ReportItem("Монтаж оборудования, цех №1"
                , "10.02.2026 13:15"
                , "approved"));
    }

    private void displayReports() {
        reportsContainer.removeAllViews();

        for (ReportItem report : reportList) {
            View reportView = getLayoutInflater().inflate(R.layout.item_report
                    , null);

            TextView tvTitle = reportView.findViewById(R.id.tvReportTitle);
            TextView tvDateTime = reportView.findViewById(R.id.tvReportDateTime);
            CheckBox cbSelect = reportView.findViewById(R.id.cbReportSelect);

            tvTitle.setText(report.title);
            tvDateTime.setText(report.dateTime);

            if ("approved".equals(report.status)) {
                tvTitle.setTextColor(getColor(android.R.color.holo_green_dark));
            }

            reportsContainer.addView(reportView);
        }

        updateCounters();
    }

    private void updateCounters() {
        int total = reportList.size();
        int pending = 0;
        int approved = 0;

        for (ReportItem report : reportList) {
            if ("pending".equals(report.status)) pending++;
            else if ("approved".equals(report.status)) approved++;
        }

        tvTotalCount.setText(String.valueOf(total));
        tvPendingCount.setText(String.valueOf(pending));
        tvApprovedCount.setText(String.valueOf(approved));
    }

    private void filterReports(String period) {
        String periodText = "";
        switch (period) {
            case "today": periodText = "сегодня"; break;
            case "week": periodText = "за неделю"; break;
            case "month": periodText = "за месяц"; break;
        }
        Toast.makeText(this, "Фильтр: " + periodText, Toast.LENGTH_SHORT).show();
        displayReports();
    }

    private void resetPeriodButtons() {
        btnToday.setTextColor(getColor(R.color.gray));
        btnWeek.setTextColor(getColor(R.color.gray));
        btnMonth.setTextColor(getColor(R.color.gray));
    }

    private class ReportItem {
        String title;
        String dateTime;
        String status; // "pending" или "approved"

        ReportItem(String title, String dateTime, String status) {
            this.title = title;
            this.dateTime = dateTime;
            this.status = status;
        }
    }
}