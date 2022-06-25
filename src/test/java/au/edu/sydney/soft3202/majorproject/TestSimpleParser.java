package au.edu.sydney.soft3202.majorproject;

import au.edu.sydney.soft3202.majorproject.model.ModelEngine;
import au.edu.sydney.soft3202.majorproject.model.parser.ModelDataParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test simple parser method in model
 */

public class TestSimpleParser {
    FakeDataClass fakeDataClass = new FakeDataClass();
    /**
     * Parse city weather arraylist to a string.
     */
    @Test
    public void testParseDataToCityWeatherDetails(){
        ModelEngine modelEngine = new ModelEngine();
        ModelDataParser modelDataParser = new ModelDataParser();
        String str1 = fakeDataClass.getFakeCityDetails();
        String str2 = modelDataParser.parseDataToCityWeatherDetails(fakeDataClass.getFakeQueryArray());
        assertEquals(str1,str2);
    }


    /**
     * Test the parseDataToCurrentChoice() method.
     */
    @Test
    public void testDataToCurrentChoice(){
        ArrayList<String> expectedList = new ArrayList<>();
        expectedList.add("04,Nantong");
        ModelEngine modelEngine = mock(ModelEngine.class);
        ModelDataParser modelDataParser = new ModelDataParser();
        assertEquals(expectedList,modelDataParser.parseDataToCurrentChoices(
                fakeDataClass.getFakeQueryArray()));
    }

    /**
     * Test whether the checkExistIcon method works fine
     */
    @Test
    public void testReadIconName(){
        ModelEngine modelEngine = new ModelEngine();
        ModelDataParser modelDataParser = new ModelDataParser();
        assertEquals(63,modelDataParser.getAllIconName().size());
        assertTrue(modelEngine.checkExistIcon("a01d.png"));
        assertFalse(modelEngine.checkExistIcon("wwwwwww.png"));
    }

    /**
     * Test whether the parseDataToComboBox works fine
     */
    @Test
    public void testParseDataToComboBox(){
        ModelEngine modelEngine = new ModelEngine();
        ModelDataParser modelDataParser = new ModelDataParser();
        ArrayList<String> gettingArrayList = modelDataParser.parseDataToComboBox(
                fakeDataClass.getFakeQueryArray());
        assertEquals(1,gettingArrayList.size());
        gettingArrayList = modelDataParser.parseDataToComboBox(
                fakeDataClass.getFakeQueryArrayTwoCity());
        assertEquals(2,gettingArrayList.size());
    }
}
