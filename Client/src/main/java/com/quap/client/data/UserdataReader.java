package com.quap.client.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserdataReader{
    final Connection connection;
    Statement statement;

    public UserdataReader(String username) throws SQLException {
        assert username != null;
        connection = DriverManager.getConnection("/sqlite/db/" + username + ".db");
        initDB();
    }

    private void initDB() throws SQLException {
        statement = connection.createStatement();
        statement.setQueryTimeout(20);

        statement.executeUpdate("create table if not exists chats (" +
                "id int not null primary key," +
                "name varchar primary key" +
                ")"
        );

        statement.executeUpdate("create table if not exists messages (" +
                "id int not null primary key," +
                "chat_id int not null references chats on delete cascade," +
                "isPrivate int check (isPrivate = 0 or isPrivate = 1)," +
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
