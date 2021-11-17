module Client {
    requires java.sql;
    requires java.prefs;
    requires org.json;
    exports com.quap.client;
    exports com.quap.client.data;
}