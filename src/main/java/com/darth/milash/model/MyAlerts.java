package com.darth.milash.model;


import com.darth.milash.MainApp;
import javafx.scene.control.Alert;

public class MyAlerts {

    public static void formatDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Incorrect Date");
        alert.setHeaderText("Date is incorrect");
        alert.setContentText("No valid date. Use the format dd.MM.yyyy HH:mm:ss");
        alert.showAndWait();
    }


    public static void timeDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Time Error");
        alert.setHeaderText("Date timing error");
        alert.setContentText("The start date must be earlier than end date");
        alert.showAndWait();
    }

    public static void chooseAlert(MainApp mainApp) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Task Selected");
        alert.setContentText("Please select a task in the table.");
        alert.showAndWait();
    }

}
