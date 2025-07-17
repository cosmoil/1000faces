package com.cosmo.thousandfaces.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.cosmo.thousandfaces.R;
import com.cosmo.thousandfaces.controller.TimelapseController;
import com.cosmo.thousandfaces.controller.ImageController;
import com.cosmo.thousandfaces.model.ImageModel;

import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private Button btnExport;
    private EditText editFilename;
    private ProgressBar progressExport;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        btnExport = findViewById(R.id.btnExport);
        editFilename = findViewById(R.id.editFilename);
        progressExport = findViewById(R.id.progressExport);
        btnBack = findViewById(R.id.btnBackFromExport);

        btnBack.setOnClickListener(v -> finish());

        btnExport.setOnClickListener(v -> {
            String filename = editFilename.getText().toString().trim();
            if (filename.isEmpty()) {
                Toast.makeText(this, "Bitte einen Dateinamen eingeben", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show loading bar and disable buttons
            progressExport.setVisibility(View.VISIBLE);
            btnExport.setEnabled(false);
            btnBack.setEnabled(false);

            List<ImageModel> images = new ImageController().loadImages(this);

            new TimelapseController().exportTimelapse(this, images, filename, new TimelapseController.ExportCallback() {
                @Override
                public void onSuccess(String path) {
                    runOnUiThread(() -> {
                        progressExport.setVisibility(View.GONE);
                        btnExport.setEnabled(true);
                        btnBack.setEnabled(true);
                        Toast.makeText(ExportActivity.this, "Exportiert nach: " + path, Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        progressExport.setVisibility(View.GONE);
                        btnExport.setEnabled(true);
                        btnBack.setEnabled(true);
                        Toast.makeText(ExportActivity.this, "Fehler: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
}
