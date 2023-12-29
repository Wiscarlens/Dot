package com.module.dot.Database.Cloud;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.module.dot.Activities.Items.Item;
import com.module.dot.Activities.MainActivity;
import com.module.dot.Activities.Users.User;
import com.module.dot.Database.Local.ItemDatabase;
import com.module.dot.Database.Local.UserDatabase;
import com.module.dot.Helpers.ImageStorageManager;
import com.module.dot.Helpers.Utils;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class FirebaseHandler {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase;

    public static String getCurrentUserOnlineID(FirebaseAuth mAuth) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        return firebaseUser.getUid();
    }


    public void createUser(User newUser, Drawable profileImage, Context context){
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword_hash())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {

                            String globalID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            newUser.setGlobalID(globalID);

                            if (MainActivity.currentUser == null){
                                newUser.setCreatorID(globalID); // Create user from log in page
                            } else {
                                newUser.setCreatorID(MainActivity.currentUser.getGlobalID());
                            }

                            if (profileImage != null){
                                newUser.setProfileImagePath(globalID);
                                saveImageToFirebaseStorage(profileImage, "Profiles/" + globalID);
                            }

                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference();
                            currentUserDb.child("users").child(globalID).setValue(newUser);

                            Log.i("Firebase", "User Added Successfully!");


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


    public static void saveImageToFirebaseStorage(Drawable image, String imagePath) {

        Bitmap bitmap = Utils.drawableToBitmap(image);

        // Compress the Bitmap into a ByteArrayOutputStream with 50% quality
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);

        // Convert the ByteArrayOutputStream to a byte array
        byte[] imageData = baos.toByteArray();

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

    public static void readItem(String tableName, Context context){
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference(tableName);
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try (ItemDatabase itemDatabase = new ItemDatabase(context)) {
                    // Iterate through Firebase data
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        Item item = userSnapshot.getValue(Item.class);

                        // Create item in the local database
                        assert item != null;

                        if (Objects.equals(MainActivity.currentUser.getCreatorID(), item.getCreatorID())){
                            downloadAndSaveImagesLocally("Items", item.getImagePath(), context);
                            itemDatabase.createItem(item);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("FirebaseItemDatabase", "Failed to sync item data from Firebase: " + e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserDatabase", "Firebase data fetch cancelled: " + error.getMessage());

            }
        });

    }


    // Synchronize user data from Firebase to SQLite
    public static void readUser(String tableName, Context context){
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference(tableName);

        // Fetch data from Firebase
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try (UserDatabase userDatabase = new UserDatabase(context)) {
                    // Iterate through Firebase data
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User firebaseUser = userSnapshot.getValue(User.class);

                        // Create user in the local database
                        assert firebaseUser != null;

                        // Do not show current user in the list
                        if (Objects.equals(firebaseUser.getGlobalID(), MainActivity.currentUser.getGlobalID())) {
                            Log.i("FirebaseUserDatabase", "Skipping current user: " + firebaseUser.getFullName());
                        } else {
                            if (Objects.equals(MainActivity.currentUser.getCreatorID(), firebaseUser.getCreatorID())){
                                downloadAndSaveImagesLocally("Profiles", firebaseUser.getProfileImagePath(), context);
                                userDatabase.createUser(firebaseUser);

                                Log.i("FirebaseUserDatabase", "User with email " + firebaseUser.getEmail() + " added to SQLite.");
                            }
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


    public static void downloadAndSaveImagesLocally(String folderName, String fileName, Context context) {
        final long ONE_MEGABYTE = 1024 * 1024;

        String imagePath = folderName + "/" + fileName;
        StorageReference imageRef = storage.getReference(imagePath);

        try {
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Drawable image = Utils.byteArrayToDrawable(bytes, context.getResources());

                ImageStorageManager.saveImageLocally(context, image, folderName, fileName);
            }).addOnFailureListener(exception -> {
                Log.e("Firebase", "Error downloading item image", exception);
            });

        } catch (IndexOutOfBoundsException e){
            Log.e("Firebase", "The maximum allowed buffer size was exceeded.", e);
        }

    }


    public static void createItem(Item newItem, Drawable itemImage){
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("items");

        // Use push to generate a unique key
        DatabaseReference newItemRef = itemsRef.push();

        String globalID = newItemRef.getKey(); // Get get item global ID

        newItem.setGlobalID(globalID);

        if(itemImage != null){
            newItem.setImagePath(globalID);
        }

        // Set the item with the generated key
        newItemRef.setValue(newItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (itemImage != null){
                    // Save the image to Firebase Storage with the generated key
                    saveImageToFirebaseStorage(itemImage, "Items/" + globalID);
                }

                Log.i("Firebase", "Item Added Successfully!");
            }
        }).addOnFailureListener(e ->
                Log.d("Firebase", "createUserWithEmail:failure")
        );
    }


}
