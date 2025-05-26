package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import com.example.car_spotting_front_end.R;
import com.example.car_spotting_front_end.dto.ClassifiyngResponseDTO;
import com.example.car_spotting_front_end.retrofit.RetrofitClient;
import com.example.car_spotting_front_end.services.ImageUploadService;
import com.google.android.material.button.MaterialButton;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsertImageActivity extends AppCompatActivity {
    private MaterialButton selectImageButton;
    private MaterialButton uploadImageButton;
    private ImageView imagePreview;
    private Uri selectedImageUri;
    private TextView classifierResponseText;
    private VideoView videoView;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imagePreview.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_image);

        initializeComponents();
    }

    private void initializeComponents() {
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        imagePreview = findViewById(R.id.imagePreview);
        classifierResponseText = findViewById(R.id.classifierResponseText);
        videoView = findViewById(R.id.backgroudVideoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_mooving_video;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoView.start();
        });

        selectImageButton.setOnClickListener(view -> {
            imagePickerLauncher.launch("image/*");
        });

        uploadImageButton.setOnClickListener(view -> {
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri);
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(Uri imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        if (filePath == null) {
            Toast.makeText(this, "Could not get file path", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        MediaType mediaType = MediaType.parse("image/*");
        RequestBody requestFile = RequestBody.create(file, mediaType);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Retrofit retrofit = RetrofitClient.getClient(this);
        ImageUploadService service = retrofit.create(ImageUploadService.class);

        service.uploadImage(body).enqueue(new Callback<ClassifiyngResponseDTO>() {
            @Override
            public void onResponse(Call<ClassifiyngResponseDTO> call, Response<ClassifiyngResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ClassifiyngResponseDTO ClassifiyngResponseDTO = response.body();
                    String responseMessage = "Car Make: " + ClassifiyngResponseDTO.getCarMake() + "\n" +
                            "Car Model: " + ClassifiyngResponseDTO.getCarModel() + "\n" +
                            "Car Year: " + ClassifiyngResponseDTO.getCarYear() + "\n" +
                            "Confidence: " + ClassifiyngResponseDTO.getConfidence() + "%";

                    classifierResponseText.setText(responseMessage);

                    Toast.makeText(InsertImageActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MainActivity", "Navigating to FeedActivity");
                            Toast.makeText(InsertImageActivity.this, "Going to Feed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InsertImageActivity.this, FeedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000); // 2000 milliseconds = 2 seconds

                } else {
                    Toast.makeText(InsertImageActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClassifiyngResponseDTO> call, Throwable t) {
                Toast.makeText(InsertImageActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path = cursor.getString(idx);
        cursor.close();
        return path;
    }
}
