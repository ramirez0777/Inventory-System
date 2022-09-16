package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This class holds all the inventory including parts and products. Is used for search operations either by string or int. Allows you to add, delete or update inventory items.*/
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**Searches allParts list by Id and returns Part, if nothing is found returns null.*/
    public static Part lookupPart(int partId){
        for(Part part : allParts){
            if(part.getId() == partId){
                return part;
            }
        }
        return null;
    }

    /**Searches allProducts list by Id and returns Product, if nothing is found returns null.*/
    public static Product lookupProduct(int productId){
        for (Product product : allProducts) {
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }

    /**Searches allParts list by Name and returns Observable List of all matches, if nothing is found returns null.*/
    public static ObservableList lookupPart(String partName){
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for(Part part : allParts){
            if(part.getName().contains(partName)){
                parts.add(part);
            }
        }
        return parts;
    }

    /**Searches allProducts list by Name and returns Observable List of all matches, if nothing is found returns null.*/
    public static ObservableList lookupProduct(String productName){
        ObservableList<Product> products = FXCollections.observableArrayList();
        for(Product product : allProducts){
            if(product.getName().contains(productName)){
                products.add(product);
            }
        }
        return products;
    }

    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**Removes Part from Inventory and returns true, if nothing is selected returns false*/
    public static boolean deletePart(Part selectedPart){
        if(selectedPart != null) {
            getAllParts().remove(selectedPart);
            return true;
        }
        return false;
    }

    /**Removes Product from Inventory and returns true, if nothing is selected returns false*/
    public static boolean deleteProduct(Product selectedProduct){
        if(selectedProduct != null && selectedProduct.getAllAssociatedParts().size() <= 0) {
            allProducts.remove(selectedProduct);
            return true;
        }
        return false;
    }

    public static ObservableList getAllParts(){
        return allParts;
    }

    public static ObservableList getAllProducts(){
        return allProducts;
    }
}
