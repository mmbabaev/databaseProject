package model.entities;

/**
 * Created by michaelborisov on 09.06.16.
 */
public class Drugstore {

    public Drugstore(int drugstore_id, String name){
        this.drugstore_id = drugstore_id;
        this.name = name;
    }

    public long getDrugstore_id() {
        return drugstore_id;
    }

    public String getName() {
        return name;
    }

    public void setDrugstore_id(int drugstore_id) {
        this.drugstore_id = drugstore_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    int drugstore_id;
    String name;
}
