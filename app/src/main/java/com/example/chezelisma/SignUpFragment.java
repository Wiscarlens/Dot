package com.example.chezelisma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class SignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private TextInputEditText position;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout passwordLayout;
    private TextInputEditText confirmPassword;
    private TextInputLayout confirmPasswordLayout;
    private Button signup;
    private TextInputEditText dateOfBirth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        position = view.findViewById(R.id.signupPositionText);
        firstName = view.findViewById(R.id.signupFirstNameText);
        lastName = view.findViewById(R.id.signupLastNameText);
        email = view.findViewById(R.id.email_signupText);
        password = view.findViewById(R.id.password_signupText);
        passwordLayout = view.findViewById(R.id.password_signupLayout);
        confirmPassword = view.findViewById(R.id.confirm_password_signupText);
        confirmPasswordLayout = view.findViewById(R.id.confirm_password_signupLayout);
        signup = view.findViewById(R.id.signupButton);
        dateOfBirth = view.findViewById(R.id.signupDOBText);

         // If the java API is less than 26, default text will be MM/dd/yyyy
        String todayDate = "mm/dd/yyyy";

        // For Device that support API above or equal to 26 select date will equal to current date by default
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            //String formattedDate =
            todayDate = currentDate.format(formatter);;
        }

        dateOfBirth.setText(todayDate);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // When the user selects a date, format it as a string and set it as the text of the EditText
                String selectedDate = String.format("%02d/%02d/%04d", month + 1, dayOfMonth, year);
                dateOfBirth.setText(selectedDate);
            }
        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
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
        });



        signup.setOnClickListener(v -> {
            String userPosition = position.getText().toString().trim();
            String userFirstName = firstName.getText().toString().trim();
            String userLastName = lastName.getText().toString().trim();
            String userEmail = email.getText().toString().toLowerCase().trim();
            String userPassword = password.getText().toString().trim();
            String userConfirmPassword = confirmPassword.getText().toString().trim();

            // This field a copy of the error message from the String resources file.
            String message = getResources().getString(R.string.field_empty);

            if(userPosition.isEmpty()){
                position.setError(message);
            }

            if(userFirstName.isEmpty()){
                firstName.setError(message);
            }

            if(userLastName.isEmpty()){
                lastName.setError(message);
            }

            if(userEmail.isEmpty()){
                email.setError(message);
            }

            if(userPassword.isEmpty()){
                // Hide password toggle so it does go over the error message
                passwordLayout.setEndIconVisible(false);
                password.setError(getResources().getString(R.string.password_empty));
            }

            if(userConfirmPassword.isEmpty()){
                // Hide password toggle so does go over the error message
                confirmPasswordLayout.setEndIconVisible(false);
                confirmPassword.setError(message);
            }


            else {
                if(!userConfirmPassword.equals(userPassword)){
                    // Hide password toggle so does go over the error message
                    confirmPasswordLayout.setEndIconVisible(false);

                    message = getResources().getString(R.string.password_not_same);
                    confirmPassword.setError(message);

                } else {
                    auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                        String toastMessage = "";
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                toastMessage = getResources().getString(R.string.signup_Successful);
                                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            } else{
                                toastMessage = getResources().getString(R.string.signup_failed);
                                Toast.makeText(getContext(), toastMessage + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        // Update the text in the TextInputEditText with the selected date
//        String dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
//        dateOfBirth.setText(dateString);
//    }

}