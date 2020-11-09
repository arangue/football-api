package com.football.api.service;

import java.sql.SQLException;
import java.util.List;

import com.football.api.exception.NotFoundException;
import com.football.api.model.*;

public class PlayerService {

	public static int countPlayersByLeague(String league) throws NotFoundException, SQLException {
        
        if(!checkInDatabase(league)){
            throw new NotFoundException(404, "Not found", null);
        }

        return totalplayers(league);

	}

    private static boolean checkInDatabase(String league) {
        return Competition.findFirst("code = ?", league) != null;
    }

    private static int totalplayers(String league) throws SQLException{
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