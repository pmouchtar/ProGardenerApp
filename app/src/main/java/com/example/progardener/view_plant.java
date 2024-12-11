package com.example.progardener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class view_plant extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_plant);

        recyclerView = findViewById(R.id.recycler_view);

        //Set the layout of the items in the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Set my Adapter for the RecyclerView
        ArrayList<Plant> plants;
        ArrayList<String> plantnameslist = new ArrayList<>();
        ArrayList<String> plantnoteslist = new ArrayList<>();
        ArrayList<String> plantwateringslist = new ArrayList<>();
        ArrayList<String> plantimagespaths = new ArrayList<>();

        PlantDBHandler db = new PlantDBHandler(this, null, null, 1);
        plants=db.getAllPlants();

        for (Plant p : plants) {
            plantnameslist.add(p.getPlantName());
            plantnoteslist.add(p.getPlantNotes());
            plantwateringslist.add(p.getWatering());
            plantimagespaths.add(p.getImagePath());
        }

        adapter = new RecyclerAdapter(plantnameslist, plantnoteslist, plantwateringslist, plantimagespaths);
        recyclerView.setAdapter(adapter);
    }
}
