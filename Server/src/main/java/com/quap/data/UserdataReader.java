package com.quap.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserdataReader {
    final Connection connection;
    Statement statement;

    public UserdataReader() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost/postgres",
                "postgres",
                "postgres");
        statement = connection.createStatement();
    }

    public JSONObject insertUser(String name, String password) {
        System.out.println("insertUser(" + name + "," + password + ")");
        PreparedStatement statement;
        JSONObject json = new JSONObject();
        json.put("return-value", "authentication");
        String query = "" +
                "INSERT INTO users(name, password)" +
                "VALUES(?,?)";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int userID = getUserID(name, null);
        if (userID == -1) {
            json.put("error", "User already exists");
            return json;
        } else {
            JSONObject data = new JSONObject();
            data.put("id", userID);
            data.put("chatrooms", chatroomsByUser(userID));
            data.put("private", chatsByUser(userID));
            json.put("data", data);
        }
        return json;
    }

    public JSONObject verifyUser(String name, String password) {
        System.out.println("verifyUser(" + name + "," + password + ")");
        JSONObject json = new JSONObject();
        json.put("return-value", "authentication");
        int userID = getUserID(name, password);
        if (userID == -1) {
            json.put("error", "Request was rejected, because the given name or password is incorrect");
            return json;
        } else {
            JSONObject data = new JSONObject();
            data.put("id", userID);
            data.put("chatrooms", chatroomsByUser(userID));
            data.put("private", chatsByUser(userID));
            json.put("data", data);
        }
        return json;
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

    public JSONObject addChat(int userID, String chatName, boolean isPrivate) {
        JSONObject json = new JSONObject();
        int chatID = -1;
        PreparedStatement statement;
        String query = "" +
                "INSERT INTO chatrooms(name, is_private)" +
                "VALUES(?,?)";
        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, chatName);
            statement.setBoolean(2, isPrivate);
            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                chatID = result.getInt("id");
                json.put("chatroom_id", chatID);
                json.put("name", chatName);
            }
            result.close();
            query = "" +
                    "INSERT INTO participants(user_id, chatroom_id) " +
                    "VALUES(?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setInt(2, chatID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("DB-json:" + json);
        return json;
    }

    public void deleteUserFromChat(int userID, int chatID) {
        PreparedStatement statement;
        String query = "" +
                "DELETE FROM participants " +
                "WHERE user_id = ? " +
                "AND chatroom_id = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setInt(2, chatID);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JSONArray chatsByUser(int id) {
        ResultSet result = null;
        JSONArray json = new JSONArray();
        try {
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
        return getObjects(result, json);
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
                    "WHERE user_id = " + id + " " +
                    "and chatrooms.is_private = false"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getObjects(result, json);
    }

    public List<Integer> userIDsByChat(int chatID) {
        List<Integer> userIDs = new ArrayList<>();
        ResultSet result = null;

        try {
            result = statement.executeQuery("" +
                    "SELECT user_id " +
                    "FROM participants " +
                    "WHERE chatroom_id =" + chatID + " " +
                    "UNION SELECT friend1_id " +
                    "FROM friends " +
                    "WHERE chat_id =" + chatID + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            while (result.next()) {
                userIDs.add(result.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert result != null;
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userIDs;
    }

    public JSONArray usersByChat(int chatID) {
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
        return getObjects(result, json);
    }

    private JSONArray getObjects(ResultSet result, JSONArray json) {
        try {
            assert result != null;
            json = resultSetToJSONArray(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert result != null;
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    private JSONArray resultSetToJSONArray(ResultSet rs) throws SQLException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        while (rs.next()) {
            int numColumns = rsMetaData.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsMetaData.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }


    public int userIDByName(String username) {
        int userID = -1;
        ResultSet result = null;

        try {
            result = statement.executeQuery("" +
                    "SELECT users.id " +
                    "FROM users " +
                    "WHERE users.name = '" + username + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            if (result.next()) {
                userID = result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert result != null;
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userID;
    }

    public JSONObject getChatByID(int chatID) {
        ResultSet result = null;
        JSONObject json = new JSONObject();

        try {
            result = statement.executeQuery("" +
                    "SELECT chatrooms.* " +
                    "FROM chatrooms " +
                    "WHERE chatrooms.id = " + chatID + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            json = resultSetToJSONArray(result).getJSONObject(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert result != null;
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public void addUserToChat(int chatID, int senderID) {
        PreparedStatement statement;
        String query = "" +
                "INSERT INTO participants(user_id, chatroom_id) " +
                "VALUES(?,?)";
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, senderID);
            statement.setInt(2, chatID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertFriends(int senderID, int friend_id) {
        PreparedStatement statement;
        String query = "" +
                "WITH chat_insert " +
                "         AS ( " +
                "        INSERT INTO chatrooms (name, is_private) " +
                "            VALUES (?, true) " +
                "            RETURNING chatrooms.id " +
                "    ) " +
                "INSERT INTO friends(friend1_id, friend2_id, chat_id) " +
                "VALUES (?, ?, ( " +
                "    SELECT id " +
                "    FROM chat_insert " +
                ")), " +
                "        (?, ?, ( " +
                "            SELECT id " +
                "            FROM chat_insert " +
                "        ))";
        int chatID = -1;
        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "privateChat-" + senderID + "-" + friend_id + "/" + friend_id + "-" + senderID);
            statement.setInt(2, senderID);
            statement.setInt(3, friend_id);
            statement.setInt(4, friend_id);
            statement.setInt(5, senderID);
            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                chatID = result.getInt("chat_id");
            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return chatID;
        }
        return chatID;
    }

    public String userNameById(int userID) {
        String username = "";
        ResultSet result = null;

        try {
            result = statement.executeQuery("" +
                    "SELECT users.name " +
                    "FROM users " +
                    "WHERE users.id = '" + userID + "';"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert result != null;
            if (result.next()) {
                username = result.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert result != null;
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    public void unfriendUsers(int friendID, int senderID, int chatID) {

    }
}
