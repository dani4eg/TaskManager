package com.darth.milash;


import com.darth.milash.controller.AlarmController;
import com.darth.milash.controller.CalendarController;
import com.darth.milash.controller.EditController;
import com.darth.milash.controller.MainController;
import com.darth.milash.model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class MainApp extends Application {

    public Logger logger = LoggerFactory.getLogger(ArrayTaskList.class);
    private static TaskList list = new ArrayTaskList();
    private Stage primaryStage;
    private BorderPane rootLayout;
    private final ObservableList<Task> taskData = FXCollections.observableArrayList();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TaskMan");

        this.primaryStage.getIcons().add(new Image("/images/icon.png"));

        initRootWindowt();
        initTaskWindow();
    }

    public void initRootWindowt() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/RootWindow.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("RootWindow data access error");
        }
    }

    public void initTaskWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/TaskWindow.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOverview);
            MainController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            logger.error("TaskWindow data access error");
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public ObservableList<Task> getTaskData() {
        return taskData;
    }

    public MainApp() throws FileNotFoundException, ParseException {
        String fileName = "files/tFile.txt";
        TaskIO.read(list, new FileReader(fileName));
        for (int i = 0; i < list.size(); i++) {
            taskData.add(list.getTask(i));
        }
    }

    public boolean showTaskEditDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/TaskEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTask(task);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            logger.error("TaskEditDialog data access error");
            return false;
        }
    }

    public boolean showCalendarWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/CalendarWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Calendar");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CalendarController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.calendar();
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            logger.error("CalendarWindow data access error");
            return false;
        }
    }

    public boolean showAlarmWindow(Date date, String task) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/AlarmWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("DING DONG");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            AlarmController alarmController = loader.getController();
            alarmController.setDialogStage(dialogStage);
            alarmController.alarmTask(date, task);
            dialogStage.showAndWait();

            return alarmController.isOkClicked();
        } catch (IOException e) {
            logger.error("AlarmWindow data access error");
            return false;
        }
    }


}