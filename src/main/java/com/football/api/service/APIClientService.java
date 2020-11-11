package com.football.api.service;

import com.football.api.exception.NotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class APIClientService {
    
    private static final String HOST = "https://api.football-data.org";
    private static final String API_KEY = "bcad86d274ae489ab890992653d3d929";
    
    public static JsonObject getLeagueFromApi(String league) throws IOException, InterruptedException, NotFoundException {
        OkHttpClient client = new OkHttpClient();
        String url = HOST+"/v2/competitions/"+league;
        Request request = new Request.Builder()
        .url(url)
        .header("X-Auth-Token", API_KEY)
        .build();
        Response response = client.newCall(request).execute();
        
        if (response.code() == 404)
            throw new NotFoundException(404, "Not found", null);
        
        String body = response.body().string();
        response.body().close();
        JsonObject convertedObject = new Gson().fromJson(body, JsonObject.class);
        return convertedObject; 
    }
    
    public static JsonObject getTeamsFromApi(String league) throws IOException, InterruptedException, NotFoundException {
        OkHttpClient client = new OkHttpClient();
        String url = HOST+"/v2/competitions/"+league+"/teams";
        Request request = new Request.Builder()
        .url(url)
        .header("X-Auth-Token", API_KEY)
        .build();
        Response response = client.newCall(request).execute();
        
        if (response.code() == 404)
            throw new NotFoundException(404, "Not found", null);
        
        String body = response.body().string();
        response.body().close();
        JsonObject convertedObject = new Gson().fromJson(body, JsonObject.class);
        return convertedObject;

    }

    public static JsonObject getTeamsPlayersFromAPI(int teamId) throws IOException, InterruptedException, NotFoundException {
        OkHttpClient client = new OkHttpClient();
        String url = HOST+"/v2/teams/"+teamId;
        Request request = new Request.Builder()
        .url(url)
        .header("X-Auth-Token", API_KEY)
        .build();
        Response response = client.newCall(request).execute();

        if (response.code() == 404)
            throw new NotFoundException(404, "Not found", null);

        boolean success = false;
        while(!success) {
            response = client.newCall(request).execute();            
            int status = response.code();    
            success = (status == 200);
            if (!response.isSuccessful()) {
                if(response.code() == 429) {
                    Thread.sleep(5000);
                } else {
                    throw new RuntimeException("Something went wrong: HTTP status: " + response.code());
                }
            }
        }

        String body = response.body().string();
        response.body().close();
        JsonObject convertedObject = new Gson().fromJson(body, JsonObject.class);
        return convertedObject;
    }
}
