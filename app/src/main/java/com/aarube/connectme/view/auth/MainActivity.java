package com.aarube.connectme.view.auth;

import com.aarube.connectme.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.aarube.connectme.model.ApiInitService;
import com.aarube.connectme.services.MyApiClient;
import com.aarube.connectme.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private final ApiInitService apiInit = MyApiClient.getApiClient().create(ApiInitService.class);
    private String userEmail;
    private EditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        TextView greeting = findViewById(R.id.greeting);
        inputEmail = findViewById(R.id.email_in);
        Button approveEmail = findViewById(R.id.approve_email);

        approveEmail.setOnClickListener(view -> {
            if (inputEmail.getText().toString().contains("@")) {
                userEmail = inputEmail.getText().toString();
                authViewModel.authenticate(apiInit, userEmail);
                observeAuth();
            }
        });
    }

    private void observeAuth() {
        authViewModel.getAuthResult().observe(this, result -> {

            if (result != null && result) {
                Intent codeInput = new Intent(this, EmailCodeInput.class);
                codeInput.putExtra("userEmail", userEmail);
                startActivity(codeInput);
            } else {
                Intent createAcc = new Intent(this, CreateAccActivity.class);
                createAcc.putExtra("userEmail", userEmail);
                startActivity(createAcc);
            }
        });
    }
}