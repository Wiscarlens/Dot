package com.example.chezelisma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class New_Item_Fragment extends Fragment {
    ArrayAdapter categoryList;
    TextInputEditText itemName;
    AutoCompleteTextView category;
    TextInputEditText option;
    TextInputEditText sku;
    TextInputEditText unitType;
    TextInputEditText price;
    Button addButton;
    //Uri uri;
    private FragmentActivity fragmentActivity;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_item_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemName = view.findViewById(R.id.itemNameText);
        category = view.findViewById(R.id.itemCategoryText);
        option = view.findViewById(R.id.itemOptionsText);
        sku = view.findViewById(R.id.itemSKUText);
        unitType = view.findViewById(R.id.itemUnitTypeText);
        price = view.findViewById(R.id.itemPriceText);
        addButton = view.findViewById(R.id.addButton);

        ArrayList<String> itemList = new ArrayList<String>();
        itemList.add("Soft Drink");
        itemList.add("Alcohol");
        itemList.add("Game");

        categoryList = new ArrayAdapter(getContext(), R.layout.list_item, itemList);
        category.setAdapter(categoryList);

        category.setOnItemClickListener((parent, view1, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            //Toast.makeText(NewItem.this, item, Toast.LENGTH_SHORT).show();
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = result.getData();
//                            uri = data.getData();
//                            itemName.setText(uri);
                    }

                }
        );

        addButton.setOnClickListener(v -> {
            uploadData();

            // Replace Add item fragment with Home Fragment
            FragmentManager fragmentManager =  fragmentActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment); // Replace previous fragment
            fragmentTransaction.addToBackStack(null); // Add the transaction to the back stack
            fragmentTransaction.commit();
        });
    }

    public void uploadData(){
        String Name = itemName.getText().toString();
        String Category = category.getText().toString();
        String Option = option.getText().toString();;
        String SKU = sku.getText().toString();;
        String UnitType = unitType.getText().toString();;
        String Price = price.getText().toString();;

        NewItemData newItemData = new NewItemData(Name, Category, Option, SKU, UnitType, Price);

        FirebaseDatabase.getInstance().getReference("Items").child(Name)
                .setValue(newItemData).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String message = getResources().getString(R.string.save);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show());
    }

}