/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Sue Lin
 */

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskListItems {
    //create strings for the task name and description
    private String taskName;
    private String taskDescription;

    //format the way the date shows up in the date picker to YYYY-MM-DD
    private LocalDate dueDate;
    private CheckBox status;
    boolean complete = false;

    //constructor
    public TaskListItems(String taskName, String taskDescription, LocalDate dueDate, boolean complete){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        //every time there is a new task, a new date picker is created
        this.dueDate = dueDate;
        //every time there is a new task, a new check box is made
        this.status = new CheckBox();
        this.complete = complete;
    }

    //get and set for task name
    public String getTaskName() {return taskName;}
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //get and set for task description
    public String getTaskDescription(){return taskDescription;}
    public void setTaskDescription(String taskDescription){
        this.taskDescription = taskDescription;
    }

    //get and set for date
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        //format the way the date shows up in the date picker to YYYY-MM-DD
        String format = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        this.dueDate = dueDate;
    }

    //get and set for checkbox
    public CheckBox getStatus(){
        return status;
    }
    public void setStatus(CheckBox status) {
        //set the bool that the task was completed
        if(status.isSelected())complete = true;
        this.status = status;
    }

    //setters and getters for boolean value
    public boolean isComplete(){ return complete;}
    public void setComplete(boolean complete){
        this.complete = complete;
    }
}