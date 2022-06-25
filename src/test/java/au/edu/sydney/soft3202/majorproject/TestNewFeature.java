package au.edu.sydney.soft3202.majorproject;

import au.edu.sydney.soft3202.majorproject.model.ModelEngine;
import au.edu.sydney.soft3202.majorproject.model.parser.ModelDataParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test methods or logic of new feature in this class.
 */

public class TestNewFeature {
    /**
     * Test the checkIntegerValidation method
     * Test whether this method can return correct output for different integers
     */
    @Test
    public void testCheckIntegerValidation(){
        ModelDataParser modelDataParser = new ModelDataParser();
        boolean result = modelDataParser.checkIntegerValidation("11");
        assertFalse(result);
        boolean result2 = modelDataParser.checkIntegerValidation("-1");
        assertFalse(result2);
        boolean result3 = modelDataParser.checkIntegerValidation("0");
        assertFalse(result3);
        boolean result4 = modelDataParser.checkIntegerValidation("5");
        assertTrue(result4);
    }

    /**
     * Test whether the updateRemainingSearchCredits class update value correctly
     */
    @Test
    public void testSearchCreditsWithoutMock(){
        ModelEngine modelEngine = new ModelEngine();
        modelEngine.setDefaultSearchCredits(2);
        assertEquals(2, modelEngine.getRemainingSearchCredits());
        boolean result = modelEngine.updateRemainingSearchCredits();
        assertEquals(1, modelEngine.getRemainingSearchCredits());
        assertTrue(result);
        result = modelEngine.updateRemainingSearchCredits();
        assertTrue(result);
        assertEquals(0, modelEngine.getRemainingSearchCredits());
        result = modelEngine.updateRemainingSearchCredits();
        assertFalse(result);
        assertEquals(0, modelEngine.getRemainingSearchCredits());
    }

    /**
     * Test the simple situation when the credit is 0
     */
    @Test
    public void testSimpleSearchCreditsWithoutMock(){
        ModelEngine modelEngine = new ModelEngine();
        modelEngine.setDefaultSearchCredits(0);
        assertEquals(0, modelEngine.getRemainingSearchCredits());
        boolean result = modelEngine.updateRemainingSearchCredits();
        assertFalse(result);
    }

    /**
     * Test whether the updateRemainingSearchCredits class update value correctly in mixed situation
     */
    @Test
    public void testMixSearchCreditsWithoutMock(){
        ModelDataParser modelDataParser = new ModelDataParser();
        ModelEngine modelEngine = new ModelEngine();
        modelEngine.setDefaultSearchCredits(3);
        boolean result = modelDataParser.checkIntegerValidation("3");
        assertTrue(result);
        assertEquals(3, modelEngine.getRemainingSearchCredits());
        result = modelEngine.updateRemainingSearchCredits();
        assertEquals(2, modelEngine.getRemainingSearchCredits());
        assertTrue(result);
        result = modelEngine.updateRemainingSearchCredits();
        assertTrue(result);
        assertEquals(1, modelEngine.getRemainingSearchCredits());
        result = modelEngine.updateRemainingSearchCredits();
        assertTrue(result);
        assertEquals(0, modelEngine.getRemainingSearchCredits());
        result = modelEngine.updateRemainingSearchCredits();
        assertFalse(result);
        assertEquals(0, modelEngine.getRemainingSearchCredits());
    }
}
