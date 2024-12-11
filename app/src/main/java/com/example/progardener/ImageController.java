package com.example.progardener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageController {

    private Context context;

    public ImageController(Context context) {
        this.context = context;
    }

    // Method to save an image to internal storage
    public void saveImageToStorage(Bitmap bitmap, String fileName) {
        File directory = context.getFilesDir();
        File imageFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the absolute path of the image
    public String getImagePath(String fileName) {
        File directory = context.getFilesDir();
        File imageFile = new File(directory, fileName);
        return imageFile.getAbsolutePath();
    }

    // Method to load an image from storage
    public Bitmap loadImageFromStorage(String path) {
        try {
            File file = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}