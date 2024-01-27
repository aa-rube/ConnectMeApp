package com.aarube.connectme.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aarube.connectme.R;
import com.aarube.connectme.model.ApiChatService;
import com.aarube.connectme.model.ApiInitService;
import com.aarube.connectme.model.CryptoKey;
import com.aarube.connectme.services.MyApiClient;
import com.aarube.connectme.view.chat.ChatMainActivity;
import com.aarube.connectme.viewmodel.AuthViewModel;
import com.aarube.connectme.viewmodel.ChatViewModule;

public class EmailCodeInput extends AppCompatActivity {
    private final ApiInitService apiInit = MyApiClient.getApiClient().create(ApiInitService.class);
    private AuthViewModel authViewModel;
    private String secret, userEmail;
    private EditText inputCode;
    private TextView greeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_input);

        greeting = findViewById(R.id.find_email);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        Button approveCode = findViewById(R.id.approve_pass);
        inputCode = findViewById(R.id.code_in);

        Intent intent = getIntent();
        if (intent.hasExtra("userEmail")) {
            userEmail = intent.getStringExtra("userEmail");
        }

        approveCode.setOnClickListener(view -> {
            secret = CryptoKey.fullKey(userEmail, inputCode.getText().toString());
            authViewModel.init(apiInit, secret);
            observeInit();
        });
    }

    private void observeInit() {
        authViewModel.getInitResult().observe(this, result -> {
            if (result.equals(true)) {
                greeting.setText("Success!");
                startChat();
            } else {
                greeting.setText("Fuck (");
            }
        });
    }

    private void startChat() {
        authViewModel.getSessionId().observe(this, result -> {
            Intent chat = new Intent(this, ChatMainActivity.class);
            chat.putExtra("id", result);
            startActivity(chat);
        });
    }

}