/*
 * File TodoUI.java - The main graphical user interface (GUI) for the To-Do List application.
 * It allows users to add, update, delete, search, refresh, and export tasks based on their date.
 */

package com.mycompany.todolistapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.io.*;

// Import JDateChooser for date selection
import com.toedter.calendar.JDateChooser;

public class TodoUI extends JFrame {
    private final TaskManager taskManager; // Manages the list of tasks
    private final JTextField descriptionField; // Field to enter the task description
    private final JComboBox<Category> categoryComboBox; // Dropdown to select the task category
    private final JCheckBox completedCheckBox; // Checkbox to indicate if the task is completed
    private final JDateChooser dateChooser; // Date picker for selecting the task date
    private final JTextArea taskListArea; // Area to display tasks
    private final JTextField searchField; // Field to search tasks

    public TodoUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        setTitle("To-Do List App 📅");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel for inputting task information
        JPanel inputPanel = new JPanel(new GridLayout(2, 4));
        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(Category.values());
        inputPanel.add(categoryComboBox);

        inputPanel.add(new JLabel("Date :"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        inputPanel.add(dateChooser);

        completedCheckBox = new JCheckBox("Completed");
        inputPanel.add(completedCheckBox);

        add(inputPanel, BorderLayout.NORTH);

        // Area to display the list of tasks
        taskListArea = new JTextArea();
        taskListArea.setEditable(false);
        add(new JScrollPane(taskListArea), BorderLayout.CENTER);

        // Panel for control buttons
        JPanel controlPanel = new JPanel(new FlowLayout());

        // Button to add a new task
        JButton addButton = new JButton("Add");
        addButton.setBackground(Color.GREEN);
        addButton.addActionListener(e -> addTask());
        controlPanel.add(addButton);

        // Button to update an existing task
        JButton updateButton = new JButton("Update");
        updateButton.setBackground(Color.YELLOW);
        updateButton.addActionListener(e -> updateTask());
        controlPanel.add(updateButton);

        // Button to delete a task
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> deleteTask());
        controlPanel.add(deleteButton);

        // Button to refresh the task list
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.CYAN);
        refreshButton.addActionListener(e -> refreshTasks());
        controlPanel.add(refreshButton);

        // Button to export tasks of a specific date
        JButton exportButton = new JButton("Export by Date");
        exportButton.setBackground(Color.MAGENTA);
        exportButton.addActionListener(e -> exportTasksByDate());
        controlPanel.add(exportButton);

        // Field to search tasks by description
        controlPanel.add(new JLabel("Search:"));
        searchField = new JTextField(10);
        searchField.addActionListener(e -> searchTasks());
        controlPanel.add(searchField);

        add(controlPanel, BorderLayout.SOUTH);

        // Load and display tasks initially
        refreshTasks();
        setVisible(true);
    }

    // Method to add a new task
    private void addTask() {
        String desc = descriptionField.getText().trim();
        Category category = (Category) categoryComboBox.getSelectedItem();
        Date date = dateChooser.getDate();

        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!desc.isEmpty()) {
            Task newTask = new Task(taskManager.getNextId(), desc, category);
            newTask.setDate(localDate);
            taskManager.addTask(newTask);
            refreshTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Description cannot be empty.");
        }
    }

    // Method to update an existing task by ID
    private void updateTask() {
        String idStr = JOptionPane.showInputDialog(this, "Enter ID to update:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            String desc = descriptionField.getText().trim();
            Category category = (Category) categoryComboBox.getSelectedItem();
            boolean completed = completedCheckBox.isSelected();
            Date date = dateChooser.getDate();

            if (date == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.");
                return;
            }

            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            taskManager.updateTask(id, desc, category, completed, localDate);
            refreshTasks();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    // Method to delete a task by ID
    private void deleteTask() {
        String idStr = JOptionPane.showInputDialog(this, "Enter ID to delete:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            taskManager.deleteTask(id);
            refreshTasks();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }

    // Method to refresh and display the current list of tasks
    private void refreshTasks() {
        List<Task> tasks = taskManager.getTasks();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task).append("\n");
        }
        taskListArea.setText(sb.toString());
    }

    // Method to search and filter tasks by description
    private void searchTasks() {
        String keyword = searchField.getText().trim().toLowerCase();
        List<Task> tasks = taskManager.getTasks();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword)) {
                sb.append(task).append("\n");
            }
        }
        taskListArea.setText(sb.toString());
    }

    // Method to export tasks to a file for a specific selected date
    private void exportTasksByDate() {
        Date date = dateChooser.getDate();
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date to export.");
            return;
        }

        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Task> tasks = taskManager.getTasks();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            if (selectedDate.equals(task.getDate())) {
                sb.append(task).append("\n");
            }
        }

        if (sb.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks found for " + selectedDate);
            return;
        }

        String fileName = "tasks_" + selectedDate + ".txt";
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(sb.toString());
            JOptionPane.showMessageDialog(this, "Exported tasks to: " + fileName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error writing file: " + e.getMessage());
        }
    }
}
