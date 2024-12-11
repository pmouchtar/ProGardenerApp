package com.example.progardener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.progardener.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;


public class add_plant extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String plantid;
    TextView idView;
    EditText plantnameBox, plantnotesBox, wateringBox;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private ImageController imageController;
    public String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.add_plant);

        /*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

         */

        //Get references to view objects
        idView = findViewById(R.id.index);
        PlantDBHandler dbHandler = new PlantDBHandler(this, null, null, 1);
        this.plantid=String.valueOf(dbHandler.countPlants());
        idView.setText("New plant id: " + this.plantid);
        //set plant id to match position in RecyclerView array
        plantnameBox = findViewById(R.id.plantName);
        plantnotesBox = findViewById(R.id.plantNotes);
        wateringBox = findViewById(R.id.plantWatering);

        //Image
        imageController = new ImageController(this);
        Button buttonUploadPicture = findViewById(R.id.uploadpicturebutton);
        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        //Image
    }

    //OnClick method for ADD button
    public void newPlant (View view) {
        PlantDBHandler db = new PlantDBHandler(this, null, null, 1);
        this.plantid = String.valueOf(db.countPlants());
        idView.setText("New plant id: " + this.plantid);
        String plantName = plantnameBox.getText().toString();
        String plantNotes = plantnotesBox.getText().toString();
        String plantWatering = wateringBox.getText().toString();
        String imagePath = path;
        //String plantImagepath = wateringBox.getText().toString();
        if (!plantName.equals("") && !plantWatering.equals("") && !plantNotes.equals("") && !(imagePath ==null)) {
            Plant found = db.findPlant(plantName);
            if (found == null) {

                Plant plant = new Plant(this.plantid, plantName, plantNotes, plantWatering, imagePath);
                db.addPlant(plant);
                idView.setText("New plant id: " + String.valueOf(db.countPlants()));
                plantnameBox.setText("");
                plantnotesBox.setText("");
                wateringBox.setText("");
            }
            //return to main
            Intent i = new Intent(this, MainActivity.class);
            //Ask Android to start the new Activity
            startActivity(i);
        }
        else {
            Snackbar.make(view, "Fill in all info and add image",Snackbar.LENGTH_LONG).show();
        }
    }
    //^ImageHandling^

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API level 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
            } else {
                openGallery();
            }
        } else {
            // Android 12 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            } else {
                openGallery();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    // Use the ImageController class to save the image and get the path
                    String fileName = "uploaded_image"+this.plantid+".png";
                    Log.d("Plant image file path to use:", fileName);
                    imageController.saveImageToStorage(bitmap, fileName);
                    String imagePath = imageController.getImagePath(fileName);
                    path = imagePath;
                    // Use the imagePath as needed
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //^ImageHandling^
    //Cancel Button
    public void cancelButton(View view) {
        Intent i = new Intent(this, MainActivity.class);
        //Ask Android to start the new Activity
        startActivity(i);
    }
}
