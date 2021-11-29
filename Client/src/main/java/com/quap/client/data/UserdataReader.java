package com.quap.client.data;

import com.quap.client.domain.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserdataReader{
    private Connection connection;
    private Statement statement;

    public UserdataReader(String username, String password) {
        assert username != null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "Client/src/main/resources/com/quap/users/" + username + "/sqlite/db/messages.db", username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            initDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDB() throws SQLException {
        statement = connection.createStatement();
        statement.setQueryTimeout(20);

        statement.executeUpdate("create table if not exists messages (" +
                "id integer primary key autoincrement," +
                "chat_id int not null," +
                "sender int not null," +
                "created_at timestamp not null default current_timestamp," +
                "content varchar not null)"
                );

        statement.executeUpdate("create index if not exists newestMessage on messages(created_at)");
        statement.executeUpdate("create index if not exists by_id on messages(id)");
    }

    public List<Message> getMessagesByChat(int id) {
        List<Message> messages = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("" +
                    "select sender, content, created_at from messages " +
                    "where chat_id = " + id + " " +
                    "order by created_at"
            );
            while(result.next()) {
                messages.add(new Message(result.getString("content"),
                        result.getDate("created_at"),
                        result.getInt("sender")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public void addMessage(int chat_id, int sender_id, String content) { //TODO: prevent sql injections
        try {
            statement.executeQuery("" +
                    "INSERT INTO messages(chat_id, sender_id, content) " +
                    "VALUES(" + chat_id + ", " + sender_id + ", " + content + ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPrivateChats() {

    }

    public void getChatrooms() {

    }

    public void updateChats() {

    }
}
