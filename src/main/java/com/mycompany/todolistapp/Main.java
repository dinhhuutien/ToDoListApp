/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.todolistapp;

/**
 *
 * @author ADMIN
 */


/**
 * Lớp Main chính để khởi động ứng dụng UI (giao diện Swing).
 */
public class Main {
    public static void main(String[] args) {
        // Tạo TaskManager
        TaskManager taskManager = new TaskManager();
        // Tạo và khởi động TodoUI
        new TodoUI(taskManager);
    }
}

