package au.edu.sydney.soft3202.majorproject.model.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class APIDataParser {

    FileParser fileParser = new FileParser();
    /**
     * Get city details(state name and city name)
     * @param pair
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<String> getCityDetails(ArrayList<String> pair) throws FileNotFoundException {
        ArrayList<ArrayList<String>> myList = fileParser.readCSVFile();
        ArrayList<String> cityDetail = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++){
            if (myList.get(i).get(1).equals(pair.get(0)) && myList.get(i).get(4).equals(pair.get(1))){
                System.out.println(myList.get(i).get(1));
                System.out.println(myList.get(i).get(4));
                cityDetail.add(myList.get(i).get(5));
                cityDetail.add(myList.get(i).get(6));
            }
        }
        return cityDetail;
    }

    /**
     * Tackle all kinds of city details in the JsonObject and store those to an Arraylist
     * @param userPost
     * @return ArrayList<String>
     * @throws FileNotFoundException
     */
    public ArrayList<String> getWeatherDetails(JsonObject userPost) throws FileNotFoundException {
        ArrayList<String> returnArrayList = new ArrayList<>();
        if(userPost == null || userPost.toString().contains("{\"error\"")){
            return null;
        }
        if(userPost.get("data") == null || userPost.get("data").toString().equals("null")){
            return null;
        }
        JsonArray dataArray = userPost.get("data").getAsJsonArray();
        JsonObject current = dataArray.get(0).getAsJsonObject();
        //add the city name

        String cityName = current.get("city_name").getAsString();
        String stateName = current.get("state_code").getAsString();
        returnArrayList.add(current.get("city_name").getAsString());
        //add the state name
        returnArrayList.add(current.get("state_code").getAsString());
        //add the temperature
        if(current.get("temp") != null  && !current.get("temp").toString().equals("null")){
            returnArrayList.add(current.get("temp").getAsString());
        }else{
            returnArrayList.add("-10000");
        }
        //add wind speed
        if(current.get("wind_spd") != null && !current.get("wind_spd").toString().equals("null")){
            returnArrayList.add(current.get("wind_spd").getAsString());
        }else{
            returnArrayList.add("-10000");
        }

        //add wind direction
        if(current.get("wind_cdir_full") != null &&
                !current.get("wind_cdir_full").toString().equals("null")){
            returnArrayList.add(current.get("wind_cdir_full").getAsString());
        }else{
            returnArrayList.add("-10000");
        }

        //add clouds
        if(current.get("clouds") != null && !current.get("clouds").toString().equals("null")){
            returnArrayList.add(current.get("clouds").getAsString());
        }else{
            returnArrayList.add("-10000");
        }

        //add the precipitation
        if(current.get("precip") != null && !current.get("precip").toString().equals("null")){
            returnArrayList.add(current.get("precip").getAsString());
        }else{
            returnArrayList.add("-10000");
        }

        //add the air quality
        if(current.get("aqi") != null && !current.get("aqi").toString().equals("null")){
            returnArrayList.add(current.get("aqi").getAsString());
        }else{
            returnArrayList.add("-10000");
        }

        //add icon symbol and weather description
        JsonObject icon = current.get("weather").getAsJsonObject();

        if(icon.get("icon") == null || icon.get("description") == null){
            return null;
        }
        returnArrayList.add(icon.get("icon").getAsString());
        returnArrayList.add(icon.get("description").getAsString());

        //add the lat
        if(!current.get("lat").toString().equals("null")){
            returnArrayList.add(current.get("lat").getAsString());
        }else{
            returnArrayList.add("-10000");
        }
        //add the lon
        if(!current.get("lon").toString().equals("null")){
            returnArrayList.add(current.get("lon").getAsString());
        }else{
            returnArrayList.add("-10000");
        }
        System.out.println(returnArrayList);
        return returnArrayList;
    }
}
