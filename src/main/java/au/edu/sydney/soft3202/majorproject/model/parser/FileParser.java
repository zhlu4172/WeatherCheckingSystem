package au.edu.sydney.soft3202.majorproject.model.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileParser {

    /**
     * Read all the city details from csv parser
     * @return city details ArrayList<ArrayList<String>>
     * @throws FileNotFoundException
     */
    public ArrayList<ArrayList<String>> readCSVFile() throws FileNotFoundException {
        String line = "";
        String splitBy = ",";
        ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(
                    "src/main/resources/csvFile/cities_20000.csv", StandardCharsets.UTF_8));

            int lineIndex = 0;
            while ((line = br.readLine()) != null)
            {
                if(lineIndex != 0){
                    String[] element = line.split(splitBy);
                    ArrayList<String> singleElement  = new ArrayList<>();
                    for (int i = 0; i < element.length; i++){
                        singleElement.add(element[i]);
                    }
                    myList.add(singleElement);
                }
                lineIndex += 1;
            }
            return myList;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
