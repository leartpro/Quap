package com.quap.data;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserdataReader {
    final Connection connection;
    Statement statement;

    public UserdataReader() throws SQLException, URISyntaxException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "password");
    }
    //neuer nutzer
    //verify nutzer

    public boolean insertUser(String name, String password) {
        return !userExists(name, password);
        //insert user here
    }
   public String verifyUser(String name, String password) {
        if(!userExists(name, password)) {
            JSONObject json = new JSONObject();
            json.put("error", "There is already a user with this name!");
            return json.toString();
        }

        return ""; //user json
   }

   private boolean userExists(String username, String password) {
        return true;
   }



}
