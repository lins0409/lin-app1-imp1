/*
 *  UCF COP3330 Fall 2021 Application Assignment 1 Solution
 *  Copyright 2021 Sue Lin
 */

import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskListItems {
    int counter = 1;
    private String taskName;
    private String taskDescription;
    private int taskID;

    //format the way the date shows up in the date picker to YYYY-MM-DD
    private LocalDate dueDate;

    private CheckBox status;

    public TaskListItems(String taskName, String taskDescription, LocalDate dueDate){
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.status = new CheckBox();
        this.taskID = nextID();
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
        this.status = status;
    }

    int nextID(){
        return counter++;
    }
    int getCounter(){
        return counter;
    }
}
