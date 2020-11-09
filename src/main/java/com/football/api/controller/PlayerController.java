package com.football.api.controller;

import java.sql.SQLException;

import com.football.api.exception.NotFoundException;
import com.football.api.service.PlayerService;
import com.google.gson.JsonObject;
import spark.*;

public class PlayerController {

    public static JsonObject totalPlayers(Request request, Response response) {

        String league = request.params(":leagueCode");
        try{ 
            int total = PlayerService.countPlayersByLeague(league);   
            return getJson(String.valueOf(total), 200, response);
        }catch(NotFoundException e){            
            return getJson("Not Found", 404, response);
        }catch(SQLException e){
            return getJson("Internal Server Error", 500, response);
        }
    }

    private static JsonObject getJson(String msg, int status, Response response){
        JsonObject json = new JsonObject();
        json.addProperty("message", msg);
        response.status(status);
        return json; 
    }     
}
