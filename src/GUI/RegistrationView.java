package GUI;

import Model.Entities.User;
import Model.SqlDriver;
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
    public static void main(String[] args) {
        launch(args);
    }

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
        firstName.setPromptText("Enter your first name.");
        GridPane.setConstraints(firstName, 0, 0);
        grid.getChildren().add(firstName);

        final TextField lastName = new TextField();
        lastName.setPromptText("Enter your last name.");
        GridPane.setConstraints(lastName, 0, 1);
        grid.getChildren().add(lastName);

        final TextField login = new TextField();
        login.setPromptText("Enter your login.");
        GridPane.setConstraints(login, 0, 2);
        grid.getChildren().add(login);

        //Defining the Comment text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");
        GridPane.setConstraints(password, 0, 3);
        grid.getChildren().add(password);

        //Defining the Submit button
        Button submit = new Button("Register");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

//Defining the Clear button
        Button clear = new Button("Clear");
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

                    //todo: идем на другой жкран
                    System.out.println("Все ок");
                }
                catch (Exception ex) {
                    MessageBox.show(primaryStage,
                            "При регистрации произошла ошибка",
                            "Ошибка",
                            MessageBox.ICON_ERROR);
                }
            }
            else {
                MessageBox.show(primaryStage,
                        "Заполните все поля!",
                        "Ошибка",
                        MessageBox.ICON_ERROR);
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
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
        clear.requestFocus();
    }


}
