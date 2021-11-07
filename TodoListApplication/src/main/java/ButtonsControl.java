/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Sue Lin
 */
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
    List<List<String>> printingList = new ArrayList<>();
    ArrayList<String> todoFile;

    private static final int limit = 256;
    private final boolean complete = false;

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
        //this sets it so that the limit of characters in the description box is 256
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
        taskFilter.setCellFactory(new PropertyValueFactory<TaskListItems, ComboBox>("filter"));

        //set editable to true so the table can be edited
        taskList.setEditable(true);
        taskColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //make the datepicker editable
        taskDueDate.setEditable(true);
        dueDateColumn.setEditable(true);

    }

    //this is so that the combo list will display the items that I want it to
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
        taskItem.setDueDate(new DatePicker().getValue());
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

    //combobox filter that will sort the elements based on if the items are marked as complete or not
    @FXML
    void listFilter(ActionEvent event) {
        String s = taskFilter.getSelectionModel().getSelectedItem().toString();
        ObservableList<TaskListItems> data = FXCollections.observableArrayList();

    }

    @FXML
    void getFile(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            //have the scanner open the input file
            BufferedReader br = new BufferedReader(new FileReader((new File(" docs/todoList.txt"))));
            String line;
            String[] array;
            //while the input file does not reach the end of the file
            while ((line = br.readLine()) != null){
                //split the input file at the comma
                array = line.split(",");
                //upload the elements from the text file to the table
                taskList.getItems().add(new TaskListItems(array[0], array[1], LocalDate.parse(array[2])));
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
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
    public void exportFile(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("docs/todoList.txt"));
        TaskListItems items = new TaskListItems(taskName.getText(), taskDescription.getText(), taskDueDate.getValue());
        for (int i = 0; i < taskList.getItems().size(); i++){
            items = taskList.getItems().get(i);
            printingList.add(new ArrayList<>());
            String formatName = String.format("%s,", items.getTaskName());
            printingList.get(i).add(formatName);
            String formatDescript = String.format("%s,", items.getTaskDescription());
            printingList.get(i).add(formatDescript);
            String formatDate = String.format("%s\n",items.getDueDate().toString());
            printingList.get(i).add(formatDate);
        }

        for (int i = 0; i < printingList.size(); i++){
            for (int j = 0; j < printingList.get(i).size(); j++) {
                writer.write(printingList.get(i).get(j));
            }
        }
        writer.close();
    }

    @FXML
    void clearText() {
        taskName.clear();
        taskDescription.clear();
        taskDueDate.setValue(null);
    }
}