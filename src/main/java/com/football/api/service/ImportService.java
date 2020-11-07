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
        // LeagueService.checkInDatabase(league);
        try {
            JsonObject competitions = APIClientService.getLeagueFromApi(league);
            // JsonObject teams = APIClientService.getTeamsFromApi(league);
            
            LeagueService.insertCompetition(competitions);
            // LeagueService.insPlayer();
            // LeagueService.insertTeam(teams);

            // Competition c = Competition.findById(2003);
            // c.add(Team.findById(672));
            // LeagueService.insertRelationCompetitionTeam(competitions,teams);
           
            // Insertar en BD usando el LeagueService
        } catch (IOException | InterruptedException | SQLException e) {
            return 504;
        } catch (NotFoundException e) {
            return 404;
        } 
		return 201; //llamada al service que llama api externa que esta dentro de ImportFromApiService
    }

}
