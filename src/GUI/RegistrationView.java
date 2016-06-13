package gui;

import model.entities.User;
import model.sql.RegistrationException;
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

public class RegistrationView extends Application {

    @Override
    public void start(Stage primaryStage) {
        SqlDriver driver = SqlDriver.getInstance();

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Name text field
        final TextField firstName = new TextField();
        firstName.setPromptText("Имя");
        GridPane.setConstraints(firstName, 0, 0);
        grid.getChildren().add(firstName);

        final TextField lastName = new TextField();
        lastName.setPromptText("Фамилия");
        GridPane.setConstraints(lastName, 0, 1);
        grid.getChildren().add(lastName);

        final TextField login = new TextField();
        login.setPromptText("Login");
        GridPane.setConstraints(login, 0, 2);
        grid.getChildren().add(login);

        //Defining the Comment text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Пароль");
        GridPane.setConstraints(password, 0, 3);
        grid.getChildren().add(password);

        //Defining the Submit button
        Button submit = new Button("Зарегистрироваться");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        //Defining the Clear button
        Button clear = new Button("Очистить");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);


        submit.setOnAction((ActionEvent e) -> {
            if (    firstName.getText() != null && !firstName.getText().isEmpty() &&
                    lastName.getText() != null && !lastName.getText().isEmpty() &&
                    login.getText() != null && !login.getText().isEmpty() &&
                    password.getText() != null && !password.getText().isEmpty() )
            {
                User user = new User(firstName.getText(), lastName.getText(), login.getText(), password.getText());
                try {
                    driver.registerUser(user);
                    MenuView mv = new MenuView(user);
                    mv.start(primaryStage);

                    System.out.println("Все ок");
                }
                catch (RegistrationException ex) {
                    showError(primaryStage, "Пользователь с таким именем уже существует!");
                }
                catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                    showError(primaryStage, "При регистрации произошла ошибка");
                }
            }
            else {
                showError(primaryStage, "Заполните все поля!");
            }
        });

        clear.setOnAction((ActionEvent e) -> {
            firstName.clear();
            lastName.clear();
            login.clear();
            password.clear();
        });

        primaryStage.setTitle("Регистрация");
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 350, 250));
        primaryStage.show();
        clear.requestFocus();
    }


    private void showError(Stage stage, String message) {
        MessageBox.show(stage,
                message,
                "Ошибка",
                MessageBox.ICON_ERROR);
    }
}
