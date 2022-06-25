package au.edu.sydney.soft3202.majorproject.model.api;

import au.edu.sydney.soft3202.majorproject.Main;
import au.edu.sydney.soft3202.majorproject.model.parser.FileParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InputSystemOnline {

    FileParser fileParser = new FileParser();


    /**
     * Launch an API GET request and get the corresponding response
     * By using lat and lon, the related city weather will be sent back in the response body
     * @param Lat
     * @param Lon
     * @return api response, which is JsonObject type
     */
    public JsonObject getWeatherAPI(Double Lat, Double Lon){
        try{
            String uri = "https://api.weatherbit.io/v2.0/current?lat=" +
                    Lat + "&lon=" + Lon + "&key=" + Main.inputKey + "&include=V";
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            return userPost;
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }


    /**
     * Transfer a String to a JsonObject
     * @param responseBody
     * @return JsonObject
     */
    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

}

