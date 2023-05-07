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

1. Please make sure that you have already successfully completed the installations from [requirements](#requirements).
2. Clone the Project from [here]() via [Git]() and got to /out/artifacts/ 
3. Navigate to the directory  /Quap/Server/src/main/resources/com/quap/db
     Open a terminal and run
    ```console
     psql -h localhost -U postgres -f create-db.sql
    ```
4. Go to Server_jar and execute the jar
5. Go to DesktopApp_jar and execute the jar with the following VM options:
    ```console
    --module-path \"PATH TO YOUR JAVA FX SDK"\javafx-sdk-17.0.0.1\lib --add-modules=javafx.controls,javafx.fxml
    ```
* If you want to work with the project, debug it, or develop it further, see the [configuration section](#configuration).

CONFIGURATION
-------------

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

