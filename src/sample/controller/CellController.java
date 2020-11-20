package sample.controller;


import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.model.Task;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CellController extends JFXListCell<Task> {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView iconImageView;

    @FXML
    private Label taskLabel;

    @FXML
    private Label descrptionLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView deleteButton;

    @FXML
    private ImageView listUpdateButton;

    @FXML
    private FXMLLoader fxmlLoader;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() {


    }

    @Override
    public void updateItem(Task myTask, boolean empty) {
        super.updateItem(myTask, empty);

        if(empty || myTask == null){
            setText(null);
            setGraphic(null);
        }else{

            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            taskLabel.setText(myTask.getTask());
            dateLabel.setText(myTask.getDatecreated().toString());
            descrptionLabel.setText(myTask.getDescription());

            int taskId = myTask.getTaskId();


            listUpdateButton.setOnMouseClicked(event -> {

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/sample/view/updateTaskForm.fxml"));


                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Parent root = loader.getRoot();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));

                        UpdateTaskController updateTaskController = loader.getController();
                        updateTaskController.setTaskField(myTask.getTask());
                        updateTaskController.setUpdateDescriptionField(myTask.getDescription());

                        updateTaskController.updateTaskButton.setOnAction(event1 -> {

                            databaseHandler = new DatabaseHandler();
                            Calendar calendar = Calendar.getInstance();

                            java.sql.Timestamp timestamp =
                                    new java.sql.Timestamp(calendar.getTimeInMillis());

                            try {

                                System.out.println("taskid " + myTask.getTaskId());

                                databaseHandler.updateTask(timestamp, updateTaskController.getDescription(),
                                        updateTaskController.getTask(), myTask.getTaskId());

                                //update our listController
                                // updateTaskController.refreshList();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }


                            FXMLLoader loader2 = new FXMLLoader();
                            loader2.setLocation(getClass().getResource("/sample/view/list.fxml"));

                            try{
                                loader2.load();
                            }catch(IOException e){
                                e.printStackTrace();
                            }

                            Parent parent = loader2.getRoot();
                            Stage stage2 = new Stage();
                            stage2.setScene(new Scene(parent));
                            stage2.showAndWait();

                        });

                        stage.show();

                    });


            //Remove a todo from the list
            deleteButton.setOnMouseClicked(event -> {
                databaseHandler = new DatabaseHandler();

                try {
                    databaseHandler.deleteTask(AddItemController.userId, taskId);
                    System.out.println("Deleted!");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                getListView().getItems().remove(getItem());

            });


            setText(null);
            setGraphic(rootAnchorPane);
        }
    }
}


