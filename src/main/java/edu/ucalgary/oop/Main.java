package edu.ucalgary.oop;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.connect() != null){
            System.out.println("Connection Successfull");
        }else{
            System.out.println("Connection Failed");
        }

        db.disconnect();
    }
}