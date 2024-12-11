package com.example.progardener;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class PlantDBHandler extends SQLiteOpenHelper {
    //Σταθερές για τη ΒΔ (όνομα ΒΔ, έκδοση, πίνακες κλπ)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "plantDB.db";
    public static final String TABLE_PLANTS = "plants";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLANTNAME = "plantname";
    public static final String COLUMN_PLANTNOTES = "plantnotes";
    public static final String COLUMN_WATERING = "watering";
    public static final String COLUMN_PATH = "image_path";
    //Constructor
    public PlantDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //Δημιουργία του σχήματος της ΒΔ (πίνακας plants)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLANTS_TABLE = "CREATE TABLE " +
                TABLE_PLANTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PLANTNAME + " TEXT," +
                COLUMN_PLANTNOTES + " TEXT," +
                COLUMN_WATERING + " TEXT," +
                COLUMN_PATH + " TEXT" + ")";
        db.execSQL(CREATE_PLANTS_TABLE);
    }
    //Αναβάθμιση ΒΔ: εδώ τη διαγραφώ και τη ξαναδημιουργώ ίδια
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        onCreate(db);
    }
    //Μέθοδος για προσθήκη ενός φυτού στη ΒΔ
    public void addPlant(Plant plant) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, plant.getID());
        values.put(COLUMN_PLANTNAME, plant.getPlantName());
        values.put(COLUMN_PLANTNOTES, plant.getPlantNotes());
        values.put(COLUMN_WATERING, plant.getWatering());
        values.put(COLUMN_PATH, plant.getImagePath());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PLANTS, null, values);
        db.close();
    }
    //Μέθοδος για εύρεση φυτού βάσει του index του
    public Plant findPlant(String plantid) {
        String query = "SELECT * FROM " + TABLE_PLANTS + " WHERE " + COLUMN_ID + " = '" + plantid + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Plant plant = new Plant();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            plant.setID(cursor.getString(0));
            plant.setPlantName(cursor.getString(1));
            plant.setPlantNotes(cursor.getString(2));
            plant.setWatering(cursor.getString(3));
            plant.setImagePath(cursor.getString(4));
            cursor.close();
        } else {
            plant = null;
        }
        db.close();
        return plant;
    }
    //Μέθοδος για διαγραφή φυτού βάσει του index του
    public boolean deletePlant(String plantid) {
        boolean result = false;
        Log.d("ID of plant to remove", plantid);
        Plant plant = findPlant(plantid);
        if (plant != null){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PLANTS, COLUMN_ID + " = ?", new String[] { String.valueOf(plant.getID()) });
            result = true;

            //renumber ids to match contents
            for (int i=Integer.parseInt(plantid)+1;i<countPlants();i++)
            {
                ContentValues updatedValues = new ContentValues();
                updatedValues.put("id", Integer.parseInt(plantid)-1);

                db.update(TABLE_PLANTS, updatedValues, "id = ?", new String[]{String.valueOf(plantid)});
            }
            db.close();
        }
        return result;
    }

    public ArrayList<Plant> getAllPlants() {
        ArrayList<Plant> plantslist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLANTS, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLANTNAME));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLANTNOTES));
                String watering = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WATERING));
                String imagepath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH));


//                Log.d("PlantDBHandler","I was here");
//                Log.d("Current plant id: ",id);
//                Log.d("Current plant name: ",name);
//                Log.d("Current plant notes: ",notes);
//                Log.d("Current plant watering: ",watering);

                Plant plant = new Plant(id, name, notes, watering, imagepath);
                plantslist.add(plant);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return plantslist;
    }

    public int countPlants() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PLANTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.d("PlantDBHandler: Plants count: ", String.valueOf(count));
        return count;
    }
}
