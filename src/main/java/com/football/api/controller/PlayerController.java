package com.football.api.controller;

import com.football.api.service.PlayerService;
import com.google.gson.JsonObject;
import spark.*;

public class PlayerController {

    public static JsonObject totalPlayers(Request request, Response response) {

        return PlayerService.playerService(request, response);    

    }
    
}
