package com.module.dot.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.module.dot.Activities.Users.SignupFragment;
import com.module.dot.ForgotPasswordFragment;
import com.module.dot.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputLayout emailLayout;
    private TextInputEditText email;
    private TextInputLayout passwordLayout;
    private TextInputEditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        TextView loginMessage = findViewById(R.id.loginMessage);
        emailLayout = findViewById(R.id.emailLayout);
        email = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.passwordLayout);
        password = findViewById(R.id.passwordText);
        CheckBox rememberMe = findViewById(R.id.checkBoxRememberMe);
        loginButton = findViewById(R.id.loginButton);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        LinearLayout signUp = findViewById(R.id.signUpLoginLL);
        LinearLayout loginLL = findViewById(R.id.loginLL);
        FrameLayout loginContainerFL = findViewById(R.id.loginContainerFL);

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
            loginButton.setClickable(false);

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

            loginButton.setClickable(true);

        });

        forgotPassword.setOnClickListener(v -> {
            loginLL.setVisibility(LinearLayout.GONE);
            loginContainerFL.setVisibility(FrameLayout.VISIBLE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.loginContainerFL, new ForgotPasswordFragment())
                    .commit();
        });

        signUp.setOnClickListener(v -> {
            loginLL.setVisibility(LinearLayout.GONE);
            loginContainerFL.setVisibility(FrameLayout.VISIBLE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.loginContainerFL, new SignupFragment())
                    .commit();
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