package au.edu.sydney.soft3202.majorproject.controller;

import au.edu.sydney.soft3202.majorproject.Main;
import au.edu.sydney.soft3202.majorproject.model.parser.ModelDataParser;
import au.edu.sydney.soft3202.majorproject.view.AutoCompleteBox;
import au.edu.sydney.soft3202.majorproject.model.ModelEngine;
import au.edu.sydney.soft3202.majorproject.model.database.SQLModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.WorldMapView;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

public class MainPageController implements Clickable{
    private String iconSymbol;
    private WorldMapView.Location currentLocation;
    private boolean test = false;
    private ModelEngine modelEngine = new ModelEngine();
    private ModelDataParser modelDataParser = new ModelDataParser();
    private Window window;
    private Stage stage;
    private Scene scene;
    private ButtonType newFetch;
    private ButtonType oldRecord;
    private AutoCompleteBox autoCompleteBox;
    private String cityValue;
    @FXML
    private TextField fillIn;
    @FXML
    private Button search;
    @FXML
    private Button report;
    @FXML
    private Slider zoom;
    @FXML
    private ListView historyList;
    @FXML
    private ComboBox combo;
    @FXML
    private SearchableComboBox searchableCombo;
    @FXML
    private WorldMapView worldMapView;
    @FXML
    private Label list;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private TextArea weatherTotal;

    @FXML
    private Label remainingSearchCredits;

    private boolean simple = false;
    private boolean natural = false;

    /**
     * Create a thread pool, allowing multiThread to be used
     */
    ExecutorService pool = Executors.newFixedThreadPool(4,
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

    public MainPageController() {
    }


    /**
     * Build up the database when the input working version is online
     * @param inputVersion
     * @param outputVersion
     */
    public void setVersion(String inputVersion, String outputVersion){
        if (inputVersion.equals("online")){
            setupDataBase(modelEngine.getSql());
        }
    }

    /**
     * Set up the autoCompete combo box before head
     * @throws FileNotFoundException
     */
    public void prepareForSearch() throws FileNotFoundException {
        setComboBoxChoiceChanged(modelEngine.modelGetCSV());
        autoCompleteBox = new AutoCompleteBox(combo);
        simple = true;
        natural = false;
    }


    /**
     * Set auto complete combo box value
     * @param totalList arraylist which needs to be set to the combo choice box
     */
    public void setComboBoxChoiceChanged(ArrayList<ArrayList<String>> totalList){
        ArrayList<String> settingList = modelDataParser.parseDataToComboBox(totalList);
        combo.setEditable(true);
        combo.getItems().addAll(settingList);
    }

    /**
     * Tackles all the button action
     * @param event
     * @throws Exception
     */
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        String button = event.getSource().toString();
        System.out.println(button);
        switch (button) {
            case "Button[id=confirm, styleClass=button]'Confirm'":
                if(checkSearchCities()){
                    if(checkAndSetNewRemainingSearchCredits()){
                        confirmChanged();
                        getChoiceBoxValueWithState();
                    }
                }
                break;
            case "Button[id=clearCache, styleClass=button]'Clear Cache'":
                if(checkInputKey()){
                    clearCache();
                }
                break;
            case "Button[id=report, styleClass=button]'Send Report'":
                sendReport();
                break;
            case "Button[id=screenShot, styleClass=button]'ScreenShot'":
                getSnapShots();
                break;
            case "MenuItem[id=about]'About'":
                break;
            case "Button[id=clear, styleClass=button]'Clear'":
                clearCurrent();
                break;
            case "Button[id=delete, styleClass=button]'Delete'":
                deleteCurrent();
                break;
        }
    }


    /**
     * Set the related work when click on the confirm button
     */
    public void confirmChanged(){
//        Runnable task1 = () -> {
        System.out.println(Thread.currentThread().getName());
        System.out.println("My Task1 started");
        boolean hasKey = checkInputKey();
        if(hasKey){
            ArrayList<String> cityWeather = null;
            if(Main.inputState.equals("online")){
                try {
                    if(modelEngine.checkCache(getChoiceBoxValueWithState())){
                        alertSub("NEW OR HISTORY?", "NewOrHistory",
                                null,cityWeather);
                    }else{
                        addSingleNewSearch();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Runnable task1 = () -> {
                        setInProgress();
                        setOfflineSpinningProgress();
                        try {
                            addNewSearchCurrent(null,false,
                                    modelEngine, Main.inputState);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        setProgressDone();
                    };
                    pool.submit(task1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

//        };
//        pool.submit(task1);
    }

    /**
     * Add single search, including fetching input api.
     * @throws FileNotFoundException
     */
    public void addSingleNewSearch() throws FileNotFoundException {
        Runnable task1 = () ->{
            setInProgress();
            ArrayList<String> cityWeather = null;
            try {
                cityWeather = modelEngine.fetchAPI(getChoiceBoxValue(), Main.inputState,
                        modelEngine.getCityWeather(
                                modelEngine.getModelCityDetails(getChoiceBoxValue()),
                                getChoiceBoxValue()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(cityWeather == null){
                System.out.println("the api returned null\n\n\n");
                setProgressDone();
                Platform.runLater(()->{
                    popUpAlert("Your fetch for weather is not successful",
                            "DATA ERROR",
                            "Your request city does not have any data. ");
                });
            }else{
                setProgressDone();
                if (cityWeather.size() != 0) {
                    try{
                        addNewSearchCurrent(cityWeather,false, modelEngine, Main.inputState);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    popUpAlert("Your fetch for weather is not successful",
                            "INPUT ERROR",
                            "Your search should not be empty, Please Try again");
                }
            }
        };
        pool.submit(task1);

    }


    /**
     * Trigger database related work in the model. Also set some updates to UI.
     * @param cityWeather
     * @param helper
     * @param modelEngine
     * @param state
     * @throws SQLException
     */
    public void addNewSearchCurrent(ArrayList<String> cityWeather, boolean helper,
                                    ModelEngine modelEngine, String state) throws SQLException {
        //call the function inside the model to tackle with the logic for adding to cache and history
        modelEngine.addToBacklog(state,helper,modelEngine,cityWeather);
        //query and show to UI
        if(!test){
            setUpdateToUI(Main.inputState);
        }
    }


    /**
     * Set some updates to UI, including the WorldMapView, choice box and history list.
     * @param state
     * @throws SQLException
     */
    public void setUpdateToUI(String state) throws SQLException {
        ArrayList<ArrayList<String>> finalGettingArrayList = modelEngine.getCurrentPairs();
        ArrayList<ArrayList<String>> finalCacheArrayList =
                modelEngine.getQueryFromModelEngine(state,modelEngine);
        ArrayList<ArrayList<String>> finalCacheArrayList1 = finalCacheArrayList;
        Platform.runLater(()->{
            setSelectedCityWeatherDetails(finalGettingArrayList);
            setWorldMapViewCurrent(finalGettingArrayList);
            setHistoryList(finalGettingArrayList);
        });
    }



    /**
     * Delete current round of history when clicking on the delete button.
     */
    public void deleteCurrent(){
        Runnable task6 = () -> {
            Platform.runLater(()->{
                if(checkCurrentHistoryChoiceEmpty()){
                    delete();
                }else{
                    popUpAlert("Your delete is not successful!","Empty choice",
                            "You have not choose a history you want to delete! " +
                                    "\nPlease try again!");
                }
            });
        };
        pool.submit(task6);
    }


    /**
     * Create a new thread when click on the screenshot button
     */
    public void getSnapShots(){
        Runnable task2 = () -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("My task2 started");
            Platform.runLater(() -> {
                getSnapShot();
            });

            try{
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("My task2 ends");
        };
        pool.submit(task2);
    }


    /**
     * Make the spinning progress spinning when it is an offline version
     */
    public void setOfflineSpinningProgress(){
        if(Main.outputState.equals("offline") || Main.inputState.equals("offline")){
            try {
                System.out.println("start sleep\n");
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Set the pop up window when we do click on the send report button, it will pop up a window
     * showing weather the send report action is successful or not
     * @param responseAfter
     * @throws InterruptedException
     */
    public void sendReportPopup(String responseAfter) throws InterruptedException {
        if(responseAfter.contains("https://pastebin.com/")){
            Platform.runLater(()->{
                setProgressDone();
                setPopUp("Paste State","success",null,responseAfter);
            });
        }else{
            Platform.runLater(()->{
                setProgressDone();
                setPopUp("Paste State", "fail", responseAfter,null);
            });
        }
    }


    /**
     * Create a new thread for send report feature and some related action
     */
    public void sendReport(){
        Runnable task5 = () -> {
            boolean hasKey = checkOutputKey();
            if(hasKey){
                if(!modelEngine.checkHistoryPairsIsEmpty()){
                    Platform.runLater(()->{
                        setInProgress();
                    });
                    setOfflineSpinningProgress();
                    try {
                        sendReportPopup(modelEngine.modelPostToOutputResponseChanged(
                                Main.outputState, modelEngine.getCurrentPairs()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    Platform.runLater(() -> {
                        popUpAlert("Empty searching history","Error",
                                "Looks like there is no searching history!\n" +
                                        "You should search one and then paste!");
                    });
                }

            }
        };
        pool.submit(task5);
    }


    /**
     * Pop up some alerts
     * @param errorHeader
     * @param errorTitle
     * @param error
     */
    public void popUpAlert(String errorHeader, String errorTitle, String error){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(errorTitle);
        alert.setHeaderText(errorHeader);
        alert.setContentText(error);
        Optional<ButtonType> result = alert.showAndWait();
    }


    /**
     * Get choice box value, which does not have state code.
     * @return an Arraylist<String>
     */
    public ArrayList<String> getChoiceBoxValue(){
        ArrayList<String> returnArray = new ArrayList<>();
        if(combo.getValue() != null){
            String city = combo.getValue().toString();
            System.out.println("hihi city" + city);
            //add the city name and country name to the array
            returnArray.add(city.split(",")[2]);
            returnArray.add(city.split(",")[0]);
        }
        System.out.println("RETURN ARRAY IS " + returnArray + "\n\n\n");
        return returnArray;
    }

    /**
     * Get choice box value, including state code.
     * @return an Arraylist<String>
     */
    public ArrayList<String> getChoiceBoxValueWithState(){
        ArrayList<String> returnArray = new ArrayList<>();
        if(combo.getValue() != null){
            String city = combo.getValue().toString();
            System.out.println("hihi city" + city);
            //add the city name and country name to the array
            returnArray.add(city.split(",")[2]);
            returnArray.add(city.split(",")[1]);
        }
        System.out.println("RETURN ARRAY IS " + returnArray + "\n\n\n");
        return returnArray;
    }



    /**
     * Clear all the records in world map
     */
    public void clearWorldMapView(){
        worldMapView.getLocations().clear();
    }


    /**
     * Set the searching history to the UI.
     * @param citiesWeatherDetail
     */
    public void setSelectedCityWeatherDetails(ArrayList<ArrayList<String>> citiesWeatherDetail){
        String totalAdding = "";
        if(citiesWeatherDetail != null && citiesWeatherDetail.size() != 0){
            totalAdding = modelDataParser.parseDataToCityWeatherDetails(citiesWeatherDetail);
            String finalTotalAdding = totalAdding;
            Platform.runLater(() -> {
                weatherTotal.setText(finalTotalAdding);
            });
        }else if(citiesWeatherDetail == null || citiesWeatherDetail.size() == 0){
            Platform.runLater(() -> {
                weatherTotal.clear();
            });
        }

    }

    /**
     * Set the search history listView
     * @param citiesWeatherDetail
     */
    public void setHistoryList(ArrayList<ArrayList<String>> citiesWeatherDetail){
        historyList.getItems().clear();
        ArrayList<String> settingList = modelDataParser.parseDataToCurrentChoices(citiesWeatherDetail);
        historyList.getItems().addAll(settingList);
    }



    /**
     * Set pop up window
     * @param popupType
     * @param popupInput
     * @param helperInput
     * @param copy
     * @return
     */
    public Optional<ButtonType> setPopUp(String popupType, String popupInput,
                                         String helperInput, String copy){
//        Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(popupType);
        ButtonType copyType = null;
        if(popupInput.equals("success")){
            alert.setHeaderText("Your Paste is successful.");
            alert.setContentText("Your copy link is " + copy);
            copyType = new ButtonType("copy URL");
//            oldRecord = new ButtonType("Fetch Record", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().add(copyType);
        }else if(popupInput.equals("fail")){
            alert.setHeaderText("Your Paste is not successful.");
            alert.setContentText("YOUR ERROR IS " + helperInput);
        }else if(popupInput.equals("NewOrHistory")) {
            newFetch = new ButtonType("Create New", ButtonBar.ButtonData.OK_DONE);
            oldRecord = new ButtonType("Fetch Record", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(newFetch, oldRecord, ButtonType.CANCEL);
            alert.setHeaderText("Cache Hit!\nYou have already searched for this city.");
            alert.setContentText("Do you want to create a new record or use the Cache.\n " +
                    "Click OK to create a new record\n Click Cancel to use the cache.");
        }
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == copyType){
            copyToClipBoard(copy);
        }
        return result;
    }

    /**
     * copy the pastebin response url to clipboard
     * @param copy
     */
    public void copyToClipBoard(String copy){
        StringSelection stringSelection = new StringSelection(copy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }


    /**
     * Pop up Alert when a cache hit happens
     * @param popupType
     * @param popupInput
     * @param helperInput
     * @param cityWeather
     */
    public void alertSub(String popupType, String popupInput, String helperInput,
                         ArrayList<String> cityWeather){

//            Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(popupType);
        alert.setHeaderText("Cache Hit!\nYou have already searched for this city in the past.");
        alert.setContentText("Do you want to create a new record or use the Cache?" +
                "\nClick OK to create a new record\nClick Cancel to use the cache.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try {
                System.out.println("hereherehrer");
                Runnable task2 = () ->{
                    setInProgress();
                    ArrayList<String> newCityWeather = null;
                    try {
                        newCityWeather = modelEngine.fetchAPI(getChoiceBoxValue(),
                                Main.inputState, modelEngine.getCityWeather(
                                        modelEngine.getModelCityDetails(getChoiceBoxValue()),
                                        getChoiceBoxValue()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(newCityWeather != null){
                        try {
                            addNewSearchCurrent(newCityWeather,true,
                                    modelEngine, Main.inputState);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        setProgressDone();
//                            setProgressDone();
                    }
                };
                pool.submit(task2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            System.out.println("no");
            try {
                modelEngine.checkCurrent(modelEngine.getMatchedHistory(
                        getChoiceBoxValueWithState().get(0),
                        getChoiceBoxValueWithState().get(1),modelEngine),false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ArrayList<ArrayList<String>> currentPairs = modelEngine.getCurrentPairs();
            setSelectedCityWeatherDetails(currentPairs);
            setWorldMapViewCurrent(currentPairs);
            setHistoryList(currentPairs);
//                    setProgressDone();
        }
//            });
    }


    /**
     * Get world Map View
     * @return WorldMapView
     */
    public WorldMapView getWorldMapView() {
        return worldMapView;
    }


    /**
     * Get zoom slider
     * @return
     */
    public Slider getZoom(){
        return zoom;
    }


    /**
     * Do some database setup by calling the related methods in model
     * @param sql
     */
    public void setupDataBase(SQLModel sql){
        modelEngine.modelSetupDB(sql);
    }


    /**
     * Show alert
     * @param input
     * @return
     */
    public ButtonType showAlert(String input){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cache Changes Warning");
        alert.setHeaderText("Changes In Cache - Are you going to " + input + " cache anyways? ");
        alert.setContentText("Choose");

        Optional<ButtonType> result = alert.showAndWait();
        System.out.println(result.get());
        return result.get();
    }


    /**
     * used for snapshot
     */
    public void getSnapShot(){
        window = worldMapView.getScene().getWindow();
        scene = worldMapView.getScene();
        stage = (Stage) worldMapView.getScene().getWindow();

        WritableImage img = new WritableImage((int) window.getWidth(), (int) window.getHeight()) ;
        Image image = scene.snapshot(img);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName("snapshot.png");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,
                        null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a new thread when click on the menubar and some corresponding actions
     */
    public void menuBarAction(){
        Platform.runLater(() -> {
            Stage popupwindow=new Stage();
            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("About");

            Label label= new Label("This application is used to inquire the weather " +
                    "in different places in the world.\n" +
                    "Developer name: Zhuyi Lu\n" +
                    "INPUT API Reference: https://www.weatherbit.io\n" +
                    "OUTPUT API Reference: https://pastebin.com/\n");
            Button button= new Button("Got it!");
            button.setOnAction(e -> popupwindow.close());
            VBox layout= new VBox(10);
            layout.getChildren().addAll(label, button);
            layout.setAlignment(Pos.CENTER);
            Scene scene1= new Scene(layout, 600, 400);
            popupwindow.setScene(scene1);
            popupwindow.showAndWait();
        });
    }


    /**
     * Set the spinning bar in progress.
     */
    public void setInProgress(){
        Platform.runLater(() -> {
            progress.setProgress(-1);
        });
    }


    /**
     * Set the spinning bar done
     */
    public void setProgressDone(){
        Platform.runLater(() -> {
            progress.setProgress(100);
        });
    }



    /**
     * Clear all the searching records when clicking on the clear button
     */
    public void clearCache(){
        Runnable task7 = () -> {
            Platform.runLater (() -> {
                if(Main.inputState.equals("online")){
                    if (showAlert("clear") == ButtonType.OK) {
                        try {
                            modelEngine.modelClearDB();
                            popUpAlert("Clear Cache Success","Success",
                                    "You clear the cache successfully");

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(Main.inputState.equals("offline")){
                    popUpAlert("Do not have permission to cache",
                            "Dummy Version Now",
                            "You are now in offline version and you cannot delete cache!" +
                                    "\n Please change your version");
                }
            });
        };
        pool.submit(task7);
    }

    /**
     * Create a new thread for clear searching history
     */
    public void clearCurrent(){
        Runnable task7 = () -> {
            modelEngine.clearCurrentPair();
            Platform.runLater (() -> {
                clearWorldMapView();
                historyList.getItems().clear();
                weatherTotal.clear();
            });
        };
        pool.submit(task7);
    }


    /**
     * Do some related work when user click on the delete button (including UI changes)
     */
    public void delete(){
        ArrayList<ArrayList<String>> gettingArrayList = new ArrayList<>();
        if(Main.inputState.equals("online")){
            removeSingleCurrentHistory();
//            ArrayList<ArrayList<String>> finalGettingArrayList = currentPairs;
            ArrayList<ArrayList<String>> finalGettingArrayList = modelEngine.getCurrentPairs();
            Platform.runLater(()->{
                setSelectedCityWeatherDetails(finalGettingArrayList);
                setWorldMapViewCurrent(finalGettingArrayList);
//                setCurrentHistoryChoices(finalGettingArrayList);
                setHistoryList(finalGettingArrayList);
            });
        }else if(Main.inputState.equals("offline")){
            clearWorldMapView();
            clearHistoryList();
            setSelectedCityWeatherDetails(gettingArrayList);
        }
    }

    /**
     * Clear history list
     */
    public void clearHistoryList(){
        this.historyList.getItems().clear();
    }

    /**
     * Remove the specified city searching history
     */
    public void removeSingleCurrentHistory(){
//        String currentSelection = currentHistory.getValue().toString();
        String currentSelection = historyList.getSelectionModel().getSelectedItem().toString();
        String stateName = currentSelection.split(",")[0];
        String cityName = currentSelection.split(",")[1];
        ArrayList<ArrayList<String>> currentPairs = modelEngine.getCurrentPairs();
        for (int i = 0; i < currentPairs.size(); i++){
            ArrayList<String> singleCurrentHistory = currentPairs.get(i);
            System.out.println("singleHistory is " + singleCurrentHistory);
            System.out.println(cityName);
            System.out.println(stateName);
            if(singleCurrentHistory.get(0).equals(cityName)
                    && singleCurrentHistory.get(1).equals(stateName)){
                modelEngine.removeCurrentPair(singleCurrentHistory);
            }
        }
    }



    /**
     * Set the world map view.
     * @param gettingArrayList
     */
    public void setWorldMapViewCurrent(ArrayList<ArrayList<String>> gettingArrayList){
        if(gettingArrayList != null && gettingArrayList.size() != 0){
            worldMapView.getLocations().clear();
            for (int i = 0; i < gettingArrayList.size(); i++){
                ArrayList<String> single = gettingArrayList.get(i);
                String icon = single.get(8);
                System.out.println("icon is " + icon + "\n\n");
                Double lat = Double.parseDouble(single.get(10));
                Double lon = Double.parseDouble(single.get(11));
                setWorldMapViewSinglePlace(icon, lat, lon);
            }
        }else{
            setWorldMapViewSinglePlace(null,null,null);
        }
    }


    /**
     * Set world map view by adding single place
     * @param icon
     * @param lat
     * @param lon
     */
    public void setWorldMapViewSinglePlace(String icon, Double lat, Double lon){
        if(icon == null || lat == null || lon == null){
            worldMapView.getLocations().clear();
        }else{
            WorldMapView.Location gettingLocation = new WorldMapView.Location(lat,lon);
            worldMapView.setLocationViewFactory(location -> {
                final Tooltip tooltip = new Tooltip();
                Button button = new Button(lat + "\n" + lon);
                button.setTextAlignment(TextAlignment.RIGHT);
                String url = "icon/" + icon + ".png";
                String iconFileName = icon + ".png";
                if(modelEngine.checkExistIcon(iconFileName)){
                    Image image = new Image(url, 50, 50,
                            true, true);
                    ImageView iv = new ImageView(image);
                    button.setGraphic(iv);
                }
                button.setTooltip(tooltip);
                Tooltip.install(button,tooltip);
                return button;
            });
            worldMapView.getLocations().add(gettingLocation);
        }
    }



    /**
     * Check whether the history choice is empty
     * @return boolean
     */
    public boolean checkCurrentHistoryChoiceEmpty(){
        if(historyList.getSelectionModel().getSelectedItem() == null){
            System.out.println("it is null!!!!\n\n\n\n");
            return false;
        }else{
            return true;
        }
    }

    /**
     * Check whether the input key is correctly set when needs that
     */
    public boolean checkInputKey(){
        if(Main.inputState.equals("online") && Main.inputKey == null){
            Platform.runLater(()->{
                popUpAlert("You are in the online version but " +
                                "you haven't set the input key! ",
                        "Empty INPUT KEY",
                        "Please exit the application and set again");
            });
            return false;
        }
        return true;
    }


    /**
     * Check whether the output key is correctly set when needs that
     */
    public boolean checkOutputKey(){
        if(Main.outputState.equals("online") && Main.outputKey == null){
            Platform.runLater(()->{
                popUpAlert("You are in the online version but you haven't set the " +
                                "output key! ",
                        "Empty OUTPUT KEY",
                        "Please exit the application and set again");
            });
            return false;
        }
        return true;
    }

    /**
     * Some related UI updating work when it has history or not(from model)
     * @param cityWeather
     * @return
     * @throws SQLException
     */
    public boolean hasRecord(ArrayList<String> cityWeather) throws SQLException {
        System.out.println("\n\n\nso shity\n\n");
        boolean hasRecord = modelEngine.hasRecord(cityWeather);
        if(hasRecord){
            Optional<ButtonType> result = setPopUp("Cache Hit",
                    "You have already searched this city!",
                    "Do you want to search again anyways?",null);
            if(result.get() == ButtonType.OK){
                return true;
            }
            return false;
        }
        return true;
    }


    /**
     * Check input version
     * @return
     */
    public boolean checkInputVersion(){
        if(Main.inputState.equals("online")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Set input state, which is a helper method for tests.
     * @param state
     */
    public void setState(String state){
        Main.inputState = state;
    }

    /**
     * A helper method for tests.
     * @param testVersion
     */
    public void setTest(boolean testVersion){
        test = testVersion;
    }

    /**
     * Check whether user select the city from choice box.
     * @return boolean
     */
    public boolean checkSearchCities(){
        String value = combo.getValue().toString();
        System.out.println(value);
        int count = 0;
        for (int i = 0; i < value.length(); i++){
            if(value.charAt(i) == ','){
                count += 1;
            }
        }
        System.out.println(count);
        if(count != 2){
            popUpAlert("Error","Cannot search",
                    "You should select one from the choice box and search. " +
                            "\nPlease try again!");
            return false;
        }
        return true;
    }

    /**
     * Set modelEngine for the controller, used in tests
     * @param modelEngine
     */
    public void setModelEngine(ModelEngine modelEngine){
        this.modelEngine = modelEngine;
    }


    /**
     * Pop up Error
     * @param errorHeader: the header of the error
     * @param errorTitle: the title of the error
     * @param error: the error message
     */
    public void popUpError(String errorHeader, String errorTitle, String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setHeaderText(errorHeader);
        alert.setContentText(error);
        alert.showAndWait();
    }

    /**
     * Pop up text input dialog.
     * Set Search credits including error handling
     * Pop up alert if needed
     */
    public void setSearchCredits(){
        TextInputDialog dialogInput = new TextInputDialog();
        dialogInput.setTitle("Set Search Credits");
        dialogInput.setHeaderText("Enter your preferred search credits number.");
        dialogInput.setContentText("You need to enter a number between 1 and 10 (edge excluded)\n" +
                "which is the number of search credits you are allowed to use afterwards.");
        dialogInput.showAndWait();
        String gettingResult = dialogInput.getResult();
        try{
            // if the integer user input is not between 1 and 10, pop up alert
            if(!modelDataParser.checkIntegerValidation(gettingResult)){
                popUpAlert("Invalid Search Credits","Invalid Search Credits!",
                        "You set the invalid Search Credits!\n" +
                                "You should type in an exact Integer between 1 and 10(excluded)\n" +
                                "Please try again!");
                this.setSearchCredits();
            }else{
                Integer gettingValue = Integer.parseInt(gettingResult);
                modelEngine.setDefaultSearchCredits(gettingValue);
                setDefaultSearchCredits(gettingResult);
            }
        }catch(Exception e){
            // if some invalid things in the input, pop up alert
            popUpAlert("Invalid Search Credits","Invalid Search Credits!",
                    "You set the invalid Search Credits!\n" +
                            "It should be an exact Integer between 1 and 10(excluded)!\n" +
                            "Please try again!");
            this.setSearchCredits();
        }
//        }

    }

    /**
     * Parse the dialog input text to modelEngine
     * Set the label text representing remaining search credits from modelEngine
     * @param string: setting string to UI
     */
    public void setDefaultSearchCredits(String string){
        String originalRemaining = this.remainingSearchCredits.getText();
        originalRemaining = originalRemaining + string;
        this.remainingSearchCredits.setText(originalRemaining);
    }

    /**
     * Update the label text representing remaining search credits from modelEngine
     * @return whether can user do the search action
     */
    public Boolean checkAndSetNewRemainingSearchCredits(){
        Boolean canChange = modelEngine.updateRemainingSearchCredits();
        //check whether it reach the limits
        if(canChange){
            String originalRemaining = "Remaining Search Credits: ";
            originalRemaining = originalRemaining + modelEngine.getRemainingSearchCredits().toString();
            String finalOriginalRemaining = originalRemaining;
            this.remainingSearchCredits.setText(finalOriginalRemaining);
            return true;
        }else{
            // if reach the search limit, pop up alert
            popUpError("Error","Run out of Search Credits",
                    "You have run out of search credits!");
            return false;
        }

    }





}