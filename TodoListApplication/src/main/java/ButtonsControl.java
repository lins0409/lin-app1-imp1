/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Sue Lin
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ButtonsControl implements Initializable {

    //file chooser for allowing user to load file
    FileChooser fileChooser = new FileChooser();
    //create an observable list for elements being added to the table
    ObservableList<TaskListItems> taskListItems = FXCollections.observableArrayList();
    //create a list of a list of strings for printing the table out to a txt file
    List<List<String>> printingList = new ArrayList<>();
    //for filtering
    FilteredList filteredList = new FilteredList(taskListItems);

    //create a global variable for character limit
    private static final int limit = 256;
    //create a boolean for filtering purposes
    public boolean complete = false;

    @FXML
    private TextField taskDescription;

    @FXML
    private DatePicker taskDueDate;

    @FXML
    private ComboBox<String> taskFilter;

    @FXML
    public TableView<TaskListItems> taskList = new TableView<>();

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

    //initializer
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

        //set new input values to spot on the list; task name, description, date, and a checkbox to mark it as complete
        taskColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("taskDescription"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //set editable to true so the table can be edited
        taskList.setEditable(true);
        taskColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //make the datepicker editable
        taskDueDate.setEditable(true);
        dueDateColumn.setEditable(true);

        //combo box elements
        ObservableList<String> options = FXCollections.observableArrayList(
                "All",
                "Complete",
                "Incomplete"
        );
        //set all the elements in the combobox to the elements in the array
        taskFilter.setItems(options);
        taskList.setItems(taskListItems);
    }

    //addTask button
    @FXML
    String addTask() {
        TaskListItems taskItems = new TaskListItems(taskName.getText(), taskDescription.getText(), taskDueDate.getValue());
        //set object to the tableview
        taskListItems = taskList.getItems();
        //add items from new input
        taskListItems.add(taskItems);
        //set the values on the table
        taskList.setItems(taskListItems);
        //clear the elements in the textbox immediately after the items have been added
        clearText();
        return "added";
    }

    //deleteTask button
    @FXML
    void deleteTask() {
        //grab the index of the event in the list (ex 0-1000)
        int selectedTask = taskList.getSelectionModel().getSelectedIndex();
        //delete the item at that index
        taskList.getItems().remove(selectedTask);
    }

    //edit the due date when double-clicked
    @FXML
    void editDueDate(CellEditEvent editCell) {
        //grab the index of the item on the tables
        TaskListItems taskItem = taskList.getSelectionModel().getSelectedItem();
        //replace the old information with new information that was just put in but casted to local date
        taskItem.setDueDate((LocalDate) editCell.getNewValue());
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

    //to get the date value in order to update the date value
    @FXML
    public void getDate() {
        LocalDate myDate = taskDueDate.getValue();
    }

    //combobox filter that will sort the elements based on if the items are marked as complete or not
    //affects the GUI
    @FXML
    void listFilter() {
        String s = taskFilter.getValue();
        TaskListItems items = null;
        if(items.getStatus().isSelected()){
            complete = true;
        }
        //listener
        taskFilter.setOnAction((t) -> {
            int selectedIndex = taskFilter.getSelectionModel().getSelectedIndex();
            Object selectedItem = taskFilter.getSelectionModel().getSelectedItem();

            if (s.equals("Complete")) taskList.setItems(taskList.getItems());
            //if the selected value in the combobox is Incomplete
            if (selectedItem.equals("Incomplete")){
                taskList.setItems((ObservableList<TaskListItems>) items);
            }
        });
    }

    //to load a file
    @FXML
    void getFile() throws IOException {
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            System.out.println("File found!");
        }
        updateTable(file);
    }
    //used to upload all the elements from the file into the table, and also to check to make sure the file is read in for testing purposes
    String updateTable(File file) {
        try {
            Scanner input = new Scanner(file);
            while ((input.hasNext())) {
                String line = input.next();
                Object[] values_line = line.split(",");
                taskListItems.addAll(new TaskListItems(values_line[0].toString(), values_line[1].toString(), LocalDate.parse((CharSequence) values_line[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "updated table";
    }

    //save the file
    @FXML
    void saveFile() throws IOException {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null){
            exportFile(file);
        }
    }
    //print the data to a txt file, also used to make sure that the file is actually exported when tested
    public String exportFile(File file) throws IOException {
        //write the file to a specified file
        BufferedWriter writer = new BufferedWriter(new FileWriter("docs/todoList.txt"));
        TaskListItems items;
        for (int i = 0; i < taskList.getItems().size(); i++){
            items = taskList.getItems().get(i);
            //add each element on the table to the new list for printing
            printingList.add(new ArrayList<>());

            //format the task name
            String formatName = String.format("%s,", items.getTaskName());
            printingList.get(i).add(formatName);

            //format the description
            String formatDescript = String.format("%s,", items.getTaskDescription());
            printingList.get(i).add(formatDescript);

            String formatDate = String.format("%s,",items.getDueDate());
            //if the date is null, print out nothing but the separate
            if(items.getDueDate() == null){
                formatDate = " ,";
            }
            printingList.get(i).add(formatDate);
            if (items.getStatus().isSelected()) {
                complete = true;
            }
            String formatStatus;
            if (complete){
                formatStatus = String.format("%s\n", "complete");
            }
            else{
                formatStatus = String.format("%s\n", "incomplete");
            }
            printingList.get(i).add(formatStatus);
        }

        for (List<String> strings : printingList) {
            for (String string : strings) {
                writer.write(string);
            }
        }
        writer.close();
        return "printed";
    }

    //I did not create a JUnit test for this function because it simply clears the text that is written in the textboxes which messes with the GUI
     void clearText() {
        taskName.clear();
        taskDescription.clear();
        taskDueDate.setValue(null);
    }

    @FXML
    //button to clear all the items on the table
    void clearTable() {
        taskList.getItems().setAll((TaskListItems) null);
        taskList.getItems().clear();
    }
}