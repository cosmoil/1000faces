package com.cosmo.thousandfaces.view;

import com.cosmo.thousandfaces.view.CameraActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.cosmo.thousandfaces.R;

public class HomeActivity extends AppCompatActivity {

    Button btnCamera, btnGallery, btnExport, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnExport = findViewById(R.id.btnExport);
        btnSettings = findViewById(R.id.btnSettings);

        btnCamera.setOnClickListener(v -> startActivity(new Intent(this, CameraActivity.class)));
        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        btnExport.setOnClickListener(v -> startActivity(new Intent(this, ExportActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }
}
