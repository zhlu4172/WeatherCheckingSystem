package au.edu.sydney.soft3202.majorproject.model.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelDataParser {

    /**
     * Parse data to city weather details.
     * @param citiesWeatherDetail
     * @return
     */
    public String parseDataToCityWeatherDetails(ArrayList<ArrayList<String>> citiesWeatherDetail){
        String totalAdding = "";
        for (int i = 0; i < citiesWeatherDetail.size(); i++){
            ArrayList<String> singleCityWeather = citiesWeatherDetail.get(i);
            String cityName = singleCityWeather.get(0);
            String stateName = singleCityWeather.get(1);
            String temp = singleCityWeather.get(2);
            String windSpeed = singleCityWeather.get(3);
            String windDirection = singleCityWeather.get(4);
            String clouds = singleCityWeather.get(5);
            String precip = singleCityWeather.get(6);
            String airQuality = singleCityWeather.get(7);
//            iconSymbol = singleCityWeather.get(8);
            String description = singleCityWeather.get(9);
            if(precip.equals("-10000") ||precip.equals("-10000.0")){
                precip = "Data Not Provided";
            }
            String addingString = "CITY NAME  " + cityName + "\nSTATE NAME  " + stateName
                    + "\nTEMPERATURE  " + temp + "\nWIND SPEED   " + windSpeed
                    + "\nWIND DIRECTION   " + windDirection + "\nCLOUDS   " + clouds
                    + "\nPRECIPITATION   " + precip + "\nAIR QUALITY   " + airQuality
                    + "\nDESCRIPTION   " + description + "\n\n";
            totalAdding += addingString;
        }
        return totalAdding;
    }

    /**
     * Parse data to the history choices arraylist
     * @param citiesWeatherDetail
     * @return
     */
    public ArrayList<String> parseDataToCurrentChoices(ArrayList<ArrayList<String>>
                                                               citiesWeatherDetail){
        ArrayList<String> settingList = new ArrayList<>();
        for (int i = 0; i < citiesWeatherDetail.size(); i++){
            ArrayList<String> singleCityWeather = citiesWeatherDetail.get(i);
            String cityName = singleCityWeather.get(0);
            String stateName = singleCityWeather.get(1);
            String adding = stateName + "," + cityName;
            settingList.add(adding);
        }
        return settingList;
    }

    /**
     * Parse the 2D arraylist to a single arraylist
     * @param totalList
     * @return
     */
    public ArrayList<String> parseDataToComboBox(ArrayList<ArrayList<String>> totalList){
        ArrayList<String> settingList = new ArrayList<>();
        for (int i = 0; i < totalList.size(); i++){
            String addingString = totalList.get(i).get(4) + "," + totalList.get(i).get(2) + ","
                    + totalList.get(i).get(1);
            settingList.add(addingString);
        }
        return settingList;
    }

    /**
     * Get all the icon png name from the fixed package
     * @return
     */
    public ArrayList<String> getAllIconName(){
        String[] pathnames;;
        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File("src/main/resources/icon");
        // Populates the array with names of files and directories
        pathnames = f.list();
        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
        }
        ArrayList<String> returnedNames = new ArrayList<>();
        returnedNames.addAll(List.of(pathnames));
        return returnedNames;
    }

    /**
     * Check Whether the Integer is between 1 and 10
     * @param gettingResult
     * @return Whether the Integer is between 1 and 10
     */
    public boolean checkIntegerValidation(String gettingResult){
        if(Integer.parseInt(gettingResult) <= 1 || Integer.parseInt(gettingResult) >= 10){
            return false;
        }else{
            return true;
        }
    }

}
