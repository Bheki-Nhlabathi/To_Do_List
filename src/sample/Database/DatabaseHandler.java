package sample.Database;

import sample.model.Task;
import sample.model.User;

import java.sql.*;

public class DatabaseHandler extends Configs{
    com.mysql.jdbc.Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = (com.mysql.jdbc.Connection) DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    //Write
    public void signUpUser(User user) throws SQLException, ClassNotFoundException {

        String insert = "INSERT INTO " + Const.USERS_TABLE + "(" + Const.USERS_FIRSTNAME + ","
                + Const.USERS_LASTNAME + "," + Const.USERS_USERNAME + "," + Const.USERS_PASSWORD + ","
                + Const.USERS_LOCATION + "," + Const.USERS_GENDER + ")" + "VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setString(1,user.getFirstName());
            preparedStatement.setString(2,user.getLastName());
            preparedStatement.setString(3,user.getUserName());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setString(5,user.getLocation());
            preparedStatement.setString(6,user.getGender());
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }




    public ResultSet getUser(User user){
        ResultSet resultSet = null;

        if(!user.getUserName().equals("") || !user.getPassword().equals("")){
            String query = "SELECT * FROM " + Const.USERS_TABLE + " WHERE " + Const.USERS_USERNAME + "=?"
                    + " AND " + Const.USERS_PASSWORD + "=?";

            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());

                resultSet = preparedStatement.executeQuery();


            }catch (SQLException e){
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Please enter your credentials...");
        }
        return resultSet;
    }




    public ResultSet getTasksByUser(int userId) throws SQLException, ClassNotFoundException {

        ResultSet resultTasks = null;
        String query = "SELECT * FROM " + Const.TASKS_TABLE + " WHERE " + Const.USERS_ID + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

        preparedStatement.setInt(1, userId);
        resultTasks = preparedStatement.executeQuery();

        return resultTasks;
    }



    public int getAllTasks(int userId) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM "
                + Const.TASKS_TABLE + " WHERE " + Const.USERS_ID + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            return resultSet.getInt(1);
        }

        return 0;
    }




    public void insertTask(Task task){
        String insert = "INSERT INTO " + Const.TASKS_TABLE + "("  + Const.USERS_ID + "," + Const.TASKS_DATE + ","
                + Const.TASKS_DESCRIPTION + "," + Const.TASKS_TASK + ")" + "VALUES(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setInt(1,task.getUserId());
            preparedStatement.setTimestamp(2,task.getDatecreated() );
            preparedStatement.setString(3,task.getDescription());
            preparedStatement.setString(4,task.getTask());

            preparedStatement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }



    public void deleteTask(int userid, int taskId) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM " + Const.TASKS_TABLE + " WHERE " +
                Const.USERS_ID + "=?" + " AND " + Const.TASKS_ID + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, userid);
        preparedStatement.setInt(2, taskId);
        preparedStatement.execute();
        preparedStatement.close();

    }


    public void updateTask(Timestamp datecreated, String description, String task, int taskId) throws SQLException, ClassNotFoundException {

        String query = "UPDATE tasks SET datecreated=?, description=?, task=? WHERE taskid=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setTimestamp(1, datecreated);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, task);
        // preparedStatement.setInt(4, userId);
        preparedStatement.setInt(4, taskId);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }
















}
