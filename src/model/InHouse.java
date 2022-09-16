package model;

/**InHouse class inherits from Part. Uses Part constructor, and allows you to set and get the Machine ID.*/
public class InHouse extends Part{
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**Sets machineId attribute*/
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**Returns machineId attribute*/
    public int getMachineId(){
        return this.machineId;
    }
}
