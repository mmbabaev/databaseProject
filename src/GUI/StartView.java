package GUI;

import Model.Entities.User;
import Model.sql.RegistrationException;
import Model.sql.SqlDriver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;

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
    }
}
