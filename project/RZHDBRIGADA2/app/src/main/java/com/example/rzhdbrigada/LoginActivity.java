package com.example.rzhdbrigada;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText editLogin
            , editPassword;
    private Button btnLogin;
    private TextView txtForgotPassword;
    private Map<String, User> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");

        try {
            setContentView(R.layout.activity_login);
            Log.d(TAG, "setContentView completed");
        } catch (Exception e) {
            Log.e(TAG, "Error in setContentView: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this
                    , "Ошибка загрузки layout: " + e.getMessage()
                    , Toast.LENGTH_LONG).show();
            return;
        }

        try {
            editLogin = findViewById(R.id.editLogin);
            editPassword = findViewById(R.id.editPassword);
            btnLogin = findViewById(R.id.btnLogin);
            txtForgotPassword = findViewById(R.id.txtForgotPassword);

            Log.d(TAG, "findViewById completed");

            if (editLogin == null) Log.e(TAG, "editLogin is NULL");
            if (editPassword == null) Log.e(TAG, "editPassword is NULL");
            if (btnLogin == null) Log.e(TAG, "btnLogin is NULL");
            if (txtForgotPassword == null) Log.e(TAG, "txtForgotPassword is NULL");

        } catch (Exception e) {
            Log.e(TAG, "Error in findViewById: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this
                    , "Ошибка инициализации полей"
                    , Toast.LENGTH_LONG).show();
            return;
        }

        initUsers();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login button clicked");

                String login = editLogin.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                Log.d(TAG, "Login entered: " + login);
                Log.d(TAG, "Password length: " + password.length());

                if (login.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this
                            , "Заполните все поля"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }

                performLogin(login, password);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this
                        , "Обратитесь к администратору"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "onCreate completed successfully");
    }

    private void initUsers() {
        Log.d(TAG, "initUsers started");
        users.put("brigadir", new User("brigadir"
                , "123"
                , "brigadier"));
        users.put("worker", new User("worker"
                , "123"
                , "worker"));
        users.put("admin", new User("admin"
                , "123"
                , "admin"));
        users.put("petrov", new User("petrov"
                , "pass"
                , "worker"));
        users.put("ivanov", new User("ivanov"
                , "pass"
                , "brigadier"));
        Log.d(TAG, "Users added: " + users.size());
    }

    private void performLogin(String login, String password) {
        Log.d(TAG, "performLogin started for login: " + login);

        try {
            User user = users.get(login);

            if (user == null) {
                Log.e(TAG, "User not found: " + login);
                Toast.makeText(this
                        , "Пользователь не найден"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "User found. Role: " + user.role);

            if (!user.password.equals(password)) {
                Log.e(TAG, "Wrong password for: " + login);
                Toast.makeText(this
                        , "Неверный пароль"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Password correct, creating intent for role: " + user.role);

            Intent intent = null;

            switch (user.role) {
                case "brigadier":
                    Log.d(TAG, "Navigating to BrigadierMenuActivity");
                    intent = new Intent(LoginActivity.this
                            , BrigadierMenuActivity.class);
                    break;
                case "worker":
                    Log.d(TAG, "Navigating to WorkerMenuActivity");
                    intent = new Intent(LoginActivity.this
                            , WorkerMenuActivity.class);
                    break;
                case "admin":
                    Log.d(TAG, "Navigating to AdminActivity");
                    intent = new Intent(LoginActivity.this
                            , AdminActivity.class);
                    break;
                default:
                    Log.e(TAG, "Unknown role: " + user.role);
                    Toast.makeText(this
                            , "Неизвестная роль: " + user.role
                            , Toast.LENGTH_LONG).show();
                    return;
            }

            if (intent != null) {
                Log.d(TAG, "Starting activity");
                startActivity(intent);
                Log.d(TAG, "Activity started, finishing LoginActivity");
                finish();
            } else {
                Log.e(TAG, "Intent is null");
                Toast.makeText(this
                        , "Ошибка создания перехода"
                        , Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION in performLogin: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this
                    , "Ошибка: " + e.getMessage()
                    , Toast.LENGTH_LONG).show();
        }
    }

    // Внутренний класс пользователя
    private class User {
        String login;
        String password;
        String role;

        User(String login, String password, String role) {
            this.login = login;
            this.password = password;
            this.role = role;
        }
    }
}