package com.todoapp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ToDoApp {
    private JFrame frame;
    private JTextField taskInput;
    private JComboBox<String> priorityComboBox;
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private ArrayList<Task> tasks;

    private final String FILE_NAME = "tasks.dat"; // File to save tasks

    public ToDoApp() {
        tasks = new ArrayList<>();
        loadTasksFromFile(); // Load tasks from the file
        setLookAndFeel();    // Apply modern look and feel
        createUI();
    }

    private void setLookAndFeel() {
        try {
            // Use Nimbus LookAndFeel for a modern look
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUI() {
        // Frame Setup
        frame = new JFrame("To-Do List App with Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        
        // Adding background image to the frame
        try {
            frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("background.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        frame.getContentPane().setBackground(new Color(250, 250, 250)); // Light background

        // Panel Setup
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around panel
        panel.setOpaque(false); // Make the panel transparent so the background image is visible

        // Task Input Field
        taskInput = new JTextField(20);
        taskInput.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(taskInput);

        // Priority ComboBox
        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        priorityComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(priorityComboBox);

        // Add Task Button with Icon
        JButton addButton = new JButton("Add Task");
        addButton.setIcon(new ImageIcon("icons/add_icon.png"));  // Add task icon
        styleButton(addButton, new Color(76, 175, 80), Color.WHITE); // Green button
        panel.add(addButton);

        // Task List
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        taskList.setBackground(new Color(240, 240, 240)); // Light grey list background
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Modern border for the task list
        frame.add(scrollPane, BorderLayout.CENTER);

        // Mark as Complete Button with Icon
        JButton completeButton = new JButton("Mark as Complete");
        completeButton.setIcon(new ImageIcon("icons/complete_icon.png")); // Complete icon
        styleButton(completeButton, new Color(33, 150, 243), Color.WHITE); // Blue button
        panel.add(completeButton);

        // Set Priority Button with Icon
        JButton setPriorityButton = new JButton("Set Priority");
        setPriorityButton.setIcon(new ImageIcon("icons/priority_icon.png")); // Priority icon
        styleButton(setPriorityButton, new Color(255, 152, 0), Color.WHITE); // Orange button
        panel.add(setPriorityButton);

        // Delete Task Button with Icon
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.setIcon(new ImageIcon("icons/delete_icon.png"));  // Delete icon
        styleButton(deleteButton, new Color(244, 67, 54), Color.WHITE); // Red button
        panel.add(deleteButton);

        frame.add(panel, BorderLayout.NORTH);

        // Load tasks into taskModel
        for (Task task : tasks) {
            taskModel.addElement(task.toString());
        }

        // Add Task Listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = taskInput.getText();
                String priority = (String) priorityComboBox.getSelectedItem();
                if (!taskName.isEmpty()) {
                    Task newTask = new Task(taskName, priority);
                    tasks.add(newTask);
                    taskModel.addElement(newTask.toString());
                    taskInput.setText("");  // Clear input field after adding
                    saveTasksToFile();  // Save tasks after adding
                }
            }
        });

        // Mark as Complete Listener
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Task selectedTask = tasks.get(selectedIndex);
                    selectedTask.setCompleted(true);  // Mark as completed
                    taskModel.set(selectedIndex, selectedTask.toString());
                    saveTasksToFile();  // Save tasks after updating
                }
            }
        });

        // Set Priority Listener
        setPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Task selectedTask = tasks.get(selectedIndex);
                    String newPriority = (String) priorityComboBox.getSelectedItem();
                    selectedTask.setPriority(newPriority);  // Set new priority
                    taskModel.set(selectedIndex, selectedTask.toString());
                    saveTasksToFile();  // Save tasks after updating
                }
            }
        });

        // Delete Task Listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    tasks.remove(selectedIndex);
                    taskModel.remove(selectedIndex);
                    saveTasksToFile();  // Save tasks after deletion
                }
            }
        });

        frame.setVisible(true);
    }

    // Helper method to style buttons with background and text color
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);  // Remove border when button is clicked
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Use modern font
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Add padding inside button
    }

    // Save tasks to a file
    private void saveTasksToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load tasks from a file
    private void loadTasksFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (ArrayList<Task>) in.readObject();
        } catch (FileNotFoundException e) {
            // No previous file found, so tasks remain empty
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ToDoApp();  // Launch the app
    }
}
