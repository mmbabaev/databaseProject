package GUI;

import Model.Entities.Drug;
import Model.Entities.User;
import Model.sql.RegistrationException;
import Model.sql.SqlDriver;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfx.messagebox.MessageBox;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchView {

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    Stage primaryStage;
    SqlDriver driver;
    public SearchView(SqlDriver driver){
        this.driver = driver;
        table.setEditable(true);
        primaryStage = new Stage();
        primaryStage.setTitle("Поиск лекарства");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);

        //Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Название");
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);


        //Defining the Search button
        Button search = new Button("Найти");
        GridPane.setConstraints(search, 0, 1);
        grid.getChildren().add(search);

        //Defining the Add button (Добавляет в закладки выделенное)
        Button addButton = new Button("Добавить в закладки");
        GridPane.setConstraints(addButton, 1, 1);
        grid.getChildren().add(addButton);


        //Create Name column
        TableColumn nameCol = new TableColumn("Название аптеки");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        //Create Cost column
        TableColumn costCol = new TableColumn("Цена");
        costCol.setCellValueFactory(
                new PropertyValueFactory<>("cost"));

        //Create Check column
        TableColumn checkedCol = new TableColumn("В закладки");
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("checked"));
        checkedCol.setCellFactory(param -> new CheckBoxTableCell<Pharmacy, Boolean>());



        table.setItems(data);
        table.getColumns().addAll(nameCol, costCol, checkedCol);
        System.out.println(table.getColumns().size());
        table.setMinWidth(width * 0.7);
        table.setMinHeight(height * 0.8);
        costCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        nameCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        checkedCol.setMinWidth(table.getMinWidth() / 5);
        GridPane.setConstraints(table, 1, 0);
        grid.getChildren().add(table);


        //Click on "Search" button
        search.setOnAction((ActionEvent e) ->{

            data.clear();
            try {
                List<Drug> drugs = driver.findDrugByName(name.getText());
                for (Drug d : drugs){
                    data.add(new Pharmacy(d.getName(), "123", false));
                }
                //data.add(new Pharmacy("Название", "Цена", false));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            //data.add(new Pharmacy("Название", "Цена", false));

        });



        //Click on "Add" button
        addButton.setOnAction((ActionEvent e) ->{
            for (Pharmacy pharmacy : data){
                if (pharmacy.getChecked() == true){

                    System.out.println("Добавлено");
                }
            }
        });

        Scene scene = new Scene(grid, width, height);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Creating the Table
    private TableView table = new TableView();

    //Storage for Pharmacies and Prices
    private final ObservableList<Pharmacy> data = FXCollections.observableArrayList();

    //Width of the window
    private int width = 800;

    //Height of the window
    private int height = 500;

    public void start(Stage primaryStage){
        table.setEditable(true);

        primaryStage.setTitle("Поиск лекарства");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);

        //Defining the Name text field
        final TextField name = new TextField();
        name.setPromptText("Название");
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);


        //Defining the Search button
        Button search = new Button("Найти");
        GridPane.setConstraints(search, 0, 1);
        grid.getChildren().add(search);

        //Defining the Add button (Добавляет в закладки выделенное)
        Button addButton = new Button("Добавить в закладки");
        GridPane.setConstraints(addButton, 1, 1);
        grid.getChildren().add(addButton);


        //Create Name column
        TableColumn nameCol = new TableColumn("Название аптеки");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        //Create Cost column
        TableColumn costCol = new TableColumn("Цена");
        costCol.setCellValueFactory(
                new PropertyValueFactory<>("cost"));

        //Create Check column
        TableColumn checkedCol = new TableColumn("В закладки");
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("checked"));
        checkedCol.setCellFactory(param -> new CheckBoxTableCell<Pharmacy, Boolean>());



        table.setItems(data);
        table.getColumns().addAll(nameCol, costCol, checkedCol);
        System.out.println(table.getColumns().size());
        table.setMinWidth(width * 0.7);
        table.setMinHeight(height * 0.8);
        costCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        nameCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        checkedCol.setMinWidth(table.getMinWidth() / 5);
        GridPane.setConstraints(table, 1, 0);
        grid.getChildren().add(table);


        //Click on "Search" button
        search.setOnAction((ActionEvent e) ->{
            data.clear();
            //TODO: найти и вывести по названию
            data.add(new Pharmacy("Название", "Цена", false));
        });



        //Click on "Add" button
        addButton.setOnAction((ActionEvent e) ->{
            for (Pharmacy pharmacy : data){
                if (pharmacy.getChecked() == true){

                    //TODO: добавить в закладки
                    System.out.println("Добавлено");
                }
            }
        });

        Scene scene = new Scene(grid, width, height);

        primaryStage.setScene(scene);
        primaryStage.show();
    }





    public static class Pharmacy{
        private final SimpleStringProperty name;
        private final SimpleStringProperty cost;
        private final SimpleBooleanProperty checked;
        Pharmacy(String name, String cost, Boolean checked){
            this.name = new SimpleStringProperty(name);
            this.cost = new SimpleStringProperty(cost);
            this.checked = new SimpleBooleanProperty(checked);
        }

        public SimpleBooleanProperty checkedProperty() {
            return this.checked;
        }

        public Boolean getChecked() {
            return this.checkedProperty().get();
        }

        public void setChecked(final Boolean checked) {
            this.checkedProperty().set(checked);
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
}

//class CheckBoxCellFactory implements Callback {
//    @Override
//    public TableCell call(Object param) {
//        CheckBoxTableCell<SearchView.Pharmacy,Boolean> checkBoxCell = new CheckBoxTableCell();
//        return checkBoxCell;
//    }
//}