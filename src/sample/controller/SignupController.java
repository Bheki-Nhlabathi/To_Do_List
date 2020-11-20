package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signupLastName;

    @FXML
    private TextField signupUsername;

    @FXML
    private TextField signupLocation;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private CheckBox signupCheckBoxMale;

    @FXML
    private CheckBox signupCheckBoxFemale;

    @FXML
    private Button signUpButton;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize(){

        databaseHandler = new DatabaseHandler();

        signUpButton.setOnAction(event -> {

            String name = signUpFirstName.getText();
            String lastName = signupLastName.getText();
            String userName = signupUsername.getText();
            String password = signupPassword.getText();
            String location = signupLocation.getText();

            String gender;
            if(signupCheckBoxFemale.isSelected()){
                gender = "Female";
            }else{
                gender = "male";
            }

            User user = new User(name, lastName, userName, password, location, gender);

            if (name.equals(signUpFirstName.getText())){

                try {
                    databaseHandler.signUpUser(user);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("New user added successfully");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sample/view/login.fxml"));

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();

            }else{
                System.out.println("Complete all fields");
            }

        });

    }

}
