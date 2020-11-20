package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.model.Task;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ListController {

    @FXML
    private ImageView listRefreshButton;

    @FXML
    private ListView<Task> listTask;

    @FXML
    private TextField listTaskField;

    @FXML
    private TextArea listDescriptionField;

    @FXML
    private Button listSaveTaskButton;

    @FXML
    private ObservableList<Task> tasks;
    private ObservableList<Task> refreshedTasks;

    @FXML
    private DatabaseHandler databaseHandler;


    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

        tasks = FXCollections.observableArrayList();

        databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getTasksByUser(AddItemController.userId);

        while (resultSet.next()) {
            Task task = new Task();
            task.setTaskId(resultSet.getInt("taskid"));
            task.setTask(resultSet.getString("task"));
            task.setDatecreated(resultSet.getTimestamp("datecreated"));
            task.setDescription(resultSet.getString("description"));

            tasks.addAll(task);
        }

        listTask.setItems(tasks);
        listTask.setCellFactory(CellController -> new CellController());

        listRefreshButton.setOnMouseClicked(event -> {
            try {
                refreshList();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            listRefreshButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/list.fxml"));

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

        listSaveTaskButton.setOnAction(event -> {
            try {
                addNewTask();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

    }


    public void addNewTask() throws SQLException, ClassNotFoundException {

        if (!listSaveTaskButton.getText().equals("") || !listDescriptionField.getText().equals("")) {

            Task myNewTask = new Task();

            Calendar calendar = Calendar.getInstance();

            Timestamp timestamp = new Timestamp(calendar.getInstance().getTimeInMillis());

            myNewTask.setUserId(AddItemController.userId);
            myNewTask.setTask(listTaskField.getText().trim());
            myNewTask.setDescription(listDescriptionField.getText().trim());
            myNewTask.setDatecreated(timestamp);

            databaseHandler.insertTask(myNewTask);

            listTaskField.setText("");
            listDescriptionField.setText("");

            try {
                initialize();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public void refreshList() throws SQLException, ClassNotFoundException {

        System.out.println("Refresh List called");

        refreshedTasks = FXCollections.observableArrayList();

        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getTasksByUser(AddItemController.userId);

        while (resultSet.next()){
            Task task = new Task();
            task.setTaskId(resultSet.getInt("taskid"));
            task.setTask(resultSet.getString("task"));
            task.setDatecreated(resultSet.getTimestamp("datecreated"));
            task.setDescription(resultSet.getString("description"));

            refreshedTasks.addAll(task);
        }

        listTask.setItems(tasks);
        listTask.setCellFactory(CellController -> new CellController());

    }








}



