package com.example.connectmeapp.view;

import com.example.connectmeapp.R;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.connectmeapp.model.ApiService;
import com.example.connectmeapp.model.CryptoKey;
import com.example.connectmeapp.services.MyApiClient;
import com.example.connectmeapp.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private final ApiService apiService = MyApiClient.getApiClient().create(ApiService.class);;
    private String userEmail;
    private String userName;
    private TextView greeting;

    private EditText inputEmail;
    private EditText inputCode;
    private EditText inputName;

    private Button approveEmail;
    private Button approveCode;
    private Button approveName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        greeting = findViewById(R.id.greeting);

        inputEmail = findViewById(R.id.email_in);
        inputCode = findViewById(R.id.pass_in);
        inputName = findViewById(R.id.name_in);

        approveEmail = findViewById(R.id.approve_email);
        approveCode = findViewById(R.id.approve_pass);
        approveName = findViewById(R.id.approve_name);

        hideAll();

        greeting.setOnClickListener(view -> {
            hideGreeting();
            showInputEmail();
        });

        approveEmail.setOnClickListener(view -> {
            if (inputEmail.getText().toString().contains("@")) {
                hideInputEmail();
                userEmail = inputEmail.getText().toString();
                authViewModel.authenticate(apiService, userEmail);
                observeAuth();
            }
        });

        approveCode.setOnClickListener(view -> {
            hideInputCode();
            authViewModel.init(apiService, CryptoKey.fullKey(userEmail, inputCode.getText().toString()));
            observeInit();
        });

        approveName.setOnClickListener(view -> {
            hideInputName();
            userName = inputName.getText().toString();
            authViewModel.createAccount(apiService, userEmail, userName);
            observeCreate();
        });
    }

    private void observeCreate() {
        authViewModel.getCreateAccResult().observe(this, result -> {
            if (result != null && result) {
                authViewModel.authenticate(apiService, userEmail);
                observeAuth();
            } else {
                showGreeting("Try create acc again");
            }
        });
    }

    private void observeAuth() {
        authViewModel.getAuthResult().observe(this, result -> {
            if (result != null && result) {
                showInputCode();
            } else {
                showInputName();
            }
        });
    }

    private void observeInit() {
        authViewModel.getInitResult().observe(this, result -> {
            if (result.equals(true)) {
                showGreeting("Welcome!");
            } else {
                showGreeting("Try again");
            }
        });
    }

    private void showInputName() {
        inputName = findViewById(R.id.name_in);
        inputName.setVisibility(View.VISIBLE);

        approveName = findViewById(R.id.approve_name);
        approveName.setVisibility(View.VISIBLE);
    }

    private void showInputEmail() {
        inputEmail = findViewById(R.id.email_in);
        inputEmail.setVisibility(View.VISIBLE);
        inputEmail.requestFocus();

        approveEmail = findViewById(R.id.approve_email);
        approveEmail.setVisibility(View.VISIBLE);
    }

    private void showGreeting(String text) {
        greeting = findViewById(R.id.greeting);
        greeting.setVisibility(View.VISIBLE);
        greeting.setText(text);
    }

    private void showInputCode() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);

        inputCode = findViewById(R.id.pass_in);
        inputCode.setVisibility(View.VISIBLE);

        approveCode = findViewById(R.id.approve_pass);
        approveCode.setVisibility(View.VISIBLE);

    }

    private void hideAll() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);

        inputCode.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);

        inputName.setVisibility(View.GONE);
        approveName.setVisibility(View.GONE);
    }


    private void hideInputName() {
        inputName.setVisibility(View.GONE);
        approveName.setVisibility(View.GONE);
    }

    private void hideInputEmail() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);
    }

    private void hideGreeting() {
        greeting.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);
    }

    private void hideInputCode() {
        inputCode.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);
    }
}