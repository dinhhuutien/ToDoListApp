package com.mycompany.todolistapp;

// Import necessary libraries
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * The TaskManager class - responsible for managing the list of tasks.
 */
public class TaskManager {
    // List to store all tasks
    private List<Task> tasks = new ArrayList<>();

    // File path to save/load tasks (binary format)
    private final String filePath = "tasks.dat";

    /**
     * Constructor - loads tasks from file when the app starts
     */
    public TaskManager() {
        loadTasks(); // Load tasks from file (if any)
    }

    /**
     * Adds a new task to the list
     * @param task the new Task object
     */
    public void addTask(Task task) {
        tasks.add(task); // Add task to the list
        saveTasks(); // Save updated list to file
    }

    /**
     * Updates the information of a specific task
     * @param id task ID to update
     * @param description new description
     * @param category new category
     * @param isCompleted new completion status
     * @param date new date
     */
    public void updateTask(int id, String description, Category category, boolean isCompleted, LocalDate date) {
        tasks.stream() // Iterate through tasks
            .filter(task -> task.getId() == id) // Find task with the matching ID
            .findFirst() // Get the first task found
            .ifPresent(task -> { // If found, update its information
                task.setDescription(description);
                task.setCategory(category);
                task.setCompleted(isCompleted);
                task.setDate(date);
                saveTasks(); // Save updated list
            });
    }

    /**
     * Deletes a task by its ID
     * @param id ID of the task to delete
     */
    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id); // Remove task with the given ID
        saveTasks(); // Save updated list
    }

    /**
     * Returns the list of all tasks
     * @return list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Generates the next available ID for a new task
     * @return next ID
     */
    public int getNextId() {
        return tasks.isEmpty() ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
    }

    /**
     * Saves the list of tasks to the file
     */
    private void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(tasks); // Write list to file
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of tasks from the file
     */
    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            tasks = (List<Task>) in.readObject(); // Read file and cast to List<Task>
        } catch (FileNotFoundException e) {
            System.out.println("No previous tasks found. Starting fresh."); // If file does not exist
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks.");
            e.printStackTrace();
        }
    }
}
