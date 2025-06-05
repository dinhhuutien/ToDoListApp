/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistapp;

/**
 *
 * @author ADMIN
 */
/*
 * File TodoUI.java - Giao diện Swing hoàn thiện với Date Picker đẹp hơn
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.io.*;

// Import JDateChooser
import com.toedter.calendar.JDateChooser;

public class TodoUI extends JFrame {
    private final TaskManager taskManager;
    private final JTextField descriptionField;
    private final JComboBox<Category> categoryComboBox;
    private final JCheckBox completedCheckBox;
    private final JDateChooser dateChooser; // Sử dụng DateChooser thay cho JSpinner
    private final JTextArea taskListArea;
    private final JTextField searchField;

    public TodoUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        setTitle("To-Do List App 📅");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel nhập thông tin task
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

        // Khu vực hiển thị danh sách task
        taskListArea = new JTextArea();
        taskListArea.setEditable(false);
        add(new JScrollPane(taskListArea), BorderLayout.CENTER);

        // Panel điều khiển
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add");
        addButton.setBackground(Color.GREEN);
        addButton.addActionListener(e -> addTask());
        controlPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(Color.YELLOW);
        updateButton.addActionListener(e -> updateTask());
        controlPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> deleteTask());
        controlPanel.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.CYAN);
        refreshButton.addActionListener(e -> refreshTasks());
        controlPanel.add(refreshButton);

        JButton exportButton = new JButton("Export by Date");
        exportButton.setBackground(Color.MAGENTA);
        exportButton.addActionListener(e -> exportTasksByDate());
        controlPanel.add(exportButton);

        controlPanel.add(new JLabel("Search:"));
        searchField = new JTextField(10);
        searchField.addActionListener(e -> searchTasks());
        controlPanel.add(searchField);

        add(controlPanel, BorderLayout.SOUTH);

        refreshTasks();
        setVisible(true);
    }

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

    private void refreshTasks() {
        List<Task> tasks = taskManager.getTasks();
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task).append("\n");
        }
        taskListArea.setText(sb.toString());
    }

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

    // Xuất file công việc của ngày được chọn
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

