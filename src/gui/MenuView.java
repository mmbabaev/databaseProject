package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import model.entities.Drug;
import model.entities.User;
import model.sql.SqlDriver;

import java.util.List;

public class MenuView extends Application {

    User user;

    public MenuView(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {

        SqlDriver driver = SqlDriver.getInstance();

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(1);
        grid.setHgap(2);

        Button favourites = new Button("Закладки");
        GridPane.setConstraints(favourites, 0, 0);
        grid.getChildren().add(favourites);

        Button search = new Button("Поиск лекарств");
        GridPane.setConstraints(search, 0, 1);
        grid.getChildren().add(search);


        favourites.setOnAction((ActionEvent e) -> {
            try {
                List<Drug> drugs = driver.findFavouriteDrugs(user);

                ChartView cv = new ChartView(drugs);
                cv.start(primaryStage);
            }
            catch (Exception ex) {
                showError(primaryStage, "Не удалось открыть закладки");
            }

        });

        search.setOnAction((ActionEvent e) -> {
            SearchView s = new SearchView(driver, user.getStringId());
        });

        primaryStage.setTitle("");
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
    }

    private void showError(Stage stage, String message) {
        MessageBox.show(stage,
                message,
                "Ошибка",
                MessageBox.ICON_ERROR);
    }
}
