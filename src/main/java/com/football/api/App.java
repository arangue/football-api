package com.football.api;

import static spark.Spark.*;

import org.javalite.activejdbc.*;
// import com.football.api.model.*;
import com.football.api.controller.*;

public class App {
    
    public static void main( String[] args ){

      port(4567);

      enableCORS("*","*","*");

      before((request, response) -> {
        if(!Base.hasConnection()){
          Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/football", "root", "root");
        }

      });

      after((request, response) -> {
        if(Base.hasConnection()) Base.close();
        response.type("application/json"); 
      });

      get("/import-league/:leagueCode", ImportController::importLeague);

      get("/total-players/:leagueCode", PlayerController::totalPlayers);

    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

      options("/*", (request, response) -> {
      /**/
          String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
          if (accessControlRequestHeaders != null) {
              response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
          }

          String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
          if (accessControlRequestMethod != null) {
              response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
          }

          return "OK";
      });

      before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
      });
    }

}