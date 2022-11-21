package com.example.lab5;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class EnterScene extends Activity {
    TextView changeTextPrev;
    TextView errorMessage;
    Button mainBtn;
    EditText inputName;
    EditText inputPassword;
    User user;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enter_scene);

        changeTextPrev = findViewById(R.id.enterTEXT);
        mainBtn = findViewById(R.id.regenterBtn);

        inputName = findViewById(R.id.inputName);
        inputPassword = findViewById(R.id.inputPassword);

        errorMessage = findViewById(R.id.errorMessage);

        Intent intent = new Intent(this, InitScene.class);
        Bundle changeMode = this.getIntent().getExtras();

        int flag = changeMode.getInt("mode");
        Intent intent1 = new Intent(this, MainScene.class);

        if(flag == 1){
            changeTextPrev.setText("Регистрация");
            mainBtn.setText("Регистрация");

            mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().toString().trim().equals("") || inputPassword.getText().toString().trim().equals("")) {
                    errorMessage.setText("Заполните поля регистрации");
                    errorMessage.setVisibility(View.VISIBLE);
                }else {
                    user = new User(inputName.getText().toString().trim(), inputPassword.getText().toString().trim());
                    if(user.checkName(db)){
                        errorMessage.setText("Пользователь с таким именем уже существует!");
                        errorMessage.setVisibility(View.VISIBLE);
                    }else if(user.checkPass()){
                        errorMessage.setText("Пароль менее 8 символов!");
                        errorMessage.setVisibility(View.VISIBLE);
                    }else{
                        errorMessage.setText("Регистрация прошла успешно!");
                        errorMessage.setVisibility(View.VISIBLE);
                        Log.i("MESSAGE", "onClick: USER ADD");
                        db.addUser(user);
                        db.close();
                        intent1.putExtra("user", user);
                        startActivity(intent1);
                    }
                }
            }
        });
        }else {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User logUser = new User(inputName.getText().toString().trim(), inputPassword.getText().toString().trim());
                    if(logUser.getLogin().equals("") || logUser.getPass().equals("")) {
                        errorMessage.setText("Заполните поля!");
                        errorMessage.setVisibility(View.VISIBLE);
                    }else if(CheckUser(logUser)){
                        db.close();
                        intent1.putExtra("user", logUser);
                        startActivity(intent1);
                    }else{
                        errorMessage.setText("Неверный логин или пароль!");
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public boolean CheckUser(User user) {
        return db.selectUserData(user);
    }
}
