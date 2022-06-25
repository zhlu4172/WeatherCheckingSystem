package au.edu.sydney.soft3202.majorproject;
import au.edu.sydney.soft3202.majorproject.controller.MainPageController;
import au.edu.sydney.soft3202.majorproject.model.api.InputSystemOnline;
import au.edu.sydney.soft3202.majorproject.model.database.SQLModel;
import au.edu.sydney.soft3202.majorproject.model.ModelEngine;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;
public class TestDBLogic {
    private InputSystemOnline mockInputSystemOnline;
    private MainPageController controller;
    private SQLModel mockSqlModel;
    private ModelEngine mockModelEngine;
    private FakeDataClass fakeDataClass = new FakeDataClass();

    /**
     * Test the logic of querying value from database when it is online version
     * The DB queried value will be mocked.
     * @throws SQLException
     */
    @Test
    public void testDBWrapperWithMockOnline() throws SQLException {
        controller = new MainPageController();
        //mock two models
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        when(mockSqlModel.queryWeather()).thenReturn(fakeDataClass.getFakeQueryArray());
        when(mockModelEngine.modelQuerySql()).thenReturn(fakeDataClass.getFakeQueryArray());
        System.out.println(mockModelEngine.getQueryFromModelEngine("online",mockModelEngine));
        verify(mockModelEngine,times(2)).modelQuerySql();
        assertEquals(fakeDataClass.getFakeQueryArray(),mockModelEngine.getQueryFromModelEngine(
                "online",mockModelEngine));
        verify(mockModelEngine,times(3)).modelQuerySql();
    }

    /**
     * Test the logic of querying value from database when it is offline/dummy version
     * The DB queried value will be mocked.
     * @throws SQLException
     */
    @Test
    public void testDBWrapperWithMockOffline() throws SQLException {
        controller = new MainPageController();
        InputSystemOnline inputSystemOnline = Mockito.spy(InputSystemOnline.class);
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
//        when(inputSystemOnline.getFakeQueryArray()).thenReturn(fakeDataClass.getFakeQueryArray());
        assertEquals(fakeDataClass.getFakeQueryArray(),mockModelEngine.getQueryFromModelEngine(
                "offline",mockModelEngine));
    }

    /**
     * Test the logic of query city by name in online version
     * @throws SQLException
     */
    @Test
    public void testDBWrapperWithMockOnlineQueryCityByName() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
//        mockModelEngine = mock(ModelEngine.class);
        when(mockSqlModel.queryWeather()).thenReturn(fakeDataClass.getFakeQueryArrayTwoCity());
        when(mockSqlModel.queryWeatherByCityName("Nanjing","04"))
                .thenReturn(fakeDataClass.getFakeWeatherDetails_Nanjing());
        when(mockModelEngine.getSql()).thenReturn(mockSqlModel);

        mockModelEngine.getMatchedHistory("Nanjing","04",mockModelEngine);
        //verify it is "Nanjing" that match the set.
        verify(mockSqlModel,times(1)).queryWeatherByCityName(
                "Nanjing","04");
        //check if it is not the city we search, the time of the call will be 0
        verify(mockSqlModel,times(0)).queryWeatherByCityName(
                "Shanghai","03");
    }

    /**
     * Check whether the checkCache() method works fine.
     * @throws SQLException
     */
    @Test
    public void testCheckCacheWithMock() throws SQLException {
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        when(mockSqlModel.queryWeather()).thenReturn(fakeDataClass.getFakeQueryArrayTwoCity());
        ArrayList<String> newSearch = new ArrayList<>();
        newSearch.add("Nantong");
        newSearch.add("04");
        //check whether the checkCache function can find the matched history
        assertTrue(mockModelEngine.checkCache(newSearch));

        ArrayList<String> searchNotExist = new ArrayList<>();
        searchNotExist.add("Shanghai");
        searchNotExist.add("01");
        //check whether it returns false when there is no such history search
        assertFalse(mockModelEngine.checkCache(searchNotExist));
    }

    /**
     * Test the logic when we need to add a new cache to the DB when it is online version
     * @throws SQLException
     */
    @Test
    public void testDBAddOnlineWithMock() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        controller.setState("online");
        controller.setTest(true);
        //add once
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails_pair2(),false,
                mockModelEngine, "online");
        verify(mockModelEngine,times(1)).modelAddSql(
                fakeDataClass.getFakeWeatherDetails_pair2());
        //add another city
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails_Nanjing(),false,
                mockModelEngine, "online");
        verify(mockModelEngine,times(1)).modelAddSql(
                fakeDataClass.getFakeWeatherDetails_Nanjing());
        //verify total times of call
        verify(mockModelEngine,times(2)).modelAddSql(any());
    }

    /**
     * Test the logic when we need to update the DB cache when it is online version
     * @throws SQLException
     */
    @Test
    public void testDBAddOnlineWithMockUpdateDB() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        controller.setState("online");
        controller.setTest(true);
        //add once
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails_pair2(),false,
                mockModelEngine, "online");
        verify(mockModelEngine,times(1)).modelAddSql(
                fakeDataClass.getFakeWeatherDetails_pair2());
        //update this city details
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails_pair2Exchange(),
                true, mockModelEngine, "online");
        verify(mockModelEngine,times(1)).modelReplaceDB(
                fakeDataClass.getFakeWeatherDetails_pair2Exchange());
        //verify total times of call
        verify(mockModelEngine,times(1)).modelAddSql(any());
    }

    /**
     * Add a new search when it is offline/online version
     * @throws SQLException
     */
    @Test
    public void testDBAddOfflineWithMock() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        controller.setState("offline");
        controller.setTest(true);
        //add once
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails_pair2(),false,
                mockModelEngine, "offline");
        verify(mockModelEngine,times(1)).setCurrentPairs(any());
        assertEquals(fakeDataClass.getFakeQueryArray(),mockModelEngine.getCurrentPairs());
    }

    /**
     * Test replace DB cache when it is online version
     * @throws SQLException
     */
    @Test
    public void testDBReplaceOnlineWithMock() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        controller.setState("online");
        controller.setTest(true);
        //add once
        controller.addNewSearchCurrent(fakeDataClass.getFakeWeatherDetails(),false,
                mockModelEngine, "online");
        verify(mockModelEngine,times(1)).modelAddSql(
                fakeDataClass.getFakeWeatherDetails());
    }

    /**
     * Test the logic that whether cache has the corresponding city weather report when online.
     * @throws SQLException
     */
    @Test
    public void checkModelHasRecordWithMock() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        controller.setState("online");
        //only one city: nantong inside the array, check whether it is true
        when(mockModelEngine.modelQuerySql()).thenReturn(fakeDataClass.getFakeQueryArray());
        assertEquals(true,mockModelEngine.hasRecord(fakeDataClass.getFakeWeatherDetails_pair2()));
        assertEquals(false,mockModelEngine.hasRecord(fakeDataClass.getFakeWeatherDetails_Nanjing()));
        //now add another city nanjing to the array, check whether it is true now
        when(mockModelEngine.modelQuerySql()).thenReturn(fakeDataClass.getFakeQueryArrayTwoCity());
        assertEquals(true,mockModelEngine.hasRecord(fakeDataClass.getFakeWeatherDetails_pair2()));
        assertEquals(true,mockModelEngine.hasRecord(fakeDataClass.getFakeWeatherDetails_Nanjing()));
    }

    /**
     * Test the Database setup and clear model with mock
     */
    @Test
    public void testDBSetupAndClearWithMock() throws SQLException {
        controller = new MainPageController();
        mockSqlModel = mock(SQLModel.class);
        mockModelEngine = Mockito.spy(ModelEngine.class);
        mockModelEngine.setSql(mockSqlModel);
        doReturn(mockSqlModel).when(mockModelEngine).getSql();
        controller.setModelEngine(mockModelEngine);
        controller.setState("online");
        controller.setupDataBase(mockSqlModel);
        verify(mockModelEngine,times(1)).modelSetupDB(any());
        verify(mockSqlModel,times(1)).createDB();
        verify(mockSqlModel,times(1)).setupDB();
        //test clear
        mockModelEngine.modelClearDB();
        verify(mockSqlModel,times(1)).clearDB();
    }
}
