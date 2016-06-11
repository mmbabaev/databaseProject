package gui;

import model.entities.User;
import model.sql.SqlDriver;
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

public class LoginView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SqlDriver driver = SqlDriver.getInstance();

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(3);
        grid.setHgap(1);

        final TextField loginField = new TextField();
        loginField.setPromptText("Login");
        GridPane.setConstraints(loginField, 0, 0);
        grid.getChildren().add(loginField);

        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");
        GridPane.setConstraints(passwordField, 0, 1);
        grid.getChildren().add(passwordField);

        Button submit = new Button("Войти");
        GridPane.setConstraints(submit, 0, 2);
        grid.getChildren().add(submit);


        submit.setOnAction((ActionEvent e) -> {

            String login = loginField.getText();
            String password = passwordField.getText();

            if (login != null && password != null &&
                    !login.isEmpty() && !password.isEmpty()) {
                User user = driver.authorize(login, password);
                if (user == null) {
                    showError(primaryStage, "При авторизации произошла ошибка");
                } else {
                    try {
                        MenuView mv = new MenuView(user);
                        mv.start(primaryStage);
                    }
                    catch (Exception ex) {
                        showError(primaryStage, "Ошибка регистрации");
                    }
                }
            }
            else {
                showError(primaryStage, "Заполните все поля!");
            }
        });

        primaryStage.setTitle("Регистрация");
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 200, 150));
        primaryStage.show();
        submit.requestFocus();
    }


    private void showError(Stage stage, String message) {
        MessageBox.show(stage,
                message,
                "Ошибка",
                MessageBox.ICON_ERROR);
    }
}
