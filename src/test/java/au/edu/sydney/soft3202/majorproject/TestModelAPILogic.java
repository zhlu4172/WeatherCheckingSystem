package au.edu.sydney.soft3202.majorproject;

import au.edu.sydney.soft3202.majorproject.controller.MainPageController;
import au.edu.sydney.soft3202.majorproject.model.api.InputSystemOnline;
import au.edu.sydney.soft3202.majorproject.model.api.InputSystemOffline;
import au.edu.sydney.soft3202.majorproject.model.api.OutputSystemOnline;
import au.edu.sydney.soft3202.majorproject.model.parser.APIDataParser;
import au.edu.sydney.soft3202.majorproject.model.database.SQLModel;
import au.edu.sydney.soft3202.majorproject.model.ModelEngine;
import au.edu.sydney.soft3202.majorproject.model.parser.ModelDataParser;
import au.edu.sydney.soft3202.majorproject.model.parser.ReportParser;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

/*
This class is used to test the model including the api
 */

public class TestModelAPILogic {
    private InputSystemOnline mockInputSystemOnline;
    private MainPageController controller;
    private SQLModel mockSqlModel;
    private ModelEngine mockModelEngine;
    private APIDataParser apiDataParser = new APIDataParser();
    private FakeDataClass fakeDataClass = new FakeDataClass();


    /**
     * Test relating logic when hit the input api request by using mock
     * but the api input key is invalid(online version)
     * @throws FileNotFoundException
     */
    @Test
    public void testInputAPIWithMock_invalidKey_online() throws FileNotFoundException {
        //mock the two model classes
        ModelEngine modelEngine = Mockito.spy(ModelEngine.class);
        InputSystemOnline inputSystemOnline = Mockito.spy(InputSystemOnline.class);
        //set the related methods return value
        doReturn(null).when(inputSystemOnline).getWeatherAPI(anyDouble(),anyDouble());
        doReturn(null).when(modelEngine).modelGetWeatherApi(anyDouble(),anyDouble());
        when(modelEngine.getCityWeather(fakeDataClass.getCityPair_pair3(),
                fakeDataClass.getChoiceBoxValue_pair3()))
                .thenReturn(fakeDataClass.getCityPair_pair3());

        when(modelEngine.getModelCityWeatherDetails(fakeDataClass.getFakeAPIResponse()))
                .thenReturn(fakeDataClass.getFakeWeatherDetails_pair2());

        ArrayList<String> cityWeather = modelEngine.fetchAPI(
                fakeDataClass.getChoiceBoxValue_pair3(),"online",
                modelEngine.getCityWeather(modelEngine.getModelCityDetails(
                                fakeDataClass.getChoiceBoxValue_pair3()),
                        fakeDataClass.getChoiceBoxValue_pair3()));
        assertEquals(null,cityWeather);
        System.out.println("my getting city weather is " + cityWeather);
    }

    /**
     * Test the input api request logic with mock in online version
     * @throws FileNotFoundException
     * @throws SQLException
     */

    @Test
    public void testInputAPIWithMockOnline() throws FileNotFoundException {
        //mock the two model classes
        ModelEngine modelEngine = Mockito.spy(ModelEngine.class);
        InputSystemOnline inputSystemOnline = Mockito.spy(InputSystemOnline.class);
        //set the related methods return value
        doReturn(fakeDataClass.getFakeAPIResponse()).when(inputSystemOnline)
                .getWeatherAPI(anyDouble(),anyDouble());
        doReturn(fakeDataClass.getFakeAPIResponse())
                .when(modelEngine).modelGetWeatherApi(anyDouble(),anyDouble());
        ArrayList<String> cityWeather = modelEngine.fetchAPI(fakeDataClass.getChoiceBoxValue_pair3(),
                "online", modelEngine.getCityWeather(
                        modelEngine.getModelCityDetails(fakeDataClass.getChoiceBoxValue_pair3()),
                        fakeDataClass.getChoiceBoxValue_pair3()));
        verify(modelEngine,times(1)).getModelCityWeatherDetails(any());
    }

    /**
     * Test the output api online version with using mockito but with invalid key.
     */
    @Test
    public void testOutputAPIOnlineWithMock_invalidKey(){
        //mock two model classes
        InputSystemOnline inputSystemOnline = Mockito.spy(InputSystemOnline.class);
        OutputSystemOnline outputSystemOnline = Mockito.spy(OutputSystemOnline.class);
        ModelEngine modelEngine = Mockito.spy(ModelEngine.class);
        ReportParser reportParser = new ReportParser();
        ArrayList<ArrayList<String>> fakeQueryArray = fakeDataClass.getFakeQueryArray();
        String sendingReport = reportParser.sendReport(fakeQueryArray);
        //set the return value of mocked methods
        doReturn("Bad API request, invalid api_dev_key").when(outputSystemOnline)
                .postToOutputAPI(sendingReport, "hgjkhkj");
        String usingInput = outputSystemOnline.postToOutputAPI(reportParser.sendReport(
                fakeDataClass.getFakeQueryArray()),"hgjkhkj");
        String output = modelEngine.modelPostToOutPutChanged_2(usingInput);
        assertEquals("Bad API request, invalid api_dev_key", output);
    }


    /**
     * Test the output api online version with using mockito with the valid api output key.
     */
    @Test
    public void testOutputAPIOnlineWithMock_validKey(){
        //mock the two model classes
        ModelEngine modelEngine = Mockito.spy(ModelEngine.class);
        InputSystemOnline inputSystemOnline = Mockito.spy(InputSystemOnline.class);
        OutputSystemOnline outputSystemOnline = Mockito.spy(OutputSystemOnline.class);
        ReportParser reportParser = new ReportParser();
        String sendingReport = reportParser.sendReport(fakeDataClass.getFakeQueryArray());
        //set the return value of these mocked methods without running them
        doReturn("https://pastebin.com/UIFdu235s").when(outputSystemOnline)
                .postToOutputAPI(sendingReport, "UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6");
        String usingInput = outputSystemOnline.postToOutputAPI(reportParser.sendReport(
                fakeDataClass.getFakeQueryArray()),"UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6");
        String output = modelEngine.modelPostToOutPutChanged_2(usingInput);
        assertEquals("yes", output);
    }

    /**
     * Test the fetch input API request logic with mock
     * online version
     * @throws FileNotFoundException
     */
    @Test
    public void testInputAPIWithMock() throws FileNotFoundException {
        //test online input api
        //mock the two model classes
        mockInputSystemOnline = mock(InputSystemOnline.class);
        mockModelEngine = mock(ModelEngine.class);
        //set the related methods return value
        when(mockInputSystemOnline.getWeatherAPI(32.03,120.87)).thenReturn(
                fakeDataClass.getFakeAPIResponse());
        JsonObject jsonObject = mockInputSystemOnline.getWeatherAPI(32.03,120.87);
        when(mockModelEngine.modelGetWeatherApi(32.03,120.87))
                .thenReturn(fakeDataClass.getFakeAPIResponse());

        ArrayList<String> choiceBoxValue = new ArrayList<>();
        choiceBoxValue.add("Nantong");
        choiceBoxValue.add("Paracel Islands");

        mockModelEngine.fetchAPI(choiceBoxValue,"online", mockModelEngine.getCityWeather(
                mockModelEngine.getModelCityDetails(choiceBoxValue),choiceBoxValue));
        //verify calling time
        verify(mockInputSystemOnline,times(1)).getWeatherAPI(32.03,120.87);
        when(mockModelEngine.fetchAPI(any(),any(),any())).thenReturn(
                fakeDataClass.getFakeWeatherDetails());

        verify(mockInputSystemOnline,times(1)).getWeatherAPI(32.03,120.87);

        //test offline input api
        when(mockModelEngine.fetchAPI(choiceBoxValue,"offline",mockModelEngine.getCityWeather(
                mockModelEngine.getModelCityDetails(choiceBoxValue),choiceBoxValue)))
                .thenReturn(fakeDataClass.fakeOffline());
        ///herrrrr
//        mockInputSystemOnline.addOffline();
        verify(mockInputSystemOnline,times(1)).getWeatherAPI(any(),any());
        mockModelEngine.fetchAPI(choiceBoxValue,"offline",mockModelEngine.getCityWeather(
                mockModelEngine.getModelCityDetails(choiceBoxValue),choiceBoxValue));
    }

    /**
     * Test the fetch input API request logic with mock
     * offline/dummy version
     * @throws FileNotFoundException
     */
    @Test
    public void testInputAPIOffline() throws FileNotFoundException {
        //mock the two model classes
        mockInputSystemOnline = mock(InputSystemOnline.class);
        mockModelEngine = mock(ModelEngine.class);
        InputSystemOffline inputSystemOffline = new InputSystemOffline();
        //set the related methods
        when(mockInputSystemOnline.getWeatherAPI(32.03,120.87))
                .thenReturn(fakeDataClass.getFakeAPIResponse());
//        when(mockInputSystemOnline.addOffline()).thenReturn(fakeDataClass.fakeOffline());
//        mockAppSystem.addOffline();
        ArrayList<String> choiceBoxValue = new ArrayList<>();
        choiceBoxValue.add("Beijing");
        choiceBoxValue.add("Paracel Islands");
        when(mockModelEngine.fetchAPI(choiceBoxValue,"offline",mockModelEngine.getCityWeather(
                mockModelEngine.getModelCityDetails(choiceBoxValue),choiceBoxValue)))
                .thenReturn(fakeDataClass.getFakeWeatherDetails_pair2());

        assertEquals(inputSystemOffline.addOffline(), mockModelEngine.fetchAPI(
                choiceBoxValue,"offline",mockModelEngine.getCityWeather(
                        mockModelEngine.getModelCityDetails(choiceBoxValue),choiceBoxValue)));
        verify(mockModelEngine,times(1)).fetchAPI(any(),any(),any());
    }





    /**
     * Test relating logic when hit the input api request by using mock
     * offline version
     * @throws FileNotFoundException
     */
    @Test
    public void testInputAPIWithMock_offline() throws FileNotFoundException{
        ModelEngine modelEngine = Mockito.spy(ModelEngine.class);
        InputSystemOnline appsystem = Mockito.spy(InputSystemOnline.class);
        when(modelEngine.modelAddOffline()).thenReturn(fakeDataClass.getFakeWeatherDetails_pair2());
        ArrayList<String> cityWeather = modelEngine.fetchAPI(any(),"offline",
                modelEngine.getCityWeather(modelEngine.getModelCityDetails(
                                fakeDataClass.getChoiceBoxValue_pair3()),
                        fakeDataClass.getChoiceBoxValue_pair3()));
        System.out.println("my getting city weather is " + cityWeather);
        assertEquals(fakeDataClass.getFakeWeatherDetails_pair2(),cityWeather);
    }

    /**
     * Test whether my model can handle the problem when api key is bad.
     * @throws FileNotFoundException
     */
    @Test
    public void testSearchEmptyCity() throws FileNotFoundException {
        InputSystemOnline inputSystemOnline = new InputSystemOnline();
        ArrayList<String> gettingList = apiDataParser.getWeatherDetails(
                fakeDataClass.getErrorJsonObject());
        assertNull(gettingList);
    }


    /**
     * Test whether my model can handle and use the jsonObject correctly without using mock.
     * @throws FileNotFoundException
     */
    @Test
    public void testInputAPIWithoutMock() throws FileNotFoundException {
        InputSystemOnline appsystem = new InputSystemOnline();
        ArrayList<String> gettingList = apiDataParser.getWeatherDetails(
                fakeDataClass.getFakeResponseBody_pair2());
        assertNotNull(gettingList);
        for (int i = 0; i < 10; i++){
            assertEquals(fakeDataClass.getFakeWeatherDetails_pair2().get(i), gettingList.get(i));
        }
    }


    /**
     * Test the output api offline version without using mockito
     */
    @Test
    public void testOutputAPIOfflineWithoutMock(){
        InputSystemOnline inputSystemOnline = new InputSystemOnline();
        ModelEngine modelEngine = new ModelEngine();
        String gettingInput = modelEngine.modelPostToOutputResponseChanged(
                "offline",fakeDataClass.getFakeQueryArray());
        String output = modelEngine.modelPostToOutPutChanged_2(gettingInput);
        assertEquals("yes",output);
        assertEquals("https://pastebin.com/tgCaBgRD",gettingInput);
    }

}
