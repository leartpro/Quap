package com.quap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.*;

public class UserdataReader {
    final Connection connection;
    Statement statement;

    public UserdataReader() throws SQLException, URISyntaxException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/postgres",
                "postgres",
                "password");
        statement = connection.createStatement();
    }

    //Sign up
    public String insertUser(String name, String password) {
        JSONObject json = new JSONObject();
        int userID = getUserID(name, null);
        if (userID != -1) { //user already exists
            json.put("error", "A user with this name already exists");
            return json.toString();
        } else {
            try {
                statement.executeUpdate("" +
                        "INSERT INTO users(name, password)" +
                        "VALUES(" + "'" + name + "'" + "," + "'" + password + "'" + ")"
                );
                json.put("success", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ""; //the new user settings as json string TODO
    }

    //Sign In
    public String verifyUser(String name, String password) {
        JSONObject json = new JSONObject();
        int userID = getUserID(name, password);
        if (userID == -1) {
            json.put("error", "Name or password is incorrect or there is already a user with this name!");
            return json.toString();
        } else {
            //json.put("data", dataByUser(userID)); //general userdata
            json.put("chats", chatsByUser(userID));
            json.put("friends", friendsByUser(userID));
        }
        return json.toString(); //user json
    }

    private int getUserID(String name, String password) {
        PreparedStatement statement = null;
        int userID = -1;
        String query = "" +
                "SELECT id FROM users WHERE " +
                "users.name = ?";
        if (password != null) {
            query += "AND users.password = ?";
        }
        query += ";";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            if (password != null) {
                statement.setString(2, password);
            }
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                userID = result.getInt("id");
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userID;
    }

    public boolean addChat(int userID, String chatName, boolean isPrivate) { //TODO: insert user by id
        try {
            statement.executeUpdate("" +
                    "INSERT INTO chatrooms(name, isPrivate)" +
                    "VALUES(" + chatName + "," + isPrivate + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void leaveChat(int chatID, int userID) { //TODO

    }

    public void updatePassword(int userID) { //TODO

    }

    public void updateName(int userID) { //TODO

    }

    public void addFriend(int userID, int friendID) {
        try {
            statement.executeUpdate("" +
                    "INSERT INTO friends(friend1_id, friend2_id)" +
                    "VALUES(" + userID + "," + friendID + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(int userID, int friendID) { //TODO

    }

    private JSONArray dataByUser(int id) { //TODO

        return new JSONArray();
    }

    private JSONArray chatsByUser(int id) {
        ResultSet result = null;
        JSONArray json = new JSONArray();
        try {
            result = statement.executeQuery("" +
                    "SELECT chatrooms.*, participants.created_at AS joined_at " +
                    "FROM participants LEFT JOIN chatrooms ON chatrooms.id = chatroom_id " +
                    "WHERE user_id = " + id
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (result.next()) {
                JSONObject chat = new JSONObject();
                int chatID = result.getInt("id");
                chat.put("id", chatID);
                chat.put("name", result.getString("name"));
                chat.put("is_private", result.getBoolean("is_private"));
                chat.put("created_at", result.getTimestamp("created_at"));
                chat.put("joined_at", result.getTimestamp("joined_at"));
                chat.put("participants", usersByChat(chatID));
                json.put(chat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    //Hilfsmethode für chatsByUser
    private JSONArray usersByChat(int chatID) {
        ResultSet result = null;
        JSONArray json = new JSONArray();

        try {
            result = statement.executeQuery("" +
                    "SELECT id, name " +
                    "FROM users " +
                    "LEFT JOIN participants " +
                    "ON participants.user_id = users.id " +
                    "WHERE chatroom_id =" + chatID + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (result.next()) {
                JSONObject user = new JSONObject();
                user.put("id", result.getInt("id"));
                user.put("name", result.getString("name"));
                json.put(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    private JSONArray friendsByUser(int id) { //TODO
        ResultSet result = null;
        JSONArray json = new JSONArray();

        return json;
    }


}
