package com.quap.client.data;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserdataReader{
    final Connection connection;
    Statement statement;

    public UserdataReader(String username) throws SQLException, URISyntaxException {
        assert username != null;
 //       URL resource = UserdataReader.class.getResource("/com/quap/users/exampleUser1/sqlite/db");
        connection = DriverManager.getConnection("jdbc:sqlite:" + "Client/src/main/resources/com/quap/users/" + username + "/sqlite/db/messages.db");
        initDB();
    }

    private void initDB() throws SQLException {
        statement = connection.createStatement();
        statement.setQueryTimeout(20);

        statement.executeUpdate("create table if not exists messages (" +
                "id int primary key autoincrement," +
                "chat_id int not null," +
                "sender int not null," +
                "created_at timestamp not null default current_timestamp," +
                "content varchar not null)"
                );

        statement.executeUpdate("create index if not exists newestMessage on messages(created_at)");
    }

    public void getMessagesByChat(int id) {

    }

    public void addMessage() {

    }

    public void getPrivateChats() {

    }

    public void getChatrooms() {

    }

    public void updateChats() {

    }
}
