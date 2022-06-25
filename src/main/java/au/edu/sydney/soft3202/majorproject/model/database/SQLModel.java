package au.edu.sydney.soft3202.majorproject.model.database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;


public class SQLModel {

    private static String dbName = "cityWeather.db";
    private static String dbURL = "jdbc:sqlite:" + dbName;
    private static Connection connection;
    private boolean test = false;


    /**
     * Set DB connection
     * @param settingDBName
     * @param settingDBURL
     */
    public void setDBConnection(String settingDBName, String settingDBURL){
        dbName = settingDBName;
        dbURL = settingDBURL;
    }

    /**
     * Sset the boolean to true when the class is used for testing use
     * @param bol
     */
    public void setTest(boolean bol){
        test = true;
    }

    /**
     * Create a Database
     */
    public void createDB() {
        if(dbName != null){
            File dbFile = new File(dbName);
            if (dbFile.exists()) {
                System.out.println("Database already created");
                return;
            }
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            System.out.println("A new database has been created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create DB for test
     */
    public void createDBForText() {
        try{
            connection = DriverManager.getConnection(dbURL);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Set up database
     */
    public void setupDB() {
        String createSavingStatusTableSQL =
                """
                CREATE TABLE IF NOT EXISTS cityWeather (
                    id INTEGER PRIMARY KEY,
                    city_name text NOT NULL,
                    state_code text NOT NULL,
                    temperature DOUBLE NOT NULL,
                    wind_speed DOUBLE NOT NULL,
                    wind_direction text NOT NULL,
                    clouds DOUBLE NOT NULL,
                    precipitation DOUBLE NOT NULL,
                    air_quality INTEGER NOT NULL,
                    icon text NOT NULL,
                    description text NOT NULL,
                    lat DOUBLE NOT NULL,
                    lon DOUBLE NOT NULL
                );
                """;

        if(!test){
            try (Connection conn = DriverManager.getConnection(dbURL);
                 Statement statement = conn.createStatement()) {
                statement.execute(createSavingStatusTableSQL);
                System.out.println("Saving Status Table Create Successfully");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }else{
            try (Statement statement = connection.createStatement()) {
                statement.execute(createSavingStatusTableSQL);
                System.out.println("Saving Status Table Create Successfully");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }


    /**
     * Add a new search to database
     * @param addingCityName
     * @param addingStateName
     * @param addingTemp
     * @param addingSpeed
     * @param addingDirection
     * @param addingClouds
     * @param addingPreci
     * @param addingAqi
     * @param addingIcon
     * @param addingDes
     * @param lati
     * @param Lont
     * @throws SQLException
     */
    public void addWeather(String addingCityName, String addingStateName, double addingTemp,
                           double addingSpeed, String addingDirection, double addingClouds,
                           double addingPreci, int addingAqi, String addingIcon, String addingDes,
                           Double lati, Double Lont) throws SQLException{
        String addStatusWithParametersSQL =
                """
                INSERT INTO cityWeather(city_name, state_code, temperature, wind_speed, 
                wind_direction, clouds, precipitation, air_quality, icon, description, lat, lon) 
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(addStatusWithParametersSQL);
        }else{
            preparedStatement = connection.prepareStatement(addStatusWithParametersSQL);
        }

        System.out.println("Added questionable data");
        preparedStatement.setString(1, addingCityName);
        preparedStatement.setString(2, addingStateName);
        preparedStatement.setDouble(3, addingTemp);
        preparedStatement.setDouble(4, addingSpeed);
        preparedStatement.setString(5, addingDirection);
        preparedStatement.setDouble(6, addingClouds);
        preparedStatement.setDouble(7, addingPreci);
        preparedStatement.setInt(8, addingAqi);
        preparedStatement.setString(9, addingIcon);
        preparedStatement.setString(10, addingDes);
        preparedStatement.setDouble(11, lati);
        preparedStatement.setDouble(12, Lont);
        preparedStatement.executeUpdate();
    }

    /**
     * Query all the weather from database
     * @return
     * @throws SQLException
     */
    public ArrayList<ArrayList<String>> queryWeather() throws SQLException {
        String matchingStatusSQL =
                """
                SELECT *
                FROM cityWeather
                """;

        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(matchingStatusSQL);
        }else{
            preparedStatement = connection.prepareStatement(matchingStatusSQL);
        }

            ResultSet results = preparedStatement.executeQuery();

            ArrayList<ArrayList<String>> allCitiesWeather = new ArrayList<>();
            while (results.next()) {
                ArrayList singleCityWeather = new ArrayList();
                singleCityWeather.add(results.getString("city_name"));
                singleCityWeather.add(results.getString("state_code"));
                singleCityWeather.add(results.getString("temperature"));
                singleCityWeather.add(results.getString("wind_speed"));
                singleCityWeather.add(results.getString("wind_direction"));
                singleCityWeather.add(results.getString("clouds"));
                singleCityWeather.add(results.getString("precipitation"));
                singleCityWeather.add(results.getString("air_quality"));
                singleCityWeather.add(results.getString("icon"));
                singleCityWeather.add(results.getString("description"));
                singleCityWeather.add(results.getString("id"));
                singleCityWeather.add(results.getString("lat"));
                singleCityWeather.add(results.getString("lon"));
                allCitiesWeather.add(singleCityWeather);
            }
            System.out.println(allCitiesWeather);
            System.out.println("Query Data Successfully!");

            return allCitiesWeather;


    }


    /**
     * Query cache which has fixed city name and state code
     * @param cityName
     * @param stateCode
     * @return ArrayList<String>
     * @throws SQLException
     */
    public ArrayList<String> queryWeatherByCityName(String cityName, String stateCode)
            throws SQLException {
        String matchingStatusSQL =
                """
                SELECT *
                FROM cityWeather
                WHERE city_name = ? AND state_code = ?
                """;
        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(matchingStatusSQL);
        }else{
            preparedStatement = connection.prepareStatement(matchingStatusSQL);
        }
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, stateCode);
            ResultSet results = preparedStatement.executeQuery();
            ArrayList<ArrayList<String>> allCitiesWeather = new ArrayList<>();
            while (results.next()) {
                ArrayList singleCityWeather = new ArrayList();
                singleCityWeather.add(results.getString("city_name"));
                singleCityWeather.add(results.getString("state_code"));
                singleCityWeather.add(results.getString("temperature"));
                singleCityWeather.add(results.getString("wind_speed"));
                singleCityWeather.add(results.getString("wind_direction"));
                singleCityWeather.add(results.getString("clouds"));
                singleCityWeather.add(results.getString("precipitation"));
                singleCityWeather.add(results.getString("air_quality"));
                singleCityWeather.add(results.getString("icon"));
                singleCityWeather.add(results.getString("description"));
//                singleCityWeather.add(results.getString("id"));
                singleCityWeather.add(results.getString("lat"));
                singleCityWeather.add(results.getString("lon"));
                allCitiesWeather.add(singleCityWeather);
            }
            System.out.println("Query Data Successfully!");
            return allCitiesWeather.get(0);
    }

    /**
     * Delete database by key/id
     * @param id
     * @throws SQLException
     */
    public void deleteDB(String id) throws SQLException{
        String clearDBSQL =
                """
                DELETE FROM cityWeather
                WHERE id = ?
                """;

        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(clearDBSQL);
        }else{
            preparedStatement = connection.prepareStatement(clearDBSQL);
        }
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            System.out.println("CLEAR SUCCESS");
    }

    /**
     * Delete one cache with fixed city name and state code
     * @param cityName
     * @param stateName
     * @throws SQLException
     */
    public void deleteByCityName(String cityName, String stateName) throws SQLException {
        String clearDBSQL =
                """
                DELETE FROM cityWeather
                WHERE city_name = ? AND state_code = ?
                """;
        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(clearDBSQL);
        }else{
            preparedStatement = connection.prepareStatement(clearDBSQL);
        }
            preparedStatement.setString(1, cityName);
            preparedStatement.setString(2, stateName);
            preparedStatement.executeUpdate();
            System.out.println("CLEAR SUCCESS");

    }

    /**
     * Replace a cache record
     * @param addingCityName
     * @param addingStateName
     * @param addingTemp
     * @param addingSpeed
     * @param addingDirection
     * @param addingClouds
     * @param addingPreci
     * @param addingAqi
     * @param addingIcon
     * @param addingDes
     * @param lati
     * @param Lont
     * @throws SQLException
     */
    public void replaceDB(String addingCityName, String addingStateName, double addingTemp,
                          double addingSpeed, String addingDirection, double addingClouds,
                          double addingPreci, int addingAqi, String addingIcon, String addingDes,
                          Double lati, Double Lont) throws SQLException {

        deleteByCityName(addingCityName, addingStateName);
        addWeather(addingCityName, addingStateName, addingTemp, addingSpeed, addingDirection,
                addingClouds, addingPreci, addingAqi, addingIcon, addingDes, lati,Lont);
    }

    /**
     * Clear all the record in database/cache
     * @throws SQLException
     */
    public void clearDB() throws SQLException {
        String clearDBSQL =
                """
                DELETE FROM cityWeather
                """;
        PreparedStatement preparedStatement = null;
        if(!test){
            Connection conn = DriverManager.getConnection(dbURL);
            preparedStatement = conn.prepareStatement(clearDBSQL);
        }else{
            preparedStatement = connection.prepareStatement(clearDBSQL);
        }
        preparedStatement.executeUpdate();
        System.out.println("CLEAR SUCCESS");
    }
}
