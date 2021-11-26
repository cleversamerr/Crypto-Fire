package com.app.models;

import java.io.Serializable;

public class Report implements Serializable {

    private final String operation, description;

    public Report(String title, String description) {
        this.operation = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                "operation='" + operation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
