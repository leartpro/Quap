package com.quap.data;

import org.json.JSONArray;
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
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/postgres",
                "postgres",
                "password");
    }

    //Sign up
    public boolean insertUser(String name, String password) {
        if(userExists(name, password)) {
            return false;
        } else {
            try {
                statement.executeUpdate("" +
                        "INSERT INTO users(name, password)" +
                        "VALUES(" + name + "," + password + ")"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //Sign In
   public String verifyUser(String name, String password) {
       JSONObject json = new JSONObject();
        if(!userExists(name, password)) {
            json.put("error", "There is already a user with this name!");
            return json.toString();
        } else {
            json.put("data", dataByUser(name));
            json.put("chats", chatsByUser(name));
            json.put("friends", friendsByUser(name));
        }
        return json.toString(); //user json
   }

    public void addChat(String name, boolean isPrivate) {
        try {
            statement.executeUpdate("" +
                    "INSERT INTO chatrooms(name, isPrivate)" +
                    "VALUES(" + name + "," + isPrivate + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }

   public void leaveChat(int chat_id, int user_id) { //TODO

   }

    public void updatePassword() { //TODO

    }

    public void updateName() { //TODO

    }

    public void addFriend(String name, String friend) {
        try {
            statement.executeUpdate("" +
                    "INSERT INTO friends(friend1_id, friend2_id)" +
                    "VALUES(" + IDByName(name) + "," + IDByName(friend) + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend() { //TODO

    }

    private boolean userExists(String username, String password) { //TODO:
        return true;
   }

   private JSONArray dataByUser(String name) { //TODO

       return new JSONArray();
   }

   private JSONArray chatsByUser(String name) { //TODO

        return new JSONArray();
   }

    private JSONArray friendsByUser(String name) { //TODO

        return new JSONArray();
    }

    private String IDByName(String name) { //TODO
        return "";
    }





}
