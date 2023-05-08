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
* [SQLite](https://www.sqlite.org/download.html)

INSTALLATION
------------

Es wird eine Postgres, eine SQLite und eine Java, als auch eine JavaFX Installation vorausgesetzt.

```sh
    git clone https://github.com/leartpro/Quap.git
```

### SERVER

Nun kann im Verzeichnis `Server/resources/com/quap/config` die Datei
[config.properties](./Server/src/main/resources/com/quap/config/config.properties) angepasst werden.
In dieser muss neben den Socket Daten auch die Datenbank Verbindung festgelegt werden.

Im Falle von unbeabsichtigten Veränderungen kann auf das folgende Template zurückgegriffen werden:
```properties
    #DATABASE
    database-name=postgres
    database-url=localhost:5432
    database-username=root
    database-password=toor
    #RUNTIME
    runtime-maxThreads=8
    #NETWORK
    network-address-scope=IPv4
    #SOCKET
    network-socket-hostname=localhost
    network-socket-port=8192
    network-socket-backlog=0
```

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
    java -jar <NAME_OF_FILE>.jar
```

### CLIENT

Zum Kompilieren kann im Verzeichnis `Client` der folgende befehl verwendet werden:
```sh
    mvn clean install
```

Es sollte nun eine ausführbare `.jar`-Datei im Verzeichnis `Client/target/` zu finden sein.
Diese kann wie folgt ausgeführt werden:
```sh
    java --module-path \<PATH_TO_YOUR_JAVA_FX_SDK>\javafx-sdk-17.0.0.1\lib --add-modules=javafx.controls,javafx.fxml -jar <NAME_OF_FILE>.jar <SERVER_IP>
```

USAGE
-----

Nach einer erfolgreichen Installation nach den oben genannten Schritten, sollte ein Ladefenster,
gefolgt von einem Login-Fenster erscheinen. Hier kann über "SignUp" ein neuer Account erstellt werden.
Ein gültiger Name besteht dabei aus vier bis zwölf Zeichen und ein gültiges Passwort besteht aus:
-mindestens acht Zeichen
-maximal zwölf Zeichen
-Zahlen
-Großbuchstaben
-Kleinbuchstaben
-Sonderzeichen, wie z.B. !?#

Über das "+" Symbol können Chats erstellt oder Freunde hinzugefügt werden.
In der Listen-Ansicht stellt ein Rechtsklick weitere Funktionen zu Verfügung.

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
  - server logging
  - client can change server ip in runtime

* #### Issues:
  - error handling in server is missing
  - client ui does not load on connection error