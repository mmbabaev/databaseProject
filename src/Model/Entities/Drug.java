package model.entities;

public class Drug {

    public Drug(int drug_id, String name){
        this.drug_id = drug_id;
        this.name = name;
    }

    public long getDrugstore_id() {
        return drug_id;
    }

    public String getName() {
        return name;
    }

    public void setDrugstore_id(int drugstore_id) {
        this.drug_id = drugstore_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    int drug_id;
    String name;
}
