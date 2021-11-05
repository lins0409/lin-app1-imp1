/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Sue Lin
 */
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ButtonsControl implements Initializable {

    //file chooser for allowing user to load file
    FileChooser fileChooser = new FileChooser();
    ObservableList<TaskListItems> taskListItems = FXCollections.observableArrayList();
    ArrayList<TaskListItems> printingList = new ArrayList<TaskListItems>(taskListItems);
    private static final int limit = 256;
    private final BooleanProperty complete = new SimpleBooleanProperty();

    @FXML
    private TextField taskDescription;

    @FXML
    private DatePicker taskDueDate;

    @FXML
    private ComboBox taskFilter;

    @FXML
    private TableView<TaskListItems> taskList = new TableView<>();

    @FXML
    private TextField taskName;

    @FXML
    private TableColumn<TaskListItems, String> descriptionColumn = new TableColumn<>("Task Description");

    @FXML
    private TableColumn<TaskListItems, Date> dueDateColumn;

    @FXML
    private TableColumn<TaskListItems, String> statusColumn;

    @FXML
    private TableColumn<TaskListItems, String> taskColumn = new TableColumn<>("Task Name");

    //intializer
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("All", "Completed", "Incomplete");
        taskFilter.setItems(list);

        taskDescription.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()){
                if(taskDescription.getText().length() >= limit){
                    taskDescription.setText(taskDescription.getText().substring(0, limit));
                }
            }
        });

        //set new input values to spot on the list; task name, description, date, and if it is completed or not
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //set editable to true so the table can be edited
        taskList.setEditable(true);
        taskColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        taskDueDate.setEditable(true);
    }

    public ObservableList<TaskListItems> getTaskListItems(){
        return taskListItems;
    }

    //addTask button
    @FXML
    void addTask() {
        TaskListItems taskItems = new TaskListItems(taskName.getText(), taskDescription.getText(), taskDueDate.getValue());
        taskListItems = taskList.getItems();
        taskListItems.add(taskItems);
        taskList.setItems(taskListItems);
    }

    //deleteTask button
    @FXML
    void deleteTask() {
        //grab the index of the event in the list (ex 0-100)
        int selectedTask = taskList.getSelectionModel().getSelectedIndex();
        //delete the item at that index
        taskList.getItems().remove(selectedTask);
    }

    //edit the due date when double-clicked
    @FXML
    void editDueDate(CellEditEvent editCell) {
        TaskListItems taskItem = taskList.getSelectionModel().getSelectedItem();
        taskItem.setDueDate(LocalDate.parse(editCell.getNewValue().toString()));
    }

    //edit task description when spot it double-clicked
    @FXML
    void editTaskDescript(CellEditEvent editCell) {
        TaskListItems taskItem = taskList.getSelectionModel().getSelectedItem();
        //replace old description with new description
        taskItem.setTaskDescription(editCell.getNewValue().toString());
    }

    //edit the task name when the spot is double-clicked
    @FXML
    void editTaskName(CellEditEvent editCell) {
       TaskListItems taskItem = taskList.getSelectionModel().getSelectedItem();
       //set old task name to new task name
       taskItem.setTaskName(editCell.getNewValue().toString());
    }

    @FXML
    public void getDate() {
        LocalDate myDate = taskDueDate.getValue();
    }

    public

    @FXML
    void listFilter(ActionEvent event) {
       //String s = taskFilter.getSelectionModel().getSelectedItem().toString();
       ObservableList<TaskListItems> data = FXCollections.observableArrayList();
       for (TaskListItems task : data){
               if(task.getStatus().isSelected()){
                   data.add(task);
               }
               
       }
    }

    @FXML
    void getFile(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                taskList.getItems();
            }

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //save the file
    @FXML
    void saveFile(MouseEvent event) throws IOException {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null){
            exportFile(file);
        }
    }
    //print the data to a txt file
    public void exportFile(File file) {

        //try{


        //}catch (IOException e) {
            //e.printStackTrace();
        //}
    }

    @FXML
    void clearText() {
        taskName.clear();
        taskDescription.clear();
        taskDueDate.setValue(null);
    }
}
