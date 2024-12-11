package com.example.progardener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //Variables storing data to display for this example
//    private final String[] plantnameslist = {"Chapter One", "Chapter Two", "Chapter Three", "Chapter Four",
//            "Chapter Five", "Chapter Six", "Chapter Seven", "Chapter Eight"};
//    private final String[] plantnoteslist = {"Item one details", "Item two details", "Item three details",
//            "Item four details", "Item file details", "Item six details", "Item seven details",
//            "Item eight details"};
//    private final String[] plantwateringslist = {"Item one details", "Item two details", "Item three details",
//            "Item four details", "Item file details", "Item six details", "Item seven details",
//            "Item eight details"};
//    private final int[] images = { R.drawable.android_image_1, R.drawable.android_image_2,
//            R.drawable.android_image_3, R.drawable.android_image_4, R.drawable.android_image_5,
//            R.drawable.android_image_6, R.drawable.android_image_7, R.drawable.android_image_8 };

    private ArrayList<String> plantnameslist = new ArrayList<>();
    private ArrayList<String> plantnoteslist = new ArrayList<>();;
    private ArrayList<String> plantwateringslist = new ArrayList<>();;
    private ArrayList<String> images = new ArrayList<>();;

    public RecyclerAdapter(ArrayList<String> plantnameslist, ArrayList<String> plantnoteslist, ArrayList<String> plantwateringslist, ArrayList<String> plantimagespaths) {
        this.plantnameslist = plantnameslist;
        this.plantnoteslist = plantnoteslist;
        this.plantwateringslist = plantwateringslist;
        this.images = plantimagespaths;
    }

    //Class that holds the items to be displayed (Views in card_layout)
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage;
        TextView plantName, plantNotes, plantWatering, idView;

        private final RecyclerAdapter adapter;
        private Context context;
        public ViewHolder(View itemView, RecyclerAdapter adapter) {
            super(itemView);
            idView = itemView.findViewById(R.id.index);
            plantImage = itemView.findViewById(R.id.plant_image);
            plantName = itemView.findViewById(R.id.plant_name);
            //plantNotes = itemView.findViewById(R.id.plant_notes);
            plantWatering = itemView.findViewById(R.id.plant_watering);

            this.adapter = adapter;
            this.context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    //send position to plant_details with intent (position is equal to the plant id in the database)
                    Intent i = new Intent(context, plant_details.class);
                    i.putExtra("PlantId", position);
                    context.startActivity(i);
                }
            });
        }
    }

    //Methods that must be implemented for a RecyclerView.Adapter
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        holder.plantName.setText(plantnameslist.get(position));
        //holder.plantNotes.setText(plantnoteslist.get(position));
        holder.plantWatering.setText(String.format("Watering Frequency: %s", plantwateringslist.get(position)));
        ImageController c = new ImageController(null);
        Bitmap image = c.loadImageFromStorage(images.get(position));
        holder.plantImage.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return plantnameslist.size();
    }
}

