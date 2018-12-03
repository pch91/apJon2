package com.example.scorp.apjon2.DAO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface Tasklistapi {

    @GET("tasks")
    Call<List<Task>> loaddescription(@Header("Authorization") String authorization);

}
