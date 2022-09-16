package model;

/**This class inherits from Part. Allows you to get and set the company name.*/
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**Sets company name attribute*/
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**Returns company name attribute*/
    public String getCompanyName(){
        return this.companyName;
    }
}
