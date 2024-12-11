package com.example.progardener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.progardener.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;


public class plant_details extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String plantid;

    TextView plantidlabel, plantnamelabel, plantnoteslabel, wateringlabel;
    ImageView plantimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.plant_details);

        //Get references to view objects
        plantidlabel = findViewById(R.id.selectedplantidtextview);
        //set plant id to match position in RecyclerView array
        plantnamelabel = findViewById(R.id.plantnametextview);
        plantnoteslabel = findViewById(R.id.plantnotestextview);
        wateringlabel = findViewById(R.id.plantwateringtextview);
        plantimageview = findViewById(R.id.plantimage);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //Retrieve data passed in the Intent
            int receivedindex = extras.getInt("PlantId");
            //For debugging: print in the Logact (Debug level)
            this.plantid = String.valueOf(receivedindex);
            //Log.d("plant_details.java", plantid);
            //Update the UI
            plantidlabel.setText("Plant number: " + plantid);

            PlantDBHandler dbHandler = new PlantDBHandler(this, null, null, 1);
            Plant plant = dbHandler.findPlant(this.plantid);
            if (plant != null) {
                plantnamelabel.setText("Plant name: " + String.valueOf(plant.getPlantName()));
                plantnoteslabel.setText(String.valueOf(plant.getPlantNotes()));
                wateringlabel.setText("Watering: " + String.valueOf(plant.getWatering()));
                ImageController c = new ImageController(null);
                Bitmap image = c.loadImageFromStorage(plant.getImagePath());
                plantimageview.setImageBitmap(image);
            } else {
                plantidlabel.setText(String.valueOf(R.string.no_match_found));
                plantnamelabel.setText("");
                plantnoteslabel.setText("");
                wateringlabel.setText("");
            }

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        }
    }
    //OnClick method for DELETE button
    public void removePlant (View view) {
        PlantDBHandler dbHandler = new PlantDBHandler(this, null, null, 1);
        //Log.d("ID of plant to remove", plantidlabel.getText().toString());
        //Plant plant = dbHandler.findPlant(plantidlabel.getText().toString());
//        if (plant != null) {
//            dbHandler.deletePlant(plant.getID());
//            renumber ids to match contents
        if (dbHandler.deletePlant(this.plantid)) {
            //return to view plant
            Intent i = new Intent(this, view_plant.class);
            //Ask Android to start the new Activity
            startActivity(i);
        } else {
            Snackbar.make(view, "Plant not found",Snackbar.LENGTH_LONG).show();
        }
    }
}
