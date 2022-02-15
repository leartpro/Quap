![Quap](./Client/DesktopApp/src/main/resources/com/quap/images/splashBackground.jpg)

INTRODUCTION
------------

This project is a messenger for the local network, written in Java, taking into account the avoidance of data storage.

The project was developed with the Jetbrains Ultimate programs IntelliJ IDEA Ultimate and DataGrip (these programs are free for students, teachers and students).

To ensure user-friendliness, the application has a graphical user interface through which the user can join chat rooms, write with friends and all this anonymously.

* For more projects, visit my [personal page](https://leartpro.github.io/) (currently not available)

* To submit bug reports and feature suggestions, or track changes click [here](https://leartpro.github.io/issues/) (currently not available)

TECHNICAL DESCRIPTION
------------

The project can be divided into three modules. A server module and a client module with a DesktopApp submodule.

* The server module contains the Main.java class, which can be used to configure the server, 
as well as the server class, which manages the ServerSocket and the ClientHandler. 
The ClientHandlers take care of the requests of the clients and access the Postgres database.

* The Client module contains...

#REQUIREMENTS
------------

This project requires the following installs:

* [JDK-16+](https://www.jetbrains.com/help/idea/sdk.html) (link to jetbrains.com)
* [Postgres](https://www.postgresql.org/) (link to postgresql.org)

INSTALLATION
------------

This project must be installed as follows:

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

#CONFIGURATION
-------------

To work in the project, the project must be opened in [IntelliJ]() (click [here]() for more information).
For an optimal working environment I recommend to use [IntelliJ-Ultimate]() and/or [DataGrip]() 
to work optimally with the databases, as well as the tool 
[SceneBuilder](https://gluonhq.com/products/scene-builder/) for a better work with .fxml files.


TROUBLESHOOTING
---------------

If you receive a connection error, the problem is that the ip configuration in your local area network 
is different from the inserted values.
To fix this problem you have to edit client.java:61 and Main.java:47. 
Simply change the default IP Adresses and restart server and client. 

If you receive a database error, 
which tolds you that no role for the database exists,
you have to change the role in the Serverside Database-Reader in UserdataReader:17.

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

####This project is currently still under development.

* #####Planned features are:
  - decentralized data synchronization
  - accessibility in the local network via DNS
  - live server console
  - profile and settings for users

GALLERY
-----------
* currently not avaiable

