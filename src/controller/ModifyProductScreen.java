package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This class is the controller for the Modify Part Screen. Imports all data of the selected part. Allows you to change any aspect of the part including the type. Contains error checking for user input. RUNTIME ERROR: Most of the code is reused from the Add Product Screen, some were not renamed correctly in the process. Fixed by renaming variables. FUTURE ENHANCEMENT: Add a delete button from this screen, that disassociates all parts. This would make it so you have less clicks when trying to delete a product.*/
public class ModifyProductScreen implements Initializable {
    public static Product productToModify;
    public static int index;
    public TableView allPartsTable;
    public TableView addedPartsTable;
    public TextField nameField;
    public TextField invField;
    public TextField maxField;
    public TextField minField;
    public TextField priceField;
    public TextField partSearch;
    public TableColumn allIdCol;
    public TableColumn allNameCol;
    public TableColumn allInvCol;
    public TableColumn allPriceCol;
    public TableColumn addedIdCol;
    public TableColumn addedNameCol;
    public TableColumn addedInvCol;
    public TableColumn addedPriceCol;

    /**Runs when screen is started. Fills in information of the selected product.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        nameField.setText(productToModify.getName());
        invField.setText(Integer.toString(productToModify.getStock()));
        minField.setText(Integer.toString(productToModify.getMin()));
        maxField.setText(Integer.toString(productToModify.getMax()));
        priceField.setText(Double.toString(productToModify.getPrice()));

        allIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsTable.setItems(Inventory.getAllParts());

        addedIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addedInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addedPartsTable.setItems(productToModify.getAllAssociatedParts());
    }

    /**Applies changes to the selected product. Validates information and shows error if something is incorrect. If not then saves product with new information*/
    public void saveProduct(ActionEvent actionEvent) throws Exception {
        int id;
        String name;
        int max;
        int min;
        double price;
        int stock;
        Alert blankAlert = new Alert(Alert.AlertType.ERROR, "You left a field(s) empty");
        Alert numAlert = new Alert(Alert.AlertType.ERROR, "Only use numbers in price, inv, min, and max");

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

        productToModify.setId(productToModify.getId());
        productToModify.setName(name);
        productToModify.setStock(stock);
        productToModify.setPrice(price);
        productToModify.setMax(max);
        productToModify.setMin(min);

        Inventory.updateProduct(index, productToModify);

        toMainScreen(actionEvent);
    }

    /**Verifies a part is selected then associates it to the product that is being changed.*/
    public void addPart(ActionEvent actionEvent){
        Object selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
        InHouse partI = null;
        Outsourced partO = null;
        boolean isInHouse;

        if(selectedPart == null) {
            Alert noneSelected = new Alert(Alert.AlertType.ERROR, "No Part selected");
            noneSelected.showAndWait();
            return;
        }

        try{
            partI = (InHouse) selectedPart;
            isInHouse = true;
        }
        catch(Exception e){
            partO = (Outsourced) selectedPart;
            isInHouse = false;
        }

        if(isInHouse) {
            productToModify.addAssociatedPart(partI);
        }
        else{
            productToModify.addAssociatedPart(partO);
        }
    }

    /**Removes a part from the associated list of parts for the Product being changed.*/
    public void removeAddedPart(ActionEvent actionEvent){
        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> confirm = confirmDeletion.showAndWait();
        if(!(confirm.isPresent() && confirm.get() == ButtonType.OK)){
            return;
        }

        Object selectedPart = addedPartsTable.getSelectionModel().getSelectedItem();
        InHouse partI = null;
        Outsourced partO = null;
        boolean isInHouse;
        Alert deleted = new Alert(Alert.AlertType.CONFIRMATION, "Part Removed");
        Alert notDeleted = new Alert(Alert.AlertType.ERROR, "No part was selected");

        try{
            partI = (InHouse) selectedPart;
            isInHouse = true;
        }
        catch(Exception e){
            partO = (Outsourced) selectedPart;
            isInHouse = false;
        }

        if(isInHouse){
            if(productToModify.deleteAssociatedPart(partI)){
                deleted.showAndWait();
                return;
            }
            else{
                notDeleted.showAndWait();
            }
        }
        else{
            if(productToModify.deleteAssociatedPart(partO)){
                deleted.showAndWait();
                return;
            }
            else{
                notDeleted.showAndWait();
            }
        }
    }

    /**Searches the all part table either by string or int. If input is blank it returns the full list.*/
    public void partSearch(ActionEvent actionEvent){
        String search = partSearch.getText();
        ObservableList<Object> returnList = FXCollections.observableArrayList();
        returnList = Inventory.lookupPart(search);

        if(returnList.size() == 0){
            try {
                int intSearch = Integer.parseInt(search);
                Object onePart;
                onePart = Inventory.lookupPart(intSearch);
                if(onePart != null){
                    returnList.add(onePart);
                }
            }
            catch (NumberFormatException e){}
        }

        if(returnList.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part not found. Try again.");
            alert.showAndWait();
            return;
        }

        allPartsTable.setItems(returnList);

    }

    /**Returns user to the main screen*/
    public void toMainScreen(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("First Screen");
        stage.setScene(scene);
        stage.show();
    }
}