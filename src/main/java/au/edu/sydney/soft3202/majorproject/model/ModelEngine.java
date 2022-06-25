package au.edu.sydney.soft3202.majorproject.model;

import au.edu.sydney.soft3202.majorproject.Main;
import au.edu.sydney.soft3202.majorproject.model.api.*;
import au.edu.sydney.soft3202.majorproject.model.database.SQLModel;
import au.edu.sydney.soft3202.majorproject.model.parser.*;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The method of the model will be processed centrally in this class.
 * Controller class cannot contact with other model classes(appSystem, witAPI, sqlModel...)
 * Controller can only use the methods in this class.
 */
public class ModelEngine {
    private InputSystemOnline weather = new InputSystemOnline();
    private InputSystemOffline inputSystemOffline = new InputSystemOffline();
    private QueryHelperParser queryHelperParser = new QueryHelperParser();
    private OutputSystemOnline outputSystemOnline = new OutputSystemOnline();
    private OutputSystemOffline outputSystemOffline = new OutputSystemOffline();
    private APIDataParser apiDataParser = new APIDataParser();
    private ModelDataParser modelDataParser = new ModelDataParser();
    private ReportParser reportParser = new ReportParser();
    private SQLModel sql = new SQLModel();
    private FileParser fileParser = new FileParser();

    private ArrayList<ArrayList<String>> currentPairs = new ArrayList<>();
    private Integer remainingSearchCredits = 0;

    /**
     * Get city details by using location pair (lat and lon)
     * @param pair
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<String> getModelCityDetails(ArrayList<String> pair)
            throws FileNotFoundException {
        return apiDataParser.getCityDetails(pair);
    }


    /**
     * get city details by using jsonObject
     * @param jsonObject
     * @return ArrayList<String>
     * @throws FileNotFoundException
     */
    public ArrayList<String> getModelCityWeatherDetails(JsonObject jsonObject)
            throws FileNotFoundException {
        ArrayList<String> returnedList = apiDataParser.getWeatherDetails(jsonObject);
        return returnedList;
    }

    /**
     * Return an offline fake details
     * @return ArrayList<String>
     */
    public ArrayList<String> modelAddOffline(){
        return inputSystemOffline.addOffline();
    }

    /**
     * Call the add cache method in sql class
     * @param cityWeather
     * @throws SQLException
     */
    public void modelAddSql(ArrayList<String> cityWeather) throws SQLException {
        sql.addWeather(cityWeather.get(0), cityWeather.get(1),
                Double.parseDouble(cityWeather.get(2)), Double.parseDouble(cityWeather.get(3)),
                cityWeather.get(4), Double.parseDouble(cityWeather.get(5)),
                Double.parseDouble(cityWeather.get(6)), Integer.parseInt(cityWeather.get(7)),
                cityWeather.get(8), cityWeather.get(9), Double.parseDouble(cityWeather.get(10)),
                Double.parseDouble(cityWeather.get(11)));
    }

    /**
     * Call the query cache method in sql class
     * @return ArrayList<ArrayList<String>>
     * @throws SQLException
     */
    public ArrayList<ArrayList<String>> modelQuerySql() throws SQLException {
        return sql.queryWeather();
    }


    /**
     * Call the clear database method in sql class
     * @throws SQLException
     */
    public void modelClearDB() throws SQLException {
        sql.clearDB();
    }



    /**
     * Call the post to output api method in appSystem class
     * @param state
     * @param currentCityWeathers
     * @return
     */
    public String modelPostToOutputResponseChanged(String state,
                                                   ArrayList<ArrayList<String>> currentCityWeathers){
        String returnedString = "";
        if(state.equals("offline")){
            returnedString = outputSystemOffline.postToOutputAPI(null,null);
        }else{
            returnedString = outputSystemOnline.postToOutputAPI(
                    reportParser.sendReport(currentCityWeathers),Main.outputKey);
        }
        return returnedString;
    }


    /**
     * Helper method which judge whether the output api response is reasonable
     * @param returnedString
     * @return
     */
    public String modelPostToOutPutChanged_2(String returnedString){
        if(returnedString.contains("https://pastebin.com/")){;
            return "yes";
        }else{
            return returnedString;
        }
    }


    /**
     * Get the weather api jsonObject response
     * @param lat
     * @param lon
     * @return
     */
    public JsonObject modelGetWeatherApi(Double lat, Double lon){
        return weather.getWeatherAPI(lat, lon);
    }


    /**
     * Set sql
     * @param newSQL
     */
    public void setSql(SQLModel newSQL){
        sql = newSQL;
    }

    /**
     * Get the sql
     * @return
     */
    public SQLModel getSql(){
        return this.sql;
    }

    /**
     * Sset up database
     * @param sql
     */
    public void modelSetupDB(SQLModel sql){
        sql.createDB();
        sql.setupDB();
    }

    /**
     * Get the details of all cities in the csv parser
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<ArrayList<String>> modelGetCSV() throws FileNotFoundException {
        return fileParser.readCSVFile();
    }


    /**
     * Do some related work after fetching the api
     * @param choiceBoxValue
     * @param state
     * @param cityPair
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<String> fetchAPI(ArrayList<String> choiceBoxValue,
                                      String state, ArrayList<String> cityPair)
            throws FileNotFoundException {
        ArrayList<String> cityWeather = new ArrayList<>();
        if(state.equals("online")){
            JsonObject jsonObject = this.modelGetWeatherApi(Double.parseDouble(cityPair.get(0)),
                    Double.parseDouble(cityPair.get(1)));
            System.out.println("\njsonObject is \n" + jsonObject + "\n\n");
            if (jsonObject != null) {
                System.out.println("hello?");
                cityWeather = this.getModelCityWeatherDetails(jsonObject);
                System.out.println("my getting city weather is " + cityWeather + "\n");
            }else{
                return null;
            }
        }else{
            cityWeather = this.modelAddOffline();
            System.out.println("offline city weather is " + cityWeather);
        }
        return cityWeather;
    }

    /**
     * Tackle city weather details arraylist when it is null or no elements
     * @param cityPair
     * @param choiceBoxValue
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<String> getCityWeather(ArrayList<String> cityPair,
                                            ArrayList<String> choiceBoxValue)
            throws FileNotFoundException {
        System.out.println("cityPair is " + cityPair);
        if(cityPair == null || cityPair.size() == 0){
            return null;
        }else{
            return cityPair;
        }
    }


    /**
     * Checks whether there is a search history for the same city
     * @param cityWeather
     * @return
     */
    public boolean hasRecord(ArrayList<String> cityWeather) throws SQLException {
        ArrayList<ArrayList<String>> historyDB = this.modelQuerySql();
        for (int i = 0; i < historyDB.size(); i++){
            ArrayList<String> singleHistory = historyDB.get(i);
            System.out.println("\n\nsingle history is " + singleHistory);
            System.out.println("\n\ncity weather is " + cityWeather);
            if(singleHistory.get(0).equals(cityWeather.get(0))
                    && singleHistory.get(1).equals(cityWeather.get(1))){
                return true;
            }
        }
        return false;
    }

    /**
     * Get the query weather history by city name and state name
     * @param cityName
     * @param stateName
     * @param modelEngine
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getMatchedHistory(String cityName, String stateName,
                                               ModelEngine modelEngine) throws SQLException {
        System.out.println("look at here!\n");
        return getSql().queryWeatherByCityName(cityName,stateName);
    }

    /**
     * Call the sql model to replace fixed cache
     * @param cityWeather
     * @throws SQLException
     */
    public void modelReplaceDB(ArrayList<String> cityWeather) throws SQLException {
        sql.replaceDB(cityWeather.get(0), cityWeather.get(1),
                Double.parseDouble(cityWeather.get(2)), Double.parseDouble(cityWeather.get(3)),
                cityWeather.get(4), Double.parseDouble(cityWeather.get(5)),
                Double.parseDouble(cityWeather.get(6)), Integer.parseInt(cityWeather.get(7)),
                cityWeather.get(8), cityWeather.get(9), Double.parseDouble(cityWeather.get(10)),
                Double.parseDouble(cityWeather.get(11)));
    }

    /**
     * Set current city pairs arraylist
     * @param givingCurrentPairs
     */
    public void setCurrentPairs(ArrayList<ArrayList<String>> givingCurrentPairs){
        this.currentPairs = givingCurrentPairs;
    }

    /**
     * Get current city pairs to the arraylist
     * @return
     */
    public ArrayList<ArrayList<String>> getCurrentPairs(){
        return this.currentPairs;
    }

    /**
     * Add a new search history to the current pair
     * @param addingPair
     */
    public void addCurrentPair(ArrayList<String> addingPair){
        this.currentPairs.add(addingPair);
    }

    /**
     * Remove a search history from the current pair
     * @param removingPair
     */
    public void removeCurrentPair(ArrayList<String> removingPair){
        this.currentPairs.remove(removingPair);
    }

    /**
     * Clear the search history arraylist
     */
    public void clearCurrentPair(){
        this.currentPairs.clear();
    }

    /**
     * Check whether the search history has the matched city history
     * @param cityWeather
     * @param helper
     */
    public void checkCurrent(ArrayList<String> cityWeather, boolean helper){
        boolean hasRecord = false;
        ArrayList<ArrayList<String>> currentPairs = getCurrentPairs();
        for (int i = 0; i < currentPairs.size(); i++){
            ArrayList<String> currentSingle = currentPairs.get(i);
            if(currentSingle.get(0).equals(cityWeather.get(0))
                    && currentSingle.get(1).equals(cityWeather.get(1))){
                if(helper){
                    removeCurrentPair(currentSingle);
                    addCurrentPair(cityWeather);
                }
                hasRecord = true;
                break;
            }
        }
        if (!hasRecord){
            addCurrentPair(cityWeather);
        }
    }

    /**
     * Add or replace database value
     * @param state
     * @param helper
     * @param modelEngine
     * @param cityWeather
     * @throws SQLException
     */
    public void addToBacklog(String state, boolean helper, ModelEngine modelEngine,
                             ArrayList<String> cityWeather) throws SQLException {
        if(state.equals("online")){
            if(!helper){
                modelEngine.modelAddSql(cityWeather);
            }else{
                modelEngine.modelReplaceDB(cityWeather);
            }
            System.out.println("hi there");
            modelEngine.checkCurrent(cityWeather, true);
        }else if(state.equals("offline")){
            modelEngine.setCurrentPairs(queryHelperParser.getFakeQueryArray());
        }
    }

    /**
     * Get the query from the database
     * @param state
     * @param modelEngine
     * @return
     * @throws SQLException
     */
    public ArrayList<ArrayList<String>> getQueryFromModelEngine(String state,
                                                                ModelEngine modelEngine)
            throws SQLException {
        ArrayList<ArrayList<String>> finalCacheArrayList = new ArrayList<>();
        if(state.equals("online")){
            finalCacheArrayList = modelEngine.modelQuerySql();
        }else if(state.equals("offline")){
            finalCacheArrayList = queryHelperParser.getFakeQueryArray();
        }
        return finalCacheArrayList;
    }


    /**
     * Check whether the needed icon value exists
     * @param iconFileName
     * @return
     */
    public boolean checkExistIcon(String iconFileName){
        return modelDataParser.getAllIconName().contains(iconFileName);
    }

    /**
     * Check whether cache has this city weather details, if yes return true otherwise false
     * @param newSearch
     * @return
     * @throws SQLException
     */
    public boolean checkCache(ArrayList<String> newSearch) throws SQLException {
        ArrayList<ArrayList<String>> historyDB = this.modelQuerySql();
        for (int i = 0; i < historyDB.size(); i++){
            ArrayList<String> singleHistory = historyDB.get(i);
            if(singleHistory.get(0).equals(newSearch.get(0))
                    && singleHistory.get(1).equals(newSearch.get(1))){
                System.out.println("i wnat to see here" + singleHistory.get(0) + "\n\n");
                return true;
            }
        }
        return false;
    }


    /**
     * Check whether the history is empty
     * @return
     */
    public boolean checkHistoryPairsIsEmpty(){
        if(currentPairs == null || currentPairs.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Set the default search credits and remaining search credits
     * @param searchCredits
     */
    public void setDefaultSearchCredits(Integer searchCredits){
        this.remainingSearchCredits = searchCredits;
    }

    /**
     * Get the remaining search credits
     * @return Integer representing the remaining search credits
     */
    public Integer getRemainingSearchCredits(){
        return this.remainingSearchCredits;
    }

    /**
     * Update the remaining Search credits
     * @return whether the minus is successful
     */
    public Boolean updateRemainingSearchCredits(){
        if(this.remainingSearchCredits <= 0){
            return false;
        }else {
            Integer integer = this.remainingSearchCredits - 1;
            this.remainingSearchCredits = integer;
            return true;
        }
    }

}