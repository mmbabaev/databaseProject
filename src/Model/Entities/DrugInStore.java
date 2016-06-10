package model.entities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class DrugInStore {
    private final SimpleStringProperty name;
    private final SimpleStringProperty cost;

    public DrugInStore(String name, String cost){
        this.name = new SimpleStringProperty(name);
        this.cost = new SimpleStringProperty(cost);
    }

    public String getName() {
        return name.get();
    }


    public String getCost() {
        return cost.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

    public void setCost(String fCost) {
        cost.set(fCost);
    }
}