package com.quap.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

//TODO: use this configuration on server startup
public class ConfigReader {

    public Set<Map.Entry<String, String>> getProperties() {
        OrderedProperties props = new OrderedProperties();
        InputStream propsInput = ClassLoader
                .getSystemResourceAsStream("Server/src/main/resources/com/quap/config/config.properties");
        if (propsInput != null) {
            try {
                props.load(propsInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new LinkedHashSet<>(props.entrySet());
    }
}
