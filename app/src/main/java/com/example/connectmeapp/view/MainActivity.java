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


import com.example.connectmeapp.viewmodel.AuthViewModel;


public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private String userEmail;
    private TextView greeting;
    private EditText inputEmail;
    private EditText inputPass;
    private Button approveEmail;
    private Button approvePass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        greeting = findViewById(R.id.greeting);
        inputEmail = findViewById(R.id.email_in);
        inputPass = findViewById(R.id.pass_in);
        approveEmail = findViewById(R.id.approve_email);
        approvePass = findViewById(R.id.approve_pass);

        hideAll();

        greeting.setOnClickListener(view -> {
            hideGreeting();
            showInputEmail();
        });

        approveEmail.setOnClickListener(view -> {
            if (inputEmail.getText().toString().contains("@")) {
                showInputPassword();
                userEmail = inputEmail.getText().toString();
            }
        });

        approvePass.setOnClickListener(view -> {
            hideInputPassword();
            authViewModel.authenticate(userEmail, inputPass.getText().toString());
        });

        observeViewModel();
    }


    private void observeViewModel() {
        authViewModel.getAuthenticationResult().observe(this, result -> {
            if (result.equals(true)) {
                showGreeting("Welcome!");
            } else {
                showGreeting("Try again");
            }
        });

        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            showGreeting("Trouble with connection. Try again.");
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

    private void showInputPassword() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);

        inputPass = findViewById(R.id.pass_in);
        inputPass.setVisibility(View.VISIBLE);

        approvePass = findViewById(R.id.approve_pass);
        approvePass.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideAll() {
        inputEmail.setVisibility(View.GONE);
        approveEmail.setVisibility(View.GONE);
        inputPass.setVisibility(View.GONE);
        approvePass.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }

    private void hideGreeting() {
        greeting.setVisibility(View.GONE);
        approvePass.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }

    private void hideInputPassword() {
        inputPass.setVisibility(View.GONE);
        approvePass.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
    }
}