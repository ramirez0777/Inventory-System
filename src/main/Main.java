package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**Main class that starts application. Contains initial setup for GUI and starts it. RUNTIME ERROR Exception in Application start method. This error came from an incorrect path for .getResource(), this was fixed by correcting the file path and starting with a /. FUTURE ENHANCEMENT: Currently I am checking if it's an InHouse or Outsourced using try catch which leaves us with empty variables. Would be nice to change it so that we don't use unnecessary variables. Also prompt the user if they want to add Test Data instead of uncommenting it in Code.*/
public class Main extends Application {

    /**Sets the stage and loads first screen.*/
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Generic Title");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
