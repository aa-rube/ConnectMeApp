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
    private TextView greeting;
    private EditText inputEmail;
    private EditText inputCode;
    private Button approveEmail;
    private Button approveCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        greeting = findViewById(R.id.greeting);
        inputEmail = findViewById(R.id.email_in);
        inputCode = findViewById(R.id.pass_in);
        approveEmail = findViewById(R.id.approve_email);
        approveCode = findViewById(R.id.approve_pass);

        hideAll();

        greeting.setOnClickListener(view -> {
            hideGreeting();
            showInputEmail();
        });

        approveEmail.setOnClickListener(view -> {
            if (inputEmail.getText().toString().contains("@")) {
                userEmail = inputEmail.getText().toString();
                authViewModel.authenticate(apiService, userEmail);
                showInputCode();
            }
        });

        approveCode.setOnClickListener(view -> {
            hideInputCode();
            authViewModel.init(apiService, CryptoKey.fullKey(userEmail, inputCode.getText().toString()));
        });

        observeViewModel();
    }

    private void observeViewModel() {
        authViewModel.getInitResult().observe(this, result -> {
            if (result.equals(true)) {
                showGreeting("Welcome!");
            } else {
                showGreeting("Try again");
            }
        });

        authViewModel.getAuthResult().observe(this, result -> {
            if (result.equals(false)) {

                authViewModel.getErrorMessage().observe(this, errorMessage -> {
                    showGreeting("Trouble with connection. Try again.");
                });

            } else {
                showGreeting("move on...");
            }
        });

    }

    private void showInputEmail() {
        inputEmail = findViewById(R.id.email_in);
        inputEmail.setVisibility(View.VISIBLE);
        inputEmail.requestFocus();

        approveEmail = findViewById(R.id.approve_email);
        approveEmail.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showGreeting(String text) {
        greeting = findViewById(R.id.greeting);
        greeting.setVisibility(View.VISIBLE);
        greeting.setText(text);


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showInputCode() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);

        inputCode = findViewById(R.id.pass_in);
        inputCode.setVisibility(View.VISIBLE);

        approveCode = findViewById(R.id.approve_pass);
        approveCode.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideAll() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);
        inputCode.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }

    private void hideGreeting() {
        greeting.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }

    private void hideInputCode() {
        inputCode.setVisibility(View.GONE);
        approveCode.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }
}