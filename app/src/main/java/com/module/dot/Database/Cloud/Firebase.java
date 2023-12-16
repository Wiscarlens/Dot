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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.module.dot.Activities.Users.Users;

import java.util.ArrayList;

// TODO: Change class name to something else. There is another class name firebase
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

    public void readUser(ArrayList<Users> users_for_display){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Users newUser = new Users(
                                null, // Profile Image Path
                                dataSnapshot.child("firstName").getValue(String.class),
                                dataSnapshot.child("lastName").getValue(String.class),
                                dataSnapshot.child("position").getValue(String.class)
                        );

                        String firstName = dataSnapshot.child("email").getValue(String.class);

                        // Now you have the user's first name
                        assert firstName != null;
                        Log.d("User's First Name", firstName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error getting user data", databaseError.toException());
                }
            });
        }
    }


}
