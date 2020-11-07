# Football-API

The goal is to make a project that exposes an API with an HTTP GET in this URI: /import-league/{leagueCode} . E.g., it must be possible to invoke the service using this URL:

    http://localhost:<port>/import-league/CL


The service implementation must get data using the given {leagueCode}, by making requests to the http://www.football-data.org/ API (you can see the documentation entering to the site, use the API v2), and import the data into a DB (MySQL is suggested, but you can use any DB of your preference). The data requested is:

    Competition ("name", "code", "areaName") 
    Team ("name", "tla", "shortName", "areaName", "email") 
    Player("name", "position", "dateOfBirth", "countryOfBirth", "nationality")

Feel free to add to this data structure any other field that you might need (for the foreign keys relationship).  Additionally, expose an HTTP GET in URI /total-players/{leagueCode} , with a simple JSON
response like this: 

    {"total" : N } and HTTP Code 200.

where N is the total amount of players belonging to all teams that participate in the given league (leagueCode). This service must rely exclusively on the data saved inside the DB (it must not access the API football-data.org). If the given leagueCode is not present into the DB, it should respond an HTTP Code 404.


# Stack
-   Maven
-   MySQL
-   ActiveJDBC
-   SparkJava

# Set up
### Create database structure

     $ mysql -u USERNAME -p PASSWORD < sql/schema.sql
Since we are going to use MySQL, we have to configure the file `{basedir}/src/main/resources/database.properties` with the proper values

    development.driver=com.mysql.cj.jdbc.Driver
    development.username= <USERNAME>
    development.password= <PASSWORD>
    development.url=jdbc:mysql://localhost:3306/football

### Run webservice
Use the script `./run.sh`
