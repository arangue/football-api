package com.football.api.service;

import org.javalite.activejdbc.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.football.api.exception.NotFoundException;
import com.football.api.model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import spark.*;

public class LeagueService {

    // private static List<Integer> idList = new ArrayList<>();

    public static boolean checkInDatabase(String league) {

        return Competition.findFirst("code = ?", league) != null;

    };

    public static boolean teamExistOnDB(int team) {

        return Team.findById(team) != null;
        
    };

    public static boolean playerExistOnDB(int player) {

        return Player.findById(player) != null;
        
    };
    

    public static void insertCompetition(JsonObject competitions) throws SQLException {

        int id = competitions.get("id").getAsInt();
        String leagueName = competitions.get("name").getAsString();
        String leagueCode = competitions.get("code").getAsString();
        String areaName = competitions.getAsJsonObject("area").get("name").getAsString();

        try {
            Competition c = new Competition();
            c.setId(id);
            c.set("name", leagueName);
            c.set("code", leagueCode);
            c.set("areaName", areaName);
            c.insert();

            
            JsonObject teams = APIClientService.getTeamsFromApi(leagueCode);
            insertTeam(teams, id);

        } catch (Exception e) {
            e.getMessage();
            throw new SQLException();
        }

    };

    public static void insertTeam(JsonObject t, int idComp) throws SQLException {
        List<Integer> idList = new ArrayList<>();
        JsonArray teams = t.get("teams").getAsJsonArray();

        try {
            for (JsonElement e : teams) {
                int teamId = e.getAsJsonObject().get("id").getAsInt();
                
                if (!teamExistOnDB(teamId)) {
                    idList.add(teamId);
                    
                    String id = e.getAsJsonObject().get("id").getAsString();
                    String name = e.getAsJsonObject().get("name").getAsString();
                    String tla = e.getAsJsonObject().get("tla").getAsString();
                    String shortName = e.getAsJsonObject().get("shortName").getAsString();
                    JsonElement email = e.getAsJsonObject().get("email");
                    boolean isEmailNull = email.isJsonNull();
                    String areaName = e.getAsJsonObject().get("area").getAsJsonObject().get("name").getAsString();

                    Competition competition = Competition.findById(idComp);
                    Team team = new Team();
                    team.setId(id);
                    team.set("name", name);
                    team.set("tla", tla);
                    team.set("shortName", shortName);
                    if(!isEmailNull) team.set("email", email.getAsString());
                    team.set("areaName", areaName);
                    team.insert();
                    competition.add(team);

                    
                } else {
                    //Insert relation between Competition and Team in join table
                    //since we don't insert again if Team was already in DB
                    Competition competition = Competition.findById(idComp);
                    Team team = Team.findById(teamId);
                    competition.add(team);
                }
            }
            //After inserting team, we call the method to insert players and the relationship between players and teams
            insertPlayer(idList);
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
            throw new SQLException();
        }

    }

    public static void insertPlayer(List<Integer> idList)
            throws IOException, InterruptedException, NotFoundException, SQLException {
        for(int idTeam : idList) {

            JsonObject squad = APIClientService.getTeamsPlayersFromAPI(idTeam);
            JsonArray players = squad.getAsJsonArray("squad");

            try {
                for (JsonElement t : players) {
                    String role = t.getAsJsonObject().get("role").getAsString();

                    int idPlayer = t.getAsJsonObject().get("id").getAsInt();
                    if("PLAYER".equals(role)){
                        
                        if(!playerExistOnDB(idPlayer)){
                            String name = t.getAsJsonObject().get("name").getAsString();
                            String position =  getNullAsEmptyString(t.getAsJsonObject().get("position"));
                            String dobString = getNullAsEmptyString(t.getAsJsonObject().get("dateOfBirth"));
                            String countryOfBirth = t.getAsJsonObject().get("countryOfBirth").getAsString();
                            String nationality = t.getAsJsonObject().get("nationality").getAsString();

                            Team team = Team.findById(idTeam);
                            Player player = new Player();
                            player.setId(idPlayer);
                            player.set("name",name);
                            if(position != null) player.set("position",position);
                            if(dobString != null) player.set("dateOfBirth",convertDate(dobString));
                            player.set("countryOfBirth",countryOfBirth);
                            player.set("nationality",nationality);
                            player.insert();
                            team.add(player);

                        } else {
                            Team team = Team.findById(idTeam);
                            Player player = Player.findById(idPlayer);
                            team.add(player);
                        }
                    }
                }
    
            } catch (Exception e1) {
                System.out.println(e1.getStackTrace().toString());
                throw new SQLException();
            }        
        }
    }
    
    private static String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? null : jsonElement.getAsString();
    }

    private static LocalDate convertDate (String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }
    
}
