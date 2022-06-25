package au.edu.sydney.soft3202.majorproject.model.parser;

import java.util.ArrayList;

public class ReportParser {
    /**
     * Arrange the details and make that to a string which can be pasted to the pastebin directly
     * @param eachWeather
     * @return
     */
    public String sendReport(ArrayList<ArrayList<String>> eachWeather){
        String postWeather = "";
        for (int i = 0; i < eachWeather.size(); i++){
            ArrayList<String> current = eachWeather.get(i);
            String currentAdding = "CITY NAME  " + current.get(0) + "\nSTATE NAME  "
                    + current.get(1) + "\nTEMPERATURE  " + current.get(2) + "\nWIND SPEED   "
                    + current.get(3) + "\nWIND DIRECTION   " + current.get(4) + "\nCLOUDS   "
                    + current.get(5) + "\nPRECIPITATION   " + current.get(6) + "\nAIR QUALITY   "
                    + current.get(7) + "\nDESCRIPTION   " + current.get(9) + "\n\n";
            postWeather += currentAdding;
        }
        return postWeather;
    }
}
