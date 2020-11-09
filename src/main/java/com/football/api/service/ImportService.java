package com.football.api.service;

import com.football.api.exception.NotFoundException;
import com.google.gson.JsonObject;

// import spark.*;

import java.io.IOException;
import java.sql.SQLException;

public class ImportService {

    public static int importService(String league) {

        if (LeagueService.checkInDatabase(league))
            return 409;
        try {
            JsonObject competitions = APIClientService.getLeagueFromApi(league);
            
            LeagueService.insertCompetition(competitions);

        } catch (IOException | InterruptedException | SQLException e) {
            return 504;
        } catch (NotFoundException e) {
            return 404;
        } 
		return 201; 
    }

}
