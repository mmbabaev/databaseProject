package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.entities.User;
import model.sql.SqlDriver;

public class StartView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(1);
        grid.setHgap(2);

        Button login = new Button("Войти");
        GridPane.setConstraints(login, 0, 0);
        grid.getChildren().add(login);

        Button register = new Button("Регистрация");
        GridPane.setConstraints(register, 0, 1);
        grid.getChildren().add(register);


        login.setOnAction((ActionEvent e) -> {
            LoginView a = new LoginView();
            a.start(primaryStage);
        });

        register.setOnAction((ActionEvent e) -> {
            RegistrationView a = new RegistrationView();
            a.start(primaryStage);
        });

        primaryStage.setTitle("");
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
        
        
        // CREATE ALL NEEDED TABLES and fill'em
        
        SqlDriver driver = null;

        try {
            driver = SqlDriver.getInstance();
            
            driver.dropAll();
        }
        catch (Exception ex) {
            System.out.println("Ошибка при удалении таблиц");
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
            driver.close();
        }
        
        
        
        createTables();
        fillTables();
    }
    
    private static void createTables() {
        SqlDriver driver = null;

        try {
            driver = SqlDriver.getInstance();
            
            driver.createDrug();
            driver.createUsers();
            driver.createDrugstore();
            driver.createInterestedDrug();
            driver.createPriceChange();
            driver.createDrugInStore();
        }
        catch (Exception ex) {
            System.out.println("Ошибка при создании таблиц");
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
            driver.close();
        }
    }
    
    private static void fillTables() {
        SqlDriver d = null;

        try {
            d = SqlDriver.getInstance();
            
            d.addDrug("Arbidol");   // 1
            d.addDrug("Morpheum");  // 2
            
            d.addUser("Volodya", "Putin", "P", "1");        // 1
            d.addUser("Ilya", "Murometz", "bogatyr", "1");  // 2
            
            d.addDrugstore("SamsonFarma");  // 1
            d.addDrugstore("Rigla");        // 2
            
            d.addInterestedDrug(1, 1);   // 1
            d.addInterestedDrug(2, 2);   // 2
            
            d.addPriceChange(1, "2016-05-01 12:00:00", 100); //, 1);   // 1
            d.addPriceChange(1, "2016-05-05 12:00:00", 90); //, 2);    // 2
            d.addPriceChange(1, "2016-05-09 12:00:00", 110); //, 2);   // 3
            
            d.addPriceChange(2, "2016-05-03 12:00:00", 50); //, 1);    // 4
            d.addPriceChange(2, "2016-05-07 12:00:00", 55); //, 2);    // 5
            d.addPriceChange(2, "2016-05-15 12:00:00", 60); //, 1);    // 6
            
            d.addDrugInStore(1, 120, 1);    // 1
            d.addDrugInStore(1, 110, 2);    // 2
            
            d.addDrugInStore(2, 60, 1);     // 3
            d.addDrugInStore(2, 70, 2);     // 4

        }
        catch (Exception ex) {
            System.out.println("Ошибка при генерации тестовых данных");
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
            d.close();
        }
    }
}
