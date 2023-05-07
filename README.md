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
`config.properties` angepasst werden.
In dieser muss neben den Socket Daten auch die Datenbank Verbindung festgelegt werden.

Das Datenbankschema kann wie folgt aufgesetzt werden:

```sh
     psql -h <IP> -U <NAME> -f create-db.sql
```

### CLIENT

Go to DesktopApp_jar and execute the jar with the following VM options:
    ```console
    --module-path \"PATH TO YOUR JAVA FX SDK"\javafx-sdk-17.0.0.1\lib --add-modules=javafx.controls,javafx.fxml
    ```
* If you want to work with the project, debug it, or develop it further, see the [configuration section](#configuration).

CONFIGURATION
-------------

### SERVER

### CLIENT
TODO: hier schreiben, was Client braucht an config und was Server braucht an config

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

#### This project is currently still under development.

* ##### Planned features are:
  - decentralized data synchronization
  - accessibility in the local network via DNS
  - live server console
  - profile and settings for users

