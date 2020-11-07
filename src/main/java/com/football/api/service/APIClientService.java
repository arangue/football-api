package com.football.api.service;

import spark.*;

import com.football.api.exception.NotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.*;
import java.net.http.*;

public class APIClientService {
    
    private static final String HOST = "https://api.football-data.org";
    private static final String API_KEY = "bcad86d274ae489ab890992653d3d929";

    public static JsonObject getLeagueFromApi(String league) throws IOException, InterruptedException, NotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(HOST+"/v2/competitions/"+league))
        .header("X-Auth-Token", API_KEY).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        
        if(response.statusCode() == 404) 
            throw new NotFoundException(404, "Not found",null);
        
        // System.out.println("Status "+response.statusCode());
        
        //ver status del response. Si es 404 tirar excepttion y capturar en otro service para generar msg
        
        
        JsonObject convertedObject = new Gson().fromJson(response.body(), JsonObject.class);
        // System.out.println("Competition traida de API "+convertedObject.toString());
        
        
        return convertedObject;
        
        
        // JsonObject jsonObject = new JsonParser().parse(response.body()).getAsJsonObject();
        // return "League " + req.params(":leagueCode");
        
        
        //Usar LeagueService
        
    }
    
    public static JsonObject getTeamsFromApi(String league) throws IOException, InterruptedException, NotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(HOST+"/v2/competitions/"+league+"/teams"))
        .header("X-Auth-Token", API_KEY)
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 404)
        throw new NotFoundException(404, "Not found", null);
        
        // boolean success = response.statusCode() == 200;
        // boolean success = false;
        // while(!success) {
        //     response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //     int status = response.statusCode();
        //     success = (status == 200);
        //     if (!success) {
        //         if(status == 429) {
        //             Thread.sleep(5000); // wait 2 seconds before retrying
        //         } else {
        //             throw new RuntimeException("Something went wrong: HTTP status: " + status);
        //         }
        //     }
        // }

        // System.out.println("Status " + response.statusCode());

        JsonObject convertedObject = new Gson().fromJson(response.body(), JsonObject.class);

        return convertedObject;

    }


    public static JsonObject getTeamsPlayersFromAPI(int teamId) throws IOException, InterruptedException, NotFoundException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(HOST+"/v2/teams/"+teamId))
        .header("X-Auth-Token", API_KEY).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 404)
            throw new NotFoundException(404, "Not found", null);
        // boolean success = response.statusCode() == 200;
        boolean success = false;
        while(!success) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            success = (status == 200);
            if (!success) {
                if(status == 429) {
                    Thread.sleep(1000);
                } else {
                    throw new RuntimeException("Something went wrong: HTTP status: " + status);
                }
            }
        }
        // System.out.println("Status Players " + response.statusCode());

        JsonObject convertedObject = new Gson().fromJson(response.body(), JsonObject.class);


        // System.out.println(convertedObject.toString());

        return convertedObject;

    }



}
