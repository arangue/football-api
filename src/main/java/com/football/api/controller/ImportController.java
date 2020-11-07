package com.football.api.controller;

import com.football.api.service.*;
import com.google.gson.JsonObject;

import spark.*;

public class ImportController {
	
	public static JsonObject importLeague (Request request, Response response) {

		String league = request.params(":leagueCode");

		int status = ImportService.importService(league);

		response.status(status);
		return getOutputMessage(status);
      }

	private static JsonObject getOutputMessage(int status) {
		JsonObject msg = new JsonObject();
		switch(status) {
			case 201 : 	msg.addProperty("message", "Successfully imported");
					   	break;
			case 409 : 	msg.addProperty("message", "League already imported");
					   	break;
			case 404 : 	msg.addProperty("message", "Not found");
						break;
			case 504 : 	msg.addProperty("message", "Server error");
						break;
			default  : msg.addProperty("message", "Internal server error");
		}
		return msg;
	}
}