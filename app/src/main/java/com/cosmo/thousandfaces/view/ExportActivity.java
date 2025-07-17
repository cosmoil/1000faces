package com.cosmo.thousandfaces.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.cosmo.thousandfaces.R;
import com.cosmo.thousandfaces.controller.TimelapseController;
import com.cosmo.thousandfaces.controller.ImageController;
import com.cosmo.thousandfaces.model.ImageModel;

import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private Button btnExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        btnExport = findViewById(R.id.btnExport);

        btnExport.setOnClickListener(v -> {
            List<ImageModel> images = new ImageController().loadImages(this);

            new TimelapseController().exportTimelapse(this, images, new TimelapseController.ExportCallback() {
                @Override
                public void onSuccess(String path) {
                    runOnUiThread(() -> Toast.makeText(ExportActivity.this, "Exportiert nach: " + path, Toast.LENGTH_LONG).show());
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> Toast.makeText(ExportActivity.this, "Fehler: " + error, Toast.LENGTH_LONG).show());
                }
            });
        });
    }
}
