package com.diego.securitysystem;

import com.diego.securitysystem.models.HistoryLog;
import com.diego.securitysystem.models.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface SecurityApi {
    String SERVER_URL = "https://superapi.netlify.app/api/";

    @GET("log")
    Call<List<HistoryLog>> getHistoryLog();

    @GET("state")
    Call<String> getState();

    @PUT("state")
    Call<Void> putState(@Body State state);
}
