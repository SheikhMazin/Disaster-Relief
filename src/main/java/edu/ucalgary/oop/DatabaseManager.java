package edu.ucalgary.oop;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private String url;
    private String username;
    private String password;
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager(String url, String username, String password){
        this.url = url;
        this.username =username;
        this.password = password;
    }

    public static DatabaseManager getInstance(String url, String username, String password) {
        if (instance == null){
            instance = new DatabaseManager(url, username, password);
        }
        return instance;
    }

    public Connection connect(){
       try{
           this.connection = DriverManager.getConnection(this.url, this.username, this.password);

       }catch (SQLException e){
          System.err.println("Connection failed");
          e.printStackTrace();
       }
       return this.connection;
    }

    public void disconnect(){
        try{
            if (this.connection != null){
                this.connection.close();
            }
        }catch (SQLException e){
            System.err.println("Disconnect failed");
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.connection;
    }



}