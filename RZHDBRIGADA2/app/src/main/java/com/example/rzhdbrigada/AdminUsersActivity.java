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

public class AdminUsersActivity extends AppCompatActivity {

    private LinearLayout usersContainer;
    private Button btnAdd, btnEdit, btnDelete;
    private List<UserItem> userList = new ArrayList<>();
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        usersContainer = findViewById(R.id.usersContainer);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        initTestData();
        displayUsers();

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminUsersActivity.this
                    , AdminCreateUserActivity.class);
            startActivityForResult(intent, 1);
        });

        btnEdit.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this
                        , "Выберите пользователя"
                        , Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(AdminUsersActivity.this
                        , AdminEditUserActivity.class);
                intent.putExtra("user"
                        , userList.get(selectedPosition));
                startActivityForResult(intent, 2);
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (selectedPosition == -1) {
                Toast.makeText(this
                        , "Выберите пользователя"
                        , Toast.LENGTH_SHORT).show();
            } else {
                userList.remove(selectedPosition);
                displayUsers();
                selectedPosition = -1;
                Toast.makeText(this
                        , "Пользователь удален"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTestData() {
        userList.add(new UserItem("Иванов Иван Иванович"
                , "Бригадир"
                , "ivanov_i"));
        userList.add(new UserItem("Петров Петр Петрович"
                , "Работник"
                , "petrov_p"));
        userList.add(new UserItem("Сидорова Анна Сергеевна"
                , "Администратор"
                , "sidorova_a"));
        userList.add(new UserItem("Козлов Дмитрий Николаевич"
                , "Работник"
                , "kozlov_d"));
        userList.add(new UserItem("Смирнова Елена Викторовна"
                , "Бригадир"
                , "smirnova_e"));
    }

    private void displayUsers() {
        usersContainer.removeAllViews();

        for (int i = 0; i < userList.size(); i++) {
            UserItem user = userList.get(i);
            LinearLayout userRow = new LinearLayout(this);
            userRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            userRow.setOrientation(LinearLayout.HORIZONTAL);
            userRow.setBackgroundColor(getColor(android.R.color.white));
            userRow.setPadding(12, 12, 12, 12);

            LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            marginParams.bottomMargin = 4;
            userRow.setLayoutParams(marginParams);

            TextView tvName = new TextView(this);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2
            ));
            tvName.setText(user.name);
            tvName.setTextColor(getColor(R.color.black));
            tvName.setTextSize(14);

            TextView tvRole = new TextView(this);
            tvRole.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            tvRole.setText(user.role);
            tvRole.setTextColor(getColor(R.color.black));
            tvRole.setTextSize(14);

            TextView tvLogin = new TextView(this);
            tvLogin.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            tvLogin.setText(user.login);
            tvLogin.setTextColor(getColor(R.color.black));
            tvLogin.setTextSize(14);

            userRow.addView(tvName);
            userRow.addView(tvRole);
            userRow.addView(tvLogin);

            int position = i;
            userRow.setOnClickListener(v -> {
                selectedPosition = position;
                for (int j = 0; j < usersContainer.getChildCount(); j++) {
                    usersContainer.getChildAt(j).setBackgroundColor(
                            j == position ? 0xFFE3F2FD : 0xFFFFFFFF
                    );
                }
            });

            usersContainer.addView(userRow);
        }
    }

    @Override
    protected void onActivityResult(int requestCode
            , int resultCode
            , Intent data) {
        super.onActivityResult(requestCode
                , resultCode
                , data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                UserItem newUser = (UserItem) data.getSerializableExtra("new_user");
                if (newUser != null) {
                    userList.add(newUser);
                    displayUsers();
                    Toast.makeText(this
                            , "Пользователь добавлен"
                            , Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2) {
                UserItem editedUser = (UserItem) data.getSerializableExtra("edited_user");
                if (editedUser != null && selectedPosition != -1) {
                    userList.set(selectedPosition, editedUser);
                    displayUsers();
                    Toast.makeText(this
                            , "Пользователь изменен"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}