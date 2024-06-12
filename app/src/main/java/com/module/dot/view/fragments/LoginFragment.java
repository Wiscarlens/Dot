package com.module.dot.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.module.dot.R;
import com.module.dot.utils.NetworkManager;
import com.module.dot.view.MainActivity;
import com.module.dot.view.utils.UIController;

public class LoginFragment extends Fragment {
    private TextInputLayout emailLayout;
    private TextInputEditText email;
    private TextInputLayout passwordLayout;
    private TextInputEditText password;
    private Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.enableNavigationViews(View.GONE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        emailLayout = view.findViewById(R.id.emailLayout);
        email = view.findViewById(R.id.email);
        passwordLayout = view.findViewById(R.id.passwordLayout);
        password = view.findViewById(R.id.passwordText);
        loginButton = view.findViewById(R.id.loginButton);
        TextView forgotPassword = view.findViewById(R.id.forgotPassword);
        LinearLayout signUp = view.findViewById(R.id.signUpLoginLL);

        UIController uiController = new UIController(requireActivity());

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


        loginButton.setOnClickListener(v -> {
            loginButton.setClickable(false);

            String userEmail = String.valueOf(email.getText()).toLowerCase().trim();
            String userPassword = String.valueOf(password.getText());

            // Check if email field is empty or format is correct
            if (!userEmail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                if (!userPassword.isEmpty()) {
                    // Check if there is internet connection
                    if (!NetworkManager.isNetworkAvailable(requireContext())) {
                        Toast.makeText(getContext(), messages[7], Toast.LENGTH_SHORT).show();
                        loginButton.setClickable(true);
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(getContext(), messages[0], Toast.LENGTH_SHORT).show();
                                // Open main login fragment

                                uiController.changeFragment(new HomeFragment());
                            }).addOnFailureListener(e -> Toast.makeText(getContext(), messages[1], Toast.LENGTH_SHORT).show());
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

        forgotPassword.setOnClickListener(v -> uiController.openBottomSheet(new ForgotPasswordFragment()));

        signUp.setOnClickListener(v -> uiController.openBottomSheet(new SignupFragment()));

    }

    private final TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String passwordInput = String.valueOf(password.getText());

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
        String emailInput = String.valueOf(email.getText()).toLowerCase().trim();
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