package com.quap.client.data;

import com.quap.client.domain.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    }

    public List<Message> getMessagesByChat(int id) {
        try {
            statement.executeQuery("" +
                    "select sender, content, created_at from messages " +
                    "where chat_id = " + id + " " +
                    "order by created_at"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addMessage(int chat_id) {

    }

    public void getPrivateChats() {

    }

    public void getChatrooms() {

    }

    public void updateChats() {

    }
}
