package com.aarube.connectme.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aarube.connectme.R;
import com.aarube.connectme.model.ApiInitService;
import com.aarube.connectme.services.MyApiClient;
import com.aarube.connectme.viewmodel.AuthViewModel;

public class CreateAccActivity extends AppCompatActivity {
    private final ApiInitService apiInit = MyApiClient.getApiClient().create(ApiInitService.class);
    private AuthViewModel authViewModel;
    private String userName, userEmail;
    private EditText inputName;
    TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_acc);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        inputName = findViewById(R.id.name_in);
        Button approveName = findViewById(R.id.approve_name);

        greeting = findViewById(R.id.reg);

        Intent intent = getIntent();
        if (intent.hasExtra("userEmail")) {
            userEmail = intent.getStringExtra("userEmail");
        }

        approveName.setOnClickListener(view -> {
            userName = inputName.getText().toString();
            authViewModel.createAccount(apiInit, userEmail, userName);
            observeCreate();
        });
    }

        private void observeCreate() {
        authViewModel.getCreateAccResult().observe(this, result -> {
            if (result != null && result) {
                authViewModel.authenticate(apiInit, userEmail);
                Intent codeInput = new Intent(this, EmailCodeInput.class);
                codeInput.putExtra("userEmail", userEmail);
                startActivity(codeInput);
            } else {
                greeting.setText("Try create again");
            }
        });
    }
}