package com.cosmo.thousandfaces.view;

import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.cosmo.thousandfaces.R;
import com.cosmo.thousandfaces.controller.ImageController;
import com.cosmo.thousandfaces.model.ImageModel;
import android.widget.Button;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    GridView galleryGrid;
    ImageAdapter adapter;
    boolean isEditMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Button btnBack = findViewById(R.id.btnBackFromGallery);
        btnBack.setOnClickListener(v -> finish());

        galleryGrid = findViewById(R.id.galleryGrid);

        List<ImageModel> images = new ImageController().loadImages(this);
        adapter = new ImageAdapter(this, images);
        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            btnEdit.setText(isEditMode ? "Done" : "Edit");
            adapter.setEditMode(isEditMode);
        });
        galleryGrid.setAdapter(adapter);
    }
}
