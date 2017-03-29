package com.darth.milash.controller;

import com.darth.milash.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CalendarController {

    private TaskList list;
    private Stage dialogStage;


    @FXML
    private TableView<Map.Entry<Date, Set<Task>>> calendar;

    @FXML
    private TableColumn<Map.Entry<Date, Set<Task>>, String> dateColumn;

    @FXML
    private TableColumn<Map.Entry<Date, Set<Task>>, String> tasksColumn;

    @FXML
    public void initialize() throws IOException, ParseException {
        list = new ArrayTaskList();
        String fileName = "files/tFile.txt";
        TaskIO.readText(list, new File(fileName));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return false;
    }

    @FXML
    public void handleOk() {
        dialogStage.close();
    }

    public void calendar() {
        String formatDate = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        Date sdate = new Date();
        Date edate = new Date(sdate.getTime() + (66400000));
        Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(sdf.format(param.getValue().getKey())));
        tasksColumn.setCellValueFactory(param -> new SimpleStringProperty(taskSet(param.getValue().getValue()).toString2()));
        ObservableList<Map.Entry<Date, Set<Task>>> mapList = FXCollections.observableArrayList(map.entrySet());
        calendar.setItems(mapList);

    }

    public ArrayTaskList taskSet(Set<Task> set) {
        Set<Task> set1 = new HashSet<>(set);
        ArrayTaskList list = new ArrayTaskList();
        for (Task task : set1) {
            list.add(task);
        }
        return list;
    }


}
