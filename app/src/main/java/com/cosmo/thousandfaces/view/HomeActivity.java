package com.cosmo.thousandfaces.view;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.cosmo.thousandfaces.R;

public class HomeActivity extends AppCompatActivity {

    Button btnCamera, btnGallery, btnExport, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnExport = findViewById(R.id.btnExport);
        btnLogOut = findViewById(R.id.btnLogOut);

        btnCamera.setOnClickListener(v -> startActivity(new Intent(this, CameraActivity.class)));
        btnGallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        btnExport.setOnClickListener(v -> startActivity(new Intent(this, ExportActivity.class)));
        btnLogOut.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Close HomeActivity
        });
    }
}
