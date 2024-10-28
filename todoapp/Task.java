package com.todoapp;

import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private boolean completed;
    private String priority;

    public Task(String name, String priority) {
        this.name = name;
        this.completed = false;  // Default to not completed
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "[" + priority + "] " + (completed ? "[Completed] " : "") + name;
    }
}
