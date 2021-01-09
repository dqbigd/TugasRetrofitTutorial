package com.example.tugasretrofittutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    private void sendData() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Mengirimkan data");
        dialog.setMessage("Loading ...");
        dialog.setCancelable(true);
        dialog.show();

        final String sUsername = etUsername.getText().toString();
        final String sPassword = etPassword.getText().toString();

        Call<LoginResponse> call = InitRetrofit.getInstance().signIn(sUsername, sPassword);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Ok.")){
                        dialog.dismiss();

                        SharedPrefManager.setAccount(getBaseContext(), sUsername, sPassword);
                        String token = response.body().getData();

                        SharedPrefManager.setLoggedInStatus(getBaseContext(),true, token);
                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                        finish();

                    }else if(response.body().getMessage().equals("Not found.")){
                        Toast.makeText(MainActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Mohon cek username atau password anda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Mohon daftar / cek koneksi anda dahulu", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });
    }
}