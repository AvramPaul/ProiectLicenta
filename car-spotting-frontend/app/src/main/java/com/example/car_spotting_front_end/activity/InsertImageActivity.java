package com.example.car_spotting_front_end.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InsertImageActivity extends AppCompatActivity {

    private LinearLayout myPostsButton;
    private LinearLayout logoutButton;
    private LinearLayout feedButton;
    private TextView profilePicture;

    private boolean pannelVisible = false;

    private LinearLayout sidePanel;
    private MaterialButton selectImageButton;
    private MaterialButton uploadImageButton;
    private ImageView imagePreview;
    private Uri selectedImageUri;
    private TextView textMake, textModel, textYear;
    private BarChart confidenceChart;
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
        myPostsButton = findViewById(R.id.myPostsButton);
        logoutButton = findViewById(R.id.logoutButton);
        feedButton = findViewById(R.id.feedButton);
        profilePicture = findViewById(R.id.profileButton);
        sidePanel = findViewById(R.id.sidePanel);
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        imagePreview = findViewById(R.id.imagePreview);
        textMake = findViewById(R.id.textMake);
        textModel = findViewById(R.id.textModel);
        textYear = findViewById(R.id.textYear);
        confidenceChart = findViewById(R.id.confidenceChart);
        videoView = findViewById(R.id.backgroudVideoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bg_mooving_video;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoView.start();
        });

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String username = prefs.getString("logged_username", "");
        profilePicture.setText(username.substring(0, 1));

        profilePicture.setOnClickListener(view -> {
            if (!pannelVisible) {
                sidePanel.setVisibility(View.VISIBLE);
                sidePanel.animate().translationX(0).setDuration(300);
                pannelVisible = true;
            } else {
                sidePanel.animate().translationX(80).setDuration(300);
                sidePanel.setVisibility(View.GONE);
                pannelVisible = false;
            }
        });
        myPostsButton.setOnClickListener(view -> {
            Intent intent = new Intent(InsertImageActivity.this, MyPostsActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("logged_username");
            editor.remove("jwt_token");
            editor.apply();
            Intent intent = new Intent(InsertImageActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        feedButton.setOnClickListener(view -> {
            Intent intent = new Intent(InsertImageActivity.this, FeedActivity.class);
            startActivity(intent);
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

                    textMake.setText(ClassifiyngResponseDTO.getCarMake());
                    textModel.setText(ClassifiyngResponseDTO.getCarModel());
                    textYear.setText(String.valueOf(ClassifiyngResponseDTO.getCarYear()));

                    List<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(0f, (float) ClassifiyngResponseDTO.getConfidence()));

                    BarDataSet set = new BarDataSet(entries, "Confidence (%) ");
                    set.setDrawValues(true);
                    set.setValueTextSize(12f);
                    set.setValueTextColor(Color.WHITE);
                    set.setValueTypeface(Typeface.DEFAULT_BOLD);

                    Legend legend = confidenceChart.getLegend();
                    legend.setEnabled(true);
                    legend.setTextColor(Color.WHITE);
                    legend.setTypeface(Typeface.DEFAULT_BOLD);
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    legend.setDrawInside(false);
                    legend.setTextSize(22f);

                    BarData data = new BarData(set);
                    data.setBarWidth(0.5f);

                    confidenceChart.setData(data);

                    XAxis xAxis = confidenceChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(Collections.singletonList(ClassifiyngResponseDTO.getCarMake() + " " + ClassifiyngResponseDTO.getCarModel() + " " + ClassifiyngResponseDTO.getCarYear())));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(1);
                    xAxis.setTextColor(Color.WHITE);
                    xAxis.setTypeface(Typeface.DEFAULT_BOLD);

                    confidenceChart.getAxisRight().setEnabled(false);
                    confidenceChart.getDescription().setEnabled(false);
                    confidenceChart.getLegend().setEnabled(true);
                    confidenceChart.setFitBars(true);

                    confidenceChart.setBackgroundColor(getResources().getColor(android.R.color.black));
                    confidenceChart.getAxisLeft().setTextColor(Color.WHITE);
                    confidenceChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);

                    confidenceChart.getAxisLeft().setTextColor(Color.WHITE);
                    confidenceChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);
                    confidenceChart.invalidate();

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
                    }, 100000);

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
