package com.mycompany.taskmanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TaskManager {
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private Connection con;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManager::new);
    }

    public TaskManager() {
        connectToDatabase();
        initializeGUI();
        loadTasksFromDatabase();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/taskmanager", "root", "");
            System.out.println("Database Connected Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeGUI() {
        frame = new JFrame("Task Manager");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane taskListScrollPane = new JScrollPane(taskList);

        JButton addTaskButton = new JButton("Add Task");
        JButton updateTaskButton = new JButton("Update Task");
        JButton deleteTaskButton = new JButton("Delete Task");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTaskButton);
        buttonPanel.add(updateTaskButton);
        buttonPanel.add(deleteTaskButton);

        frame.add(taskListScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addTaskButton.addActionListener(e -> showAddTaskDialog());
        updateTaskButton.addActionListener(e -> showUpdateTaskDialog());
        deleteTaskButton.addActionListener(e -> deleteSelectedTask());

        frame.setVisible(true);
    }

    private void loadTasksFromDatabase() {
        taskListModel.clear();
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM tasks")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                taskListModel.addElement("ID: " + id + " | " + name + " | " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddTaskDialog() {
        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        
        if (JOptionPane.showConfirmDialog(frame, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            addTaskToDatabase(nameField.getText().trim(), descField.getText().trim());
        }
    }

    private void addTaskToDatabase(String name, String description) {
        if (name.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO tasks (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Task Added Successfully!");
            loadTasksFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateTaskDialog() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Select a task to update.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedTask = taskListModel.getElementAt(selectedIndex);
        int taskId = Integer.parseInt(selectedTask.split(" ")[1]);
        
        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(15);
        JPanel panel = new JPanel();
        panel.add(new JLabel("New Name:"));
        panel.add(nameField);
        panel.add(new JLabel("New Description:"));
        panel.add(descField);

        if (JOptionPane.showConfirmDialog(frame, panel, "Update Task", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            updateTaskInDatabase(taskId, nameField.getText().trim(), descField.getText().trim());
        }
    }

    private void updateTaskInDatabase(int id, String name, String description) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE tasks SET name = ?, description = ? WHERE id = ?")) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Task Updated Successfully!");
            loadTasksFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Select a task to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedTask = taskListModel.getElementAt(selectedIndex);
        int taskId = Integer.parseInt(selectedTask.split(" ")[1]);
        
        if (JOptionPane.showConfirmDialog(frame, "Are you sure?", "Confirm Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            deleteTaskFromDatabase(taskId);
        }
    }

    private void deleteTaskFromDatabase(int id) {
        try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Task Deleted Successfully!");
            loadTasksFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
