package au.edu.sydney.soft3202.majorproject.model.api;

import java.util.ArrayList;

public class InputSystemOffline {

    /**
     * Return a fake arraylist which contains the weather details of one city
     * @return ArrayList<String>
     */
    public ArrayList<String> addOffline(){
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
//        returnedList.add("mno");
        returnedList.add("Overcast clouds");
        returnedList.add("32.03028");
        returnedList.add("120.87472");
        return returnedList;
    }
}
