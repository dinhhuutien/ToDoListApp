/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistapp;

/**
 *
 * @author ADMIN
 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Lớp quản lý danh sách các task.
 */
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private final String filePath = "tasks.dat";

    public TaskManager() {
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void updateTask(int id, String description, Category category, boolean isCompleted, LocalDate date) {
        tasks.stream()
            .filter(task -> task.getId() == id)
            .findFirst()
            .ifPresent(task -> {
                task.setDescription(description);
                task.setCategory(category);
                task.setCompleted(isCompleted);
                task.setDate(date);
                saveTasks();
            });
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getNextId() {
        return tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
    }

    private void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            tasks = (List<Task>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous tasks found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks.");
            e.printStackTrace();
        }
    }
}
