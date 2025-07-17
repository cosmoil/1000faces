package com.cosmo.thousandfaces.controller;

import android.content.Context;
import android.os.Environment;

import com.cosmo.thousandfaces.model.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageController {

    public List<ImageModel> loadImages(Context context) {
        List<ImageModel> images = new ArrayList<>();

        File picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDir != null && picturesDir.exists()) {
            File[] files = picturesDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        images.add(new ImageModel(file.getAbsolutePath()));
                    }
                }
            }
        }

        //  Sortieren nach Änderungsdatum
        images.sort((img1, img2) -> {
            File file1 = new File(img1.getImagePath());
            File file2 = new File(img2.getImagePath());
            return Long.compare(file1.lastModified(), file2.lastModified());
        });

        return images;
    }
}