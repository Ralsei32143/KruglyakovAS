package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WorkerMenuActivity extends AppCompatActivity {

    private Button btnTasks
            , btnCreateDocument
            , btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);

        btnTasks = findViewById(R.id.btnTasks);
        btnCreateDocument = findViewById(R.id.btnCreateDocument);
        btnLogout = findViewById(R.id.btnLogout);

        btnTasks.setOnClickListener(v -> {
            Intent intent = new Intent(WorkerMenuActivity.this
                    , WorkerTasksActivity.class);
            startActivity(intent);
        });

        btnCreateDocument.setOnClickListener(v -> {
            Intent intent = new Intent(WorkerMenuActivity.this
                    , WorkerCreateDocumentActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(WorkerMenuActivity.this
                    , LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}