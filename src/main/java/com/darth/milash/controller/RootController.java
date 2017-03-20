package com.darth.milash.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RootController {

    @FXML
    public void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Milash Bohdan\n2017");

        alert.showAndWait();
    }

    /**
     * Закрывает приложение.
     */
    @FXML
    public void handleExit() {
        System.exit(0);
    }
}