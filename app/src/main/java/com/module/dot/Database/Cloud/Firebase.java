package com.module.dot.Database.Cloud;

import static androidx.fragment.app.FragmentManager.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.module.dot.Activities.Users.Users;

public class Firebase {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;


    public void createUser(Users newUser, Context context){
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        try {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(userId).setValue(newUser);
                        } catch (Exception e) {
                            Log.e("Firebase", "Error while adding user to online database", e);
                        }

                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "createUserWithEmail:success");
                        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                        // Inside your Fragment class
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
