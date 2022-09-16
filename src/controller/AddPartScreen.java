package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;

/**This class is the controller for the Add Part Screen. Allows user to add a product to Inventory and specify type of part. Contains error checking for inputs and method to create new ID.*/
public class AddPartScreen implements Initializable {
    public TextField id;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public Label partTypeLabel;
    public TextField partTypeField;
    public TextField maxField;
    public TextField priceField;
    public TextField invField;
    public TextField nameField;
    public TextField minField;
    public boolean partTypeSetToInHouse = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**Adds a new part to the Inventory list. Runs when the save button is clicked. Verifies user input is valid and generates an Id for the part.*/
    public void savePart(ActionEvent actionEvent) throws Exception {
        int id = newId();
        String name;
        int max;
        int min;
        double price;
        int stock;
        Alert blankAlert = new Alert(Alert.AlertType.ERROR, "You left a field(s) empty");
        Alert numAlert = new Alert(Alert.AlertType.ERROR, "Only use numbers in inv, min, and max");

        if(nameField.getText().equals("")){
            blankAlert.showAndWait();
            return;
        }
        else{
            name = nameField.getText();
        }

        try{
            max = Integer.parseInt(maxField.getText());
            min = Integer.parseInt(minField.getText());
            price = Double.parseDouble(priceField.getText());
            stock = Integer.parseInt(invField.getText());
        }
        catch (NumberFormatException e){
            numAlert.showAndWait();
            return;
        }

        if(max < min){
            Alert maxOverMin = new Alert(Alert.AlertType.ERROR, "Max can't be less than Min");
            maxOverMin.showAndWait();
            return;
        }

        if(stock < min || stock > max){
            Alert stockWrong = new Alert(Alert.AlertType.ERROR, "Inv must be between Max and Min");
            stockWrong.showAndWait();
            return;
        }

        if(partTypeSetToInHouse){
            int machineId;

            try{
                machineId = Integer.parseInt(partTypeField.getText());
            }
            catch (NumberFormatException e){
                Alert machineIdAlert = new Alert(Alert.AlertType.ERROR, "Machine Id must be a number");
                machineIdAlert.showAndWait();
                return;
            }

            InHouse newPart = new InHouse(id, name, price, stock, min, max, machineId);
            Inventory.addPart(newPart);
        }
        else{
            String companyName;

            if(!(partTypeField.getText().equals(""))){
                companyName = partTypeField.getText();
                Outsourced newPart = new Outsourced(id, name, price, stock, min , max, companyName);
                Inventory.addPart(newPart);
            }
            else{
                Alert companyNameAlert = new Alert(Alert.AlertType.ERROR, "Do not leave Company Name blank");
                companyNameAlert.showAndWait();
                return;
            }
        }

        toMainScreen(actionEvent);
    }

    /**Returns a new Id number. Generates a number between 0 - maxIds, then verifies it isn't being used currently.*/
    public int newId(){
        int id = 0;
        int maxIds = 1001;
        boolean notUsed = false;
        Outsourced checkedPartO;
        InHouse checkedPartI;

        while(!notUsed){
            id = (int)(Math.random() * maxIds);
            System.out.println(id);
            notUsed = true;
            for(Object part : Inventory.getAllParts()){
                System.out.println(part.getClass());
                String checkClass = "" + part.getClass();
                if(checkClass.contains("Outsourced")){
                    checkedPartO = (Outsourced) part;
                    if(id == checkedPartO.getId())
                    {
                        notUsed = false;
                    }
                }
                else{
                    checkedPartI = (InHouse) part;
                    if(id == checkedPartI.getId()){
                        notUsed = false;
                    }
                }
            }
        }
        return id;
    }

    /**Switches the Part Type and updates label.*/
    public void inHousePart(ActionEvent actionEvent){
        partTypeLabel.setText("Machine ID");
        partTypeLabel.setFont(new Font("Default", 18.0));
        partTypeSetToInHouse = true;
    }

    /**Switches the Part Type and updates label.*/
    public void outsourcedPart(ActionEvent actionEvent){
        partTypeLabel.setText("Company Name");
        partTypeLabel.setFont(new Font("Default", 13.0));
        partTypeSetToInHouse = false;
    }

    /**Returns user to the main screen.*/
    public void toMainScreen(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("First Screen");
        stage.setScene(scene);
        stage.show();
    }

}