package com.darth.milash.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmController {

    @FXML
    private Label alarmTask;
    @FXML
    private Label alarmDate;

    private Stage dialogStage;

    public boolean isOkClicked() {
        return false;
    }

    @FXML
    public void handleOk() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void alarmTask(Date date, String task) {
        String formatDate = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        alarmDate.setText(sdf.format(date));
        alarmTask.setText(task);
    }
}
