package com.module.dot.Database.Cloud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.module.dot.Activities.Users.Users;
import com.module.dot.Database.Local.UserDatabase;
import com.module.dot.Helpers.Utils;

import java.util.ArrayList;

public class FirebaseHandler {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    public static String getCurrentUserOnlineID(FirebaseAuth mAuth) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        return firebaseUser.getUid();
    }


    public void createUser(Users newUser, ImageView profileImage, Context context){
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the user ID
                        try {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String UID = firebaseUser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            saveImageToFirebaseStorage(profileImage, UID);
                            newUser.setProfileImagePath(UID);
                            mDatabase.child("users").child(UID).setValue(newUser);

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

    public static void readUser(ArrayList<Users> userList){
        // Firebase database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Attach a ValueEventListener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear(); // Clear the list before populating it again

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    userList.add(user);
                }

                Log.i("Firebase", "Data read successfully");

                // Notify the adapter that the data set has changed
//                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });

    }

    public void saveImageToFirebaseStorage(ImageView profileImage, String UID) {
        String imagePath = "Profiles/" + UID;

        // Get the data from an ImageView as bytes
        profileImage.setDrawingCacheEnabled(true);
        profileImage.buildDrawingCache();

        byte[] imageData = Utils.getByteArrayFromDrawable(profileImage.getDrawable());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(imagePath);

        UploadTask uploadTask = storageReference.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("Firebase", "Error while uploading image to Firebase Storage", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Firebase", "Image uploaded successfully");
            }
        });
    }


    // Synchronize user data from Firebase to SQLite
    public static void syncUserDataFromFirebase(Context context, String tableName){
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference(tableName);

        // Fetch data from Firebase
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try (UserDatabase userDatabase = new UserDatabase(context)) {
                    // Iterate through Firebase data
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Users firebaseUser = userSnapshot.getValue(Users.class);

                        SQLiteDatabase db = userDatabase.getWritableDatabase();

                        // Check if the user with the same email already exists in the local database
                        assert firebaseUser != null;
                        if (userDatabase.isEmailExists(db, firebaseUser.getEmail(), tableName, "email")) {
                            Log.i("UserDatabase", "User with email " + firebaseUser.getEmail() + " already exists in SQLite.");
                            continue;  // Skip inserting duplicate users
                        }

                        // Create user in the local database
                        userDatabase.createUser(firebaseUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("UserDatabase", "Failed to sync user data from Firebase: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserDatabase", "Firebase data fetch cancelled: " + databaseError.getMessage());
            }
        });
    }


}
