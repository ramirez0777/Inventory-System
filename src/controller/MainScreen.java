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
import model.*;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**This class is the controller for the Main Screen. Gathers data for the two tables on the screen. Allows you to delete Parts and Products from their respective tables. Contains navigation to other screens and error checking if items aren't selected when they are required.*/
public class MainScreen implements Initializable {
    public TableView partsTable;
    public TableColumn partID;
    public TableColumn partName;
    public TableColumn partInventory;
    public TableColumn partPrice;
    public TableView productsTable;
    public TableColumn productID;
    public TableColumn productName;
    public TableColumn productInventory;
    public TableColumn productPrice;
    public TextField productSearch;
    public TextField partSearch;

    /**Runs when screen is started and sets information for tables.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //addTestData();

        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(Inventory.getAllProducts());
    }
    //Uncomment this and the first line under Initialize to add test data
/*
    private static boolean firstTime = true;
    private void addTestData(){
        if(firstTime){
            InHouse whale = new InHouse(1, "barney", 4.0, 3, 1, 4, 12345);
            Inventory.addPart(whale);
            Outsourced dog = new Outsourced(2, "chile", 12.0, 3, 1, 4, "Mars");
            Inventory.addPart(dog);
            firstTime = false;
            System.out.println("test items added");
            Product cat = new Product(1, "child", 4.0, 1, 1, 4);
            cat.addAssociatedPart(whale);
            Product notCat = new Product(2, "adulet", 4.1, 2, 5, 3);
            notCat.addAssociatedPart(dog);
            Inventory.addProduct(cat);
            Inventory.addProduct(notCat);
        }
        else{
            return;
        }
    }
*/

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

        partsTable.setItems(returnList);
    }

    /**Searches the all product table either by string or int. If input is blank it returns the full list.*/
    public void productSearch(ActionEvent actionEvent){
        String search = productSearch.getText();
        ObservableList<Product> returnList = FXCollections.observableArrayList();
        returnList = Inventory.lookupProduct(search);

        //string search, then int search
        if(returnList.size() == 0){
            try {
                int intSearch = Integer.parseInt(search);
                Product oneProduct = Inventory.lookupProduct(intSearch);
                if(oneProduct != null){
                    returnList.add(oneProduct);
                }
            }
            catch (NumberFormatException e){}
        }

        if(returnList.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found. Try again.");
            alert.showAndWait();
            return;
        }

        productsTable.setItems(returnList);
    }

    /**Deletes part from the Inventory list. Confirms deletion with user and if no part is selected, displays an error.*/
    public void deletePart(ActionEvent actionEvent){
        Object selected = partsTable.getSelectionModel().getSelectedItem();
        boolean isInHouse = true;
        InHouse inhouse;
        Outsourced outsourced;
        boolean deleted;

        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Optional<ButtonType> confirm = confirmDeletion.showAndWait();
        if(!(confirm.isPresent() && confirm.get() == ButtonType.OK)){
            return;
        }

        try{
            inhouse = (InHouse) selected;
        }
        catch(Exception e) {
            outsourced = (Outsourced) selected;
            isInHouse = false;
        }

        if(isInHouse) {
            //wasn't deleted
            inhouse = (InHouse) selected;
            deleted = Inventory.deletePart(inhouse);
        }
        else{
            outsourced = (Outsourced) selected;
            deleted = Inventory.deletePart(outsourced);
        }

        if(!deleted) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part was not deleted");
            alert.showAndWait();
        }
    }

    /**Deletes product from the Inventory list. Confirms deletion with user and if no part is selected, displays an error.*/
    public void deleteFromProduct(ActionEvent actionevent){
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();

        Alert confirmDeletion = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product? It will not delete if it has associated parts.");
        Optional<ButtonType> confirm = confirmDeletion.showAndWait();
        if(!(confirm.isPresent() && confirm.get() == ButtonType.OK)){
            return;
        }

        if(!Inventory.deleteProduct(selectedProduct)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product was not deleted");
            alert.showAndWait();
        }
    }

    /**Switches to add product screen.*/
    public void toAddProductScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**Switches to the Modify Product Screen. If a product is not selected it shows an error instead.*/
    public void toModifyProductScreen(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR, "No product selected");
            noSelectionAlert.showAndWait();
            return;
        }
        ModifyProductScreen.index = Inventory.getAllProducts().indexOf(selectedProduct);
        ModifyProductScreen.productToModify = selectedProduct;
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProductScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**Switches to Add Part Screen.*/
    public void toAddPartScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**Switches to Modify Part Screen. If no Part is selected it displays an error instead.*/
    public void toModifyPartScreen(ActionEvent actionEvent) throws IOException {
        Object selectedPart = partsTable.getSelectionModel().getSelectedItem();
        InHouse inHouse;
        Outsourced outsourced;

        if(selectedPart == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR, "No part selected");
            noSelectionAlert.showAndWait();
            return;
        }

        ModifyPartScreen.index = Inventory.getAllParts().indexOf(selectedPart);

        try{
            inHouse = (InHouse) selectedPart;
            ModifyPartScreen.partToModifyI = inHouse;
            ModifyPartScreen.partTypeSetToInHouse = true;
        }
        catch (Exception e){
            outsourced = (Outsourced) selectedPart;
            ModifyPartScreen.partToModifyO = outsourced;
            ModifyPartScreen.partTypeSetToInHouse = false;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPartScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**Closes application.*/
    public void quit(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
