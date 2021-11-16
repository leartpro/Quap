package com.quap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.*;

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
       JSONObject json = new JSONObject();
        if(!userExists(name, password)) {
            json.put("error", "There is already a user with this name!");
            return json.toString();
        }
        int userID = getIdbyName(name);
        json.put("chats", chatsByUserID(userID));
        json.put("friends", friendsByUserID(userID));
        return json.toString(); //user json
   }

    public void addChat() {

   }

   public void leaveChat() {

   }

    public void updatePassword() {

    }

    public void updateName() {

    }

    public void addFriend() {

    }

    public void removeFriend() {

    }

    private int getIdbyName(String name) {
        try {
            final ResultSet result = statement.executeQuery(
                    "SELECT * FROM users" +
                    "");
            return result.getInt("id()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean userExists(String username, String password) {
        return true;
   }

   private JSONArray chatsByUserID(int id) {

        return new JSONArray();
   }

    private JSONArray friendsByUserID(int userID) {

        return new JSONArray();
    }




}
