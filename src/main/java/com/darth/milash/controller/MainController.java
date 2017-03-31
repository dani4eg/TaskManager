package com.darth.milash.controller;

import com.darth.milash.model.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.darth.milash.MainApp;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressFBWarnings(value="NN_NAKED_NOTIFY", justification="Notify is not naked")
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static String fileName = "files/tFile.txt";
    private TaskList list;
    static final Object MONITOR = new Object();
    static boolean edit = false;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private Label title;
    @FXML
    private Label start;
    @FXML
    private Label end;
    @FXML
    private Label interval;
    @FXML
    private Label active;

    private MainApp mainApp;

    @FXML
    public void initialize() throws FileNotFoundException, ParseException {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        showTaskDetails(null);

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

        list = new ArrayTaskList();
        TaskIO.readText(list, new File(fileName));
        myThread.setDaemon(true);
        myThread.start();

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        taskTable.setItems(mainApp.getTaskData());
    }

    public void showTaskDetails(Task task) {
        String formatDate = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        if (task==null) {
            title.setText("");
            start.setText("");
            end.setText("");
            interval.setText("");
            active.setText("");
        }
        else if (task.getInterval()==0){
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getTime()));
            end.setText("");
            interval.setText("");
            if (task.isActive()) active.setText("YES");
            else
            active.setText("NO");
        }
        else {
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getStartTime()));
            end.setText(sdf.format(task.getEndTime()));
            interval.setText(task.reInterval(task.getInterval()));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }


    @FXML
    public void deleteTask() throws FileNotFoundException, ParseException {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            taskTable.getItems().remove(selectedIndex);
            list.remove(list.getTask(selectedIndex));
            TaskIO.writeText(list, new File(fileName));
            synchronized (MONITOR) {
                MONITOR.notifyAll();
            }
        }
        else {
            MyAlerts.chooseAlert(mainApp);
        }
    }

    @FXML
    public void handleNewTask() {
        Task tempTask = new Task("", new Date());
        boolean okClicked = mainApp.showTaskEditDialog(tempTask);
        if (okClicked) {
            mainApp.getTaskData().add(tempTask);
            tempTask.setTitle(tempTask.getTitle());
            tempTask.setStart(tempTask.getStartTime());
            tempTask.setEnd(tempTask.getEndTime());
            tempTask.setInterval(tempTask.getInterval());
            tempTask.setActive(tempTask.isActive());
            list.add(tempTask);
            TaskIO.writeText(list, new File(fileName));
            synchronized (MONITOR) {
                MONITOR.notifyAll();
            }
        }
    }

    @FXML
    public void handleEditTask() {
        Task selectedPerson = taskTable.getSelectionModel().getSelectedItem();
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedPerson == null) {
            MyAlerts.chooseAlert(mainApp);
        } else {
            boolean okClicked = mainApp.showTaskEditDialog(selectedPerson);
            if (okClicked) {
                showTaskDetails(selectedPerson);
            }
            list.getTask(selectedIndex).setTitle(selectedPerson.getTitle());
            list.getTask(selectedIndex).setStart(selectedPerson.getStartTime());
            list.getTask(selectedIndex).setEnd(selectedPerson.getEndTime());
            list.getTask(selectedIndex).setInterval(selectedPerson.getInterval());
            list.getTask(selectedIndex).setActive(selectedPerson.isActive());
            taskTable.getItems().set(selectedIndex, selectedPerson);
            TaskIO.writeText(list, new File(fileName));
            synchronized (MONITOR) {
                MONITOR.notifyAll();
            }

        }
    }

    @FXML
    public void handleCloneTask() {
        Task selectedPerson = taskTable.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            MyAlerts.chooseAlert(mainApp);
        } else {
            Task tempTask = new Task("", new Date());
            mainApp.getTaskData().add(tempTask);
            tempTask.setTitle(selectedPerson.getTitle()+"_copy");
            tempTask.setStart(selectedPerson.getStartTime());
            tempTask.setEnd(selectedPerson.getEndTime());
            tempTask.setInterval(selectedPerson.getInterval());
            tempTask.setActive(selectedPerson.isActive());
            list.add(tempTask);
            TaskIO.writeText(list, new File(fileName));
            synchronized (MONITOR) {
                MONITOR.notifyAll();
            }
        }
    }

    @FXML
    public void handleCalendar() {
        mainApp.showCalendarWindow();
            }


    Thread myThread = new Thread(new Runnable() {
        @SuppressFBWarnings(value="UW_UNCOND_WAIT", justification="Not unconditional wait")
        @Override
        public void run() {
            while (true) {
                long waitmills;
                Date sdate = new Date();
                Date edate = new Date(sdate.getTime() + (66400000));
                Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);
                if (map.isEmpty()) {
                    try {
                        synchronized (MONITOR) {
                            MONITOR.wait();
                        }
                    } catch (InterruptedException e1) {
                        LOGGER.error("Error InterruptedException");
                    }
                }
                for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                    int size = list.size();
                    waitmills = pair.getKey().getTime() - (sdate.getTime());
                    sdate = pair.getKey();
                    edit = false;
                    try {
                        synchronized (MONITOR) {
                            MONITOR.wait(waitmills);
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("Error InterruptedException");
                    }
                    if (size == list.size() && !edit) {
                        for (Task task : pair.getValue()) {
                            Platform.runLater(() -> {
                                mainApp.showAlarmWindow(pair.getKey(), task.getTitle());
                            });
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    });
}
