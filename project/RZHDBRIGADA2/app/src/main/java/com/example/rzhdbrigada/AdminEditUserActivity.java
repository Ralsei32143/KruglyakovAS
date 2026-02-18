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

public class AdminEditUserActivity extends AppCompatActivity {

    private EditText etName
            , etLogin
            , etPassword;
    private RadioGroup rgRole;
    private Button btnEdit
            , btnCancel;
    private UserItem currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);

        etName = findViewById(R.id.etName);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);

        currentUser = (UserItem) getIntent().getSerializableExtra("user");
        if (currentUser != null) {
            etName.setText(currentUser.name);
            etLogin.setText(currentUser.login);
            if ("Бригадир".equals(currentUser.role)) {
                rgRole.check(R.id.rbBrigadier);
            } else if ("Работник".equals(currentUser.role)) {
                rgRole.check(R.id.rbWorker);
            } else {
                rgRole.check(R.id.rbAdmin);
            }
        }

        btnEdit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String login = etLogin.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            int selectedId = rgRole.getCheckedRadioButtonId();
            RadioButton rb = findViewById(selectedId);
            String role = rb != null ? rb.getText().toString() : "";

            if (name.isEmpty() || login.isEmpty() || role.isEmpty()) {
                Toast.makeText(this
                        , "Заполните все поля"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.name = name;
            currentUser.login = login;
            currentUser.role = role;

            Intent intent = new Intent();
            intent.putExtra("edited_user", currentUser);
            setResult(RESULT_OK, intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}