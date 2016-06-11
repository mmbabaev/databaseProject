package gui;

import javafx.event.EventHandler;
import jfx.messagebox.MessageBox;
import model.entities.Drug;
import model.entities.DrugInStore;
import model.sql.SqlDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class SearchView {

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    Stage primaryStage;
    SqlDriver driver;

    Drug currentDrug;

    public SearchView(SqlDriver driver, String userId){
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

        //Defining isInterested checkbox
        CheckBox checkBox = new CheckBox("Следить за ценой");
        GridPane.setConstraints(checkBox, 1, 1);
        grid.getChildren().add(checkBox);
        checkBox.setDisable(true);
        checkBox.setText("Лекарство не выбрано!");

        //Create Name column
        TableColumn nameCol = new TableColumn("Название аптеки");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        //Create Cost column
        TableColumn costCol = new TableColumn("Цена");
        costCol.setCellValueFactory(
                new PropertyValueFactory<>("cost"));



        table.setItems(data);
        table.getColumns().addAll(nameCol, costCol);
        System.out.println(table.getColumns().size());
        table.setMinWidth(width * 0.7);
        table.setMinHeight(height * 0.8);
        costCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        nameCol.setMinWidth((table.getMinWidth() - table.getMinWidth() / 5) / 2);
        GridPane.setConstraints(table, 1, 0);
        grid.getChildren().add(table);

        //Click on "Search" button
        search.setOnAction((ActionEvent e) -> {
            System.out.println("Search clicked");

            data.clear();

            // костыль, лень придумать как-то получше
            EventHandler<ActionEvent> event = checkBox.getOnAction();

            try {
                currentDrug = driver.findDrugByName(name.getText()).get(0);
                boolean isInterested = driver.isDrugInterestingForUser(currentDrug.getId(), userId);

                List<DrugInStore> drugs = driver.findDrugPrices(name.getText());
                for (DrugInStore d : drugs) {
                    System.out.println(d.getName());
                    data.add(d);
                }

                checkBox.setOnAction(null);
                checkBox.setSelected(isInterested);
                checkBox.setText("Добавить " + currentDrug.getName() + " в закладки");
                checkBox.setDisable(false);
            }
            catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                checkBox.setDisable(true);
                checkBox.setText("Лекарство не выбрано!");
                showError(primaryStage, "Произошла ошибка поиска!");
            }

            checkBox.setOnAction(event);
        });


        // Check box:
        checkBox.setOnAction((ActionEvent event) -> {
            System.out.println("check box clicked" + " isSelected = " + checkBox.isSelected());

            try {
                if (checkBox.isSelected()) {
                    System.out.println("add");
                    driver.addDrugInterest(userId, currentDrug.getId());
                }
                else {
                    System.out.println("delete");
                    driver.deleteFromInterest(userId, currentDrug.getId());
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
                showError(primaryStage, "Ошибка добавления или удаления закладки!");
            }
        });

        Scene scene = new Scene(grid, width, height);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Creating the Table
    private TableView table = new TableView();

    //Storage for Pharmacies and Prices
    private final ObservableList<DrugInStore> data = FXCollections.observableArrayList();

    //Width of the window
    private int width = 800;

    //Height of the window
    private int height = 500;

    private void showError(Stage stage, String message) {
        MessageBox.show(stage,
                message,
                "Ошибка",
                MessageBox.ICON_ERROR);
    }
}
