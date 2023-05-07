package com.quap.data;

import java.io.IOException;

public class DataTests {
    public static void main(String[] args) {
        ConfigReader reader = new ConfigReader();
        try {
            reader.loadDefaultProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
