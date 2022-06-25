package au.edu.sydney.soft3202.majorproject.model.parser;

import java.util.ArrayList;

public class QueryHelperParser {

    /**
     * Get Fake query array, which does not come from database
     * @return query result ArrayList<ArrayList<String>>
     */
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
}
