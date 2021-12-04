package com.quap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public String insertUser(String name, String password) { //TODO: message format
        System.out.println("insertUser(" + name + "," + password + ")");
        PreparedStatement statement;
        JSONObject json = new JSONObject();
        json.put("return-value", "void");
        String query = "" +
                "INSERT INTO users(name, password)" +
                "VALUES(?,?)";
        int userID = getUserID(name, null);
        if (userID != -1) { //user already exists
            json.put("error", "User already exists");
            return json.toString();
        } else {
            try {
                statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, password);
                statement.executeUpdate();
                json.put("data", "null");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return json.toString();
    }

    //Sign In
    public String verifyUser(String name, String password) { //TODO: message format
        System.out.println("verifyUser(" + name + "," + password + ")");
        JSONObject json = new JSONObject();
        json.put("return-value", "authentication");
        int userID = getUserID(name, password);
        if (userID == -1) {
            json.put("error", "Request was rejected, because the given name or password is incorrect");
            return json.toString();
        } else {
            JSONObject data = new JSONObject();
            data.put("id", userID);
            data.put("chatrooms", chatroomsByUser(userID));
            data.put("private", chatsByUser(userID));
            json.put("data", data);
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
                assert statement != null;
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

    private JSONArray chatsByUser(int id) {
        ResultSet result = null;
        JSONArray json = new JSONArray();
        try {
            //no problemo
            result = statement.executeQuery("" +
                    "SELECT users.name, users.id AS user_id, chatrooms.id AS chatrooms_id, chatrooms.created_at " +
                    "FROM friends " +
                    "LEFT JOIN users " +
                    "ON users.id = friends.friend2_id " +
                    "LEFT JOIN chatrooms " +
                    "ON chatrooms.id = friends.chat_id " +
                    "WHERE friend1_id = " + id
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            json = resultSetToJSONArray(result);
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

    private JSONArray chatroomsByUser(int id) {
        ResultSet result = null;
        JSONArray json = new JSONArray();
        try {
            result = statement.executeQuery("" +
                    "SELECT chatrooms.id, chatrooms.name, chatrooms.created_at, participants.created_at " +
                    "AS joined_at " +
                    "FROM participants " +
                    "LEFT JOIN chatrooms " +
                    "ON chatrooms.id = chatroom_id " +
                    "WHERE user_id = " + id
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            json = resultSetToJSONArray(result);
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

    public List<Integer> userIDsByChat(int chatID) {
        List<Integer> userIDs = new ArrayList<>();
        ResultSet result = null;

        try {
            result = statement.executeQuery("" + //TODO ???
                    "SELECT user_id " +
                    "FROM participants " +
                    "WHERE chatroom_id =" + chatID + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            while(result.next()) {
                userIDs.add(result.getInt("user_id"));
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
        return userIDs;
    }

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
            assert result != null;
            json = resultSetToJSONArray(result);
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

    /*private JSONArray friendsByUser(int id) { //returns in current time the chats instead of friends
        ResultSet result = null;
        JSONArray json = new JSONArray();
        try {
            result = statement.executeQuery("" +
                    "SELECT users.id, users.name " +
                    "FROM users " +
                    "LEFT JOIN friends " +
                    "ON users.id = friends.friend2_id " +
                    "WHERE friend1_id = " + id
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            json = resultSetToJSONArray(result);
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
    }*/

    private JSONArray resultSetToJSONArray(ResultSet rs) throws SQLException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }


}
