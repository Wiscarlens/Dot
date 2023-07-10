package com.example.chezelisma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

public class SignUpFragment extends Fragment {
    private FirebaseAuth auth;

    private ImageButton step1Button;
    private ImageButton step2Button;
    private ImageButton step3Button;
    private View contactLeft;
    private View contactRight;
    private View OtherRight;
    private TextView stepTwoTextView;
    private TextView stepThreeTextView;
    private ProgressBar progressBar;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private FrameLayout stepContentContainer;
    private View stepOneLayout;
    private View stepTwoLayout;
    private View stepThreeLayout;
    private int currentStep = 1; // track step for next and previous button

    // Declare Form part one field
    private TextInputEditText firstName;
    private TextInputEditText middleName;
    private TextInputEditText lastName;
    private TextInputEditText DOB;
    private TextInputLayout DOB_layout;
    private TextInputEditText gender;

    // Declare Form part two field
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText streetName;
    private TextInputEditText city;
    private TextInputEditText state;
    private TextInputEditText zipCode;

    // Declare Form part two field
    private ImageView profileImage;
    private TextInputEditText position;
    private TextInputEditText password;
    private TextInputLayout passwordLayout;
    private TextInputEditText confirmedPassword;
    private TextInputLayout confirmedPasswordLayout;

    private Button saveButton;

    private String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        progressBar = view.findViewById(R.id.progress_horizontal);

        step1Button = view.findViewById(R.id.stepOneButton);
        step2Button = view.findViewById(R.id.stepTwoButton);
        step3Button = view.findViewById(R.id.stepThreeButton);

        contactLeft = view.findViewById(R.id.contactLeftView);
        contactRight = view.findViewById(R.id.contactRightView);
        OtherRight = view.findViewById(R.id.OtherRightView);

        stepTwoTextView = view.findViewById(R.id.stepTwoTextView);
        stepThreeTextView = view.findViewById(R.id.stepThreeTextView);

        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);
        saveButton = view.findViewById(R.id.saveButton);

        stepContentContainer = view.findViewById(R.id.stepContentContainer);

        // Steps layout
        stepOneLayout = LayoutInflater.from(getContext()).inflate(R.layout.form_step_one, null);
        stepTwoLayout = LayoutInflater.from(getContext()).inflate(R.layout.form_step_two, null);
        stepThreeLayout = LayoutInflater.from(getContext()).inflate(R.layout.form_step_three, null);

        // Step one form field
        firstName = stepOneLayout.findViewById(R.id.signupFirstNameText);
        middleName = stepOneLayout.findViewById(R.id.signupMiddleNameText);
        lastName = stepOneLayout.findViewById(R.id.signupLastNameText);
        DOB_layout = stepOneLayout.findViewById(R.id.signupDOBLayout);
        DOB = stepOneLayout.findViewById(R.id.signupDOBText);
        gender = stepOneLayout.findViewById(R.id.signupGenderText);

        // Step two form field
        email = stepTwoLayout.findViewById(R.id.signupEmailText);
        phoneNumber = stepTwoLayout.findViewById(R.id.signupPhoneNumberText);;
        streetName = stepTwoLayout.findViewById(R.id.signupStreetNameText);
        city = stepTwoLayout.findViewById(R.id.signupCityText);
        state = stepTwoLayout.findViewById(R.id.signupStateText);
        zipCode = stepTwoLayout.findViewById(R.id.signupZipCodeText);

        // Step three form field
        profileImage = stepThreeLayout.findViewById(R.id.newProfileImage);
        position = stepThreeLayout.findViewById(R.id.signupPositionText);
        password = stepThreeLayout.findViewById(R.id.signupPasswordText);
        passwordLayout = stepThreeLayout.findViewById(R.id.signupPasswordLayout);
        confirmedPassword = stepThreeLayout.findViewById(R.id.signupConfirmPasswordText);

        // This field a copy of the error message from the String resources file.
        message = getResources().getString(R.string.field_empty);

        // When user click Calendar icon
        DOB_layout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });


        // Progress bar default value
        progressBar.setProgress(33);

        step1Button.setOnClickListener(v -> {
            // Handle Step 1 button click
            showStepContent(1);
        });

        step2Button.setOnClickListener(v -> {
            // Handle Step 2 button click
            showStepContent(2);
        });

        step3Button.setOnClickListener(v -> {
            // Handle Step 3 button click
            showStepContent(3);
        });

        previousButton.setOnClickListener(v -> {
            currentStep--; // Decrease currentStep by 1
            showStepContent(currentStep);
        });

        nextButton.setOnClickListener(v -> {
            currentStep++; // Increase currentStep by 1
            showStepContent(currentStep);
        });

        // Show the initial step content
        showStepContent(currentStep);

        saveButton.setOnClickListener(v -> {
            showDialogMessage(); // Test Line


//            String userPosition = position.getText().toString().trim();
//            String userFirstName = firstName.getText().toString().trim();
//            String userLastName = lastName.getText().toString().trim();
//            String userEmail = email.getText().toString().toLowerCase().trim();
//            String userPassword = password.getText().toString();
//            String userConfirmPassword = confirmedPassword.getText().toString();
//
//            // This field a copy of the error message from the String resources file.
//            String message = getResources().getString(R.string.field_empty);
//
//            if(userPosition.isEmpty()){
//                position.setError(message);
//            }
//
//            if(userFirstName.isEmpty()){
//                firstName.setError(message);
//            }
//
//            if(userLastName.isEmpty()){
//                lastName.setError(message);
//            }
//
//            if(userEmail.isEmpty()){
//                email.setError(message);
//            }
//
//            if(userPassword.isEmpty()){
//                // Hide password toggle so it does go over the error message
//                passwordLayout.setEndIconVisible(false);
//                password.setError(getResources().getString(R.string.password_empty));
//            }
//
//            if(userConfirmPassword.isEmpty()){
//                // Hide password toggle so does go over the error message
//                confirmedPasswordLayout.setEndIconVisible(false);
//                confirmedPassword.setError(message);
//            }
//
//
//            else {
//                if(!userConfirmPassword.equals(userPassword)){
//                    // Hide password toggle so does go over the error message
//                    confirmedPasswordLayout.setEndIconVisible(false);
//
//                    message = getResources().getString(R.string.password_not_same);
//                    confirmedPassword.setError(message);
//
//                } else {
//                    auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener
//                            (new OnCompleteListener<AuthResult>() {
//                        String toastMessage = "";
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                toastMessage = getResources().getString(R.string.signup_Successful);
//                                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getContext(), MainActivity.class));
//                            } else{
//                                toastMessage = getResources().getString(R.string.signup_failed);
//                                Toast.makeText(getContext(), toastMessage + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//
//            }
        });

    }

    private void showStepContent(int step) {
        // Default Color
        int colorYellow = ContextCompat.getColor(getContext(), R.color.dark_yellow);
        int colorGray = ContextCompat.getColor(getContext(), R.color.light_gray);
        int colorWhite = ContextCompat.getColor(getContext(), R.color.black);

        if (step < 1) {
            step = 1;
        }

        else if(step > 3) {
            step = 3;
        }

        // Set active button background
        switch (step) {
            case 1:
                stepContentContainer.removeAllViews();
                stepContentContainer.addView(stepOneLayout);

                currentStep = 1; // Reset currentStep value to one

                step1Button.setClickable(false); // Make step1Button not clickable
                step2Button.setClickable(true); // Make step2Button clickable
                step3Button.setClickable(true); // Make step3Button clickable

                previousButton.setClickable(false); // Make previousButton not clickable
                nextButton.setClickable(true); // Make nextButton clickable

                // Reset other views colors to gray
                contactLeft.setBackgroundColor(colorGray);
                step2Button.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                stepTwoTextView.setTextColor(colorGray);
                contactRight.setBackgroundColor(colorGray);
                step3Button.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                stepThreeTextView.setTextColor(colorGray);
                OtherRight.setBackgroundColor(colorGray);

                progressBar.setProgress(33);

                // Reset previous button color
                previousButton.setBackgroundResource(R.drawable.button_style);
                previousButton.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                previousButton.setColorFilter(colorWhite);

                // Reset next button color
                nextButton.setBackgroundResource(R.drawable.button_style);
                nextButton.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                saveButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);

                break;
            case 2:
                stepContentContainer.removeAllViews();
                stepContentContainer.addView(stepTwoLayout);

                currentStep = 2; // Reset currentStep value to two

                // Step button clickable
                step1Button.setClickable(true);
                step2Button.setClickable(false);
                step3Button.setClickable(true);

                previousButton.setClickable(true); // Make previousButton to clickable
                nextButton.setClickable(true); // Make nextButton to clickable

                contactLeft.setBackgroundColor(colorYellow);
                step2Button.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                stepTwoTextView.setTextColor(colorYellow);

                // Reset other views colors to gray
                contactRight.setBackgroundColor(colorGray);
                step3Button.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                stepThreeTextView.setTextColor(colorGray);
                OtherRight.setBackgroundColor(colorGray);

                progressBar.setProgress(58);

                // Change previous button color
                previousButton.setBackgroundResource(R.drawable.button_style);
                previousButton.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                previousButton.setColorFilter(colorWhite);

                // Reset next button color
                nextButton.setBackgroundResource(R.drawable.button_style);
                nextButton.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                saveButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);

                break;
            case 3:
                stepContentContainer.removeAllViews();
                stepContentContainer.addView(stepThreeLayout);

                currentStep = 3; // Reset currentStep value to three

                // Step button clickable
                step1Button.setClickable(true);
                step2Button.setClickable(true);
                step3Button.setClickable(false);

                previousButton.setClickable(true); // Make previousButton to clickable
                nextButton.setClickable(true);

                contactLeft.setBackgroundColor(colorYellow);
                step2Button.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                stepTwoTextView.setTextColor(colorYellow);
                contactRight.setBackgroundColor(colorYellow);
                step3Button.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                stepThreeTextView.setTextColor(colorYellow);
                OtherRight.setBackgroundColor(colorYellow);

                progressBar.setProgress(100);

                // Change previous button color
                previousButton.setBackgroundResource(R.drawable.button_style);
                previousButton.setBackgroundTintList(ColorStateList.valueOf(colorYellow));
                previousButton.setColorFilter(colorWhite);

                nextButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

                break;
        }

    }

    private void selectDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            // When the user selects a date, format it as a string and set it as the text of the EditText
            calendar.set(year, month, dayOfMonth);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calendar.getTime());

            DOB.setText(formattedDate);
        };

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                dateSetListener,
                year,
                month,
                dayOfMonth);
        datePickerDialog.show();
    }

    private void showDialogMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Confirm")
                .setMessage("Do you want to create a new user?")
                .setNegativeButton("No", (dialog, which) -> {
                    // If user click on NO nothing happen
                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Open ConfirmationFragment when user click on YES


                }).show();
    }

}