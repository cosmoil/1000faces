package com.cosmo.thousandfaces.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cosmo.thousandfaces.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class CameraActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button takePhotoButton;
    private Button btnBack;

    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Laufzeitberechtigung für Kamera abfragen
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
        }

        imageView = findViewById(R.id.cameraPreviewImage);

        takePhotoButton = findViewById(R.id.btnTakePhoto);
        btnBack = findViewById(R.id.btnBackFromCamera);

        takePhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        });

        btnBack.setOnClickListener(v -> finish());

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                                // Bild speichern
                                File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                        "captured_" + System.currentTimeMillis() + ".jpg");
                                try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    Toast.makeText(CameraActivity.this, "Bild gespeichert in: " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Toast.makeText(CameraActivity.this, "Fehler beim Speichern", Toast.LENGTH_SHORT).show();
                                }

                                // Vorschau anzeigen
                                if (imageBitmap != null) {
                                    WeakReference<Bitmap> resultRef = new WeakReference<>(
                                            Bitmap.createScaledBitmap(imageBitmap,
                                                    imageBitmap.getWidth(),
                                                    imageBitmap.getHeight(),
                                                    false).copy(Bitmap.Config.RGB_565, true));
                                    imageView.setImageBitmap(resultRef.get());
                                    Toast.makeText(CameraActivity.this, "Foto aufgenommen!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(CameraActivity.this, "Kamera abgebrochen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
