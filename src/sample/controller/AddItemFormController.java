package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.model.Task;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.ResourceBundle;


public class AddItemFormController {

    @FXML
    private int userId;

    @FXML
    private DatabaseHandler databaseHandler;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField taskField;

    @FXML
    private Button saveTaskButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button todosButton;

    @FXML
    private Label successLabel;

    @FXML
    void initialize() {

        databaseHandler = new DatabaseHandler();

        saveTaskButton.setOnAction(event -> {
            Task task = new Task();

            Calendar calendar = Calendar.getInstance();

            Timestamp timestamp = new Timestamp(calendar.getInstance().getTimeInMillis());

            String taskText = taskField.getText().trim();
            String taskDescription = descriptionField.getText().trim();

           if(!taskText.equals("") || !taskDescription.equals("")){

               System.out.println("User Id " + AddItemController.userId);

                task.setUserId(AddItemController.userId);
                task.setDatecreated(timestamp);
                task.setDescription(taskDescription);
                task.setTask(taskText);
                databaseHandler.insertTask(task);
               System.out.println("Task added successfully!");

               successLabel.setVisible(true);
               todosButton.setVisible(true);

               int taskNumber = 0;
               try {
                   taskNumber = databaseHandler.getAllTasks(AddItemController.userId);
               } catch (SQLException e) {
                   e.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }

               todosButton.setText("My To-do's: " + "("+ taskNumber +")");
               taskField.setText("");
               descriptionField.setText("");

               //take by 4 = 9..so if there is number one, considering all the values.maximum



               todosButton.setOnAction(event1 -> {
                   //send users to the list screen

                   FXMLLoader loader = new FXMLLoader();
                   loader.setLocation(getClass().getResource("/sample/view/list.fxml"));

                   try {
                       loader.load();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   Parent root = loader.getRoot();
                   Stage stage = new Stage();
                   stage.setScene(new Scene(root));
                   stage.showAndWait();

               });

            }else{
                System.out.println("Nothing happened!");
            }

        });
    }


    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println(this.userId);

    }

}
