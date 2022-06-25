package au.edu.sydney.soft3202.majorproject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class FakeDataClass {
    public ArrayList<String> fakeOffline(){
        ArrayList<String> returnArray = new ArrayList<>();
        returnArray.add("Hangzhou");
        returnArray.add("02");
        returnArray.add("19");
        returnArray.add("3");
        returnArray.add("northeast");
        returnArray.add("75");
        returnArray.add("0.151522");
        returnArray.add("187");
        returnArray.add("d02d");
        returnArray.add("Drizzle");
        return returnArray;
    }

    public JsonObject getErrorJsonObject(){
        String responseBody = "{\"error\":\"API key not valid, or not yet activated.\"}";
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }


    public JsonObject getFakeAPIResponse(){
        String responseBody = "{\"data\":[{\"rh\":66.875,\"pod\":\"d\",\"lon\":120.87,\"pres\":1011.5,\"timezone\":\"Asia\\/Shanghai\",\"ob_time\":\"2022-05-12 02:11\",\"country_code\":\"CN\",\"clouds\":98,\"ts\":1652321493,\"solar_rad\":230.3,\"state_code\":\"04\",\"city_name\":\"Nan" +
                "tong\",\"wind_spd\":3.6113,\"wind_cdir_full\":\"northeast\",\"wind_cdir\":\"NE\",\"slp\":1012,\"vis\":10,\"h_angle\":-25.7,\"sunset\":\"10:41\",\"dni\":911.54,\"dewpt\":15.9,\"snow\":0,\"uv\":2.83503,\"precip\":0,\"wind_dir\":49,\"sunrise\":\"21:00\",\"ghi\":909.83,\"dhi\"\n" +
                ":120.07,\"aqi\":80,\"lat\":32.03,\"weather\":{\"icon\":\"c04d\",\"code\":804,\"description\":\"Overcast clouds\"},\"datetime\":\"2022-05-12:02\",\"temp\":22.5,\"station\":\"ZSSS\",\"elev_angle\":61.07,\"app_temp\":22.5}],\"count\":1}";
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public JsonObject getFakeResponseBody_pair2(){
        String responseBody = "{\"data\":[{\"rh\":57.375,\"pod\":\"d\",\"lon\":120.87,\"pres\":1016.5,\"timezone\":\"Asia\\/Shanghai\",\"ob_time\":\"2022-05-14 09:33\",\"country_code\":\"CN\",\"clouds\":100,\"ts\":1652520793,\"solar_rad\":61.4,\"state_code\":\"04\",\"city_name\":\"Nan" +
                "tong\",\"wind_spd\":3.38535,\"wind_cdir_full\":\"east-northeast\",\"wind_cdir\":\"ENE\",\"slp\":1017,\"vis\":10,\"h_angle\":77.1,\"sunset\":\"10:42\",\"dni\":667.37,\"dewpt\":8.8,\"snow\":0,\"uv\":0.71178,\"precip\":0,\"wind_dir\":71,\"sunrise\":\"20:59\",\"ghi\":306.92,\n" +
                "\"dhi\":77.11,\"aqi\":90,\"lat\":32.03,\"weather\":{\"icon\":\"c04d\",\"code\":804,\"description\":\"Overcast clouds\"},\"datetime\":\"2022-05-14:09\",\"temp\":17.3,\"station\":\"ZSSS\",\"elev_angle\":20.75,\"app_temp\":17.3}],\"count\":1}";
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }


    public ArrayList<String> getFakeWeatherDetails(){
        ArrayList<String> weatherDetails = new ArrayList<>();
        weatherDetails.add("Nantong");
        weatherDetails.add("04");
        weatherDetails.add("22.2");
        weatherDetails.add("4.83878");
        weatherDetails.add("northeast");
        weatherDetails.add("100");
        weatherDetails.add("0.883927");
        weatherDetails.add("78");
        weatherDetails.add("d02d");
        weatherDetails.add("Drizzle");
        weatherDetails.add("32.03028");
        weatherDetails.add("120.87472");
        return  weatherDetails;
    }

    public ArrayList<String> getFakeWeatherDetails_pair2(){
        ArrayList<String> returnedList = new ArrayList<>();
        returnedList.add("Nantong");
        returnedList.add("04");
        returnedList.add("17.3");
        returnedList.add("3.38535");
        returnedList.add("east-northeast");
        returnedList.add("100");
        returnedList.add("0");
        returnedList.add("90");
        returnedList.add("c04d");
        returnedList.add("Overcast clouds");
        returnedList.add("32.03028");
        returnedList.add("120.87472");
        return returnedList;
    }

    public ArrayList<String> getFakeWeatherDetails_pair2Exchange(){
        ArrayList<String> returnedList = new ArrayList<>();
        returnedList.add("Nantong");
        returnedList.add("04");
        returnedList.add("16.5");
        returnedList.add("4.3290");
        returnedList.add("east");
        returnedList.add("100");
        returnedList.add("0");
        returnedList.add("90");
        returnedList.add("c04d");
        returnedList.add("Clouds");
        returnedList.add("32.03028");
        returnedList.add("120.87472");
        return returnedList;
    }

    public ArrayList<String> getFakeWeatherDetails_Nanjing(){
        ArrayList<String> returnedList = new ArrayList<>();
        returnedList.add("Nanjing");
        returnedList.add("04");
        returnedList.add("26.2");
        returnedList.add("4.45389");
        returnedList.add("east-southeast");
        returnedList.add("8");
        returnedList.add("0");
        returnedList.add("55");
        returnedList.add("c01d");
        returnedList.add("Clear sky");
        returnedList.add("32.06167");
        returnedList.add("118.77778");
        return returnedList;
    }

    public ArrayList<ArrayList<String>> getFakeQueryArray(){
        ArrayList<ArrayList<String>> returnedArray = new ArrayList<>();
        ArrayList<String> singleArray = new ArrayList<>();
        singleArray.add("Nantong");
        singleArray.add("04");
        singleArray.add("14.9");
        singleArray.add("2.59031");
        singleArray.add("northeast");
        singleArray.add("100.0");
        singleArray.add("0.0");
        singleArray.add("141");
        singleArray.add("c04n");
        singleArray.add("Overcast clouds");
        singleArray.add("1");
        singleArray.add("32.03028");
        singleArray.add("120.87472");
        returnedArray.add(singleArray);
        return returnedArray;
    }

    public ArrayList<ArrayList<String>> getFakeQueryArrayTwoCity(){
        ArrayList<ArrayList<String>> returnedArray = new ArrayList<>();
        ArrayList<String> singleArray = new ArrayList<>();
        singleArray.add("Nantong");
        singleArray.add("04");
        singleArray.add("14.9");
        singleArray.add("2.59031");
        singleArray.add("northeast");
        singleArray.add("100.0");
        singleArray.add("0.0");
        singleArray.add("141");
        singleArray.add("c04n");
        singleArray.add("Overcast clouds");
        singleArray.add("32.03028");
        singleArray.add("120.87472");
        returnedArray.add(singleArray);
        returnedArray.add(getFakeWeatherDetails_Nanjing());
        return returnedArray;
    }

    public String getFakePostWeather(){
        return "CITY NAME  Sydney\n" +
                "STATE NAME  02\n" +
                "TEMPERATURE  21.1\n" +
                "WIND SPEED   1.78399\n" +
                "WIND DIRECTION   northwest\n" +
                "CLOUDS   0.0\n" +
                "PRECIPITATION   0.0\n" +
                "AIR QUALITY   58\n" +
                "DESCRIPTION   Clear sky";
    }

    public String getFakeCityDetails(){
        return "CITY NAME  Nantong\n" +
                "STATE NAME  04\n" +
                "TEMPERATURE  14.9\n" +
                "WIND SPEED   2.59031\n" +
                "WIND DIRECTION   northeast\n" +
                "CLOUDS   100.0\n" +
                "PRECIPITATION   0.0\n" +
                "AIR QUALITY   141\n" +
                "DESCRIPTION   Overcast clouds\n\n";
    }

    public ArrayList<String> getChoiceBoxValue_pair3(){
        ArrayList<String> returnedString = new ArrayList<>();
        returnedString.add("Nantong");
        returnedString.add("Paracel Islands");
        return returnedString;
    }

    public ArrayList<String> getCityPair_pair3(){
        ArrayList<String> returnedString = new ArrayList<>();
        returnedString.add("32.03028");
        returnedString.add("120.87472");
        return returnedString;
    }
}
