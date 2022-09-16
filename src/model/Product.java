package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This class has getters and setters for every attribute. As well contains an Observable List of parts that can be added to and removed from.*/
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public void setMin(int min){
        this.min = min;
    }

    public void setMax(int max){
        this.max = max;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public int getStock(){
        return this.stock;
    }

    public int getMin(){
        return this.min;
    }

    public int getMax(){
        return this.max;
    }

    /**Adds selected part to the associatedParts list.*/
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**Removes selected part from the associatedParts list.*/
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(selectedAssociatedPart != null)
        {
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        return false;
    }

    /**Returns list of all associated parts*/
    public ObservableList getAllAssociatedParts(){
        return this.associatedParts;
    }
}
