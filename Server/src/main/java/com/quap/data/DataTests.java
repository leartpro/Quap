package com.quap.data;

import java.net.URISyntaxException;
import java.sql.SQLException;

public class DataTests {
    public static void main(String[] args) {
        try {
            UserdataReader dataReader = new UserdataReader();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
        ConfigReader reader = new ConfigReader();
    }
}
