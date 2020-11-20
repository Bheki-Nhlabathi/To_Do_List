package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.animations.Shaker;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private int userId;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginUsername;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button loginSignupButton;

    @FXML
    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() {
        databaseHandler = new DatabaseHandler();

        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String loginText = loginUsername.getText().trim();
            String loginPwd = loginPassword.getText().trim();

            User user = new User();
            user.setUserName(loginText);
            user.setPassword(loginPwd);


            ResultSet userRow = databaseHandler.getUser(user);

            int counter = 0;
                try {
                    while (userRow.next()){
                        counter++;
                        String name = userRow.getString("firstname");
                        userId = userRow.getInt("userid");

                        System.out.println("Welcome " + name + "!");
                    }

                    if(counter == 1){
                        showAddItemScreen();  //Add Item Controller
                    }else {
                        Shaker UsernameShaker = new Shaker(loginUsername);
                        Shaker PasswordShaker = new Shaker(loginPassword);
                        UsernameShaker.shake();
                        PasswordShaker.shake();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


        });

        loginSignupButton.setOnAction(event -> {

            //Take users to signup screenY
            loginSignupButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/signup.fxml"));

            try{
                loader.load();
            }catch(IOException e){
                e.printStackTrace();
            }

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        });

    }

    private void showAddItemScreen() {
        loginButton.setOnAction(event -> {

            //Take users to signup screen
            loginButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/addItem.fxml"));

            try{
                loader.load();
            }catch(IOException e){
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            AddItemController addItemController = loader.getController();
            addItemController.setUserId(userId);

            stage.showAndWait();
        });
    }


}
