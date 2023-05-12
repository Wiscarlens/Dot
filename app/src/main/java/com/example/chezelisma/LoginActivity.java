package com.example.chezelisma;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView loginMessage;
    private TextInputLayout emailLayout;
    private TextInputEditText email;
    private TextInputLayout passwordLayout;
    private TextInputEditText password;
    private CheckBox rememberMe;
    private Button loginButton;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginMessage = findViewById(R.id.loginMessage);
        emailLayout = findViewById(R.id.emailLayout);
        email = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.passwordLayout);
        password = findViewById(R.id.passwordText);
        rememberMe = findViewById(R.id.checkBoxRememberMe);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);

        // This string array hold the login page message in the string file.
        final String[] messages = {
                getResources().getString(R.string.login_successful),
                getResources().getString(R.string.login_failed),
                getResources().getString(R.string.password_empty),
                getResources().getString(R.string.email_empty),
                getResources().getString(R.string.valid_email),
                getResources().getString(R.string.required),
                getResources().getString(R.string.invalid_email),
                getResources().getString(R.string.no_internet)
        };

        password.addTextChangedListener(loginTextWatcher);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = checkEmptyEmail();
                emailLayout.setHelperText(message);
                emailLayout.setHelperTextEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Create an intent to open main page later
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);

        loginButton.setOnClickListener(v -> {
            String userEmail = email.getText().toString().toLowerCase().trim();
            String userPassword = password.getText().toString().trim();

            // Check if there is internet

            // Check if email field is empty or format is correct
            if (!userEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                if (!userPassword.isEmpty()) {
                    auth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(LoginActivity.this, messages[0], Toast.LENGTH_SHORT).show();
                                // Open main activity
                                startActivity(mainActivity);
                                finish();
                            }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, messages[1], Toast.LENGTH_SHORT).show());
                } else {
                    passwordLayout.setEndIconVisible(false);
                    password.setError(messages[2]);
                }
            } else if (userEmail.isEmpty()) {
                email.setError(messages[3]);
            } else {
                email.setError(messages[4]);
            }
        });
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String passwordInput = password.getText().toString().trim();

            String required = getResources().getString(R.string.required);

            if (passwordInput.isEmpty() && !loginButton.isEnabled()) {
                passwordLayout.setHelperText(required + "*");
                passwordLayout.setHelperTextEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String checkEmptyEmail() {
        String emailInput = email.getText().toString().trim();
        String message;

        String required = getResources().getString(R.string.required);
        String emailInvalid = getResources().getString(R.string.invalid_email);

        if (emailInput.isEmpty()) {
            message = required + "*";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            message = emailInvalid;
        } else {
            message = null;
        }

        return message;
    }
}