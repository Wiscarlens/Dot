package com.module.dot.Database.Cloud;

import android.content.Context;
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
import java.util.Objects;

public class FirebaseHandler {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    public static String getCurrentUserOnlineID(FirebaseAuth mAuth) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        return firebaseUser.getUid();
    }

    // Check if current user is administrator
    public interface AdminCheckCallback {
        void onAdminCheckResult(boolean isAdmin);
    }

    public static void isCurrentUserAdmin(FirebaseAuth mAuth, AdminCheckCallback callback) {
        String currentUserID = getCurrentUserOnlineID(mAuth);
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("users");

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Users firebaseUser = userSnapshot.getValue(Users.class);

                    if (firebaseUser != null && firebaseUser.getUserID().equals(currentUserID)) {
                        if (firebaseUser.getPositionTitle().equals("Administrator")) {
                            isAdmin = true;
                        }
                        break;
                    }
                }

                callback.onAdminCheckResult(isAdmin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseUserDatabase", "Failed to sync user data from Firebase: " + databaseError.getMessage());
                callback.onAdminCheckResult(false); // Assume not admin in case of error
            }
        });
    }


    public void createUser(Users newUser, ImageView profileImage, Context context){
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String UID = firebaseUser.getUid(); // Get the user ID

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

                        // Create user in the local database
                        assert firebaseUser != null;

                        Log.d("FirebaseUserDatabase", "User: " + firebaseUser.toString());

                        // Do not show current user in the list
                        if (!Objects.equals(firebaseUser.getUserID(), getCurrentUserOnlineID(FirebaseAuth.getInstance()))) {
                            userDatabase.createUser(firebaseUser);
                            Log.i("UserDatabase", "User with email " + firebaseUser.getEmail() + " added to SQLite.");
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("FirebaseUserDatabase", "Failed to sync user data from Firebase: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserDatabase", "Firebase data fetch cancelled: " + databaseError.getMessage());
            }
        });
    }


}