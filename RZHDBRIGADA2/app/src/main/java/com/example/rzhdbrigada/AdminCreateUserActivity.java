package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminCreateUserActivity extends AppCompatActivity {

    private EditText etName
            , etLogin
            , etPassword;
    private RadioGroup rgRole;
    private Button btnCreate
            , btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_user);

        etName = findViewById(R.id.etName);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            int selectedId = rgRole.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);
            String role = rb != null ? rb.getText().toString() : "";

            if (name.isEmpty() || login.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this
                        , "Заполните все поля"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            UserItem newUser = new UserItem(name, role, login);
            Intent intent = new Intent();
            intent.putExtra("new_user", newUser);
            setResult(RESULT_OK, intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}