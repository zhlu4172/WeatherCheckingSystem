package au.edu.sydney.soft3202.majorproject;

import au.edu.sydney.soft3202.majorproject.model.database.SQLModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestSQLFunction {

    private SQLModel sql;
    private SQLModel mockSql;

    /**
     * Do preparations for each test
     */
    @BeforeEach
    public void setUp(){
        sql = new SQLModel();
        sql.setDBConnection(null,"jdbc:sqlite::memory:");
        mockSql = mock(SQLModel.class);
    }


    /**
     * Test add and query database
     * @throws SQLException
     */
    @Test
    public void testAddAndQuery() throws SQLException {
        sql.setTest(true);
        sql.createDBForText();
        sql.setupDB();
        when(mockSql.queryWeather()).thenReturn(singleExample());
        try{
            sql.addWeather("Sydney","02",19.4,
                    1.34927, "northwest", 100,
                    4.42038, 53, "r02d", "Moderate rain",
                    34.05223, -118.24368);
            assertEquals(singleExample(),sql.queryWeather());
            assertEquals(sql.queryWeather(), mockSql.queryWeather());
        }catch (SQLException e){
            assertEquals(SQLException.class, e);
        }
    }

    /**
     * Test mixed situation, with add, delete, clear and replace.
     */
    @Test
    public void testMixSituation(){
        //test setUpDB
        sql.setTest(true);
        sql.createDBForText();
        sql.setupDB();
        try{
            //test add and query
            sql.addWeather("Sydney","02",19.4,
                    1.34927, "northwest", 100,
                    4.42038, 53, "r02d", "Moderate rain",
                    34.05223, -118.24368);
            sql.addWeather("Los Angeles","CA",14.2,
                    1.52177,"east",14,0.0,
                    74,"c02n","Few clouds",-33.86785,151.20732);
            sql.addWeather("Melbourne","07",21.1,
                    2.20657,"north-northeast",100,
                    0.0,41,"c04n","Overcast clouds",
                    -37.814, 144.96332);
            ArrayList<ArrayList<String>> gettingQueryResults = sql.queryWeather();
            System.out.println("query is" + gettingQueryResults);
            ArrayList<ArrayList<String>> expectedResults = severalExample();
            System.out.println("expected is " + expectedResults);
            assertTrue(expectedResults.size() == gettingQueryResults.size()
                    && expectedResults.containsAll(gettingQueryResults)
                    && gettingQueryResults.containsAll(expectedResults));

            //test query by city name
            ArrayList<String> gettingQueryResultsByName = sql.queryWeatherByCityName(
                    "Sydney","02");
            assertEquals(singleExample1D(), gettingQueryResultsByName);

            //test delete
            sql.deleteDB("2");
            ArrayList<String> firstAdding = new ArrayList<>();
            String[] list = new String[]{"Los Angeles","CA","14.2","1.52177","east","14.0","0.0",
                    "74","c02n","Few clouds","2","-33.86785", "151.20732"};
            firstAdding.addAll(List.of(list));
            expectedResults.remove(firstAdding);
            gettingQueryResults = sql.queryWeather();
            assertTrue(expectedResults.size() == gettingQueryResults.size()
                    && expectedResults.containsAll(gettingQueryResults)
                    && gettingQueryResults.containsAll(expectedResults));

            //test delete city by name
            sql.addWeather("Los Angeles","CA",14.2,
                    1.52177,"east",14,0.0,
                    74,"c02n","Few clouds",-33.86785,151.20732);
            sql.deleteByCityName("Los Angeles","CA");
            gettingQueryResults = sql.queryWeather();
            assertTrue(expectedResults.size() == gettingQueryResults.size()
                    && expectedResults.containsAll(gettingQueryResults)
                    && gettingQueryResults.containsAll(expectedResults));

            //test clear;
            sql.clearDB();
            expectedResults.clear();
            gettingQueryResults = sql.queryWeather();
            assertTrue(expectedResults.size() == gettingQueryResults.size()
                    && expectedResults.containsAll(gettingQueryResults)
                    && gettingQueryResults.containsAll(expectedResults));

        }catch (SQLException e){
            assertEquals(SQLException.class, e);
        }
    }


    /**
     * 2D arraylist fake example for test to use
     * @return
     */
    public ArrayList<ArrayList<String>> singleExample(){
        ArrayList<ArrayList<String>> returnArraylist2D = new ArrayList<>();
        ArrayList<String> returnArrayList = new ArrayList<>();
        returnArrayList.add("Sydney");
        returnArrayList.add("02");
        returnArrayList.add("19.4");
        returnArrayList.add("1.34927");
        returnArrayList.add("northwest");
        returnArrayList.add("100.0");
        returnArrayList.add("4.42038");
        returnArrayList.add("53");
        returnArrayList.add("r02d");
        returnArrayList.add("Moderate rain");
        returnArrayList.add("1");
        returnArrayList.add("34.05223");
        returnArrayList.add("-118.24368");
        returnArraylist2D.add(returnArrayList);
        return returnArraylist2D;
    }

    /**
     * 1D arraylist example for test to use
     * @return
     */
    public ArrayList<String> singleExample1D(){
        ArrayList<String> returnArrayList = new ArrayList<>();
        returnArrayList.add("Sydney");
        returnArrayList.add("02");
        returnArrayList.add("19.4");
        returnArrayList.add("1.34927");
        returnArrayList.add("northwest");
        returnArrayList.add("100.0");
        returnArrayList.add("4.42038");
        returnArrayList.add("53");
        returnArrayList.add("r02d");
        returnArrayList.add("Moderate rain");
        returnArrayList.add("34.05223");
        returnArrayList.add("-118.24368");
        return returnArrayList;
    }

    /**
     * 2D Arraylist example with several cities for tests to use
     * @return
     */
    public ArrayList<ArrayList<String>> severalExample(){
        ArrayList<ArrayList<String>> returnedList = singleExample();
        ArrayList<String> firstAdding = new ArrayList<>();
        String[] list = new String[]{"Los Angeles","CA","14.2","1.52177","east","14.0","0.0","74",
                "c02n","Few clouds","2","-33.86785", "151.20732"};
        firstAdding.addAll(List.of(list));
        returnedList.add(firstAdding);
        ArrayList<String> secondAdding = new ArrayList<>();
        String[] list2 = new String[]{"Melbourne", "07", "21.1", "2.20657", "north-northeast",
                "100.0", "0.0", "41", "c04n", "Overcast clouds","3", "-37.814", "144.96332"};
        secondAdding.addAll(List.of(list2));
        returnedList.add(secondAdding);
//        System.out.println(returnedList);
        return returnedList;
    }
}
