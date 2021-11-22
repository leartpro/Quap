package com.quap.client.data;

public class DataTests {
    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader("Otto");
        //TODO define readUser()
        configReader.createUser();
        configReader.readUser();
    }
}
