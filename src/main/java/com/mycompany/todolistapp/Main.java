/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.todolistapp;

/**
 *
 * @author ADMIN
 */


/**
 * 
 */
public class Main {
    public static void main(String[] args) {
        // create TaskManager
        TaskManager taskManager = new TaskManager();
        // create and start TodoUI
        new TodoUI(taskManager);
    }
}

