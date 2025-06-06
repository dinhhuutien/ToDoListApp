/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistapp;

/**
 *
 * @author ADMIN
 */


import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String description;
    private Category category;
    private boolean isCompleted;
    private LocalDate date;  // 

    public Task(int id, String description, Category category) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.isCompleted = false;
        this.date = null;
    }

    // getter & setter
    public int getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "[" + id + "] " + description + " | " + category + " | " + date + " | Completed: " + isCompleted;
    }
}
