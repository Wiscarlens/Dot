package com.module.dot.Activities.Users;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.module.dot.Activities.MainActivity;
import com.module.dot.Database.Cloud.FirebaseHandler;
import com.module.dot.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignupFragment extends Fragment {

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
    private TextInputEditText lastName;
    private TextInputEditText DOB;

    // Declare Form part two field
    private TextInputEditText email;
    private TextInputEditText phoneNumber;
    private TextInputEditText address;

    // Declare Form part three field
    private ImageView profileImage;
    private TextInputEditText companyName;
    private Spinner positionTitle;
    private TextInputEditText password;

    private Button saveButton;

    private FragmentActivity fragmentActivity;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        lastName = stepOneLayout.findViewById(R.id.signupLastNameText);
        TextInputLayout DOB_layout = stepOneLayout.findViewById(R.id.signupDOBLayout);
        DOB = stepOneLayout.findViewById(R.id.signupDOBText);

        // Step two form field
        email = stepTwoLayout.findViewById(R.id.signupEmailText);
        phoneNumber = stepTwoLayout.findViewById(R.id.signupPhoneNumberText);
        address = stepTwoLayout.findViewById(R.id.signupAddressText);

        // Step three form field
        profileImage = stepThreeLayout.findViewById(R.id.newProfileImage);
        companyName = stepThreeLayout.findViewById(R.id.signupCompanyNameText);
        positionTitle = stepThreeLayout.findViewById(R.id.signupPositionText);
        password = stepThreeLayout.findViewById(R.id.signupPasswordText);
        TextInputLayout passwordLayout = stepThreeLayout.findViewById(R.id.signupPasswordLayout);

        // Have data from the database
        ArrayList<String> positionOptions = new ArrayList<>(); // Category Option Spinner

        // This field a copy of the error message from the String resources file.
        String message = getResources().getString(R.string.field_empty);

        // When user click Calendar icon
        DOB_layout.setEndIconOnClickListener(v -> selectDate());

        // Selected Image
        ActivityResultLauncher<Intent> imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                assert result.getData() != null;
                                Uri uri = result.getData().getData();
                                profileImage.setImageURI(uri);  // Set image to itemImage view
                            }
                        });

        // Click on Image Item
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            imagePickerLauncher.launch(intent);
        });

        // Admin Create new user
        if (MainActivity.currentUser != null) {
            positionOptions.add("Manager");
            positionOptions.add("Cashier");

            companyName.setText(MainActivity.currentUser.getCompanyName());

            companyName.setVisibility(View.GONE);
            positionTitle.setVisibility(View.VISIBLE);
        } else {
            positionOptions.add("Administrator");

            positionTitle.setVisibility(View.GONE);
        }

        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, positionOptions);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionTitle.setAdapter(positionAdapter);


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
            saveToDatabase();

            // Replace Add item fragment with Home Fragment
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            UsersFragment usersFragment = new UsersFragment();
            fragmentTransaction.replace(R.id.fragment_container, usersFragment); // Replace previous fragment
            fragmentTransaction.addToBackStack(null); // Add the transaction to the back stack
            fragmentTransaction.commit();
        });


    }

    private void showStepContent(int step) {
        // Default Color
        int colorYellow = ContextCompat.getColor(getContext(), R.color.black);
        int colorGray = ContextCompat.getColor(getContext(), R.color.light_gray);
        int colorWhite = ContextCompat.getColor(getContext(), R.color.white);

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

    private void saveToDatabase() {
        User newUser = new User(
                String.valueOf(firstName.getText()),
                String.valueOf(lastName.getText()),
                String.valueOf(DOB.getText()),
                String.valueOf(email.getText()),
                String.valueOf(phoneNumber.getText()),
                String.valueOf(address.getText()),
                String.valueOf(companyName.getText()),
                String.valueOf(positionTitle.getSelectedItem()),
                String.valueOf(password.getText())
        );

        FirebaseHandler firebaseHandler = new FirebaseHandler();
        firebaseHandler.createUser(newUser, profileImage, getContext());
    }


}