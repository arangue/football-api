package com.football.api.exception;

import com.google.gson.JsonObject;

public class NotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int status;

    public NotFoundException(int status, Throwable cause) {
        super(cause.getMessage(), cause);
        this.status = status;
    }

    public NotFoundException(int status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }     

    public String getMsg(){
        JsonObject json = new JsonObject();
        json.addProperty("message", this.getMessage());
        json.addProperty("status", this.status);   
        return json.toString();
    }
}

// public class StudentNotFoundException extends Exception {
 
//     public StudentNotFoundException(String message) {
//         super(message);
//     }
// }