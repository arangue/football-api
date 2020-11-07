package com.football.api.service;

import java.sql.SQLException;
import java.util.List;

import com.football.api.model.CompetitionsTeams;
import com.football.api.model.TeamsPlayers;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;

public class PlayerService {

	public static JsonObject playerService(Request request, Response response) {

        String league = request.params(":leagueCode");
        String total = "";
        JsonObject msg = new JsonObject();

        if(!checkInDatabase(league)){
            response.status(404);
            msg.addProperty("message","Not Found");
            return msg;
        }

        total = String.valueOf(totalplayers(league));
        // JsonObject msgSuccess = new JsonObject();
        response.status(200);
        msg.addProperty("message",total);
        return msg;
	}

    private static boolean checkInDatabase(String league) {
        return Competition.findFirst("code = ?", league) != null;
    }

    private static int totalplayers(String league) {
        int count = 0;
        Competition c = new Competition();
        c = Competition.findFirst("code = ?", league);
        int idLeague = c.getInteger("id");
        List<CompetitionsTeams> ct = CompetitionsTeams.where("competition_id = ?", idLeague);

        for(CompetitionsTeams cteams : ct){
            int teamId = cteams.getInteger("team_id");
            List<TeamsPlayers> teamPlayers = TeamsPlayers.where("team_id = ?", teamId);
            
            count += teamPlayers.size();
        }
    
        return count;
    }
}
