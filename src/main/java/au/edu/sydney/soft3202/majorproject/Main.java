package au.edu.sydney.soft3202.majorproject;

import au.edu.sydney.soft3202.majorproject.controller.MainPageController;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
    public static String inputKey;
    public static String outputKey;
    public static String inputState;
    public static String outputState;

    /**
     * Start up and default work when application starts
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        fetchEnvironmentVariable();
        System.out.println(inputKey);
        FXMLLoader mainPage = new FXMLLoader(getClass().getResource("/view/mainPage.fxml"));
        Parent root = mainPage.load();
        MainPageController mainPageController = mainPage.getController();
        mainPageController.setVersion(inputState,outputState);
        mainPageController.prepareForSearch();
        mainPageController.setSearchCredits();
        Bindings.bindBidirectional(mainPageController.getZoom().valueProperty(),
                mainPageController.getWorldMapView().zoomFactorProperty());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WeatherBit App");
        primaryStage.show();
    }

    /**
     * Fetch environment variable
     */
    public static void fetchEnvironmentVariable(){
        inputKey = System.getenv("INPUT_API_KEY");
        outputKey = System.getenv("PASTEBIN_API_KEY");
    }


    /**
     * Set the input and output state for the whole application
     * @param args
     */
    public static void setState(String[] args){
        if (args.length == 0){
            inputState = "online";
            outputState = "online";
        }else{
            inputState = args[0];
            outputState = args[1];
        }
    }

    /**
     * Main function for the application, set the state and launch the application.
     * @param args
     */
    public static void main(String[] args) {
        setState(args);
        launch(args);
    }
}

