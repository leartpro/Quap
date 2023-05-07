![Quap](./Client/DesktopApp/src/main/resources/com/quap/images/splashBackground.jpg)

INTRODUCTION
------------

This project is a messenger for the local network, written in Java, taking into account the avoidance of data storage.

The project was developed as part of my term paper.

To ensure user-friendliness, the application has a graphical user interface through which the user can join chat rooms, 
write with friends and all this anonymously.

* For more projects, visit my [personal page](https://lennartprotte.tech/)

REQUIREMENTS
------------

This project requires the following installs:

* [JDK-16+](https://jdk.java.net)
* [Postgres](https://www.postgresql.org/)
* [JavaFx SDK](https://gluonhq.com/products/javafx/)

INSTALLATION
------------

Es wird eine Postgres und eine Java, als auch eine JavaFX Installation vorausgesetzt.

```sh
    git clone https://github.com/leartpro/Quap.git
```

### SERVER

Nun kann im Verzeichnis `Server/resources/com/quap/config` die Datei
`config.properties` angepasst werden (siehe [Configuration](#server-1)).
In dieser muss neben den Socket Daten auch die Datenbank Verbindung festgelegt werden.

Das Datenbankschema kann wie folgt aufgesetzt werden:

```sh
     psql -h <IP> -U <NAME> -f create-db.sql
```

Nun kann im Verzeichnis `Server` folgender Befehl zum Kompilieren verwendet werden:

```sh
    mvn clean install
```

Es sollte nun eine ausführbare `.jar`-Datei im Verzeichnis `Server/target/` zu finden sein.
Diese kann wie folgt ausgeführt werden:

```sh
    java -jar <NAME_OF_FILE>
```

### CLIENT

Go to DesktopApp_jar and execute the jar with the following VM options:
    ```sh
    --module-path \"PATH TO YOUR JAVA FX SDK"\javafx-sdk-17.0.0.1\lib --add-modules=javafx.controls,javafx.fxml
    ```

CONFIGURATION
-------------

TODO: hier schreiben, was Client braucht an config und was Server braucht an config

### SERVER

The [config file](./Server/src/main/resources/com/quap/config/config.properties) contains

Im Falle von unbeabsichtigten Veränderungen kann auf das folgende Template zurückgegriffen werden:
```properties
    #DATABASE
    database-postgres-name=postgres@localhost
    database-postgres-socket=localhost\:5432
    database-postgres-username=postgres
    database-postgres-password=password
    #RUNTIME
    runtime-maxThreads=null
    #NETWORK
    network-prefer-protocol=java.net.preferIPv4Stack
    network-adapter-name=Ethernet
    network-ip-address=192.168.56.1
    network-subnet-mask=255.255.0.0
    network-default-gateway=192.168.178.1
    #SOCKET
    network-socket-hostname=192.168.178.69
    network-socket-port=8192
    network-socket-backlog=0
```

### CLIENT



FAQ
---

<font size="3">Q</font> : 
I do not receive messages when I am offline. Is this normal?

<font size="3">A</font>: 
Yes, Quap is a livechat to collect as little data as possible.

<font size="3">Q</font>: 
I changed my device, and now I don't see my messages. Have they been deleted?

<font size="3">A</font>: 
Your messages are only stored on your device and give you absolute control over your data. 
The ability to transfer your messages to another device is currently under development.


The Outlook
-----------

* #### Planned features are:
  - decentralized data synchronization
  - accessibility in the local network via DNS
  - live server console
  - profile and settings for users

* #### Term Paper:
  - this project is the practical part of my term paper