package com.example.tugasretrofittutorial;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    //Login
    @FormUrlEncoded
    @POST("auth/signin")
    Call<LoginResponse> signIn(@Field("username") String username,
                              @Field("password") String password);

    //Content Education (Home)
    @GET("/education")
    Call<ContentEducationResponse> contentEducation(@Header("Authorization") String token);


}
