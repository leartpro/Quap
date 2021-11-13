package com.quap.data;

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



}
