package com.example.progardener;

public class Plant {
    private String _id;
    private String _plantname;
    private String _plantnotes;
    private String _watering;
    private String _imagepath;
    public Plant() {
    }
    public Plant(String id, String plantname, String plantnotes, String watering, String imagepath) {
        this._id = id;
        this._plantname = plantname;
        this._plantnotes = plantnotes;
        this._watering = watering;
        this._imagepath = imagepath;
    }
    public Plant(String plantname, String plantnotes, String watering) {
        this._plantname = plantname;
        this._plantnotes = plantnotes;
        this._watering = watering;
    }
    public Plant(String plantname, String plantnotes, String watering, String imagePath) {
        this._plantname = plantname;
        this._plantnotes = plantnotes;
        this._watering = watering;
        this._imagepath = imagePath;
    }
    public void setID(String id) {
        this._id = id;
    }
    public String getID() {
        return this._id;
    }
    public void setPlantName(String plantname) {
        this._plantname = plantname;
    }
    public String getPlantName() {
        return this._plantname;
    }
    public void setPlantNotes(String plantnotes) {
        this._plantnotes = plantnotes;
    }
    public String getPlantNotes() {
        return this._plantnotes;
    }
    public void setWatering(String watering) {
        this._watering = watering;
    }
    public String getWatering() {
        return this._watering;
    }
    public void setImagePath(String imagepath) {
        this._imagepath = imagepath;
    }
    public String getImagePath() {
        return this._imagepath;
    }
}
