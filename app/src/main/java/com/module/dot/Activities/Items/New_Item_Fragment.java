package com.module.dot.Activities.Items;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.module.dot.Database.MyDatabaseHelper;
import com.module.dot.R;

import java.util.ArrayList;


public class New_Item_Fragment extends Fragment {
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


    // Step One field
    private ImageView itemImage;
    private TextInputLayout itemNameLayout;
    private TextInputEditText itemName;
    private Spinner category;
    private TextInputLayout unitPriceLayout;
    private TextInputEditText unitPrice;

    // Step Two field
    private TextInputEditText sku;
    private Spinner unitType;
    private TextInputEditText itemStock;

    // Step Three field
    private TextInputEditText wholesalePrice;
    private TextInputEditText itemTax;
    private TextInputEditText itemDescription;

    private Button saveButton;
    private FragmentActivity fragmentActivity;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_item_, container, false);
    }

    @Override
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
        stepOneLayout = LayoutInflater.from(getContext()).inflate(R.layout.new_item_form_step_one, null);
        stepTwoLayout = LayoutInflater.from(getContext()).inflate(R.layout.new_item_form_step_two, null);
        stepThreeLayout = LayoutInflater.from(getContext()).inflate(R.layout.new_item_form_step_three, null);

        // Step One form
        itemImage = stepOneLayout.findViewById(R.id.newItemImage);
        itemNameLayout = stepOneLayout.findViewById(R.id.itemNameLayout);
        itemName = stepOneLayout.findViewById(R.id.itemNameText);
        category = stepOneLayout.findViewById(R.id.productCategoryText);
        unitPriceLayout = stepOneLayout.findViewById(R.id.unitPriceLayout);
        unitPrice = stepOneLayout.findViewById(R.id.unitPriceText);

        // Step two form
        sku = stepTwoLayout.findViewById(R.id.SKUText);
        unitType = stepTwoLayout.findViewById(R.id.unitSpinner);
        itemStock = stepTwoLayout.findViewById(R.id.stockText);

        // Step three form
        wholesalePrice = stepThreeLayout.findViewById(R.id.wholesalesPrice);
        itemTax = stepThreeLayout.findViewById(R.id.taxText);
        itemDescription = stepThreeLayout.findViewById(R.id.productDescriptionText);

        // Have data from the database
        ArrayList<String> categoryOptions = new ArrayList<>(); // Category Option Spinner
        ArrayList<String> unitOptions = new ArrayList<>(); // Unit Option Spinner

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

        // Load data to Category Option Spinner
        categoryOptions.add("Category");
        categoryOptions.add("Soft Drink");
        categoryOptions.add("Alcohol");
        categoryOptions.add("Game");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        // Load data to Product Unit spinner
        unitOptions.add("Unit");
        unitOptions.add("Hour");
        unitOptions.add("Liter");

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unitOptions);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitType.setAdapter(unitAdapter);

        // Selected Image
        ActivityResultLauncher<Intent> imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                assert result.getData() != null;
                                Uri uri = result.getData().getData();
                                itemImage.setImageURI(uri);  // Set image to itemImage view
                            }
                        });

        // Click on Image Item
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imagePickerLauncher.launch(intent);

            }
        });

        saveButton.setOnClickListener(v -> {
            setItems(); // Save data locally
            //uploadData();

            // Replace Add item fragment with Home Fragment
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ItemsFragment itemsFragment = new ItemsFragment();
            fragmentTransaction.replace(R.id.fragment_container, itemsFragment); // Replace previous fragment
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
    private void setItems() {
        Items newItem = new Items(
                itemImage.getDrawable(),
                String.valueOf(itemName.getText()).trim(),
                Double.parseDouble(String.valueOf(unitPrice.getText()).trim()),
                category.getSelectedItem().toString(),
                String.valueOf(sku.getText()).trim(),
                unitType.getSelectedItem().toString(),
                Integer.parseInt(String.valueOf(itemStock.getText()).trim()),
                Double.parseDouble(String.valueOf(wholesalePrice.getText()).trim()),
                Double.parseDouble(String.valueOf(itemTax.getText()).trim()),
                String.valueOf(itemDescription.getText())
        );

        try (MyDatabaseHelper myDB = new MyDatabaseHelper(getContext())) {
            myDB.setItem(newItem);
        }
    }

//    TODO: Upload data to firebase
//    public void uploadData(){
//        String Name = String.valueOf(itemName.getText()).trim();
//        double Price = Double.parseDouble(String.valueOf(unitPrice.getText()));
//        String Category = category.getSelectedItem().toString();
//
//        String SKU = String.valueOf(sku.getText()).trim();
//        String UnitType = unitType.getSelectedItem().toString();
//        int stock = Integer.parseInt(String.valueOf(itemStock.getText()));
//
//        double wholesalesPrice = Double.parseDouble(String.valueOf(wholesalePrice.getText()));
//        double tax = Double.parseDouble(String.valueOf(itemTax.getText()));
//        String description = String.valueOf(itemDescription.getText());
//
//        NewItemData newItemData = new NewItemData(Name, Price, Category, SKU, UnitType, stock, wholesalesPrice, tax, description);
//
//        FirebaseDatabase.getInstance().getReference("Items").child(Name)
//                .setValue(newItemData).addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        String message = getResources().getString(R.string.save);
//                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
//    }

}