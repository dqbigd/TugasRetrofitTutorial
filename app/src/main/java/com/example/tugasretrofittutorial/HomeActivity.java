package com.example.tugasretrofittutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvContent;
    ProgressBar pbContent;

    private ArrayList<ContentEducationData> contentEducationDataArrayList = new ArrayList<>();
    private ContentEducationAdapter contentEducationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvContent = findViewById(R.id.rvContent);
        pbContent = findViewById(R.id.pbContent);

        loadContentEducation();

    }

    private void loadContentEducation() {
        rvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvContent.setAdapter(contentEducationAdapter);

        pbContent.setVisibility(View.VISIBLE);
        String token = SharedPrefManager.getKeyToken(getApplicationContext());

        Call<ContentEducationResponse> call = InitRetrofit.getInstance().contentEducation(token);
        call.enqueue(new Callback<ContentEducationResponse>() {
            @Override
            public void onResponse(Call<ContentEducationResponse> call, Response<ContentEducationResponse> response) {
                if (response.isSuccessful()) {
                    pbContent.setVisibility(View.GONE);
                    if (response.body() != null && response.body().getMessage().equals("Ok.")) {
                        if (response.body().getData().toString().equals("[]")) {
                            Toast.makeText(getApplicationContext(), "Kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            contentEducationDataArrayList = new ArrayList<>(response.body().getData());
                            contentEducationAdapter = new ContentEducationAdapter(contentEducationDataArrayList);
                            rvContent.setAdapter(contentEducationAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ContentEducationResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Mohon cek jaringan internet anda", Toast.LENGTH_SHORT).show();
                Log.d("Response Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}