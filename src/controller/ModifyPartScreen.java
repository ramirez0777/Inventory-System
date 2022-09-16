package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.net.URL;
import java.util.ResourceBundle;

/**This class is the controller for the Modify Part Screen. Imports data of selected part when loaded. Allows you to change the type of Part and any attribute of that part. Contains error checking for incorrect inputs.*/
public class ModifyPartScreen implements Initializable {
    public static InHouse partToModifyI;
    public static Outsourced partToModifyO;
    public static boolean partTypeSetToInHouse;
    public static int index;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public TextField idField;
    public TextField nameField;
    public TextField invField;
    public TextField priceField;
    public TextField maxField;
    public TextField minField;
    public TextField partTypeField;
    public Label partTypeLabel;

    /**Runs when screen is started. Fills in information of the selected part.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        if(partTypeSetToInHouse){
            inHouse.setSelected(true);
            idField.setText(Integer.toString(partToModifyI.getId()));
            nameField.setText(partToModifyI.getName());
            invField.setText(Integer.toString(partToModifyI.getStock()));
            priceField.setText(Double.toString(partToModifyI.getPrice()));
            maxField.setText(Integer.toString(partToModifyI.getMax()));
            minField.setText(Integer.toString(partToModifyI.getMin()));
            partTypeField.setText(Integer.toString(partToModifyI.getMachineId()));
        }
        else{
            outsourced.setSelected(true);
            idField.setText(Integer.toString(partToModifyO.getId()));
            nameField.setText(partToModifyO.getName());
            invField.setText(Integer.toString(partToModifyO.getStock()));
            priceField.setText(Double.toString(partToModifyO.getPrice()));
            maxField.setText(Integer.toString(partToModifyO.getMax()));
            minField.setText(Integer.toString(partToModifyO.getMin()));
            partTypeField.setText(partToModifyO.getCompanyName());
            partTypeLabel.setFont(new Font("Default", 13.0));
            partTypeLabel.setText("Company Name");
        }

    }

    /**Applies changes to the selected part. Validates information and shows error if something is incorrect. If not then saves part with new information*/
    public void modifyPart(ActionEvent actionEvent) throws Exception {
        int id = Integer.parseInt(idField.getText());
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
            InHouse partToUpdate = new InHouse(id, name, price, stock, min, max, machineId);
            Inventory.updatePart(index, partToUpdate);
        }
        else{
            String companyName;
            if(!(partTypeField.getText().equals(""))){
                companyName = partTypeField.getText();
                Outsourced partToUpdate = new Outsourced(id, name, price, stock, min , max, companyName);
                Inventory.updatePart(index, partToUpdate);
            }
            else{
                Alert companyNameAlert = new Alert(Alert.AlertType.ERROR, "Do not leave Company Name blank");
                companyNameAlert.showAndWait();
                return;
            }
        }

        toMainScreen(actionEvent);
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

    /**Switches to the Main Screen.*/
    public void toMainScreen(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("First Screen");
        stage.setScene(scene);
        stage.show();
    }
}