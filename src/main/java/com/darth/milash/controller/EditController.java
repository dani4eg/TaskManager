package com.darth.milash.controller;

import com.darth.milash.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class EditController {

    @FXML
    private TextField title;
    @FXML
    private TextField start;
    @FXML
    private TextField end;
    @FXML
    private TextField interval;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private CheckBox checker;
    @FXML
    private RadioButton chYes;
    @FXML
    private RadioButton chNo;

    private Stage dialogStage;
    private Task task;
    private boolean okClicked = false;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTask(Task task) {
        this.task = task;
        Date startDate = task.getTime();
        Date endDate = task.getEndTime();
        LocalDate localStart = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEnd = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formatDate = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        title.setText(task.getTitle());
        startPicker.setValue(localStart);
        start.setText(sdf.format(task.getTime()));
        if (task.getInterval() == 0) {
            checker.setSelected(false);
            endPicker.setVisible(false);
            end.setVisible(false);
            interval.setVisible(false);
        } else {
            checker.setSelected(true);
            endPicker.setVisible(true);
            end.setVisible(true);
            interval.setVisible(true);
            endPicker.setValue(localEnd);
            end.setText(sdf.format(task.getEndTime()));
            interval.setText(Integer.toString(task.getInterval()/1000));
        }
        if (task.isActive()) {
            chYes.setSelected(true);
            chNo.setSelected(false);
        }
        else {
            chYes.setSelected(false);
            chNo.setSelected(true);
        }
    }

    public void ifSelected() {
        if (checker.isSelected()) {
            endPicker.setVisible(true);
            end.setVisible(true);
            interval.setVisible(true);
        }
        else {
            endPicker.setVisible(false);
            end.setVisible(false);
            interval.setVisible(false);
        }
    }

    public void isYes() {
        if (chYes.isSelected()) chNo.setSelected(false);
        else chNo.setSelected(true);
    }
    public void isNo() {
        if (chNo.isSelected()) chYes.setSelected(false);
        else chYes.setSelected(true);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    public void handleOk() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        if (isInputValid()) {
            task.setTitle(title.getText());
            if (checker.isSelected()) {
                task.setInterval(Integer.parseInt(interval.getText())*1000);
                try {
                    Date startDate = sdf2.parse(startPicker.getValue() + " " + start.getText());
                    Date endDate = sdf2.parse(endPicker.getValue() + " " + end.getText());
                    if (startDate.before(endDate)) {
                        task.setStart(startDate);
                        task.setEnd(endDate);
                        dialogStage.close();
                    }
                    else MyAlerts.timeDateAlert();
                } catch (ParseException e1) {
                    MyAlerts.formatDateAlert();
                }
            }
            else {
                task.setInterval(0);
                try {
                    task.setStart(sdf2.parse(startPicker.getValue() + " " + start.getText()));
                    dialogStage.close();
                } catch (ParseException e) {
                    MyAlerts.formatDateAlert();
                }
            }
            if (chYes.isSelected()) task.setActive(true);
            else task.setActive(false);
            okClicked = true;
            MainController.edit = true;
//            synchronized (MainController.MONITOR) {
//                MainController.MONITOR.notifyAll();
//            }
        }
        else okClicked = false;
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (title.getText() == null || title.getText().length() == 0) {
            errorMessage += "No valid task name!\n";
        }
        if (start.getText() == null || start.getText().length() == 0) {
            errorMessage += "Start date is empty\n";
        }
        if (checker.isSelected() && (interval.getText() == null || interval.getText().length() == 0)) {
            errorMessage += "Repeat task must have interval";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}